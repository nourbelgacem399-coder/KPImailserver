import com.kpimailserver.KPI;
import com.kpimailserver.DatabaseManager;

import java.util.ArrayList;

public class TestReadKPI {

    public static void main(String[] args) {

        String date = "2026-07-29";

        int startHour = 13;

        int endHour = 17;

        ArrayList<KPI> data =
                DatabaseManager.getKPIBetweenHours(
                        date,
                        startHour,
                        endHour
                );

        System.out.println(
                "================================"
        );

        System.out.println(
                "Nombre de lignes : "
                        + data.size()
        );

        System.out.println(
                "================================"
        );

        for (KPI kpi : data) {

            System.out.println(
                    kpi.getDate()
                            + " | "
                            + kpi.getHour()
                            + " | Relayed : "
                            + kpi.getMailRelayed()
                            + " | Spam : "
                            + kpi.getSpam()
                            + " | Virus : "
                            + kpi.getVirus()
            );
        }
    }
}