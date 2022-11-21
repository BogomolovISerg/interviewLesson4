package org.example;

import java.sql.*;
import java.text.SimpleDateFormat;

public class DbHandler {

    private static final String CON_STR = "jdbc:sqlite:lesson4.s3db";

    private static DbHandler instance = null;

    public static synchronized DbHandler getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null)
            instance = new DbHandler();
        return instance;
    }

    private Connection connection;

    private DbHandler() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        this.connection = DriverManager.getConnection(CON_STR);
        createDB();
    }

    public void createDB() throws ClassNotFoundException, SQLException
    {
        Statement statmt = this.connection.createStatement();
        statmt.execute("CREATE TABLE if not exists 'movies' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "'nameMovies' text, 'duration' INT);");
        statmt.execute("CREATE TABLE if not exists 'sales' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'id_screenings' INT, 'amount' INT, FOREIGN KEY (id_screenings)  REFERENCES screenings (id));");
        statmt.execute("CREATE TABLE if not exists 'screenings' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'id_movies' INT, 'screening_times' TEXT, 'price' INT, FOREIGN KEY (id_movies)  REFERENCES movies (id));");

        System.out.println("Таблицы для практического задания урока 4 созданы или уже существуют.");
    }

    public void addMovies(Movies movies) {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "INSERT INTO movies (id, nameMovies, duration) VALUES(?, ?, ?)")) {
            statement.setObject(1, movies.getId());
            statement.setObject(2, movies.getNameMovies());
            statement.setObject(3, movies.getDuration());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addScreenings(Screenings screenings) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try (PreparedStatement statement = this.connection.prepareStatement(
                "INSERT INTO screenings (id, id_movies, screening_times, price) VALUES(?, ?, ?, ?)")) {
            statement.setObject(1, screenings.getId());
            statement.setObject(2, screenings.getId_movies());
            statement.setObject(3, sdf.format(screenings.getScreening_times()));
            statement.setObject(4, screenings.getPrice());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addSales(Sales sales) {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "INSERT INTO sales (id, id_screenings, amount) VALUES(?, ?, ?)")) {
            statement.setObject(1, sales.getId());
            statement.setObject(2, sales.getId_screenings());
            statement.setObject(3, sales.getAmount());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Проверка дублей
    public void Duplicates(){
        try (Statement statement = this.connection.createStatement()) {

            String qwery = "if OBJECT_ID(N'TempDB..#Interval') IS NOT NULL DROP TABLE #Interval " +
                    "CREATE TABLE #Interval " +
                    "(Id bigINT," +
                    "data1 NVARCHAR(30)," +
                    "data2 NVARCHAR(30)," +
                    ",id_movies bigint) " +
                    "INSERT INTO #Interval " +
                    "SELECT  v1.id, CAST(v1.screening_times as datetime),DATEADD(MI,v2.duration,CAST(v1.screening_times as datetime)),v1.id_movies FROM screenings v1 " +
                    "left join movies v2 on v2.id=v1.id_movies " +
                    "select v1.*,v2.*,v3.* " +
                    "FROM #Interval v1, movies v2 " +
                    "left join screenings v3 on v3.id_movies=v2.id " +
                    "where v1.id_movies <> v2.id " +
                    "and ((CAST(v3.screening_times as datetime) >= v1.data1 AND CAST(v3.screening_times as datetime) <= v1.data2) " +
                    "or (DATEADD(MI,v2.duration,CAST(v3.screening_times as datetime)) >= v1.data1 AND DATEADD(MI,v2.duration,CAST(v3.screening_times as datetime)) <= v1.data2))";
            ResultSet resultSet = statement.executeQuery(qwery);
            while (resultSet.next()) {
                System.out.println(resultSet.getDate("data1") + ", " +
                        resultSet.getInt("duration") + ", " + resultSet.getString("nameMovies"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Продажи
    public void Sales(){
        try (Statement statement = this.connection.createStatement()) {
            String qwery = "select SUM(v2.price * v1.amount) pr, sum(v1.amount) v, v3.nameMovies film, avg(v1.amount) av " +
                    "from sales v1 " +
                    "left join screenings v2 on v2.id=v1.id_screenings " +
                    "left join movies v3 on v3.id=v2.id_movies " +
                    "group by v3.nameMovies " +
                    "order by SUM(v2.price * v1.amount) desc";
            ResultSet resultSet = statement.executeQuery(qwery);
            while (resultSet.next()) {
                System.out.println(resultSet.getString("film") + ": общее число посетителей " +
                        resultSet.getInt("v") + ", среднее число зрителей " + resultSet.getInt("av") +
                        ", общая сумма сборов " + resultSet.getInt("pr"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Посетители по датам
    public void Visitors(){
        try (Statement statement = this.connection.createStatement()) {
            String qwery = "if OBJECT_ID(N'TempDB..#Interval') IS NOT NULL " +
                    "DROP TABLE #Interval " +
                    "CREATE TABLE #Interval " +
                    "(k INT," +
                    "NVARCHAR(30)) " +
                    "INSERT INTO #Interval " +
                    "select vo.amount k, 'с 9 до 15' " +
                    "from sales vo " +
                    "left join screenings v1 on vo.id_screenings=v1.id " +
                    "where CAST(v1.screening_times as datetime) >= CAST(left(v1.screening_times,10)+' 09:00' as datetime) " +
                    "AND CAST(v1.screening_times as datetime) < CAST(left(v1.screening_times,10)+' 15:00' as datetime) " +
                    "UNION " +
                    "select vo.amount k,'с 15 до 18' " +
                    "from sales vo " +
                    "left join screenings v1 on vo.id_screenings=v1.id " +
                    "where CAST(v1.screening_times as datetime) >= CAST(left(v1.screening_times,10)+' 15:00' as datetime) " +
                    "AND CAST(v1.screening_times as datetime) < CAST(left(v1.screening_times,10)+' 18:00' as datetime) " +
                    "UNION " +
                    "select vo.amount k,'с 18 до 21' " +
                    "from sales vo " +
                    "left join screenings v1 on vo.id_screenings=v1.id " +
                    "where CAST(v1.screening_times as datetime) >= CAST(left(v1.screening_times,10)+' 18:00' as datetime) " +
                    "AND CAST(v1.screening_times as datetime) < CAST(left(v1.screening_times,10)+' 21:00' as datetime) " +
                    "UNION " +
                    "select vo.amount k,'с 21 до 00' " +
                    "from sales vo " +
                    "left join screenings v1 on vo.id_screenings=v1.id " +
                    "where CAST(v1.screening_times as datetime) >= CAST(left(v1.screening_times,10)+' 21:00' as datetime) " +
                    "AND CAST(v1.screening_times as datetime) < CAST(left(v1.screening_times,10)+' 23:59' as datetime) " +
                    "select t, sum(k) k " +
                    "from #Interval " +
                    "Group by t";
            ResultSet resultSet = statement.executeQuery(qwery);
            while (resultSet.next()) {
                System.out.println("Период : " + resultSet.getString("t") + ", количество поситителей: " +
                        resultSet.getInt("k"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
