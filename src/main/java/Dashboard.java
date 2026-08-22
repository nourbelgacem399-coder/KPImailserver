
import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {

    private JPanel chartContainer;

    private JTextField dateField;
    private JTextField startField;
    private JTextField endField;

    public Dashboard() {

        setTitle("KPI Mail Dashboard");

        setSize(1200, 800);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );


        JPanel topPanel =
                new JPanel(
                        new FlowLayout()
                );


        JLabel title =
                new JLabel(
                        "KPI Mail Server"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        topPanel.add(title);



        topPanel.add(
                new JLabel("Date :")
        );

        dateField =
                new JTextField(
                        "2026-07-29",
                        10
                );

        topPanel.add(
                dateField
        );



        topPanel.add(
                new JLabel("De :")
        );

        startField =
                new JTextField(
                        "2",
                        3
                );

        topPanel.add(
                startField
        );




        topPanel.add(
                new JLabel("A :")
        );

        endField =
                new JTextField(
                        "12",
                        3
                );

        topPanel.add(
                endField
        );



        JButton afficher =
                new JButton(
                        "Afficher"
                );

        topPanel.add(
                afficher
        );


        add(
                topPanel,
                BorderLayout.NORTH
        );



        chartContainer =
                new JPanel(
                        new BorderLayout()
                );

        add(
                chartContainer,
                BorderLayout.CENTER
        );



        afficher.addActionListener(e -> {

            try {

                String date =
                        dateField
                                .getText()
                                .trim();


                int startHour =
                        Integer.parseInt(
                                startField
                                        .getText()
                                        .trim()
                        );


                int endHour =
                        Integer.parseInt(
                                endField
                                        .getText()
                                        .trim()
                        );



                if (startHour < 0 ||
                        startHour > 23 ||
                        endHour < 0 ||
                        endHour > 23 ||
                        startHour > endHour) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Intervalle horaire invalide !"
                    );

                    return;
                }




                GraphKPI graph =
                        new GraphKPI(
                                date,
                                startHour,
                                endHour
                        );




                chartContainer.removeAll();


                chartContainer.add(
                        graph,
                        BorderLayout.CENTER
                );


                chartContainer.revalidate();

                chartContainer.repaint();


            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Veuillez entrer des heures valides."
                );
            }
        });


        SwingUtilities.invokeLater(() -> {

            afficher.doClick();

        });
    }




    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            Dashboard dashboard =
                    new Dashboard();

            dashboard.setVisible(true);

        });
    }
}

