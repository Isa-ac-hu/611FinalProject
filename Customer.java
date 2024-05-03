import java.util.ArrayList;

public class Customer extends User implements Observer {
    ArrayList<BankAccount> accounts;
    ArrayList<Loan> loans;

    public Customer(String fn, String ln, String un, String pw, ArrayList<BankAccount> accounts, ArrayList<Loan> loans){
        super(fn, ln, un, pw);
        this.accounts = accounts;
        this.loans = loans;
    }

    @Override
    public void update() {

    }
}
