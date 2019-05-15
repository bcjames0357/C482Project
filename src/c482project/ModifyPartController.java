/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c482project;

import static c482project.MainScreenController.getModifiedPart;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author brandon
 */
public class ModifyPartController implements Initializable {
    
    @FXML private TextField partIDTextField;
    @FXML private TextField partNameTextField;
    @FXML private TextField partInventoryTextField;
    @FXML private TextField partCostTextField;
    @FXML private TextField partMaxTextField;
    @FXML private TextField partMinTextField;
    @FXML private TextField partCoMachTextField;
    @FXML private Label partCoMachLabel;
    @FXML private RadioButton inHouseRadioButton;
    @FXML private RadioButton outsourcedRadioButton;
    private ToggleGroup sourceToggleGroup;
    private boolean isInHouse;
    private final Part modifyPart;
    
    public ModifyPartController()
    {
        this.modifyPart = getModifiedPart();
    }
    
    public void radioButtonChanged()
    {
        if(this.sourceToggleGroup.getSelectedToggle().equals(inHouseRadioButton))
        {
            this.partCoMachLabel.setText("Machine ID");
            isInHouse = true;
        }
        
        if(this.sourceToggleGroup.getSelectedToggle().equals(outsourcedRadioButton))
        {
            this.partCoMachLabel.setText("Company");
            isInHouse = false;
        }
    }
   
    public void saveButtonPushed(ActionEvent event) throws IOException
    {
        String partIDString = partIDTextField.getText();
        String partName = partNameTextField.getText();
        String partInventory = partInventoryTextField.getText();
        String partPrice = partCostTextField.getText();
        String partMin = partMinTextField.getText();
        String partMax = partMaxTextField.getText();
        String partCoMach = partCoMachTextField.getText();
    
        if (partInventory.equals(null))
        {
            partInventory = "0";
        }
        
        if(Integer.parseInt(partInventory) > Integer.parseInt(partMax) 
                || Integer.parseInt(partInventory) < Integer.parseInt(partMin))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("InventoryError");
            alert.setHeaderText("Invalid inventory quantity");    
            alert.setContentText("Current inventory must be between minimum and maximum.");
            alert.showAndWait();
            return;
        }
        
        if (isInHouse) {
            InHouse modifiedPart = new InHouse();
            modifiedPart.setPartID(Integer.parseInt(partIDString));
            modifiedPart.setName(partName);
            modifiedPart.setPrice(Double.parseDouble(partPrice));
            modifiedPart.setInStock(Integer.parseInt(partInventory));
            modifiedPart.setMin(Integer.parseInt(partMin));
            modifiedPart.setMax(Integer.parseInt(partMax));
            modifiedPart.setMachineID(Integer.parseInt(partCoMach));
            
            //try {
                //modifiedPart.isValid();
            
                if(modifyPart == null) {
                    modifiedPart.setPartID(Inventory.getPartsCount());
                    Inventory.addPart(modifiedPart);
                } else {
                    int partID = modifyPart.getPartID();
                    modifiedPart.setPartID(partID);
                    Inventory.updatePart(modifiedPart);
                }
                
                Parent loader = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(loader);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            //} catch (InventoryException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("InventoryError");
                alert.setHeaderText("Part not valid");
                alert.setContentText("Part modifications invalid.");
                alert.showAndWait();
            //}
        } else {
            
            Outsourced modifiedPart = new Outsourced();
            modifiedPart.setName(partName);
            modifiedPart.setPrice(Double.parseDouble(partPrice));
            modifiedPart.setInStock(Integer.parseInt(partInventory));
            modifiedPart.setMin(Integer.parseInt(partMin));
            modifiedPart.setMax(Integer.parseInt(partMax));
            modifiedPart.setCompanyName(partCoMach);
            if(modifyPart == null) {
                modifiedPart.setPartID(Inventory.getPartsCount());
                Inventory.addPart(modifiedPart);
            } else {
                int partID = modifyPart.getPartID();
                modifiedPart.setPartID(partID);
                Inventory.updatePart(modifiedPart);
            }
            
            Parent loader = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(loader);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();           
        }
    }
    
    public void cancelButtonPushed(ActionEvent event) throws IOException
    {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partIDTextField.setDisable(true);
        sourceToggleGroup = new ToggleGroup();
        this.inHouseRadioButton.setToggleGroup(sourceToggleGroup);
        this.outsourcedRadioButton.setToggleGroup(sourceToggleGroup);
        this.outsourcedRadioButton.setSelected(true);
        partIDTextField.setText(Integer.toString(modifyPart.getPartID()));
        partNameTextField.setText(modifyPart.getName());
        partInventoryTextField.setText(Integer.toString(modifyPart.getInStock()));
        partCostTextField.setText(Double.toString(modifyPart.getPrice()));
        partMinTextField.setText(Integer.toString(modifyPart.getMin()));
        partMaxTextField.setText(Integer.toString(modifyPart.getMax()));
        
        
        if(modifyPart instanceof InHouse)
        {
            partCoMachTextField.setText(Integer.toString(((InHouse) modifyPart).getMachineID()));
            this.inHouseRadioButton.setSelected(true);
            this.partCoMachLabel.setText("Machine ID");
        } else {
            partCoMachTextField.setText(((Outsourced) modifyPart).getCompanyName());
            this.outsourcedRadioButton.setSelected(true);
            this.partCoMachLabel.setText("Company Name");
        }
    }    
    
}
