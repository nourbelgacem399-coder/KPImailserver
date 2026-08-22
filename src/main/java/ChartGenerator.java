import com.kpimailserver.KPI;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ChartGenerator {

    public JPanel createChart(ArrayList<KPI> data) {


        XYSeries mailSeries =
                new XYSeries("Mail Relayed");

        XYSeries spamSeries =
                new XYSeries("Spam");

        XYSeries virusSeries =
                new XYSeries("Virus");

        int i = 0;

        for (KPI kpi : data) {

            mailSeries.add(
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

            i++;
        }



        XYSeriesCollection mailDataset =
                new XYSeriesCollection();

        mailDataset.addSeries(mailSeries);


        JFreeChart chart =
                ChartFactory.createXYLineChart(
                        "Evolution des KPI",
                        "Heure",
                        "Mail Relayed",
                        mailDataset,
                        PlotOrientation.VERTICAL,
                        true,
                        true,
                        false
                );

        XYPlot plot =
                chart.getXYPlot();



        XYBarRenderer barRenderer =
                new XYBarRenderer();

        barRenderer.setSeriesPaint(
                0,
                Color.BLUE
        );

        barRenderer.setDrawBarOutline(false);


        barRenderer.setMargin(0.05);

        plot.setRenderer(
                0,
                barRenderer
        );



        XYSeriesCollection lineDataset =
                new XYSeriesCollection();

        lineDataset.addSeries(spamSeries);
        lineDataset.addSeries(virusSeries);

        plot.setDataset(
                1,
                lineDataset
        );

        NumberAxis secondAxis =
                new NumberAxis("Spam / Virus");

        plot.setRangeAxis(
                1,
                secondAxis
        );

        plot.mapDatasetToRangeAxis(
                1,
                1
        );


        XYLineAndShapeRenderer lineRenderer =
                new XYLineAndShapeRenderer();



        lineRenderer.setSeriesPaint(
                0,
                Color.RED
        );

        lineRenderer.setSeriesStroke(
                0,
                new BasicStroke(3.0f)
        );

        lineRenderer.setSeriesShapesVisible(
                0,
                true
        );


        lineRenderer.setSeriesPaint(
                1,
                Color.GREEN
        );

        lineRenderer.setSeriesStroke(
                1,
                new BasicStroke(3.0f)
        );

        lineRenderer.setSeriesShapesVisible(
                1,
                true
        );

        // Installer le renderer
        plot.setRenderer(
                1,
                lineRenderer
        );


        plot.setDatasetRenderingOrder(
                DatasetRenderingOrder.FORWARD
        );



        plot.getDomainAxis()
                .setLabel("Heure");



        plot.getRangeAxis(0)
                .setLabel("Mail Relayed");



        secondAxis.setLabel(
                "Spam / Virus"
        );



        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);



        ChartPanel chartPanel =
                new ChartPanel(chart);

        chartPanel.setMouseWheelEnabled(true);

        chartPanel.setDomainZoomable(true);

        chartPanel.setRangeZoomable(true);

        chartPanel.setPreferredSize(
                new Dimension(
                        1000,
                        600
                )
        );



        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.add(
                chartPanel,
                BorderLayout.CENTER
        );

        return panel;
    }
}