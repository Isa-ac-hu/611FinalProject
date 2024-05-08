import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class LoanPaymentPage extends JFrame implements Subject {

    private JComboBox<Loan> loanDropdown;
    private JTextField paymentAmountField;
    private JButton payButton;
    private JLabel statusLabel;
    private JButton backButton;
    private Customer customer;
    private ATM atm;

    List<Observer> observers = new ArrayList<>();

    // Constructor
    public LoanPaymentPage(User user, ATM atm) {
        observers.add(atm.getHomePage());
        this.customer = (Customer)user;
        this.atm = atm;
        setTitle("Loan Payment Interface");
        setSize(530, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create components
        loanDropdown = new JComboBox<>();
        paymentAmountField = new JTextField(10);
        payButton = new JButton("Pay");
        backButton = new JButton("Back");  // Proper initialization
        statusLabel = new JLabel("");

        // Populate loan dropdown with user's loans
        for (Loan loan : customer.getLoans()) {
            loanDropdown.addItem(loan);
        }

        // Panel for loan dropdown and payment amount input
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(new JLabel("Select Loan:"));
        inputPanel.add(loanDropdown);
        inputPanel.add(new JLabel("Payment Amount:"));
        inputPanel.add(paymentAmountField);
        inputPanel.add(payButton);
        inputPanel.add(backButton);  // Now correctly adding the initialized button

        // Add components to the frame
        add(inputPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        // Add action listener to the pay button
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLoanPayment();
            }
        });
        backButton.addActionListener(e -> atm.goBack());  // Add action to go back
    }

    // Handle loan payment
    private void handleLoanPayment() {
        // Get the selected loan and payment amount
        Loan selectedLoan = (Loan) loanDropdown.getSelectedItem();
        String paymentAmountText = paymentAmountField.getText();

        // Validate payment amount input
        double paymentAmount;
        try {
            paymentAmount = Double.parseDouble(paymentAmountText);
            if(paymentAmount <= 0){
                JOptionPane.showMessageDialog(null, "Must input positive number!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid payment amount.");
            return;
        }

        // Calculate the user's net account value
        double netAccountValue = 0;
        for (BankAccount account : customer.getAccounts()) {
            netAccountValue += account.getBalance();
        }

        if(paymentAmount > 0){
            // Check if user can afford the payment
            if (netAccountValue >= paymentAmount) {
                // Drain money from accounts and pay off the loan
                double remainingAmount = paymentAmount;

                for (BankAccount account : customer.getAccounts()) {
                    double accountBalance = account.getBalance();
                    if (remainingAmount > 0) {
                        double amountToDrain = Math.min(remainingAmount, accountBalance);
                        account.setBalance(accountBalance - amountToDrain);
                        remainingAmount -= amountToDrain;
                    }
                }
                if(selectedLoan.getDebtAmount() == paymentAmount){
                    selectedLoan.setDebtAmount(selectedLoan.getDebtAmount() - paymentAmount);
                    JOptionPane.showMessageDialog(null, "Payment successful! Loan cleared!");
                    customer.getLoans().remove(selectedLoan);
                    atm.goBack();
                }
                else if(selectedLoan.getDebtAmount() >= paymentAmount) {
                    // Reduce the loan debt amount
                    selectedLoan.setDebtAmount(selectedLoan.getDebtAmount() - paymentAmount);

                    // Update status label
                    JOptionPane.showMessageDialog(null, "Payment successful! Remaining debt: " +
                            selectedLoan.getDebtAmount());
                    atm.goBack();
                }
                else{
                    JOptionPane.showMessageDialog(null, "Payment amount cannot exceed debt amount!");
                }
            } else {
                // Update status label with an error message
                JOptionPane.showMessageDialog(null, "Insufficient funds in accounts.");
            }

            notifyObservers();
        }

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
        for(Observer o: observers){
            o.update();
        }

    }
}