package Server.Model;

public class Product {
    private int id;
    private String name;
    private String amount;
    private double price;
    private String picture;

    public Product(int id, String name, String amount, double price, String picture) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.picture = picture;
    }

    public Product(String name, String amount, double price, String picture) {
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.picture = picture;
    }

    public Product(Product product) {
        this.id = product.id;
        this.name = product.name;
        this.amount = product.amount;
        this.price = product.price;
        this.picture = product.picture;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", amount='" + amount + '\'' +
                ", price=" + price +
                ", picture='" + picture + '\'' +
                '}';
    }

    public Product() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
