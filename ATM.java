

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class ATM {
    private Stack<JFrame> windowStack = new Stack<>();
    private Bank bank;
    private HomePage homePage;
    User currentUser = null;




    // Constructor
    public ATM() {
        // Optionally, initialize with the login page
        launchBankApp();
        this.bank = new Bank();
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setHomePage(HomePage homePage) {
        this.homePage = homePage;
    }

    public HomePage getHomePage() {
        return homePage;
    }

    // Launches a new window and adds it to the stack
    public void launchWindow(JFrame newWindow) {
        if (!windowStack.isEmpty()) {
            windowStack.peek().setVisible(false); // Hide the current top window
        }
        windowStack.push(newWindow);
        newWindow.setVisible(true);
        newWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newWindow.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                goBack();
            }
        });
    }

    // Goes back to the previous window if possible
    public void goBack() {
        saveData();
        if (windowStack.size() > 1) {
            JFrame currentWindow = windowStack.pop();
            currentWindow.dispose(); // Close current window
            windowStack.peek().setVisible(true); // Show previous window
        }
        else if (windowStack.size() == 1) {
            // If it's the last window, just show it again without popping it from the stack
            windowStack.peek().setVisible(true);
        }else {
            System.exit(0); // Exit application if no more windows in the stack
        }
    }

    // Launches the bank application by opening the login page
    public void launchBankApp() {
        JFrame loginPage = new LoginPage(this);
        launchWindow(loginPage);
    }

    public Bank getBank() {
        return bank;
    }


    public void saveData(){
        ArrayList<User> users = LoginPage.getUsers();
        ArrayList<BankAccount> accounts = LoginPage.getAccounts();
        ArrayList<Loan> loans = LoginPage.getLoans();
        ArrayList<SecurityAccount> securityAccounts = LoginPage.getSecurityAccounts();
        ArrayList<Stock> stocks = (ArrayList<Stock>) Bank.getStocks();
        ArrayList<Trade> trades = Bank.getTrades();

        String filePath = "/Users/abdelazimlokma/Desktop/Desktop/Uni/Spring 24/CS 611 OOP/Final Project/repo/Untitled/Users.txt";

        // Try-with-resources statement to handle file writing
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Iterate through the list of users
            for (User user : users) {
                // Retrieve user data fields
                int id = user.getUserID();
                String firstName = user.getFirstName();
                String lastName = user.getLastName();
                String username = user.getUserName();
                String password = user.getPassword();

                // Format the user data as a string (e.g., "ID, firstName, lastName, username, password")
                String userData = id + "," + firstName + "," + lastName + "," + username + "," + password;

                // Write the user data to the file, followed by a newline character
                writer.write(userData);
                writer.newLine();
            }
        } catch (IOException e) {
            // Handle any exceptions that may occur during file writing
            e.printStackTrace();
        }

        filePath = "/Users/abdelazimlokma/Desktop/Desktop/Uni/Spring 24/CS 611 OOP/Final Project/repo/Untitled/Accounts.txt";

        // Try-with-resources statement to handle file writing
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Iterate through the list of accounts
            for (BankAccount account : accounts) {
                // Retrieve account data fields
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
                String accountData = userID + "," + type + "," + balance + "," + accountID;

                // Write the account data to the file, followed by a newline character
                writer.write(accountData);
                writer.newLine();
            }
        } catch (IOException e) {
            // Handle any exceptions that may occur during file writing
            e.printStackTrace();
        }

        // Define the file path for Loans.txt
        filePath = "/Users/abdelazimlokma/Desktop/Desktop/Uni/Spring 24/CS 611 OOP/Final Project/repo/Untitled/Loans.txt";

        // Try-with-resources statement to handle file writing
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Iterate through the list of loans
            for (Loan loan : loans) {
                // Retrieve loan data fields
                int loanID = loan.getLoanID();
                int customerID = loan.getCustomerID();
                double initialLoanAmount = loan.getInitialLoanAmount();
                double debtAmount = loan.getDebtAmount();
                String collateralName = loan.getCollateral() != null ? loan.getCollateral().getName() : "None";
                double collateralAmount = loan.getCollateral() != null ? loan.getCollateral().getValue() : 0.0;

                // Format the loan data as a string (e.g., "LoanID, customerID, initialLoanAmount, debtAmount, collateralName, collateralAmount")
                String loanData = loanID + "," + customerID + "," + initialLoanAmount + "," + debtAmount + "," + collateralName + "," + collateralAmount + "," + loan.getLoanStartDate();

                // Write the loan data to the file, followed by a newline character
                writer.write(loanData);
                writer.newLine();
            }
        } catch (IOException e) {
            // Handle any exceptions that may occur during file writing
            e.printStackTrace();
        }

        // Define the file path for SecurityAccounts.txt
        filePath = "/Users/abdelazimlokma/Desktop/Desktop/Uni/Spring 24/CS 611 OOP/Final Project/repo/Untitled/SecAcct.txt";

        // Try-with-resources statement to handle file writing
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Iterate through the list of security accounts
            for (SecurityAccount account : securityAccounts) {
                // Retrieve account data fields
                int userID = account.getUserID();
                int linkedAccountID = account.getLinkedAccountID();
                double totalBalance = account.getTotalBalance();

                // Format the security account data as a string (e.g., "userID, linkedAccountID, total balance")
                String accountData = userID + "," + linkedAccountID + "," + totalBalance;

                // Write the account data to the file, followed by a newline character
                writer.write(accountData);
                writer.newLine();
            }
        } catch (IOException e) {
            // Handle any exceptions that may occur during file writing
            e.printStackTrace();
        }

        filePath = "/Users/abdelazimlokma/Desktop/Desktop/Uni/Spring 24/CS 611 OOP/Final Project/repo/Untitled/Stocks.txt";

        // Try-with-resources statement to handle file writing
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Iterate through the list of stocks
            for (Stock stock : stocks) {
                // Retrieve stock data fields
                String ticker = stock.getTicker();
                double value = stock.getCurrPrice();

                // Format the stock data as a string (e.g., "ticker, value")
                String stockData = ticker + "," + value;

                // Write the stock data to the file, followed by a newline character
                writer.write(stockData);
                writer.newLine();
            }
        } catch (IOException e) {
            // Handle any exceptions that may occur during file writing
            e.printStackTrace();
        }

        filePath = "/Users/abdelazimlokma/Desktop/Desktop/Uni/Spring 24/CS 611 OOP/Final Project/repo/Untitled/Trades.txt";

        // Try-with-resources statement to handle file writing
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Iterate through the list of trades
            for (Trade trade : trades) {
                // Retrieve trade data fields
                int securityAccountID = trade.getSecurityAccountID();
                String stockTicker = trade.getStockTicker();
                int purchasePrice = trade.getPurchasePrice();
                int currentPrice = trade.getCurrentPrice();
                int quantity = trade.getQuantity();
                int unrealizedGainOrLoss = trade.getUnrealizedGainOrLoss();

                // Format the trade data as a string (e.g., "securityAccountID, stockTicker, purchasePrice, currentPrice, quantity, unrealizedGainOrLoss")
                String tradeData = securityAccountID + "," + stockTicker + "," + purchasePrice + "," + currentPrice + "," + quantity + "," + unrealizedGainOrLoss;

                // Write the trade data to the file, followed by a newline character
                writer.write(tradeData);
                writer.newLine();
            }
        } catch (IOException e) {
            // Handle any exceptions that may occur during file writing
            e.printStackTrace();
        }
    }




}
