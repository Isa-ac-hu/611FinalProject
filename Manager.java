public class Manager extends User{

    private final Bank managedBank;

    // Constructor for dto.Manager
    public Manager(String firstName, String lastName, String username, String password, Bank bank) {
        super(0, "M", firstName, lastName, username, password);
        this.managedBank = bank;
    }

    // Method to change the time in the logic.Bank
    public void changeTime(int numDays) {
        this.managedBank.incrementTime(numDays);
        notifyObservers();  // Notify all observers about the time change
    }

    public int getTime(){
        return Bank.getTime();
    }


    // Notify all observers about changes
    private void notifyObservers() {
        for (Observer o : managedBank.getObservers()) {
            o.update();
        }
    }


}
