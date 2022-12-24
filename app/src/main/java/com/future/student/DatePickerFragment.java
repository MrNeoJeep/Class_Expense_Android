package com.future.student;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.future.R;
import com.future.util.LocalDateTimeUtil;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DatePickerFragment extends DialogFragment {

    private static final String ARG_DATE = "date";

    public static final String EXTRA_DATE = "RecordDate";
    private DatePicker mDatePicker;


    // TODO: Rename and change types of parameters
    private Date mDate = null;

    public DatePickerFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static DatePickerFragment newInstance(Date date) throws ParseException {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle args = new Bundle();
        if(date == null) {
            Date date1 = new Date();
            String s1 = String.format("%tY-%tm-%td",date1,date1,date1);
            date = LocalDateTimeUtil.StrToDate(s1);
            args.putSerializable(ARG_DATE, date);
        }else{
            args.putSerializable(ARG_DATE, date);
        }

        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDate = (Date) getArguments().getSerializable(ARG_DATE);
            Log.e("datepickerfragment",mDate.toString());
        }
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_date_picker, container, false);
//    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_date_picker,null);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);

        mDatePicker = v.findViewById(R.id.dialog_data_picker);
        mDatePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                null);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("修改Record的日期")
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int year = mDatePicker.getYear();
                                int month = mDatePicker.getMonth();
                                int day = mDatePicker.getDayOfMonth();
                                Date date = new GregorianCalendar(year,month,day).getTime();
                                sendResult(Activity.RESULT_OK,date);

                            }
                        })
                .create();
    }
    private void sendResult(int resultCode, Date date){
        if(getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE,date);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);

    }
}