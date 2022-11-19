import javax.swing.*;
import java.util.*;
import java.io.*;

public class Warehouse implements Serializable {
    private ProductList products;
    private ClientList clients;
    private static Warehouse warehouse;

    private Warehouse() {
        products = ProductList.instance();
        clients = ClientList.instance();
    }

    public static Warehouse instance() {
        if (warehouse == null){
            return (warehouse = new Warehouse());
        } else {
            return warehouse;
        }
    }

    public Product addProduct(String name, double salePrice, int inStock) {
        Product product = new Product(name, salePrice, inStock);
        if (products.addProduct(product)) {
            return (product);
        }
        return null;
    }

    public Client addClient(String name, String address) {
        Client client = new Client(name, address);
        if (clients.addClient(client)) {
            return (client);
        }
        return null;
    }

    public boolean addToClientWishlist(String cid, String pid, int quantity, JFrame frame) {
        Client client = clients.findClient(cid);
        Product product = products.findProduct(pid);

        if((client == null) || (product == null)){
            JOptionPane.showMessageDialog(frame, "Invalid information entered.");
            return false;
        }

        Entry entry = new Entry(quantity, product);
        WishList wishlist = client.getWishList();
        wishlist.addEntry(entry);
        return true;
    }

    public boolean removeFromClientWishlist(String cid, String pid, JFrame frame){
        Client client = clients.findClient(cid);
        Product product = products.findProduct(pid);


        if((client == null) || (product == null)){
            JOptionPane.showMessageDialog(frame, "Invalid information entered.");
            return false;
        }

        WishList wishlist = client.getWishList();
        Entry entry = wishlist.findEntry(product);

        return wishlist.removeEntry(entry);
    }

    public boolean editWishlistQuantity(String cid, String pid, int quantity, JFrame frame){
        Client client = clients.findClient(cid);
        Product product = products.findProduct(pid);

        if((client == null) || (product == null)){
            JOptionPane.showMessageDialog(frame, "Invalid information entered.");
            return false;
        }

        WishList wishlist = client.getWishList();
        Entry entry = wishlist.findEntry(product);

        return wishlist.editEntryQuantity(entry, quantity);
    }


    public void displayClients(JFrame frame){
        clients.displayList(frame);
    }

    public void displayProducts(JFrame frame){
        products.displayList(frame);
    }

    public void displayClientWishlist(String cid, JFrame frame){
        Client client = clients.findClient(cid);

        if(client == null){
            JOptionPane.showMessageDialog(frame, "Invalid information entered.");
            return;
        }

        WishList wishlist = client.getWishList();

        wishlist.displayList(frame);
    }

    public Iterator<?> getProducts() {
        return products.getProducts();
    }

    public Iterator<?> getClients() {
        return clients.getClients();
    }

    public void processClientWishlist(String cid, Scanner reader, JFrame frame){
        Invoice invoice = new Invoice();
        Client client = clients.findClient(cid);

        if(client == null){
            JOptionPane.showMessageDialog(frame, "Invalid information entered.");
            return;
        }

        for(Iterator<?> current = client.getWishList().getWishList(); current.hasNext();){
            Entry entry = (Entry) current.next();

            //Display entry to user
            int choice = Integer.parseInt(JOptionPane.showInputDialog(frame, "Current entry" + entry.toString()
                + "\nSelect an option:\n1 - Leave on wishlist\n2 - Order product with existing quantity\n"
                + "3 - Order product with new quantity"));

            if(choice == 2){
                invoice.addEntry(entry, client);
                current.remove();
            }
            else if(choice == 3){
                int qty = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter new quantity:"));
                entry.setQuantity(qty);
                invoice.addEntry(entry, client);
                current.remove();
            }
        }

        invoice.displayList(frame);
        client.charge(invoice.getTotal());
        client.addInvoice(invoice);
    }

    public void displayClientsWithBalance(JFrame frame){
        String lineString = "";
        for(Iterator<?> current = clients.getClients(); current.hasNext();){
            Client client = (Client) current.next();
            if(client.getAmountDue() > 0 ){
                lineString += client.toString() + ", Amount Due: " + Float.toString(client.getAmountDue()) + "\n";
            }
        }
        JOptionPane.showMessageDialog(frame, lineString);
    }

    public void displayProductWaitlist(String pid, JFrame frame){
        Product product = products.findProduct(pid);

        if(product == null){
            JOptionPane.showMessageDialog(frame, "Invalid information entered.");
            return;
        }

        Waitlist waitlist = product.getWaitlist();

        waitlist.displayList(frame);
    }

    public void processShipment(String pid, int quantity, Scanner reader, JFrame frame){
        Product product = products.findProduct(pid);

        if(product == null){
            JOptionPane.showMessageDialog(frame, "Invalid information entered.");
            return;
        }

        Shipment shipment = new Shipment(quantity, product);
        product.addStock(shipment.getQuantity());

        for(Iterator<?> current = product.getWaitlist().getWaitlist(); current.hasNext();){
            int shipAmt = shipment.getQuantity();

            if(shipAmt == 0){
                break;
            }

            Request request = (Request) current.next();

            int choice = Integer.parseInt(JOptionPane.showInputDialog(frame, "Current request: " + request.toString() + "\n" +
                "Select and option:\n1 - Leave on waitlist\n2 - Order product with existing quantity\n3 - Order product with new quantity"));

            if((choice != 2) && (choice !=3)){
                continue;
            }
            else if(choice == 3){
                int qty = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter new quantity:"));
                request.setQuantity(qty);
            }

            Entry entry = new Entry(request.getQuantity(), product);

            if(entry.getQuantity() > shipAmt){
                entry.setQuantity(shipAmt);
                request.setQuantity(request.getQuantity() - entry.getQuantity());
            }else{
                current.remove();
            }

            shipment.removeQuantity(entry.getQuantity());

            Invoice invoice = new Invoice();
            invoice.addEntry(entry, request.getClient());
            request.getClient().charge(invoice.getTotal());
        }

        product.getWaitlist().displayList(frame);
    }

    public String toString() {
        return products + "\n" + clients;
    }

    public static Warehouse retrieve(){
        try {
            FileInputStream file = new FileInputStream("WarehouseData");
            ObjectInputStream in = new ObjectInputStream(file);
            //List all objects that need to be read.
            in.readObject();
            ClientIdServer.retrieve(in);
            ProductIdServer.retrieve(in);
            return warehouse;
        } catch(IOException ioe) {
            ioe.printStackTrace();
            return null;
        } catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            return null;
        }
    }

    public boolean save(){
        try {
            FileOutputStream file = new FileOutputStream("WarehouseData");
            try (ObjectOutputStream out = new ObjectOutputStream(file)) {
                //List all objects that need to be written
                out.writeObject(warehouse);
                out.writeObject(ClientIdServer.instance());
                out.writeObject(ProductIdServer.instance());
            }
            return true;
        } catch(IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
        
    }

    private void writeObject(java.io.ObjectOutputStream output){
        try{
            output.defaultWriteObject();
            output.writeObject(warehouse);
        } catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    private void readObject(java.io.ObjectInputStream input){
        try{
            if(warehouse != null){
                return;
            } else{
                input.defaultReadObject();
                if(warehouse == null){
                    warehouse = (Warehouse) input.readObject();
                } else {
                    input.readObject();
                }
            }
        } catch (IOException ioe){
            ioe.printStackTrace();
        } catch(ClassNotFoundException cnfe){
            cnfe.printStackTrace();
        }
        
    }

    public void displayClientDetails(String cid, JFrame frame){
        Client client = clients.findClient(cid);

        JOptionPane.showMessageDialog(frame, client.toString());
    }

    public void displayClientTransactions(String cid, JFrame frame){
        Client client = clients.findClient(cid);

        JOptionPane.showMessageDialog(frame, client.getInvoiceList().toString());
    }

    public void acceptClientPayment(String cid, float amount){
        Client client = clients.findClient(cid);

        client.pay(amount);
    }

    public boolean searchClient(String cid){
        Client client = clients.findClient(cid);

        if(client == null){
            return false;
        }
        else{
            return true;
        }
    }

    public void displayInactiveClients(JFrame frame){
        clients.displayInactiveClients(frame);
    }
}
