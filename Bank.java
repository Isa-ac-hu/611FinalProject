import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bank {
    private static int time;
    private List<Observer> observers = new ArrayList<>();

    private static List<Stock> market;

    private static ArrayList<Stock> stocks;

    private static ArrayList<Trade> trades;
    public Bank(){
        this.market = new ArrayList<>();
        market.add(new Stock("AAPL", 1000));
        String filePath = "/Users/abdelazimlokma/Desktop/Desktop/Uni/Spring 24/CS 611 OOP/Final Project/repo/Untitled/Stocks.txt";
        stocks = readStocksFromFile(filePath);


    }

    public static List<Stock> getMarket() {
        return market;
    }
    public static Stock getStock(String ticker){
        for (Stock s: market){
            if (s.getTicker().equals(ticker)){
                return s;
            }
        }
        throw new RuntimeException("Stock not found");
    }

    public void incrementTime(int numMonths) {
        time += numMonths; // set new time
        notifyAllObservers(); // notify all observers about the time change
    }

    public static int getTime() {
        return time;
    }

    public void attachObserver(Observer observer) {
        observers.add(observer);
    }

    public void detachObserver(Observer observer) {
        observers.remove(observer);
    }

    private void notifyAllObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public List<Observer> getObservers() {
        return observers;
    }


    public ArrayList<Stock> readStocksFromFile(String filePath) {
        ArrayList<Stock> stocks = new ArrayList<>();

        // Try-with-resources statement to handle file reading
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Read the file line by line
            while ((line = br.readLine()) != null) {
                // Split the line using ", " as the delimiter
                String[] stockData = line.split(",\\s+");

                // Parse the ticker and price from the line
                String ticker = stockData[0];
                double price = Double.parseDouble(stockData[1]);

                // Instantiate a Stock object using the parsed data
                Stock stock = new Stock(ticker, price);

                // Add the Stock object to the list
                stocks.add(stock);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return the list of Stock objects
        return stocks;
    }

    public static ArrayList<Stock> getStocks(){
        return stocks;
    }

    public static ArrayList<Trade> getTrades(){
        return trades;
    }
}