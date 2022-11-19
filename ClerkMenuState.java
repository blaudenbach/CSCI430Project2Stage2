import java.util.*;
//import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//import java.text.*;

public class ClerkMenuState extends WareState implements ActionListener{
  private static ClerkMenuState clerkmenustate;
  private Scanner reader = new Scanner(System.in);
  private static Warehouse warehouse;
  private static final int EXIT = 0;
  private static final int ADD_CLIENT = 1;
  private static final int LIST_PRODUCTS = 5;
  private static final int CLIENT_QUERY = 20;
  private static final int ACCEPT_PAYMENT = 13;
  private static final int BECOME_CLIENT = 14;
  private static final int DISPLAY_PRODUCT_WAITLIST = 9;
  private static final int HELP = 21;

  private JFrame frame;
  private AbstractButton addClientButton, listProductsButton, clientQueryButton, acceptPaymentButton, becomeClientButton, displayWaitlistButton, exitButton;

  
  private ClerkMenuState() {
    warehouse = Warehouse.instance();
  }

  public static ClerkMenuState instance() {
    if (clerkmenustate == null) {
      return clerkmenustate = new ClerkMenuState();
    } else {
      return clerkmenustate;
    }
  }

  @Override
  public void actionPerformed(java.awt.event.ActionEvent e) {
    if(e.getSource().equals(this.addClientButton)){
      this.addClient();
    }
    else if(e.getSource().equals(this.listProductsButton)){
      this.displayProducts();
    }
    else if(e.getSource().equals(this.clientQueryButton)){
      this.clientQuery();
    }
    else if(e.getSource().equals(this.acceptPaymentButton)){
      this.acceptPayment();
    }
    else if(e.getSource().equals(this.becomeClientButton)){
      this.becomeClient();
    }
    else if(e.getSource().equals(this.displayWaitlistButton)){
      this.displayProductWaitlist();
    }
    else if(e.getSource().equals(this.exitButton)){
      this.logout();
    }
  }

  public void help() {
    System.out.println("CLERK MENU");
    System.out.println(EXIT + "  | Exit");
    System.out.println(ADD_CLIENT + "  | Add a client");
    System.out.println(LIST_PRODUCTS + "  | List all products and information");
    System.out.println(CLIENT_QUERY + "  | Perform a client query action");
    System.out.println(ACCEPT_PAYMENT + " | Accept a client payment");
    if(WareContext.instance().getLogin() == WareContext.IsClerk){
      System.out.println(BECOME_CLIENT + " | Log in as a client");
    }
    System.out.println(DISPLAY_PRODUCT_WAITLIST + "  | Display a product's waitlist");
    System.out.println(HELP + " | Help");
  }

  public void process() {
    int command;
    help();
    command = Integer.parseInt(reader.nextLine());
    while (command != EXIT) {
      switch (command) {

        case ADD_CLIENT:
            addClient();
            break;
        case LIST_PRODUCTS:
            displayProducts();
            break;
        case CLIENT_QUERY:
            clientQuery();
            break;
        case ACCEPT_PAYMENT:
            acceptPayment();
            break;
        case BECOME_CLIENT:
            if (WareContext.instance().getLogin() == WareContext.IsManager){
              System.out.println("You are logged in as a manager. Please log out first.");
              break;
            }
            becomeClient();
            break;
        case DISPLAY_PRODUCT_WAITLIST:
            displayProductWaitlist();
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

  public void addClient(){
    String name = JOptionPane.showInputDialog(frame, "Enter client name:");
    String address = JOptionPane.showInputDialog(frame, "Enter client address:");

    Client client = warehouse.addClient(name, address);

    if(client == null){
      JOptionPane.showMessageDialog(frame, "Client information invalid.");
    }
    else{
      JOptionPane.showMessageDialog(frame, "Client added.");
    }
  }

  public void displayProducts(){
    warehouse.displayProducts(frame);
  }

  public void clientQuery(){
    WareContext.instance().changeState(4);
  }

  public void acceptPayment(){
    String cid = JOptionPane.showInputDialog(frame, "Enter client ID:");

    float amount = Float.parseFloat(JOptionPane.showInputDialog(frame, "Enter amount to charge client:"));

    warehouse.acceptClientPayment(cid, amount);

  }

  public void becomeClient(){
    String clientID = JOptionPane.showInputDialog(frame, "Enter client ID:");

    if(warehouse.searchClient(clientID)){
      WareContext.instance().setUser(clientID);
      WareContext.instance().changeState(0);
    }
    else{
      JOptionPane.showMessageDialog(frame, "Invalid clientID.");
    }
  }

  public void displayProductWaitlist(){
    String pid = JOptionPane.showInputDialog(frame, "Enter product ID:");

    warehouse.displayProductWaitlist(pid, frame);
  }

  public void run() {
    frame = WareContext.instance().getFrame();
    frame.getContentPane().removeAll();
    frame.getContentPane().setLayout(new FlowLayout());
    addClientButton = new JButton("Add Client");
    addClientButton.addActionListener(this);
    listProductsButton = new JButton("List Products");
    listProductsButton.addActionListener(this);
    clientQueryButton = new JButton("Perform Client Query");
    clientQueryButton.addActionListener(this);
    acceptPaymentButton = new JButton("Accept Payment");
    acceptPaymentButton.addActionListener(this);
    becomeClientButton = new JButton("Become Client");
    becomeClientButton.addActionListener(this);
    displayWaitlistButton = new JButton("Display Product Waitlist");
    displayWaitlistButton.addActionListener(this);
    exitButton = new JButton("Exit");
    exitButton.addActionListener(this);

    frame.getContentPane().add(this.addClientButton);
    frame.getContentPane().add(this.listProductsButton);
    frame.getContentPane().add(this.clientQueryButton);
    frame.getContentPane().add(this.acceptPaymentButton);
    if(WareContext.instance().getLogin() == WareContext.IsClerk){
      frame.getContentPane().add(this.becomeClientButton);
    }
    frame.getContentPane().add(this.displayWaitlistButton);
    frame.getContentPane().add(this.exitButton);
    frame.setVisible(true);
    frame.paint(frame.getGraphics());
    frame.toFront();
    frame.requestFocus();

    //process();
  }

  public void logout()
  {
    //.getLogin is what the user actually IS.
    if (WareContext.instance().getLogin() == WareContext.IsClerk)
       {  //system.out.println(" going to clerk \n ");
        (WareContext.instance()).changeState(1); // [1][1] = 3
       }
    else if (WareContext.instance().getLogin() == WareContext.IsManager) {
      (WareContext.instance()).changeState(2); // [1][2] = 2
    } else {
      (WareContext.instance()).changeState(3); // [1][3] = -2
    }
  }
 
}
