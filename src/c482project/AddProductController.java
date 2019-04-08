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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author brandon
 */
public class AddProductController implements Initializable {
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
    
    private ObservableList<Part> productParts = FXCollections.observableArrayList();
    private final Product modifiedProduct;
    
    public AddProductController()
    {
        this.modifiedProduct = getModifiedProduct();
    }
    
    public void addButtonPushed()
    {
        Part part = partSearchTableView.getSelectionModel().getSelectedItem();
        productParts.add(part);
        populateDeletePartsTableView();
        
    }
    
    public void searchButtonPushed(ActionEvent event) throws IOException 
    {
        String partSearchString = partSearchTextField.getText();
        Part searchedPart = Inventory.lookupPart(Integer.parseInt(partSearchString));
        
        if(searchedPart != null)
        {
            ObservableList<Part> filteredParts = FXCollections.observableArrayList();
            filteredParts.add(searchedPart);
            partSearchTableView.setItems(filteredParts);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search error");
            alert.setHeaderText("No part found");
            alert.setContentText("There are no parts that match that part ID.");
            alert.showAndWait();
        }
    }
    
    public void deleteButtonPushed()
    {
        if(productParts.size() > 2)
        {
            Part part = partSearchTableView.getSelectionModel().getSelectedItem();
            productParts.remove(part);
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
        String productCost = productCostTextField.getText();
        String productMin = productMinTextField.getText();
        String productMax = productMaxTextField.getText();
        
        Product newProduct = new Product();
        newProduct.setName(productName);
        newProduct.setPrice(Double.parseDouble(productCost));
        newProduct.setInStock(Integer.parseInt(productInventory));
        newProduct.setMin(Integer.parseInt(productMin));
        newProduct.setMax(Integer.parseInt(productMax));
        
        for(Part p : productParts)
        {
            newProduct.addAssociatedPart(p);
        }
        
        if(modifiedProduct == null)
        {
            newProduct.setProductID(Inventory.getProductsCount());
            Inventory.addProduct(newProduct);
        } else {
            newProduct.setProductID(modifiedProduct.getProductID());
            Inventory.updateProduct(newProduct);
        }
        Parent loader = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        Scene scene = new Scene(loader);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
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
        int productAutoID = Inventory.getProductsCount();
        productIDTextField.setText("" + productAutoID);
        productIDTextField.setDisable(true);
        partSearchID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPartID()).asObject());    
        partSearchName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        partSearchInventory.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInStock()).asObject());
        partSearchCost.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        
        partDeleteID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPartID()).asObject());
        partDeleteName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        partDeleteInventory.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInStock()).asObject());
        partDeleteCost.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
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


