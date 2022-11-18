import java.util.*;
import java.io.*;
public class InvoiceList implements Serializable{
    private static final long serialVersionUID = 1L;
    private LinkedList<Invoice> invoices;
    private static InvoiceList invoiceList;

    public InvoiceList(){
        invoices = new LinkedList<Invoice>();
    }

    public boolean addInvoice(Invoice invoice){
        invoices.add(invoice);

        return true;
    }

    public String toString(){
        return invoices.toString();
    }

    private static void writeObject(java.io.ObjectOutputStream output) {
        try{
          output.defaultWriteObject();
          output.writeObject(invoiceList);
        } catch(IOException ioe){
          ioe.printStackTrace();
        } //end try-catch block
      }//end writeObject
    
    //---------------readObject------------------------
      private static void readObject(java.io.ObjectInputStream input){
        try{
          if(invoiceList != null)
            return;
          else{
            input.defaultReadObject();
            if(invoiceList == null){
              invoiceList = (InvoiceList) input.readObject();
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

}