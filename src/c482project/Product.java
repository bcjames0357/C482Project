/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c482project;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author brandon
 */
class Product {
    
    private int productID, inStock, min, max;
    private double price;
    private String name;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    public Product()
    {
        
    }
    
    public int getProductID() 
    {
        return productID;
    }

    public void setProductID(int productID) 
    {
        this.productID = productID;
    }

    public int getInStock() 
    {
        return inStock;
    }

    public void setInStock(int inStock) 
    {
        this.inStock = inStock;
    }

    public int getMin() 
    {
        return min;
    }

    public void setMin(int min) 
    {
        this.min = min;
    }

    public int getMax()
    {
        return max;
    }

    public void setMax(int max) 
    {
        this.max = max;
    }

    public double getPrice() 
    {
        return price;
    }

    public void setPrice(double price) 
    {
        this.price = price;
    }

    public String getName() 
    {
        return name;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public void addAssociatedPart(Part part) 
    {
        this.associatedParts.add(part);
    }

    public boolean removeAssociatedParts(int partID) 
    {
        return this.associatedParts.remove(lookupAssociatedPart(partID));
    }
    
    public Part lookupAssociatedPart(int partID)
    {
        for(Part p : associatedParts)
        {
            if(p.getPartID() == partID) 
            {
                return p;
            }
        }
        return null;
    }
    
    public ObservableList<Part> getAssociatedParts()
    {
        return associatedParts;
    }
    
    public int getAssociatedPartsCount()
    {
        return associatedParts.size();
    }
    
    public boolean isValid() throws InventoryException
    {
        double partsPrice = 0.00;
        
        for(Part p : getAssociatedParts())
        {
            partsPrice += p.getPrice();
        }
        
        if(getName().equals("")) {
            throw new InventoryException("Please enter a product name.");
        }
        
        if(getPrice() < 0)
        {
            throw new InventoryException("The price must be greater than zero.");
        }
        
        if(getMin() < 0) 
        {
            throw new InventoryException("The minimum inventory must be greater than zero.");
        }
        
        if(getMin() > getMax())
        {
            throw new InventoryException("The minimum inventory must be less than the maximum inventory");
        }
        
        if(getInStock() <= 0)
        {
            throw new InventoryException("The current stock cannot be negative.");
        }
        
        if(getAssociatedPartsCount() < 1)
        {
            throw new InventoryException("At least one part must be associated with the product.");
        }
        
        if(partsPrice > getPrice())
        {
            throw new InventoryException("The product price must be greater than the cost of its associated parts.");
        }
        
        if(getInStock() < getMin() || getInStock() > getMax()) 
        {
            throw new InventoryException("The current inventory must be between the product's minimum and maximum stock levels.");
        }
        
        return true;
    }

    public void purgeAssociatedParts()
    {
        associatedParts = FXCollections.observableArrayList();
    }
}
