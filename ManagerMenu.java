package foodordering;

import java.util.LinkedHashMap;
import java.util.Map;

/** MANAGER: food items, pricing, discounts, and financial reports. */
public class ManagerMenu {

    public static void show(User manager) {
        while (true) {
            System.out.println("\n--- MANAGER MENU ---");
            System.out.println("1. View menu");
            System.out.println("2. Add food item");
            System.out.println("3. Set price");
            System.out.println("4. Set discount");
            System.out.println("5. Remove food item");
            System.out.println("6. View financial report");
            System.out.println("0. Logout");
            int choice = Input.integer("Choose: ");

            switch (choice) {
                case 1:
                    FoodItem.printTable(FileStorage.menu);
                    break;
                case 2:
                    addItem();
                    break;
                case 3:
                    setPrice();
                    break;
                case 4:
                    setDiscount();
                    break;
                case 5:
                    removeItem();
                    break;
                case 6:
                    financialReport();
                    break;
                case 0:
                    System.out.println("Logged out.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void addItem() {
        String name = Input.clean(Input.text("Food name: "));
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
        for (FoodItem f : FileStorage.menu) {
            if (f.getName().equalsIgnoreCase(name)) {
                System.out.println("That item already exists.");
                return;
            }
        }
        double price = Input.decimal("Price: ");
        if (price <= 0) {
            System.out.println("Price must be greater than 0.");
            return;
        }
        FoodItem item = new FoodItem(FileStorage.nextItemId(), name, price, 0);
        FileStorage.menu.add(item);
        FileStorage.saveMenu();
        System.out.println("Added: " + name + " (ID " + item.getId() + ")");
    }

    private static FoodItem pickItem() {
        FoodItem.printTable(FileStorage.menu);
        if (FileStorage.menu.isEmpty()) return null;
        FoodItem item = FileStorage.findItem(Input.integer("Enter item ID: "));
        if (item == null) System.out.println("Item not found.");
        return item;
    }

    private static void setPrice() {
        FoodItem item = pickItem();
        if (item == null) return;
        double price = Input.decimal("New price for " + item.getName() + ": ");
        if (price <= 0) {
            System.out.println("Price must be greater than 0.");
            return;
        }
        item.setPrice(price);
        FileStorage.saveMenu();
        System.out.println("Price updated.");
    }

    private static void setDiscount() {
        FoodItem item = pickItem();
        if (item == null) return;
        double discount = Input.decimal("Discount % for " + item.getName() + " (0 to remove): ");
        if (discount < 0 || discount > 100) {
            System.out.println("Discount must be between 0 and 100.");
            return;
        }
        item.setDiscount(discount);
        FileStorage.saveMenu();
        System.out.printf("Discount set. New final price: %.2f%n", item.getFinalPrice());
    }

    private static void removeItem() {
        FoodItem item = pickItem();
        if (item == null) return;
        FileStorage.menu.remove(item);
        FileStorage.saveMenu();
        System.out.println("Removed " + item.getName() + ".");
    }

    private static void financialReport() {
        int served = 0, pending = 0;
        double revenue = 0, pendingValue = 0;
        Map<String, double[]> perItem = new LinkedHashMap<>(); // name -> {qty, revenue}

        for (Order o : FileStorage.orders) {
            if (o.getStatus() == Order.Status.READY_TO_SERVE) {
                served++;
                revenue += o.getTotal();
                for (Order.Line l : o.getLines()) {
                    double[] totals = perItem.get(l.name);
                    if (totals == null) {
                        totals = new double[2];
                        perItem.put(l.name, totals);
                    }
                    totals[0] += l.qty;
                    totals[1] += l.subtotal();
                }
            } else {
                pending++;
                pendingValue += o.getTotal();
            }
        }

        System.out.println("\n===== FINANCIAL REPORT =====");
        System.out.println("Total orders        : " + FileStorage.orders.size());
        System.out.println("Completed orders    : " + served);
        System.out.println("Pending orders      : " + pending);
        System.out.printf("Revenue (completed) : %.2f%n", revenue);
        System.out.printf("Pending value       : %.2f%n", pendingValue);

        if (!perItem.isEmpty()) {
            System.out.println("\nSales per item (completed orders):");
            System.out.printf("%-22s %8s %12s%n", "Item", "Qty", "Revenue");
            System.out.println(Input.line(44));
            for (Map.Entry<String, double[]> e : perItem.entrySet()) {
                System.out.printf("%-22s %8d %12.2f%n", e.getKey(), (int) e.getValue()[0], e.getValue()[1]);
            }
        }
    }
}
