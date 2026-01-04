package service;

import model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Test
    void kullaniciEkleniyorMu() {
        UserService userService = new UserService();

        userService.addUser("u1", "Alper");

        assertEquals(1, userService.getAllUsers().size());
        assertEquals("Alper", userService.getAllUsers().get(0).getName());
    }

    @Test
    void ayniIdIleKullaniciEklenemez() {
        UserService userService = new UserService();

        userService.addUser("u1", "Alper");

        assertThrows(IllegalArgumentException.class, () ->
                userService.addUser("u1", "Tekrar")
        );
    }

    @Test
    void kullaniciIdIleBulunabiliyorMu() {
        UserService userService = new UserService();

        userService.addUser("u1", "Alper");

        User found = userService.findUserById("u1");

        assertNotNull(found);
        assertEquals("u1", found.getId());
    }
}
