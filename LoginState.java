import java.util.*;
//import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//import java.text.*;


public class LoginState extends WareState implements ActionListener{
  private static final int CLIENT_LOGIN = 0;
  private static final int CLERK_LOGIN = 1;
  private static final int MANAGER_LOGIN = 2;
  private static final int EXIT = 5;
  private Scanner reader = new Scanner(System.in);
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
    System.out.print("Enter client ID: ");
    String clientID = JOptionPane.showInputDialog(frame, "Input client ID: ");
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

  public void process() {
    int command;
    System.out.println("LOGIN SCREEN");
    System.out.println("0 | Login as Client\n"+ 
                        "1 | Login as Clerk\n" +
                        "2 | Login as Manager\n" +
                        "5 | Exit the system\n");
    command = Integer.parseInt(reader.nextLine());
    while (command != EXIT) {
      switch (command) {
        case CLIENT_LOGIN:
          client();
          break;
        case CLERK_LOGIN:
          clerk();
          break;
        case MANAGER_LOGIN:
          manager();
          break;

        default:
          System.out.println("Invalid choice");  
      }
      System.out.println("LOGIN SCREEN");
      System.out.println("0 | Login as Client\n"+ 
                          "1 | Login as Clerk\n" +
                          "2 | Login as Manager\n" +
                          "5 | Exit the system\n");
      command = Integer.parseInt(reader.nextLine());
    }
    (WareContext.instance()).changeState(5);
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
