import java.util.*;
//import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//import java.text.*;

public class ManagerMenuState extends WareState implements ActionListener{

  private static ManagerMenuState managermenustate;
  private Scanner reader = new Scanner(System.in);
  private static Warehouse warehouse;

  private JFrame frame;
  private AbstractButton addProductButton, receiveShipmentButton, becomeClerkButton, exitButton;

  private ManagerMenuState() {
    warehouse = Warehouse.instance();
  }

  public static ManagerMenuState instance() {
    if (managermenustate == null) {
      return managermenustate = new ManagerMenuState();
    } else {
      return managermenustate;
    }
  }

  @Override
  public void actionPerformed(java.awt.event.ActionEvent e) {
    if(e.getSource().equals(this.addProductButton)){
      this.addProduct();
    }
    else if(e.getSource().equals(this.receiveShipmentButton)){
      this.receiveShipment();
    }
    else if(e.getSource().equals(this.becomeClerkButton)){
      this.becomeClerk();
    }
    else if(e.getSource().equals(this.exitButton)){
      this.logout();
    }
  }

  public void addProduct(){
    String name = JOptionPane.showInputDialog(frame, "Enter product name:");
    double price = Double.parseDouble(JOptionPane.showInputDialog(frame, "Enter product price:"));

    int inStock = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter amount of product in stock:"));

    Product product = warehouse.addProduct(name, price, inStock);

    if(product == null){
        JOptionPane.showMessageDialog(frame, "Product information invalid.");
    }
    else{
      JOptionPane.showMessageDialog(frame, "Product added.");
    }
  }

  public void receiveShipment(){
    String pid = JOptionPane.showInputDialog(frame, "Enter ID of product in shipment:");

    int qty = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter quantity of product in shipment:"));

    warehouse.processShipment(pid, qty, reader, frame);
  }

  public void becomeClerk(){
    WareContext.instance().changeState(1); //[2][1] = 1
  }

  public void run() {
    frame = WareContext.instance().getFrame();
    frame.getContentPane().removeAll();
    frame.getContentPane().setLayout(new FlowLayout());
    addProductButton = new JButton("Add Product");
    addProductButton.addActionListener(this);
    receiveShipmentButton = new JButton("Receive Shipment");
    receiveShipmentButton.addActionListener(this);
    becomeClerkButton = new JButton("Become Clerk");
    becomeClerkButton.addActionListener(this);
    exitButton = new JButton("Exit");
    exitButton.addActionListener(this);

    frame.getContentPane().add(this.addProductButton);
    frame.getContentPane().add(this.receiveShipmentButton);
    frame.getContentPane().add(this.becomeClerkButton);
    frame.getContentPane().add(this.exitButton);
    frame.setVisible(true);
    frame.paint(frame.getGraphics());
    frame.toFront();
    frame.requestFocus();


    //process();
  }

    
  public void logout(){
      if (WareContext.instance().getLogin() == WareContext.IsManager) {
          //Logout of manager -> login
          (WareContext.instance()).changeState(2); // [2][2] = 3
      } else {  //Should not happen this an error
          (WareContext.instance()).changeState(3); // [2][3] = -2
      }
  }
}

