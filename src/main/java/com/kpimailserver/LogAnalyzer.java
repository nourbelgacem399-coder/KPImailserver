package com.kpimailserver;

import java.io.*;
import java.util.*;

public class LogAnalyzer {

    public ArrayList<KPI> analyze(String filePath) {

        ArrayList<KPI> results = new ArrayList<>();

        Map<String, int[]> hourlyData = new LinkedHashMap<>();

        File file = new File(filePath);

        String date = file.getName()
                .replace(".log", "")
                .replace("(1)", "");

        try (BufferedReader br =
                     new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = br.readLine()) != null) {

                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\s+");

                if (parts.length == 0) {
                    continue;
                }

                String time = parts[0];

                if (!time.matches("\\d{2}:\\d{2}:\\d{2}.*")) {
                    continue;
                }

                String hour = time.substring(0, 2) + ":00";

                hourlyData.putIfAbsent(
                        hour,
                        new int[]{0, 0, 0}
                );

                int[] values = hourlyData.get(hour);

                // ==========================================
                // MAIL RELAYED / ENVOI MX
                // Règle :
                // grep "got:250" fichier.log | wc -l
                // ==========================================

                if (line.contains("got:250")) {
                    values[0]++;
                }

                // ==========================================
                // SPAM
                // Règle :
                // grep "KAS_STATUS_SPAM" fichier.log
                // | grep -v "completed" | wc -l
                // ==========================================

                if (line.contains("KAS_STATUS_SPAM")
                        && !line.contains("completed")) {
                    values[1]++;
                }

                // ==========================================
                // VIRUS
                // Règle :
                // grep "eX-KAV-Status: DETECT" fichier.log
                // | wc -l
                // ==========================================

                if (line.contains("eX-KAV-Status: DETECT")) {
                    values[2]++;
                }
            }

        } catch (IOException e) {

            System.out.println(
                    "Erreur lecture : " + filePath
            );

            e.printStackTrace();
        }

        // ==========================================
        // CRÉATION DES KPI PAR HEURE
        // ==========================================

        for (Map.Entry<String, int[]> entry :
                hourlyData.entrySet()) {

            String hour = entry.getKey();

            int[] values = entry.getValue();

            KPI kpi = new KPI(
                    date,
                    hour,
                    values[0],
                    values[1],
                    values[2]
            );

            results.add(kpi);
        }

        return results;
    }
}