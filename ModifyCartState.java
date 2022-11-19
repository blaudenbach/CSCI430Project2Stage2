import java.util.*;
//import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//import java.text.*;

public class ModifyCartState extends WareState implements ActionListener{
    private static ModifyCartState modifycartstate;
    private Scanner reader = new Scanner(System.in);
    private static Warehouse warehouse;
    private static final int EXIT = 0;
    private static final int VIEW_CART = 16;
    private static final int ADD_TO_WISHLIST = 17;
    private static final int REMOVE_FROM_WISHLIST = 18;
    private static final int EDIT_QUANTITY = 19;
    private static final int HELP = 22;

    private JFrame frame;
    private AbstractButton viewCartButton, addToWishlistButton, removeFromWishlistButton, editQuantityButton, exitButton;

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

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
      if(e.getSource().equals(this.viewCartButton)){
        this.viewCart();
      }
      else if(e.getSource().equals(this.addToWishlistButton)){
        this.addToWishlist();
      }
      else if(e.getSource().equals(this.removeFromWishlistButton)){
        this.removeFromWishlist();
      }
      else if(e.getSource().equals(this.editQuantityButton)){
        this.editQuantity();
      }
      else if(e.getSource().equals(this.exitButton)){
        this.logout();
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
    
        warehouse.displayClientWishlist(clientID, frame);
    }

    public void addToWishlist(){
        String clientID = WareContext.instance().getClient();

        String pid = JOptionPane.showInputDialog(frame, "Enter product ID:");

        int qty = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter quantity to add:"));

        if(warehouse.addToClientWishlist(clientID, pid, qty, frame)){
           JOptionPane.showMessageDialog(frame, "Product has been added to wishlist.");
        }
        else{
            JOptionPane.showMessageDialog(frame, "Invalid information entered.");
        }
    }

    public void removeFromWishlist(){
        String clientID = WareContext.instance().getClient();

        String pid = JOptionPane.showInputDialog(frame, "Enter product ID:");

        if(warehouse.removeFromClientWishlist(clientID, pid, frame)){
            JOptionPane.showMessageDialog(frame, "Product removed from wishlist.");
        }
        else{
            JOptionPane.showMessageDialog(frame, "Invalid information entered.");
        }
    }

    public void editQuantity(){
        String clientID = WareContext.instance().getClient();

        String pid = JOptionPane.showInputDialog(frame, "Enter product ID:");

        int quantity = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter product quantity:"));

        if(warehouse.editWishlistQuantity(clientID, pid, quantity, frame)){
            JOptionPane.showMessageDialog(frame, "Product quantity has been updated.");
        }
        else{
            JOptionPane.showMessageDialog(frame, "Invalid information entered.");
        }
    }
  
    public void run(){
        frame = WareContext.instance().getFrame();
        frame.getContentPane().removeAll();
        frame.getContentPane().setLayout(new FlowLayout());
        viewCartButton = new JButton("View Cart");
        viewCartButton.addActionListener(this);
        addToWishlistButton = new JButton("Add to Wishlist");
        addToWishlistButton.addActionListener(this);
        removeFromWishlistButton = new JButton("Remove from Wishlist");
        removeFromWishlistButton.addActionListener(this);
        editQuantityButton = new JButton("Edit Wishlist Quantity");
        editQuantityButton.addActionListener(this);
        exitButton = new JButton("Exit");
        exitButton.addActionListener(this);
    
        frame.getContentPane().add(this.viewCartButton);
        frame.getContentPane().add(this.addToWishlistButton);
        frame.getContentPane().add(this.removeFromWishlistButton);
        frame.getContentPane().add(this.editQuantityButton);
        frame.getContentPane().add(this.exitButton);
        frame.setVisible(true);
        frame.paint(frame.getGraphics());
        frame.toFront();
        frame.requestFocus();
        
        //process();
    }
      
    public void logout(){
        WareContext.instance().changeState(3);
    }

}
