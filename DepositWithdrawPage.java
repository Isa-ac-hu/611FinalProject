import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class DepositWithdrawPage extends JFrame implements Subject {

    private JComboBox<BankAccount> accountDropdown;
    private JTextField amountField;
    private JButton depositButton;
    private JButton withdrawButton;
    private JButton backButton; // Add the back button
    private JLabel statusLabel;
    private User user;
    private Customer customer;
    private List<Observer> observers = new ArrayList<>();
    private ATM atm; // Reference to ATM for navigation



    // Constructor
    public DepositWithdrawPage(User user,ATM atm) {
        registerObserver(atm.getHomePage());
        this.user = user;
        this.customer = (Customer)user;

        setTitle("Deposit/Withdraw Page");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create components
        accountDropdown = new JComboBox<>();
        amountField = new JTextField(10);
        depositButton = new JButton("Deposit");
        withdrawButton = new JButton("Withdraw");
        statusLabel = new JLabel("");
        backButton =  new JButton("Back");

        // Populate account dropdown with user's accounts
        for (BankAccount account : customer.getAccounts()) {
            accountDropdown.addItem(account);
        }

        // Panel for account dropdown and amount input
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(new JLabel("Select Account:"));
        inputPanel.add(accountDropdown);
        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(backButton);

        // Add components to the frame
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(statusLabel, BorderLayout.NORTH);

        backButton.addActionListener(e -> atm.goBack());

        // Add action listeners to buttons
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDeposit();
            }
        });

        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleWithdraw();
            }
        });
    }

    // Handle deposit action
    private void handleDeposit() {
        // Get the selected account and the deposit amount
        BankAccount selectedAccount = (BankAccount) accountDropdown.getSelectedItem();
        String amountText = amountField.getText();

        // Validate deposit amount input
        double amount;
        try {
            amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                statusLabel.setText("Amount must be positive.");
                return;
            }
        } catch (NumberFormatException ex) {
            statusLabel.setText("Invalid amount.");
            return;
        }

        // Perform the deposit
        selectedAccount.setBalance(selectedAccount.getBalance() + amount);
        statusLabel.setText("Deposit successful! New balance: " + selectedAccount.getBalance());
    }

    // Handle withdraw action
    private void handleWithdraw() {
        // Get the selected account and the withdraw amount
        BankAccount selectedAccount = (BankAccount) accountDropdown.getSelectedItem();
        String amountText = amountField.getText();

        // Validate withdraw amount input
        double amount;
        try {
            amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                statusLabel.setText("Amount must be positive.");
                return;
            }
        } catch (NumberFormatException ex) {
            statusLabel.setText("Invalid amount.");
            return;
        }

        // Check if the account has enough balance
        if (selectedAccount.getBalance() < amount) {
            statusLabel.setText("Insufficient balance.");
            return;
        }

        // Perform the withdrawal
        selectedAccount.setBalance(selectedAccount.getBalance() - amount);
        statusLabel.setText("Withdrawal successful! New balance: " + selectedAccount.getBalance());
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
