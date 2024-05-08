import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class BankAccCreationPage extends JFrame implements Subject{
    private Customer customer; // Assuming user ID is provided when creating an instance of this page
    private JComboBox<BankAccTypes> accountTypeComboBox;
    private JTextField balanceInput;
    private JButton createAccountButton;
    private HomePage homePage;

    private JButton backButton;

    List<Observer> observers = new ArrayList<>();

    private ATM atm;


    public BankAccCreationPage(Customer customer, ATM atm) {
        this.customer = customer;
        this.atm = atm;
        homePage = atm.getHomePage();
        initializeUI();
    }

    private void initializeUI() {
        observers.add(homePage);
        setTitle("Create Bank Account");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        // Account Type Selection
        add(new JLabel("Select Account Type:"));
        accountTypeComboBox = new JComboBox<>(BankAccTypes.values());
        add(accountTypeComboBox);

        // Balance Input
        add(new JLabel("Initial Balance:"));
        balanceInput = new JTextField();
        add(balanceInput);

        // Create Account Button
        createAccountButton = new JButton("Create Account");
        createAccountButton.addActionListener(this::createAccountAction);
        add(createAccountButton);

        backButton = new JButton("Back");
        backButton.addActionListener(e -> atm.goBack());
        add(backButton);

        setVisible(true);
    }

    private void createAccountAction(ActionEvent e) {
        // Get selected account type
        BankAccTypes selectedType = (BankAccTypes) accountTypeComboBox.getSelectedItem();

        // Validate and parse the balance input
        String balanceStr = balanceInput.getText();
        if (!Input.queryInt(balanceStr, 0, Integer.MAX_VALUE)) {
            JOptionPane.showMessageDialog(this, "Invalid balance. Please enter a valid number greater than 0.");
            return;
        }

        double balance = Double.parseDouble(balanceStr);

        // Create the account based on the selected type
        BankAccount newAccount = null;
        switch (selectedType) {
            case SAVINGS:
                newAccount = new SavingsAccount(customer.getUserID(), balance, null);
                break;
            case CHECKING:
                newAccount = new CheckingAccount(customer.getUserID(), balance, null);
                break;
        }

        if (newAccount != null) {
            JOptionPane.showMessageDialog(this, "Account created successfully!");
            customer.getAccounts().add(newAccount);
            LoginPage.getAccounts().add(newAccount);
            notifyObservers();
            atm.goBack();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create account. Please try again.");
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
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
