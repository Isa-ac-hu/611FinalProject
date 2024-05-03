package dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    @JsonProperty("firstName")
    String firstName;
    @JsonProperty("lastName")
    String lastName;
    @JsonProperty("userName")
    String userName;
    @JsonProperty("password")
    String password;
//    @JsonProperty("ID")

    int ID;
    @JsonProperty("role")
    String role;
    public User(){

    }

    public User(int ID, String role, String _firstName, String _lastName, String _username, String _password){
        firstName=_firstName;
        lastName=_lastName;
        userName=_username;
        password=_password;
        //this.ID = ID;
        ID = 5;
        this.role = role;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String _firstName){
        this.firstName=_firstName;
    }

    public String getLastName(String _lastName){
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

    public int getID(){
        return ID;
    }
    public String getRole(){
        return role;
    }
}
