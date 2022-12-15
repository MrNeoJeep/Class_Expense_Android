package com.future.student;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.future.R;

import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecordListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordListFragment extends Fragment {

    private static final String ARG_NAME = "name";

    //视图组件
    private String mName;
    private RecyclerView mRecordRecyclerView;
    private RecordAdapter mRecordAdapter;
    private RecordLab mRecordLab;
    private List<Record> records;

    private Button addBtn;




    public RecordListFragment() {
        // Required empty public constructor
    }


    public static RecordListFragment newInstance(String name) {
        RecordListFragment fragment = new RecordListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString(ARG_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_list, container, false);
        mRecordRecyclerView = view.findViewById(R.id.record_recycle_view);
        mRecordRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        addBtn = view.findViewById(R.id.add_button);
        //初始化数据
        //todo 向后端请求record数据
        //records = new ArrayList<>();
        mRecordLab = RecordLab.get(getActivity());
        mRecordAdapter = new RecordAdapter(mRecordLab.getRecords());
        mRecordRecyclerView.setAdapter(mRecordAdapter);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo 跳转至新增记录页
                Intent intent = RecordActivity.newIntent(getActivity(),null);
                startActivity(intent);
            }
        });

        return view;
    }
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateUI(){
        RecordLab recordLab = RecordLab.get(getActivity());
        List<Record> records = recordLab.getRecords();
        if(mRecordAdapter == null){
            mRecordAdapter = new RecordAdapter(records);
            mRecordRecyclerView.setAdapter(mRecordAdapter);
        }else{
            mRecordAdapter.setRecords(records);
            mRecordAdapter.notifyDataSetChanged();
        }
    }

    private class RecordHolder extends RecyclerView.ViewHolder {

        private Record mRecord;
        //list_item组件
        private ImageView photo;
        private TextView recordName;
        private TextView recordTime;
        private Button editBtn;

        public RecordHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_record,parent,false));
            photo = itemView.findViewById(R.id.list_item_photo);
            recordName = itemView.findViewById(R.id.list_record_name);
            recordTime = itemView.findViewById(R.id.list_record_time);
            editBtn = itemView.findViewById(R.id.list_item_edit);

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //todo 修改跳转至RecordEditActivity

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("点击的记录名：",mRecord.getRecordName());
                    Intent intent = RecordActivity.newIntent(getActivity(),mRecord.getRecordName());
                    startActivity(intent);
                    mRecordAdapter.notifyItemChanged(2);
                }
            });

        }

        public void bind(Record record){
            this.mRecord = record;
            photo.setImageResource(R.drawable.ic_launcher_background);
            recordName.setText(mRecord.getRecordName());
            recordTime.setText(mRecord.getRecordDate().toString());
        }

    }

    private class RecordAdapter extends RecyclerView.Adapter<RecordHolder> {

        private List<Record> mRecords;
        public RecordAdapter(List<Record> records){
            this.mRecords = records;
        }
        @NonNull
        @Override
        public RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new RecordHolder(inflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull RecordHolder holder, int position) {
            Record record = mRecords.get(position);
            holder.bind(record);
        }

        @Override
        public int getItemCount() {
            return mRecords.size();
        }

        public void setRecords(List<Record> records){
            mRecords = records;
        }
    }
}