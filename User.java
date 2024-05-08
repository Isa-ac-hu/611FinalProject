
public abstract class User {
    String firstName;
    String lastName;
    String userName;
    String password;
    Integer userID;
    String role;

    static int globalUserID = 0;

    public User(){

    }

    public User(int userID, String role, String firstName, String lastName, String userName, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.userID = globalUserID;
        globalUserID++;
        this.role = role;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String _firstName){
        this.firstName=_firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String _lastName){
        this.lastName=_lastName;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String _userName){
        this.userName=_userName;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String _password){
        this.password=_password;
    }


    public String getRole(){
        return role;
    }

    public void setRole(String _role){
        this.role=_role;
    }

    public int getUserID(){
        return userID;
    }

}
