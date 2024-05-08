public class Trade {
    private int securityAccountID;
    private String stockTicker;
    private int purchasePrice;
    private int currentPrice;
    private int quantity;
    private int unrealizedGainOrLoss;

    public Trade(int securityAccountID, String stockTicker, int purchasePrice, int currentPrice, int quantity) {
        this.securityAccountID = securityAccountID;
        this.stockTicker = stockTicker;
        this.purchasePrice = purchasePrice;
        this.currentPrice = currentPrice;
        this.quantity = quantity;
        this.unrealizedGainOrLoss = (currentPrice - purchasePrice) * quantity;
    }

    // Update market value
    public void updateMarketValue(int newCurrentPrice) {
        this.currentPrice = newCurrentPrice;
        this.unrealizedGainOrLoss = (newCurrentPrice - this.purchasePrice) * this.quantity;
    }

    public String getStockTicker() {
        return stockTicker;
    }

    public int getPurchasePrice() {
        return purchasePrice;
    }

    public int getUnrealizedGainOrLoss() {
        return unrealizedGainOrLoss;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getSecurityAccountID() {
        return securityAccountID;
    }

    public void setUnrealizedGainOrLoss(int unrealizedGainOrLoss) {
        this.unrealizedGainOrLoss = unrealizedGainOrLoss;
    }


    // Method to save to file
    public String serialize() {
        return String.format("%d,%s,%d,%d,%d,%d", securityAccountID, stockTicker, purchasePrice, currentPrice, quantity, unrealizedGainOrLoss);
    }
}
