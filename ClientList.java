import java.util.*;
import java.io.*;
import javax.swing.*;

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

  public void displayList(JFrame frame){
    String lineString = "";
	  for(Iterator<?> current = clients.iterator(); current.hasNext();){
		  Client C = (Client) current.next();
		  
      lineString += C.toString() + "\n";
	  }

    JOptionPane.showMessageDialog(frame, lineString);
  }

  public String toString() {
    return clients.toString();
  }

  public void displayInactiveClients(JFrame frame){
    String lineString = "";
    for(Iterator<?> current = clients.iterator(); current.hasNext();){
      Client C = (Client) current.next();

      if(!C.isActive()){
        lineString += C.toString() + "\n";
      }
    }
    JOptionPane.showMessageDialog(frame, "Inactive Clients:\n" + lineString);
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