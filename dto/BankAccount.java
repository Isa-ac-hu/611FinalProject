package dto;

import logic.Bank;
import logic.BankAccTypes;

public class BankAccount {
    String userName;
    BankAccTypes accountType;
    String accountName;
    Integer balance;

    public BankAccount(){

    }
    public BankAccount(String userName,BankAccTypes at, String an, Integer b){
        this.userName = userName;
        this.accountName = an;
        this.balance = b;
        this.accountType = at;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String _userName){
        this.userName=_userName;
    }

    public String getAccountName(){
        return accountName;
    }

    public void setAccountName(String _accountName){
        this.accountName=_accountName;
    }

    public BankAccTypes getAccountType(){
        return accountType;
    }

    public void setAccountType(BankAccTypes _bankAccTypes){
        this.accountType=_bankAccTypes;
    }

    public Integer getBalance(){
        return balance;
    }

    public void setBalance(Integer _balance){
        this.balance=_balance;
    }
}
