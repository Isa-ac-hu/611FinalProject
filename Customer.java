import java.util.ArrayList;

public class Customer extends User{
    ArrayList<BankAccount> accounts;
    ArrayList<Loan> loans;

    public Customer(ArrayList<BankAccount> accounts, ArrayList<Loan> loans){
        this.accounts = accounts;
        this.loans = loans;
    }
}
