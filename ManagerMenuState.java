import java.util.*;

public class ManagerMenuState extends WareState {

  private static ManagerMenuState managermenustate;
  private Scanner reader = new Scanner(System.in);
  private static Warehouse warehouse;
  private static final int EXIT = 0;
  private static final int ADD_PRODUCT = 2;
  private static final int RECEIVE_SHIPMENT = 10;
  private static final int BECOME_CLERK = 15;
  private static final int HELP = 22;
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
    System.out.println("Enter product name:");
    String name = reader.nextLine();
    System.out.println("Enter product price:");
    double price = Double.parseDouble(reader.nextLine());

    System.out.println("Enter amount of product in stock:");
    int inStock = Integer.parseInt(reader.nextLine());

    Product product = warehouse.addProduct(name, price, inStock);

    if(product == null){
        System.out.println("Product information invalid.");
    }
    else{
        System.out.println("Product added.");
    }
  }

  public void receiveShipment(){
    System.out.println("Enter ID of product in shipment:");
    String pid = reader.nextLine();

    System.out.println("Enter quantity of product in shipment:");
    int qty = Integer.parseInt(reader.nextLine());

    warehouse.processShipment(pid, qty, reader);
  }

  public void becomeClerk(){
    WareContext.instance().changeState(1); //[2][1] = 1
  }

  public void run() {
    process();
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

