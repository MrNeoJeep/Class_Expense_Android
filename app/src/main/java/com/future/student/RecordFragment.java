package com.future.student;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.future.R;
import com.future.common.Constants;
import com.future.util.LocalDateTimeUtil;
import com.future.util.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordFragment extends Fragment {

    private static final String ARG_RECORD_NAME = "record_name";

    private String mRecordName;
    private static final int REQUEST_DATE = 0;

    //组件
    private EditText recordName;
    private Button recordDateBtn;
    private EditText recordMoneyText;
    private ImageView photoImg;
    private ImageView receiptImg;
    private ImageButton takePhotoBtn;
    private ImageButton takeReceiptBtn;
    private EditText checkEditText;
    private EditText state;

    private Button addRecordBtn;
    private Button updateRecordBtn;
    private Button questionBtn;
    private RecyclerView questionRecyclerView;

    private String role;

    private Record mRecord;

    public RecordFragment() {
        // Required empty public constructor
    }

    public static RecordFragment newInstance(String recordName) {
        RecordFragment fragment = new RecordFragment();
        Bundle args = new Bundle();
        if(recordName != null) {
            args.putString(ARG_RECORD_NAME, recordName);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecordName = getArguments().getString(ARG_RECORD_NAME);
        }
        mRecord = RecordLab.get(getActivity()).getRecord(mRecordName);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        recordName = view.findViewById(R.id.record_name);
        recordDateBtn = view.findViewById(R.id.record_date);
        recordMoneyText = view.findViewById(R.id.record_money);
        photoImg = view.findViewById(R.id.photo_img);
        receiptImg = view.findViewById(R.id.receipt_img);
        takePhotoBtn = view.findViewById(R.id.take_photo);
        takeReceiptBtn = view.findViewById(R.id.take_receipt);
        checkEditText = view.findViewById(R.id.check_edittext);
        state = view.findViewById(R.id.state);
        addRecordBtn = view.findViewById(R.id.add_record);
        updateRecordBtn = view.findViewById(R.id.update_record);
        questionBtn = view.findViewById(R.id.question_btn);

        role = (String) SharedPreferencesUtils.getParam(getContext(), "role", "");
        if(mRecordName == null) {
            updateRecordBtn.setVisibility(View.GONE);
            recordDateBtn.setEnabled(false);
            recordDateBtn.setText(LocalDateTimeUtil.getDate());
        }else {
            recordName.setText(mRecord.getRecordName());
            recordDateBtn.setText(mRecord.getRecordDate()+"");
            recordMoneyText.setText(mRecord.getRecordMoney() + "");
            Integer recordState = mRecord.getState();
            if(recordState == 0) {
                state.setText("问题未关闭");
            }else{
                state.setText("问题已关闭");
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        FormBody.Builder params = new FormBody.Builder();
                        params.add("id",mRecord.getCheckId());
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(Constants.IP+"/user/findById")
                                .post(params.build())
                                .build();
                        Response response = client.newCall(request).execute();//执行发送的指令
                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("data"));
                        String username = jsonObject1.getString("username");
                        checkEditText.setText(username);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();


        }
        //如果是学生，则只有查看的权限和质疑的权限
        if(role.equals("3")) {
            updateRecordBtn.setVisibility(View.GONE);
            addRecordBtn.setVisibility(View.GONE);
            recordDateBtn.setEnabled(false);
            recordMoneyText.setEnabled(false);
            photoImg.setEnabled(false);
            receiptImg.setEnabled(false);
            takePhotoBtn.setEnabled(false);
            takeReceiptBtn.setEnabled(false);
            checkEditText.setEnabled(false);
            state.setEnabled(false);


        }

        //todo 日期选择器
        recordDateBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
//                DatePickerFragment dialog = new DatePickerFragment();
                DatePickerFragment dialog = null;
                if(mRecord != null){
                    try {
                        dialog = DatePickerFragment.newInstance(LocalDateTimeUtil.LocalDateTimeToDate(mRecord.getRecordDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        dialog = DatePickerFragment.newInstance(null);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                assert dialog != null;
                dialog.setTargetFragment(RecordFragment.this,REQUEST_DATE);
                dialog.show(fm,"RecordDate");

            }
        });

        //todo 从图库选择图片
        photoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (recordName.getText() == null) {
                    Toast.makeText(getActivity(), "记录名未填写", Toast.LENGTH_SHORT).show();
                }

            }
        });

        receiptImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recordName.getText() == null) {
                    //提示用户添加失败
                    new AlertDialog.Builder(getActivity())
                            .setTitle("信息")
                            .setMessage("图片选取失败，请检查记录名是否填写完整")
                            .setPositiveButton("确定", null)
                            .show();
                }else{
                    mRecord.setRecordName(recordName.getText().toString());
                }
            }
        });

        //todo 点击拍照按钮获取图片
        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recordName.getText().toString() != null) {
                    Log.e("recordname",recordName.getText().toString());
                    mRecord.setRecordName(recordName.getText().toString());

                }else{
                    //提示用户添加失败
                    new AlertDialog.Builder(getActivity())
                            .setTitle("信息")
                            .setMessage("图片选取失败，请检查记录名是否填写完整")
                            .setPositiveButton("确定", null)
                            .show();
                }
            }
        });

        takeReceiptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recordName.getText() == null) {
                    //提示用户添加失败
                    new AlertDialog.Builder(getActivity())
                            .setTitle("信息")
                            .setMessage("图片选取失败，请检查记录名是否填写完整")
                            .setPositiveButton("确定", null)
                            .show();
                }else{
                    mRecord.setRecordName(recordName.getText().toString());
                }
            }
        });

        //todo 添加记录功能
        addRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //todo 修改记录/更新记录
        updateRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            try {
                Log.e("date:",LocalDateTimeUtil.DateToLocalDateTime(date).toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                mRecord.setRecordDate(LocalDateTimeUtil.DateToLocalDateTime(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.e("record fragment add 日期",mRecord.getRecordDate().toString());
            recordDateBtn.setText(mRecord.getRecordDate().toString());
        }
    }
}