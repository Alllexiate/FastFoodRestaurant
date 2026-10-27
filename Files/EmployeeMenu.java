package foodordering;

public class EmployeeMenu {

    public static void show(User employee) {
        while (true) {
            System.out.println("\n--- EMPLOYEE MENU ---");
            System.out.println("1. View pending orders");
            System.out.println("2. Mark order as READY TO SERVE");
            System.out.println("3. View ready-to-serve orders");
            System.out.println("0. Logout");
            int choice = Input.integer("Choose: ");

            switch (choice) {
                case 1:
                    listByStatus(Order.Status.PENDING);
                    break;
                case 2:
                    markReady();
                    break;
                case 3:
                    listByStatus(Order.Status.READY_TO_SERVE);
                    break;
                case 0:
                    System.out.println("Logged out.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static boolean listByStatus(Order.Status status) {
        boolean found = false;
        for (Order o : FileStorage.orders) {
            if (o.getStatus() == status) {
                o.display();
                System.out.println();
                found = true;
            }
        }
        if (!found) System.out.println("No " + status + " orders.");
        return found;
    }

    private static void markReady() {
        if (!listByStatus(Order.Status.PENDING)) return;
        Order order = FileStorage.findOrder(Input.integer("Order ID to mark as ready: "));
        if (order == null) {
            System.out.println("Order not found.");
            return;
        }
        if (order.getStatus() != Order.Status.PENDING) {
            System.out.println("That order is not pending.");
            return;
        }
        order.setStatus(Order.Status.READY_TO_SERVE);
        FileStorage.saveOrders();
        System.out.println("Order #" + order.getId() + " is now READY TO SERVE.");
    }
}
