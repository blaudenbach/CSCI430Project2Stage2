import java.util.*;
import java.io.*;
public class ClientList implements Serializable {
  private static final long serialVersionUID = 1L;
  private LinkedList<Client> clients;	
  private static ClientList clientList;
  private ClientList() {
	  clients = new LinkedList<Client>();
  }
  public static ClientList instance() {
    if (clientList == null) {
      return (clientList = new ClientList());
    } else {
      return clientList;
    }
  }

  public boolean addClient(Client client) {
    clients.add(client);
    return true;
  }

  public Iterator<?> getClients(){
     return clients.iterator();
  }

  private void writeObject(java.io.ObjectOutputStream output) {
    try {
      output.defaultWriteObject();
      output.writeObject(clientList);
    } catch(IOException ioe) {
      ioe.printStackTrace();
    }
  }
  private void readObject(java.io.ObjectInputStream input) {
    try {
      if (clientList != null) {
        return;
      } else {
        input.defaultReadObject();
        if (clientList == null) {
          clientList = (ClientList) input.readObject();
        } else {
          input.readObject();
        }
      }
    } catch(IOException ioe) {
      ioe.printStackTrace();
    } catch(ClassNotFoundException cnfe) {
      cnfe.printStackTrace();
    }
  }

  public void displayList(){
	  for(Iterator<?> current = clients.iterator(); current.hasNext();){
		  Client C = (Client) current.next();
		  System.out.println(C.toString());
	  }
  }

  public String toString() {
    return clients.toString();
  }

  public Client findClient(String cid){
    Iterator<?> current = clients.iterator();
    while(current.hasNext()){
      Client client = (Client)current.next();
      if(client.getId().equals(cid)){
        return client;
      }
    }
  return null;
  }

}