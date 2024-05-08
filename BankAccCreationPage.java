import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;
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
        double initialBalance = Double.parseDouble(balanceStr);
        double fee = initialBalance * 0.02; // 2% fee on the initial deposit
        double finalBalance = initialBalance - fee;

        // Create the account based on the selected type
        BankAccount newAccount = null;
        switch (selectedType) {
            case SAVINGS:
                newAccount = new SavingsAccount(customer.getUserID(), finalBalance, null);
                break;
            case CHECKING:
                newAccount = new CheckingAccount(customer.getUserID(), finalBalance, null);
                break;
        }

        if (newAccount != null) {
            writeAccountToFile(newAccount);
            JOptionPane.showMessageDialog(this, "Account created successfully! Fee charged: $" + String.format("%.2f", fee));
            customer.getAccounts().add(newAccount);
            LoginPage.getAccounts().add(newAccount);
            notifyObservers();
            atm.goBack();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create account. Please try again.");
        }
    }

    public void writeAccountToFile(BankAccount account){
        String filePath = "/Users/abdelazimlokma/Desktop/Desktop/Uni/Spring 24/CS 611 OOP/Final Project/repo/Untitled/Accounts.txt";
        int userID = account.getUserID();
        String type = "";
        if(account instanceof CheckingAccount) {
            type = "Checking";
        }
        if(account instanceof SavingsAccount) {
            type = "Saving";
        }
        // Assuming type can be retrieved as a string
        double balance = account.getBalance();
        int accountID = account.getAccountID();

        // Format the account data as a string (e.g., "UserID, type, balance, AccountID")
        String accountData = userID + "," + type + "," + balance + "," + accountID+"\n";

        // Write the account data to the file, followed by a newline character
        try (FileWriter fileWriter = new FileWriter(filePath, true)) {
            // Append the userString to the file
            fileWriter.write(accountData);
        } catch (IOException e) {
            e.printStackTrace();
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