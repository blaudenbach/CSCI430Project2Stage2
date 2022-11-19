import java.io.Serializable;
import java.util.*;
import java.time.*;
import javax.swing.*;

public class Invoice implements Serializable {
    private LinkedList<Entry> entries;
    private static Invoice invoice;
    private float total;
    private LocalDate date;

    public Invoice(){
        entries = new LinkedList<Entry>();
        total = 0;
        date = LocalDate.now();
    }

    public static Invoice instance(){
        if (invoice == null) {
            return (invoice = new Invoice());
        }
        else {
            return invoice;
        }
    }

    public boolean addEntry(Entry entry, Client client)
    {
        //Get amount of product in stock
        int stock = entry.getProduct().getStock();

        //Update product quantity and add out of stock item to waitlist
        if(entry.getQuantity() > stock){
            int numRequested = entry.getQuantity() - stock;
            entry.setQuantity(stock);

            //Add numRequested to the products waitlist
            Waitlist waitlist = entry.getProduct().getWaitlist();
            Request request = new Request(numRequested, client);
            waitlist.addRequest(request);
            System.out.println("The product is partially out of stock.\nOrdered " + stock + " and waitlisted " + numRequested + "."); //Let's the user know not all of it was ordered.
        }

        //Add entry and update stock
        entries.add(entry);
        entry.getProduct().removeStock(entry.getQuantity());

        //Update total
        total += (entry.getQuantity() * entry.getProduct().getSalePrice());

        return true;
    }

    public String toString() {
        return entries.toString();
    }

    public void displayList(JFrame frame){
        JOptionPane.showMessageDialog(frame, toString());
    }

    public float getTotal(){
        return total;
    }

    public LocalDate getDate(){
        return date;
    }

}
