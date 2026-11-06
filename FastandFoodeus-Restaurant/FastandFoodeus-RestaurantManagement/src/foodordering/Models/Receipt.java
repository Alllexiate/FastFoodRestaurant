package foodordering.Models;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import foodordering.Utilities.FileStorage;
import foodordering.Utilities.Input;

public class Receipt {
    private static final int WIDTH = 59;
    private static final int NAME_WIDTH = 36;

    public static String build(Order order) {
        double total = order.getTotal();
        double discount = order.getListTotal() - total;
        double cash = order.getCashTendered();
        double change = cash - total;

        String thick = rule('=');
        String thin = Input.line(WIDTH);

        StringBuilder sb = new StringBuilder();
        sb.append(thick).append("\n");
        sb.append(center("Your order number is:")).append("\n");
        sb.append(center("[" + order.getId() + "]")).append("\n");
        sb.append(thick).append("\n");
        sb.append(center("FAST & FOODEUS OPERATING CORP.")).append("\n");
        sb.append(center("Fast & FoodEUS - PINCHEON BRANCH")).append("\n");
        sb.append(center("Pincheon Road, Davao City")).append("\n");
        sb.append(center("VAT REG TIN 010-345-678-000")).append("\n");
        sb.append(thick).append("\n");
        sb.append(String.format("Order No   : %d%n", order.getId()));
        sb.append(String.format("Date/Time  : %s%n", order.getDateTime().isEmpty() ? "-" : order.getDateTime()));
        sb.append(String.format("Customer   : %s%n", order.getCustomer()));
        sb.append("Type       : Dine-In / Eat-In\n");
        sb.append("Server     : Cashier 01\n");
        sb.append(thick).append("\n\n");
        sb.append(String.format("%-4s %-" + NAME_WIDTH + "s %8s %8s%n", "QTY", "ITEM DESCRIPTION", "PRICE", "TOTAL"));
        sb.append(thin).append("\n");
        for (Order.Line l : order.getLines()) {
            String first = l.name.length() > NAME_WIDTH ? l.name.substring(0, NAME_WIDTH) : l.name;
            String rest = l.name.length() > NAME_WIDTH ? l.name.substring(NAME_WIDTH) : "";
            sb.append(String.format("%-4d %-" + NAME_WIDTH + "s %8.2f %8.2f%n", l.qty, first, l.listPrice, l.listSubtotal()));
            if (!rest.isEmpty()) sb.append("     ").append(rest).append("\n");
        }
        sb.append(thin).append("\n\n");
        sb.append(String.format("%-34s: PHP %10.2f%n", "Subtotal", order.getListTotal()));
        sb.append(String.format("%-34s: PHP %10.2f%n", "Discount / Adj", discount > 0.004 ? -discount : 0.0));
        sb.append(String.format("%-34s: PHP %10.2f%n", "VAT (12%) included", order.getVat()));
        sb.append(thin).append("\n");
        sb.append(String.format("%-34s: PHP %10.2f%n%n", "TOTAL AMOUNT DUE", total));
        sb.append(String.format("%-34s: %s%n", "Payment Method", "Cash"));
        sb.append(String.format("%-34s: PHP %10.2f%n", "Cash Tendered", cash));
        sb.append(String.format("%-34s: PHP %10.2f%n%n", "Change", change));
        sb.append(thick).append("\n");
        sb.append(center("Thank you! Please come again.")).append("\n");
        sb.append(center("For online ordering, visit fastandfoodeus.ph")).append("\n");
        sb.append(center("Tell us about your experience: www.foodeusfeed.com")).append("\n");
        sb.append(thick).append("\n");
        return sb.toString();
    }

    /** Prints the receipt on screen and saves it as Files/Receipt_<order id>.txt */
    public static void print(Order order) {
        String receipt = build(order);
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

    private static String rule(char c) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < WIDTH; i++) sb.append(c);
        return sb.toString();
    }

    private static String center(String text) {
        int pad = Math.max(0, (WIDTH - text.length()) / 2);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pad; i++) sb.append(' ');
        return sb.append(text).toString();
    }
}
