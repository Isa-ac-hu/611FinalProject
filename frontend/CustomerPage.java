package frontend;

import dto.BankAccount;
import logic.BankAccTypes;
import services.impl.BankAccountImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class CustomerPage extends JFrame{
    /*Registration interface properties*/
    JButton btnTransfer = new JButton("Transfer");
    JButton btnStock = new JButton("Stock");
    JButton btnLoan = new JButton("Loan");
    JButton btnLogout = new JButton("Logout");
    JButton btnSetting = new JButton("Setting");
    JLabel lblUserTitle=new JLabel("Username:");
    JLabel lblUserName=new JLabel("");
    JLabel lblTitle1=new JLabel("Checking");
    JButton btnChecking=new JButton("Create");
    JLabel lblTitle2=new JLabel("Saving");
    JButton btnSaving=new JButton("Create");
    JLabel lblTitle3=new JLabel("Security");
    JButton btnSecurity=new JButton("Create");
    JList<BankAccount> listChecking=new JList();
    JList<BankAccount> listSaving=new JList();
    JList<BankAccount> listSecurity=new JList();
    DefaultListModel<BankAccount> model = new DefaultListModel<>();
    DefaultListModel<BankAccount> modelSaving = new DefaultListModel<>();
    DefaultListModel<BankAccount> modelSecurity = new DefaultListModel<>();

    public CustomerPage() {
        setFont(new Font("宋体", Font.BOLD, 16));
        /*Add properties and set window layout*/
        setTitle("test");
        setDefaultCloseOperation(EXIT_ON_CLOSE);//Set off exit function
//
        //First panel: username and password input
        JPanel MainJP=new JPanel();
        MainJP.setLayout(new BoxLayout(MainJP, BoxLayout.X_AXIS)); // 设置水平布局
        MainJP.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 添加边距


        JPanel LeftJP = new JPanel();
        LeftJP.setPreferredSize(new Dimension(200, 600)); // 设置面板的首选大小
        LeftJP.setLayout(new BoxLayout(MainJP, BoxLayout.Y_AXIS)); // 设置垂直布局
        LeftJP.setLayout(new GridLayout(10,1,1,1));
        LeftJP.add(btnTransfer);
        LeftJP.add(btnStock);
        LeftJP.add(btnLoan);
        LeftJP.add(btnLogout);
        LeftJP.add(btnSetting);
        LeftJP.add(new JLabel());

        JPanel RightJP = new JPanel();
        RightJP.setPreferredSize(new Dimension(600, 600)); // 设置面板的首选大小
        //RightJP.setLayout(new BoxLayout(MainJP, BoxLayout.Y_AXIS)); // 设置垂直布局
        RightJP.setLayout(new GridLayout(16,1));
        RightJP.add(lblUserTitle);
        RightJP.add(lblUserName);
        //RightJP.add(Box.createVerticalStrut(10)); // 添加水平间距
        RightJP.add(lblTitle1);
        btnChecking.setPreferredSize(new Dimension(50, 30));
        RightJP.add(btnChecking);

        try{
            InitChecking();
        }
        catch (Exception e){

        }
        JScrollPane scrollPane1 = new JScrollPane(listChecking);
        RightJP.add(scrollPane1, BorderLayout.NORTH);

        //RightJP.add(Box.createVerticalStrut(10)); // 添加水平间距
        RightJP.add(lblTitle2);
        btnSaving.setPreferredSize(new Dimension(50, 30));
        RightJP.add(btnSaving);

        try{
            InitSaving();
        }
        catch (Exception e){

        }
        JScrollPane scrollPane2 = new JScrollPane(listSaving);
        RightJP.add(scrollPane2, BorderLayout.NORTH);

        //RightJP.add(Box.createVerticalStrut(10)); // 添加水平间距
        RightJP.add(lblTitle3);
        btnSecurity.setPreferredSize(new Dimension(50, 30));
        RightJP.add(btnSecurity);

        try{
            InitSecurity();
        }
        catch (Exception e){

        }
        JScrollPane scrollPane3 = new JScrollPane(listSecurity);
        RightJP.add(scrollPane3, BorderLayout.NORTH);

        RightJP.add(new JLabel());


        MainJP.add(LeftJP);
        MainJP.add(Box.createHorizontalStrut(10)); // 添加水平间距
        MainJP.add(RightJP);
        add(MainJP);



        setSize(800, 600);
        //Nested layout: Layout contains layout. With the help of panel JPanel, generally one layout cannot solve the problem or there are many components.
        setLayout(new FlowLayout());
        setResizable(false);   //Set non-stretchable size
        setVisible(true);
        setLocationRelativeTo(null);//Center the window

        /*Return to login page button*/
        btnChecking.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                AccountPage accountPage=new AccountPage(BankAccTypes.CHECKING);
                accountPage.setVisible(true);
                setVisible(false); //close the display window
                //model.addElement(new BankAccount("aa", BankAccTypes.CHECKING,"test1",100));
                //model.addElement(new BankAccount("aa", BankAccTypes.CHECKING,"test2",400));
                //model.addElement(new BankAccount("aa", BankAccTypes.CHECKING,"test3",200));
            }
        });
        btnSaving.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                AccountPage accountPage=new AccountPage(BankAccTypes.SAVINGS);
                accountPage.setVisible(true);
                setVisible(false); //close the display window
                //model.addElement(new BankAccount("aa", BankAccTypes.CHECKING,"test1",100));
                //model.addElement(new BankAccount("aa", BankAccTypes.CHECKING,"test2",400));
                //model.addElement(new BankAccount("aa", BankAccTypes.CHECKING,"test3",200));
            }
        });
        btnSecurity.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                AccountPage accountPage=new AccountPage(BankAccTypes.SECURITY);
                accountPage.setVisible(true);
                setVisible(false); //close the display window
                //model.addElement(new BankAccount("aa", BankAccTypes.CHECKING,"test1",100));
                //model.addElement(new BankAccount("aa", BankAccTypes.CHECKING,"test2",400));
                //model.addElement(new BankAccount("aa", BankAccTypes.CHECKING,"test3",200));
            }
        });

        Init();
    }

    private void Init(){
        lblUserName.setText(LoginPage.user.getUserName());
    }

    private void InitChecking() throws IOException {
        listChecking = new JList<>(model);
        BankAccountImpl bankAccount=new BankAccountImpl();
        List<BankAccount> bankAccountList=bankAccount.getBankAccountList(LoginPage.user.getUserName(), BankAccTypes.CHECKING);
        if(bankAccountList!=null){
            for(BankAccount account:bankAccountList){
                model.addElement(new BankAccount(account.getUserName(), account.getAccountType(),account.getAccountName(),account.getBalance()));
            }
        }
        listChecking.setCellRenderer(new BankAccountListRenderer());
    }

    private void InitSaving() throws IOException {
        listSaving = new JList<>(modelSaving);
        BankAccountImpl bankAccount=new BankAccountImpl();
        List<BankAccount> bankAccountList=bankAccount.getBankAccountList(LoginPage.user.getUserName(), BankAccTypes.SAVINGS);
        if(bankAccountList!=null){
            for(BankAccount account:bankAccountList){
                modelSaving.addElement(new BankAccount(account.getUserName(), account.getAccountType(),account.getAccountName(),account.getBalance()));
            }
        }
        listSaving.setCellRenderer(new BankAccountListRenderer());
    }

    private void InitSecurity() throws IOException {
        listSecurity = new JList<>(modelSecurity);
        BankAccountImpl bankAccount=new BankAccountImpl();
        List<BankAccount> bankAccountList=bankAccount.getBankAccountList(LoginPage.user.getUserName(), BankAccTypes.SECURITY);
        if(bankAccountList!=null){
            for(BankAccount account:bankAccountList){
                modelSecurity.addElement(new BankAccount(account.getUserName(), account.getAccountType(),account.getAccountName(),account.getBalance()));
            }
        }
        listSecurity.setCellRenderer(new BankAccountListRenderer());
    }

    private class BankAccountListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            BankAccount person = (BankAccount) value;
            String text = "Account name: "+person.getAccountName()+"\n  Balance: "+String.valueOf(person.getBalance());
            return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
        }
    }
}
