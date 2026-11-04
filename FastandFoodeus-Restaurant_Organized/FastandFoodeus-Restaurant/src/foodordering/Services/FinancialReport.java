package foodordering.Services;

import foodordering.Models.*;
import foodordering.Utilities.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class FinancialReport {

    public static void showReport() {
        int completed = 0;
        int pending = 0;
        int cancelled = 0;
        double revenue = 0;
        double pendingValue = 0;

        Map<String, double[]> perItem = new LinkedHashMap<>(); // name -> {qty, revenue}

        for (Order o : FileStorage.orders) {
            if (o.getStatus() == Order.Status.READY_TO_SERVE) {
                completed++;
                revenue += o.getTotal();
                for (Order.Line l : o.getLines()) {
                    double[] totals = perItem.get(l.name);
                    if (totals == null) {
                        totals = new double[2];
                        perItem.put(l.name, totals);
                    }
                    totals[0] += l.qty;
                    totals[1] += l.subtotal();
                }
            } else if (o.getStatus() == Order.Status.CANCELLED) {
                cancelled++;
            } else {                       // PENDING or DELAYED
                pending++;
                pendingValue += o.getTotal();
            }
        }

        System.out.println("\n===== FINANCIAL REPORT =====");
        System.out.println("Total orders        : " + FileStorage.orders.size());
        System.out.println("Completed orders    : " + completed);
        System.out.println("Pending orders      : " + pending);
        System.out.println("Cancelled orders    : " + cancelled);
        System.out.printf("Revenue (completed) : %.2f%n", revenue);
        System.out.printf("Pending value       : %.2f%n", pendingValue);

        if (!perItem.isEmpty()) {
            System.out.println("\nSales per item (completed orders):");
            System.out.printf("%-22s %8s %12s%n", "Item", "Qty", "Revenue");
            System.out.println(Input.line(44));
            for (Map.Entry<String, double[]> e : perItem.entrySet()) {
                System.out.printf("%-22s %8d %12.2f%n", e.getKey(), (int) e.getValue()[0], e.getValue()[1]);
            }
        }
    }
}
