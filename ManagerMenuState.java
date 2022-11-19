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
  private static final int EXIT = 0;
  private static final int ADD_PRODUCT = 2;
  private static final int RECEIVE_SHIPMENT = 10;
  private static final int BECOME_CLERK = 15;
  private static final int HELP = 22;

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

  public void help() {
    System.out.println("MANAGER MENU");
    System.out.println(EXIT + "  | Exit");
    System.out.println(ADD_PRODUCT + "  | Add a product");
    System.out.println(RECEIVE_SHIPMENT + " | Receive shipment for a product");
    System.out.println(BECOME_CLERK + " | Become a clerk");
    System.out.println(HELP + " | for help");
  }

  public void process() {
    int command;
    help();
    command = Integer.parseInt(reader.nextLine());
    while (command != EXIT) {
      switch (command) {

        case ADD_PRODUCT:
            addProduct();
            break;
        case RECEIVE_SHIPMENT:
            receiveShipment();
            break;
        case BECOME_CLERK:
            becomeClerk();
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

