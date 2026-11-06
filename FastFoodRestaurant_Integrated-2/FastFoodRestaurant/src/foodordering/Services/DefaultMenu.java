package foodordering.Services;

import foodordering.Models.FoodItem;
import foodordering.Utilities.FileStorage;

/**
 * The built-in menu of Fast & FoodEUS (35 items).
 * It is loaded automatically the first time the program runs, so no file is needed.
 * After that the Manager can add, change and remove items as usual.
 */
public class DefaultMenu {

    // categories
    private static final String RICE_MEALS = "Array Rice Meals [case Sets]";
    private static final String SAVER_COMBOS = "short Saver Combos";
    private static final String ENTREES = "keyword Entrees";
    private static final String DRINKS = "data Drinks";
    private static final String SNACKS = "byte Snacks";
    private static final String ADD_ONS = "increment Add-Ons";

    // groups (items such as 17a, 17b... belong to a group)
    private static final String BURGERS = "block Burgers";
    private static final String BUNS = "long Buns";
    private static final String MILKSHAKES = "literal Milkshakes";
    private static final String JUICES = "float Juices";
    private static final String PIES = "file Pies";
    private static final String NO_GROUP = "";

    /**
     * Adds every built-in item whose code is not on the menu yet, then saves the menu.
     * Returns how many items were added.
     */
    public static int loadMissing() {
        int added = 0;

        // ----- Array Rice Meals [case Sets] -----
        added += add("01", "w/ Beef Patty", 50, 450, RICE_MEALS, NO_GROUP);
        added += add("02", "w/ Bacon & Egg", 60, 400, RICE_MEALS, NO_GROUP);
        added += add("03", "w/ Sausage & Egg", 60, 500, RICE_MEALS, NO_GROUP);
        added += add("04", "w/ Hotdog & Egg", 60, 450, RICE_MEALS, NO_GROUP);
        added += add("05", "w/ Fried Chicken", 70, 500, RICE_MEALS, NO_GROUP);
        added += add("06", "w/ Fried Shrimp (3 pcs.)", 70, 350, RICE_MEALS, NO_GROUP);

        // ----- short Saver Combos -----
        added += add("07", "block Burger + String Spaghetti", 85, 550, SAVER_COMBOS, NO_GROUP);
        added += add("08", "method Fried Chicken + double Cheese Sandwich", 75, 550, SAVER_COMBOS, NO_GROUP);
        added += add("09", "terminal Fried Shrimp + flowchart Carbonara", 100, 550, SAVER_COMBOS, NO_GROUP);
        added += add("10", "file Pie + nested Fries", 70, 550, SAVER_COMBOS, NO_GROUP);
        added += add("11", "long Bun + literal Milkshake", 75, 650, SAVER_COMBOS, NO_GROUP);

        // ----- keyword Entrees -----
        added += add("12", "String Spaghetti", 50, 300, ENTREES, NO_GROUP);
        added += add("13", "flowchart Carbonara", 70, 400, ENTREES, NO_GROUP);
        added += add("14", "method Fried Chicken", 45, 300, ENTREES, NO_GROUP);
        added += add("15", "terminal Fried Shrimp (3 pcs.)", 40, 150, ENTREES, NO_GROUP);
        added += add("16", "double Cheese Sandwich", 35, 250, ENTREES, NO_GROUP);
        added += add("17a", "Beef Burger", 40, 250, ENTREES, BURGERS);
        added += add("17b", "Chicken Burger", 40, 300, ENTREES, BURGERS);
        added += add("17c", "Cheese Burger", 40, 300, ENTREES, BURGERS);
        added += add("17d", "double Cheese Burger", 45, 400, ENTREES, BURGERS);
        added += add("17e", "BLT Burger", 50, 350, ENTREES, BURGERS);
        added += add("18a", "Hotdog Bun", 35, 250, ENTREES, BUNS);
        added += add("18b", "Sausage Bun", 35, 300, ENTREES, BUNS);

        // ----- data Drinks -----
        added += add("19a", "Strawberry Milkshake", 45, 400, DRINKS, MILKSHAKES);
        added += add("19b", "Vanilla Milkshake", 45, 400, DRINKS, MILKSHAKES);
        added += add("19c", "Chocolate Milkshake", 45, 420, DRINKS, MILKSHAKES);
        added += add("19d", "Cookies & Cream Milkshake", 45, 450, DRINKS, MILKSHAKES);
        added += add("20a", "Pineapple Juice", 30, 130, DRINKS, JUICES);
        added += add("20b", "Mango Juice", 30, 140, DRINKS, JUICES);
        added += add("20c", "Coconut Juice", 30, 120, DRINKS, JUICES);

        // ----- byte Snacks -----
        added += add("21", "nested Fries", 40, 300, SNACKS, NO_GROUP);
        added += add("22a", "Ube Pie", 35, 250, SNACKS, PIES);
        added += add("22b", "Coconut Pie", 35, 260, SNACKS, PIES);

        // ----- increment Add-Ons -----
        added += add("23", "int Rice", 10, 200, ADD_ONS, NO_GROUP);
        added += add("24", "char Fried Shrimp (1 pc)", 15, 50, ADD_ONS, NO_GROUP);

        if (added > 0) FileStorage.saveMenu();
        return added;
    }

    // code, name, price, kcal, category, group
    private static int add(String code, String name, double price, int kcal, String category, String group) {
        if (FileStorage.hasCode(code)) return 0;
        FileStorage.addMenuItem(new FoodItem(code, name, price, 0, kcal, category, group));
        return 1;
    }
}
