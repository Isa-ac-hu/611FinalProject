import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class DepositWithdrawPage extends JFrame implements Subject {

    private JComboBox<BankAccount> accountDropdown;
    private JComboBox<String> currencyDropdown;
    private JTextField amountField;
    private JButton depositButton;
    private JButton withdrawButton;
    private JButton backButton; // Back button to navigate back
    private JLabel statusLabel;
    private Customer customer;
    private List<Observer> observers = new ArrayList<>();
    private ATM atm;

    public DepositWithdrawPage(User user, ATM atm) {
        this.customer = (Customer) user;
        this.atm = atm;
        registerObserver(atm.getHomePage());

        setTitle("Deposit/Withdraw Page");
        setSize(450, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Components
        accountDropdown = new JComboBox<>();
        currencyDropdown = new JComboBox<>(new String[]{"USD", "EUR", "JPY"}); // Currency options
        amountField = new JTextField(10);
        depositButton = new JButton("Deposit");
        withdrawButton = new JButton("Withdraw");
        backButton = new JButton("Back");
        statusLabel = new JLabel("");

        // Populate account dropdown
        for (BankAccount account : customer.getAccounts()) {
            accountDropdown.addItem(account);
        }

        // Layout
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(new JLabel("Select Account:"));
        inputPanel.add(accountDropdown);
        inputPanel.add(new JLabel("Currency:"));
        inputPanel.add(currencyDropdown);
        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(backButton);

        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(statusLabel, BorderLayout.NORTH);

        // Event Listeners
        depositButton.addActionListener(this::handleDeposit);
        withdrawButton.addActionListener(this::handleWithdraw);
        backButton.addActionListener(e -> atm.goBack());
    }

    private void handleDeposit(ActionEvent e) {
        performTransaction(true);
    }

    private void handleWithdraw(ActionEvent e) {
        performTransaction(false);
    }

    private void performTransaction(boolean isDeposit) {
        try {
            BankAccount selectedAccount = (BankAccount) accountDropdown.getSelectedItem();
            String selectedCurrency = (String) currencyDropdown.getSelectedItem();
            double amount = Double.parseDouble(amountField.getText());

            if (amount <= 0) {
                statusLabel.setText("Amount must be positive.");
                return;
            }

            // Convert amount if necessary
            double convertedAmount = convertCurrency(amount, selectedCurrency, isDeposit);
            double fee = convertedAmount * 0.02; // 2% fee on the initial deposit
            double finalConverted = convertedAmount - fee;

            if (!isDeposit && selectedAccount.getBalance() < convertedAmount) {
                statusLabel.setText("Insufficient balance.");
                return;
            }

            if (isDeposit) {
                selectedAccount.setBalance(selectedAccount.getBalance() + finalConverted);
            } else {
                selectedAccount.setBalance(selectedAccount.getBalance() - finalConverted);
            }

            statusLabel.setText((isDeposit ? "Deposit" : "Withdrawal") + " successful! Fee charged: $" + String.format("%.2f", fee) + ". New balance: $" + String.format("%.2f", selectedAccount.getBalance()));
        } catch (NumberFormatException ex) {
            statusLabel.setText("Invalid amount.");
        }
    }

    // Function to convert currency amounts based on selected currency
    private double convertCurrency(double amount, String currency, boolean isDeposit) {
        CurrencyConversionStrategy strategy;
        switch (currency) {
            case "EUR":
                strategy = new UsdToEurConversion();
                break;
            case "JPY":
                strategy = new UsdToJpyConversion();
                break;
            default:
                return amount; // No conversion needed if USD
        }
        return isDeposit ? strategy.convertTo(amount) : strategy.convertBack(amount);
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
        observers.forEach(Observer::update);
    }
}