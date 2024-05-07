package services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.BankAccount;
import logic.BankAccTypes;
import services.intf.IBankAccountIntf;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BankAccountImpl implements IBankAccountIntf {
    private String filePath=System.getProperty("user.dir")+"/data/account.json";
    private ObjectMapper mapper=new ObjectMapper();
    @Override
    public Map<String,Object> addBankAccount(BankAccount bankAccount) throws IOException {
        Map<String, Object> mp=new HashMap<>();
        int flag=0;
        String msg="";
        //Add user logic
        List<BankAccount> accounts = readAll();
        if(bankAccount.getAccountType()==BankAccTypes.SECURITY){
            //Start to determine whether the SECURITY account is allowed to be added
            for(int i=0;i<accounts.size();i++){
                if(accounts.get(i).getAccountType()==BankAccTypes.SAVINGS){
                    if(accounts.get(i).getAccountName().equals(bankAccount.getAccountName())){
                        if(accounts.get(i).getBalance()<5000){
                            flag=2;
                            msg="The selected account balance only has"+" $"+String.valueOf(accounts.get(i).getBalance());
                            mp.put("flag",flag);
                            mp.put("msg",msg);
                            return mp;
                        }
                        else{
                            int diff=accounts.get(i).getBalance()-bankAccount.getBalance();
                            if(diff<2500){
                                flag=2;
                                msg="After creation, the balance of the selected account is less than $2500";
                                mp.put("flag",flag);
                                mp.put("msg",msg);
                                return mp;
                            }
                            else{
                                //Deduct the balance of the selected account
                                BankAccount setAccount=accounts.get(i);
                                setAccount.setBalance(diff);
                                accounts.set(i,setAccount);
                            }
                        }
                    }
                }
            }
        }
        else{
            bankAccount.setBalance(bankAccount.getBalance()-50);
        }
        accounts.add(bankAccount);
        try{
            mapper.writeValue(new File(filePath), accounts);
            flag = 1;
            msg="Added successfully";
        }catch (Exception e){

        }

        mp.put("flag",flag);
        mp.put("msg",msg);
        return mp;
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

    @Override
    public int isRepeat(String accountName, BankAccTypes type) throws IOException {
        int flag=0;
        List<BankAccount> bankAccounts = readAll();
        List<BankAccount> accounts= bankAccounts.stream().filter(l -> l.getAccountName().equals(accountName) && l.getAccountType().equals(type)).collect(Collectors.toList());
        if(accounts!=null){
            if(accounts.size()>0){
                flag=1;
            }
        }
        return flag;
    }

    @Override
    public Map<String, Object> saveTransfer(String userName, String accountName,BankAccTypes bankAccTypes,int opType, int money) throws IOException {
        Map<String, Object> mp=new HashMap<>();
        int flag=0;
        String msg="";
        List<BankAccount> bankAccounts = readAll();
        for(int i=0;i<bankAccounts.size();i++){
            if(bankAccounts.get(i).getUserName().equals(userName)
            && bankAccounts.get(i).getAccountName().equals(accountName)
            && bankAccounts.get(i).getAccountType()==bankAccTypes){
                BankAccount account=bankAccounts.get(i);
                if(opType==1){
                    //存款
                    if(bankAccTypes==BankAccTypes.SECURITY){
                        for(int j=0;j<bankAccounts.size();j++){
                            if(bankAccounts.get(j).getAccountType()==BankAccTypes.SAVINGS){
                                if(bankAccounts.get(j).getAccountName().equals(accountName)){
                                    int diff=bankAccounts.get(j).getBalance()-money;
                                    if(diff<2500){
                                        flag=2;
                                        msg="After depositing, the balance of the selected account is less than $2500";
                                        mp.put("flag",flag);
                                        mp.put("msg",msg);
                                        return mp;
                                    }
                                    else{
                                        //扣除选择的账户的余额
                                        BankAccount setAccount=bankAccounts.get(j);
                                        setAccount.setBalance(diff);
                                        bankAccounts.set(j,setAccount);
                                        account.setBalance(account.getBalance()+money);
                                    }
                                }
                            }
                        }
                    }
                    else{
                        account.setBalance(account.getBalance()+money);
                    }
                    bankAccounts.set(i,account);
                }
                else{
                    //取款
                    if(bankAccTypes==BankAccTypes.SECURITY){
                        for(int j=0;j<bankAccounts.size();j++){
                            if(bankAccounts.get(j).getAccountType()==BankAccTypes.SAVINGS){
                                if(bankAccounts.get(j).getAccountName().equals(accountName)){
                                    BankAccount setAccount=bankAccounts.get(j);
                                    setAccount.setBalance(setAccount.getBalance()+money);
                                    bankAccounts.set(j,setAccount);
                                    account.setBalance(account.getBalance()-money);
                                }
                            }
                        }
                    }
                    else{
                        account.setBalance(account.getBalance()-money-10);
                    }
                    bankAccounts.set(i,account);
                }
            }
        }
        try{
            mapper.writeValue(new File(filePath), bankAccounts);
            flag = 1;
            msg="D&W successfully";
            mp.put("flag",flag);
            mp.put("msg",msg);
        }catch (Exception e){

        }
        return mp;
    }

    public List<BankAccount> readAll() throws IOException {
        File file=new File(filePath);
        mapper=new ObjectMapper();
        //return mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, User.class));
        List<BankAccount> accounts = mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, BankAccount.class));
        return accounts;
    }
}
