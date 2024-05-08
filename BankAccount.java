public abstract class BankAccount {

    private static int globalID = 0;
    private int userID;
    private double balance;
    private final Integer accountID;

    public BankAccount(int userID, double balance, Integer id){
        this.userID = userID;
        this.balance = balance;
        if (id == null) {
            accountID = globalID;
            globalID += 1;
        }
        else{
            accountID = id;
        }
    }

    public double getBalance(){
        return balance;
    }

    public Integer getAccountID() {
        return accountID;
    }



    public void setBalance(double balance){
        this.balance= balance;
    }

    @Override
    public String toString() {
        return "Account ID: " + accountID + ", balance =" + balance + "]";
    }

    public int getUserID(){
        return userID;
    }

    public static void incrementGlobalID(){
        globalID+=1;
    }
}
