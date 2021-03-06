/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c482project;

import java.io.IOException;
import static java.lang.Integer.parseInt;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author brandon
 */
public class MainScreenController implements Initializable {
    
    @FXML private Button exitButton;
    @FXML private Button modifyPartButton;
    @FXML private Button modifyProductButton;
    @FXML private Button deletePartButton;
    @FXML private Button deleteProductButton;
    
    @FXML private TextField searchPartTextField;
    @FXML private TextField searchProductTextField;
    
    @FXML private TableView<Part> partsTableView;
    @FXML private TableColumn<Part, Integer> partID;
    @FXML private TableColumn<Part, String> partName;
    @FXML private TableColumn<Part, Integer> partInventory;
    @FXML private TableColumn<Part, Double> partCost;    
    
    @FXML private TableView<Product> productTableView;
    @FXML private TableColumn<Product, Integer> productID;
    @FXML private TableColumn<Product, String> productName;
    @FXML private TableColumn<Product, Integer> productInventory;
    @FXML private TableColumn<Product, Double> productCost;
    
    private static Part modifiedPart;
    private static Product modifiedProduct;
    
    public MainScreenController()
    {
        
    }
    
    public static Part getModifiedPart()
    {
        return modifiedPart;
    }
    
    public void setModifiedPart(Part modifiedPart)
    {
        MainScreenController.modifiedPart = modifiedPart;
    }
    
    public static Product getModifiedProduct()
    {
        return modifiedProduct;
    }
    
    public void setModifiedProduct(Product modifiedProduct)
    {
        MainScreenController.modifiedProduct = modifiedProduct;
    }
    
    public void addPartButtonPushed(ActionEvent event) throws IOException
    {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("AddPart.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
    }
    
    public void modifyPartButtonPushed(ActionEvent event) throws IOException
    {       
        modifiedPart = partsTableView.getSelectionModel().getSelectedItem();
        setModifiedPart(modifiedPart);
        
        Parent loader = FXMLLoader.load(getClass().getResource("ModifyPart.fxml"));
        Scene scene = new Scene(loader);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    
        public void deletePartButtonPushed()
    {
        ObservableList<Part> selectedPartRows, allParts;
        allParts = partsTableView.getItems();
        
        selectedPartRows = partsTableView.getSelectionModel().getSelectedItems();
        
        for (Part part: selectedPartRows)
        {
            allParts.remove(part);
        }
    }
    
    /**
     *
     * @param event
     * @throws IOException
     */
    public void searchPartButtonPushed(ActionEvent event) throws IOException
    {
        String searchString = searchPartTextField.getText();
        if("".equals(searchString))
        {
        } else {
            Part searchedPart = Inventory.lookupPart(parseInt(searchString));
            if(searchedPart != null)
            {
                ObservableList<Part> searchResults = FXCollections.observableArrayList();
                searchResults.add(searchedPart);
                partsTableView.setItems(searchResults);
            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("No search results");
                alert.setContentText("The search terms do not match an existing part.");
                alert.showAndWait();
            }
        }
    }
    
    public void addProductButtonPushed(ActionEvent event) throws IOException
    {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
    }
    
    public void modifyProductButtonPushed(ActionEvent event) throws IOException
    {
        modifiedProduct = productTableView.getSelectionModel().getSelectedItem();
        setModifiedProduct(modifiedProduct);
        
        Parent loader = FXMLLoader.load(getClass().getResource("ModifyProduct.fxml"));
        Scene scene = new Scene(loader);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    
    public void deleteProductButtonPushed()
    {
        ObservableList<Product> selectedProductRows, allProducts;
        allProducts = productTableView.getItems();
        
        selectedProductRows = productTableView.getSelectionModel().getSelectedItems();
        
        for (Product product: selectedProductRows)
        {
            allProducts.remove(product);
        }
     
        
    }
    
    public void searchProductButtonPushed(ActionEvent event) throws IOException
    {
        String searchString = searchProductTextField.getText();
        
        if("".equals(searchString))
        {
        } else {
            Product searchedProduct = Inventory.lookupProduct(parseInt(searchString));
            if(searchedProduct != null)
            {
                ObservableList<Product> searchResults = FXCollections.observableArrayList();
                searchResults.add(searchedProduct);
                productTableView.setItems(searchResults);
            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("No search results");
                alert.setContentText("The search terms do not match an existing product.");
                alert.showAndWait();
            }
        }
    }
    
    public void exitButtonPushed(ActionEvent event)
    {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
        
    }
    
    public void userClickedOnPartsTable()
    {
        if(partsTableView.getSelectionModel().isEmpty()) return;
        
        this.modifyPartButton.setDisable(false);
        this.deletePartButton.setDisable(false);
        this.deleteProductButton.setDisable(true);
        this.modifyProductButton.setDisable(true);
    }
    
    public void userClickedOnProductsTable()
    {
        if(productTableView.getSelectionModel().isEmpty()) return;
        
        this.modifyProductButton.setDisable(false);
        this.deleteProductButton.setDisable(false);
        this.deletePartButton.setDisable(true);
        this.modifyPartButton.setDisable(true);
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        partID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPartID()).asObject());
        partName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        partInventory.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInStock()).asObject());
        partCost.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        
        productID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getProductID()).asObject());
        productName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        productInventory.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInStock()).asObject());
        productCost.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        
        this.modifyPartButton.setDisable(true);
        this.modifyProductButton.setDisable(true);
        this.deleteProductButton.setDisable(true);
        this.deletePartButton.setDisable(true);
        productTableView.setItems(Inventory.getProducts());
        partsTableView.setItems(Inventory.getParts());
    }
    
    
    void setMainApp(C482Project aThis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
