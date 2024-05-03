import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoanCreationPage extends JFrame {
    private JTextField loanAmountField;
    private JTextField collateralNameField;
    private JTextField collateralAmountField;

    public LoanCreationPage() {
        setTitle("Loan Creation Interface");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel loanAmountLabel = new JLabel("Loan Amount:");
        loanAmountField = new JTextField(10);
        JLabel collateralNameLabel = new JLabel("Collateral Name:");
        collateralNameField = new JTextField(10);
        JLabel collateralAmountLabel = new JLabel("Collateral Amount:");
        collateralAmountField = new JTextField(10);
        JButton createLoanButton = new JButton("Create Loan");

        createLoanButton.addActionListener(new CreateLoanAction());

        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        JPanel panel = new JPanel(gridBagLayout);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(loanAmountLabel, constraints);
        constraints.gridx = 1;
        panel.add(loanAmountField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(collateralNameLabel, constraints);
        constraints.gridx = 1;
        panel.add(collateralNameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(collateralAmountLabel, constraints);
        constraints.gridx = 1;
        panel.add(collateralAmountField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(createLoanButton, constraints);

        add(panel);
        setVisible(true);
    }

    private class CreateLoanAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                double loanAmount = Double.parseDouble(loanAmountField.getText());
                String collateralName = collateralNameField.getText();
                double collateralAmount = Double.parseDouble(collateralAmountField.getText());

                if(Input.queryInt(loanAmountField.getText(), 0,  Integer.MAX_VALUE) == false){
                    JOptionPane.showMessageDialog(null, "Loan Amount exceeds valid range");
                }
                else if(Input.queryInt(collateralAmountField.getText(), 0,  Integer.MAX_VALUE) == false){
                    JOptionPane.showMessageDialog(null, "Collateral Amount exceeds valid range");
                }
                else if(loanAmount > collateralAmount){
                    JOptionPane.showMessageDialog(null, "Inadequate value for collateral");
                }
                else{
                    Loan loan = new Loan(loanAmount, new Collateral(collateralName, collateralAmount), LoginPage.getCustomer());
                    LoginPage.getCustomer().loans.add(loan);
                    JOptionPane.showMessageDialog(null, "Loan created: " + loan);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter valid numeric values for loan amount and collateral amount.");
            }
        }
    }
}