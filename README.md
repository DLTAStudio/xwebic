#XWebIC

##Introduction
XWebIC is a Web wrapper for XCode Server API. It provides a more user friendly UI based on Angular JS.

## How does it works
XCode Server 6 had a great Web UI, but release 7 downgraded to a useless UI. In the meantime, Apple published a REST API (which is still at beta stage) : 
https://developer.apple.com/library/ios/documentation/Xcode/Conceptual/XcodeServerAPIReference/index.html
 
XWebIC provides a more user-friendly Web Interface for XCode Server. 

We have decided to make an Angular JS application to provide a better User experience. 
To allow cross-origin and embedded authentification in Web pages, we have wrapped the XCodeServer in a simple Java Web application that can be easily embedded in a Tomcat server. 

The WebApp embedded the Angular App for deployment, manage authentification tokens, and perform calls to XCode Server.
 
## Installation
***Requirements *** : JDK 8, Tomcat 8, XCodeServer 7.2.

From the bin directory : 

* Copy xwebic.war to Tomcat's webapps directory
* Edit the xwebic.xml file and replace "myxcodeserver.com" by the DNS name of your XCodeServer
* Copy the xwebic.xml in tomcat/conf/Catalina/localhost
* Launch your tomcat and browse to https://myxcodeserver.com/xwebic. You must use your XCodeServer username.

##Building from the sources

###Building the WebApp
***Requirements *** : NPM (NodeJS package manager, provided with NodeJS).

Download the WebApp directory.

Install Bower : 
> sudo npm install -g bower

Install Grunt:
> sudo npm install -g grunt

Install node-modules : 
> npm install

Build release WebApp : 
> grunt release

Now, just copy/replace the dist/release content to ServerWS/src/main/webapp/app to integrate in the WAR.

You can also build a debug version of the WebApp :
> grunt debug

The debug version use non-uglified Javascript files and call the services on localhost.

###Building the WebService App
***Requirements *** : Gradle 2.11 or later.

Just browse to ServerWS directory and launch gradle : 
>  gradle build

Output will be generated in build/libs/xwebic.war

When developing, you can enable Cross-Origin by setting xwebic.allowcrossorigin to true in the xwebic.xml configuration file. 

