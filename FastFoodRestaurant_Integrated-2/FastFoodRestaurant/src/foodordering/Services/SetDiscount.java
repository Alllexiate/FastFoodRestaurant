package foodordering.Services;

import foodordering.Models.*;
import foodordering.Utilities.*;

public class SetDiscount {

    public static void setDiscount() {

        FoodItem.printTable(FileStorage.menu);

        if (FileStorage.menu.isEmpty()) {
            System.out.println("No food items available.");
            return;
        }

        FoodItem item = FileStorage.findItem(Input.text("Enter menu code (e.g. 05 or 17a): "));

        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        double discount = Input.decimal("Discount percentage (0 to remove): ");

        if (discount < 0 || discount > 100) {
            System.out.println("Discount must be 0-100.");
            return;
        }

        item.setDiscount(discount);
        FileStorage.saveMenu();

        System.out.printf("Discount updated! Final price: %.2f%n", item.getFinalPrice());
    }
}
