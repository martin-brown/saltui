package com.riverinnovations.saltui.model.yaml;

import com.riverinnovations.saltui.model.BadYamlException;
import com.riverinnovations.saltui.model.ModelException;
import com.riverinnovations.saltui.model.gpg.GpgEncryptor;
import com.riverinnovations.saltui.model.user.User;
import com.riverinnovations.saltui.model.user.Users;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;

/**
 * Represents the user data stored in a Pillar.
 *
 * Items without annotation are assumed to be NonNull (default)
 */
@DefaultQualifier(value = NonNull.class)
public class UserState {

    /** The name of the file we want to manage for SaltStack State*/
    private final Path stateFilePath;

    /** The name of the pillar file that holds all the data */
    private final Path pillarFilePath;

    /** The name of the GPG key file */
    private final Path gpgKeyFilePath;

    /**
     * Constructor.
     * @param stateFilePath The path to the file that we're going to manage.
     *                     Must not be null.
     *                     Must be readable and writable.
     *                     Parent directory must be readable and writable.
     * @param pillarFilePath The path to the file that will hold all
     *                       the data about the stuff we're managing.
     *                       Must not be null.
     *                       Must be readable and writable.
     *                       Parent directory must be readable and writable.
     * @param gpgKeyFilePath The path to the file holding the GPG public key
     *                       to encrypt sensitive data.
     *                       Must not be null.
     */
    public UserState(Path stateFilePath,
                     Path pillarFilePath,
                     Path gpgKeyFilePath) {
        this.stateFilePath = stateFilePath;
        this.pillarFilePath = pillarFilePath;
        this.gpgKeyFilePath = gpgKeyFilePath;
    }

    /**
     * Casts the object to a users map.
     * @param oUsersMap Object holding users map.
     * @return Map.
     */
    @SuppressWarnings("unchecked")
    private Map<@Nullable Object, @Nullable Object> castObjectToUsersMap(Object oUsersMap) {
        return (Map<@Nullable Object, @Nullable Object>)oUsersMap;
    }

    /**
     * Casts the object to a user map.
     * @param oUserMap Object holding user map.
     * @return Map.
     */
    @SuppressWarnings("unchecked")
    private Map<@Nullable Object, @Nullable Object> castObjectToUserMap(Object oUserMap){
        return (Map<@Nullable Object, @Nullable Object>) oUserMap;
    }

    /**
     * Loads a YAML file into memory.
     * @return A map of user name to User object.
     */
    public Users load() throws IOException, ModelException {
        Users users = new Users();

        Yaml yaml = new Yaml(new SafeConstructor());
        try (InputStream istr = Files.newInputStream(pillarFilePath)) {
            Map<String, ?> map = yaml.load(istr);

            if (map != null) {
                if (map.containsKey("users")) {
                    @Nullable Object oUsersMap = map.get("users");
                    if (oUsersMap == null) {
                        throw new BadYamlException("Value of users key was null");
                    }
                    else if (!(oUsersMap instanceof Map)) {
                        throw new BadYamlException("Cannot find users map in pillar");
                    }
                    else {
                        Map<@Nullable Object, @Nullable Object> usersMap = this.castObjectToUsersMap(oUsersMap);

                        for (Map.Entry<@Nullable Object, @Nullable Object> usersEntry : usersMap.entrySet()) {
                            @Nullable Object oName = usersEntry.getKey();
                            if (oName == null) {
                                throw new IOException("User entry key is null");
                            }
                            else {
                                String name = oName.toString();

                                @Nullable Object value = usersEntry.getValue();
                                if (!(value instanceof Map)) {
                                    throw new IOException("Value for user '" + name + " is not Map");
                                }
                                else {
                                    Map<@Nullable Object, @Nullable Object> userMap = this.castObjectToUserMap(value);
                                    User user = User.fromPillarMap(userMap);
                                    users.addUser(user);
                                }
                            }
                        }
                    }
                }
            }

            return users;
        }
    }

    /**
     * Saves users to a YAML file.
     * @param users The set of users to save.
     * @throws Exception If something goes wrong (TODO tidy exceptions!)
     */
    public void save(Users users) throws Exception {

        // Set the options to create readable YAML that this parser will cope with
        // Notably we don't want to split lines as otherwise string entries
        // may not be parsable.
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setIndent(2);
        dumperOptions.setSplitLines(false);
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
        // TODO - always quote strings to avoid parsing numeric data incorrectly!

        // Encryption settings
        GpgEncryptor encryptor = new GpgEncryptor(this.gpgKeyFilePath);

        Yaml yaml = new Yaml(dumperOptions);
        try (Writer w = Files.newBufferedWriter(stateFilePath,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            yaml.dump(users.getYamlState(), w);
        }

        try (Writer w = Files.newBufferedWriter(pillarFilePath,
                                                StandardCharsets.UTF_8,
                                                StandardOpenOption.CREATE,
                                                StandardOpenOption.TRUNCATE_EXISTING)) {
            yaml.dump(users.getYamlPillar(encryptor), w);
        }
    }

}
