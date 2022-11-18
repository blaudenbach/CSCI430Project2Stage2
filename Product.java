
/**************************************************************************
Product Class 
 Create product object. Each product has a description, id,
 Sale Price and amount in stock.
***************************************************************************/

import java.io.*;

public class Product implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private String name;
  private String id;
  private int inStock;
  private double salePrice;
  private static final String PRODUCT_STRING = "P";
  private Waitlist waitlist;

//----------Contructor-------------------
  public Product(String n, double sp, int st){
    id = PRODUCT_STRING + (ProductIdServer.instance().getId());
    name = n;
    salePrice = sp;
    inStock = st;
    waitlist = new Waitlist();
  }

//---------accessors------------------
  public String getProductNumber(){
    return id;   }

  public String getName(){
    return name;  }

  public double getSalePrice(){
    return salePrice;     }

  public int getStock(){
	return inStock;  }

  
  public String toString(){
    String string = " Id: " + id + ", Name: " + name + ", Sale Price: " + salePrice + ", Stock: " + inStock;
    return string;
  }
    
    
//-----------mutators----------------
  public void setName(String input){
    name = input;  }
    
  public void setSalePrice(double input){
    salePrice = input;     }

    
  public void setData(){
    System.out.println("To use data Setter input data members in this order\n\tString name\n\tdouble salePrice");
    }
    
  public void setData(String n, double sp, int st){
    name            =    n;
    salePrice       =   sp;
    inStock 		=   st;

    }
    
    
//-------------removeStock------------
// Remove specified amount of stock
//------------------------------------
  public void removeStock(int r){
	inStock -= r;
  }//end removeStock
  
//-----------AddItem-----------------
// Add Item to Stock from Shipment
//-----------------------------------
  public void addStock(int quantity){
	  inStock += quantity;
  }//end addShippedItem

  public Waitlist getWaitlist(){
    return waitlist;
  }
	  
}//end Product Class





