import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CalcView {

    private JButton btnCalculate, btnSaveResult, btnSaveHistory;
    private JTextField txtExpression;
    private JLabel lblResult;

    public CalcView() {
        initUI();
    }

    private void initUI() {

        JFrame frame = new JFrame("Calculator");

        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);

        txtExpression = new JTextField();
        lblResult = new JLabel("Result");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Expression: "));
        panel.add(txtExpression);
        panel.add(lblResult);

        btnCalculate = new JButton("Calculate");
        btnSaveResult = new JButton("Save Result");
        btnSaveHistory = new JButton("Save History");

        JPanel controls = new JPanel(new GridLayout(1, 3, 5, 5));
        controls.add(btnCalculate);
        controls.add(btnSaveResult);
        controls.add(btnSaveHistory);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(controls, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    public String getEquation() {
        return txtExpression.getText().trim();
    }

    public void showResult(String result) {
        lblResult.setText("Result: " + result);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    public void addCalculateListener(ActionListener listener) {
        btnCalculate.addActionListener(listener);
    }

    public void addSaveResultListener(ActionListener listener) {
        btnSaveResult.addActionListener(listener);}

    public void addSaveHistoryListener(ActionListener listener) {
        btnSaveHistory.addActionListener(listener);
    }

}
