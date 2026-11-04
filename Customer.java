package customer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Customer {

    public static void main(String[] args) throws IOException {
        
        Scanner s = new Scanner(System.in);
        
        String[] id = new String[50];
        String[] name = new String[50];
        int[] price = new int[50];
        int[] kcal = new int[50];
        int[] qt = new int[50];
        int order = 0;
        
        System.out.println("Welcome, Customer!\n");
        
        File m = new File("menu1.txt"); 

        if (m.exists()) {
            try (Scanner r = new Scanner(new File("menu1.txt"))) {
                while (r.hasNextLine()) {
                    String l = r.nextLine();
                    
                    // Output raw line directly to match menu1.txt format in terminal
                    System.out.println(l);

                    if (l.trim().isEmpty() || !l.contains("-") || !l.contains("PHP")) {
                        continue;
                    }

                    // Split item ID from the rest of the text
                    String[] part = l.trim().split(" - ");
                    if (part.length >= 2) {
                        id[order] = part[0].trim();
                        
                        String rest = part[1].trim();
                        String[] namePrice = rest.split("PHP");
                        
                        if (namePrice.length >= 2) {
                            name[order] = namePrice[0].trim();
                            
                            // Parse price by isolating digits after 'PHP'
                            String priceStr = namePrice[1].trim().split("\\s+")[0];
                            price[order] = Integer.parseInt(priceStr);
                            
                            // Parse calories inside (xxx kcal) if available
                            if (name[order].contains("(") && name[order].contains("kcal)")) {
                                try {
                                    String kcalPart = name[order].substring(name[order].lastIndexOf("(") + 1, name[order].lastIndexOf("kcal")).trim();
                                    kcal[order] = Integer.parseInt(kcalPart);
                                } catch (Exception e) {
                                    kcal[order] = 0;
                                }
                            } else {
                                kcal[order] = 0;
                            }

                            qt[order] = 0;
                            order++;
                        }
                    }
                }
            }
        } else {
            System.out.println("Error: menu1.txt not found at " + m.getAbsolutePath());
        }
        
        int total = 0;
        int totalKcal = 0;
        
        System.out.println("\n==================================================\n");
        
        while (true) {
            System.out.printf("Type:\n" + "Menu ID no.\n" + "[1] DONE WITH ORDER\n" + "[2] CANCEL\n");
            System.out.printf("Menu Item No.:\n");
            String itemNo = s.nextLine();
            
            if (itemNo.equalsIgnoreCase("1")) {
                if (total == 0) {
                    System.out.println("You haven't placed orders yet. Select from the menu.");
                    continue;
                } else {
                    break;
                }
            }
            if (itemNo.equals("2")) {
                for (int i = 0; i < order; i++) {
                    qt[i] = 0;
                    total = 0;
                    totalKcal = 0;
                }
                System.out.printf("Order Cancelled. Thank you and Come Again.\n");
                break;
            }
            
            int sameId = 0;
            
            for (int i = 0; i < order; i++) {
                if (id[i].equalsIgnoreCase(itemNo)) {
                    sameId++;
                    while (true) {
                        // Display Name, PHP Price, and Calories in prompt
                        System.out.printf(name[i] + " - PHP " + price[i] + "\n[d] REMOVE item" + "\nQUANTITY: ");
                        String qtyI = s.nextLine();
                        int qty;
            
                        if (qtyI.equalsIgnoreCase("d")) {
                            qty = 0;
                        } else {
                            try {
                                qty = Integer.parseInt(qtyI);
                            } catch (NumberFormatException e) {
                                System.out.printf("INVALID input. ENTER a VALID quantity number" + "\n[d] REMOVE\n");
                                continue;
                            }
                        }  
                        if (qty == 0) {
                            if (qt[i] == 0) {
                                System.out.println("Item was not in your order.");                 
                            } else {
                                total = total - (price[i] * qt[i]);
                                totalKcal = totalKcal - (kcal[i] * qt[i]);
                                qt[i] = 0;
                                System.out.println("REMOVED " + name[i] + " from your order.");
                            }
                            break;
                        } else if (qty > 0) {
                            qt[i] = qt[i] + qty;
                                
                            int sTotal = price[i] * qty;
                            int sKcal = kcal[i] * qty;
                            
                            total = total + sTotal;
                            totalKcal = totalKcal + sKcal;
                                
                            System.out.println("ADDED " + qty + " x " + name[i] + " = PHP " + sTotal + " (" + sKcal + " kcal)");
                            break;
                        } else {
                            System.out.println("Quantity cannot be negative.");
                        }
                    }
                    break;
                }
            }
            
            if (sameId == 0) {
                System.out.println("Invalid Menu Item No. Try again.");
            }
        }
        
        if (total > 0) {
            System.out.println("==============================");
            System.out.println("Subtotal: PHP " + total);
            System.out.println("Total Calories: " + totalKcal + " kcal");
            System.out.println("==============================");
            
            System.out.println("Add available discount (e.g. 20 for 20%):");
            int discount = s.nextInt();
            
            int discountCalcu = (total * discount) / 100;
            int fTotal = total - discountCalcu;
            
            System.out.println("==============================");
            System.out.println("Discount added: -PHP " + discountCalcu); 
            System.out.println("New Total: PHP " + fTotal);
            System.out.println("==============================");
            
            boolean payS = false;

            for (int i = 1; i <= 3; i++) {
                System.out.print("Cash Amount: PHP ");
                int cash = s.nextInt();

                if (cash >= fTotal) { 
                    int change = cash - fTotal;
                    System.out.println("Change: PHP " + change);
                    System.out.printf("\nThank You and Come Again!\n");
                    
                    saveOrder(name, price, qt, order, total, discountCalcu, fTotal, cash, change);
                    payS = true;
                    break; 
                } else if (i < 3) {
                    System.out.printf("Insufficient Amount. " + (3 - i) + " of 3 attempts left.\n");
                } else {
                    System.out.printf("Insufficient Amount. Reached maximum number of attempts.\n");
                }
            }
            if (!payS) {
                System.out.printf("Payment failed. Order Cancelled.\nThank You and Come Again.\n");
            }
        }
    }
  
    public static void saveOrder(String[] name, int[] price, int[] qt, int orderCount, int subtotal, int discount, int fTotal, int cash, int change) throws IOException {
        File o = new File("orders.txt");
        int orderNo = 1;

        // Calculate Order Number based on existing receipts in orders.txt
        if (o.exists()) {
            try (Scanner r = new Scanner(o)) {
                while (r.hasNextLine()) {
                    String line = r.nextLine();
                    if (line.startsWith("Order No")) {
                        orderNo++;
                    }
                }
            }
        }

        // Standard String variable for date/time (No Date/Time libraries needed)
        String dateTime = "Nov 04, 2026 - 02:15 PM";

        // Calculate 12% VAT based on final total
        double vat = fTotal * 0.12;

        try (FileWriter w = new FileWriter("orders.txt", true)) {
            w.write("==================================================\n");
            w.write("             FAST & FOODEUS              \n");
            w.write("       Pincheon, Davao City Branch       \n");
            w.write("==================================================\n");
            w.write(String.format("Order No   : %d\n", orderNo));
            w.write(String.format("Date/Time  : %s\n", dateTime));
            w.write("Type       : Dine-In / Eat-In\n");
            w.write("Server     : Cashier 01\n");
            w.write("==================================================\n\n");
            w.write(String.format("%-4s %-28s %-8s %-7s\n", "QTY", "ITEM DESCRIPTION", "PRICE", "TOTAL"));
            w.write("--------------------------------------------------\n");

            for (int i = 0; i < orderCount; i++) {
                if (qt[i] > 0) {
                    double itemPrice = price[i];
                    double itemTotal = price[i] * qt[i];
                    w.write(String.format("%-4d %-28s %8.2f %8.2f\n", qt[i], name[i], itemPrice, itemTotal));
                }
            }

            w.write("--------------------------------------------------\n\n");
            w.write(String.format("Subtotal                         : PHP %.2f\n", (double) subtotal));
            w.write(String.format("Discount / Adj                   : PHP -%.2f\n", (double) discount));
            w.write(String.format("VAT (12%%)                        : PHP  %.2f\n", vat));
            w.write("--------------------------------------------------\n");
            w.write(String.format("TOTAL AMOUNT DUE                 : PHP %.2f\n\n", (double) fTotal));
            w.write("Payment Method                   : Cash\n");
            w.write(String.format("Cash Tendered                    : PHP %.2f\n", (double) cash));
            w.write(String.format("Change                           : PHP %.2f\n\n", (double) change));
            w.write("==================================================\n");
            w.write("        Thank you! Please come again.        \n");
            w.write("==================================================\n\n\n");

            System.out.println("order saved at " + o.getAbsolutePath());
        }
    }
}