package com.future.util;



import android.os.Build;
import android.os.ParcelUuid;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class LocalDateTimeUtil {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String LoaclDateTimeToStr(LocalDateTime localDateTime) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return df.format(localDateTime);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDateTime StrToLoaclDateTime(String time) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String str = "";
        for(int i = 0;i < time.length();i++){
            if(time.charAt(i) == 'T'){
                str += ' ';
            }else{
                str += time.charAt(i);
            }
        }
        return LocalDateTime.parse(str,df);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDateTime DateToLocalDateTime(Date date) throws ParseException {
        Log.e("datetolocalDatetime",date.toString());
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();

        // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
        LocalDateTime localDate = instant.atZone(zoneId).toLocalDateTime();
        Log.e("localdateTime",localDate.toString());

        return localDate;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Date LocalDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = LocalDate.now();
        ZonedDateTime zdt = localDate.atStartOfDay(zoneId);

        Date date = Date.from(zdt.toInstant());
        return date;
    }

    public static Date StrToDate(String s) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(s);
        return date;
    }

    public static String DateToStr(Date date) {

        String str = date.toString();
        Log.e("datetostring",date.toString());
        String res = "";
        for(int i = 0;i < str.length();i++) {
            if(str.charAt(i) == 'T'){
                res += ' ';
            }else {
                res += str.charAt(i);
            }
        }
        return res;
    }

    public static String getDate() {
        Date date =new Date();
        System.out.println(date);

        //Date类型转String  //可指定任意的返回格式  yyyy
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = sdf.format(date);
        return dateString;
    }
}
