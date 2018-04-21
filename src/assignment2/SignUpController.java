/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment2;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
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
import model.User;

/**
 *
 * @author rayan
 */
public class SignUpController implements Initializable{
    
    @FXML private TextField userField;

    @FXML private PasswordField passwordField;

    @FXML private PasswordField confirmField;

    @FXML private Label errMsgLabel;
    
    @FXML private Button backButton;
    
    private User user;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    public void signUpButtonPushed(ActionEvent event){
    
        if(validPasswords()){
            try{
            user = new User(userField.getText(), passwordField.getText());

            errMsgLabel.setText("");
            user.insertIntoDB();


            SceneChanger sc  = new SceneChanger();
            sc.changeScene(event,"Login.fxml","Login");
            }
            catch(Exception e)
            {
                errMsgLabel.setText(e.getMessage());
            }
        }
    }
        
    public boolean validPasswords(){
        if (passwordField.getText().length() < 8){
            errMsgLabel.setText("Password must be greater then 8 characters.");
            return false;
        }
        if (passwordField.getText().equals(confirmField.getText()))
            return true;
        else
            errMsgLabel.setText("Password does not match.");
            return false;
    }
}
    
     

    

