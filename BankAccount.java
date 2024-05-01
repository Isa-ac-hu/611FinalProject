public abstract class BankAccount {
    BankAccTypes accountType;
    String accountName;
    Integer balance;

    public BankAccount(BankAccTypes at, String an, Integer b){
        this.accountName = an;
        this.balance = b;
        this.accountType = at;
    }



}
