import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HomePage extends JFrame implements Observer, Subject{
    private static ATM atm;
    private User user; // To keep track of the user and their accounts
    private JPanel accountsPanel; // Panel to dynamically update accounts and loans
    private JPanel buttonPanel; // Panel for action buttons

    private ArrayList<Stock> stocks = Bank.getStocks();
    private ArrayList<User> users = LoginPage.getUsers();
    private List<Observer> observers = new ArrayList<>();
    private Bank bank;


    public HomePage(User user, ATM atm) {
        this.user = user;
        this.atm = atm;

        bank = atm.getBank();
        if(user instanceof Customer){
            initializeCustomerUI();
        }
        else if(user instanceof Manager){
            initializeManagerUI();
        }

    }

    private void initializeCustomerUI() {
        setTitle("ObjectBank Home Page");
        setSize(1400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JLabel("ObjectBank", JLabel.LEFT), BorderLayout.WEST);
        topPanel.add(new JLabel("Welcome, " + user.getFirstName() + "!", JLabel.RIGHT), BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        JButton backButton = new JButton("Log Out");
        backButton.addActionListener(e -> atm.goBack());
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.add(backButton);
        add(backPanel, BorderLayout.SOUTH);



        buttonPanel = new JPanel(new GridLayout(5, 1)); // Updated grid layout to accommodate one more button
        addButton("Create Account", e -> launchAccCreationPage());
        addButton("Loans", e -> launchLoanCreationPage());
        addButton("Pay Loan", e -> launchLoanPaymentPage());
        addButton("Deposit and Withdraw", e -> launchDepositWithdrawPage());
        addButton("Stock Market", e -> launchStockMarketPage()); // Added Stock Market button
        add(buttonPanel, BorderLayout.WEST);


        accountsPanel = new JPanel(new GridLayout(1, 5));
        add(accountsPanel, BorderLayout.CENTER);

        if (user instanceof Customer) {
            displayAccountsAndLoans((Customer) user);
        } else if (user instanceof Manager) {
            initializeManagerUI();
        }
    }

    private void addButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        buttonPanel.add(button);
    }

    private void launchLoanCreationPage() {
        LoanCreationPage loanPage = new LoanCreationPage((Customer) user, atm);
        atm.launchWindow(loanPage);
    }

    private void launchLoanPaymentPage() {
        atm.launchWindow(new LoanPaymentPage(user, atm));
    }

    private void launchDepositWithdrawPage() {
        atm.launchWindow(new DepositWithdrawPage(user, atm));
    }

    private void launchAccCreationPage() {
        atm.launchWindow(new BankAccCreationPage((Customer) user, atm));
    }

    private void launchStockMarketPage() {
        CustomerStockMarketPage stockMarketPage = new CustomerStockMarketPage(atm);
        observers.add(stockMarketPage);
        atm.launchWindow(stockMarketPage);
    }

    private void setupCustomerInterface(Customer customer) {
        displayAccountsAndLoans(customer);
    }

    private void initializeManagerUI() {
        // Existing setup code
        setTitle("ObjectBank Home Page");
        setSize(1400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top panel with the title and welcome message
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JLabel("ObjectBank", JLabel.LEFT), BorderLayout.WEST);
        topPanel.add(new JLabel("Welcome, " + user.getFirstName() + "!", JLabel.RIGHT), BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Bottom panel with a logout button
        JButton backButton = new JButton("Log Out");
        backButton.addActionListener(e -> atm.goBack());
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.add(backButton);
        add(backPanel, BorderLayout.SOUTH);

        // Set up button panel on the left side
        JPanel buttonPanel = new JPanel(new GridLayout(1, 1)); // 3 buttons: Stocks, Time, Users
        buttonPanel.setPreferredSize(new Dimension(150, 0)); // Set preferred width

        // Time display button
        JButton timeButton = new JButton("Time: " + bank.getTime()); // Display current time as an integer
        buttonPanel.add(timeButton);


        // Add button panel to the left side of the accountsPanel
        accountsPanel = new JPanel(new GridLayout(1, 3));
        add(accountsPanel, BorderLayout.CENTER);
        accountsPanel.add(buttonPanel, BorderLayout.WEST);
        // Set up the middle section (list of customers sorted by debt)
        JPanel customersPanel = new JPanel(new BorderLayout());

        // Sort users by total debt
        List<User> sortedUsers = new ArrayList<>(users);
        sortedUsers.sort(Comparator.comparingDouble(this::getTotalDebt));

        // Create a list model to display the sorted users
        DefaultListModel<String> customerListModel = new DefaultListModel<>();
        for (User user : sortedUsers) {
            String name = user.getFirstName() + " " + user.getLastName();
            double totalDebt = getTotalDebt(user);
            customerListModel.addElement(name + " - Total Debt: $" + totalDebt);
        }

        JList<String> customerList = new JList<>(customerListModel);
        customersPanel.add(new JScrollPane(customerList), BorderLayout.CENTER);

        // Add customers panel to the center of the accountsPanel
        accountsPanel.add(customersPanel, BorderLayout.CENTER);

        // Add a ListSelectionListener to the customerList
        customerList.addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedIndex = customerList.getSelectedIndex();
                if (selectedIndex != -1) {
                    // Get the selected customer
                    User selectedUser = sortedUsers.get(selectedIndex);

                    // Open a popup window to display customer information
                    showCustomerInfoPopup(selectedUser);
                }
            }
        });

        JPanel stocksPanel = new JPanel(new BorderLayout());
        DefaultListModel<String> stockListModel = new DefaultListModel<>();

        // Add all active stocks and their prices to the list
        for (Stock stock : stocks) {
            stockListModel.addElement(stock.getTicker() + " - Price: $" + stock.getCurrPrice());
        }

        JList<String> stockList = new JList<>(stockListModel);
        stocksPanel.add(new JScrollPane(stockList), BorderLayout.CENTER);

        // Add action listener to the stock list
        stockList.addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedIndex = stockList.getSelectedIndex();
                if (selectedIndex != -1) {
                    // Get the selected stock
                    Stock selectedStock = stocks.get(selectedIndex);

                    // Show pop-up box with options for the selected stock
                    showStockOptionsPopup(selectedStock, stockListModel, selectedIndex);
                }
            }
        });

// Add stocks panel to the right side of the accountsPanel
        accountsPanel.add(stocksPanel, BorderLayout.EAST);



        // Add action listener to the time button
        timeButton.addActionListener(e -> handleTimeAdjustment(timeButton));

        // Refresh the accountsPanel layout
        accountsPanel.revalidate();
        accountsPanel.repaint();
    }

    private void showStockOptionsPopup(Stock selectedStock, DefaultListModel<String> stockListModel, int selectedIndex) {
        // Create a pop-up dialog with options
        JDialog dialog = new JDialog(this, "Stock Options", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(3, 1));

        // Create stock button
        JButton createStockButton = new JButton("Create Stock");
        createStockButton.addActionListener(e -> {
            dialog.dispose();
            showCreateStockPopup(stockListModel);
        });
        panel.add(createStockButton);

        // Delete stock button
        JButton deleteStockButton = new JButton("Delete Stock");
        deleteStockButton.addActionListener(e -> {
            stocks.remove(selectedIndex);
            stockListModel.remove(selectedIndex);
            dialog.dispose();
        });
        panel.add(deleteStockButton);

        // Change price button
        JButton changePriceButton = new JButton("Change Price");
        changePriceButton.addActionListener(e -> {
            dialog.dispose();
            showChangePricePopup(selectedStock, stockListModel, selectedIndex);
        });
        panel.add(changePriceButton);

        dialog.add(panel);
        dialog.setVisible(true);
    }
    private void showChangePricePopup(Stock selectedStock, DefaultListModel<String> stockListModel, int selectedIndex) {
        JDialog dialog = new JDialog(this, "Change Stock Price", true);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(2, 1));

        JTextField priceField = new JTextField();
        JButton changeButton = new JButton("Change");

        panel.add(new JLabel("New Price:"));
        panel.add(priceField);
        panel.add(changeButton);

        dialog.add(panel);

        changeButton.addActionListener(e -> {
            double newPrice = 0.0;
            try {
                newPrice = Double.parseDouble(priceField.getText());
                if (newPrice < 0) {
                    JOptionPane.showMessageDialog(dialog, "Please enter a positive price.");
                    return;
                }
                selectedStock.setCurrPrice(newPrice);
                stockListModel.set(selectedIndex, selectedStock.getTicker() + " - Price: $" + newPrice);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input. Please enter a valid price.");
            }
        });

        dialog.setVisible(true);
    }
    private void showCreateStockPopup(DefaultListModel<String> stockListModel) {
        JDialog dialog = new JDialog(this, "Create Stock", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(3, 1));

        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JButton createButton = new JButton("Create");

        panel.add(new JLabel("Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Price:"));
        panel.add(priceField);

        panel.add(createButton);

        dialog.add(panel);

        createButton.addActionListener(e -> {
            String name = nameField.getText();
            try {
                double price = Double.parseDouble(priceField.getText());
                if (price < 0) {
                    JOptionPane.showMessageDialog(dialog, "Please enter a positive price.");
                    return;
                }
                Stock newStock = new Stock(name, price);
                stocks.add(newStock);
                stockListModel.addElement(name + " - Price: $" + price);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input. Please enter a valid price.");
            }
        });

        dialog.setVisible(true);
    }

    private void showCustomerInfoPopup(User user) {
        if(user instanceof Customer){
            Customer customer = (Customer)user;
            // Create a popup window to display customer information
            JDialog dialog = new JDialog(this, "Customer Information", true);
            dialog.setSize(400, 300);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout());

            // Create a panel to display customer data
            JPanel customerDataPanel = new JPanel(new GridLayout(0, 1));
            customerDataPanel.add(new JLabel("Customer ID: " + customer.getUserID()));
            customerDataPanel.add(new JLabel("First Name: " + customer.getFirstName()));
            customerDataPanel.add(new JLabel("Last Name: " + customer.getLastName()));
            customerDataPanel.add(new JLabel("Username: " + customer.getUserName()));

            // Display the customer's accounts
            customerDataPanel.add(new JLabel("Accounts:"));
            for (BankAccount account : customer.getAccounts()) {
                customerDataPanel.add(new JLabel(account.toString()));
            }

            // Display the customer's loans
            customerDataPanel.add(new JLabel("Loans:"));
            for (Loan loan : customer.getLoans()) {
                customerDataPanel.add(new JLabel(loan.toString()));
            }

            dialog.add(customerDataPanel, BorderLayout.CENTER);

            // Add a close button to the dialog
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(e -> dialog.dispose());
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(closeButton);
            dialog.add(buttonPanel, BorderLayout.SOUTH);

            dialog.setVisible(true);
        }

    }

    // Handle time adjustment when the time button is clicked
    private void handleTimeAdjustment(JButton timeButton) {
        // Create a popup window with an input box and accept button

        JTextField inputField = new JTextField(10);
        JButton acceptButton = new JButton("Accept");
        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter time adjustment (positive integer):"));
        panel.add(inputField);
        panel.add(acceptButton);

        // Create a dialog
        JDialog dialog = new JDialog(this, "Adjust Time", true);
        dialog.setSize(300, 100);
        dialog.setLocationRelativeTo(this);
        dialog.add(panel);

        // Handle accept button action
        acceptButton.addActionListener(e -> {
            System.out.println("Hello world");
            try {
                int adjustment = Integer.parseInt(inputField.getText());
                if (adjustment > 0) {
                    bank.incrementTime(adjustment); // Advance time in the bank
                    // Update the time button display

                    timeButton.setText("Time: " + bank.getTime());
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Please enter a positive integer.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input. Please enter a valid integer.");
            }
        });
        dialog.setVisible(true);


    }

    // Calculate the total debt of a user by summing up the debt amount of all their loans
    private double getTotalDebt(User user) {
        double totalDebt = 0.0;
        if(user instanceof Customer){
            Customer customer = (Customer)user;
            for (Loan loan : customer.getLoans()) {
                totalDebt += loan.getDebtAmount();
            }
        }

        return totalDebt;
    }

    private void displayAccountsAndLoans(Customer customer) {
        accountsPanel.removeAll();

        createAccountList(customer, SavingsAccount.class, "Savings Accounts");
        createAccountList(customer, CheckingAccount.class, "Checking Accounts");
        createLoanList(customer);

        accountsPanel.revalidate();
        accountsPanel.repaint();
    }

    private void createAccountList(Customer customer, Class<? extends BankAccount> accountType, String title) {
        List<String> accountDescriptions = customer.getAccounts().stream()
                .filter(accountType::isInstance)
                .map(BankAccount::toString)
                .collect(Collectors.toList());
        JList<String> accountList = new JList<>(accountDescriptions.toArray(new String[0]));
        JScrollPane scrollPane = new JScrollPane(accountList);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(title), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        accountsPanel.add(panel);
    }

    private void createLoanList(Customer customer) {
        List<String> loanDescriptions = customer.getLoans().stream()
                .map(loan -> String.format("Loan ID: %d, Amount: $%.2f, Date Issued: %d",
                        loan.getLoanID(), loan.getInitialLoanAmount(), loan.getLoanStartDate()))
                .collect(Collectors.toList());
        JList<String> loansList = new JList<>(loanDescriptions.toArray(new String[0]));
        JScrollPane scrollPane = new JScrollPane(loansList);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Loans"), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        accountsPanel.add(panel);
    }

    @Override
    public void update() {
        if (user instanceof Customer) {
            displayAccountsAndLoans((Customer) user);
        } else if (user instanceof Manager) {
            initializeManagerUI();
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
        for(Observer o : observers){
            o.update();
        }
    }




}
