import com.kpimailserver.KPI;

import com.kpimailserver.DatabaseManager;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYBarDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GraphKPI extends JPanel {



    public GraphKPI(
            String date,
            int startHour,
            int endHour) {

        setLayout(new BorderLayout());


        ArrayList<KPI> data =
                DatabaseManager.getKPIBetweenHours(
                        date,
                        startHour,
                        endHour
                );



        XYSeries relayed =
                new XYSeries("Mail Relayed");

        XYSeries spam =
                new XYSeries("Spam");

        XYSeries virus =
                new XYSeries("Virus");


        int maxValue = 0;




        for (KPI kpi : data) {

            int hour;

            try {

                hour = Integer.parseInt(
                        kpi.getHour().substring(0, 2)
                );

            } catch (Exception e) {

                continue;
            }




            if (hour < startHour ||
                    hour > endHour) {

                continue;
            }


            int mail =
                    Math.max(
                            0,
                            kpi.getMailRelayed()
                    );

            int spamValue =
                    Math.max(
                            0,
                            kpi.getSpam()
                    );

            int virusValue =
                    Math.max(
                            0,
                            kpi.getVirus()
                    );


            relayed.add(
                    hour,
                    mail
            );

            spam.add(
                    hour,
                    spamValue
            );

            virus.add(
                    hour,
                    virusValue
            );


            maxValue =
                    Math.max(
                            maxValue,
                            mail
                    );

            maxValue =
                    Math.max(
                            maxValue,
                            spamValue
                    );

            maxValue =
                    Math.max(
                            maxValue,
                            virusValue
                    );
        }




        XYSeriesCollection relayedDataset =
                new XYSeriesCollection();

        relayedDataset.addSeries(
                relayed
        );

        IntervalXYDataset barDataset =
                new XYBarDataset(
                        relayedDataset,
                        0.25
                );




        XYSeriesCollection lineDataset =
                new XYSeriesCollection();

        lineDataset.addSeries(spam);

        lineDataset.addSeries(virus);



        NumberAxis xAxis =
                new NumberAxis(
                        "Heure"
                );

        xAxis.setAutoRange(false);

        xAxis.setLowerMargin(0);

        xAxis.setUpperMargin(0);



        xAxis.setRange(
                startHour - 0.5,
                endHour + 0.5
        );

        xAxis.setTickUnit(
                new NumberTickUnit(1)
        );




        NumberAxis yAxis =
                new NumberAxis(
                        "Nombre de mails"
                );

        yAxis.setAutoRange(false);

        yAxis.setLowerBound(0);


        int maxY;

        if (maxValue <= 0) {

            maxY = 200;

        } else {

            maxY =
                    ((maxValue + 199) / 200)
                            * 200;
        }


        yAxis.setUpperBound(
                maxY
        );


        yAxis.setTickUnit(
                new NumberTickUnit(200)
        );




        XYPlot plot =
                new XYPlot(
                        barDataset,
                        xAxis,
                        yAxis,
                        null
                );



        XYBarRenderer barRenderer =
                new XYBarRenderer();

        barRenderer.setShadowVisible(
                false
        );

        barRenderer.setDrawBarOutline(
                false
        );

        barRenderer.setMargin(
                0.25
        );


        barRenderer.setSeriesPaint(
                0,
                Color.BLUE
        );

        plot.setRenderer(
                0,
                barRenderer
        );




        plot.setDataset(
                1,
                lineDataset
        );


        XYLineAndShapeRenderer lineRenderer =
                new XYLineAndShapeRenderer();

        lineRenderer.setDefaultLinesVisible(
                true
        );

        lineRenderer.setDefaultShapesVisible(
                true
        );




        lineRenderer.setSeriesPaint(
                0,
                Color.RED
        );




        lineRenderer.setSeriesPaint(
                1,
                Color.ORANGE
        );


        plot.setRenderer(
                1,
                lineRenderer
        );




        plot.mapDatasetToRangeAxis(
                0,
                0
        );

        plot.mapDatasetToRangeAxis(
                1,
                0
        );




        plot.setDatasetRenderingOrder(
                DatasetRenderingOrder.FORWARD
        );



        String titre =
                "KPI Mail Server - "
                        + date
                        + " | "
                        + String.format(
                        "%02d",
                        startHour
                )
                        + "h → "
                        + String.format(
                        "%02d",
                        endHour
                )
                        + "h";




        JFreeChart chart =
                new JFreeChart(
                        titre,
                        JFreeChart.DEFAULT_TITLE_FONT,
                        plot,
                        true
                );




        ChartPanel chartPanel =
                new ChartPanel(
                        chart
                );

        chartPanel.setMouseWheelEnabled(
                true
        );

        chartPanel.setDomainZoomable(
                false
        );

        chartPanel.setRangeZoomable(
                true
        );


        add(
                chartPanel,
                BorderLayout.CENTER
        );
    }
}

