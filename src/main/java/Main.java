import com.kpimailserver.KPI;
import com.kpimailserver.CSVManager;
import com.kpimailserver.DatabaseManager;
import com.kpimailserver.EmailService;
import com.kpimailserver.LogAnalyzer;

import java.io.File;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        System.out.println("      KPI MAIL SERVER - VIP02");

        // ==========================================
        // DOSSIER DES LOGS
        // ==========================================

        File dossier = new File(
                "/home/nour/IdeaProjects/KPImailserver/logs"
        );

        // ==========================================
        // NOUVEAUX LOGS MX À ANALYSER
        // ==========================================

        String[] nomsFichiers = {
                "2026-08-15.log",
                "2026-08-16.log",
                "2026-08-17.log",
                "2026-08-18.log",
                "2026-08-19.log",
                "2026-08-20.log",
                "2026-08-21.log"
        };

        ArrayList<KPI> toutesLesDonnees =
                new ArrayList<>();

        LogAnalyzer analyzer =
                new LogAnalyzer();

        // ==========================================
        // ANALYSE DES FICHIERS LOG
        // ==========================================

        for (String nomFichier : nomsFichiers) {

            File fichier =
                    new File(dossier, nomFichier);

            if (!fichier.exists()) {

                System.out.println(
                        "ERREUR : fichier introuvable : "
                                + fichier.getAbsolutePath()
                );

                continue;
            }

            System.out.println(
                    "\nAnalyse du fichier : "
                            + fichier.getName()
            );

            ArrayList<KPI> resultats =
                    analyzer.analyze(
                            fichier.getAbsolutePath()
                    );

            toutesLesDonnees.addAll(
                    resultats
            );

            System.out.println(
                    "KPI trouvés : "
                            + resultats.size()
            );
        }

        // ==========================================
        // TOTAL KPI
        // ==========================================

        System.out.println(
                "\nTOTAL KPI : "
                        + toutesLesDonnees.size()
        );

        // ==========================================
        // AFFICHAGE DES KPI
        // ==========================================

        for (KPI kpi : toutesLesDonnees) {

            System.out.println(
                    kpi.getDate()
                            + " | "
                            + kpi.getHour()
                            + " | Relay : "
                            + kpi.getMailRelayed()
                            + " | Spam : "
                            + kpi.getSpam()
                            + " | Virus : "
                            + kpi.getVirus()
            );
        }

        // ==========================================
        // SAUVEGARDE MYSQL
        // ==========================================

        System.out.println(
                "\nSAUVEGARDE MYSQL"
        );

        for (KPI kpi : toutesLesDonnees) {

            DatabaseManager.saveKPI(kpi);
        }

        System.out.println(
                "Sauvegarde MySQL terminée."
        );

        // ==========================================
        // SAUVEGARDE CSV
        // ==========================================

        System.out.println(
                "\nSauvegarde CSV..."
        );

        CSVManager csv =
                new CSVManager();

        csv.save(
                toutesLesDonnees,
                "kpi_resultats.csv"
        );

        System.out.println(
                "CSV créé avec succès !"
        );

        // ==========================================
        // MONITORING VIP02
        // ==========================================

        System.out.println(
                "\nMONITORING VIP02"
        );

        Monitoring monitoring =
                new Monitoring();

        EmailService emailService =
                new EmailService();

        boolean alerteTrouvee =
                false;

        for (KPI kpi : toutesLesDonnees) {

            if (monitoring.hasAlert(kpi)) {

                alerteTrouvee = true;

                String report =
                        monitoring.generateReport(kpi);

                System.out.println(report);

                emailService.sendAlert(
                        report
                );
            }
        }

        if (!alerteTrouvee) {

            System.out.println(
                    "Aucune alerte détectée."
            );
        }

        System.out.println(
                "Monitoring terminé."
        );

        // ==========================================
        // DASHBOARD
        // ==========================================

        System.out.println(
                "\nLancement du Dashboard..."
        );

        javax.swing.SwingUtilities.invokeLater(() -> {

            Dashboard dashboard =
                    new Dashboard();

            dashboard.setVisible(true);
        });

        System.out.println(
                "Dashboard lancé."
        );
    }
}