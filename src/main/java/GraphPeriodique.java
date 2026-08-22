import com.kpimailserver.KPI;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GraphPeriodique extends JPanel {

    private ArrayList<KPI> data;
    private String title;
    private String xAxisLabel;

    public GraphPeriodique(
            ArrayList<KPI> data,
            String title,
            String xAxisLabel
    ) {

        this.data = data;
        this.title = title;
        this.xAxisLabel = xAxisLabel;

        setLayout(new BorderLayout());

        createGraph();
    }

    private void createGraph() {

        XYSeries relaySeries =
                new XYSeries("Mails relayés");

        XYSeries spamSeries =
                new XYSeries("Spam");

        XYSeries virusSeries =
                new XYSeries("Virus");


        for (int i = 0; i < data.size(); i++) {

            KPI kpi = data.get(i);

            relaySeries.add(
                    i,
                    kpi.getMailRelayed()
            );

            spamSeries.add(
                    i,
                    kpi.getSpam()
            );

            virusSeries.add(
                    i,
                    kpi.getVirus()
            );
        }


        XYSeriesCollection dataset =
                new XYSeriesCollection();

        dataset.addSeries(relaySeries);
        dataset.addSeries(spamSeries);
        dataset.addSeries(virusSeries);


        JFreeChart chart =
                ChartFactory.createXYLineChart(
                        title,
                        xAxisLabel,
                        "Nombre",
                        dataset,
                        org.jfree.chart.plot.PlotOrientation.VERTICAL,
                        true,
                        true,
                        false
                );


        XYPlot plot =
                chart.getXYPlot();



        plot.getRenderer()
                .setSeriesPaint(
                        0,
                        Color.BLUE
                );

        plot.getRenderer()
                .setSeriesPaint(
                        1,
                        Color.RED
                );

        plot.getRenderer()
                .setSeriesPaint(
                        2,
                        Color.ORANGE
                );



        java.awt.BasicStroke stroke =
                new java.awt.BasicStroke(
                        2.5f
                );

        plot.getRenderer()
                .setSeriesStroke(
                        0,
                        stroke
                );

        plot.getRenderer()
                .setSeriesStroke(
                        1,
                        stroke
                );

        plot.getRenderer()
                .setSeriesStroke(
                        2,
                        stroke
                );


        ChartPanel chartPanel =
                new ChartPanel(chart);

        chartPanel.setMouseWheelEnabled(true);

        chartPanel.setDomainZoomable(true);

        chartPanel.setRangeZoomable(true);


        add(
                chartPanel,
                BorderLayout.CENTER
        );
    }
}