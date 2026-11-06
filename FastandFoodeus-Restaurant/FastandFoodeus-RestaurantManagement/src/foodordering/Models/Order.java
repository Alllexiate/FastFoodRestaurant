package foodordering.Models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Order {
    public enum Status {
        PENDING("Preparing in Kitchen"),
        DELAYED("Delayed (Kitchen Load)"),
        READY_TO_SERVE("Ready to Serve!"),
        CANCELLED("Cancelled");

        private final String label;

        Status(String label) { this.label = label; }

        public String getLabel() { return label; }
    }

    /** One item in an order. The price is locked in when the order is placed. */
    public static class Line {
        public final String name;
        public final int qty;
        public final double unitPrice;   // price actually charged (after discount)
        public final double listPrice;   // price before discount

        public Line(String name, int qty, double unitPrice) {
            this(name, qty, unitPrice, unitPrice);
        }

        public Line(String name, int qty, double unitPrice, double listPrice) {
            this.name = name;
            this.qty = qty;
            this.unitPrice = unitPrice;
            this.listPrice = listPrice;
        }

        public double subtotal() { return qty * unitPrice; }
        public double listSubtotal() { return qty * listPrice; }
    }

    private final int id;
    private final String customer;
    private Status status;
    private final List<Line> lines = new ArrayList<>();
    private double cashTendered;     // 0 = no payment recorded
    private String dateTime = "";

    /** A brand-new order (pending, stamped with the current date and time). */
    public Order(int id, String customer) {
        this(id, customer, Status.PENDING);
        this.dateTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("MMM dd, yyyy - hh:mm a", Locale.ENGLISH));
    }

    public Order(int id, String customer, Status status) {
        this.id = id;
        this.customer = customer;
        this.status = status;
    }

    public int getId() { return id; }
    public String getCustomer() { return customer; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public List<Line> getLines() { return lines; }
    public void addLine(Line line) { lines.add(line); }
    public double getCashTendered() { return cashTendered; }
    public void setCashTendered(double cashTendered) { this.cashTendered = cashTendered; }
    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }

    public double getTotal() {
        double total = 0;
        for (Line l : lines) total += l.subtotal();
        return total;
    }

    /** Total before any discounts. */
    public double getListTotal() {
        double total = 0;
        for (Line l : lines) total += l.listSubtotal();
        return total;
    }

    /** The 12% VAT that is already included in the total price. */
    public double getVat() {
        return getTotal() / 1.12 * 0.12;
    }

    public void display() {
        System.out.println("Order #" + id + " | Customer: " + customer + " | Status: " + status
                + (dateTime.isEmpty() ? "" : " | " + dateTime));
        for (Line l : lines) {
            System.out.printf("   %-34s x%-3d @ %8.2f = %9.2f%n", l.name, l.qty, l.unitPrice, l.subtotal());
        }
        System.out.printf("   TOTAL: %.2f%n", getTotal());
    }

    // Stored in orders.txt as:
    // id|customer|STATUS|name:qty:price:listPrice;name:qty:price:listPrice|cash|dateTime
    public String toLine() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            Line l = lines.get(i);
            if (i > 0) sb.append(";");
            sb.append(l.name).append(":").append(l.qty).append(":")
              .append(l.unitPrice).append(":").append(l.listPrice);
        }
        return id + "|" + customer + "|" + status + "|" + sb + "|" + cashTendered + "|" + dateTime;
    }

    public static Order fromLine(String line) {
        String[] p = line.split("\\|", -1);
        Order o = new Order(Integer.parseInt(p[0]), p[1], Status.valueOf(p[2]));
        for (String part : p[3].split(";")) {
            if (part.trim().isEmpty()) continue;
            String[] f = part.split(":");
            double unit = Double.parseDouble(f[2]);
            double list = f.length > 3 ? Double.parseDouble(f[3]) : unit;
            o.addLine(new Line(f[0], Integer.parseInt(f[1]), unit, list));
        }
        if (p.length > 4 && !p[4].trim().isEmpty()) o.cashTendered = Double.parseDouble(p[4]);
        if (p.length > 5) o.dateTime = p[5];
        return o;
    }
}
