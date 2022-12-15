package com.future.student;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecordLab {

    private static RecordLab sRecordLab;

    private List<Record> mRecords;

    public RecordLab(List<Record> records) {
        mRecords = records;
    }
    public static RecordLab get(Context context){
        List<Record> records = new ArrayList<>();
        if(null == sRecordLab){
            //todo 请求后端获取数据
            new Thread(new Runnable() {
                @Override
                public void run() {

                }
            }).start();
            return new RecordLab(records);
        }
        return sRecordLab;
    }

    public List<Record> getRecords() {
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
