package ui.graphics;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;


public class chartPieUI{

    String title;
    JFreeChart chart;

    chartPieUI(String title) {
        this.title = title;
    }

    void createChart(String[] name, Float[] data) {


        DefaultPieDataset pieDataset = new DefaultPieDataset();

        for(int i = 0; name.length > i && data.length > i; i++){
            pieDataset.setValue(name[i], data[i]);
        }

         chart = ChartFactory.createPieChart(
                title,  // chart title
                 pieDataset,             // data
                false,               // no legend
                true,                // tooltips
                true                // no URL generation
        );

//        TextTitle t = chart.getTitle();
//        t.setHorizontalAlignment(HorizontalAlignment.LEFT);
//        t.setPaint(new Color(240, 240, 240));
//        t.setFont(new Font("Arial", Font.BOLD, 26));
//
//        PiePlot plot = (PiePlot) chart.getPlot();
//        plot.setBackgroundPaint(null);
//        plot.setInteriorGap(0.04);
//        plot.setOutlineVisible(false);

        //add(new ChartPanel(chart));
    }

    ChartPanel getPanel(){
        return new ChartPanel(chart);
    }

    JFreeChart getChart() {
        return chart;
    }
}
