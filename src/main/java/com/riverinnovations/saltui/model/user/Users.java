package com.riverinnovations.saltui.model.user;

import com.riverinnovations.saltui.model.DuplicateNameException;
import com.riverinnovations.saltui.model.UnknownUserException;
import com.riverinnovations.saltui.model.gpg.GpgEncryptionException;
import com.riverinnovations.saltui.model.gpg.GpgEncryptor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents the users managed by the system.
 *
 * Items without annotation are assumed to be NonNull (default)
 */
@DefaultQualifier(value = NonNull.class)
public class Users {

    /** Maps name to user */
    private final Map<String, User> userMap = new ConcurrentHashMap<>();

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
    public void addUser(User user) throws DuplicateNameException {
        if (this.userMap.containsKey(user.getName())) {
            throw new DuplicateNameException("User '" + user.getName() + "' already exists!");
        }
        else {
            this.userMap.put(user.getName(), user);
        }
    }

    /**
     * Deletes the user from the set of users.
     */
    public void deleteUser(String username) throws UnknownUserException {
        if (this.userMap.remove(username) == null) {
            throw new UnknownUserException("Cannot find user with name '" + username + "' to delete.");
        }
    }

    /**
     * Returns the user with the given name.
     * @param name The name of the user to return
     * @return The user, or null if the user doesn't exist.
     */
    public @Nullable User getUser(String name) {
        return this.userMap.get(name);
    }

    /**
     * Returns all the users.
     */
    public Collection<User> getUsers() {
        return this.userMap.values();
    }

    /**
     * Returns all the users in a structure suitable for conversion to YAML for a salt state.
     * @return All the users as a structure of maps.
     */
    public Map<String, Map<String, List<Map<String, @Nullable Object>>>> getYamlState() throws Exception {
        Map<String, Map<String, List<Map<String, @Nullable Object>>>> usersMap = new HashMap<>();

        for (User u: this.userMap.values()) {
            usersMap.put("saltui-users-" + u.getName(), u.toStateMap());
        }
        return usersMap;
    }

    /**
     * Returns all the users in a structure suitable for conversion to YAML for a Salt Pillar.
     * @return All the users as a structure of maps.
     */
    public Map<String, Map<String, Map<String, @Nullable Object>>> getYamlPillar(GpgEncryptor encryptor)
    throws GpgEncryptionException {

        Map<String, Map<String, @Nullable Object>> usersMap = new HashMap<>();

        for (User u: this.userMap.values()) {
            usersMap.put(u.getName(), u.toPillarMap(encryptor));
        }

        Map<String, Map<String, Map<String, @Nullable Object>>> pillarMap = new HashMap<>();
        pillarMap.put("users", usersMap);
        return pillarMap;
    }

    /**
     * Used to construct the users map from YAML.
     * Clears the existing contents of the map.
     * @param users The users to add to the map.
     */
    public void setUsers(Collection<User> users) throws DuplicateNameException {
        this.userMap.clear();
        for (User u: users) {
            this.addUser(u);
        }
    }

}
