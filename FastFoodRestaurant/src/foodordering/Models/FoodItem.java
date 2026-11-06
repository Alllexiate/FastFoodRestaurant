package foodordering.Models;

import java.util.List;

import foodordering.Utilities.Input;

/** One menu item, e.g. code "01" or "17a" (a burger inside the "block Burgers" group). */
public class FoodItem {
    private final String code;      // what the customer types, e.g. "01" or "17a"
    private String name;
    private double price;           // base price
    private double discount;        // percent, 0 - 100
    private int kcal;
    private final String category;  // section heading, e.g. "keyword Entrees"
    private final String group;     // sub-group heading, e.g. "block Burgers" ("" if none)

    public FoodItem(String code, String name, double price, double discount,
                    int kcal, String category, String group) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.kcal = kcal;
        this.category = category;
        this.group = group;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public double getDiscount() { return discount; }
    public int getKcal() { return kcal; }
    public String getCategory() { return category; }
    public String getGroup() { return group; }
    public void setPrice(double price) { this.price = price; }
    public void setDiscount(double discount) { this.discount = discount; }

    /** The digits at the start of the code: "17a" -> "17". */
    public String numberPart() {
        return code.replaceAll("[^0-9].*$", "");
    }

    public double getFinalPrice() {
        double result = price - (price * discount / 100.0);
        return Math.round(result * 100.0) / 100.0;
    }

    // Stored in menu.txt as: code|name|price|discount|kcal|category|group
    public String toLine() {
        return code + "|" + name + "|" + price + "|" + discount + "|" + kcal + "|" + category + "|" + group;
    }

    public static FoodItem fromLine(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length >= 7) {
            return new FoodItem(p[0], p[1], Double.parseDouble(p[2]), Double.parseDouble(p[3]),
                    Integer.parseInt(p[4]), p[5], p[6]);
        }
        // older 4-field format: id|name|price|discount
        return new FoodItem(String.format("%02d", Integer.parseInt(p[0])), p[1],
                Double.parseDouble(p[2]), Double.parseDouble(p[3]), 0, "Other Items", "");
    }

    /** Prints the menu grouped by category and sub-group. */
    public static void printTable(List<FoodItem> items) {
        if (items.isEmpty()) {
            System.out.println("No food items yet.");
            return;
        }
        String line = Input.line(87);
        System.out.println(line);
        System.out.printf("%-6s %-46s %6s %9s %6s %9s%n", "Code", "Item", "kcal", "Price", "Disc", "Final");
        System.out.println(line);

        String lastCategory = null;
        String lastGroup = "";
        for (FoodItem f : items) {
            if (!f.category.equals(lastCategory)) {
                System.out.println("\n--- " + f.category + " ---");
                lastCategory = f.category;
                lastGroup = "";
            }
            if (!f.group.equals(lastGroup)) {
                if (!f.group.isEmpty()) {
                    System.out.printf("%-6s %s:%n", f.numberPart(), f.group);
                }
                lastGroup = f.group;
            }
            String shownName = f.group.isEmpty() ? f.name : "  " + f.name;
            System.out.printf("%-6s %-46s %6d %9.2f %5.1f%% %9.2f%n",
                    f.code, shownName, f.kcal, f.price, f.discount, f.getFinalPrice());
        }
        System.out.println(line);
    }
}
