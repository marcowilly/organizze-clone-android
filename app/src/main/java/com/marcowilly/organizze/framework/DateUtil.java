package com.marcowilly.organizze.framework;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import java.text.SimpleDateFormat;

public abstract class DateUtil {

    public static final String DATE_PATTERN_BR = "dd/MM/yyyy";

    public static String dataAtual(){
        return  new SimpleDateFormat(DATE_PATTERN_BR).format(System.currentTimeMillis());
    }

    public static String mouthYearOnDate(final String data){
        final String arrayData[] = data.split("/");
        return arrayData[1] + arrayData[2];
    }

    public static String mouthYearOnDate(final CalendarDay data){
        return String.format("%02d%s",data.getMonth()+1, data.getYear());
    }
}
