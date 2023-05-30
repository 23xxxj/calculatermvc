import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Model model = new Model();
        CalcView view = new CalcView();
        Controller controller = new Controller(view, model);

        view.addCalculateListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.calculate();
            }
        });

        view.addSaveResultListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.saveResult();
            }
        });

        view.addSaveHistoryListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.saveHistory(null);
            }
        });

    }
}


