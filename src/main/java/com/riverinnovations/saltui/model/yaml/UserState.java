package com.riverinnovations.saltui.model.yaml;

import com.riverinnovations.saltui.model.user.Users;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Represents the user data stored in a Pillar.
 */
public class UserState {

    /** The name of the file we want to manage for SaltStack State*/
    private final Path stateFilePath;

    /** The name of the pillar file that holds all the data */
    private final Path pillarFilePath;

    /**

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
     */
    public UserState(@NonNull Path stateFilePath,
                     @NonNull Path pillarFilePath) {
        this.stateFilePath = stateFilePath;
        this.pillarFilePath = pillarFilePath;
    }

    /**
     * Loads a YAML file into memory.
     * @return A map of user name to User object.
     */
    /*public Map<String, ?> load() throws IOException {
        Yaml yaml = new Yaml(new SafeConstructor());
        try (InputStream istr = Files.newInputStream(stateFilePath)) {
            Map<String, ?> map = yaml.load(istr);
            return map;
        }
    }*/

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
        //dumperOptions.setCanonical(true);
        // TODO - always quote strings to avoid parsing numeric data incorrectly!

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
            yaml.dump(users.getYamlPillar(), w);
        }
    }

}
