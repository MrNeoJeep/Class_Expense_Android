package com.future.student;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import com.future.util.AvatarUtil;
import com.future.util.LocalDateTimeUtil;
import com.future.util.SharedPreferencesUtils;

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

    private String role;




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
        //        view = inflater.inflate(getlayoutId(), null);
        //        return view;
        //避免切换Fragment 的时候重绘UI 。失去数据
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_record_list, null);
        }
        // 缓存的viewiew需要判断是否已经被加过parent，
        // 如果有parent需要从parent删除，要不然会发生这个view已经有parent的错误。
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        mRecordRecyclerView = view.findViewById(R.id.record_recycle_view);
        mRecordRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        addBtn = view.findViewById(R.id.add_button);

        role = (String) SharedPreferencesUtils.getParam(getContext(), "role", "");
        if(role.equals("3")){
            addBtn.setVisibility(View.GONE);
        }
        //初始化数据
        //todo 向后端请求record数据
        //records = new ArrayList<>();
        mRecordLab = RecordLab.get(getActivity());
        mRecordAdapter = new RecordAdapter(mRecordLab.getRecords(getActivity()));
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
        List<Record> records = recordLab.getRecords(getActivity());
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
            editBtn.setVisibility(View.GONE);

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

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Record record){
            this.mRecord = record;
            photo.setImageBitmap(AvatarUtil.getAvatar(RecordLab.get(getActivity()).getPhotoFile(record)));
            recordName.setText(mRecord.getRecordName());
            recordTime.setText(LocalDateTimeUtil.LoaclDateTimeToStr(mRecord.getRecordDate()));
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

        @RequiresApi(api = Build.VERSION_CODES.O)
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