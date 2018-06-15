package com.riverinnovations.saltui.model;

import com.riverinnovations.saltui.model.user.User;
import com.riverinnovations.saltui.model.user.Users;
import com.riverinnovations.saltui.model.yaml.UserState;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ModelTest {
    // TODO

    @Test
    public void testSave() throws Exception {
        User one = new User("one");
        one.setGecosFullname("The first user");
        one.setPasswordPlain("secret1");
        List<String> oneGroups = new ArrayList<>();
        oneGroups.add("g-one");
        oneGroups.add("g-two");
        one.setGroups(oneGroups);

        User two = new User("two");
        two.setGecosFullname("The second user");
        two.setPasswordPlain("secret2");
        two.setAbsentForce(true);

        Users users = new Users();
        users.addUser(one);
        users.addUser(two);
        assertEquals(2, users.getUsers().size());

        Path statePath = Paths.get("target/test/userstate.yaml");
        Path pillarPath = Paths.get("target/test/userpillar.yaml");
        Files.createDirectories(pillarPath.getParent());
        UserState userState = new UserState(statePath, pillarPath);
        userState.save(users);

        // Try to read them back in again
        Users restoredUsers = userState.load();

        /*Map<String, ?> restoredUsers = userState.load();
        for (Map.Entry<String, ?> usersEntry: restoredUsers.entrySet()) {
            System.err.println(usersEntry);
            if (usersEntry.getValue() instanceof Map) {
                for (Map.Entry<String, Map<String, Object>> userEntry: ((Map<String, Map<String, Object>>)usersEntry.getValue()).entrySet()) {
                    System.err.println("User " + userEntry.getKey() + " => " + userEntry.getValue());
                    //User user = User.fromMap(userEntry.getValue());
                }
            }
        }*/
    }
}
