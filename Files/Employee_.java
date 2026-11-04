package employee_;

import java.io.*;
import java.util.*;

public class Employee_ { 
    static int MAX_ORDERS = 50;
    static int[] customerNumbers = new int[MAX_ORDERS];
    static String[] orderReceipts = new String[MAX_ORDERS];
    static int[] orderTotals = new int[MAX_ORDERS];
    static int[] statusCodes = new int[MAX_ORDERS]; 
    
    static int orderCounter = 0;     
    static int totalActiveOrders = 0; 

    // Read Order File & Confirm
    public static void readAndConfirmOrder(Scanner sc) {
        String fileName = "orders.txt"; 
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("\n>> Error: File '" + fileName + "' not found!");
            System.out.println(">> Make sure 'orders.txt' exists in your project directory.");
            return;
        }

        System.out.println("\n---------------------------------------------------------");
        System.out.println("             READING ORDER DETAILS FROM FILE             ");
        System.out.println("---------------------------------------------------------");

        String itemLinesOnly = ""; 
        double totalSum = 0;
        int totalCalories = 0;
        int amountPaid = 0;
        int change = 0;

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                System.out.println(line); 

                String trimmed = line.trim();
                String lower = trimmed.toLowerCase();

                if ((lower.startsWith("subtotal") || lower.startsWith("total amount due")) && trimmed.contains("PHP")) {
                    try {
                        String pricePart = trimmed.substring(trimmed.indexOf("PHP") + 3).trim();
                        totalSum = Double.parseDouble(pricePart);
                    } catch (Exception e) { }
                }
                if ((lower.contains("cash tendered") || lower.contains("cash paid") || lower.startsWith("cash")) && trimmed.contains("PHP")) {
                    try {
                        String cashPart = trimmed.substring(trimmed.indexOf("PHP") + 3).trim();
                        amountPaid = (int) Math.round(Double.parseDouble(cashPart));
                    } catch (Exception e) { }
                }
                if (lower.startsWith("change") && trimmed.contains("PHP")) {
                    try {
                        String changePart = trimmed.substring(trimmed.indexOf("PHP") + 3).trim();
                        change = (int) Math.round(Double.parseDouble(changePart));
                    } catch (Exception e) { }
                }
                String[] rawParts;
                if (trimmed.contains("|")) {
                    rawParts = trimmed.split("\\|");
                } else {
                    rawParts = trimmed.split(" "); 
                }
                String[] parts = new String[rawParts.length];
                int partCount = 0;
                for (int i = 0; i < rawParts.length; i++) {
                    if (!rawParts[i].trim().isEmpty()) {
                        parts[partCount] = rawParts[i].trim();
                        partCount++;
                    }
                }

                if (partCount >= 3) {
                    try {
                        int qty = Integer.parseInt(parts[0]);
                        double price = Double.parseDouble(parts[partCount - 2].replace("PHP", "").trim());
                        String itemName = "";
                        if (trimmed.contains("|")) {
                            itemName = parts[1];
                        } else {
                            for (int k = 1; k < partCount - 2; k++) {
                                itemName += parts[k] + " ";
                            }
                            itemName = itemName.trim();
                        }
                        
                        if (totalSum == 0) {
                            totalSum += (price * qty);
                        }
                        itemLinesOnly += String.format(" %-4d| %-35s | PHP %.2f\n", qty, itemName, price * qty);
                    } catch (NumberFormatException e) {
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to read file.");
            return;
        }
        int grandTotal = (int) Math.round(totalSum);
        if (amountPaid == 0) {
            amountPaid = grandTotal;
        }

        System.out.println("---------------------------------------------------------");
        System.out.println("Eat-In Total   : PHP " + grandTotal);
        System.out.println("Cash Tendered  : PHP " + amountPaid);
        System.out.println("Change         : PHP " + change);
        System.out.println("---------------------------------------------------------");

        System.out.print("CONFIRM this order and send to pending list? (Y/N): ");
        String confirm = sc.nextLine();

        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println(">> Order cancelled before confirmation.");
            return;
        }
        orderCounter++;
        int currentCustomerNo = orderCounter;

        String receipt = "";
        receipt += "---------------------------------------------------------\n";
        receipt += "                 Your order number is:                   \n";
        receipt += String.format("                         [%d]                            \n", currentCustomerNo);
        receipt += "---------------------------------------------------------\n";
        receipt += "             FAST & FOODEUS OPERATING CORP.              \n";
        receipt += "            Fast & FoodEUS - PINCHEON BRANCH             \n";
        receipt += "                Pincheon Road, Davao City                \n";
        receipt += "                 VAT REG TIN 010-345-678-000             \n";
        receipt += "                   For online ordering,                  \n";
        receipt += "                visit fastandfoodeus.ph                  \n";
        receipt += "      Tell us about your Fast & Foodeus experience       \n";
        receipt += "                www.foodeusfeed.com                      \n\n";
        receipt += "QTY   | Item                                | Price      \n";
        receipt += "---------------------------------------------------------\n";
        receipt += itemLinesOnly;
        receipt += "---------------------------------------------------------\n";
        receipt += String.format("Eat-In Total:   PHP %d\n", grandTotal);
        receipt += String.format("Cash Tendered:  PHP %d\n", amountPaid);
        receipt += String.format("Change:         PHP %d\n\n", change);
        receipt += "              Thank you, please come again.              \n";
        receipt += "---------------------------------------------------------\n";

        customerNumbers[totalActiveOrders] = currentCustomerNo;
        orderReceipts[totalActiveOrders] = receipt;
        orderTotals[totalActiveOrders] = grandTotal;
        statusCodes[totalActiveOrders] = 1; 
        totalActiveOrders++;

        try {
            String receiptFileName = "Customer_" + currentCustomerNo + "_Receipt.txt";
            FileWriter writer = new FileWriter(receiptFileName);
            writer.write(receipt);
            writer.close();
            System.out.println("\n>> Order CONFIRMED! Customer #" + currentCustomerNo + " added to pending orders.");
            System.out.println(">> Receipt saved to '" + receiptFileName + "'.");
        } catch (IOException e) {
            System.out.println("Unable to write file.");
        }
    }

    // View Live Order Status Board 
    public static void viewOrderStatus() {
        System.out.println("\n==========================================================================");
        System.out.println("                   FAST & FOODEUS LIVE ORDER STATUS BOARD                ");
        System.out.println("==========================================================================");
        System.out.printf("%-15s | %-15s | %-30s%n", "CUSTOMER NO.", "TOTAL AMOUNT", "ORDER STATUS");
        System.out.println("--------------------------------------------------------------------------");

        if (totalActiveOrders == 0) {
            System.out.println("No pending customer orders found.");
        } else {
            for (int i = 0; i < totalActiveOrders; i++) {
                String statusText;
                switch (statusCodes[i]) {
                    case 1: statusText = "Preparing in Kitchen"; break;
                    case 2: statusText = "Ready to Serve!"; break;
                    case 3: statusText = "Delayed (Kitchen Load)"; break;
                    case 4: statusText = "CANCELLED"; break;
                    default: statusText = "Unknown"; break;
                }
                System.out.printf("Customer #%-7d | PHP %-11d | %-30s%n", 
                        customerNumbers[i], orderTotals[i], statusText);
            }
        }
        System.out.println("==========================================================================\n");
    }

    // Update Order Status + Alert System
    public static void updateOrderStatus(Scanner sc) {
        if (totalActiveOrders == 0) {
            System.out.println("\nNo pending orders to update.");
            return;
        }

        System.out.print("\nEnter Customer Number to update (e.g., 1 for Customer #1): ");
        int targetNum;
        try {
            targetNum = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Customer Number must be an integer.");
            return;
        }

        int index = -1;
        for (int i = 0; i < totalActiveOrders; i++) {
            if (customerNumbers[i] == targetNum) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            System.out.println("Customer #" + targetNum + " not found.");
            return;
        }

        System.out.println("\nCurrent Status for Customer #" + targetNum + ": " + getStatusString(statusCodes[index]));
        System.out.println("[1] Preparing in Kitchen");
        System.out.println("[2] Ready to Serve!");
        System.out.println("[3] Delayed (Kitchen Load)");
        System.out.println("[4] CANCELLED");
        System.out.print("Choice (1-4): ");

        try {
            int newStatus = Integer.parseInt(sc.nextLine());
            if (newStatus >= 1 && newStatus <= 4) {
                statusCodes[index] = newStatus;
                System.out.println(">> Status updated successfully to: " + getStatusString(newStatus));

                if (newStatus == 2) {
                    System.out.println("\n**************************************************");
                    System.out.println("  SYSTEM ALERT FOR CUSTOMER #" + targetNum + ": FOOD IS READY!");
                    System.out.println("**************************************************");
                }
            } else {
                System.out.println("Invalid status choice.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid status input.");
        }
    }
    private static String getStatusString(int statusCode) {
        switch (statusCode) {
            case 1: return "Preparing in Kitchen";
            case 2: return "Ready to Serve!";
            case 3: return "Delayed (Kitchen Load)";
            case 4: return "CANCELLED";
            default: return "Unknown";
        }
    }

    // Cancel or Delete Order
    public static void cancelOrDeleteOrder(Scanner sc) {
        if (totalActiveOrders == 0) {
            System.out.println("\nNo pending orders to manage.");
            return;
        }

        System.out.print("\nEnter Customer Number to manage: ");
        int targetNum;
        try {
            targetNum = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Customer Number must be an integer.");
            return;
        }

        int index = -1;
        for (int i = 0; i < totalActiveOrders; i++) {
            if (customerNumbers[i] == targetNum) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            System.out.println("Customer #" + targetNum + " not found.");
            return;
        }

        if (statusCodes[index] == 2) {
            System.out.println("\n>> CANNOT CANCEL OR DELETE ORDER!");
            System.out.println(">> Order for Customer #" + targetNum + " is already 'Ready to Serve!'");
            return;
        }

        System.out.println("\nFound Record: Customer #" + customerNumbers[index] + " (Status: " + getStatusString(statusCodes[index]) + ")");
        System.out.println("[1] Cancel Order (Keep in History)");
        System.out.println("[2] Delete Order Permanently");
        System.out.print("Choice: ");
        String choice = sc.nextLine();

        if (choice.equals("1")) {
            statusCodes[index] = 4;
            System.out.println(">> Order for Customer #" + targetNum + " marked as CANCELLED.");
        } else if (choice.equals("2")) {

            for (int i = index; i < totalActiveOrders - 1; i++) {
                customerNumbers[i] = customerNumbers[i + 1];
                orderReceipts[i] = orderReceipts[i + 1];
                orderTotals[i] = orderTotals[i + 1];
                statusCodes[i] = statusCodes[i + 1];
            }
            totalActiveOrders--;
            System.out.println(">> Order record for Customer #" + targetNum + " DELETED permanently.");
        } else {
            System.out.println("Invalid choice.");
        }
    }

    // Main Control Loop
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n==============================================");
            System.out.println("        FAST & FOODEUS ORDER SYSTEM           ");
            System.out.println("==============================================");
            System.out.println("1. Read File & Confirm Order");
            System.out.println("2. View Live Order Status Board");
            System.out.println("3. Update Order Status");
            System.out.println("4. Cancel or Delete Order");
            System.out.println("5. Exit");
            System.out.print("Choose option (1-5): ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1": readAndConfirmOrder(sc); break;
                case "2": viewOrderStatus(); break;
                case "3": updateOrderStatus(sc); break;
                case "4": cancelOrDeleteOrder(sc); break;
                case "5": 
                    System.out.println("Exiting Fast & FoodEUS System. Goodbye!");
                    running = false; 
                    break;
                default: System.out.println("Invalid choice. Try again."); break;
            }
        }
    }
}
