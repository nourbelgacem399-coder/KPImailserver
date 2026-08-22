import com.kpimailserver.KPI;
public class Monitoring {

    public boolean hasAlert(KPI kpi) {

        if (kpi.getMailRelayed() > 1000) {
            return true;
        }

        if (kpi.getSpam() > 500) {
            return true;
        }

        if (kpi.getVirus() > 100) {
            return true;
        }

        return false;
    }

    public String generateReport(KPI kpi) {

        String report = "";

        report += "MONITORING SERVEUR VIP02\n";
        report += "\n";

        report += "Date : " + kpi.getDate() + "\n";
        report += "Heure : " + kpi.getHour() + "\n";

        report += "Mail relayés : " + kpi.getMailRelayed() + "\n";
        report += "Spam : " + kpi.getSpam() + "\n";
        report += "Virus : " + kpi.getVirus() + "\n\n";

        if (hasAlert(kpi)) {
            report += "ALERTE : seuil dépassé !\n";
        } else {
            report += " Etat normal.\n";
        }

        return report;
    }
}