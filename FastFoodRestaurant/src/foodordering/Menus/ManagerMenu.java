package foodordering.Menus;

import java.util.ArrayList;
import java.util.List;

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
            System.out.println("7. Restore default menu items");
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
                case 7:
                    restoreDefaults();
                    break;
                case 0:
                    System.out.println("Logged out.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // ---------- add ----------

    private static void addItem() {
        String name = Input.clean(Input.text("Food name: "));
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
        for (FoodItem f : FileStorage.menu) {
            if (f.getName().equalsIgnoreCase(name)) {
                System.out.println("That item already exists (code " + f.getCode() + ").");
                return;
            }
        }
        double price = Input.decimal("Price: ");
        if (price <= 0) {
            System.out.println("Price must be greater than 0.");
            return;
        }
        int kcal = Input.integer("Calories in kcal (0 if unknown): ");
        if (kcal < 0) {
            System.out.println("Calories cannot be negative.");
            return;
        }

        String category = chooseCategory();
        if (category == null) return;
        String group = chooseGroup(category);
        if (group == null) return;

        String code = nextCode(category, group);
        if (code == null) {
            System.out.println("That group is full (a to z). Choose another group.");
            return;
        }

        FoodItem item = new FoodItem(code, name, price, 0, kcal, category, group);
        FileStorage.addMenuItem(item);
        FileStorage.saveMenu();
        System.out.println("Added: " + name + " (code " + code + ", " + category
                + (group.isEmpty() ? "" : " / " + group) + ")");
    }

    private static List<String> categories() {
        List<String> list = new ArrayList<>();
        for (FoodItem f : FileStorage.menu) {
            if (!list.contains(f.getCategory())) list.add(f.getCategory());
        }
        return list;
    }

    private static List<String> groupsIn(String category) {
        List<String> list = new ArrayList<>();
        for (FoodItem f : FileStorage.menu) {
            if (f.getCategory().equals(category) && !f.getGroup().isEmpty() && !list.contains(f.getGroup())) {
                list.add(f.getGroup());
            }
        }
        return list;
    }

    /** Returns the chosen category name, or null if cancelled. */
    private static String chooseCategory() {
        List<String> cats = categories();
        System.out.println("\nCategories:");
        for (int i = 0; i < cats.size(); i++) {
            System.out.println((i + 1) + ". " + cats.get(i));
        }
        System.out.println((cats.size() + 1) + ". New category");
        int pick = Input.integer("Choose category: ");

        if (pick >= 1 && pick <= cats.size()) return cats.get(pick - 1);
        if (pick == cats.size() + 1) {
            String name = Input.clean(Input.text("New category name: "));
            if (name.isEmpty()) {
                System.out.println("Category name cannot be empty.");
                return null;
            }
            return name;
        }
        System.out.println("Invalid choice.");
        return null;
    }

    /** Returns the group name ("" for no group), or null if cancelled. */
    private static String chooseGroup(String category) {
        List<String> groups = groupsIn(category);
        System.out.println("\nGroups in this category:");
        System.out.println("0. No group (single item)");
        for (int i = 0; i < groups.size(); i++) {
            System.out.println((i + 1) + ". " + groups.get(i));
        }
        System.out.println((groups.size() + 1) + ". New group");
        int pick = Input.integer("Choose group: ");

        if (pick == 0) return "";
        if (pick >= 1 && pick <= groups.size()) return groups.get(pick - 1);
        if (pick == groups.size() + 1) {
            String name = Input.clean(Input.text("New group name: "));
            if (name.isEmpty()) {
                System.out.println("Group name cannot be empty.");
                return null;
            }
            return name;
        }
        System.out.println("Invalid choice.");
        return null;
    }

    private static int nextNumber() {
        int max = 0;
        for (FoodItem f : FileStorage.menu) {
            String n = f.numberPart();
            if (!n.isEmpty()) max = Math.max(max, Integer.parseInt(n));
        }
        return max + 1;
    }

    /** Single items get the next number (25); group items get the group number plus a letter (17f). */
    private static String nextCode(String category, String group) {
        if (group.isEmpty()) return String.format("%02d", nextNumber());

        String prefix = null;
        char lastLetter = (char) ('a' - 1);
        for (FoodItem f : FileStorage.menu) {
            if (f.getCategory().equals(category) && f.getGroup().equalsIgnoreCase(group)) {
                prefix = f.numberPart();
                char c = Character.toLowerCase(f.getCode().charAt(f.getCode().length() - 1));
                if (Character.isLetter(c) && c > lastLetter) lastLetter = c;
            }
        }
        if (prefix == null) prefix = String.format("%02d", nextNumber());   // brand-new group
        char next = (char) (lastLetter + 1);
        if (next > 'z') return null;
        return prefix + next;
    }

    // ---------- change / remove ----------

    private static FoodItem pickItem() {
        FoodItem.printTable(FileStorage.menu);
        if (FileStorage.menu.isEmpty()) return null;
        FoodItem item = FileStorage.findItem(Input.text("Enter menu code (e.g. 05 or 17a): "));
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
        String confirm = Input.text("Remove " + item.getName() + " (" + item.getCode() + ")? (y/n): ");
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("Cancelled.");
            return;
        }
        FileStorage.menu.remove(item);
        FileStorage.saveMenu();
        System.out.println("Removed " + item.getName() + ".");
    }

    /** Puts back any built-in menu items that were deleted (does not touch other items). */
    private static void restoreDefaults() {
        int added = DefaultMenu.loadMissing();
        if (added == 0) {
            System.out.println("Nothing to restore. All built-in menu items are already on the menu.");
        } else {
            System.out.println("Restored " + added + " built-in menu item(s).");
        }
    }
}
