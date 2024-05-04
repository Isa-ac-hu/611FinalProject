package services.intf;

import dto.BankAccount;
import logic.BankAccTypes;

import java.io.IOException;
import java.util.List;

public interface IBankAccountIntf {
    int addBankAccount(BankAccount bankAccount) throws IOException;
    List<BankAccount> getBankAccountList(String userName, BankAccTypes bankAccTypes) throws IOException;
    List<BankAccount> getBankAccountListBySecurity(String userName, BankAccTypes bankAccTypes,int money) throws IOException;

}
