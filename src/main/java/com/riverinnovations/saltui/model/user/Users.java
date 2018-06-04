package com.riverinnovations.saltui.model.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents the users managed by the system.
 */
public class Users {

    /** Maps name to user */
    private Map<String, User> userMap = new ConcurrentHashMap<>();

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(Users.class);

    /**
     * Default constructor.
     */
    public Users() {
        // No code
    }

    /**
     * Adds the user to the set of users.
     * @param user The user to add. Must not be null (IllegalArgumentException).
     */
    public void addUser(User user) {
        if (user == null) throw new IllegalArgumentException("User is null");
        this.userMap.put(user.getName(), user);
    }

    /**
     * Returns the user with the given name.
     * @param name The name of the user to return
     * @return The user, or null if the user doesn't exist.
     */
    public User getUser(String name) {
        return this.userMap.get(name);
    }

    /**
     * Returns all the users in a structure suitable for conversion to YAML.
     * @return All the users as a structure of maps.
     */
    public Map<String, Map<String, Map<String, Object>>> getYamlUsers() throws Exception {
        Map<String, Map<String, Map<String, Object>>> usersMaps = new HashMap<>();
        Map<String, Map<String, Object>> userMaps = new HashMap<>();
        usersMaps.put("users", userMaps);
        for (User u: this.userMap.values()) {
            LOGGER.info("Returning user " + u.getName() + " in user maps");
            userMaps.put(u.getName(), u.toMap());
        }
        return usersMaps;
    }

    /**
     * Used to construct the users map from YAML.
     * Clears the existing contents of the map.
     * @param users The users to add to the map.
     */
    public void setUsers(Collection<User> users) {
        this.userMap.clear();
        for (User u: users) {
            this.addUser(u);
        }
    }

}
