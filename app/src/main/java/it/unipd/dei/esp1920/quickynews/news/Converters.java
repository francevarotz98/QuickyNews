package it.unipd.dei.esp1920.quickynews.news;

import androidx.room.TypeConverter;

import java.util.Date;

/*
* We need this class to convert some specific types
* which Room can't handle.
* */


public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Source fromSource(String s){
        return s == null ? null : new Source(null,null); //TODO: CAPIRE CHE ERRORI POSSA CAUSARE ...
    }

    @TypeConverter
    public static String conversionSource(Source s){
        return s == null ? null : s.getName();
    }

}
