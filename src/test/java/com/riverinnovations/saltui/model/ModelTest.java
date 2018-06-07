package com.riverinnovations.saltui.model;

import com.riverinnovations.saltui.model.user.User;
import com.riverinnovations.saltui.model.user.Users;
import com.riverinnovations.saltui.model.yaml.UserPillar;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ModelTest {
    // TODO

    @Test
    public void testSave() throws Exception {
        User one = new User();
        one.setName("one");
        one.setGecosFullname("The first user");
        one.setPasswordPlain("secret1");

        User two = new User();
        two.setName("two");
        two.setGecosFullname("The second user");
        two.setPasswordPlain("secret2");
        two.setAbsentForce(true);

        Users users = new Users();
        users.addUser(one);
        users.addUser(two);
        assertEquals(2, users.getUsers().size());

        Path pillarPath = Paths.get("target/test/userpillar.yaml");
        Files.createDirectories(pillarPath.getParent());
        UserPillar userPillar = new UserPillar(pillarPath);
        userPillar.save(users);

        Map<String, ?> restoredUsers = userPillar.load();
        for (Map.Entry<String, ?> usersEntry: restoredUsers.entrySet()) {
            System.err.println(usersEntry);
            if (usersEntry.getValue() instanceof Map) {
                for (Map.Entry<String, Map<String, Object>> userEntry: ((Map<String, Map<String, Object>>)usersEntry.getValue()).entrySet()) {
                    System.err.println("User " + userEntry.getKey() + " => " + userEntry.getValue());
                    //User user = User.fromMap(userEntry.getValue());
                }
            }
        }
    }
}