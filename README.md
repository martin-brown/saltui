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
