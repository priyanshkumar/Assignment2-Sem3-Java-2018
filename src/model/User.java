/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author rayan
 */
public class User {
    
    private String userName, password;
    private byte[] salt;
    private int userId;
    
    public User(String userName, String password) throws NoSuchAlgorithmException{
        setUserName(userName);
        salt = PasswordHassing.getSalt();
        this.password = PasswordHassing.getSHA512Password(password, salt);
    }
    public int getUserID() {
        return userId;
    }

    public void setUserID(int userID) {
        if(userID >= 0)
            this.userId = userID;
        else 
            throw new IllegalArgumentException("Employee ID must be > 0");
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public void insertIntoDB() throws SQLException{
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try
        {
            // 1. connect to dastabase
            conn = (Connection) DriverManager.getConnection("jdbc:mysql://sql.computerstudi.es:3306/gc200360677", "gc200360677", "JGZFKtUW");

            //2. string that holds query
            String sql = "INSERT INTO login(userName, password, salt)"+
                    "VALUES(?,?,?)";

            // 3> prepare the query
            preparedStatement = conn.prepareStatement(sql);


            //5. Bind the value to parameters
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);
            preparedStatement.setBlob(3, new javax.sql.rowset.serial.SerialBlob(salt));

            preparedStatement.executeUpdate();
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            if(preparedStatement != null)
                preparedStatement.close();
            if(conn != null)
                conn.close();
        }
    }    
}
