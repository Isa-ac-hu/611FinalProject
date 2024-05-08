public class Stock {
    private String ticker;
    private double currPrice;

    public Stock(String ticker, double price){
        this.ticker = ticker;
        this.currPrice = price;
    }

    public void setCurrPrice(double currPrice) {
        this.currPrice = currPrice;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public double getCurrPrice() {
        return currPrice;
    }

    public String getTicker() {
        return ticker;
    }
}
