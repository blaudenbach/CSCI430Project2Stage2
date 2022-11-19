import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//import java.text.*;


public class LoginState extends WareState implements ActionListener{
  //private WareContext context;
  private JFrame frame;
  private static LoginState instance;
  private AbstractButton clientButton, clerkButton, managerButton, exitButton;
  private LoginState() {
      super();

     // context = LibContext.instance();
  }

  public static LoginState instance() {
    if (instance == null) {
      instance = new LoginState();
    }
    return instance;
  }

  @Override
  public void actionPerformed(java.awt.event.ActionEvent e) {
    if(e.getSource().equals(this.clientButton)){
      this.client();
    }
    else if(e.getSource().equals(this.clerkButton)){
      this.clerk();
    }
    else if(e.getSource().equals(this.managerButton)){
      this.manager();
    }
    else if(e.getSource().equals(this.exitButton)){
      WareContext.instance().changeState(5);
    }
  }

  public void clear(){
    frame.getContentPane().removeAll();
    frame.paint(frame.getGraphics());
  }

  private void client(){  //Client
    String clientID = JOptionPane.showInputDialog(frame, "Enter client ID: ");
    if (Warehouse.instance().searchClient(clientID)){  //Warehouse.instance()
      (WareContext.instance()).setLogin(WareContext.IsClient);
      (WareContext.instance()).setUser(clientID);
      (WareContext.instance()).changeState(0);
    }
    else 
    JOptionPane.showMessageDialog(frame, "Invalid client ID.");
  } 

  private void clerk(){
    (WareContext.instance()).setLogin(WareContext.IsClerk);
    (WareContext.instance()).changeState(1);
  }

  private void manager(){
    (WareContext.instance()).setLogin(WareContext.IsManager);
    (WareContext.instance()).changeState(2);
  }

  public void run() {
    frame = WareContext.instance().getFrame();
    frame.getContentPane().removeAll();
    frame.getContentPane().setLayout(new FlowLayout());
    clientButton = new JButton("Client");
    clientButton.addActionListener(this);
    clerkButton = new JButton("Clerk");
    clerkButton.addActionListener(this);
    managerButton = new JButton("Manager");
    managerButton.addActionListener(this);
    exitButton = new JButton("Exit");
    exitButton.addActionListener(this);

    frame.getContentPane().add(this.clientButton);
    frame.getContentPane().add(this.clerkButton);
    frame.getContentPane().add(this.managerButton);
    frame.getContentPane().add(this.exitButton);
    frame.setVisible(true);
    frame.paint(frame.getGraphics());
    frame.toFront();
    frame.requestFocus();
    //process();
  }
}
