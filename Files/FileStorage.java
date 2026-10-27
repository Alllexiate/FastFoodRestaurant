package foodordering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * The "database": everything is kept in memory and saved to .txt files.
 * When run from NetBeans, the .txt files are created in the project's root folder.
 */
public class FileStorage {
    private static final String USERS_FILE = "users.txt";
    private static final String MENU_FILE = "menu.txt";
    private static final String ORDERS_FILE = "orders.txt";

    public static List<User> users = new ArrayList<>();
    public static List<FoodItem> menu = new ArrayList<>();
    public static List<Order> orders = new ArrayList<>();

    public static void loadAll() {
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

    public static FoodItem findItem(int id) {
        for (FoodItem f : menu) if (f.getId() == id) return f;
        return null;
    }

    public static Order findOrder(int id) {
        for (Order o : orders) if (o.getId() == id) return o;
        return null;
    }

    public static int nextItemId() {
        int max = 0;
        for (FoodItem f : menu) max = Math.max(max, f.getId());
        return max + 1;
    }

    public static int nextOrderId() {
        int max = 0;
        for (Order o : orders) max = Math.max(max, o.getId());
        return max + 1;
    }
}
