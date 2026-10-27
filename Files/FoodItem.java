package foodordering;

import java.util.List;

public class FoodItem {
    private final int id;
    private String name;
    private double price;     // base price
    private double discount;  // percent, 0 - 100

    public FoodItem(int id, String name, double price, double discount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.discount = discount;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public double getDiscount() { return discount; }
    public void setPrice(double price) { this.price = price; }
    public void setDiscount(double discount) { this.discount = discount; }

    public double getFinalPrice() {
        double result = price - (price * discount / 100.0);
        return Math.round(result * 100.0) / 100.0;
    }

    // Stored in menu.txt as: id|name|price|discount
    public String toLine() {
        return id + "|" + name + "|" + price + "|" + discount;
    }

    public static FoodItem fromLine(String line) {
        String[] p = line.split("\\|", -1);
        return new FoodItem(Integer.parseInt(p[0]), p[1],
                Double.parseDouble(p[2]), Double.parseDouble(p[3]));
    }

    public static void printTable(List<FoodItem> items) {
        if (items.isEmpty()) {
            System.out.println("No food items yet.");
            return;
        }
        System.out.printf("%-4s %-22s %10s %10s %12s%n", "ID", "Name", "Price", "Discount", "Final Price");
        System.out.println(Input.line(62));
        for (FoodItem f : items) {
            System.out.printf("%-4d %-22s %10.2f %9.1f%% %12.2f%n",
                    f.id, f.name, f.price, f.discount, f.getFinalPrice());
        }
    }
}
