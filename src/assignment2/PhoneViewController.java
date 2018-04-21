/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment2;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import model.Phone;

/**
 * FXML Controller class
 *
 * @author rayan
 */
public class PhoneViewController implements Initializable,ControllerClass {

    /**
     * Initializes the controller class.
     */
    @FXML private TextField modelTextField;

    @FXML private TextField BrandTextField;

    @FXML private TextField colorTextField;
    
    @FXML private TextField sizeTextField;
    
    @FXML private Label headerlabel;
    
    @FXML private TextField priceTextField;
    
    @FXML private ChoiceBox osChoiceBox;
    
    @FXML private ChoiceBox ramChoiceBox;
    
    @FXML private Label errMsgLabel;
    
    @FXML private ImageView imageView;
    
    @FXML private Button cancelButton;
    
    @FXML private Label nameLable;

    private File imageFile;

    private boolean imageFileChanged;
    
    private Phone phone;
        
    public void savePhoneButtonPushed(ActionEvent event){
        try
            {
                if(phone != null){
                    updatePhone();
                    phone.updateIntoDB();
                }
                else{
                    if(imageFileChanged){
                        phone = new Phone(modelTextField.getText(), BrandTextField.getText(), osChoiceBox.getValue().toString(),
                                                       ramChoiceBox.getValue().toString(), colorTextField.getText(),
                                                        sizeTextField.getText(), Float.parseFloat(priceTextField.getText()), imageFile);
                    }
                    else{
                        phone = new Phone(modelTextField.getText(), BrandTextField.getText(), osChoiceBox.getValue().toString(),
                                                       ramChoiceBox.getValue().toString(), colorTextField.getText(), sizeTextField.getText(),
                                                        Float.parseFloat(priceTextField.getText()));
                    }

                errMsgLabel.setText("");
                phone.insertIntoDB();
                
                }

                SceneChanger sc  = new SceneChanger();
                sc.changeScene(event,"PhoneTableView.fxml","Contact");
            }
            catch(Exception e)
            {
                errMsgLabel.setText(e.getMessage());
            }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
         NameInterface username = (message) ->
         nameLable.setText("Prepared By " + message);
         
         username.Message("PriyanshKumar Radadiya");
        
        imageFileChanged = false;//Intialy let be false
        errMsgLabel.setText("");
        
        osChoiceBox.getItems().add("IOS");
        osChoiceBox.getItems().add("Android");
        osChoiceBox.getItems().add("Windows");
        
        ramChoiceBox.getItems().add("16gb");
        ramChoiceBox.getItems().add("32gb");
        ramChoiceBox.getItems().add("64gb");
        ramChoiceBox.getItems().add("128gb");
        ramChoiceBox.getItems().add("256gb");
        
        FileInterface filename = () -> {
            try
            {
                imageFile = new File("./src/images/image1.png"); 
                BufferedImage bufferedImage = ImageIO.read(imageFile);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                imageView.setImage(image);
            }
            catch(IOException e){
                System.err.println(e.getMessage());
            }
        };
        
        filename.displayImage();
        
        cancelButton.setOnAction((event) -> {
            try {
                SceneChanger sc = new SceneChanger();
                sc.changeScene(event, "PhoneTableView.fxml", "Phone Available");
            } catch (IOException ex) {
                Logger.getLogger(PhoneViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }  
    
    /**
     * when this button is pushed it will allow to change the user to browse new image. after choosing it will update the image. 
     */
    public void chooseImageButtonPushed(ActionEvent event){
    
        //get the stage to open new window
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        //instantiate FileChoser object
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        
        //filter for jpg and png
        FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("Image File (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("Image File (*.png)", "*.png");
        fileChooser.getExtensionFilters().addAll(jpgFilter, pngFilter);
        
        // set user directory if not avilabel
        String userDirectoryString = System.getProperty("user.home")+"\\pictures";
        File userDirectory = new File(userDirectoryString);
        
        // if you cannot navigate to picture directory go to user home
        if(!userDirectory.canRead())
            userDirectory = new File(System.getProperty("user.home"));
        
        fileChooser.setInitialDirectory(userDirectory);
        
        //open the file dialog window
        File tmpImageFile = fileChooser.showOpenDialog(stage);
        
        if(tmpImageFile != null){
            
            imageFile = tmpImageFile;

            //update the image view with new image
            if(imageFile.isFile())
            {
                try{
                        BufferedImage bufferedImage = ImageIO.read(imageFile);
                        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                        imageView.setImage(image);
                        imageFileChanged = true;
                }
                catch(IOException e){
                    System.err.println(e.getMessage());
                }
            }
        }
         
    }
    
    
//    public void cancelButtonPushed(ActionEvent event) throws IOException{
//        SceneChanger sc = new SceneChanger();
//        sc.changeScene(event, "PhoneTableView.fxml", "Phone Available");
//    }
    
    

    @Override
    public void preloadData(Phone phone) {
        this.phone = phone;
        this.modelTextField.setText(phone.getModel());
        this.BrandTextField.setText(phone.getBrand());
        this.osChoiceBox.setValue(phone.getOs().toString());
        this.ramChoiceBox.setValue(phone.getRam().toString());
        this.colorTextField.setText(phone.getColor());
        this.sizeTextField.setText(phone.getSize());
        this.priceTextField.setText(String.valueOf(phone.getPrice()));
        this.headerlabel.setText("Edit Phone");
      
      try{
          String imgLocation = ".\\src\\images\\" + phone.getImageFile().getName();
          imageFile = new File(imgLocation);
          BufferedImage bufferedImage = ImageIO.read(imageFile);
          Image img = SwingFXUtils.toFXImage(bufferedImage, null);
          imageView.setImage(img);
      }
      catch(IOException e)
      {
          System.err.println(e.getMessage());
      }
    }
    
    public void updatePhone() throws IOException{
        phone.setModel(modelTextField.getText());
        phone.setBrand(BrandTextField.getText());
        phone.setOs(osChoiceBox.getValue().toString());
        phone.setRam(ramChoiceBox.getValue().toString());
        phone.setColor(colorTextField.getText());
        phone.setSize(sizeTextField.getText());
        phone.setPrice(Float.parseFloat(priceTextField.getText()));
        phone.setImageFile(imageFile);
        phone.copyImageFile();
    }
    
    public interface FileInterface {
    void displayImage();
}
    
}
  