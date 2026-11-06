package foodordering.Menus;

import foodordering.Models.*;
import foodordering.Utilities.*;

public class EmployeeMenu {

    public static void show(User employee) {
        while (true) {
            System.out.println("\n--- EMPLOYEE MENU ---");
            System.out.println("1. View active orders (pending / delayed)");
            System.out.println("2. Update order status");
            System.out.println("3. View ready-to-serve orders");
            System.out.println("4. Cancel or delete order");
            System.out.println("5. Print receipt");
            System.out.println("6. Live order status board");
            System.out.println("0. Logout");
            int choice = Input.integer("Choose: ");

            switch (choice) {
                case 1:
                    listOrders("No active orders.", Order.Status.PENDING, Order.Status.DELAYED);
                    break;
                case 2:
                    updateStatus();
                    break;
                case 3:
                    listOrders("No ready-to-serve orders.", Order.Status.READY_TO_SERVE);
                    break;
                case 4:
                    cancelOrDelete();
                    break;
                case 5:
                    printReceipt();
                    break;
                case 6:
                    statusBoard();
                    break;
                case 0:
                    System.out.println("Logged out.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static boolean listOrders(String emptyMessage, Order.Status... statuses) {
        boolean found = false;
        for (Order o : FileStorage.orders) {
            for (Order.Status s : statuses) {
                if (o.getStatus() == s) {
                    o.display();
                    System.out.println();
                    found = true;
                    break;
                }
            }
        }
        if (!found) System.out.println(emptyMessage);
        return found;
    }

    private static void statusBoard() {
        if (FileStorage.orders.isEmpty()) {
            System.out.println("No orders yet.");
            return;
        }
        String line = Input.line(86);
        System.out.println("\n" + line);
        System.out.println("                         LIVE ORDER STATUS BOARD");
        System.out.println(line);
        System.out.printf("%-9s %-14s %12s  %-24s %s%n", "ORDER NO.", "CUSTOMER", "TOTAL (PHP)", "STATUS", "PLACED");
        System.out.println(line);
        for (Order o : FileStorage.orders) {
            System.out.printf("#%-8d %-14s %12.2f  %-24s %s%n",
                    o.getId(), o.getCustomer(), o.getTotal(), o.getStatus().getLabel(), o.getDateTime());
        }
        System.out.println(line);
    }

    private static void updateStatus() {
        if (!listOrders("No active orders to update.", Order.Status.PENDING, Order.Status.DELAYED)) return;

        Order order = FileStorage.findOrder(Input.integer("Order ID to update: "));
        if (order == null) {
            System.out.println("Order not found.");
            return;
        }
        if (order.getStatus() != Order.Status.PENDING && order.getStatus() != Order.Status.DELAYED) {
            System.out.println("Only pending or delayed orders can be updated.");
            return;
        }

        System.out.println("Current status: " + order.getStatus());
        System.out.println("1. PENDING (preparing in kitchen)");
        System.out.println("2. DELAYED (kitchen load)");
        System.out.println("3. READY TO SERVE");
        int choice = Input.integer("New status: ");

        Order.Status newStatus;
        switch (choice) {
            case 1:
                newStatus = Order.Status.PENDING;
                break;
            case 2:
                newStatus = Order.Status.DELAYED;
                break;
            case 3:
                newStatus = Order.Status.READY_TO_SERVE;
                break;
            default:
                System.out.println("Invalid status choice.");
                return;
        }

        order.setStatus(newStatus);
        FileStorage.saveOrders();
        System.out.println(">> Order #" + order.getId() + " is now " + newStatus + ".");

        if (newStatus == Order.Status.READY_TO_SERVE) {
            System.out.println("\n**************************************************");
            System.out.println("  SYSTEM ALERT FOR ORDER #" + order.getId() + ": FOOD IS READY!");
            System.out.println("**************************************************");
        }
    }

    private static void cancelOrDelete() {
        if (!listOrders("No orders to manage.",
                Order.Status.PENDING, Order.Status.DELAYED, Order.Status.CANCELLED)) return;

        Order order = FileStorage.findOrder(Input.integer("Order ID to manage: "));
        if (order == null) {
            System.out.println("Order not found.");
            return;
        }
        if (order.getStatus() == Order.Status.READY_TO_SERVE) {
            System.out.println(">> CANNOT CANCEL OR DELETE: order #" + order.getId() + " is already READY TO SERVE.");
            return;
        }

        System.out.println("Order #" + order.getId() + " (" + order.getStatus() + ")");
        System.out.println("1. Cancel order (keep in history)");
        System.out.println("2. Delete order permanently");
        String choice = Input.text("Choice: ");

        if (choice.equals("1")) {
            if (order.getStatus() == Order.Status.CANCELLED) {
                System.out.println("That order is already cancelled.");
                return;
            }
            order.setStatus(Order.Status.CANCELLED);
            FileStorage.saveOrders();
            System.out.println(">> Order #" + order.getId() + " marked as CANCELLED.");
        } else if (choice.equals("2")) {
            String confirm = Input.text("Delete order #" + order.getId() + " permanently? (y/n): ");
            if (confirm.equalsIgnoreCase("y")) {
                FileStorage.orders.remove(order);
                FileStorage.saveOrders();
                System.out.println(">> Order #" + order.getId() + " deleted permanently.");
            } else {
                System.out.println("Cancelled.");
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private static void printReceipt() {
        if (FileStorage.orders.isEmpty()) {
            System.out.println("No orders yet.");
            return;
        }
        Order order = FileStorage.findOrder(Input.integer("Order ID for receipt: "));
        if (order == null) {
            System.out.println("Order not found.");
            return;
        }
        if (order.getStatus() == Order.Status.CANCELLED) {
            System.out.println("Cancelled orders have no receipt.");
            return;
        }

        if (order.getCashTendered() <= 0) {   // older order with no payment recorded
            double total = order.getTotal();
            double cash;
            while (true) {
                cash = Input.decimal(String.format("Total is PHP %.2f. Cash tendered: ", total));
                if (cash >= total) break;
                System.out.println("Cash is less than the total.");
            }
            order.setCashTendered(cash);
            FileStorage.saveOrders();
        }
        Receipt.print(order);
    }
}
