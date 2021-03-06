# Filehosting-Tool

A Java EE 7 based "Dropbox" like clone to manage files online

## Features

 * Filemanager
 * User management
 * Group management
 * Internationalization
 * REST API

## Requirements
 
 * JBoss Wildfly 8
 * MySQL Database

## Installation

 * Download JBoss Wildfly 8
 * Create a datasource with the JNDI name "java:/jdbc/DefaultDS"
 * Import the [filehosting_tool.sql](https://github.com/danielkueffer/filehosting-tool/blob/master/sql/filehosting_tool.sql) file in your database
 * In the JBoss directory standalone/data create the directories "files" and "profile-images"
 * Build a war and deploy it to the JBoss server
 
In the standalone.xml under "default-host" add the following line:

	<location name="/profile-img" handler="profile-image"/>
	
In the standalone.xml under "handlers" add the following line:

	<file name="profile-image" path="${jboss.server.data.dir}/profile-images"/>
	
## Desktop Client

A JavaFX [Desktop Client](https://github.com/danielkueffer/filehosting-tool-desktop-client) for the [Filehosting-Tool](https://github.com/danielkueffer/filehosting-tool) to synchronize the files on the desktop with the server.

## License

[MIT License](http://www.opensource.org/licenses/mit-license.php). See LICENSE.
