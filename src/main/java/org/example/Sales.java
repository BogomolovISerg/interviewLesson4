package org.example;

public class Sales {
    private int id;
    private int id_screenings;
    private int amount;

    public Sales(int id, int id_screenings, int amount) {
        this.id = id;
        this.id_screenings = id_screenings;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public int getId_screenings() {
        return id_screenings;
    }

    public int getAmount() {
        return amount;
    }
}
