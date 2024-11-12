package com.taller.renderer;
 
import java.io.Serial;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
 
import javax.swing.JFormattedTextField.AbstractFormatter;
 
public class DateLabelFormatter extends AbstractFormatter {
	@Serial
    private static final long serialVersionUID = 1L;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyy");
     
    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parseObject(text);
    }
 
    @Override
    public String valueToString(Object value) {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return dateFormatter.format(cal.getTime());
        }else{
            return "";
        }
    }
}