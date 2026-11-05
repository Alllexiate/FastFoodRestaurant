/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package admin2;
import java.util.Scanner;
/**
 *
 * @author ACER NITRO V15
 */
public class Admin2 {

    /**
     * @param args the command line arguments
     */
    
      
    static Scanner input = new Scanner(System.in);

    // LOGIN ACCOUNTS
    static String[] usernames = {
        "customer", "employee", "admin"
    };

    static String[] passwords = {
        "123", "123", "admin123"
    };

    static String[] roles = {
        "CUSTOMER", "EMPLOYEE", "ADMIN"
    };

    public static void main(String[] args) {

        while (true) {

            System.out.println("\n==============================");
            System.out.println("       FOOD ORDERING SYSTEM");
            System.out.println("==============================");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print("Choose: ");

            int choice = input.nextInt();
            input.nextLine();

            if (choice == 1) {
                login();
            } 
            else if (choice == 2) {
                System.out.println("Thank you for using the system!");
                break;
            } 
            else {
                System.out.println("Invalid choice!");
            }
        }
    }

    // LOGIN FUNCTION
    public static void login() {

        System.out.println("\n---------- LOGIN ----------");

        System.out.print("Username: ");
        String username = input.nextLine();

        System.out.print("Password: ");
        String password = input.nextLine();

        boolean found = false;

        for (int i = 0; i < usernames.length; i++) {

            if (usernames[i].equals(username) &&
                passwords[i].equals(password)) {

                found = true;

                System.out.println("\nLogin Successful!");
                System.out.println("Welcome, " + username);
                System.out.println("Role: " + roles[i]);

                // IDENTIFY USER ROLE
                switch (roles[i]) {

                    case "CUSTOMER":
                        customerMenu();
                        break;

                    case "EMPLOYEE":
                        employeeMenu();
                        break;

                    case "ADMIN":
                        adminMenu();
                        break;
                }

                break;
            }
        }

        if (!found) {
            System.out.println("\nInvalid username or password!");
        }
    }

    // CUSTOMER MENU
    public static void customerMenu() {

        while (true) {

            System.out.println("\n========== CUSTOMER MENU ==========");
            System.out.println("1. View Food Menu");
            System.out.println("2. Place Order");
            System.out.println("3. View My Orders");
            System.out.println("4. Logout");

            System.out.print("Choose: ");
            int choice = input.nextInt();

            switch (choice) {

                case 1:
                    System.out.println("Displaying available food...");
                    break;

                case 2:
                    System.out.println("Place your food order...");
                    break;

                case 3:
                    System.out.println("Displaying your orders...");
                    break;

                case 4:
                    System.out.println("Customer logged out.");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // EMPLOYEE MENU
    public static void employeeMenu() {

        while (true) {

            System.out.println("\n========== EMPLOYEE MENU ==========");
            System.out.println("1. View Food Menu");
            System.out.println("2. View Customer Orders");
            System.out.println("3. Manage Orders");
            System.out.println("4. Logout");

            System.out.print("Choose: ");
            int choice = input.nextInt();

            switch (choice) {

                case 1:
                    System.out.println("Displaying food menu...");
                    break;

                case 2:
                    System.out.println("Displaying customer orders...");
                    break;

                case 3:
                    System.out.println("Managing customer orders...");
                    break;

                case 4:
                    System.out.println("Employee logged out.");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // ADMIN MENU
    public static void adminMenu() {

        while (true) {

            System.out.println("\n========== ADMIN MENU ==========");
            System.out.println("1. View User Accounts");
            System.out.println("2. Add Accounts");
            System.out.println("3. Change Account Roles");
            System.out.println("4. Reset Passwords");
            System.out.println("5. Delete Accounts");
            System.out.println("6. View Food and Order Records");
            System.out.println("7. Add Food Items");
            System.out.println("8. Change Food Prices");
            System.out.println("9. Apply Discounts");
            System.out.println("10. View Financial Reports");
            System.out.println("11. Logout");

            System.out.print("Choose: ");
            int choice = input.nextInt();

            switch (choice) {

                case 1:
                    System.out.println("Viewing all user accounts...");
                    break;

                case 2:
                    System.out.println("Adding a new account...");
                    break;

                case 3:
                    System.out.println("Changing account roles...");
                    break;

                case 4:
                    System.out.println("Resetting passwords...");
                    break;

                case 5:
                    System.out.println("Deleting accounts...");
                    break;

                case 6:
                    System.out.println("Viewing food and order records...");
                    break;

                case 7:
                    System.out.println("Adding food items...");
                    break;

                case 8:
                    System.out.println("Changing food prices...");
                    break;

                case 9:
                    System.out.println("Applying discounts...");
                    break;

                case 10:
                    System.out.println("Viewing financial reports...");
                    break;

                case 11:
                    System.out.println("Admin logged out.");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}