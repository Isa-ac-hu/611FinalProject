
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class RegisterPage extends JFrame {

    /*Registration interface properties*/
    JLabel firstNameLable = new JLabel("Firstname: ");
    JLabel lastNameLable = new JLabel("Lastname: ");
    JLabel userNameLable = new JLabel("Username: ");
    JLabel passwordLable = new JLabel("Password: ");
    JLabel passwordAaginLable = new JLabel("Confirm password: ");


    //logic.Input box
    JTextField firstName = new JTextField(10);
    JTextField lastName = new JTextField(10);
    JTextField userName = new JTextField(10);
    JPasswordField password = new JPasswordField(10);
    JPasswordField passwordAagin = new JPasswordField(10);

    //Buttons
    JButton returnLogin = new JButton("Return to login");
    JButton registerOK = new JButton("Confirm");
    JButton cancel = new JButton("Clear");

    ATM atm;

    public RegisterPage(ATM atm) {
        this.atm = atm;
        setFont(new Font("宋体", Font.BOLD, 16));
        /*Add properties and set window layout*/
        setTitle("Registration");
        setDefaultCloseOperation(EXIT_ON_CLOSE);//Set off exit function
//
        //First panel: username and password input
        JPanel UserdataJP = new JPanel();
        UserdataJP.setLayout(new GridLayout(5,2,5,5));
        UserdataJP.add(userNameLable);
        UserdataJP.add(userName);
        UserdataJP.add(firstNameLable);
        UserdataJP.add(firstName);
        UserdataJP.add(lastNameLable);
        UserdataJP.add(lastName);
        UserdataJP.add(passwordLable);
        UserdataJP.add(password);
        UserdataJP.add(passwordAaginLable);
        UserdataJP.add(passwordAagin);
        add(UserdataJP);

        JPanel Bujp = new JPanel();
        Bujp.setLayout(new FlowLayout(FlowLayout.CENTER));
        Bujp.add(registerOK); //Confirm registration
        Bujp.add(cancel);     //Re-enter button
        Bujp.add(returnLogin); //Return to login
        add(Bujp);  //Add a sixth panel button component: button component

        setSize(312, 400);
        //Nested layout: Layout contains layout. With the help of panel JPanel, generally one layout cannot solve the problem or there are many components.
        setLayout(new FlowLayout());
        setResizable(false);   //Set non-stretchable size
        setVisible(true);
        setLocationRelativeTo(null);//Center the window

        /*Return to login page button*/
        returnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                atm.goBack(); // Change here to use the ATM's goBack method
            }
        });

        /*Re-enter button*/
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                userName.setText("");
                password.setText("");
                firstName.setText("");
                lastName.setText("");
                passwordAagin.setText("");
            }
        });

        /*Confirm registration button*/
        registerOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                /*Determine whether the username and password are empty*/
                if(userName.getText().length() != 0 && password.getPassword().length != 0 &&
                        passwordAagin.getPassword().length != 0){
                    /*Use spaces to demonstrate special characters*/
                    if(userName.getText().contains(" ")){
                        /*Reload prompt box*/
                        JOptionPane.showMessageDialog(null,"Username cannot contain special characters"
                                ,"Wrong user information", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    /*Password has special characters*/
                    if(password.getPassword().toString().contains(" ")|| passwordAagin.getPassword().toString().contains(" ")){
                        JOptionPane.showMessageDialog(null,"Password cannot contain special characters"
                                ,"Wrong user information", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    /*Passwords are inconsistent*/
                    if (!(new String(password.getPassword()).trim().equals(new String(passwordAagin.getPassword()).trim()))){
                        JOptionPane.showMessageDialog(null,"Passwords are inconsistent"
                                ,"Wrong user information", JOptionPane.WARNING_MESSAGE);
                        return;
                    }



                    String firstNameString = firstName.getText().toString();
                    String lastNameString = lastName.getText().toString();
                    //Username
                    String userNameString = userName.getText().toString();
                    //Password
                    String passwordString = new String(password.getPassword());


                    ArrayList<BankAccount> accounts = new ArrayList<>();
                    ArrayList<Loan> loans = new ArrayList<>();
                    User user = new Customer(LoginPage.getCurrentID(), firstNameString,
                            lastNameString, userNameString, passwordString, accounts, loans);

                    try{
                        writeUserToFile(user, "/Users/abdelazimlokma/Desktop/Desktop/Uni/Spring 24/CS 611 OOP/Final Project/repo/Untitled/Users.txt");
                        LoginPage.addUser(user);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    JOptionPane.showMessageDialog(null,"Account created successfully!");
                    HomePage homePage = new HomePage(user, atm);
                    atm.setHomePage(homePage);
                    atm.launchWindow(homePage);
                }else {
                    JOptionPane.showMessageDialog(null,"Username and password cannot be empty");
                    return;
                }
            }
        });

    }

    public void writeUserToFile(User user, String filePath) {
        // Define the format to write the user object to the file
        String userString = user.getUserID() + "," + user.getFirstName() + "," +
                user.getLastName() + "," + user.getUserName() + "," +
                user.getPassword() + "\n";

        // Use FileWriter to append the userString to the end of the file
        try (FileWriter fileWriter = new FileWriter(filePath, true)) {
            // Append the userString to the file
            fileWriter.write(userString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
