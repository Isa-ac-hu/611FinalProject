package services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.BankAccount;
import dto.User;
import logic.Bank;
import logic.BankAccTypes;
import services.intf.IBankAccountIntf;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BankAccountImpl implements IBankAccountIntf {
    private String filePath=System.getProperty("user.dir")+"/data/account.json";
    private ObjectMapper mapper=new ObjectMapper();
    @Override
    public int addBankAccount(BankAccount bankAccount) throws IOException {
        //Add user logic
        List<BankAccount> accounts = readAll();
        accounts.add(bankAccount);
        try{
            mapper.writeValue(new File(filePath), accounts);
            return 1;
        }catch (Exception e){

        }
        return 0;
    }

    @Override
    public List<BankAccount> getBankAccountList(String userName, BankAccTypes bankAccTypes) throws IOException {
        List<BankAccount> bankAccounts = readAll();
        List<BankAccount> accounts= bankAccounts.stream().filter(l -> l.getUserName().equals(userName) && l.getAccountType().equals(bankAccTypes)).collect(Collectors.toList());
        return accounts;
    }

    @Override
    public List<BankAccount> getBankAccountListBySecurity(String userName, BankAccTypes bankAccTypes, int money) throws IOException {
        List<BankAccount> bankAccounts = readAll();
        List<BankAccount> accounts= bankAccounts.stream().filter(l -> l.getUserName().equals(userName) && l.getAccountType().equals(BankAccTypes.SAVINGS) && l.getBalance()>money).collect(Collectors.toList());
        return accounts;
    }

    public List<BankAccount> readAll() throws IOException {
        File file=new File(filePath);
        mapper=new ObjectMapper();
        //return mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, User.class));
        List<BankAccount> accounts = mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, BankAccount.class));
        return accounts;
    }
}
