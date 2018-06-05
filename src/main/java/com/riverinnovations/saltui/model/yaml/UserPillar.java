package com.riverinnovations.saltui.model.yaml;

import com.riverinnovations.saltui.model.user.Users;
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
 */
public class UserPillar {

    /** The name of the file we want to manage */
    private final Path yamlFilePath;

    /**
     * Constructor.
     * @param yamlFilePath The path to the file that we're going to manage.
     *                     Must not be null.
     *                     Must be readable and writable.
     *                     Parent directory must be readable and writable.
     */
    public UserPillar(Path yamlFilePath) {
        if (yamlFilePath == null) throw new IllegalArgumentException("yamlFilePath is null");
        this.yamlFilePath = yamlFilePath;
    }

    /**
     * Loads a YAML file into memory.
     * @return A Users object that we want to load.
     */
    public Map<String, ?> load() throws IOException {
        Yaml yaml = new Yaml(new SafeConstructor());
        try (InputStream istr = Files.newInputStream(yamlFilePath)) {
            Map<String, ?> map = yaml.load(istr);
            return map;
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
        dumperOptions.setIndent(4);
        dumperOptions.setSplitLines(false);
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(dumperOptions);
        try (Writer w = Files.newBufferedWriter(yamlFilePath,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            yaml.dump(users.getYamlUsers(), w);
        }
    }

}
