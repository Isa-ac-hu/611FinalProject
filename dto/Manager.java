package dto;
import logic.*;
import java.time.LocalDate;

public class Manager extends User{

    private final Bank managedBank;

    // Constructor for dto.Manager
    public Manager(String firstName, String lastName, String username, String password, Bank bank) {
        super(0, "M", firstName, lastName, username, password);
        this.managedBank = bank;
    }

    // Method to change the time in the logic.Bank
    public void changeTime(LocalDate newTime) {
        this.managedBank.setTime(newTime);
        notifyObservers();  // Notify all observers about the time change
    }

    public LocalDate getTime(){
        return this.managedBank.getTime();
    }


    // Notify all observers about changes
    private void notifyObservers() {
        for (Observer o : managedBank.getObservers()) {
            o.update();
        }
    }
}
