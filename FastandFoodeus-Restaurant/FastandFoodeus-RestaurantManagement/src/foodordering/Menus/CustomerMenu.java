package foodordering.Menus;

import java.util.LinkedHashMap;
import java.util.Map;

import foodordering.Models.*;
import foodordering.Utilities.*;

public class CustomerMenu {

    public static void show(User customer) {
        System.out.println("\nWelcome, " + customer.getUsername() + "!");
        while (true) {
            System.out.println("\n--- CUSTOMER MENU ---");
            System.out.println("1. View menu");
            System.out.println("2. Place order");
            System.out.println("3. View my orders");
            System.out.println("0. Logout");
            int choice = Input.integer("Choose: ");

            switch (choice) {
                case 1:
                    FoodItem.printTable(FileStorage.menu);
                    break;
                case 2:
                    placeOrder(customer);
                    break;
                case 3:
                    myOrders(customer);
                    break;
                case 0:
                    System.out.println("Logged out.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void placeOrder(User customer) {
        if (FileStorage.menu.isEmpty()) {
            System.out.println("The menu is empty right now. Please check back later.");
            return;
        }

        FoodItem.printTable(FileStorage.menu);
        Map<FoodItem, Integer> cart = new LinkedHashMap<>();

        // ---- choose items ----
        while (true) {
            System.out.println("\nType the Menu Code of an item (e.g. 01, 17a)");
            System.out.println("[1] DONE WITH ORDER   [2] CANCEL   [m] SHOW MENU");
            String entry = Input.text("Menu code: ");

            if (entry.equals("1")) {
                if (cart.isEmpty()) {
                    System.out.println("You haven't placed orders yet. Select from the menu.");
                    continue;
                }
                break;
            }
            if (entry.equals("2")) {
                System.out.println("Order cancelled. Thank you and come again.");
                return;
            }
            if (entry.equalsIgnoreCase("m")) {
                FoodItem.printTable(FileStorage.menu);
                continue;
            }

            FoodItem item = FileStorage.findItem(entry);
            if (item == null) {
                System.out.println("Invalid menu code. Try again.");
                continue;
            }
            askQuantity(item, cart);
        }

        // ---- build the order ----
        Order order = new Order(FileStorage.nextOrderId(), customer.getUsername());
        int totalKcal = 0;
        for (Map.Entry<FoodItem, Integer> e : cart.entrySet()) {
            FoodItem f = e.getKey();
            int qty = e.getValue();
            order.addLine(new Order.Line(f.getName(), qty, f.getFinalPrice(), f.getPrice()));
            totalKcal += f.getKcal() * qty;
        }

        System.out.println("\n==============================");
        System.out.println("ORDER SUMMARY");
        System.out.println("==============================");
        order.display();
        double savings = order.getListTotal() - order.getTotal();
        if (savings > 0.004) System.out.printf("   Discounts applied: -PHP %.2f%n", savings);
        System.out.println("   Total Calories: " + totalKcal + " kcal");

        // ---- payment (3 attempts) ----
        double total = order.getTotal();
        boolean paid = false;
        for (int attempt = 1; attempt <= 3; attempt++) {
            double cash = Input.decimal("Cash Amount: PHP ");
            if (cash >= total) {
                order.setCashTendered(cash);
                System.out.printf("Change: PHP %.2f%n", cash - total);
                paid = true;
                break;
            } else if (attempt < 3) {
                System.out.println("Insufficient amount. " + (3 - attempt) + " of 3 attempts left.");
            } else {
                System.out.println("Insufficient amount. Reached maximum number of attempts.");
            }
        }
        if (!paid) {
            System.out.println("Payment failed. Order cancelled.\nThank you and come again.");
            return;
        }

        FileStorage.orders.add(order);
        FileStorage.saveOrders();
        System.out.println("\nOrder #" + order.getId() + " placed! Status: PENDING");
        System.out.println("Thank you and come again!");
        Receipt.print(order);
    }

    /** Add, increase or remove one item in the cart. */
    private static void askQuantity(FoodItem item, Map<FoodItem, Integer> cart) {
        int inCart = cart.containsKey(item) ? cart.get(item) : 0;
        System.out.printf("%s - PHP %.2f   (in your order: %d)%n", item.getName(), item.getFinalPrice(), inCart);

        while (true) {
            String input = Input.text("Quantity ([d] to remove item): ");
            int qty;
            if (input.equalsIgnoreCase("d")) {
                qty = 0;
            } else {
                try {
                    qty = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Enter a valid quantity number, or [d] to remove.");
                    continue;
                }
            }

            if (qty < 0) {
                System.out.println("Quantity cannot be negative.");
                continue;
            }
            if (qty == 0) {
                if (inCart == 0) {
                    System.out.println("Item was not in your order.");
                } else {
                    cart.remove(item);
                    System.out.println("REMOVED " + item.getName() + " from your order.");
                }
                return;
            }
            cart.put(item, inCart + qty);
            System.out.printf("ADDED %d x %s = PHP %.2f (%d kcal)%n",
                    qty, item.getName(), qty * item.getFinalPrice(), qty * item.getKcal());
            return;
        }
    }

    private static void myOrders(User customer) {
        boolean found = false;
        for (Order o : FileStorage.orders) {
            if (o.getCustomer().equalsIgnoreCase(customer.getUsername())) {
                o.display();
                System.out.println();
                found = true;
            }
        }
        if (!found) System.out.println("You have no orders yet.");
    }
}
