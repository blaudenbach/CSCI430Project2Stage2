public class Request {
    private int quantity;
    private Client client;

    public Request(int quantity, Client client){
        this.quantity = quantity;
        this.client = client;
    }

    public int getQuantity() {
        return quantity;
    }

    public String toString() {
        String string = "Client: " + client.toString() + ", Quantity: " + quantity;
        return string;
    }

    public Client getClient() {
        return client;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
