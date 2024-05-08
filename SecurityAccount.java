import java.util.ArrayList;
import java.util.List;

public class SecurityAccount {
    private int userID;
    private int linkedAccountID;
    private List<Trade> accTrades;
    private double totalBalance;

    public SecurityAccount(int userID, int linkedAccountID, double totalBalance) {
        this.userID = userID;
        this.linkedAccountID = linkedAccountID;
        this.totalBalance = totalBalance;
        this.accTrades = new ArrayList<>();
    }

    // Getters and setters
    public void addTrade(Trade trade) {
        this.accTrades.add(trade);
    }

    // Method to save to file
    public String serialize() {
        return String.format("%d,%d,%d,%d", userID, linkedAccountID, totalBalance);
    }

    public int getLinkedAccountID() {
        return linkedAccountID;
    }

    public double getTotalBalance() {
        return totalBalance;
    }

    public List<Trade> getAccTrades() {
        return accTrades;
    }

    public int getUserID() {
        return userID;
    }

    public void setTotalBalance(double totalBalance) {
        this.totalBalance = totalBalance;
    }

    public int getRealizedGain(){
        int sum = 0;
        for (Trade t: accTrades){
            sum+=t.getUnrealizedGainOrLoss();
        }
        return sum;
    }

}
