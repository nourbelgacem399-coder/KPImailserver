import com.kpimailserver.KPI;
import com.kpimailserver.DatabaseManager;

public class TestSaveKPI {

    public static void main(String[] args) {

        KPI kpi = new KPI(
                "2026-08-13",
                "13:00",
                5000,
                250,
                3
        );

        DatabaseManager.saveKPI(kpi);

        System.out.println(
                "Test terminé."
        );
    }
}