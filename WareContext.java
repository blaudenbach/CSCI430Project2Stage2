import java.util.*;

public class WareContext {
    private int currentState;
    private static Warehouse warehouse;
    private static WareContext context;
    private int currentUser;
    private String clientID;
    private Scanner reader = new Scanner(System.in);
    public static final int IsClient = 0;
    public static final int IsClerk = 1;
    public static final int IsManager = 2;

    private WareState[] states;
    private int[][] nextState;

    private void retrieve() {
        try {
            Warehouse tempWarehouse = Warehouse.retrieve();
            if (tempWarehouse != null) {
                System.out.println(" The warehouse has been successfully retrieved from the file WarehouseData \n" );
                warehouse = tempWarehouse;
            } else {
                System.out.println("File doesnt exist; creating new WarehouseData" );
                warehouse = Warehouse.instance();
            }
        } catch(Exception cnfe) {
            cnfe.printStackTrace();
        }
    }

    public void setLogin(int code)
        {currentUser = code;}

    public void setUser(String cid)
        { clientID = cid;}

    public int getLogin()
        { return currentUser;}

    public String getClient()
        { return clientID;}

    private WareContext() { //constructor
        System.out.print("Search for saved data to use? (Y/N):");
        String choice = reader.nextLine();
        if(choice.equals("Y") || choice.equals("y")) {
            retrieve();
        } else {
            warehouse = Warehouse.instance();
        }
        // set up the FSM and transition table;
        //0 is login as client, 1 is login as clerk, 2 is login as manager, 3 is exit
        states = new WareState[4];
        states[0] = ClientMenuState.instance();
        states[1] = ClerkMenuState.instance();
        states[2] = ManagerMenuState.instance();
        states[3]=  LoginState.instance();
        nextState = new int[4][4];
        nextState[0][0] = 3;nextState[0][1] = 1;nextState[0][2] = -2;nextState[0][3] = -2;  //[0][3] doesnt exist
        nextState[1][0] = 0;nextState[1][1] = 3;nextState[1][2] = 2;nextState[1][3] = -2;  //[1][3] doesnt exist
        nextState[2][0] = -2;nextState[2][1] = 1;nextState[2][2] = 3;nextState[2][3] = -2;  //[2][3] doesnt exist
        nextState[3][0] = 0;nextState[3][1] = 1;nextState[3][2] = 2;nextState[3][3] = -1;
        currentState = 3;
    }

    public void changeState(int transition){
        //System.out.println("current state " + currentState + " \n \n ");
        currentState = nextState[currentState][transition];
        if (currentState == -2) 
            {System.out.println("Error has occurred"); terminate();}
        if (currentState == -1) 
            terminate();
        //System.out.println("current state " + currentState + " \n \n ");
        states[currentState].run();
    }

    private void terminate(){
        System.out.println("Save data? (Y/N):");
        String choice = reader.nextLine();
        if(choice.equals("Y") || choice.equals("y")) {
            if (warehouse.save()) {
                System.out.println("The warehouse has been successfully saved in the file WarehouseData \n" );
            } else {
                System.out.println("There has been an error in saving \n" );
            }
        }
        System.out.println("Goodbye \n "); System.exit(0);
    }

    public static WareContext instance() {
        if (context == null) {
            context = new WareContext();
        }
        return context;
    }

    public void process(){
        states[currentState].run();
    }

    public static void main (String[] args){
        WareContext.instance().process(); 
    }
}
