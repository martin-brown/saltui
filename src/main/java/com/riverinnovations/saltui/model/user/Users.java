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
     * Constructs a Users object from YAML.
     */
    public Users(Map<String, ?> yamlData) {

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
     * Returns all the users.
     */
    public Collection<User> getUsers() {
        return this.userMap.values();
    }

    /**
     * Returns all the users in a structure suitable for conversion to YAML.
     * @return All the users as a structure of maps.
     */
    public Map<String, Map<String, List<Map<String, Object>>>> getYamlState() throws Exception {
        Map<String, Map<String, List<Map<String, Object>>>> usersMap = new HashMap<>();

        for (User u: this.userMap.values()) {
            LOGGER.info("Returning user " + u.getName() + " in user maps");
            usersMap.put("saltui-users-" + u.getName(), u.toStateMap());
        }
        return usersMap;
    }

    public Map<String, Map<String, Map<String, Object>>> getYamlPillar() throws Exception {
        Map<String, Map<String, Object>> usersMap = new HashMap<>();

        for (User u: this.userMap.values()) {
            LOGGER.info("Returning user " + u.getName() + " in user maps");
            usersMap.put(u.getName(), u.toPillarMap());
        }

        Map<String, Map<String, Map<String, Object>>> pillarMap = new HashMap<>();
        pillarMap.put("users", usersMap);
        return pillarMap;
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
