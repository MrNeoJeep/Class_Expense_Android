package com.future.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AvatarUtil {
//    public static File PHOTO_DIR = new File(Environment.getExternalStorageDirectory()+"/image");//设置保存路径
    public static void saveAvatar(Bitmap avatar, File avaterFile){
        Log.e("path:",avaterFile.getAbsolutePath());
//        File avaterFile = new File(PHOTO_DIR, fileName);//设置文件名称

//        if(avaterFile.exists()){
//            avaterFile.delete();
//        }
        try {
            //avaterFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(avaterFile);
            avatar.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Bitmap getAvatar(File avaterFile){
        Bitmap bitmap = null;
        try{
//            File avaterFile = new File(PHOTO_DIR, fileName);
            if (!avaterFile.exists()){
                Log.e("avatar file","not exist");
            }
            if(avaterFile.exists()) {
                Log.e("getAvatar path:",avaterFile.getAbsolutePath());
                bitmap = BitmapFactory.decodeFile(avaterFile.getAbsolutePath());
            }
        } catch (Exception e) {}

        return bitmap;
    }

    public static void deleteAvatar(String fileName){

    }
}
