/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 *
 * @author rayan
 */
public class Phone {
    private String model, brand, os, ram, color, size;
    private float price;
    private File imageFile;
    private int phoneID;
    
      /*
    *Constructor takes the all argument with default image
    */
    public Phone(String model, String brand, String os, String ram, String color, String size, Float price) throws NoSuchAlgorithmException {
        setModel(model);
        setBrand(brand);
        setOs(os);
        setRam(ram);
        setColor(color);
        setSize(size);
        setPrice(price);
        setImageFile(new File("./src/images/images1.png"));
      
    }

    /*
    *Constructor takes the argument with the image uploaded
    */
    public Phone(String model, String brand, String os, String ram, String color, String size, Float price, File imageFile) throws IOException, NoSuchAlgorithmException {
        this(model, brand, os, ram, color, size, price);
        setImageFile(imageFile);
        copyImageFile();
        
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        if(price > 0){
            this.price = price;
        }
        else{
            throw new IllegalArgumentException("Price must be greater than 0.");
        }
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public int getPhoneID() {
        return phoneID;
    }

    public void setPhoneID(int phoneID) {
        if(phoneID >= 0)
            this.phoneID = phoneID;
        else 
            throw new IllegalArgumentException("Employee ID must be > 0");
    }
    /**
     *This would copy the image path and would not alow it to create its instant every time and create one unique path
    */
    public void copyImageFile() throws IOException{
    Path sourcePath = imageFile.toPath();
    
    String uniqueFileName = getUniqueFileName(imageFile.getName());
    
    Path targetPath = Paths.get("./src/images/"+uniqueFileName);
   
    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
    
    //update the image file to point to new
    imageFile = new File(targetPath.toString());
    }
    
    /**
     * This would get a filename and return the unique file name prefixed with letters
     *
     */
    private String getUniqueFileName(String oldFileName){
        String uniqueName;
        
        //Random number generator
        SecureRandom src = new SecureRandom();
        
        //loops untill have unique filename
        do{
            uniqueName = "";
            
            //loop to get 32 random character
            for(int i = 0; i < 32; i++ ){
                int nextChar;
                
                do{
                
                    nextChar = src.nextInt(123);
                }while(!validCharacterValue(nextChar));
                
                uniqueName = String.format("%s%c", uniqueName, nextChar);
            }
             uniqueName += oldFileName;
        }while(!uniqueFileInDirectory(uniqueName));
        return uniqueName;
    }
    
    /**
     *This method will search for filename and check whether it is unique or not 
     */
    public boolean uniqueFileInDirectory(String fileName){
        File directory = new File("./src/images/");
        File[] dir_contents = directory.listFiles();
        
        for(File file: dir_contents)
        {
            if(file.getName().equals(fileName))
                return false;
        }
        return true;
    }
    
    
    /**
     * THis method would create valid set of number as ASCII character that could be used in filename
     * 
     */
    public boolean validCharacterValue(int value){
        
        //0 - 9
        if(value >= 48 && value <= 57)
            return true;
        
        // A-Z
        if(value >= 65 && value <= 90)
            return true;
        
        //a - z
        if(value >= 97 && value <= 122)
            return true;
        
        return false;
    }
    
    /**
     * The method to write instance to database
     */
    public void insertIntoDB() throws SQLException{
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try
        {
            // 1. connect to dastabase
            conn = (Connection) DriverManager.getConnection("jdbc:mysql://sql.computerstudi.es:3306/gc200360677", "gc200360677", "JGZFKtUW");

            //2. string that holds query
            String sql = "INSERT INTO phone(model, brand, os, ram, color, size, price, imageFile)"+
                    "VALUES(?,?,?,?,?,?,?,?)";

            // 3> prepare the query
            preparedStatement = conn.prepareStatement(sql);

            //4. Bind the value to parameters
            preparedStatement.setString(1, model);
            preparedStatement.setString(2, brand);
            preparedStatement.setString(3, os);
            preparedStatement.setString(4, ram);
            preparedStatement.setString(5, color);
            preparedStatement.setString(6, size);
            preparedStatement.setFloat(7, price);
            preparedStatement.setString(8, imageFile.getName());

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
    
    public void StoreOs(){
    
    }
    
     public void updateIntoDB() throws SQLException{
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try
        {
            // 1. connect to dastabase
            conn = (Connection) DriverManager.getConnection("jdbc:mysql://sql.computerstudi.es:3306/gc200360677", "gc200360677", "JGZFKtUW");

            //2. string that holds query
            String sql = "UPDATE phone SET model = ?, brand = ?, os = ?, ram = ?, color = ?, size = ?, price = ?, imageFile = ?"
                    +"WHERE phoneID = ?";

            // 3> prepare the query
            preparedStatement = conn.prepareStatement(sql);

            //4. Bind the value to parameters
            preparedStatement.setString(1, model);
            preparedStatement.setString(2, brand);
            preparedStatement.setString(3, os);
            preparedStatement.setString(4, ram);
            preparedStatement.setString(5, color);
            preparedStatement.setString(6, size);
            preparedStatement.setFloat(7, price);
            preparedStatement.setString(8, imageFile.getName());
            preparedStatement.setInt(9, phoneID);

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
