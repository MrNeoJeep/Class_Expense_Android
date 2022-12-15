package com.future.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.future.R;

import java.io.Serializable;
import java.util.UUID;

public class RecordActivity extends AppCompatActivity {

    private static final String EXTRA_RECORD_NAME = "record_name";

    public static Intent newIntent(Context packageContext,String recordName){
        Intent intent = new Intent(packageContext,RecordActivity.class);
        if(recordName != null){
            intent.putExtra(EXTRA_RECORD_NAME,recordName);
        }
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        String recordName = (String) getIntent().getSerializableExtra(EXTRA_RECORD_NAME);


        if(fragment == null){
            fragment = RecordFragment.newInstance(recordName);
            fm.beginTransaction()
                    .add(R.id.fragment_container,fragment)
                    .commit();
        }
    }
}