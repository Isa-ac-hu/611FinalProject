import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bank implements Subject{
    private static int time;
    private List<Observer> observers = new ArrayList<>();



    private static ArrayList<Stock> stocks;

    private static ArrayList<Trade> trades =new ArrayList<>();
    public Bank(){
        String filePath = "/Users/abdelazimlokma/Desktop/Desktop/Uni/Spring 24/CS 611 OOP/Final Project/repo/Untitled/Stocks.txt";
        stocks = readStocksFromFile(filePath);


    }

    public static void setTime(int time) {
        Bank.time = time;
    }

    public static List<Stock> getStocks() {
        return stocks;
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
                String[] stockData = line.split(",");

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


    public static ArrayList<Trade> getTrades(){
        return trades;
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
        for(Observer o: observers){
            o.update();
        }

    }
}