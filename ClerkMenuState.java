import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//import java.text.*;

public class ClerkMenuState extends WareState implements ActionListener{
  private static ClerkMenuState clerkmenustate;
  private static Warehouse warehouse;


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
