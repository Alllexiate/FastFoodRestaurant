package foodordering.Models;

import foodordering.Utilities.FileStorage;
import foodordering.Utilities.Input;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Receipt {
    private static final int WIDTH = 57;

    public static String build(Order order, double cash) {
        double total = order.getTotal();
        double change = cash - total;
        String line = Input.line(WIDTH);

        StringBuilder sb = new StringBuilder();
        sb.append(line).append("\n");
        sb.append(center("Your order number is:")).append("\n");
        sb.append(center("[" + order.getId() + "]")).append("\n");
        sb.append(line).append("\n");
        sb.append(center("FAST & FOODEUS OPERATING CORP.")).append("\n");
        sb.append(center("Fast & FoodEUS - PINCHEON BRANCH")).append("\n");
        sb.append(center("Pincheon Road, Davao City")).append("\n");
        sb.append(center("VAT REG TIN 010-345-678-000")).append("\n");
        sb.append(center("For online ordering,")).append("\n");
        sb.append(center("visit fastandfoodeus.ph")).append("\n");
        sb.append(center("Tell us about your Fast & Foodeus experience")).append("\n");
        sb.append(center("www.foodeusfeed.com")).append("\n\n");
        sb.append("Customer: ").append(order.getCustomer()).append("\n");
        sb.append("QTY  | Item                           | Price\n");
        sb.append(line).append("\n");
        for (Order.Line l : order.getLines()) {
            sb.append(String.format(" %-3d | %-30s | PHP %.2f%n", l.qty, l.name, l.subtotal()));
        }
        sb.append(line).append("\n");
        sb.append(String.format("Total:          PHP %.2f%n", total));
        sb.append(String.format("Cash Tendered:  PHP %.2f%n", cash));
        sb.append(String.format("Change:         PHP %.2f%n%n", change));
        sb.append(center("Thank you, please come again.")).append("\n");
        sb.append(line).append("\n");
        return sb.toString();
    }

    /** Prints the receipt on screen and saves it as Receipt_<order id>.txt */
    public static void print(Order order, double cash) {
        String receipt = build(order, cash);
        System.out.println();
        System.out.println(receipt);

        new java.io.File(FileStorage.DATA_DIR).mkdirs();
        String fileName = FileStorage.DATA_DIR + "/Receipt_" + order.getId() + ".txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            pw.print(receipt);
            System.out.println(">> Receipt saved to '" + fileName + "'.");
        } catch (IOException e) {
            System.out.println("Could not save receipt: " + e.getMessage());
        }
    }

    private static String center(String text) {
        int pad = Math.max(0, (WIDTH - text.length()) / 2);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pad; i++) sb.append(' ');
        return sb.append(text).toString();
    }
}
