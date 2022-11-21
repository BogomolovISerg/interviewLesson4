package org.example;

public class Movies {
    private int id;
    private String nameMovies;
    private int duration; // Длительность в минутах

    public Movies(int id, String nameMovies, int duration){
        this.id = id;
        this.nameMovies = nameMovies;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public String getNameMovies() {
        return nameMovies;
    }

    public int getDuration() {
        return duration;
    }
}
