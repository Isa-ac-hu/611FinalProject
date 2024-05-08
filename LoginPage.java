import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginPage extends JFrame {

    static ArrayList<User> users = new ArrayList<>();

    static ArrayList<Stock> stocks = new ArrayList<>();

    static ArrayList<Trade> trades = Bank.getTrades();

    static int currentID = 0;
    private ATM atm;
    static ArrayList<BankAccount> accounts = new ArrayList<>();


    static ArrayList<SecurityAccount> securityAccounts = new ArrayList<>();
    static ArrayList<Loan> loans = new ArrayList<>();
    /*Add properties to the window as follows：*/
    JButton Login = new JButton("Login");
    JButton Register = new JButton("Register");
    JLabel usernameLable = new JLabel("Username：");
    JLabel passwordLable = new JLabel("Password：");
    JPasswordField password = new JPasswordField(10);
    JTextField username = new JTextField(10);

    private Integer loadTime(){
        String filePath = "/Users/abdelazimlokma/Desktop/Desktop/Uni/Spring 24/CS 611 OOP/Final Project/repo/Untitled/Time.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            if (line != null) {
                return Integer.parseInt(line.trim()); // Convert the string to a double
            } else {
                throw new IOException("The file is empty");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*Methods are as follows：*/
    public LoginPage(ATM atm){
        this.atm = atm;
        setTitle("Login Page");

        loadUsers();
        loadAccounts();
        associateAccountsToUsers();
        parseLoanData();
        associateLoansToUsers();
        atm.getBank().setTime(loadTime());
        //Set flow layout 1 or FlowLayout.CENTER, center alignment

        //The first panel: fill in user data
        JPanel Userdata = new JPanel();
        Userdata.setLayout(new FlowLayout(FlowLayout.LEFT));
        Userdata.add(usernameLable);
        Userdata.add(username);
        Userdata.add(passwordLable);
        Userdata.add(password);
        add(Userdata); //Add the first panel to the user interface

        //Second panel: button component
        JPanel BuJp = new JPanel();
        BuJp.setLayout(new GridLayout(3,1,5,5));
        BuJp.add(Login);
        BuJp.add(Register);  //registration
        add(BuJp); //Add the second panel to the user interface

        setLayout(new FlowLayout());
        setSize(450,250);
        setResizable(false);   //Set non-stretchable size
        setLocationRelativeTo(null);//Center the window
        setVisible(true); //Open display window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Set the exiting action

        //Add a listener event for the login button
        Login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (username.getText().trim().length() == 0 || new String(password.getPassword()).trim().length() == 0) {
                    JOptionPane.showMessageDialog(null, "Username and password are not allowed to be empty");
                    return;
                }

                try {
                    String myUsername = username.getText().trim();
                    String myPassword = new String(password.getPassword()).trim();

                    boolean found = false;
                    User matchedUser = null;
                    // Loop through the ArrayList of users to find a match
                    for (User user : users) {
                        if (user.getUserName().equals(myUsername) && user.getPassword().equals(myPassword)) {
                            // Match found
                            found = true;
                            matchedUser = user;
                            atm.setCurrentUser(matchedUser);
                            break;
                        }
                    }

                    if (found) {
                        // Set the "user" field to the matched user
                        // Optionally, you could add code here to handle the successful login (e.g., navigating to another screen).
                        loadSecurityAccounts();
                        loadTrades();
                        HomePage homePage = new HomePage(matchedUser, atm);
                        atm.setHomePage(homePage);
                        atm.launchWindow(homePage);
                    } else {
                        // Account does not exist, display a message
                        JOptionPane.showMessageDialog(null, "Account does not exist");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        //Add a listening event for the user registration button
        Register.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                RegisterPage registerPage = new RegisterPage(atm);
                atm.launchWindow(registerPage);
            }
        });

    }

    public void loadAccounts(){

        String filePath = "/Users/abdelazimlokma/Desktop/Desktop/Uni/Spring 24/CS 611 OOP/Final Project/repo/Untitled/Accounts.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] accountFields = line.split(",");

                // Parse each field
                int userId = Integer.parseInt(accountFields[0]);
                String type = accountFields[1];
                double amount = Double.parseDouble(accountFields[2]);
                Integer accID = Integer.parseInt(accountFields[3]);
                BankAccount.incrementGlobalID();

                // Create a new Account object and add it to the list
                BankAccount account = null;
                if(type.equals("Checking")){
                    account = new CheckingAccount(userId, amount, accID);
                }
                else if(type.equals("Saving")){
                    account = new SavingsAccount(userId, amount, accID);
                }
                accounts.add(account);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void loadUsers(){
        String filePath = "/Users/abdelazimlokma/Desktop/Desktop/Uni/Spring 24/CS 611 OOP/Final Project/repo/Untitled/Users.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {


                String[] userFields = line.split(",");

                // Parse each field
                int id = Integer.parseInt(userFields[0]);
                String firstName = userFields[1];
                String lastName = userFields[2];
                String username = userFields[3];
                String password = userFields[4];
                User user = null;
                if(id == 0){
                    user = new Manager(firstName, lastName, username, password, null);
                }
                else{
                    ArrayList<BankAccount> accounts = new ArrayList<>();
                    ArrayList<Loan> loans = new ArrayList<>();
                    user = new Customer(id, firstName, lastName, username, password, accounts, loans);
                }
                users.add(user);
                currentID++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void associateAccountsToUsers(){

        Customer customer = null;
        for (User user : users) {
            if (user.userID != 0){
                customer = (Customer) user;
                ArrayList<BankAccount> userAccounts = new ArrayList<>();
                for (BankAccount account : accounts) {
                    // Check if the account ID matches the user's ID
                    if (account.getUserID() == customer.getUserID()) {
                        userAccounts.add(account);
                    }
                }
                customer.setAccounts(userAccounts);
            }
        }
    }

    public List<Loan> parseLoanData() {
        String filePath = "/Users/abdelazimlokma/Desktop/Desktop/Uni/Spring 24/CS 611 OOP/Final Project/repo/Untitled/Loans.txt";
        // Try-with-resources statement to handle file reading
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Read the file line by line
            while ((line = br.readLine()) != null) {
                // Split the line by commas
                String[] loanData = line.split(",");

                // Check if the parsed data has the correct length (7 fields)
                if (loanData.length == 7) {
                    // Parse each field from the line
                    int loanID = Integer.parseInt(loanData[0]);
                    int userID = Integer.parseInt(loanData[1]);
                    double initialLoanAmount = Double.parseDouble(loanData[2]);
                    double debtAmount = Double.parseDouble(loanData[3]);
                    String collateralName = loanData[4];
                    double collateralAmount = Double.parseDouble(loanData[5]);
                    int creationTime = Integer.parseInt(loanData[6]);

                    // Create the appropriate Collateral object (adjust as necessary)
                    Collateral collateral = null;
                    if (!collateralName.equalsIgnoreCase("none")) {
                        collateral = new Collateral(collateralName, collateralAmount);
                    }

                    // Assuming you have a method to fetch a customer by ID from the bank
                    int customerID = userID;

                    // Create a new Loan object using the constructor
                    Loan loan = new Loan(loanID, customerID, initialLoanAmount, debtAmount, collateral, creationTime);

                    // Add the loan object to the list of loans
                    loans.add(loan);
                } else {
                    System.err.println("Warning: Incorrect data format in line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return the list of Loan objects
        return loans;
    }

    public void associateLoansToUsers() {
        // Iterate through each user in the list
        for (User user : users) {
            // Check if the user is a valid Customer (not a guest user or invalid user)
            if (user instanceof Customer) {
                // Cast the user to a Customer object
                Customer customer = (Customer) user;

                // Create a list to hold the customer's loans
                ArrayList<Loan> customerLoans = new ArrayList<>();

                // Iterate through the list of loans
                for (Loan loan : loans) {
                    // Check if the loan's user ID matches the customer's user ID
                    if (loan.getCustomerID() == customer.getUserID()) {
                        // Add the loan to the customer's list of loans
                        customerLoans.add(loan);
                    }
                }

                // Set the list of loans for the customer
                customer.setLoans(customerLoans);
            }
        }
    }



    public static int getCurrentID() {
        return currentID;
    }
    public static void setCurrentID(int ID) {
        currentID = ID;
    }

    public static void incrementID() {
        currentID++;
    }
    public static void addUser(User user){
        users.add(user);
        incrementID();
    }

    public void addLoan(Loan loan){
        Customer customer = (Customer)atm.getCurrentUser();
        customer.getLoans().add(loan);
    }

    public static ArrayList<Stock> getStocks(){
        return stocks;
    }

    public static ArrayList<User> getUsers(){
        return users;
    }
    public static ArrayList<BankAccount> getAccounts(){
        return accounts;
    }

    public static ArrayList<SecurityAccount> getSecurityAccounts(){
        return securityAccounts;
    }

    public static ArrayList<Loan> getLoans(){
        return loans;
    }

    public void loadSecurityAccounts() {
        String filePath = "/Users/abdelazimlokma/Desktop/Desktop/Uni/Spring 24/CS 611 OOP/Final Project/repo/Untitled/SecAcct.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] accountData = line.split(",");
                int userID = Integer.parseInt(accountData[0]);
                int linkedAccountID = Integer.parseInt(accountData[1]);
                int totalBalance = (int) Double.parseDouble(accountData[2]);
                SecurityAccount secAccount = new SecurityAccount(userID, linkedAccountID, totalBalance);
                // Find the corresponding user and add this account to them
                for (User user : users) {
                    if (user.getUserID() == userID && user instanceof Customer) {
                        ((Customer) user).setSecAcct(secAccount);
                        break;
                    }
                }
                securityAccounts.add(secAccount);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTrades() {
        String filePath = "/Users/abdelazimlokma/Desktop/Desktop/Uni/Spring 24/CS 611 OOP/Final Project/repo/Untitled/Trades.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tradeData = line.split(",");
                int securityAccountID = Integer.parseInt(tradeData[0]);
                String stockTicker = tradeData[1];
                int purchasePrice = Integer.parseInt(tradeData[2]);
                int currentPrice = Integer.parseInt(tradeData[3]);
                int quantity = Integer.parseInt(tradeData[4]);
                Trade trade = new Trade(securityAccountID, stockTicker, purchasePrice, currentPrice, quantity);
                // Find the corresponding security account and add this trade to it
                trades.add(trade);
                users.stream()
                        .filter(u -> u instanceof Customer)
                        .map(u -> ((Customer) u).getSecurityAccount())
                        .filter(sa -> sa != null && sa.getLinkedAccountID() == securityAccountID)
                        .forEach(sa -> sa.addTrade(trade));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
