import java.util.*;

public class ClerkMenuState extends WareState {
  private static ClerkMenuState clerkmenustate;
  private Scanner reader = new Scanner(System.in);
  private static Warehouse warehouse;
  private static final int EXIT = 0;
  private static final int ADD_CLIENT = 1;
  private static final int LIST_PRODUCTS = 5;
  private static final int LIST_CLIENTS = 4;
  private static final int LIST_CLIENTS_OUTSTANDING = 8;
  private static final int ACCEPT_PAYMENT = 13;
  private static final int BECOME_CLIENT = 14;
  private static final int DISPLAY_PRODUCT_WAITLIST = 9;
  private static final int HELP = 16;
  
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

  public void help() {
    System.out.println("CLERK MENU");
    System.out.println(EXIT + "  | Exit");
    System.out.println(ADD_CLIENT + "  | Add a client");
    System.out.println(LIST_PRODUCTS + "  | List all products and information");
    System.out.println(LIST_CLIENTS + "  | List all clients and information");
    System.out.println(LIST_CLIENTS_OUTSTANDING + "  | List clients with an outstanding balance");
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
        case LIST_CLIENTS:
            displayClients();
            break;
        case LIST_CLIENTS_OUTSTANDING:
            displayOutstandingBalances();
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
    System.out.print("Enter client name: ");
    String name = reader.nextLine();
    System.out.print("Enter client address: ");
    String address = reader.nextLine();

    Client client = warehouse.addClient(name, address);

    if(client == null){
      System.out.println("Client information invalid.");
    }
    else{
      System.out.println("Client added.");
    }
  }

  public void displayProducts(){
    warehouse.displayProducts();
  }

  public void displayClients(){
    warehouse.displayClients();
  }

  public void displayOutstandingBalances(){
    warehouse.displayClientsWithBalance();
  }

  public void acceptPayment(){
    System.out.print("Enter client ID: ");
    String cid = reader.nextLine();

    System.out.print("Enter amount to charge client: ");
    float amount = Float.parseFloat(reader.nextLine());

    warehouse.acceptClientPayment(cid, amount);

  }

  public void becomeClient(){
    System.out.print("Enter client ID: ");
    String clientID = reader.nextLine();

    if(warehouse.searchClient(clientID)){
      WareContext.instance().setUser(clientID);
      WareContext.instance().changeState(0);
    }
    else{
      System.out.println("Invalid clientID.");
    }
  }

  public void displayProductWaitlist(){
    System.out.print("Enter product ID: ");
    String pid = reader.nextLine();

    warehouse.displayProductWaitlist(pid);
  }

  public void run() {
    process();
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
