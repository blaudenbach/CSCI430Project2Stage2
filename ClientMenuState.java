import java.util.*;
//import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//import java.text.*;

public class ClientMenuState extends WareState implements ActionListener{
  private static ClientMenuState clientState;
  private Scanner reader = new Scanner(System.in);
  private static Warehouse warehouse;
  private static final int EXIT = 0;
  private static final int MODIFY_CART = 3;
  private static final int DISPLAY_PRODUCTS = 5;
  private static final int DISPLAY_WISHLIST = 6;
  private static final int PLACE_ORDER = 7;
  private static final int SHOW_DETAILS = 11;
  private static final int DISPLAY_TRANSACTIONS = 12;
  private static final int HELP = 22;

  private JFrame frame;
  private AbstractButton modifyCartButton, listProductsButton, displayWishlistButton, placeOrderButton, showDetailsButton, displayTransactionsButton, exitButton;

  private ClientMenuState() {
    warehouse = Warehouse.instance();
  }

  public static ClientMenuState instance() {
    if (clientState == null) {
      return clientState = new ClientMenuState();
    } else {
      return clientState;
    }
  }

  @Override
  public void actionPerformed(java.awt.event.ActionEvent e) {
    if(e.getSource().equals(this.modifyCartButton)){
      this.modifyCart();
    }
    else if(e.getSource().equals(this.listProductsButton)){
      this.displayProducts();
    }
    else if(e.getSource().equals(this.displayWishlistButton)){
      this.displayWishlist();
    }
    else if(e.getSource().equals(this.placeOrderButton)){
      this.placeOrder();
    }
    else if(e.getSource().equals(this.showDetailsButton)){
      this.showDetails();
    }
    else if(e.getSource().equals(this.displayTransactionsButton)){
      this.displayTransactions();
    }
    else if(e.getSource().equals(this.exitButton)){
      this.logout();
    }
  }

  public void help() {
    System.out.println("CLIENT MENU");
    System.out.println(EXIT + "  | Exit");
    System.out.println(MODIFY_CART + "  | Modify the shopping cart");
    System.out.println(DISPLAY_PRODUCTS + "  | Display products with prices");
    System.out.println(DISPLAY_WISHLIST + "  | Display wishlist");
    System.out.println(PLACE_ORDER + "  | Place an order");
    System.out.println(SHOW_DETAILS + " | Display client details");
    System.out.println(DISPLAY_TRANSACTIONS + " | Display transactions");
    System.out.println(HELP + " | Help");
  }

  public void process() {
    int command;
    help();
    command = Integer.parseInt(reader.nextLine());
    while (command != EXIT) {
      switch (command) {

        case MODIFY_CART:
            modifyCart();
            break;
        case DISPLAY_PRODUCTS:
            displayProducts();
            break;
        case DISPLAY_WISHLIST:
            displayWishlist();
            break;
        case PLACE_ORDER:
            placeOrder();
            break;
        case SHOW_DETAILS:
            showDetails();
            break;
        case DISPLAY_TRANSACTIONS:
            displayTransactions();
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

  public void modifyCart(){
    WareContext.instance().changeState(3);
  }

  public void displayProducts(){
    warehouse.displayProducts(frame);
  }

  public void displayWishlist(){
    String clientID = WareContext.instance().getClient();

    warehouse.displayClientWishlist(clientID, frame);
  }

  public void placeOrder(){
    String clientID = WareContext.instance().getClient();

    warehouse.processClientWishlist(clientID, reader, frame);
  }

  public void showDetails(){
    String clientID = WareContext.instance().getClient();

    warehouse.displayClientDetails(clientID, frame);
  }

  public void displayTransactions(){
    String clientID = WareContext.instance().getClient();

    warehouse.displayClientTransactions(clientID, frame);
  }

  public void run() {
    frame = WareContext.instance().getFrame();
    frame.getContentPane().removeAll();
    frame.getContentPane().setLayout(new FlowLayout());
    modifyCartButton = new JButton("Modify Cart");
    modifyCartButton.addActionListener(this);
    listProductsButton = new JButton("List Products");
    listProductsButton.addActionListener(this);
    displayWishlistButton = new JButton("Display Wishlist");
    displayWishlistButton.addActionListener(this);
    placeOrderButton = new JButton("Place Order");
    placeOrderButton.addActionListener(this);
    showDetailsButton = new JButton("Show Details");
    showDetailsButton.addActionListener(this);
    displayTransactionsButton = new JButton("Display Transactions");
    displayTransactionsButton.addActionListener(this);
    exitButton = new JButton("Exit");
    exitButton.addActionListener(this);

    frame.getContentPane().add(this.modifyCartButton);
    frame.getContentPane().add(this.listProductsButton);
    frame.getContentPane().add(this.displayWishlistButton);
    frame.getContentPane().add(this.placeOrderButton);
    frame.getContentPane().add(this.showDetailsButton);
    frame.getContentPane().add(this.displayTransactionsButton);
    frame.getContentPane().add(this.exitButton);
    frame.setVisible(true);
    frame.paint(frame.getGraphics());
    frame.toFront();
    frame.requestFocus();
    
    //process();
  }

  public void logout()
  {
    if ((WareContext.instance()).getLogin() == WareContext.IsClient)
    { //client->client logout go to logout
      (WareContext.instance()).changeState(0); // [0][0] = 3
    }
    else if (WareContext.instance().getLogin() == WareContext.IsClerk)
    {  //clerk->client logout go to clerk
        (WareContext.instance()).changeState(1); // [0][1] = 1
    }
    else {
      (WareContext.instance()).changeState(3); // [0][3] = -2
    }
  }
}
