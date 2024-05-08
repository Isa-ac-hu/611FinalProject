
import java.util.ArrayList;

public class Customer extends User implements Observer {
    private ArrayList<BankAccount> accounts;
    private ArrayList<Loan> loans;

    private SecurityAccount secAcct;


    public Customer(int ID, String firstName, String lastName, String userName, String password, ArrayList<BankAccount> accounts, ArrayList<Loan> loans){
        super(1, "C" ,firstName, lastName, userName, password);
        this.accounts = accounts;
        this.loans = loans;
    }



    public void setAccounts(ArrayList<BankAccount> accounts){
        this.accounts = accounts;
    }

    public void setLoans(ArrayList<Loan> loans){
        this.loans = loans;
    }

    public ArrayList<BankAccount> getAccounts() {
        return accounts;
    }

    public ArrayList<Loan> getLoans(){
        return loans;
    }


    @Override
    public void update() {

    }
    public SecurityAccount getSecurityAccount() {
        return secAcct;
    }

    public void setSecAcct(SecurityAccount secAcct) {
        this.secAcct = secAcct;
    }
}
