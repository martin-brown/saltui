package com.riverinnovations.saltui.model.user;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Map;

/**
 * Class models a User who is to be granted logon permissions to a system managed by SaltStack.
 *
 * Field names are based on the SaltStack field names, expanded to avoid confusion.
 */
public class User {

    /** The name of the user - must be unique */
    private String name;

    /** The password hash for use on Linux, FreeBSD, NetBSD, OpenBSD, Solaris machines */
    private String passwordHash;

    /** The plain-text password for use on Windows machines, or to hash for UNIX machines */
    private String passwordPlain;

    /** Whether to hash the plaintext password for UNIX machines */
    private boolean hashPassword = false;

    /** false implies if user has changed password on the machine then don't change it */
    private boolean enforcePassword = true;

    /** The shell for this user (can be null) */
    private String shell;

    /** The home directory for this user (can be null in which case default location is used) */
    private String home;

    /** Whether the home directory will be created if it doesn't exit. Default is true. */
    private boolean createHome = true;

    /** The UID for this user (can be null in which case next available GID will be used) */
    private Integer uid;

    /** Choose a UI in the range of FIRST_SYSTEM_UID and LAST_SYSTEM_UID if true */
    private boolean system = false;

    /** The GID for this user (can be null in which case next available GID will be used) */
    private Integer gid;

    /** Whether to use the GID from the group with the same name as the user */
    private boolean gidFromName;

    /** The full name of the user for display (can be null) (Linux, BSD, MacOS only) */
    private String gecosFullname;

    /** The user's room number (can be null) (Linux, BSD only) */
    private String gecosRoomNumber;

    /** The user's workphone (can be null) (Linux, BSD only) */
    private String gecosWorkphone;

    /** The user's home phone (can be null) (Linux, BSD only) */
    private String gecosHomephone;

    /** Other user data (can be null) (Linux, BSD only) */
    private String gecosOther;

    /** Date of last password change, in days since epoch (can be null) (Linux only) */
    private Integer dateLastPasswordChange;

    /** Minimum number of days between password changes (can be null) (Linux only) */
    private Integer minDaysBetweenPasswordChanges;

    /** Maximum number of days between password changes (can be null) (Linux only) */
    private Integer maxDaysBetweenPasswordChanges;

    /** Number of days after a password expires before an account is locked (can be null) (Linux only) */
    private Integer inactDaysBeforeLocked;

    /** Number of days before maxDaysBetweenPasswordChanges to warn users (can be null) (Linux only) */
    private Integer warnDaysBeforeMaxDaysBetweenPasswordChanges;

    /** Date that account expires, in days since epoch (can be null) (Linux only) */
    private Integer dateExpire;

    /** Drive letter for the home directory (can be null in which case home will be on UNC path) (Windows only) */
    private String winHomedrive;

    /** Custom profile directory of the user. (can be null in which case uses default of underlying system) (Windows only) */
    private String winProfile;

    /** Full path to logon script to run when user logs in (can be null) (Windows only) */
    private String winLogonscript;

    /** Brief description of the users account (can be null) (Windows only) */
    private String winDescription;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordPlain() {
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

    /**
     * Converts the contents into a map.
     * @return The map of bean properties
     */
    public Map toMap() throws Exception {
        Map<String, String> map = BeanUtils.describe(this);
        //BeanMap map = new BeanMap(this);
        map.remove("class");
        return map;
    }

    /**
     * Constructs a bean from the contents of a map.
     */
    public static User fromMap(Map<String, String> map) throws Exception {
        User user = new User();
        BeanUtils.populate(user, map);
        return user;
    }
}
