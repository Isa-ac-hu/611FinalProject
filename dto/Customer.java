package dto;
import logic.*;

import java.util.ArrayList;

public class Customer extends User implements Observer {
    ArrayList<BankAccount> accounts;
    ArrayList<Loan> loans;

    public Customer(int ID, String firstName, String lastName, String userName, String password, ArrayList<BankAccount> accounts, ArrayList<Loan> loans){
        super(1, "C" ,firstName, lastName, userName, password);
        this.accounts = accounts;
        this.loans = loans;
    }

    @Override
    public void update() {

    }
}
