package org.example;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Main {
    public static void main(String[] args){
        try {
            DbHandler dbHandler = DbHandler.getInstance();

            dbHandler.addMovies(new Movies(0,"Зеленая миля",189));
            dbHandler.addMovies(new Movies(1,"Список Шиндлера",195));
            dbHandler.addMovies(new Movies(2,"Побег из Шоушенка",142));
            dbHandler.addMovies(new Movies(3,"Форест Гамп",142));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dbHandler.addScreenings(new Screenings(0,0,sdf.parse("2022-11-19 06:00:00"),500));
            dbHandler.addScreenings(new Screenings(1,1,sdf.parse("2022-11-19 08:00:00"),420));
            dbHandler.addScreenings(new Screenings(2,2,sdf.parse("2022-11-19 11:30:00"),380));
            dbHandler.addScreenings(new Screenings(3,3,sdf.parse("2022-11-19 14:30:00"),600));
            dbHandler.addScreenings(new Screenings(4,2,sdf.parse("2022-11-19 17:00:00"),620));
            dbHandler.addScreenings(new Screenings(5,3,sdf.parse("2022-11-19 20:00:00"),720));
            dbHandler.addScreenings(new Screenings(6,1,sdf.parse("2022-11-19 23:00:00"),800));

            dbHandler.addSales(new Sales(0,0,2));
            dbHandler.addSales(new Sales(1,0,3));
            dbHandler.addSales(new Sales(2,1,5));
            dbHandler.addSales(new Sales(3,2,1));
            dbHandler.addSales(new Sales(4,2,1));
            dbHandler.addSales(new Sales(5,3,2));
            dbHandler.addSales(new Sales(6,4,1));
            dbHandler.addSales(new Sales(7,4,1));
            dbHandler.addSales(new Sales(8,4,1));
            dbHandler.addSales(new Sales(9,5,3));
            dbHandler.addSales(new Sales(10,6,4));

            dbHandler.Duplicates();
            dbHandler.Sales();
            dbHandler.Visitors();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}