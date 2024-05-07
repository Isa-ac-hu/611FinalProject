package frontend;

import dto.BankAccount;
import logic.BankAccTypes;
import services.impl.BankAccountImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Pattern;

public class TransferPage extends JFrame {
    DefaultComboBoxModel<BankAccount> model=null;
    BankAccTypes type=BankAccTypes.CHECKING;
    /*Registration interface properties*/
    JLabel lblAccountTypeTitle = new JLabel("Account type: ");
    String[] str = {"CHECKING","SAVINGS","SECURITY"};
    JComboBox cbType = new JComboBox(str);
    JLabel lblAccountNameTitle = new JLabel("Account name: ");
    JComboBox<BankAccount> cbAcount=new JComboBox<>();
    JRadioButton JR1 =new JRadioButton("Deposit");//
    JRadioButton JR2 =new JRadioButton("Withdraw");//
    ButtonGroup BG=new ButtonGroup(); //创建按钮组
    JLabel lblDepositTitle = new JLabel("Amount: ");
    JTextField txtDeposit=new JTextField(20);
    JLabel lblPrompt = new JLabel("Withdrawal fee $10 (except Security)");


    //Buttons
    JButton btnReturn = new JButton("Return");
    JButton btnOK = new JButton("Confirm");
    JButton btnClear = new JButton("Clear");

    public TransferPage() {
        setFont(new Font("宋体", Font.BOLD, 16));
        /*Add properties and set window layout*/
        setTitle("Transfer Page");
        setDefaultCloseOperation(EXIT_ON_CLOSE);//Set off exit function
//
        //First panel: username and password input
        JPanel UserdataJP = new JPanel();
        UserdataJP.setLayout(new GridLayout(3,2,3,3));
        UserdataJP.add(lblAccountTypeTitle);
        UserdataJP.add(cbType);
        UserdataJP.add(lblAccountNameTitle);
        try{
            InitCB();
            UserdataJP.add(cbAcount);
        }catch (Exception e){

        }
        UserdataJP.add(lblDepositTitle);
        UserdataJP.add(txtDeposit);
        //BG.add(JR1);
        //BG.add(JR2);
        //UserdataJP.add(JR1);
        //UserdataJP.add(JR2);
        add(UserdataJP);

        JPanel jpOp = new JPanel();
        jpOp.setLayout(new FlowLayout(FlowLayout.CENTER));
        jpOp.setPreferredSize(new Dimension(600, 50));
        BG.add(JR1);
        BG.add(JR2);
        jpOp.add(JR1);
        jpOp.add(JR2);
        //jpOp.add(BG);
        add(jpOp);

        if(type==BankAccTypes.SECURITY){
            lblPrompt.setText("The selected account balance must be at least $5000");

        }
        JPanel jpPrompt = new JPanel();
        jpPrompt.setLayout(new FlowLayout(FlowLayout.CENTER));
        jpPrompt.setPreferredSize(new Dimension(600, 50));
        lblPrompt.setForeground(Color.red);
        jpPrompt.add(lblPrompt);
        add(jpPrompt);

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

        cbType.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // 处理选项变化的代码
                    //System.out.println("Selected item: " + e.getItem());
                    try{
                        cbAcount.removeAllItems();
                        BankAccountImpl bankAccount=new BankAccountImpl();
                        BankAccTypes types=BankAccTypes.CHECKING;
                        String typeSel=(String)cbType.getSelectedItem();
                        if(typeSel.equals("CHECKING"))
                            types=BankAccTypes.CHECKING;
                        else if(typeSel.equals("SAVINGS"))
                            types=BankAccTypes.SAVINGS;
                        else if(typeSel.equals("SECURITY"))
                            types=BankAccTypes.SECURITY;

                        List<BankAccount> bankAccountList=bankAccount.getBankAccountList(LoginPage.user.getUserName(), types);

                        // 使用DefaultComboBoxModel添加实体对象
                        Vector<BankAccount> persons = new Vector<>();
                        if(bankAccountList!=null){
                            for(BankAccount account:bankAccountList){
                                persons.add(account);
                                cbAcount.addItem(account);
                            }
                        }
                        model = new DefaultComboBoxModel<>(persons);
                    }catch (Exception ex){

                    }
                }
            }
        });

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
                txtDeposit.setText("");
            }
        });

        /*Confirm registration button*/
        btnOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if(!isInteger(txtDeposit.getText())){
                    JOptionPane.showMessageDialog(null,"Please enter an integer");
                    return;
                }
                BankAccountImpl ba=new BankAccountImpl();
                Map<String, Object> mp=null;
                try {
                    int opType=0;
                    if(JR1.isSelected()){
                        opType=1;
                    }
                    else if(JR2.isSelected()){
                        opType=2;
                    }
                    BankAccTypes types=BankAccTypes.CHECKING;
                    String typeSel=(String)cbType.getSelectedItem();
                    if(typeSel.equals("CHECKING"))
                        types=BankAccTypes.CHECKING;
                    else if(typeSel.equals("SAVINGS"))
                        types=BankAccTypes.SAVINGS;
                    else if(typeSel.equals("SECURITY"))
                        types=BankAccTypes.SECURITY;
                    BankAccount bankAccountSel=(BankAccount)cbAcount.getSelectedItem();
                    mp=ba.saveTransfer(LoginPage.user.getUserName(),bankAccountSel.getAccountName(),types,opType,Integer.valueOf(txtDeposit.getText()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if(mp.get("flag").toString().equals("1")){
                    CustomerPage customerPage = new CustomerPage();//Registration window
                    customerPage.setVisible(true);
                    setVisible(false); //Close display window
                }
                else{
                    JOptionPane.showMessageDialog(null,mp.get("msg").toString());
                }
            }
        });

    }

    private void InitCB() throws IOException {
        BankAccountImpl bankAccount=new BankAccountImpl();
        BankAccTypes types=BankAccTypes.CHECKING;
        String typeSel=(String)cbType.getSelectedItem();
        if(typeSel.equals("CHECKING"))
            types=BankAccTypes.CHECKING;
        else if(typeSel.equals("SAVINGS"))
            types=BankAccTypes.SAVINGS;
        else if(typeSel.equals("SECURITY"))
            types=BankAccTypes.SECURITY;

        List<BankAccount> bankAccountList=bankAccount.getBankAccountList(LoginPage.user.getUserName(), types);

        // 使用DefaultComboBoxModel添加实体对象
        Vector<BankAccount> persons = new Vector<>();
        if(bankAccountList!=null){
            for(BankAccount account:bankAccountList){
                persons.add(account);

            }
        }
        model = new DefaultComboBoxModel<>(persons);

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

    private boolean isInteger(String str){
        if(str!=""){
            Pattern pattern=Pattern.compile("[1-9]\\d*");
            return pattern.matcher(str).matches();
        }
        return false;
    }
}
