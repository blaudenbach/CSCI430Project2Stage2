import java.util.*;
//import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//import java.text.*;

public class ClientQueryState extends WareState implements ActionListener{
    private static ClientQueryState clientquerystate;
    private Scanner reader = new Scanner(System.in);
    private static Warehouse warehouse;
    private static final int EXIT = 0;
    private static final int LIST_CLIENTS = 4;
    private static final int LIST_CLIENTS_OUTSTANDING = 8;
    private static final int LIST_INACTIVE_CLIENTS = 21;
    private static final int HELP = 22;


    private JFrame frame;
    private AbstractButton listClientsButton, listOustandingButton, listInactiveButton, exitButton;

    private ClientQueryState() {
        warehouse = Warehouse.instance();
    }
  
    public static ClientQueryState instance() {
        if (clientquerystate == null) {
        return clientquerystate = new ClientQueryState();
        } else {
        return clientquerystate;
        }
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if(e.getSource().equals(this.listClientsButton)){
            this.listClients();
        }
        else if(e.getSource().equals(this.listOustandingButton)){
            this.listOutstandingClients();
        }
        else if(e.getSource().equals(this.listInactiveButton)){
            this.listInactiveClients();
        }
        else if(e.getSource().equals(this.exitButton)){
            this.logout();
        }
    }
  
    public void help() {
        System.out.println("MODIFY CART MENU");
        System.out.println(EXIT + "  | Exit");
        System.out.println(LIST_CLIENTS + "  | Display the list of clients");
        System.out.println(LIST_CLIENTS_OUTSTANDING + " | Display the list of clients with an outstanding balance");
        System.out.println(LIST_INACTIVE_CLIENTS + " | Display the list of clients with no transactions in the last six months");
        System.out.println(HELP + " | for help");
    }
  
    public void process() {
        int command;
        help();
        command = Integer.parseInt(reader.nextLine());
        while (command != EXIT) {
            switch (command) {
                case LIST_CLIENTS:
                    listClients();
                    break;
                case LIST_CLIENTS_OUTSTANDING:
                    listOutstandingClients();
                    break;
                case LIST_INACTIVE_CLIENTS:
                    listInactiveClients();
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

    public void listClients(){
        warehouse.displayClients(frame);
    }

    public void listOutstandingClients(){
        warehouse.displayClientsWithBalance(frame);
    }

    public void listInactiveClients(){
        warehouse.displayInactiveClients(frame);
    }
  
    public void run(){
        frame = WareContext.instance().getFrame();
        frame.getContentPane().removeAll();
        frame.getContentPane().setLayout(new FlowLayout());
        listClientsButton = new JButton("List Clients");
        listClientsButton.addActionListener(this);
        listOustandingButton = new JButton("List Outstanding Balances");
        listOustandingButton.addActionListener(this);
        listInactiveButton = new JButton("List Inactive Clients");
        listInactiveButton.addActionListener(this);
        exitButton = new JButton("Exit");
        exitButton.addActionListener(this);
    
        frame.getContentPane().add(this.listClientsButton);
        frame.getContentPane().add(this.listOustandingButton);
        frame.getContentPane().add(this.listInactiveButton);
        frame.getContentPane().add(this.exitButton);
        frame.setVisible(true);
        frame.paint(frame.getGraphics());
        frame.toFront();
        frame.requestFocus();


        //process();
    }
      
    public void logout(){
        WareContext.instance().changeState(4);
    }
}
