/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment2;

import model.PasswordHassing;
import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


/**
 * FXML Controller class
 *
 * @author rayan
 */
public class LoginController implements Initializable {

    
    @FXML private TextField userIDTextField;

    @FXML private PasswordField passwordField;

    @FXML private Label errMsgLabel;
    
    @FXML private Button backButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errMsgLabel.setText("");
        
        //Lambda Expression
        backButton.setOnAction((event) -> {
        SceneChanger sc  = new SceneChanger();
            try {
                sc.changeScene(event,"LoginSignUpView.fxml","Login Sign Up View");
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }    
    
     public void loginButtonPushed(ActionEvent event) throws IOException{
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        
        String userNum = userIDTextField.getText();
        
        try{
        
            conn = DriverManager.getConnection("jdbc:mysql://sql.computerstudi.es:3306/gc200360677", "gc200360677", "JGZFKtUW");
            
            String sql = "select password, salt FROM login WHERE userName = ?";
            
            ps = conn.prepareStatement(sql);
            
            ps.setString(1, userNum);
            
            resultSet = ps.executeQuery();
            
            String dbPassword = null;
            byte[] salt = null;
            while(resultSet.next()){
                dbPassword = resultSet.getString("password");
                
                Blob blob = resultSet.getBlob("salt");
                
                int bloblength = (int) blob.length();
                salt = blob.getBytes(1, bloblength);
            }
            
            String userPW = PasswordHassing.getSHA512Password(passwordField.getText(), salt);
            
            SceneChanger sc = new SceneChanger();
            if(userPW.equals(dbPassword)){
                sc.changeScene(event, "PhoneTableView.fxml", "Phone Table View");
            }
            else
                errMsgLabel.setText("The user Id and Password do not match");
        
        } 
        catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
}
