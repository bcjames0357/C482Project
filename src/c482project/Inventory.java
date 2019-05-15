/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c482project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author brandon
 */
class Inventory {
    
    private final static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private final static ObservableList<Product> products = FXCollections.observableArrayList();
    
    public Inventory()
    {
        
    }
    
    public static void addProduct(Product product)
    {
        products.add(product);
    }
    
    public static boolean removeProduct(int productID)
    {
        for(Product p : products)
        {
            if(p.getProductID() == productID)
            {
                products.remove(p);
                return true;
            }
        }
        return false;
    }
    
    public static Product lookupProduct(int productID)
    {        
        for(Product p : products)
        {
            if(p.getProductID() == productID)
            {
                return p;
            }
        }
        return null;
    }
    
    public static void updateProduct(Product product)
    {
        products.set(product.getProductID(), product);
        
    }
    
    public static void addPart(Part part)
    {
        allParts.add(part);
    }
    
    public static boolean deletePart(int partID)
    {
        for(Part p : allParts)
        {
            if(p.getPartID() == partID)
            {
                allParts.remove(p);
                return true;
            }
        }
        return false;
    }
    
    public static Part lookupPart(int partID)
    {
        for(Part p : allParts)
        {
            if(p.getPartID() == partID)
            {
                return p;
            }
        }
        return null;
    }
    
    public static void updatePart(Part part)
    {
        allParts.set(part.getPartID(), part);
    }
    
    public static ObservableList<Part> getParts()
    {
        return allParts;
    }
    
    public static int getPartsCount()
    {
        return allParts.size();
    }
    
    public static int getProductsCount()
    {
        return products.size();
    }
    
    public static ObservableList<Product> getProducts()
    {
        return products;
    }
    
}
