import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//import java.text.*;

public class ModifyCartState extends WareState implements ActionListener{
    private static ModifyCartState modifycartstate;
    private static Warehouse warehouse;

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
