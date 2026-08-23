package com.kpimailserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class DatabaseManager {

    private static final String DATABASE_URL =
            System.getenv("DATABASE_URL");

    private static final boolean IS_POSTGRES =
            DATABASE_URL != null && !DATABASE_URL.isBlank();

    private static final String MYSQL_URL =
            "jdbc:mysql://localhost:3306/kpi_mail";

    private static final String MYSQL_USER =
            "kpi_user";

    private static final String MYSQL_PASSWORD =
            "kpi123";

    public static Connection getConnection() {

        try {

            if (IS_POSTGRES) {

                java.net.URI dbUri =
                        new java.net.URI(DATABASE_URL.trim());

                String userInfo =
                        dbUri.getUserInfo();

                if (userInfo == null) {
                    throw new SQLException(
                            "DATABASE_URL sans user:password"
                    );
                }

                String[] userPass =
                        userInfo.split(":", 2);

                String username = userPass[0];
                String password = userPass[1];

                int port =
                        dbUri.getPort() == -1
                                ? 5432
                                : dbUri.getPort();

                String jdbcUrl =
                        "jdbc:postgresql://" +
                                dbUri.getHost() + ":" + port +
                                dbUri.getPath() +
                                "?sslmode=require";

                Properties properties =
                        new Properties();

                properties.setProperty(
                        "user",
                        username
                );

                properties.setProperty(
                        "password",
                        password
                );

                return DriverManager.getConnection(
                        jdbcUrl,
                        properties
                );
            }

            return DriverManager.getConnection(
                    MYSQL_URL,
                    MYSQL_USER,
                    MYSQL_PASSWORD
            );

        } catch (Exception e) {

            System.out.println(
                    "Erreur connexion DB : " +
                            e.getMessage()
            );

            return null;
        }
    }

    public static void saveKPI(KPI kpi) {

        String sql;

        if (IS_POSTGRES) {

            sql =
                    "INSERT INTO kpi_hourly " +
                            "(date_kpi, heure, mail_relayed, spam, virus) " +
                            "VALUES (?, ?, ?, ?, ?) " +
                            "ON CONFLICT (date_kpi, heure) " +
                            "DO UPDATE SET " +
                            "mail_relayed = EXCLUDED.mail_relayed, " +
                            "spam = EXCLUDED.spam, " +
                            "virus = EXCLUDED.virus";

        } else {

            sql =
                    "INSERT INTO kpi_hourly " +
                            "(date_kpi, heure, mail_relayed, spam, virus) " +
                            "VALUES (?, ?, ?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE " +
                            "mail_relayed = VALUES(mail_relayed), " +
                            "spam = VALUES(spam), " +
                            "virus = VALUES(virus)";
        }

        Connection connection =
                getConnection();

        if (connection == null) {
            return;
        }

        try (
                connection;
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    kpi.getDate()
            );

            int hour =
                    Integer.parseInt(
                            kpi.getHour().substring(0, 2)
                    );

            statement.setInt(
                    2,
                    hour
            );

            statement.setInt(
                    3,
                    kpi.getMailRelayed()
            );

            statement.setInt(
                    4,
                    kpi.getSpam()
            );

            statement.setInt(
                    5,
                    kpi.getVirus()
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            System.out.println(
                    "Erreur save KPI : " +
                            e.getMessage()
            );
        }
    }

    public static ArrayList<KPI> getKPIBetweenHours(
            String date,
            int startHour,
            int endHour) {

        ArrayList<KPI> result =
                new ArrayList<>();

        String sql =
                "SELECT date_kpi, heure, " +
                        "mail_relayed, spam, virus " +
                        "FROM kpi_hourly " +
                        "WHERE date_kpi = ? " +
                        "AND heure BETWEEN ? AND ? " +
                        "ORDER BY heure";

        Connection connection =
                getConnection();

        if (connection == null) {
            return result;
        }

        try (
                connection;
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setDate(
                    1,
                    java.sql.Date.valueOf(date)
            );

            statement.setInt(
                    2,
                    startHour
            );

            statement.setInt(
                    3,
                    endHour
            );

            ResultSet rs =
                    statement.executeQuery();

            while (rs.next()) {

                String hour =
                        String.format(
                                "%02d:00",
                                rs.getInt("heure")
                        );

                result.add(
                        new KPI(
                                rs.getString("date_kpi"),
                                hour,
                                rs.getInt("mail_relayed"),
                                rs.getInt("spam"),
                                rs.getInt("virus")
                        )
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Erreur lecture heures : " +
                            e.getMessage()
            );
        }

        return result;
    }

    public static ArrayList<KPI> getKPIByDate(
            String date) {

        return getKPIBetweenHours(
                date,
                0,
                23
        );
    }

    public static ArrayList<KPI> getKPIBetweenDates(
            String startDate,
            String endDate) {

        ArrayList<KPI> result =
                new ArrayList<>();

        String sql =
                "SELECT date_kpi, heure, " +
                        "mail_relayed, spam, virus " +
                        "FROM kpi_hourly " +
                        "WHERE date_kpi BETWEEN ? AND ? " +
                        "ORDER BY date_kpi, heure";

        Connection connection =
                getConnection();

        if (connection == null) {
            return result;
        }

        try (
                connection;
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setDate(1, java.sql.Date.valueOf(startDate));
            statement.setDate(2, java.sql.Date.valueOf(endDate));

            ResultSet rs =
                    statement.executeQuery();

            while (rs.next()) {

                String hour =
                        String.format(
                                "%02d:00",
                                rs.getInt("heure")
                        );

                result.add(
                        new KPI(
                                rs.getString("date_kpi"),
                                hour,
                                rs.getInt("mail_relayed"),
                                rs.getInt("spam"),
                                rs.getInt("virus")
                        )
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Erreur lecture dates : " +
                            e.getMessage()
            );
        }

        return result;
    }
}