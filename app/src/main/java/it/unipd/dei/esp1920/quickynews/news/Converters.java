package it.unipd.dei.esp1920.quickynews.news;

import android.view.View;

import androidx.room.TypeConverter;

import java.util.ArrayList;
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


    @TypeConverter
    public static ArrayList<View> fromString(String s){
        return s == null ? null : new ArrayList<View>(); //TODO: CAPIRE CHE ERRORI POSSA CAUSARE RESTITUENDO UN OGGETTO "VUOTO"...
    }

    @TypeConverter
    public static String conversionSource(ArrayList<View> s){
        if(s!=null){
        StringBuilder toReturn= new StringBuilder();
        for(View v : s)
            toReturn.append(v.toString());
        return toReturn.toString();
        }
        else
            return "";
    }


}
