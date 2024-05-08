import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class CustomerStockMarketPage extends JFrame implements Observer{
    private JTable stockTable;
    private JButton buyButton;
    private JButton sellButton;
    private JButton securityAccountButton;
    private JLabel unrealizedGainLabel;
    private JLabel totalStocksLabel;
    private JButton backButton;

    private ATM atm;
    private Bank bank;

    static ArrayList<Trade> trades = Bank.getTrades();

    public CustomerStockMarketPage(ATM atm) {
        this.atm = atm;
        this.bank = atm.getBank();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Stock Market");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize column names and data for the table
        Vector<String> columnNames = new Vector<>(Arrays.asList("Ticker", "Price", "Owned", "Current Value", "Purchase Price"));
        Vector<Vector<String>> data = prepareStockTableData();
        stockTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(stockTable);
        add(scrollPane, BorderLayout.CENTER);

        // Info panel with financial labels
        JPanel infoPanel = new JPanel(new GridLayout(1, 3));
        unrealizedGainLabel = new JLabel("Unrealized Gain: $0.00");
        totalStocksLabel = new JLabel("Total Stocks: 0");
        infoPanel.add(unrealizedGainLabel);
        infoPanel.add(totalStocksLabel);
        add(infoPanel, BorderLayout.NORTH);

        // Button panel for actions
        JPanel buttonPanel = new JPanel();
        buyButton = new JButton("Buy");
        sellButton = new JButton("Sell");
        securityAccountButton = new JButton("Link Security Account");
        backButton = new JButton("Back");  // Initialize the back button

        // Add action listeners to buttons
        buyButton.addActionListener(this::buyStock);
        sellButton.addActionListener(this::sellStock);
        securityAccountButton.addActionListener(this::launchSecurityAccountDialog);
        backButton.addActionListener(e -> atm.goBack());  // Add action to go back using ATM's goBack method

        // Add buttons to the panel
        buttonPanel.add(buyButton);
        buttonPanel.add(sellButton);
        buttonPanel.add(securityAccountButton);
        buttonPanel.add(backButton);  // Add the back button to the panel
        add(buttonPanel, BorderLayout.SOUTH);

        updateButtonStatus();
        updateFinancialLabels();
        setVisible(true);
    }

    private Vector<Vector<String>> prepareStockTableData() {
        Vector<Vector<String>> data = new Vector<>();
        Customer customer = (Customer) atm.getCurrentUser();
        SecurityAccount securityAccount = customer.getSecurityAccount();

        if (securityAccount == null) {
            return data;  // If there's no linked security account, return empty data
        }

        // Assuming market stocks and their current prices are retrievable via bank.getMarket()
        for (Stock stock : bank.getMarket()) {
            Vector<String> row = new Vector<>();
            row.add(stock.getTicker());
            row.add(String.valueOf(stock.getCurrPrice()));
            long owned = securityAccount.getAccTrades().stream()
                    .filter(trade -> trade.getStockTicker().equals(stock.getTicker()))
                    .mapToInt(Trade::getQuantity)
                    .sum();
            double currentValue = owned * stock.getCurrPrice();
            double purchasePrice = securityAccount.getAccTrades().stream()
                    .filter(trade -> trade.getStockTicker().equals(stock.getTicker()))
                    .mapToDouble(Trade::getPurchasePrice)
                    .average().orElse(0);

            row.add(String.valueOf(owned));
            row.add(String.format("$%.2f", currentValue));
            row.add(String.format("$%.2f", purchasePrice));

            data.add(row);
        }

        return data;
    }

    private void buyStock(ActionEvent e) {
        int selectedRow = stockTable.getSelectedRow();
        if (selectedRow >= 0) {
            String ticker = (String) stockTable.getValueAt(selectedRow, 0);
            int currentPrice = (int) Double.parseDouble(stockTable.getValueAt(selectedRow, 1).toString().replace("$", ""));

            // Ask for the quantity to buy
            String quantityString = JOptionPane.showInputDialog(this, "Enter quantity to buy for " + ticker + " at $" + currentPrice + " each:");
            try {
                int quantity = Integer.parseInt(quantityString);
                if (quantity > 0) {
                    Customer customer = (Customer) atm.getCurrentUser();
                    SecurityAccount securityAccount = customer.getSecurityAccount();
                    Trade newTrade = new Trade(securityAccount.getLinkedAccountID(), ticker, currentPrice, currentPrice, quantity);
                    securityAccount.addTrade(newTrade);
                    trades.add(newTrade);

                    // Assuming you have a method to update the total balance in security account
                    double newBalance = securityAccount.getTotalBalance() - quantity * currentPrice;
                    securityAccount.setTotalBalance(newBalance);

                    // Refresh table and labels
                    refreshTableAndLabels();
                    JOptionPane.showMessageDialog(this, "Purchase successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Quantity must be greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a stock.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        updateFinancialLabels();
    }


    private void sellStock(ActionEvent e) {
        int selectedRow = stockTable.getSelectedRow();
        if (selectedRow >= 0) {
            String ticker = (String) stockTable.getValueAt(selectedRow, 0);
            int currentPrice = (int) Double.parseDouble(stockTable.getValueAt(selectedRow, 1).toString().replace("$", ""));

            // Ask for the quantity to sell
            String quantityString = JOptionPane.showInputDialog(this, "Enter quantity to sell for " + ticker + " at $" + currentPrice + " each:");
            try {
                int quantity = Integer.parseInt(quantityString);
                Customer customer = (Customer) atm.getCurrentUser();
                SecurityAccount securityAccount = customer.getSecurityAccount();
                if (quantity > 0 && hasSufficientShares(securityAccount, ticker, quantity)) {
                    Trade newTrade = new Trade(securityAccount.getLinkedAccountID(), ticker, currentPrice, currentPrice, -quantity);
                    securityAccount.addTrade(newTrade);
                    trades.add(newTrade);

                    // Update balance
                    double newBalance = securityAccount.getTotalBalance() + quantity * currentPrice;
                    securityAccount.setTotalBalance(newBalance);

                    // Refresh table and labels
                    refreshTableAndLabels();
                    JOptionPane.showMessageDialog(this, "Sale successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Not enough shares or invalid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a stock.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        updateFinancialLabels();
    }

    private boolean hasSufficientShares(SecurityAccount account, String ticker, int quantity) {
        return account.getAccTrades().stream()
                .filter(t -> t.getStockTicker().equals(ticker))
                .mapToInt(Trade::getQuantity)
                .sum() >= quantity;
    }

    private void launchSecurityAccountDialog(ActionEvent e) {
        Customer customer = (Customer) atm.getCurrentUser();
        List<SavingsAccount> eligibleAccounts = customer.getAccounts().stream()
                .filter(SavingsAccount.class::isInstance)
                .map(SavingsAccount.class::cast)
                .filter(acc -> acc.getBalance() >= 5000)
                .collect(Collectors.toList());

        JComboBox<SavingsAccount> accountComboBox = new JComboBox<>(new Vector<>(eligibleAccounts));
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Select a Savings Account with at least $5000 balance:"));
        panel.add(accountComboBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Link Security Account", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            SavingsAccount selectedAccount = (SavingsAccount) accountComboBox.getSelectedItem();
            SecurityAccount newSecurityAccount = new SecurityAccount(customer.getUserID(), selectedAccount.getAccountID(), selectedAccount.getBalance());
            customer.setSecAcct(newSecurityAccount);
            JOptionPane.showMessageDialog(this, "Security account linked successfully: " + selectedAccount);
            updateButtonStatus(); // Update button status after linking
        }
    }

    private void updateButtonStatus() {
        Customer customer = (Customer) atm.getCurrentUser();
        boolean hasSecurityAccount = customer.getSecurityAccount() != null;
        buyButton.setEnabled(hasSecurityAccount);
        sellButton.setEnabled(hasSecurityAccount);
        securityAccountButton.setEnabled(!hasSecurityAccount);
    }

    private void refreshTableAndLabels() {
        Vector<Vector<String>> data = prepareStockTableData();
        ((DefaultTableModel) stockTable.getModel()).setDataVector(data, new Vector<>(Arrays.asList("Ticker", "Price", "Owned", "Current Value", "Purchase Price")));
        stockTable.repaint();

        // Update labels as needed, perhaps recalculate unrealized and realized gains
    }

    private void updateFinancialLabels() {
        Customer customer = (Customer) atm.getCurrentUser();
        SecurityAccount securityAccount = customer.getSecurityAccount();
        if (securityAccount != null) {
            // Calculating unrealized and realized gains and the total number of stocks
            double unrealizedGain = 0.0;
            int totalStocks = 0;

            for (Trade trade : securityAccount.getAccTrades()) {
                int quantity = trade.getQuantity();
                double purchasePrice = trade.getPurchasePrice();
                double currentPrice = trade.getCurrentPrice();
                totalStocks += quantity;
                unrealizedGain += (currentPrice - purchasePrice) * quantity;
            }

            // Update labels
            unrealizedGainLabel.setText(String.format("Unrealized Gain: $%.2f", unrealizedGain));
            totalStocksLabel.setText(String.format("Total Stocks: %d", totalStocks));
        } else {
            // Default values if no security account is linked
            unrealizedGainLabel.setText("Unrealized Gain: No Security Act. Linked");
            totalStocksLabel.setText("Total Stocks:  No Security Act. Linked");
        }
    }


    @Override
    public void update() {
        refreshTableAndLabels();
        updateFinancialLabels();
    }
}
