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
         chart = ChartFactory.createPieChart(title, pieDataset,false,true,true);
    }

    ChartPanel getPanel(){
        return new ChartPanel(chart);
    }

    JFreeChart getChart() {
        return chart;
    }
}
