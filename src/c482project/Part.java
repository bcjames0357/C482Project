/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c482project;

/**
 *
 * @author brandon
 */

abstract class Part {
    private int partID, inStock, min, max;
    private double price;
    private String name;

    public Part()
    {
        
    }

    public int getPartID() {
        return partID;
    }

    public void setPartID(int partID) {
        this.partID = partID;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public boolean isValid() throws InventoryException 
    {
        if(getName().equals("")) {
            throw new InventoryException("Please enter a part name.");
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
        
        if(getInStock() < getMin() || getInStock() > getMax())
        {
            throw new InventoryException("The current stock must be between the minimum and maximum inventory.");
        }
        
        return true;
    }
    
    
    
    
    
    
}
