import com.kpimailserver.KPI;
import com.kpimailserver.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

public class FilterKPI extends JFrame {

    private JTextField dateField;
    private JSpinner startHour;
    private JSpinner endHour;
    private JComboBox<String> filterType;


    public FilterKPI() {

        setTitle("Filtre KPI - Mail Server");

        setSize(550, 350);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );


        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );


        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        10,
                        10,
                        10,
                        10
                );


        // =========================================
        // TYPE D'AFFICHAGE
        // =========================================

        JLabel typeLabel =
                new JLabel(
                        "Affichage par :"
                );


        filterType =
                new JComboBox<>(
                        new String[]{
                                "Par heure",
                                "Par jour",
                                "Par semaine",
                                "Par mois"
                        }
                );


        gbc.gridx = 0;
        gbc.gridy = 0;

        panel.add(
                typeLabel,
                gbc
        );


        gbc.gridx = 1;

        panel.add(
                filterType,
                gbc
        );


        // =========================================
        // DATE
        // =========================================

        JLabel dateLabel =
                new JLabel(
                        "Date :"
                );


        dateField =
                new JTextField(
                        "2026-08-02",
                        10
                );


        gbc.gridx = 0;
        gbc.gridy = 1;

        panel.add(
                dateLabel,
                gbc
        );


        gbc.gridx = 1;

        panel.add(
                dateField,
                gbc
        );


        // =========================================
        // HEURE DEBUT
        // =========================================

        JLabel startLabel =
                new JLabel(
                        "De :"
                );


        startHour =
                new JSpinner(
                        new SpinnerNumberModel(
                                0,
                                0,
                                23,
                                1
                        )
                );


        gbc.gridx = 0;
        gbc.gridy = 2;

        panel.add(
                startLabel,
                gbc
        );


        gbc.gridx = 1;

        panel.add(
                startHour,
                gbc
        );


        // =========================================
        // HEURE FIN
        // =========================================

        JLabel endLabel =
                new JLabel(
                        "À :"
                );


        endHour =
                new JSpinner(
                        new SpinnerNumberModel(
                                23,
                                0,
                                23,
                                1
                        )
                );


        gbc.gridx = 0;
        gbc.gridy = 3;

        panel.add(
                endLabel,
                gbc
        );


        gbc.gridx = 1;

        panel.add(
                endHour,
                gbc
        );


        // =========================================
        // BOUTON
        // =========================================

        JButton button =
                new JButton(
                        "Afficher le graphique"
                );


        gbc.gridx = 0;
        gbc.gridy = 4;

        gbc.gridwidth = 2;


        panel.add(
                button,
                gbc
        );


        // =========================================
        // ACTIVER / DESACTIVER LES HEURES
        // =========================================

        filterType.addActionListener(e -> {

            String type =
                    (String)
                            filterType
                                    .getSelectedItem();


            boolean parHeure =
                    "Par heure".equals(type);


            startHour.setEnabled(
                    parHeure
            );

            endHour.setEnabled(
                    parHeure
            );
        });


        // =========================================
        // ACTION DU BOUTON
        // =========================================

        button.addActionListener(e -> {

            try {

                String type =
                        (String)
                                filterType
                                        .getSelectedItem();


                String dateText =
                        dateField
                                .getText()
                                .trim();


                if (dateText.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Veuillez entrer une date."
                    );

                    return;
                }


                // Vérification du format
                LocalDate date =
                        LocalDate.parse(
                                dateText
                        );


                // =====================================
                // PAR HEURE
                // =====================================

                if ("Par heure".equals(type)) {

                    int start =
                            (Integer)
                                    startHour
                                            .getValue();


                    int end =
                            (Integer)
                                    endHour
                                            .getValue();


                    if (start > end) {

                        JOptionPane.showMessageDialog(
                                this,
                                "L'heure de début doit être avant l'heure de fin."
                        );

                        return;
                    }


                    GraphKPI graph =
                            new GraphKPI(
                                    dateText,
                                    start,
                                    end
                            );


                    showGraphWindow(
                            graph,
                            "KPI - "
                                    + dateText
                                    + " - "
                                    + start
                                    + "h à "
                                    + end
                                    + "h"
                    );


                    return;
                }


                // =====================================
                // PAR JOUR
                // =====================================

                if ("Par jour".equals(type)) {

                    ArrayList<KPI> data =
                            DatabaseManager
                                    .getKPIByDate(
                                            dateText
                                    );


                    if (data.isEmpty()) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Aucune donnée pour cette date."
                        );

                        return;
                    }


                    GraphPeriodique graph =
                            new GraphPeriodique(
                                    data,
                                    "KPI du "
                                            + dateText,
                                    "Heure"
                            );


                    showGraphWindow(
                            graph,
                            "KPI - "
                                    + dateText
                    );


                    return;
                }


                // =====================================
                // PAR SEMAINE
                // =====================================

                if ("Par semaine".equals(type)) {

                    LocalDate monday =
                            date.with(
                                    java.time.temporal
                                            .TemporalAdjusters
                                            .previousOrSame(
                                                    DayOfWeek.MONDAY
                                            )
                            );


                    LocalDate sunday =
                            monday.plusDays(6);


                    ArrayList<KPI> rawData =
                            DatabaseManager
                                    .getKPIBetweenDates(
                                            monday.toString(),
                                            sunday.toString()
                                    );


                    ArrayList<KPI> weeklyData =
                            aggregateByDate(
                                    rawData
                            );


                    if (weeklyData.isEmpty()) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Aucune donnée pour cette semaine."
                        );

                        return;
                    }


                    GraphPeriodique graph =
                            new GraphPeriodique(
                                    weeklyData,
                                    "KPI - Semaine du "
                                            + monday
                                            + " au "
                                            + sunday,
                                    "Jour"
                            );


                    showGraphWindow(
                            graph,
                            "KPI - Semaine"
                    );


                    return;
                }


                // =====================================
                // PAR MOIS
                // =====================================

                if ("Par mois".equals(type)) {

                    LocalDate firstDay =
                            date.withDayOfMonth(1);


                    LocalDate lastDay =
                            date.withDayOfMonth(
                                    date.lengthOfMonth()
                            );


                    ArrayList<KPI> rawData =
                            DatabaseManager
                                    .getKPIBetweenDates(
                                            firstDay.toString(),
                                            lastDay.toString()
                                    );


                    ArrayList<KPI> monthlyData =
                            aggregateByDate(
                                    rawData
                            );


                    if (monthlyData.isEmpty()) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Aucune donnée pour ce mois."
                        );

                        return;
                    }


                    GraphPeriodique graph =
                            new GraphPeriodique(
                                    monthlyData,
                                    "KPI - Mois "
                                            + date.getMonthValue()
                                            + "/"
                                            + date.getYear(),
                                    "Jour"
                            );


                    showGraphWindow(
                            graph,
                            "KPI - Mois"
                    );
                }


            } catch (
                    java.time.format.DateTimeParseException ex
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Date invalide. Format : YYYY-MM-DD"
                );
            }
        });


        add(panel);
    }


    // =============================================
    // AGREGATION PAR JOUR
    // =============================================

    private ArrayList<KPI> aggregateByDate(
            ArrayList<KPI> data
    ) {

        ArrayList<KPI> result =
                new ArrayList<>();


        if (data == null ||
                data.isEmpty()) {

            return result;
        }


        String currentDate = null;

        int relay = 0;
        int spam = 0;
        int virus = 0;


        for (KPI kpi : data) {

            String date =
                    kpi.getDate();


            if (currentDate == null) {

                currentDate =
                        date;
            }


            if (!currentDate.equals(date)) {

                result.add(
                        new KPI(
                                currentDate,
                                "00:00",
                                relay,
                                spam,
                                virus
                        )
                );


                currentDate =
                        date;

                relay = 0;
                spam = 0;
                virus = 0;
            }


            relay +=
                    Math.max(
                            0,
                            kpi.getMailRelayed()
                    );


            spam +=
                    Math.max(
                            0,
                            kpi.getSpam()
                    );


            virus +=
                    Math.max(
                            0,
                            kpi.getVirus()
                    );
        }


        // Dernier jour

        if (currentDate != null) {

            result.add(
                    new KPI(
                            currentDate,
                            "00:00",
                            relay,
                            spam,
                            virus
                    )
            );
        }


        return result;
    }


    // =============================================
    // OUVRIR LE GRAPH DANS UNE FENETRE
    // =============================================

    private void showGraphWindow(
            JPanel graph,
            String title
    ) {

        JFrame frame =
                new JFrame(title);


        frame.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );


        frame.setSize(
                1100,
                700
        );


        frame.setLocationRelativeTo(
                this
        );


        frame.add(
                graph,
                BorderLayout.CENTER
        );


        frame.setVisible(true);
    }


    // =========================================
    // MAIN
    // =========================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(() -> {

            FilterKPI window =
                    new FilterKPI();

            window.setVisible(true);
        });
    }
}