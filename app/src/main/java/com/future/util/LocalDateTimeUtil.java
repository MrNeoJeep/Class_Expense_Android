package com.future.util;



import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
}
