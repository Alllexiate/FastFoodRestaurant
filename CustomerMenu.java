package foodordering;

/** CUSTOMER: views items and places orders. */
public class CustomerMenu {

    public static void show(User customer) {
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

        Order order = new Order(FileStorage.nextOrderId(), customer.getUsername());
        FoodItem.printTable(FileStorage.menu);

        while (true) {
            int id = Input.integer("Item ID to add (0 to finish): ");
            if (id == 0) break;

            FoodItem item = FileStorage.findItem(id);
            if (item == null) {
                System.out.println("Item not found.");
                continue;
            }
            int qty = Input.integer("Quantity: ");
            if (qty <= 0) {
                System.out.println("Quantity must be at least 1.");
                continue;
            }
            order.addLine(new Order.Line(item.getName(), qty, item.getFinalPrice()));
            System.out.println("Added " + qty + " x " + item.getName());
        }

        if (order.getLines().isEmpty()) {
            System.out.println("Order cancelled (nothing selected).");
            return;
        }

        System.out.println("\n--- ORDER SUMMARY ---");
        order.display();
        String confirm = Input.text("Confirm order? (y/n): ");
        if (confirm.equalsIgnoreCase("y")) {
            FileStorage.orders.add(order);
            FileStorage.saveOrders();
            System.out.println("Order #" + order.getId() + " placed! Status: PENDING");
        } else {
            System.out.println("Order cancelled.");
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
