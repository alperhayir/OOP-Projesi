package service;

import model.User;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private List<User> users = new ArrayList<>();

    public void addUser(String id, String name) {
        if (userExists(id)) {
            throw new IllegalArgumentException("Bu ID ile kullanıcı zaten var");
        }
        users.add(new User(id, name));
    }

    public User findUserById(String id) {
        for (User u : users) {
            if (u.getId().equals(id)) return u;
        }
        return null;
    }

    public boolean userExists(String id) {
        return findUserById(id) != null;
    }

    public List<User> getAllUsers() {
        return users;
    }
}
