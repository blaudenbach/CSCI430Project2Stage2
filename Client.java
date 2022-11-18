import java.io.*;

public class Client implements Serializable {
  private static final long serialVersionUID = 1L;
  private String name;
  private String address;
  private String id;
  private static final String CLIENT_STRING = "C";
  private WishList wishlist;
  private float amountDue;
  private InvoiceList invoices;

  public  Client (String name, String address) {
    this.name = name;
    this.address = address;
    id = CLIENT_STRING + (ClientIdServer.instance().getId());
    wishlist = new WishList();
    this.amountDue = 0;
    invoices = new InvoiceList();
  }

  public String getName() {
    return name;
  }

  public String getAddress() {
    return address;
  }
  public String getId() {
    return id;
  }
  public WishList getWishList(){
    return wishlist;
  }

  public float getAmountDue(){
    return amountDue;
  }

  public void setName(String newName) {
    name = newName;
  }
  public void setAddress(String newAddress) {
    address = newAddress;
  }

  public void charge(float amount){
    amountDue += amount;
  }

  public boolean equals(String id) {
    return this.id.equals(id);
  }
  public String toString() {
    return "Id: " + id + ", Name: " + name + ", Address: " + address;
  }

  public InvoiceList getInvoiceList(){
    return invoices;
  }

  public void pay(float amount){
    amountDue -= amount;
  }

  public void addInvoice(Invoice invoice){
    invoices.addInvoice(invoice);
  }

  public boolean isActive(){
    if(invoices.hasRecent()){
      return true;
    }
    else{
      return false;
    }
  }

}