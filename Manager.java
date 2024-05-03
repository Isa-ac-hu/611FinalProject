import java.time.LocalDate;

public class Manager extends User{

    private final Bank managedBank;

    // Constructor for Manager
    public Manager(String firstName, String lastName, String username, String password, Bank bank) {
        super(firstName, lastName, username, password);
        this.managedBank = bank;
    }

    // Method to change the time in the Bank
    public void changeTime(LocalDate newTime) {
        this.managedBank.setTime(newTime);
        notifyObservers();  // Notify all observers about the time change
    }

    // Notify all observers about changes
    private void notifyObservers() {
        for (Observer o : managedBank.getObservers()) {
            o.update();
        }
    }
}
