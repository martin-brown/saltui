package com.riverinnovations.saltui.model.user;

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

}
