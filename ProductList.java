/********************************************************************
  Product List Class
        List of products in warehouse system
**********************************************************************/

import java.util.*;
import java.io.*;
import javax.swing.*;

public class ProductList implements Serializable{
  private static final long serialVersionUID = 1L;
 
  private List<Product> products = new LinkedList<Product>();
  private static ProductList pList;

    
//----------Constructor-------------
  public ProductList(){    }

    
//----------instance()--------------------
  public static ProductList instance() {
  	if(pList == null){
  		return (pList = new ProductList());
  	} else{
  		return pList;
  	}//end ifelse
  }//end instance()

    
//----------accessor---------------------
   public Iterator<?> getProducts(){
     return products.iterator();  }

//----------size-------------
// size of product List
//---------------------------
    public int size(){
        return products.size();
    }//end size()
    
//-------------displayList-----------------
// Display Product List
//-----------------------------------------
    public void displayList(JFrame frame){
        JOptionPane.showMessageDialog(frame, toString());
    }//end DisplayList
    
//-----------Add Product--------------------
// Add product to product List
//------------------------------------------
  public boolean addProduct(Product product){
    products.add(product);
    return true;
  }//end addProduct

  public boolean addProducts(Product...multipleProducts) {
    for (Product product : multipleProducts) {
      products.add(product);
    }
    return true;
  }
    
//----------------findProduct----------------------
// Find product in product List by product id
// returns Null if id not found in list
//-----------------------------------------------
  public Product findProduct(String id){
    for(Iterator<?> current = products.iterator(); current.hasNext();){
      Product P = (Product) current.next();
      if(P.getProductNumber().equals(id)){
        return P;
      }
    }
    return null;
  }
    
//---------------writeObject---------------------
  private static void writeObject(java.io.ObjectOutputStream output) {
    try{
      output.defaultWriteObject();
      output.writeObject(pList);
    } catch(IOException ioe){
      ioe.printStackTrace();
    } //end try-catch block
  }//end writeObject

//---------------readObject------------------------
  private static void readObject(java.io.ObjectInputStream input){
    try{
      if(pList != null)
        return;
      else{
        input.defaultReadObject();
        if(pList == null){
          pList = (ProductList) input.readObject();
        } else {
          input.readObject();
        }//end if-else
      }//end if-else
    } catch (IOException ioe){
      ioe.printStackTrace();
    } catch(ClassNotFoundException cnfe) {
      cnfe.printStackTrace();
    }//end try-catch block
  }//end readObject
//
//-----------toString()------------------------
  public String toString(){
  	String returnedString = "";
  	Iterator<?> curr = products.iterator();
  	while(curr.hasNext())
  		returnedString = returnedString.concat(curr.next().toString() + '\n');
  	return returnedString;
  }//end toString

//------------Search--------------------------
// Searches by product ID
//--------------------------------------------
  public List<Product> search(String parameter){
    List<Product> returnProducts = new ArrayList<Product>();
    Iterator<?> current = products.iterator();
    while(current.hasNext()){
      Product tProduct = (Product)current.next();
      if(tProduct.getProductNumber() == parameter){
        returnProducts.add(tProduct);
      }//end if parameter equals
    }//end while

    return returnProducts;
  }//end search with id

 }
