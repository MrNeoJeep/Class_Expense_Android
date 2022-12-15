package com.future.student;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.future.MainActivity;
import com.future.R;
import com.future.util.UserInfoUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalFragment extends Fragment {

    private static final String ARG_NAME = "name";

    private String mName;

    //视图组件
    private TextView username;
    private TextView role;
    private TextView classname;
    private Button logoutBtn;

    public PersonalFragment() {
        // Required empty public constructor
    }

    public static PersonalFragment newInstance(String name) {
        PersonalFragment fragment = new PersonalFragment();
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
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        username = view.findViewById(R.id.user_name);
        role = view.findViewById(R.id.role);
        classname = view.findViewById(R.id.classname);
        logoutBtn = view.findViewById(R.id.logout);

        UserInfo userInfo = UserInfoUtil.getUserInfo(getActivity());

        Log.i("userInfo id",userInfo.getId());
        username.setText(userInfo.getUsername());
        if(userInfo.getRole() == 1){
            role.setText("老师");
        }else if(userInfo.getRole() == 2) {
            role.setText("记账员");
        }else {
            role.setText("学生");
        }

        classname.setText("   班级:"+userInfo.getClassname());
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //销毁用户信息
                UserInfoUtil.deleteUserInfo(getActivity());
                // 销毁所有Activity

                //跳转登录界面
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}