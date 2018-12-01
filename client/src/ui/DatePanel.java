package ui;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Properties;

public class DatePanel extends JPanel {


    JDatePickerImpl datePicker;
    public DatePanel() {

        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);

        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker.setPreferredSize(new Dimension(90,20));
        //datePicker
        datePicker.getJFormattedTextField().setPreferredSize(new Dimension(90,20));
        add(datePicker);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height+5);
        //пуе
        remove(datePicker);
        datePicker.setPreferredSize(new Dimension(width,height));

        datePicker.getComponent(0).setPreferredSize(new Dimension(width-height,height));

        datePicker.getComponent(1).setPreferredSize(new Dimension(height,height));



        System.out.println(datePicker.getComponent(1));
        add(datePicker);
        //datePicker.setSize(width,height);
    }
    
    public String getTextDate(){
        return datePicker.getJFormattedTextField().getText();
    }

    public void setValueData(Object value){
        datePicker.getJFormattedTextField().setValue(value);
    }
    private class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

        private String datePattern = "yyyy-MM-dd";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) {
            if(value instanceof String)
                return (String) value;
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }

            return "";
        }

    }
}
