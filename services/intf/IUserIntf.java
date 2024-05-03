package services.intf;

import dto.User;

import java.io.IOException;

public interface IUserIntf {
    User login(String username,String password) throws IOException;
    int addUser(User user) throws IOException;
}
