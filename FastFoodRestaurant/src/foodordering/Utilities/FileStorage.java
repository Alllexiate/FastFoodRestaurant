package foodordering.Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import foodordering.Models.*;

/** The "database": everything is kept in memory and saved to .txt files in Files/. */
public class FileStorage {
    public static final String DATA_DIR = "Files";   // all .txt data is saved here
    private static final String USERS_FILE = DATA_DIR + "/users.txt";
    private static final String MENU_FILE = DATA_DIR + "/menu.txt";
    private static final String ORDERS_FILE = DATA_DIR + "/orders.txt";

    public static List<User> users = new ArrayList<>();
    public static List<FoodItem> menu = new ArrayList<>();
    public static List<Order> orders = new ArrayList<>();

    // Marker file: exists once the built-in menu has been loaded, so deleted items stay deleted.
    private static final String DEFAULTS_FLAG = DATA_DIR + "/defaults_loaded.txt";

    public static void loadAll() {
        new File(DATA_DIR).mkdirs();

        users = load(USERS_FILE, User::fromLine);
        menu = load(MENU_FILE, FoodItem::fromLine);
        orders = load(ORDERS_FILE, Order::fromLine);

        if (users.isEmpty()) {   // first run: create default accounts
            users.add(new User("admin", "admin123", User.Role.ADMIN));
            users.add(new User("manager", "manager123", User.Role.MANAGER));
            users.add(new User("employee", "employee123", User.Role.EMPLOYEE));
            users.add(new User("customer", "customer123", User.Role.CUSTOMER));
            saveUsers();
        }
    }

    private static <T> List<T> load(String file, Function<String, T> parser) {
        List<T> list = new ArrayList<>();
        File f = new File(file);
        if (!f.exists()) return list;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) list.add(parser.apply(line));
            }
        } catch (Exception e) {
            System.out.println("Could not read " + file + ": " + e.getMessage());
        }
        return list;
    }

    private static void save(String file, List<String> lines) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            for (String l : lines) pw.println(l);
        } catch (IOException e) {
            System.out.println("Could not save " + file + ": " + e.getMessage());
        }
    }

    public static boolean isDefaultMenuLoaded() {
        return new File(DEFAULTS_FLAG).exists();
    }

    public static void markDefaultMenuLoaded() {
        List<String> note = new ArrayList<>();
        note.add("The built-in menu has been loaded. Delete this file to load it again.");
        save(DEFAULTS_FLAG, note);
    }

    public static void saveUsers() {
        List<String> lines = new ArrayList<>();
        for (User u : users) lines.add(u.toLine());
        save(USERS_FILE, lines);
    }

    public static void saveMenu() {
        List<String> lines = new ArrayList<>();
        for (FoodItem f : menu) lines.add(f.toLine());
        save(MENU_FILE, lines);
    }

    public static void saveOrders() {
        List<String> lines = new ArrayList<>();
        for (Order o : orders) lines.add(o.toLine());
        save(ORDERS_FILE, lines);
    }

    public static User findUser(String username) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) return u;
        }
        return null;
    }

    /** Finds an item by its menu code. Not case-sensitive; "5" also finds "05". */
    public static FoodItem findItem(String code) {
        String c = code.trim();
        if (c.isEmpty()) return null;
        for (FoodItem f : menu) {
            if (f.getCode().equalsIgnoreCase(c)) return f;
        }
        if (c.matches("\\d")) {
            String padded = "0" + c;
            for (FoodItem f : menu) {
                if (f.getCode().equalsIgnoreCase(padded)) return f;
            }
        }
        return null;
    }

    /** True if an item with exactly this code already exists. */
    public static boolean hasCode(String code) {
        for (FoodItem f : menu) {
            if (f.getCode().equalsIgnoreCase(code)) return true;
        }
        return false;
    }

    /**
     * Adds an item to the menu list, placing it next to the items of the same group
     * (or the same category) so the menu stays grouped. Does not save.
     */
    public static void addMenuItem(FoodItem item) {
        int index = -1;
        if (!item.getGroup().isEmpty()) {
            for (int i = 0; i < menu.size(); i++) {
                FoodItem f = menu.get(i);
                if (f.getCategory().equals(item.getCategory()) && f.getGroup().equals(item.getGroup())) index = i;
            }
        }
        if (index < 0) {
            for (int i = 0; i < menu.size(); i++) {
                if (menu.get(i).getCategory().equals(item.getCategory())) index = i;
            }
        }
        if (index >= 0) menu.add(index + 1, item);
        else menu.add(item);
    }

    public static Order findOrder(int id) {
        for (Order o : orders) if (o.getId() == id) return o;
        return null;
    }

    public static int nextOrderId() {
        int max = 0;
        for (Order o : orders) max = Math.max(max, o.getId());
        return max + 1;
    }
}
