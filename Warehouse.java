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

    public boolean addToClientWishlist(String cid, String pid, int quantity) {
        Client client = clients.findClient(cid);
        Product product = products.findProduct(pid);

        if((client == null) || (product == null)){
            System.out.println("Invalid information entered.");
            return false;
        }

        Entry entry = new Entry(quantity, product);
        WishList wishlist = client.getWishList();
        wishlist.addEntry(entry);
        return true;
    }


    public void displayClients(){
        clients.displayList();
    }

    public void displayProducts(){
        products.displayList();
    }

    public void displayClientWishlist(String cid){
        Client client = clients.findClient(cid);

        if(client == null){
            System.out.println("Invalid information entered.");
            return;
        }

        WishList wishlist = client.getWishList();

        wishlist.displayList();
    }

    public Iterator<?> getProducts() {
        return products.getProducts();
    }

    public Iterator<?> getClients() {
        return clients.getClients();
    }

    public void processClientWishlist(String cid, Scanner reader){
        Invoice invoice = new Invoice();
        Client client = clients.findClient(cid);

        if(client == null){
            System.out.println("Invalid information entered.");
            return;
        }

        for(Iterator<?> current = client.getWishList().getWishList(); current.hasNext();){
            Entry entry = (Entry) current.next();

            //Display entry to user
            System.out.println("Current entry: " + entry.toString());

            //Giver user 3 options
            System.out.println("Select an option:");
            System.out.println("1 - Leave on wishlist");
            System.out.println("2 - Order product with existing quantity");
            System.out.println("3 - Order product with new quantity");
            int choice = Integer.parseInt(reader.nextLine());

            if(choice == 2){
                invoice.addEntry(entry, client);
                current.remove();
            }
            else if(choice == 3){
                System.out.print("Enter new quantity: ");
                int qty = Integer.parseInt(reader.nextLine());
                entry.setQuantity(qty);
                invoice.addEntry(entry, client);
                current.remove();
            }
        }

        System.out.println("Here is the finalized invoice:");
        invoice.displayList();
        client.charge(invoice.getTotal());
        client.addInvoice(invoice);
    }

    public void displayClientsWithBalance(){

        for(Iterator<?> current = clients.getClients(); current.hasNext();){
            Client client = (Client) current.next();
            if(client.getAmountDue() > 0 ){
                System.out.println(client.toString() + ", Amount Due: " + client.getAmountDue());
            }
        }
    }

    public void displayProductWaitlist(String pid){
        Product product = products.findProduct(pid);

        if(product == null){
            System.out.println("Invalid information entered.");
            return;
        }

        Waitlist waitlist = product.getWaitlist();

        waitlist.displayList();
    }

    public void processShipment(String pid, int quantity, Scanner reader){
        Product product = products.findProduct(pid);

        if(product == null){
            System.out.println("Invalid information entered.");
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

            //Display entry to user
            System.out.println("Current request: " + request.toString());

            //Giver user 3 options
            System.out.println("Select an option:");
            System.out.println("1 - Leave on waitlist");
            System.out.println("2 - Order product with existing quantity");
            System.out.println("3 - Order product with new quantity");
            int choice = Integer.parseInt(reader.nextLine());

            if((choice != 2) && (choice !=3)){
                continue;
            }
            else if(choice == 3){
                System.out.print("Enter new quantity: ");
                int qty = Integer.parseInt(reader.nextLine());
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

        System.out.println("Updated product information: " + product.toString());
        System.out.println("Updated product waitlist:");
        product.getWaitlist().displayList();
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

    public void displayClientDetails(String cid){
        Client client = clients.findClient(cid);

        System.out.println(client.toString());
    }

    public void displayClientTransactions(String cid){
        Client client = clients.findClient(cid);

        System.out.println(client.getInvoiceList().toString());
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
}
