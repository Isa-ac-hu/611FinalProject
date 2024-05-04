package frontend;

import dto.BankAccount;
import dto.User;
import logic.Bank;
import logic.BankAccTypes;
import services.impl.BankAccountImpl;
import services.impl.UserImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class AccountPage extends JFrame {
    BankAccTypes type=BankAccTypes.CHECKING;
    /*Registration interface properties*/
    JLabel lblAccountTypeTitle = new JLabel("AccountType: ");
    JLabel lblAccountType = new JLabel("");
    JLabel lblAccountNameTitle = new JLabel("AccountName: ");
    JTextField txtAccountName=new JTextField(20);
    JLabel lblDepositTitle = new JLabel("Deposit amount: ");
    JTextField txtDeposit=new JTextField(20);
    JComboBox<BankAccount> cbAcount=new JComboBox<>();

    //Buttons
    JButton btnReturn = new JButton("Return");
    JButton btnOK = new JButton("Confirm");
    JButton btnClear = new JButton("Clear");

    public AccountPage(BankAccTypes _type) {
        type=_type;
        setFont(new Font("宋体", Font.BOLD, 16));
        /*Add properties and set window layout*/
        setTitle("Add");
        setDefaultCloseOperation(EXIT_ON_CLOSE);//Set off exit function
//
        //First panel: username and password input
        JPanel UserdataJP = new JPanel();
        UserdataJP.setLayout(new GridLayout(3,2,3,3));
        UserdataJP.add(lblAccountTypeTitle);
        String valueType="";
        if(type==BankAccTypes.CHECKING)
            lblAccountType.setText("Checking");
        else if(type==BankAccTypes.SAVINGS)
            lblAccountType.setText("Saving");
        else if(type==BankAccTypes.SECURITY){
            lblAccountType.setText("Security");
        }
        UserdataJP.add(lblAccountType);
        UserdataJP.add(lblAccountNameTitle);
        if(type==BankAccTypes.SECURITY){
            try{
                InitCB();
            }catch (Exception e){

            }

            UserdataJP.add(cbAcount);
        }

        else
            UserdataJP.add(txtAccountName);
        UserdataJP.add(lblDepositTitle);
        UserdataJP.add(txtDeposit);
        add(UserdataJP);

        JPanel Bujp = new JPanel();
        Bujp.setLayout(new FlowLayout(FlowLayout.CENTER));
        Bujp.add(btnOK); //Confirm registration
        Bujp.add(btnClear);     //Re-enter button
        Bujp.add(btnReturn); //Return to login
        add(Bujp);  //Add a sixth panel button component: button component

        setSize(500, 400);
        //Nested layout: Layout contains layout. With the help of panel JPanel, generally one layout cannot solve the problem or there are many components.
        setLayout(new FlowLayout());
        setResizable(false);   //Set non-stretchable size
        setVisible(true);
        setLocationRelativeTo(null);//Center the window

        /*Return to login page button*/
        btnReturn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                CustomerPage customerPage = new CustomerPage();//Registration window
                customerPage.setVisible(true);
                setVisible(false); //Close display window
            }
        });

        /*Re-enter button*/
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                txtAccountName.setText("");
                txtDeposit.setText("");
            }
        });

        /*Confirm registration button*/
        btnOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                /*Determine whether the username and password are empty*/
                BankAccount bankAccount=new BankAccount();
                bankAccount.setUserName(LoginPage.user.getUserName());
                bankAccount.setAccountType(type);
                if(type==BankAccTypes.SECURITY){
                    BankAccount bankAccountSel=(BankAccount)cbAcount.getSelectedItem();
                    bankAccount.setAccountName(bankAccountSel.getAccountName());
                }
                else
                    bankAccount.setAccountName(txtAccountName.getText());
                bankAccount.setBalance(Integer.valueOf(txtDeposit.getText()));
                BankAccountImpl ba=new BankAccountImpl();
                int flag=0;
                try {
                    flag=ba.addBankAccount(bankAccount);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if(flag>0){
                    CustomerPage customerPage = new CustomerPage();//Registration window
                    customerPage.setVisible(true);
                    setVisible(false); //Close display window
                }
                else{
                    JOptionPane.showMessageDialog(null,"Error");
                }
            }
        });

    }

    private void InitCB() throws IOException {
        BankAccountImpl bankAccount=new BankAccountImpl();
        List<BankAccount> bankAccountList=bankAccount.getBankAccountListBySecurity(LoginPage.user.getUserName(), BankAccTypes.SECURITY,5000);

        // 使用DefaultComboBoxModel添加实体对象
        Vector<BankAccount> persons = new Vector<>();
        if(bankAccountList!=null){
            for(BankAccount account:bankAccountList){
                persons.add(account);

            }
        }
        DefaultComboBoxModel<BankAccount> model = new DefaultComboBoxModel<>(persons);;

        cbAcount = new JComboBox<>(model);

        // 设置渲染器以显示实体对象的自定义字符串表示
        cbAcount.setRenderer(new ListCellRenderer<BankAccount>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends BankAccount> list, BankAccount value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value == null) {
                    return new JLabel();
                }
                return new JLabel(value.getAccountName());
            }
        });
    }
}
