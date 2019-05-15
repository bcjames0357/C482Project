/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c482project;

import static c482project.MainScreenController.getModifiedProduct;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author brandon
 */
public class ModifyProductController implements Initializable {
    
    @FXML private TextField productIDTextField;
    @FXML private TextField productNameTextField;
    @FXML private TextField productInventoryTextField;
    @FXML private TextField productCostTextField;
    @FXML private TextField productMaxTextField;
    @FXML private TextField productMinTextField;
    @FXML private TextField partSearchTextField;
    
    @FXML private TableView<Part> partSearchTableView;
    @FXML private TableColumn<Part, Integer> partSearchID;
    @FXML private TableColumn<Part, String> partSearchName;
    @FXML private TableColumn<Part, Integer> partSearchInventory;
    @FXML private TableColumn<Part, Double> partSearchCost;    
    
    @FXML private TableView<Part> partDeleteTableView;
    @FXML private TableColumn<Part, Integer> partDeleteID;
    @FXML private TableColumn<Part, String> partDeleteName;
    @FXML private TableColumn<Part, Integer> partDeleteInventory;
    @FXML private TableColumn<Part, Double> partDeleteCost;
    
    private final Product modifiedProduct;
    private ObservableList<Part> productParts = FXCollections.observableArrayList();

    
    public ModifyProductController()
    {
        this.modifiedProduct = getModifiedProduct();
    }
    
    public void cancelButtonPushed(ActionEvent event) throws IOException
    {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
    }
    
    public void addPartButtonPushed(ActionEvent event) throws IOException
    {
        Part part = partSearchTableView.getSelectionModel().getSelectedItem();
        productParts.add(part);
        //modifiedProduct.addAssociatedPart(part);
        populateDeletePartsTableView();
    }
    
    public void deletePartButtonPushed(ActionEvent event) throws IOException
    {
        
        if(productParts.size() >= 2) 
        {
            Part part = partDeleteTableView.getSelectionModel().getSelectedItem();
            //modifiedProduct.removeAssociatedParts(part.getPartID());
            productParts.remove(part);
            
            populateDeletePartsTableView();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Part delete error");
            alert.setHeaderText("Minimum part requirement not met.");
            alert.setContentText("At least one part must be associated with the product.");
            alert.showAndWait();
        }
        
    }
    
    public void saveButtonPushed(ActionEvent event) throws IOException
    {
        String productName = productNameTextField.getText();
        String productInventory = productInventoryTextField.getText();
        String productPrice = productCostTextField.getText();
        String productMin = productMinTextField.getText();
        String productMax = productMaxTextField.getText();
        
        Product newProduct = new Product();
        newProduct.setName(productName);    
        newProduct.setPrice(Double.parseDouble(productPrice));
        newProduct.setInStock(Integer.parseInt(productInventory));
        newProduct.setMin(Integer.parseInt(productMin));
        newProduct.setMax(Integer.parseInt(productMax));
        
        if(modifiedProduct != null)
        {
            modifiedProduct.purgeAssociatedParts();
        }
        
        for(Part p : productParts)
        {
            newProduct.addAssociatedPart(p);
        }
        
        
        try {
            
            if(modifiedProduct == null) 
            {
                newProduct.setProductID(Inventory.getProductsCount());
                Inventory.addProduct(newProduct);
            } else {
                newProduct.setProductID(modifiedProduct.getProductID());    
                Inventory.updateProduct(newProduct);
            }
            
            //Will not return to main screen if product modifications are invalid
            if(newProduct.isValid())
            {
            Parent loader = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(loader);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
            }
        } catch (InventoryException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("InventoryError");
            alert.setHeaderText("Part not valid");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    
    public void searchButtonPushed()
    {
        String partsSearchString = partSearchTextField.getText();
        Part searchedPart = Inventory.lookupPart(Integer.parseInt(partsSearchString));
        
        if(searchedPart != null)
        {
            ObservableList<Part> filteredPartsList = FXCollections.observableArrayList();
            filteredPartsList.add(searchedPart);
            partSearchTableView.setItems(filteredPartsList);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search error");
            alert.setHeaderText("Product not found");
            alert.setContentText("No matching product found.");
            alert.showAndWait();
        }
        
        //populateSearchPartsTableView();
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productIDTextField.setText(Integer.toString(modifiedProduct.getProductID()));
        productIDTextField.setDisable(true);
        
        productNameTextField.setText(modifiedProduct.getName());
        productInventoryTextField.setText(Integer.toString(modifiedProduct.getInStock()));
        productCostTextField.setText(Double.toString(modifiedProduct.getPrice()));
        productMinTextField.setText(Integer.toString(modifiedProduct.getMin()));
        productMaxTextField.setText(Integer.toString(modifiedProduct.getMax()));
        
        productParts = modifiedProduct.getAssociatedParts();
        partDeleteID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPartID()).asObject());
        partDeleteName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        partDeleteInventory.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInStock()).asObject());
        partDeleteCost.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());         
        
        partSearchID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPartID()).asObject());
        partSearchName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        partSearchInventory.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInStock()).asObject());
        partSearchCost.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        
        populateSearchPartsTableView();
        populateDeletePartsTableView();
    }    
    
    public void populateSearchPartsTableView() 
    {
        partSearchTableView.setItems(Inventory.getParts());
    }
    
    public void populateDeletePartsTableView()
    {
        partDeleteTableView.setItems(productParts);
    }
    
}
