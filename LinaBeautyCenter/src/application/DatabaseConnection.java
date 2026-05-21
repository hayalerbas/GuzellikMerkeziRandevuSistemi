package application;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public static Connection baglan() {

        try {

            Connection conn =
                    DriverManager.getConnection(

                            "jdbc:mysql://localhost:3306/lina_beauty_center",

                            "root",

                            ""
                    );

            return conn;

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }
}