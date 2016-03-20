package com.dltastudio.services;

import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.Server;
import org.hsqldb.server.ServerAcl;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.sql.*;

/**
 * Database Instance for HSQLDB
 * Store user token
 */
public class DatabaseInstance  {

    /**
     * Database server
     */
    private Server server;

    /**
     * Initialise database instance
     * @param servletContext ServletContext
     * @throws ServletException ServletException occurs
     */
    private void initServer(ServletContext servletContext) throws ServletException {
        try {
            File workingDir = (File)servletContext.getAttribute(ServletContext.TEMPDIR);
            File dbFile = new File(workingDir,"xcodeAuthDB");
            System.out.println("Starting Database");
            HsqlProperties p = new HsqlProperties();
            String databaseFile = "file:"+dbFile.getCanonicalPath();

            p.setProperty("server.database.0", databaseFile);
            p.setProperty("server.dbname.0", "xcodeAuthDB");
            p.setProperty("server.port", "9001");
            server = new Server();
            server.setProperties(p);
            server.setLogWriter(null); // can use custom writer
            server.setErrWriter(null); // can use custom writer
            server.start();
        } catch (ServerAcl.AclFormatException afex) {
            throw new ServletException(afex);
        } catch (IOException ioex) {
            throw new ServletException(ioex);
        }
    }

    /**
     * Create DataBase instance from ServletContext
     * @param servletContext ServletContext
     * @throws ServletException ServletException occurs
     */
    public DatabaseInstance(ServletContext servletContext) throws ServletException {
        initServer(servletContext);
        initDB();
    }

    /**
     * Retrieve Database instance connection
     * @return Connection to the database
     * @throws ClassNotFoundException The JDBC drive has not been found
     * @throws SQLException SQLException
     */
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection connection=null;
        Class.forName("org.hsqldb.jdbc.JDBCDriver" );
        connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/xcodeAuthDB", "SA", "");
        connection.setAutoCommit(true);
        return connection;
    }

    /**
     * Tests if a table already exists
     * @param tableName Table name to look for
     * @return true if table existe, otherwise false.
     */
    private boolean existTable(String tableName) {
        boolean existTable=false;

        String request="SELECT count(*) FROM INFORMATION_SCHEMA.SYSTEM_COLUMNS WHERE TABLE_NAME ='"+tableName+"'";
        try(Connection connection=getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(request)) {
            while(rs.next()) {
                existTable=(1==rs.getInt(1));
            }
        }
        catch(Exception e) {
            existTable=false;
        }
        return existTable;
    }

    /**
     * Execute an update statement
     * @param request SQL request to execute
     * @return true if successful, otherwise false
     */
    private boolean executeUpdateStatement(String request) {
        try(Connection connection=getConnection();
            Statement statement = connection.createStatement()) {
            statement.executeQuery(request);
            return true;
        }
        catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
        catch(Exception e) {
            return false;
        }
    }

    /**
     * Initialise database for storing tokens
     * @return true if successul, otherwise false
     */
    private boolean initDB() {
        boolean bSuccess=true;
        if (!existTable("user_infos"))
        {

            String request="CREATE TABLE  user_infos ("+
            "    id IDENTITY,"+
            "    token varchar(256) NOT NULL,"+
            "    http_auth varchar(256) NOT NULL,"+
            "    registration_date DATE NOT NULL"+
                    ");";
            bSuccess=executeUpdateStatement(request);

            if (bSuccess) {
                request = "CREATE UNIQUE INDEX token_index ON user_infos(token);";
                bSuccess = executeUpdateStatement(request);
            }
        }
        return bSuccess;
    }

    /**
     * Destroy database instance
     */
    public void destroy() {
        server.shutdown();
    }
}


