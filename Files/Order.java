package foodordering;

import java.util.ArrayList;
import java.util.List;

public class Order {
    public enum Status { PENDING, READY_TO_SERVE }

    /** One item in an order. The price is locked in when the order is placed. */
    public static class Line {
        public final String name;
        public final int qty;
        public final double unitPrice;

        public Line(String name, int qty, double unitPrice) {
            this.name = name;
            this.qty = qty;
            this.unitPrice = unitPrice;
        }

        public double subtotal() { return qty * unitPrice; }
    }

    private final int id;
    private final String customer;
    private Status status;
    private final List<Line> lines = new ArrayList<>();

    public Order(int id, String customer) {
        this(id, customer, Status.PENDING);
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

    public double getTotal() {
        double total = 0;
        for (Line l : lines) total += l.subtotal();
        return total;
    }

    public void display() {
        System.out.println("Order #" + id + " | Customer: " + customer + " | Status: " + status);
        for (Line l : lines) {
            System.out.printf("   %-20s x%-3d @ %8.2f = %9.2f%n", l.name, l.qty, l.unitPrice, l.subtotal());
        }
        System.out.printf("   TOTAL: %.2f%n", getTotal());
    }

    // Stored in orders.txt as: id|customer|STATUS|name:qty:price;name:qty:price
    public String toLine() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            Line l = lines.get(i);
            if (i > 0) sb.append(";");
            sb.append(l.name).append(":").append(l.qty).append(":").append(l.unitPrice);
        }
        return id + "|" + customer + "|" + status + "|" + sb;
    }

    public static Order fromLine(String line) {
        String[] p = line.split("\\|", -1);
        Order o = new Order(Integer.parseInt(p[0]), p[1], Status.valueOf(p[2]));
        for (String part : p[3].split(";")) {
            if (part.trim().isEmpty()) continue;
            String[] f = part.split(":");
            o.addLine(new Line(f[0], Integer.parseInt(f[1]), Double.parseDouble(f[2])));
        }
        return o;
    }
}
