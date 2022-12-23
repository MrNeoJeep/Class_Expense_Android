package com.future.student;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.future.R;
import com.future.util.SharedPreferencesUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordFragment extends Fragment {

    private static final String ARG_RECORD_NAME = "record_name";

    private String mRecordName;
    private DatePicker mDatePicker;

    //组件
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
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
}