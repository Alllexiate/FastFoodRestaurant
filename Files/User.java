package foodordering;

public class User {
    public enum Role { ADMIN, MANAGER, EMPLOYEE, CUSTOMER }

    private final String username;
    private String password;
    private Role role;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(Role role) { this.role = role; }

    // Stored in users.txt as: username|password|ROLE
    public String toLine() {
        return username + "|" + password + "|" + role;
    }

    public static User fromLine(String line) {
        String[] p = line.split("\\|", -1);
        return new User(p[0], p[1], Role.valueOf(p[2]));
    }
}
