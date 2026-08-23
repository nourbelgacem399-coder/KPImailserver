import com.kpimailserver.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDatabase {

    public static void main(String[] args) {

        try {

            Connection connection =
                    DatabaseManager.getConnection();

            if (connection != null) {

                System.out.println(
                        "La base de données fonctionne !"
                );

                connection.close();

            } else {

                System.out.println(
                        "Connexion échouée."
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Erreur connexion : "
                            + e.getMessage()
            );
        }
    }
}
