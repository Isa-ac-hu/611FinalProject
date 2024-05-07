package services.intf;

import dto.BankAccount;
import logic.BankAccTypes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IBankAccountIntf {
    Map<String,Object> addBankAccount(BankAccount bankAccount) throws IOException;
    List<BankAccount> getBankAccountList(String userName, BankAccTypes bankAccTypes) throws IOException;
    List<BankAccount> getBankAccountListBySecurity(String userName, BankAccTypes bankAccTypes,int money) throws IOException;
    int isRepeat(String accountName,BankAccTypes type) throws IOException;
    Map<String,Object> saveTransfer(String userName,String accountName,BankAccTypes bankAccTypes,int opType,int money) throws IOException;
}
