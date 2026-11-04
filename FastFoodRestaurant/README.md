# Fast Food Restaurant (Java console app)

Role-based ordering system. Data is saved in .txt files in the project folder.

| Role | What it does | File |
|---|---|---|
| Admin | Manage accounts and roles, view data | `AdminMenu.java` |
| Manager | Menu items, prices, discounts, financial report | `ManagerMenu.java`, `SetDiscount.java`, `FinancialReport.java` |
| Employee | Update order status, cancel orders, print receipts | `EmployeeMenu.java`, `Receipt.java` |
| Customer | View menu, place orders | `CustomerMenu.java` |

Shared classes: `User`, `FoodItem`, `Order`, `FileStorage`, `Input`. Entry point: `Main.java`.

## Run (NetBeans)
Set the main class to `foodordering.Main` and press F6.

Default logins: admin/admin123, manager/manager123, employee/employee123, customer/customer123
