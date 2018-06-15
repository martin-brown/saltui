package com.riverinnovations.saltui.model.user;

import com.riverinnovations.saltui.model.BadYamlException;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Class models a User who is to be granted logon permissions to a system managed by SaltStack.
 *
 * Field names are based on the SaltStack field names, expanded to avoid confusion.
 */
public class User {

    public class YamlEntries {
        private final Map<String, Object> present;
        private final Map<String, Object> absent;
        private final Map<String, Object> nop;

        /**
         * Constructor for YamlEntries for this User.
         * One of present or absent must not be null (IllegalArgumentException)
         * @param present The properties if this user is to be present.
         * @param absent The properties if this user is to be absent.
         * @param nop The properties that are not elsewhere. Must not be null
         *            (IllegalArgumentException).
         */
        public YamlEntries(Map<String, Object> present,
                           Map<String, Object> absent,
                           Map<String, Object> nop) {
            if (present == null && absent == null) throw new IllegalArgumentException("Both present and absent are null");
            if (nop == null) throw new IllegalArgumentException("nop is null");
            this.present = present;
            this.absent = absent;
            this.nop = nop;
        }
        public Map<String, Object> getPresent() {
            return present;
        }
        public Map<String, Object> getAbsent() {
            return absent;
        }
        public Map<String, Map<String, Object>> getNopMap() {
            Map<String, Map<String, Object>> nopMap = new HashMap<>();
            nopMap.put(User.STATE_NOP, nop);
            return nopMap;
        }
        public Map<String, Map<String, Object>> getStateMap() {
            Map<String, Map<String, Object>> stateMap = new HashMap<>();
            if (present != null) {
                stateMap.put(User.STATE_USER_PRESENT, present);
            }
            else {
                stateMap.put(User.STATE_USER_ABSENT, absent);
            }
            return stateMap;
        }
    }

    public static final String NAME = "name";

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(User.class);

    // Names for SaltStack user state parameters
    private static final String UID = "uid";
    private static final String GID = "gid";
    private static final String GID_FROM_NAME = "gid_from_name";
    private static final String SYSTEM = "system";
    private static final String HOME = "home";
    private static final String CREATEHOME = "createhome";
    private static final String HASH_PASSWORD = "hash_password";
    private static final String ENFORCE_PASSWORD = "enforce_password";
    private static final String PASSWORD = "password";
    private static final String PASSWORD_PILLAR_REF = "{{ pillar['users']['%s']['password'] }}";
    private static final String SHELL = "shell";
    private static final String FULLNAME = "fullname";
    private static final String ROOMNUMBER = "roomnumber";
    private static final String WORKPHONE = "workphone";
    private static final String HOMEPHONE = "homephone";
    private static final String OTHER = "other";
    private static final String DATE = "date";
    private static final String MINDAYS = "mindays";
    private static final String MAXDAYS = "maxdays";
    private static final String INACTDAYS = "inactdays";
    private static final String WARNDAYS = "warndays";
    private static final String EXPIRE = "expire";
    private static final String WIN_HOMEDRIVE = "win_homedrive";
    private static final String WIN_PROFILE = "win_profile";
    private static final String WIN_LOGONSCRIPT = "win_logonscript";
    private static final String WIN_DESCRIPTION = "win_description";
    private static final String PURGE = "purge";
    private static final String FORCE = "force";

    // States
    public static final String STATE_USER_PRESENT = "user.present";
    public static final String STATE_USER_ABSENT = "user.absent";
    public static final String STATE_NOP = "test.nop";

    // Groups to be a member of
    public static final String GROUPS = "groups";

    // Default values
    private static final boolean DEFAULT_CREATEHOME = true;
    private static final boolean DEFAULT_SYSTEM = false;
    private static final boolean DEFAULT_HASH_PASSWORD = false;
    private static final boolean DEFAULT_ENFORCE_PASSWORD = true;
    private static final boolean DEFAULT_ABSENT_PURGE = false;
    private static final boolean DEFAULT_ABSENT_FORCE = false;

    /** The name of the user - must be unique */
    private @NonNull String name;

    /** Whether this user should exist; default true */
    private boolean present = true;

    /** The password hash for use on Linux, FreeBSD, NetBSD, OpenBSD, Solaris machines */
    private @Nullable String passwordHash;

    /** The plain-text password for use on Windows machines, or to hash for UNIX machines */
    private @Nullable String passwordPlain;

    /** Whether to hash the plaintext password for UNIX machines */
    private boolean hashPassword = DEFAULT_HASH_PASSWORD;

    /** false implies if user has changed password on the machine then don't change it */
    private boolean enforcePassword = DEFAULT_ENFORCE_PASSWORD;

    /** The shell for this user (can be null) */
    private @Nullable String shell;

    /** The home directory for this user (can be null in which case default location is used) */
    private @Nullable String home;

    /** Whether the home directory will be created if it doesn't exit. Default is true. */
    private boolean createHome = true;

    /** The UID for this user (can be null in which case next available GID will be used) */
    private @Nullable Integer uid;

    /** Choose a UI in the range of FIRST_SYSTEM_UID and LAST_SYSTEM_UID if true */
    private boolean system = DEFAULT_SYSTEM;

    /** The GID for this user (can be null in which case next available GID will be used) */
    private @Nullable Integer gid;

    /** Whether to use the GID from the group with the same name as the user */
    private boolean gidFromName;

    /** The full name of the user for display (can be null) (Linux, BSD, MacOS only) */
    private @Nullable String gecosFullname;

    /** The user's room number (can be null) (Linux, BSD only) */
    private @Nullable String gecosRoomNumber;

    /** The user's workphone (can be null) (Linux, BSD only) */
    private @Nullable String gecosWorkphone;

    /** The user's home phone (can be null) (Linux, BSD only) */
    private @Nullable String gecosHomephone;

    /** Other user data (can be null) (Linux, BSD only) */
    private @Nullable String gecosOther;

    /** Date of last password change, in days since epoch (can be null) (Linux only) */
    private @Nullable Integer dateLastPasswordChange;

    /** Minimum number of days between password changes (can be null) (Linux only) */
    private @Nullable Integer minDaysBetweenPasswordChanges;

    /** Maximum number of days between password changes (can be null) (Linux only) */
    private @Nullable Integer maxDaysBetweenPasswordChanges;

    /** Number of days after a password expires before an account is locked (can be null) (Linux only) */
    private @Nullable Integer inactDaysBeforeLocked;

    /** Number of days before maxDaysBetweenPasswordChanges to warn users (can be null) (Linux only) */
    private @Nullable Integer warnDaysBeforeMaxDaysBetweenPasswordChanges;

    /** Date that account expires, in days since epoch (can be null) (Linux only) */
    private @Nullable Integer dateExpire;

    /** Drive letter for the home directory (can be null in which case home will be on UNC path) (Windows only) */
    private @Nullable String winHomedrive;

    /** Custom profile directory of the user. (can be null in which case uses default of underlying system) (Windows only) */
    private @Nullable String winProfile;

    /** Full path to logon script to run when user logs in (can be null) (Windows only) */
    private @Nullable String winLogonscript;

    /** Brief description of the users account (can be null) (Windows only) */
    private @Nullable String winDescription;

    /** Deletion setting - purge all files */
    private boolean absentPurge = DEFAULT_ABSENT_PURGE;

    /** Deletion setting - force deletion */
    private boolean absentForce = DEFAULT_ABSENT_FORCE;

    /** Groups that this user is a member of */
    private final List<String> groups = new ArrayList<>();

    public @NonNull String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public @Nullable String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(@Nullable String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public @Nullable String getPasswordPlain() {
        return passwordPlain;
    }

    public void setPasswordPlain(@NonNull String passwordPlain) {
        this.passwordPlain = passwordPlain;
    }

    public boolean isHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(boolean hashPassword) {
        this.hashPassword = hashPassword;
    }

    public boolean isEnforcePassword() {
        return enforcePassword;
    }

    public void setEnforcePassword(boolean enforcePassword) {
        this.enforcePassword = enforcePassword;
    }

    public String getShell() {
        return shell;
    }

    public void setShell(String shell) {
        this.shell = shell;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public boolean isCreateHome() {
        return createHome;
    }

    public void setCreateHome(boolean createHome) {
        this.createHome = createHome;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public boolean isGidFromName() {
        return gidFromName;
    }

    public void setGidFromName(boolean gidFromName) {
        this.gidFromName = gidFromName;
    }

    public String getGecosFullname() {
        return gecosFullname;
    }

    public void setGecosFullname(String gecosFullname) {
        this.gecosFullname = gecosFullname;
    }

    public String getGecosRoomNumber() {
        return gecosRoomNumber;
    }

    public void setGecosRoomNumber(String gecosRoomNumber) {
        this.gecosRoomNumber = gecosRoomNumber;
    }

    public String getGecosWorkphone() {
        return gecosWorkphone;
    }

    public void setGecosWorkphone(String gecosWorkphone) {
        this.gecosWorkphone = gecosWorkphone;
    }

    public String getGecosHomephone() {
        return gecosHomephone;
    }

    public void setGecosHomephone(String gecosHomephone) {
        this.gecosHomephone = gecosHomephone;
    }

    public String getGecosOther() {
        return gecosOther;
    }

    public void setGecosOther(String gecosOther) {
        this.gecosOther = gecosOther;
    }

    public Integer getDateLastPasswordChange() {
        return dateLastPasswordChange;
    }

    public void setDateLastPasswordChange(Integer dateLastPasswordChange) {
        this.dateLastPasswordChange = dateLastPasswordChange;
    }

    public Integer getMinDaysBetweenPasswordChanges() {
        return minDaysBetweenPasswordChanges;
    }

    public void setMinDaysBetweenPasswordChanges(Integer minDaysBetweenPasswordChanges) {
        this.minDaysBetweenPasswordChanges = minDaysBetweenPasswordChanges;
    }

    public Integer getMaxDaysBetweenPasswordChanges() {
        return maxDaysBetweenPasswordChanges;
    }

    public void setMaxDaysBetweenPasswordChanges(Integer maxDaysBetweenPasswordChanges) {
        this.maxDaysBetweenPasswordChanges = maxDaysBetweenPasswordChanges;
    }

    public Integer getInactDaysBeforeLocked() {
        return inactDaysBeforeLocked;
    }

    public void setInactDaysBeforeLocked(Integer inactDaysBeforeLocked) {
        this.inactDaysBeforeLocked = inactDaysBeforeLocked;
    }

    public Integer getWarnDaysBeforeMaxDaysBetweenPasswordChanges() {
        return warnDaysBeforeMaxDaysBetweenPasswordChanges;
    }

    public void setWarnDaysBeforeMaxDaysBetweenPasswordChanges(Integer warnDaysBeforeMaxDaysBetweenPasswordChanges) {
        this.warnDaysBeforeMaxDaysBetweenPasswordChanges = warnDaysBeforeMaxDaysBetweenPasswordChanges;
    }

    public Integer getDateExpire() {
        return dateExpire;
    }

    public void setDateExpire(Integer dateExpire) {
        this.dateExpire = dateExpire;
    }

    public String getWinHomedrive() {
        return winHomedrive;
    }

    public void setWinHomedrive(String winHomedrive) {
        this.winHomedrive = winHomedrive;
    }

    public String getWinProfile() {
        return winProfile;
    }

    public void setWinProfile(String winProfile) {
        this.winProfile = winProfile;
    }

    public String getWinLogonscript() {
        return winLogonscript;
    }

    public void setWinLogonscript(String winLogonscript) {
        this.winLogonscript = winLogonscript;
    }

    public String getWinDescription() {
        return winDescription;
    }

    public void setWinDescription(String winDescription) {
        this.winDescription = winDescription;
    }

    public boolean isAbsentPurge() {
        return absentPurge;
    }

    public void setAbsentPurge(boolean absentPurge) {
        this.absentPurge = absentPurge;
    }

    public boolean isAbsentForce() {
        return absentForce;
    }

    public void setAbsentForce(boolean absentForce) {
        this.absentForce = absentForce;
    }

    public List<String> getGroups() {
        return Collections.unmodifiableList(groups);
    }

    public void setGroups(Collection<String> groups) {
        this.groups.clear();
        this.groups.addAll(groups);
    }

    /**
     * Equals method generated by IntelliJ
     * @param o Object to compare
     * @return True if o equals this
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return hashPassword == user.hashPassword &&
                enforcePassword == user.enforcePassword &&
                createHome == user.createHome &&
                system == user.system &&
                gidFromName == user.gidFromName &&
                Objects.equals(name, user.name) &&
                Objects.equals(present, user.present) &&
                Objects.equals(passwordHash, user.passwordHash) &&
                Objects.equals(passwordPlain, user.passwordPlain) &&
                Objects.equals(shell, user.shell) &&
                Objects.equals(home, user.home) &&
                Objects.equals(uid, user.uid) &&
                Objects.equals(gid, user.gid) &&
                Objects.equals(gecosFullname, user.gecosFullname) &&
                Objects.equals(gecosRoomNumber, user.gecosRoomNumber) &&
                Objects.equals(gecosWorkphone, user.gecosWorkphone) &&
                Objects.equals(gecosHomephone, user.gecosHomephone) &&
                Objects.equals(gecosOther, user.gecosOther) &&
                Objects.equals(dateLastPasswordChange, user.dateLastPasswordChange) &&
                Objects.equals(minDaysBetweenPasswordChanges, user.minDaysBetweenPasswordChanges) &&
                Objects.equals(maxDaysBetweenPasswordChanges, user.maxDaysBetweenPasswordChanges) &&
                Objects.equals(inactDaysBeforeLocked, user.inactDaysBeforeLocked) &&
                Objects.equals(warnDaysBeforeMaxDaysBetweenPasswordChanges, user.warnDaysBeforeMaxDaysBetweenPasswordChanges) &&
                Objects.equals(dateExpire, user.dateExpire) &&
                Objects.equals(winHomedrive, user.winHomedrive) &&
                Objects.equals(winProfile, user.winProfile) &&
                Objects.equals(winLogonscript, user.winLogonscript) &&
                Objects.equals(winDescription, user.winDescription) &&
                Objects.equals(absentPurge, user.absentPurge) &&
                Objects.equals(absentForce, user.absentForce) &&
                Objects.equals(groups, user.groups);
    }

    /**
     * Generates a hashcode based on the contents of this object.
     * Generated by IntelliJ.
     * @return A hashcode.
     */
    @Override
    public int hashCode() {

        return Objects.hash(name,
                            present,
                            passwordHash,
                            passwordPlain,
                            hashPassword,
                            enforcePassword,
                            shell,
                            home,
                            createHome,
                            uid,
                            system,
                            gid,
                            gidFromName,
                            gecosFullname,
                            gecosRoomNumber,
                            gecosWorkphone,
                            gecosHomephone,
                            gecosOther,
                            dateLastPasswordChange,
                            minDaysBetweenPasswordChanges,
                            maxDaysBetweenPasswordChanges,
                            inactDaysBeforeLocked,
                            warnDaysBeforeMaxDaysBetweenPasswordChanges,
                            dateExpire,
                            winHomedrive,
                            winProfile,
                            winLogonscript,
                            winDescription,
                            absentPurge,
                            absentForce,
                            groups);
    }

    /**
     * Adds a property to a sequence as required by Salt State structure.
     */
    private void addProperty(@NonNull List<Map<String, Object>> seq,
                             @NonNull String key,
                             @Nullable Object value) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(key, value);
        seq.add(map);
    }

    /**
     * Utility method to add a value to a map if the value isn't null.
     * Used to filter out items that shouldn't be specified in the
     * YAML SLS file.
     * @param seq List to add property to.
     * @param key Key for value
     * @param value Value to add if it isn't null
     */
    private void addIfNotNull(List<Map<String, Object>> seq, String key, Object value) {
        if (value != null) {
            this.addProperty(seq, key, value);
        }
    }

    /**
     * Utility method to add a boolean value to a map if it isn't the default value.
     * Reduces the number of entries we make in the YAML SLS file.
     * @param seq Sequence to add to
     * @param key Key for value
     * @param value Value to add if it isn't the default value
     * @param defaultValue Default value to check against
     */
    private void addIfNotDefault(List<Map<String, Object>> seq, String key, boolean value, boolean defaultValue) {
        if (value != defaultValue) {
            this.addProperty(seq, key, value);
        }
    }

    /**
     * Converts the contents into a map suitable for a Salt State entry.
     * @return The map of bean properties to create the entry for one user in a Salt State file (.sls)
     */
    public @NonNull Map<String, List<Map<String, Object>>> toStateMap() throws Exception {

        // Map of properties of this object, with the correct salt name as per
        // https://docs.saltstack.com/en/latest/ref/states/all/salt.states.user.html
        // Store extra properties in a test.nop map in the same userStateMap
        List<Map<String, Object>> state = new ArrayList<>();
        this.addProperty(state, NAME, this.name);

        if (this.present) {

            // UID and GID handling
            this.addIfNotNull(state, UID, this.uid);
            this.addIfNotNull(state, GID, this.gid);
            this.addIfNotNull(state, GID_FROM_NAME, this.gidFromName);
            this.addIfNotDefault(state, SYSTEM, this.system, DEFAULT_SYSTEM);

            // Home directory. Note parent of home directory must always exist.
            this.addIfNotNull(state, HOME, this.home);
            this.addIfNotDefault(state,
                    CREATEHOME, this.createHome, DEFAULT_CREATEHOME);

            // Password handling - reference value in pillar
            this.addIfNotDefault(state, HASH_PASSWORD, this.hashPassword, DEFAULT_HASH_PASSWORD);
            this.addIfNotDefault(state, ENFORCE_PASSWORD, this.enforcePassword, DEFAULT_ENFORCE_PASSWORD);
            this.addIfNotNull(state, PASSWORD, String.format(PASSWORD_PILLAR_REF, this.name));

            // User's shell
            this.addIfNotNull(state, SHELL, this.shell);

            // GECOS fields
            this.addIfNotNull(state, FULLNAME, this.gecosFullname);
            this.addIfNotNull(state, ROOMNUMBER, this.gecosRoomNumber);
            this.addIfNotNull(state, WORKPHONE, this.gecosWorkphone);
            this.addIfNotNull(state, HOMEPHONE, this.gecosHomephone);
            this.addIfNotNull(state, OTHER, this.gecosOther);

            // Shadow attributes
            this.addIfNotNull(state, DATE, this.dateLastPasswordChange);
            this.addIfNotNull(state, MINDAYS, this.minDaysBetweenPasswordChanges);
            this.addIfNotNull(state, MAXDAYS, this.maxDaysBetweenPasswordChanges);
            this.addIfNotNull(state, INACTDAYS, this.inactDaysBeforeLocked);
            this.addIfNotNull(state, WARNDAYS, this.warnDaysBeforeMaxDaysBetweenPasswordChanges);
            this.addIfNotNull(state, EXPIRE, this.dateExpire);

            // Windows
            this.addIfNotNull(state, WIN_HOMEDRIVE, this.winHomedrive);
            this.addIfNotNull(state, WIN_PROFILE, this.winProfile);
            this.addIfNotNull(state, WIN_LOGONSCRIPT, this.winLogonscript);
            this.addIfNotNull(state, WIN_DESCRIPTION, this.winDescription);

            // Groups
            this.addProperty(state, GROUPS, this.groups);
        }
        else {
            // user.absent properties
            this.addIfNotDefault(state, PURGE, this.absentPurge, DEFAULT_ABSENT_PURGE);
            this.addIfNotDefault(state, FORCE, this.absentForce, DEFAULT_ABSENT_FORCE);
        }

        Map<String, List<Map<String, Object>>> stateMap = new HashMap<>();
        if (this.present) {
            stateMap.put(User.STATE_USER_PRESENT, state);
        }
        else {
            stateMap.put(User.STATE_USER_ABSENT, state);
        }
        return stateMap;
    }

    /**
     * Returns the data for this object for putting in a pillar.
     * Includes all the properties of this object, not necessarily in a 
     * format compatible with SaltStack. Used to serialize the User
     * to disk.
     * @return A map containing all the data to be used when serializing
     *         to disk.
     */
    public @NonNull List<Map<String, Object>>  toPillarMap() {
        List<Map<String, Object>> pillarSeq = new ArrayList<>();

        this.addProperty(pillarSeq, NAME, this.name);

        // UID and GID handling
        this.addIfNotNull(pillarSeq, UID, this.uid);
        this.addIfNotNull(pillarSeq, GID, this.gid);
        this.addIfNotNull(pillarSeq, GID_FROM_NAME, this.gidFromName);
        this.addIfNotDefault(pillarSeq, SYSTEM, this.system, DEFAULT_SYSTEM);

        // Home directory. Note parent of home directory must always exist.
        this.addIfNotNull(pillarSeq, HOME, this.home);
        this.addIfNotDefault(pillarSeq, CREATEHOME, this.createHome, DEFAULT_CREATEHOME);

        // Password handling
        this.addIfNotDefault(pillarSeq, HASH_PASSWORD, this.hashPassword, DEFAULT_HASH_PASSWORD);
        this.addIfNotDefault(pillarSeq, ENFORCE_PASSWORD, this.enforcePassword, DEFAULT_ENFORCE_PASSWORD);
        this.addProperty(pillarSeq, PASSWORD, this.passwordPlain);

        // User's shell
        this.addIfNotNull(pillarSeq, SHELL, this.shell);

        // GECOS fields
        this.addIfNotNull(pillarSeq, FULLNAME, this.gecosFullname);
        this.addIfNotNull(pillarSeq, ROOMNUMBER, this.gecosRoomNumber);
        this.addIfNotNull(pillarSeq, WORKPHONE, this.gecosWorkphone);
        this.addIfNotNull(pillarSeq, HOMEPHONE, this.gecosHomephone);
        this.addIfNotNull(pillarSeq, OTHER, this.gecosOther);

        // Shadow attributes
        this.addIfNotNull(pillarSeq, DATE, this.dateLastPasswordChange);
        this.addIfNotNull(pillarSeq, MINDAYS, this.minDaysBetweenPasswordChanges);
        this.addIfNotNull(pillarSeq, MAXDAYS, this.maxDaysBetweenPasswordChanges);
        this.addIfNotNull(pillarSeq, INACTDAYS, this.inactDaysBeforeLocked);
        this.addIfNotNull(pillarSeq, WARNDAYS, this.warnDaysBeforeMaxDaysBetweenPasswordChanges);
        this.addIfNotNull(pillarSeq, EXPIRE, this.dateExpire);

        // Windows
        this.addIfNotNull(pillarSeq, WIN_HOMEDRIVE, this.winHomedrive);
        this.addIfNotNull(pillarSeq, WIN_PROFILE, this.winProfile);
        this.addIfNotNull(pillarSeq, WIN_LOGONSCRIPT, this.winLogonscript);
        this.addIfNotNull(pillarSeq, WIN_DESCRIPTION, this.winDescription);

        // user.absent properties
        this.addIfNotDefault(pillarSeq, PURGE, this.absentPurge, DEFAULT_ABSENT_PURGE);
        this.addIfNotDefault(pillarSeq, FORCE, this.absentForce, DEFAULT_ABSENT_FORCE);

        return pillarSeq;
    }
    
    /**
     * Utility function called from fromPillarMap() to set properties on a User from a Map.
     */
    private static void setProperties(User user, Map<String, Object> map) throws BadYamlException {
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                try {
                    switch (key) {
                        case NAME:
                            user.name = (String) value;
                            break;
                        case UID:
                            user.uid = (Integer) value;
                            break;
                        case GID:
                            user.gid = (Integer) value;
                            break;
                        case GID_FROM_NAME:
                            user.gidFromName = (Boolean) value;
                            break;
                        case SYSTEM:
                            user.system = (Boolean) value;
                            break;
                        case HOME:
                            user.home = (String) value;
                            break;
                        case CREATEHOME:
                            user.createHome = (Boolean) value;
                            break;
                        case HASH_PASSWORD:
                            user.hashPassword = (Boolean) value;
                            break;
                        case SHELL:
                            user.shell = (String) value;
                            break;
                        case FULLNAME:
                            user.gecosFullname = (String) value;
                            break;
                        case ROOMNUMBER:
                            user.gecosRoomNumber = (String) value;
                            break;
                        case WORKPHONE:
                            user.gecosWorkphone = (String) value;
                            break;
                        case HOMEPHONE:
                            user.gecosHomephone = (String) value;
                            break;
                        case OTHER:
                            user.gecosOther = (String) value;
                            break;
                        case DATE:
                            user.dateLastPasswordChange = (Integer) value;
                            break;
                        case MINDAYS:
                            user.minDaysBetweenPasswordChanges = (Integer) value;
                            break;
                        case MAXDAYS:
                            user.maxDaysBetweenPasswordChanges = (Integer) value;
                            break;
                        case INACTDAYS:
                            user.inactDaysBeforeLocked = (Integer) value;
                            break;
                        case WARNDAYS:
                            user.warnDaysBeforeMaxDaysBetweenPasswordChanges = (Integer) value;
                            break;
                        case EXPIRE:
                            user.dateExpire = (Integer) value;
                            break;
                        case WIN_HOMEDRIVE:
                            user.winHomedrive = (String) value;
                            break;
                        case WIN_PROFILE:
                            user.winProfile = (String) value;
                            break;
                        case WIN_LOGONSCRIPT:
                            user.winLogonscript = (String) value;
                            break;
                        case WIN_DESCRIPTION:
                            user.winDescription = (String) value;
                            break;
                        case PURGE:
                            user.absentPurge = (Boolean) value;
                            break;
                        case FORCE:
                            user.absentForce = (Boolean) value;
                            break;
                        default:
                            throw new BadYamlException("Unknown user key: " + key);
                    }
                } catch (ClassCastException e) {
                    throw new BadYamlException("Bad type for key " + key + " with value " + value
                            + ": cannot convert from type " + value.getClass().getName()
                            + ": " + e.getMessage(), e);
                }
            }
        }
    }
    
    /**
     * Constructs a bean from the contents of a series of maps.
     * @param pillarMap Map of properties to construct the state from
     */
    public static @NonNull User fromPillarMap(@NonNull Map<String, Object> pillarMap)
            throws BadYamlException {
        User user = new User();

        // Set the object properties from each possible entry
        setProperties(user, pillarMap);

        return user;
    }

}
