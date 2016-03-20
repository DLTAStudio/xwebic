package com.dltastudio.model;

import com.dltastudio.services.DatabaseInstance;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.Base64;
import java.util.UUID;

/**
 * DAO to store session token
 */
public class TokenDAO {

    /**
     * Build HTTP Basic Authentification header from username/password
     * @param username username
     * @param password password
     * @return Encoded http header authorization. Null on error
     */
    public static String buildHttpAuthString(String username,String password) {
       try {
           String authString = username + ":" + password;
           String base64Auth = Base64.getEncoder().encodeToString(authString.getBytes("UTF-8"));
           String authHeader = "Basic " + base64Auth;
           return authHeader;
       } catch (UnsupportedEncodingException e) {
           e.printStackTrace();
           return null;
       }
    }

    /**
     * Register a username/password as return the resulting session token
     * @param username username
     * @param password password
     * @return Encoded http header authorization. Null on error
     */
    public static String registerToken(String username,String password) {
        String result=null;

        purgeTokens();
        String token= UUID.randomUUID().toString();
        String authHeader=buildHttpAuthString(username,password);
        String request="INSERT INTO user_infos(token,http_auth,registration_date) values (?,?,now())";
        try(Connection connection= DatabaseInstance.getConnection();
            PreparedStatement statement = connection.prepareStatement(request)) {
            statement.setString(1,token);
            statement.setString(2,authHeader);
            statement.executeUpdate();
            result=token;
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        catch(Exception e) {
        }
        return result;
    }

    /**
     * Update last access date for token
     * @param token Token to be updated
     */
    private static void updateTokenAccessDate(String token) {
        String request="UPDATE user_infos set registration_date=now() WHERE token=?";
        try(Connection connection= DatabaseInstance.getConnection();
            PreparedStatement statement = connection.prepareStatement(request)) {
            statement.setString(1,token);
            statement.executeUpdate();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        catch(Exception e) {
        }

    }

    /**
     * Load Http header authorization string for a token
     * @param token User token
     * @return Http header authorization string for a token, null if not found
     */
    public static String loadAuthString(String token) {
        String result=null;
        purgeTokens();

        String request="SELECT http_auth FROM user_infos WHERE token=?";
        try(Connection connection= DatabaseInstance.getConnection();
            PreparedStatement statement = connection.prepareStatement(request)) {
            statement.setString(1,token);
            ResultSet rs=statement.executeQuery();
            if (rs.next()) {
                result=rs.getString(1);
                updateTokenAccessDate(token);
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        catch(Exception e) {
        }
        return result;
    }

    /**
     * Remove all tokens from database
     */
    private static void purgeTokens() {
        String request="DELETE FROM user_infos WHERE (now()-registration_date)>3600*24";
        try(Connection connection= DatabaseInstance.getConnection();
            Statement statement = connection.createStatement()) {
             statement.executeUpdate(request);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        catch(Exception e) {
        }
    }
}
