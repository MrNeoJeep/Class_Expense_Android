package com.future.student;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.future.MainActivity;
import com.future.common.Constants;
import com.future.util.LocalDateTimeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RecordLab {

    private static RecordLab sRecordLab;

    private List<Record> mRecords;

    public RecordLab(Context context) {
        List<Record> records = new ArrayList<>();
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                try{
                    //创建http客户端
                    OkHttpClient client = new OkHttpClient();
                    //创建http请求
                    Request request = new Request.Builder()
                            .url(Constants.IP+"/record/findAll")
                            .build();
                    Response response = client.newCall(request).execute();//执行
                    String res = response.body().string();
                    Log.i("res = ",res);
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    Log.i("jsonarray",jsonArray.toString());
                    for(int i = 0;i < jsonArray.length();i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Record record = new Record();
                        record.setId(object.getString("id"));
                        record.setRecordName(object.getString("recordName"));
                        String recordDateStr = object.getString("recordDate");
                        LocalDateTime localDateTime = LocalDateTimeUtil.StrToLoaclDateTime(recordDateStr);
                        record.setRecordDate(localDateTime);
                        record.setRecordMoney(object.getDouble("recordMoney"));
                        record.setPhotos(object.getString("photos"));
                        record.setReceipt(object.getString("receipt"));
                        record.setCheckId(object.getString("checkId"));
                        record.setState(object.getInt("state"));
                        records.add(record);
                    }
                    Log.i("record num",records.size() + "");

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(context,Constants.NETERR,Toast.LENGTH_SHORT).show();
                }

            }
        }).start();
        mRecords = records;
    }
    public static RecordLab get(Context context){
        if(null == sRecordLab){
            sRecordLab =  new RecordLab(context);
        }
        return sRecordLab;
    }

    public List<Record> getRecords(Context context) {
        List<Record> records = new ArrayList<>();
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                try{
                    //创建http客户端
                    OkHttpClient client = new OkHttpClient();
                    //创建http请求
                    Request request = new Request.Builder()
                            .url(Constants.IP+"/record/findAll")
                            .build();
                    Response response = client.newCall(request).execute();//执行
                    String res = response.body().string();
                    Log.i("res = ",res);
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    Log.i("jsonarray",jsonArray.toString());
                    for(int i = 0;i < jsonArray.length();i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Record record = new Record();
                        record.setId(object.getString("id"));
                        record.setRecordName(object.getString("recordName"));
                        String recordDateStr = object.getString("recordDate");
                        LocalDateTime localDateTime = LocalDateTimeUtil.StrToLoaclDateTime(recordDateStr);
                        record.setRecordDate(localDateTime);
                        record.setRecordMoney(object.getDouble("recordMoney"));
                        record.setPhotos(object.getString("photos"));
                        record.setReceipt(object.getString("receipt"));
                        record.setCheckId(object.getString("checkId"));
                        record.setState(object.getInt("state"));
                        records.add(record);
                    }
                    Log.i("record num",records.size() + "");

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(context,Constants.NETERR,Toast.LENGTH_SHORT).show();
                }

            }
        }).start();
        mRecords = records;
        return mRecords;
    }

    public Record getRecord(String recordName) {
        for (Record mRecord : mRecords) {
            if(mRecord.getRecordName().equals(recordName)){
                return mRecord;
            }
        }
        return null;
    }



}
