package frontend;

import dto.User;
import services.impl.UserImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class LoginPage extends JFrame {

    public static User user;
    /*Add properties to the window as follows：*/
    JButton Login =      new JButton("Login");
    JButton Cancel =     new JButton("Reset");
    JButton Register =   new JButton("Register");
    JLabel usernameLable = new JLabel("Username：");
    JLabel passwordLable = new JLabel("Password：");
    JPasswordField password  = new JPasswordField(10);
    JTextField username = new JTextField(10);

    /*Methods are as follows：*/
    public LoginPage(){
        setTitle("Login Page");
        //Set flow layout 1 or FlowLayout.CENTER, center alignment

        //The first panel: fill in user data
        JPanel Userdata = new JPanel();
        Userdata.setLayout(new FlowLayout(FlowLayout.LEFT));
        Userdata.add(usernameLable);
        Userdata.add(username);
        Userdata.add(passwordLable);
        Userdata.add(password);
        add(Userdata); //Add the first panel to the user interface

        //Second panel: button component
        JPanel BuJp = new JPanel();
        BuJp.setLayout(new GridLayout(3,1,5,5));
        BuJp.add(Login);
        BuJp.add(Cancel);
        BuJp.add(Register);  //registration
        add(BuJp); //Add the second panel to the user interface

        setLayout(new FlowLayout());
        setSize(450,250);
        setResizable(false);   //Set non-stretchable size
        setLocationRelativeTo(null);//Center the window
        setVisible(true); //Open display window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Set the exiting action

        //Add a listener event for the login button
        Login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (username.getText().trim().length() == 0 || new String(password.getPassword()).trim().length() == 0) {
                    JOptionPane.showMessageDialog(null, "Username and password are not allowed to be empty");
                    return;
                }

                try{
                    String myusername = username.getText().trim();
                    String mypassword = new String(password.getPassword()).trim();
                    UserImpl userImpl=new UserImpl();
                    user=userImpl.login(myusername, mypassword);
                    if(user!=null) {

                        //JOptionPane.showMessageDialog(null, "Successfully logged in");
                        CustomerPage customerPage = new CustomerPage();//registration window
                        customerPage.setVisible(true);
                        setVisible(false); //close the display window
                    }
                    else JOptionPane.showMessageDialog(null, "Username or password is incorrect");
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        //Add a listener event for the re-enter button
        Cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                username.setText("");
                password.setText("");

            }
        });

        //Add a listening event for the user registration button
        Register.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                RegisterPage resiter = new RegisterPage();//registration window
                resiter.setVisible(true);
                setVisible(false); //close the display window
            }
        });

    }
}
