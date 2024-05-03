package services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.User;
import services.intf.IUserIntf;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserImpl implements IUserIntf {
    private String filePath=System.getProperty("user.dir")+"/data/user.json";
    private ObjectMapper mapper=new ObjectMapper();
    @Override
    public User login(String username, String password) throws IOException {
        //Determine whether the login is successful. Returning null indicates login failure.。
        List<User> users = readAll();
        Optional<User> user= users.stream().filter(l -> l.getUserName().equals(username) && l.getPassword().equals(password)).findFirst();
        if(user.isPresent()){
            return user.get();
        }
        else{
            return null;
        }
    }

    @Override
    public int addUser(User user) throws IOException {
        //Add user logic
        List<User> users = readAll();
        users.add(user);
        try{
            mapper.writeValue(new File(filePath), users);
            return 1;
        }catch (Exception e){

        }
        return 0;
    }

    public List<User> readAll() throws IOException {
        File file=new File(filePath);
        mapper=new ObjectMapper();
        //return mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, User.class));
        List<User> users = mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, User.class));
        return users;
    }
}
