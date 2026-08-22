import com.kpimailserver.DatabaseManager;

import java.sql.Connection;

public class TestDatabase {

    public static void main(String[] args) {

        Connection connection =
                DatabaseManager.getConnection();

        if (connection != null) {

            System.out.println(
                    "La base de données fonctionne !"
            );

        } else {

            System.out.println(
                    "Connexion échouée."
            );
        }
    }
}