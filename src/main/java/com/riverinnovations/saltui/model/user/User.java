package com.riverinnovations.saltui.model.user;

import com.riverinnovations.saltui.model.BadYamlException;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
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
 *
 * Items without annotation are assumed to be NonNull (default)
 */
@DefaultQualifier(value = NonNull.class)
public class User {

    /** User name */
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
    private static final String STATE_USER_PRESENT = "user.present";
    private static final String STATE_USER_ABSENT = "user.absent";

    // Groups to be a member of
    private static final String GROUPS = "groups";

    // Default values
    private static final boolean DEFAULT_GID_FROM_NAME = false;
    private static final boolean DEFAULT_CREATEHOME = true;
    private static final boolean DEFAULT_SYSTEM = false;
    private static final boolean DEFAULT_HASH_PASSWORD = false;
    private static final boolean DEFAULT_ENFORCE_PASSWORD = true;
    private static final boolean DEFAULT_ABSENT_PURGE = false;
    private static final boolean DEFAULT_ABSENT_FORCE = false;

    /** The name of the user - must be unique */
    private final String name;

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
    private boolean gidFromName = DEFAULT_GID_FROM_NAME;

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

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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

    public void setPasswordPlain(String passwordPlain) {
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

    public @Nullable String getShell() {
        return shell;
    }

    public void setShell(@Nullable String shell) {
        this.shell = shell;
    }

    public @Nullable String getHome() {
        return home;
    }

    public void setHome(@Nullable String home) {
        this.home = home;
    }

    public boolean isCreateHome() {
        return createHome;
    }

    public void setCreateHome(boolean createHome) {
        this.createHome = createHome;
    }

    public @Nullable Integer getUid() {
        return uid;
    }

    public void setUid(@Nullable Integer uid) {
        this.uid = uid;
    }

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    public @Nullable Integer getGid() {
        return gid;
    }

    public void setGid(@Nullable Integer gid) {
        this.gid = gid;
    }

    public boolean isGidFromName() {
        return gidFromName;
    }

    public void setGidFromName(boolean gidFromName) {
        this.gidFromName = gidFromName;
    }

    public @Nullable String getGecosFullname() {
        return gecosFullname;
    }

    public void setGecosFullname(@Nullable String gecosFullname) {
        this.gecosFullname = gecosFullname;
    }

    public @Nullable String getGecosRoomNumber() {
        return gecosRoomNumber;
    }

    public void setGecosRoomNumber(@Nullable String gecosRoomNumber) {
        this.gecosRoomNumber = gecosRoomNumber;
    }

    public @Nullable String getGecosWorkphone() {
        return gecosWorkphone;
    }

    public void setGecosWorkphone(@Nullable String gecosWorkphone) {
        this.gecosWorkphone = gecosWorkphone;
    }

    public @Nullable String getGecosHomephone() {
        return gecosHomephone;
    }

    public void setGecosHomephone(@Nullable String gecosHomephone) {
        this.gecosHomephone = gecosHomephone;
    }

    public @Nullable String getGecosOther() {
        return gecosOther;
    }

    public void setGecosOther(@Nullable String gecosOther) {
        this.gecosOther = gecosOther;
    }

    public @Nullable Integer getDateLastPasswordChange() {
        return dateLastPasswordChange;
    }

    public void setDateLastPasswordChange(@Nullable Integer dateLastPasswordChange) {
        this.dateLastPasswordChange = dateLastPasswordChange;
    }

    public @Nullable Integer getMinDaysBetweenPasswordChanges() {
        return minDaysBetweenPasswordChanges;
    }

    public void setMinDaysBetweenPasswordChanges(@Nullable Integer minDaysBetweenPasswordChanges) {
        this.minDaysBetweenPasswordChanges = minDaysBetweenPasswordChanges;
    }

    public @Nullable Integer getMaxDaysBetweenPasswordChanges() {
        return maxDaysBetweenPasswordChanges;
    }

    public void setMaxDaysBetweenPasswordChanges(@Nullable Integer maxDaysBetweenPasswordChanges) {
        this.maxDaysBetweenPasswordChanges = maxDaysBetweenPasswordChanges;
    }

    public @Nullable Integer getInactDaysBeforeLocked() {
        return inactDaysBeforeLocked;
    }

    public void setInactDaysBeforeLocked(@Nullable Integer inactDaysBeforeLocked) {
        this.inactDaysBeforeLocked = inactDaysBeforeLocked;
    }

    public @Nullable Integer getWarnDaysBeforeMaxDaysBetweenPasswordChanges() {
        return warnDaysBeforeMaxDaysBetweenPasswordChanges;
    }

    public void setWarnDaysBeforeMaxDaysBetweenPasswordChanges(@Nullable Integer warnDaysBeforeMaxDaysBetweenPasswordChanges) {
        this.warnDaysBeforeMaxDaysBetweenPasswordChanges = warnDaysBeforeMaxDaysBetweenPasswordChanges;
    }

    public @Nullable Integer getDateExpire() {
        return dateExpire;
    }

    public void setDateExpire(@Nullable Integer dateExpire) {
        this.dateExpire = dateExpire;
    }

    public @Nullable String getWinHomedrive() {
        return winHomedrive;
    }

    public void setWinHomedrive(@Nullable String winHomedrive) {
        this.winHomedrive = winHomedrive;
    }

    public @Nullable String getWinProfile() {
        return winProfile;
    }

    public void setWinProfile(@Nullable String winProfile) {
        this.winProfile = winProfile;
    }

    public @Nullable String getWinLogonscript() {
        return winLogonscript;
    }

    public void setWinLogonscript(@Nullable String winLogonscript) {
        this.winLogonscript = winLogonscript;
    }

    public @Nullable String getWinDescription() {
        return winDescription;
    }

    public void setWinDescription(@Nullable String winDescription) {
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

    public void setGroups(@Nullable Collection<String> groups) {
        this.groups.clear();
        if (groups != null) {
            this.groups.addAll(groups);
        }
    }

    /**
     * Equals method generated by IntelliJ
     * @param o Object to compare
     * @return True if o equals this
     */
    @Override
    public boolean equals(@Nullable Object o) {
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
    private void addProperty(final List<Map<String, @Nullable Object>> seq,
                             final String key,
                             @Nullable final Object value) {
        final Map<String, @Nullable Object> map = new HashMap<>(1);
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
    private void addIfNotNullOrEmpty(final List<Map<String, @Nullable Object>> seq,
                                     final String key,
                                     @Nullable final Object value) {
        if (value != null) {
            if (value instanceof Collection) {
                if (!((Collection)value).isEmpty()) {
                    this.addProperty(seq, key, value);
                }
            }
            else {
                this.addProperty(seq, key, value);
            }
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
    private void addIfNotDefault(final List<Map<String, @Nullable Object>> seq,
                                 final String key,
                                 final boolean value,
                                 final boolean defaultValue) {
        if (value != defaultValue) {
            this.addProperty(seq, key, value);
        }
    }

    /**
     * Converts the contents into a map suitable for a Salt State entry.
     * @return The map of bean properties to create the entry for one user in a Salt State file (.sls)
     */
    public Map<String, List<Map<String, @Nullable Object>>> toStateMap() throws Exception {

        // Map of properties of this object, with the correct salt name as per
        // https://docs.saltstack.com/en/latest/ref/states/all/salt.states.user.html
        // Store extra properties in a test.nop map in the same userStateMap
        List<Map<String, @Nullable Object>> state = new ArrayList<>();
        this.addProperty(state, NAME, this.name);

        if (this.present) {

            // UID and GID handling
            this.addIfNotNullOrEmpty(state, UID, this.uid);
            this.addIfNotNullOrEmpty(state, GID, this.gid);
            this.addIfNotDefault(state, GID_FROM_NAME, this.gidFromName, DEFAULT_GID_FROM_NAME);
            this.addIfNotDefault(state, SYSTEM, this.system, DEFAULT_SYSTEM);

            // Home directory. Note parent of home directory must always exist.
            this.addIfNotNullOrEmpty(state, HOME, this.home);
            this.addIfNotDefault(state,
                    CREATEHOME, this.createHome, DEFAULT_CREATEHOME);

            // Password handling - reference value in pillar
            this.addIfNotDefault(state, HASH_PASSWORD, this.hashPassword, DEFAULT_HASH_PASSWORD);
            this.addIfNotDefault(state, ENFORCE_PASSWORD, this.enforcePassword, DEFAULT_ENFORCE_PASSWORD);
            this.addIfNotNullOrEmpty(state, PASSWORD, String.format(PASSWORD_PILLAR_REF, this.name));

            // User's shell
            this.addIfNotNullOrEmpty(state, SHELL, this.shell);

            // GECOS fields
            this.addIfNotNullOrEmpty(state, FULLNAME, this.gecosFullname);
            this.addIfNotNullOrEmpty(state, ROOMNUMBER, this.gecosRoomNumber);
            this.addIfNotNullOrEmpty(state, WORKPHONE, this.gecosWorkphone);
            this.addIfNotNullOrEmpty(state, HOMEPHONE, this.gecosHomephone);
            this.addIfNotNullOrEmpty(state, OTHER, this.gecosOther);

            // Shadow attributes
            this.addIfNotNullOrEmpty(state, DATE, this.dateLastPasswordChange);
            this.addIfNotNullOrEmpty(state, MINDAYS, this.minDaysBetweenPasswordChanges);
            this.addIfNotNullOrEmpty(state, MAXDAYS, this.maxDaysBetweenPasswordChanges);
            this.addIfNotNullOrEmpty(state, INACTDAYS, this.inactDaysBeforeLocked);
            this.addIfNotNullOrEmpty(state, WARNDAYS, this.warnDaysBeforeMaxDaysBetweenPasswordChanges);
            this.addIfNotNullOrEmpty(state, EXPIRE, this.dateExpire);

            // Windows
            this.addIfNotNullOrEmpty(state, WIN_HOMEDRIVE, this.winHomedrive);
            this.addIfNotNullOrEmpty(state, WIN_PROFILE, this.winProfile);
            this.addIfNotNullOrEmpty(state, WIN_LOGONSCRIPT, this.winLogonscript);
            this.addIfNotNullOrEmpty(state, WIN_DESCRIPTION, this.winDescription);

            // Groups
            this.addIfNotNullOrEmpty(state, GROUPS, this.groups);
        }
        else {
            // user.absent properties
            this.addIfNotDefault(state, PURGE, this.absentPurge, DEFAULT_ABSENT_PURGE);
            this.addIfNotDefault(state, FORCE, this.absentForce, DEFAULT_ABSENT_FORCE);
        }

        // Wrap the object properties in present/absent commands
        Map<String, List<Map<String, @Nullable Object>>> stateMap = new HashMap<>();
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
    public Map<String, @Nullable Object>  toPillarMap() {
        final Map<String, @Nullable Object> pillarMap = new HashMap<>();

        pillarMap.put(NAME, this.name);

        // UID and GID handling
        pillarMap.put(UID, this.uid);
        pillarMap.put(GID, this.gid);
        pillarMap.put(GID_FROM_NAME, this.gidFromName);
        pillarMap.put(SYSTEM, this.system);

        // Home directory. Note parent of home directory must always exist.
        pillarMap.put(HOME, this.home);
        pillarMap.put(CREATEHOME, this.createHome);

        // Password handling
        pillarMap.put(HASH_PASSWORD, this.hashPassword);
        pillarMap.put(ENFORCE_PASSWORD, this.enforcePassword);
        pillarMap.put(PASSWORD, this.passwordPlain);

        // User's shell
        pillarMap.put(SHELL, this.shell);

        // GECOS fields
        pillarMap.put(FULLNAME, this.gecosFullname);
        pillarMap.put(ROOMNUMBER, this.gecosRoomNumber);
        pillarMap.put(WORKPHONE, this.gecosWorkphone);
        pillarMap.put(HOMEPHONE, this.gecosHomephone);
        pillarMap.put(OTHER, this.gecosOther);

        // Shadow attributes
        pillarMap.put(DATE, this.dateLastPasswordChange);
        pillarMap.put(MINDAYS, this.minDaysBetweenPasswordChanges);
        pillarMap.put(MAXDAYS, this.maxDaysBetweenPasswordChanges);
        pillarMap.put(INACTDAYS, this.inactDaysBeforeLocked);
        pillarMap.put(WARNDAYS, this.warnDaysBeforeMaxDaysBetweenPasswordChanges);
        pillarMap.put(EXPIRE, this.dateExpire);

        // Windows
        pillarMap.put(WIN_HOMEDRIVE, this.winHomedrive);
        pillarMap.put(WIN_PROFILE, this.winProfile);
        pillarMap.put(WIN_LOGONSCRIPT, this.winLogonscript);
        pillarMap.put(WIN_DESCRIPTION, this.winDescription);

        // user.absent properties
        pillarMap.put(PURGE, this.absentPurge);
        pillarMap.put(FORCE, this.absentForce);

        return pillarMap;
    }
    
    /**
     * Utility function called from fromPillarMap() to set properties on a User from a Map.
     */
    private static void setProperties(User user, Map<Object, @Nullable Object> map) throws BadYamlException {
        if (map != null) {
            for (Map.Entry<Object, @Nullable Object> entry : map.entrySet()) {
                if (entry.getKey() == null) {
                    throw new BadYamlException("key is null");
                }

                String key = entry.getKey().toString();
                @Nullable Object value = entry.getValue();

                try {
                    switch (key) {
                        case NAME:
                            // Ignore - already set in constructor
                            break;
                        case UID:
                            user.uid = (Integer) value;
                            break;
                        case GID:
                            user.gid = (Integer) value;
                            break;
                        case GID_FROM_NAME:
                            user.gidFromName = (value == null ? DEFAULT_GID_FROM_NAME : (Boolean) value);
                            break;
                        case SYSTEM:
                            user.system = (value == null ? DEFAULT_SYSTEM : (Boolean) value);
                            break;
                        case HOME:
                            user.home = (String) value;
                            break;
                        case CREATEHOME:
                            user.createHome = (value == null ? DEFAULT_CREATEHOME : (Boolean) value);
                            break;
                        case HASH_PASSWORD:
                            user.hashPassword = (value == null ? DEFAULT_HASH_PASSWORD : (Boolean) value);
                            break;
                        case ENFORCE_PASSWORD:
                            user.enforcePassword = (value == null ? DEFAULT_ENFORCE_PASSWORD : (Boolean) value);
                            break;
                        case PASSWORD:
                            user.passwordPlain = (String) value;
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
                            user.absentPurge = (value == null ? DEFAULT_ABSENT_PURGE : (Boolean) value);
                            break;
                        case FORCE:
                            user.absentForce = (value == null ? DEFAULT_ABSENT_FORCE : (Boolean) value);
                            break;
                        default:
                            throw new BadYamlException("Unknown user key: " + key);
                    }
                }
                catch (ClassCastException e) {
                    @MonotonicNonNull String type = null;
                    if (value != null) {
                        type = value.getClass().getName();
                    }
                    throw new BadYamlException("Bad type for key " + key + " with value " + value
                            + ": cannot convert from type " + type
                            + ": " + e.getMessage(), e);
                }
            }
        }
    }
    
    /**
     * Constructs a bean from the contents of a series of maps.
     * @param pillarMap Map of properties to construct the state from
     */
    public static User fromPillarMap(Map<Object, @Nullable Object> pillarMap)
            throws BadYamlException {

        // Find the user's name
        @Nullable Object oName = pillarMap.get(User.NAME);
        if (oName == null) {
            throw new BadYamlException("No name for User in pillar data");
        }

        // Create the user
        User user = new User(oName.toString());

        // Set the object properties from each possible entry
        setProperties(user, pillarMap);

        return user;
    }

}
