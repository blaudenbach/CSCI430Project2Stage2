public class Shipment {
    private int quantity;
    private Product product;

    Shipment(int quantity, Product product) {
        this.quantity = quantity;
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public String toString() {
        String string = "Product: " + product.getName() + ", Quantity: " + quantity;
        return string;
    }

    public Product getProduct() {
        return product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void removeQuantity(int qty){
        this.quantity -= qty;
    }
}
