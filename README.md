saltui
==============

UI application for SaltStack.

The initial aim of this project is to provide user management via SaltStack.
Servers don't require all the facilities or complexity of something like
Active Directory. However, they do require:
* A user per administrator. We don't want all sysadmins logging in using the 
same username and password.
* Password management. It should be possible for users to change their own
password via a web UI.
* Password expiry. Some standards, such as PCI-DSS require that passwords
expire every 90 days. 
* Secure password encryption. 

## TODO
* Proper unit testing.
* Full use of Nullable / NonNull attributes
* Thread safety and locking for concurrent updates of User/Users
* Framework to manage User/Users & files on disk - autosave etc
* Correct password handling:
  * Encryption using Salt mechanisms (gpg)
  * Not trying to load password back into User object once written
  
## Objective
This application will manage a [Salt State file](https://docs.saltstack.com/en/latest/topics/tutorials/starting_states.html) 
file; format TBD, but possibly something like this:
```yaml
saltui-groups-alpha:
    group.present:
        - name: alpha
        - system: false
        
saltui-groups-beta:
    group.present:
        - name: beta
        - system: false
        
saltui-groups-gamma:
    group.absent:
        - name: gamma    
        
saltui-users-two:
    user.present:
        - gid_from_name: false
        - fullname: The second user
        - system: false
        - enforce_password: true
        - hash_password: false
        - name: two
        - createhome: true
        - password: {{ pillar['users']['two']['password'] }}
        - groups: 
          - alpha
          - beta
        
saltui-users-one:
    user.present:
        - gid_from_name: false
        - fullname: The first user
        - system: false
        - enforce_password: true
        - hash_password: false
        - name: one
        - createhome: true
        - password: {{ pillar['users']['two']['password'] }}
        
saltui-users-three:
    user.absent:
        - name: three
        - purge: true
        - force: true
```

This will create users 'one' and 'two', while deleting the account and all the files of user 'three'.

Group membership can be done in two places:
* Groups can have members, in which case the users need to be defined before the groups; or
* Users can have a list of groups they are members of, in which case the groups need to be defined before the users.

Note that UNIX groups cannot be members of another group, so in general this won't be supported. 

The application will use [Salt Encryption](https://docs.saltstack.com/en/latest/topics/pillar/index.html#pillar-encryption)
to encrypt the passwords stored in the pillar. If Windows clients are to be supported then it will be necessary to keep 
the passwords in (encrypted) plain text and set `hash_password: false`; otherwise the passwords can be the unix password 
hash value for extra security.

Regarding deleting users and purging their files:
* Need to test this to see what it does on various platforms.
* Even if a user cannot log in via /etc/password it may be possible for them to gain access through other mechansims,
for example SSH keys. Hence purging all their files may be more secure. Possibly platform dependent.

A sane UI requires that all data can be read in, regardless of how much is written 
to the State file. Thus the application stores all user data in a Pillar YAML file
as well as the required state in the State YAML file. At some point in the future
it could use a 'proper' database but memory objects persisted to disk files will
do for now.

# Initial Aims
Provide a user interface that makes the basic SaltStack functionality available through a couple of user-interfaces:
* Administrator UI where the administrator can create and manage accounts;
* User UI where each user can reset their password and set certain properties; e.g. their full name, office, phone number etc.
* Button to trigger `state.apply` and monitor the results.

# Application Level Functionality
There are some items of functionality that would need to be provided by the application as an 
extension to SaltStack and the underlying minion OSs.

## UID and GID
Ensure these are consistent across all minions for easier file transfer and less confusion.

## Adding User to Group for time limited period
Sometimes it is desirable to give users access to a group for a few hours; for example for customer support.

## Model Groups of Groups
It can be useful to be able to make groups of users members of other groups. This could be done
by adding virtual groups within the application, resolving the membership when creating the 
YAML .sls file.

Platform
========
There were two candiate platforms for this project:
* Python, as SaltStack is based on Python.
* Java with Vaadin.

Of these the latter was chosen because:
* Vaadin is awesome - it makes application development very easy
* Vaadin is secure - the browser only sees a dumb terminal view of the 
application. There is much less to worry about than trying to think of all
the security holes in a REST API.

Downside is requiring Java as well as Python on the Salt Master. However,
given the performance of modern servers this isn't a real issue.


Workflow
========

To compile the entire project, run "mvn install".

To run the application, run "mvn jetty:run" and open http://localhost:8080/ .

To produce a deployable production mode WAR:
- change productionMode to true in the servlet class configuration (nested in the UI class)
- run "mvn clean package"
- test the war file with "mvn jetty:run-war"

Client-Side compilation
-------------------------

The generated maven project is using an automatically generated widgetset by default. 
When you add a dependency that needs client-side compilation, the maven plugin will 
automatically generate it for you. Your own client-side customizations can be added into
package "client".

Debugging client side code
  - run "mvn vaadin:run-codeserver" on a separate console while the application is running
  - activate Super Dev Mode in the debug window of the application

Developing a theme using the runtime compiler
-------------------------

When developing the theme, Vaadin can be configured to compile the SASS based
theme at runtime in the server. This way you can just modify the scss files in
your IDE and reload the browser to see changes.

To use the runtime compilation, open pom.xml and comment out the compile-theme 
goal from vaadin-maven-plugin configuration. To remove a possibly existing 
pre-compiled theme, run "mvn clean package" once.

When using the runtime compiler, running the application in the "run" mode 
(rather than in "debug" mode) can speed up consecutive theme compilations
significantly.

It is highly recommended to disable runtime compilation for production WAR files.

Using Vaadin pre-releases
-------------------------

If Vaadin pre-releases are not enabled by default, use the Maven parameter
"-P vaadin-prerelease" or change the activation default value of the profile in pom.xml .
