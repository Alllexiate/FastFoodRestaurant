package foodordering;

/** ADMIN: manages the database and the logins of all roles. */
public class AdminMenu {

    public static void show(User admin) {
        while (true) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. View all accounts");
            System.out.println("2. Add account");
            System.out.println("3. Change account role");
            System.out.println("4. Reset account password");
            System.out.println("5. Delete account");
            System.out.println("6. View menu database");
            System.out.println("7. View orders database");
            System.out.println("0. Logout");
            int choice = Input.integer("Choose: ");

            switch (choice) {
                case 1:
                    viewUsers();
                    break;
                case 2:
                    addUser();
                    break;
                case 3:
                    changeRole(admin);
                    break;
                case 4:
                    resetPassword();
                    break;
                case 5:
                    deleteUser(admin);
                    break;
                case 6:
                    FoodItem.printTable(FileStorage.menu);
                    break;
                case 7:
                    viewOrders();
                    break;
                case 0:
                    System.out.println("Logged out.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void viewUsers() {
        System.out.printf("%-20s %-12s %-10s%n", "Username", "Role", "Password");
        System.out.println(Input.line(44));
        for (User u : FileStorage.users) {
            System.out.printf("%-20s %-12s %-10s%n", u.getUsername(), u.getRole(), "****");
        }
    }

    private static User.Role pickRole() {
        User.Role[] roles = User.Role.values();
        for (int i = 0; i < roles.length; i++) {
            System.out.println((i + 1) + ". " + roles[i]);
        }
        int pick = Input.integer("Select role: ");
        if (pick < 1 || pick > roles.length) return null;
        return roles[pick - 1];
    }

    private static void addUser() {
        String username = Input.clean(Input.text("New username: "));
        if (username.isEmpty()) {
            System.out.println("Username cannot be empty.");
            return;
        }
        if (FileStorage.findUser(username) != null) {
            System.out.println("That username already exists.");
            return;
        }
        String password = Input.clean(Input.text("Password: "));
        if (password.isEmpty()) {
            System.out.println("Password cannot be empty.");
            return;
        }
        User.Role role = pickRole();
        if (role == null) {
            System.out.println("Invalid role.");
            return;
        }
        FileStorage.users.add(new User(username, password, role));
        FileStorage.saveUsers();
        System.out.println("Account created: " + username + " (" + role + ")");
    }

    private static void changeRole(User admin) {
        User target = FileStorage.findUser(Input.text("Username: "));
        if (target == null) {
            System.out.println("Account not found.");
            return;
        }
        if (target == admin) {
            System.out.println("You cannot change your own role.");
            return;
        }
        User.Role role = pickRole();
        if (role == null) {
            System.out.println("Invalid role.");
            return;
        }
        target.setRole(role);
        FileStorage.saveUsers();
        System.out.println(target.getUsername() + " is now " + role + ".");
    }

    private static void resetPassword() {
        User target = FileStorage.findUser(Input.text("Username: "));
        if (target == null) {
            System.out.println("Account not found.");
            return;
        }
        String password = Input.clean(Input.text("New password: "));
        if (password.isEmpty()) {
            System.out.println("Password cannot be empty.");
            return;
        }
        target.setPassword(password);
        FileStorage.saveUsers();
        System.out.println("Password updated for " + target.getUsername() + ".");
    }

    private static void deleteUser(User admin) {
        User target = FileStorage.findUser(Input.text("Username to delete: "));
        if (target == null) {
            System.out.println("Account not found.");
            return;
        }
        if (target == admin) {
            System.out.println("You cannot delete your own account.");
            return;
        }
        String confirm = Input.text("Delete " + target.getUsername() + "? (y/n): ");
        if (confirm.equalsIgnoreCase("y")) {
            FileStorage.users.remove(target);
            FileStorage.saveUsers();
            System.out.println("Account deleted.");
        } else {
            System.out.println("Cancelled.");
        }
    }

    private static void viewOrders() {
        if (FileStorage.orders.isEmpty()) {
            System.out.println("No orders yet.");
            return;
        }
        for (Order o : FileStorage.orders) {
            o.display();
            System.out.println();
        }
    }
}
