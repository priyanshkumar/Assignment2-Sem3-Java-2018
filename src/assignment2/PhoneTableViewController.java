/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment2;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Phone;
import model.User;

/**
 * FXML Controller class
 *
 * @author rayan
 */
public class PhoneTableViewController implements Initializable {
    
    @FXML private TableView<Phone> phoneTabel;
    @FXML private TableColumn<Phone, Integer> IDColumn;
    @FXML private TableColumn<Phone, String> modelColumn;
    @FXML private TableColumn<Phone, String> brandColumn;
    @FXML private TableColumn<Phone, String> osColumn;
    @FXML private TableColumn<Phone, String> ramColumn;
    @FXML private TableColumn<Phone, String> colorColumn;
    @FXML private TableColumn<Phone, String> sizeColumn;
    @FXML private TableColumn<Phone, Float> priceColumn;
    
    @FXML private Label errMsgLable;
    @FXML private Label nameLable;
    @FXML private Label AndroidLable;
    @FXML private Label IOSLable;
    @FXML private Label WindowsLable;
  
    public void newPhoneButtonPusshed(ActionEvent event) throws IOException{
        SceneChanger sc = new SceneChanger();
        sc.changeScene(event, "PhoneView.fxml", "Creat New Phone");
     
    }
     
    public void logOutButtonPusshed(ActionEvent event) throws IOException{
        SceneChanger sc = new SceneChanger();
        sc.changeScene(event, "LoginSignUpView.fxml", "Creat New Phone");
     
    }
     
      /**
     * This method will lose to new phone view
     */
    public void editButtonPusshed(ActionEvent event) throws IOException{
        try{
            SceneChanger sc = new SceneChanger();
            Phone phone = this.phoneTabel.getSelectionModel().getSelectedItem();
            PhoneViewController ph = new PhoneViewController();
            sc.changeScene(event, "PhoneView.fxml", " Edit Phone", phone, ph);
        }
        catch(Exception e){
            errMsgLable.setText("Please select the field to be edited");
        }
    } 
     
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errMsgLable.setText("");
        
         NameInterface username = (message) ->
         nameLable.setText("Prepared By " + message);
         
         username.Message("PriyanshKumar Radadiya");
         
        //configure the tabel
        IDColumn.setCellValueFactory(new PropertyValueFactory<Phone, Integer>("phoneID"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<Phone, String>("model"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<Phone, String>("brand"));
        osColumn.setCellValueFactory(new PropertyValueFactory<Phone, String>("os"));
        ramColumn.setCellValueFactory(new PropertyValueFactory<Phone, String>("ram"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<Phone, String>("color"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<Phone, String>("size"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Phone, Float>("price"));
       try{
            loadPhone();
        }
        catch(SQLException e){
            System.err.println(e.getMessage()); 
        }
    }  
    //This method will load Phone from database
    public void loadPhone() throws SQLException{
        ObservableList<Phone> Phones = FXCollections.observableArrayList();
        
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try{
            //1 connect to database
            conn = (Connection) DriverManager.getConnection("jdbc:mysql://sql.computerstudi.es:3306/gc200360677", "gc200360677", "JGZFKtUW");

            //2 create a statement object
            statement = conn.createStatement();

            //3 create sql query
            resultSet = statement.executeQuery("SELECT * FROM phone");

            //4 create Phone object from each record
            while(resultSet.next()){

               Phone newPhone = new Phone(resultSet.getString("model"),
                                                  resultSet.getString("brand"), 
                                                  resultSet.getString("os"),
                                                  resultSet.getString("ram"),
                                                  resultSet.getString("color"), 
                                                  resultSet.getString("size"),
                                                  resultSet.getFloat("price"));

               newPhone.setPhoneID(resultSet.getInt("PhoneID"));
               newPhone.setImageFile(new File(resultSet.getString("imageFile")));
               Phones.add(newPhone);
            }
            phoneTabel.getItems().addAll(Phones);

           //Stream fumctionality
            List<String> OS = new ArrayList<>();
            for(Phone phone : Phones)
            {
                OS.add(phone.getOs());
            }
            //filter function
            int AndroidCount = (int) OS.parallelStream().filter(string -> string.equals("Android")).count();
            int IOSCount = (int) OS.parallelStream().filter(string -> string.equals("IOS")).count();
            int WindowsCount = (int) OS.parallelStream().filter(string -> string.equals("Windows")).count();
            
            AndroidLable.setText(String.valueOf(AndroidCount));
            IOSLable.setText(String.valueOf(IOSCount));
            WindowsLable.setText(String.valueOf(WindowsCount));
        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }
        finally{
           if(conn != null)
               conn.close();
           if(statement != null)
               statement.close();
           if(resultSet != null)
               resultSet.close();
        }
    }
}
