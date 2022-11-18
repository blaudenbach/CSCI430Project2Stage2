import java.util.*;

public class ModifyCartState extends WareState{
    private static ModifyCartState modifycartstate;
    private Scanner reader = new Scanner(System.in);
    private static Warehouse warehouse;
    private static final int EXIT = 0;
    private static final int VIEW_CART = 16;
    private static final int ADD_TO_WISHLIST = 17;
    private static final int REMOVE_FROM_WISHLIST = 18;
    private static final int EDIT_QUANTITY = 19;
    private static final int HELP = 22;
    private ModifyCartState() {
        warehouse = Warehouse.instance();
    }
  
    public static ModifyCartState instance() {
        if (modifycartstate == null) {
        return modifycartstate = new ModifyCartState();
        } else {
        return modifycartstate;
        }
    }
  
    public void help() {
        System.out.println("MODIFY CART MENU");
        System.out.println(EXIT + "  | Exit");
        System.out.println(VIEW_CART + "  | View the cart");
        System.out.println(ADD_TO_WISHLIST + " | Add an item to the wishlist");
        System.out.println(REMOVE_FROM_WISHLIST + " | Remove an item from the wishist");
        System.out.println(EDIT_QUANTITY + " | Edit an item's quantity");
        System.out.println(HELP + " | for help");
    }
  
    public void process() {
        int command;
        help();
        command = Integer.parseInt(reader.nextLine());
        while (command != EXIT) {
            switch (command) {
                case VIEW_CART:
                    viewCart();
                    break;
                case ADD_TO_WISHLIST:
                    addToWishlist();
                    break;
                case REMOVE_FROM_WISHLIST:
                    removeFromWishlist();
                    break;
                case EDIT_QUANTITY:
                    editQuantity();
                    break;
                case HELP:
                    help();
                    break;
                default:
                System.out.println("Invalid choice");
            }
            help();
            command = Integer.parseInt(reader.nextLine());
        }
        logout();
    }

    public void viewCart(){
        String clientID = WareContext.instance().getClient();
    
        warehouse.displayClientWishlist(clientID);
    }

    public void addToWishlist(){
        String clientID = WareContext.instance().getClient();

        System.out.println("Enter product ID: ");
        String pid = reader.nextLine();

        System.out.println("Enter quantity to add: ");
        int qty = Integer.parseInt(reader.nextLine());

        if(warehouse.addToClientWishlist(clientID, pid, qty)){
            System.out.println("Product has been added to wishlist.");
        }
        else{
            System.out.println("Invalid information entered.");
        }
    }

    public void removeFromWishlist(){
        String clientID = WareContext.instance().getClient();

        System.out.println("Enter product ID: ");
        String pid = reader.nextLine();

        if(warehouse.removeFromClientWishlist(clientID, pid)){
            System.out.println("Product removed from wishlist.");
        }
        else{
            System.out.println("Invalid information entered.");
        }
    }

    public void editQuantity(){
        String clientID = WareContext.instance().getClient();

        System.out.println("Enter product ID: ");
        String pid = reader.nextLine();

        System.out.println("Enter product quantity: ");
        int quantity = Integer.parseInt(reader.nextLine());

        if(warehouse.editWishlistQuantity(clientID, pid, quantity)){
            System.out.println("Product quantity has been updated.");
        }
        else{
            System.out.println("Invalid information entered.");
        }
    }
  
    public void run(){
        process();
    }
      
    public void logout(){
        WareContext.instance().changeState(0);
    }

}
