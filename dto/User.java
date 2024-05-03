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

    public User(){

    }

    public User(String _firstName,String _lastName,String _username,String _password){
        firstName=_firstName;
        lastName=_lastName;
        userName=_username;
        password=_password;
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
}
