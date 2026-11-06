package foodordering;

import foodordering.Menus.*;
import foodordering.Models.User;
import foodordering.Services.DefaultMenu;
import foodordering.Utilities.FileStorage;
import foodordering.Utilities.Input;

public class Main {
    public static void main(String[] args) {
        FileStorage.loadAll();
        System.out.println("Data folder: " + new java.io.File(FileStorage.DATA_DIR).getAbsolutePath());

        // First run: load the built-in menu (no file needed).
        // After that the saved Files/menu.txt is used, so the Manager's changes are kept.
        if (!FileStorage.isDefaultMenuLoaded()) {
            int added = DefaultMenu.loadMissing();
            FileStorage.markDefaultMenuLoaded();
            if (added > 0) {
                System.out.println("Built-in menu loaded (" + added + " items).");
            }
        }

        while (true) {
            System.out.println("\n===== FOOD ORDERING SYSTEM =====");
            System.out.println("1. Login");
            System.out.println("2. Register as Customer");
            System.out.println("0. Exit");
            int choice = Input.integer("Choose: ");

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 0:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void login() {
        String username = Input.text("Username: ");
        String password = Input.text("Password: ");

        User user = FileStorage.findUser(username);
        if (user == null || !user.getPassword().equals(password)) {
            System.out.println("Invalid username or password.");
            return;
        }

        System.out.println("\nWelcome, " + user.getUsername() + " (" + user.getRole() + ")");
        switch (user.getRole()) {
            case ADMIN:
                AdminMenu.show(user);
                break;
            case MANAGER:
                ManagerMenu.show(user);
                break;
            case EMPLOYEE:
                EmployeeMenu.show(user);
                break;
            case CUSTOMER:
                CustomerMenu.show(user);
                break;
        }
    }

    private static void register() {
        String username = Input.clean(Input.text("Choose a username: "));
        if (username.isEmpty()) {
            System.out.println("Username cannot be empty.");
            return;
        }
        if (FileStorage.findUser(username) != null) {
            System.out.println("That username is already taken.");
            return;
        }
        String password = Input.clean(Input.text("Choose a password: "));
        if (password.isEmpty()) {
            System.out.println("Password cannot be empty.");
            return;
        }
        FileStorage.users.add(new User(username, password, User.Role.CUSTOMER));
        FileStorage.saveUsers();
        System.out.println("Account created! You can now log in.");
    }
}
