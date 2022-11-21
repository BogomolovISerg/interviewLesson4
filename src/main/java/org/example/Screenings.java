package org.example;

import java.math.BigDecimal;
import java.util.Date;

public class Screenings {
    private int id;
    private int id_movies;
    private Date screening_times;
    private int price;

    public Screenings(int id, int id_movies, Date screening_times, int price) {
        this.id = id;
        this.id_movies = id_movies;
        this.screening_times = screening_times;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public int getId_movies() {
        return id_movies;
    }

    public Date getScreening_times() {
        return screening_times;
    }

    public int getPrice() {
        return price;
    }
}
