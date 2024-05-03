import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Bank {
    private LocalDate time;
    private List<Observer> observers = new ArrayList<>();

    public void setTime(LocalDate time) {
        this.time = time; // set new time
        notifyAllObservers(); // notify all observers about the time change
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
}