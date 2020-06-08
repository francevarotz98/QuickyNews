package it.unipd.dei.esp1920.quickynews.news;

import android.view.View;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        return s == null ? null : new Source(s,s); //TODO: CAPIRE CHE ERRORI POSSA CAUSARE ...
    }

    @TypeConverter
    public static String conversionSource(Source s){
        return s == null ? null : s.getName();
    }


    @TypeConverter
    public static List<String> fromString(String s){
        List<String> returned = new ArrayList<>();
        //return a == null ? null : new ArrayList<String>().add(s); //TODO: CAPIRE CHE ERRORI POSSA CAUSARE RESTITUENDO UN OGGETTO "VUOTO"...
        if(s!=null)
            returned.add(s);
        return returned;
    }

    @TypeConverter
    public static String conversionSource(List<String> s){
        if(s!=null){
        StringBuilder toReturn= new StringBuilder();
        for(String v : s)
            toReturn.append(v);
        return toReturn.toString();
        }
        else
            return "";
    }


}
