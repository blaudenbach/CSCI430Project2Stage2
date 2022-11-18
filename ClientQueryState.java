import java.util.*;

public class ClientQueryState extends WareState{
    private static ClientQueryState clientquerystate;
    private Scanner reader = new Scanner(System.in);
    private static Warehouse warehouse;
    private static final int EXIT = 0;
    private static final int LIST_CLIENTS = 4;
    private static final int LIST_CLIENTS_OUTSTANDING = 8;
    private static final int LIST_INACTIVE_CLIENTS = 21;
    private static final int HELP = 22;
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
        warehouse.displayClients();
    }

    public void listOutstandingClients(){
        warehouse.displayClientsWithBalance();
    }

    public void listInactiveClients(){
        warehouse.displayInactiveClients();
    }
  
    public void run(){
        process();
    }
      
    public void logout(){
        WareContext.instance().changeState(1);
    }
}
