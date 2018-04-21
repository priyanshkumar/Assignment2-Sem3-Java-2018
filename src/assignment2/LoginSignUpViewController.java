/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment2;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author rayan
 */
public class LoginSignUpViewController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }  
    
    public void loginButtonPushed(ActionEvent event) throws IOException{
            SceneChanger sc  = new SceneChanger();
            sc.changeScene(event,"Login.fxml","Login");
    }
    
    public void signUpButtonPushed(ActionEvent event) throws IOException{
         SceneChanger sc  = new SceneChanger();
         sc.changeScene(event,"SignUp.fxml","Sign Up");
    }
    
}
