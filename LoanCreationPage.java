import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoanCreationPage extends JFrame implements Subject{

    private ATM atm;
    private Customer customer;
    private JTextField loanAmountField;
    private JTextField collateralNameField;
    private JTextField collateralAmountField;

    private List<Observer> observers = new ArrayList<>();
    private JButton backButton;

    public LoanCreationPage(Customer customer, ATM atm) {
        registerObserver(atm.getHomePage());
        this.atm = atm;
        this.customer = customer;
        setTitle("Loan Creation Interface");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4);

        // Loan Amount
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Loan Amount:"), gbc);
        gbc.gridx = 1;
        loanAmountField = new JTextField(10);
        panel.add(loanAmountField, gbc);

        // Collateral Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Collateral Name:"), gbc);
        gbc.gridx = 1;
        collateralNameField = new JTextField(10);
        panel.add(collateralNameField, gbc);

        // Collateral Amount
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Collateral Amount:"), gbc);
        gbc.gridx = 1;
        collateralAmountField = new JTextField(10);
        panel.add(collateralAmountField, gbc);

        // Buttons
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 3;
        JButton createLoanButton = new JButton("Create Loan");
        createLoanButton.addActionListener(new CreateLoanAction());
        panel.add(createLoanButton, gbc);

        gbc.gridy = 4;
        backButton = new JButton("Back");
        backButton.addActionListener(e -> atm.goBack());
        panel.add(backButton, gbc);

        add(panel);
        setVisible(true);
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
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
                    Loan loan = new Loan(Loan.getCurrentLoanID(), customer.getUserID(), loanAmount, loanAmount, new Collateral(collateralName, collateralAmount), Bank.getTime());
                    LoginPage.getLoans().add(loan);
                    customer.getLoans().add(loan);
                    JOptionPane.showMessageDialog(null, "Loan created: " + loan);
                    writeLoanToFile(loan, "/Users/abdelazimlokma/Desktop/Desktop/Uni/Spring 24/CS 611 OOP/ Monsters and Heroes/BankingSystemV2/src/Loans.txt");
                    notifyObservers();
                }
                atm.goBack();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter valid numeric values for loan amount and collateral amount.");
            }
        }
    }

    public void writeLoanToFile(Loan loan, String filePath) {
        // Use FileWriter to append data to the end of the file in append mode
        try (FileWriter fileWriter = new FileWriter(filePath, true)) {
            // Format the loan data as a comma-separated string
            String loanData = String.format("%d,%d,%.2f,%.2f,%s,%.2f,%d\n",
                    loan.getLoanID(),
                    loan.getCustomerID(),
                    loan.getInitialLoanAmount(),
                    loan.getDebtAmount(),
                    loan.getCollateral().getName(),
                    loan.getCollateral().getValue(),
                    loan.getLoanStartDate());

            // Append the loan data to the file
            fileWriter.write(loanData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}