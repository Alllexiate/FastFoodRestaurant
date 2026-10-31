package viewfinancialreports;

import java.util.Scanner;

public class ViewFinancialReports {

    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("===== FINANCIAL REPORT =====");

        System.out.print("Enter Total Sales: PHP ");
        double totalSales = input.nextDouble();

        System.out.print("Enter Completed Sales: ");
        int completedSales = input.nextInt();

        System.out.printf("Total Sales: PHP %.2f%n", totalSales);
        System.out.println("Completed Sales: " + completedSales);

        double averageSale = 0.0;

        if (completedSales > 0) {
            averageSale = totalSales / completedSales;
        }

        System.out.printf("Average Sale: PHP %.2f%n", averageSale);

        System.out.println("============================");
    }
}