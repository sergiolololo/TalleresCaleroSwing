package com.taller.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilidades {

    public static String formatDate(Date date){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyy");
        return dateFormatter.format(date);
    }
}
