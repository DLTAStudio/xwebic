package com.dltastudio.services;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebListener;


/**
 * Implemente general settings for WebApp
 */
@WebListener
public class GeneralSettings implements ServletContextListener {

    /**
     * Database instance for storing connexion tokens
     */
    private DatabaseInstance databaseInstance;

    /**
     * Base URL for xCode Server
     */
    private String XCodeServerBaseURL;

    /**
     * True for accepting cross-domain origin (useful when debugging)
     */
    private boolean allowCrossOriginAllowed;

    /**
     * Port number for HSQLDB  Server
     */
    private int HSQLDBServerPort;

    /**
     * singleton instance
     */
    private static GeneralSettings instance;

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        if (null!=databaseInstance)
            databaseInstance.destroy();
        System.out.println("ServletContextListener destroyed");
    }

    //Run this before web application is started
    @Override
    public void contextInitialized(ServletContextEvent event) {
        InitialContext initialContext=null;

        instance=this;
        try {
            XCodeServerBaseURL="";
            allowCrossOriginAllowed=false;
            initialContext = new InitialContext();
            XCodeServerBaseURL = (String) initialContext.lookup("java:comp/env/xwebic.baseurl");
            allowCrossOriginAllowed = (Boolean)initialContext.lookup("java:comp/env/xwebic.allowcrossorigin");
            HSQLDBServerPort=(Integer)initialContext.lookup("java:comp/env/xwebic.hsqldbPort");
            databaseInstance = new DatabaseInstance(event.getServletContext());
            System.out.println("ServletContextListener started");
        }
        catch (NamingException|ServletException e) {
            XCodeServerBaseURL="";
            allowCrossOriginAllowed=false;
            databaseInstance=null;
            HSQLDBServerPort=9001;
        }
        if (null!=initialContext) {
            try {
                initialContext.close();
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieve GeneralSettings instance
     * @return The GeneralSettings instance
     */
    static public GeneralSettings getGeneralSettings() {
        return instance;
    }

    /**
     * Retrieve XCode Server base URL for API
     * @return XCode server base URL for API
     */
    static public String getXCodeBaseURL() {
        return getGeneralSettings().XCodeServerBaseURL+":20343/api/";
    }

    /**
     * Retrieve XCode Server URL for internal API
     * @return XCode Server URL for internal API
     */
    static public String getXCodeInternalBaseURL() {
        return getGeneralSettings().XCodeServerBaseURL+"/xcode/internal/api/";
    }

    /**
     * Test if cross-domain origin is allowed
     * @return true if cross-origin is allowed, otherwise false
     */
    static public boolean isCrossOriginAllowed() {
        return getGeneralSettings().allowCrossOriginAllowed;
    }

    /**
     * Return port number for HSQLDB
     * @return HSQLDB Port
     */
    static public int getHSQLDBServerPort() {
        return getGeneralSettings().HSQLDBServerPort;
    }

}
