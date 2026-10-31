package viewdiscounts;

import java.util.Scanner;

public class Viewdiscounts {

    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("===== DISCOUNT MANAGER =====");

        System.out.print("Enter Food Name: ");
        String name = input.nextLine();

        System.out.print("Enter Original Price: PHP ");
        double price = input.nextDouble();

        System.out.print("Enter Discount Percentage: ");
        double discount = input.nextDouble();

        if (price < 0 || discount < 0 || discount > 100) {
            System.out.println("Invalid price or discount.");
            return;
        }

        double discountAmount = price * discount / 100;
        double finalPrice = price - discountAmount;

        System.out.println("\n===== DISCOUNT REPORT =====");
        System.out.println("Food Name: " + name);
        System.out.printf("Original Price: PHP %.2f%n", price);
        System.out.printf("Discount: %.2f%%%n", discount);
        System.out.printf("Discount Amount: PHP %.2f%n", discountAmount);
        System.out.printf("Final Price: PHP %.2f%n", finalPrice);

        System.out.println("============================");
    }
}