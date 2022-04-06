public class Product {
    String name;
    String brand;
    String sku;
    String kaspiSku;
    private int innerPrise;
    int lowestPrice;
    int sellingPrice;
    int actualPrice;

    public Product(String name, int lowestPrice) {
        this.name = name;
        this.lowestPrice = lowestPrice;
    }

    @Override
    public String toString() {
        return "Products{" +
                "name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", sku='" + sku + '\'' +
                ", kaspiSku='" + kaspiSku + '\'' +
                ", lowestPrice=" + lowestPrice +
                ", sellingPrice=" + sellingPrice +
                ", actualPrice=" + actualPrice +
                '}';
    }
}
