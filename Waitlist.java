import java.util.*;
import java.io.*;

public class Waitlist implements Serializable{
    private LinkedList<Request> requests = new LinkedList<Request>();
    
    public Waitlist(){
    }

    public boolean addRequest(Request request){
        Request result = findRequest(request.getClient());
        if (result == null) {
          requests.add(request);
          return true;
        } else {
          result.setQuantity(request.getQuantity());
          return true;
        }
    }

    public String toString() {
        return requests.toString();
    }
    
    public Iterator<?> getWaitlist(){
        return requests.iterator();
    }
    
    public void displayList(){
        System.out.println(toString());
    }

    public Request findRequest(Client client){
        for(Iterator<?> current = requests.iterator(); current.hasNext();){
            Request request = (Request) current.next();
            if(request.getClient().equals(client)){
            return request;
            }
        }
        return null;
    }
}