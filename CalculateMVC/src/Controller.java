import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller {

    private CalcView view;
    private Model model;
    private List<String[]> history = new ArrayList<>(); //хранение введенных уравнений и результатов

    public Controller(CalcView view, Model model) {
        this.view = view;
        this.model = model;
    }

    public void calculate() {

        String[] equation = view.getEquation().split("\\s+");
        int len = equation.length;

        // подсчет выражений в скобках
        while (containsBrackets(equation)) {
            for (int i = 0; i < equation.length; i++) {
                if (equation[i].equals("(")) {
                    int start = i + 1; // начало выражения в скобках
                    int end = findClosingBracket(equation, start); // конец выражения в скобках
                    String[] subExpression = Arrays.copyOfRange(equation, start, end);
                    double result = calculateExpression(subExpression); // рекурсивный вызов метода для подсчета выражения в скобках
                    equation[i] = String.valueOf(result);
                    for (int j = i + 1; j <= end; j++) {
                        equation[j] = "";
                    }
                }
            }
            equation = Arrays.stream(equation).filter(e -> !e.equals("")).toArray(String[]::new); // удаляем пустые элементы массива
        }

        // вычисление оставшейся части выражения
        double res = 0;

        try {
            res = Double.parseDouble(equation[0]);

            for (int i = 1; i < equation.length; i += 2) {
                double operand = Double.parseDouble(equation[i + 1]);
                switch (equation[i]) {
                    case "+":
                        res += operand;
                        break;
                    case "-":
                        res -= operand;
                        break;
                    case "*":
                        res *= operand;
                        break;
                    case "/":
                        res /= operand;
                        break;
                    case "%":
                        res %= operand;
                        break;
                    case "//":
                        res = Math.floor(res / operand);
                        break;
                    case "^":
                    case "pow":
                        res = Math.pow(res, operand);
                        break;
                    default:
                        view.showError("Invalid operator: " + equation[i]);
                        return;
                }
            }
            model.setExpression(view.getEquation()); //сохраняем выражение в модели
            model.setResult(String.valueOf(res)); //сохраняем результат в модели
            history.add(new String[] {view.getEquation(), String.valueOf(res)}); //добавляем историю
            view.showResult(String.valueOf(res));

        } catch (NumberFormatException e) {
            view.showError("Invalid numbers in the equation");
            return;
        }




    }

    // метод для поиска закрывающей скобки
    private int findClosingBracket(String[] arr, int start) {
        int cnt = 1; // счетчик для подсчета количества открывающих и закрывающих скобок
        for (int i = start; i < arr.length; i++) {
            if (arr[i].equals("(")) {
                cnt++;
            } else if (arr[i].equals(")")) {
                cnt--;
            }
            if (cnt == 0) {
                return i;
            }
        }
        return -1; // закрывающая скобка не найдена
    }

    // метод для проверки наличия скобок в выражении
    private boolean containsBrackets(String[] arr) {
        for (String e : arr) {
            if (e.equals("(")) {
                return true;
            }
        }
        return false;
    }

    // рекурсивный метод для подсчета значения выражения
    private double calculateExpression(String[] equation) {
        if (equation.length == 1) {
            return Double.parseDouble(equation[0]);
        }
        double res = 0;
        try {
            res = Double.parseDouble(equation[0]);
            for (int i = 1; i < equation.length; i += 2) {
                double operand;
                if (equation[i + 1].equals("(")) {
                    int start = i + 2;
                    int end = findClosingBracket(equation, start);
                    String[] subExpression = Arrays.copyOfRange(equation, start, end);
                    operand = calculateExpression(subExpression);
                } else {
                    operand = Double.parseDouble(equation[i + 1]);
                }
                switch (equation[i]) {
                    case "+":
                        res += operand;
                        break;
                    case "-":
                        res -= operand;
                        break;
                    case "*":
                        res *= operand;
                        break;
                    case "/":
                        res /= operand;
                        break;
                    case "%":
                        res %= operand;
                        break;
                    case "//":
                        res = Math.floor(res / operand);
                        break;
                    case "^":
                    case "pow":
                        res = Math.pow(res, operand);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid operator: " + equation[i]);
                }
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numbers in the equation");
        }
        return res;
    }

    public void saveHistory(String path) {
        if (history.isEmpty()) {
            view.showError("History is empty"); //сообщаем об ошибке отсутствия истории
            return;
        }

        String fileName;

        if (path != null) {
            File file = new File(path);
            if (file.isDirectory()) {
                fileName = "log.log"; //если указан путь, но не указано имя, сохраняем с именем log.log
                path = file.getAbsolutePath() + File.separator + fileName; //формируем полный путь
            } else if (file.isFile()) {
                fileName = file.getName(); //если указан полный путь с именем файла, берем имя файла
            } else {
                fileName = path; //если указан только имя файла, сохраняем втекущей директории
                path = fileName;
            }
        } else {
            fileName = "history.log"; //если не указан путь, сохраняем с именем history.log в директории где программа была запущена
            path = fileName;
        }

        File file = new File(path);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String[] entry : history) {
                writer.write(entry[0] + " = " + entry[1] + System.lineSeparator());
            }

            view.showMessage("History saved to file: " + file.getAbsolutePath()); //выводим сообщение об успешном сохранении

        } catch (IOException e) {
            view.showError("Failed to save the history: " + e.getMessage()); //сообщаем об ошибке сохранения файла
        }
    }

    public void saveResult() {

        if (model.getExpression().isEmpty()) {
            view.showError("No calculations to save"); //сообщаем об ошибке сохранения результата
            return;
        }

        //формируем путь и имя файла
        String fileName = model.getExpression().replaceAll("[^\\w\\.\\-]+", "_"); //заменяем неразрешенные символы в имени файла
        String directory = "/";
        File file = null;

        try {
            file = new File(directory, fileName + ".log"); //формируем объект для файла
            file.createNewFile(); //создаем новый файл

            try (
                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(model.getExpression() + " = " + model.getResult()); //записываем уравнение и результат
            }

        } catch (IOException e) {
            view.showError(e.getMessage()); //сообщаем об ошибке сохранения файла
        }

        if (file != null) {
            view.showMessage("Result saved to file: " + file.getAbsolutePath()); //выводим сообщение об успешном сохранении
        }
    }

}