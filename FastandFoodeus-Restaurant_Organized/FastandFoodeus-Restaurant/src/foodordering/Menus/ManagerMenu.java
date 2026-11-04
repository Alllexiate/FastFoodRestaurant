package foodordering.Menus;

import foodordering.Models.*;
import foodordering.Services.*;
import foodordering.Utilities.*;

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
                    SetDiscount.setDiscount();
                    break;
                case 5:
                    removeItem();
                    break;
                case 6:
                    FinancialReport.showReport();
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

    private static void removeItem() {
        FoodItem item = pickItem();
        if (item == null) return;
        FileStorage.menu.remove(item);
        FileStorage.saveMenu();
        System.out.println("Removed " + item.getName() + ".");
    }
}
