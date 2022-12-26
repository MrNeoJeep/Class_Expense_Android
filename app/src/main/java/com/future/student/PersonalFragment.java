package com.future.student;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.future.MainActivity;
import com.future.R;
import com.future.common.Constants;
import com.future.util.UserInfoUtil;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalFragment extends Fragment {

    private static final String ARG_NAME = "name";

    private String mName;
    private double expense;

    //视图组件
    private TextView username;
    private TextView role;
    private TextView classname;
    private Button logoutBtn;
    private Button updateExpenseBtn;
    private TextView classExpense;

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
        updateExpenseBtn = view.findViewById(R.id.update_expense);
        classExpense = view.findViewById(R.id.classexpense);

        UserInfo userInfo = UserInfoUtil.getUserInfo(getActivity());

        Log.i("userInfo id",userInfo.getId());
        username.setText(userInfo.getUsername());
        //todo 获取班费

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FormBody.Builder params = new FormBody.Builder();
                    params.add("className",userInfo.getClassname());
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(Constants.IP+"/class/getExpense")
                            .post(params.build())
                            .build();
                    Response response = client.newCall(request).execute();//执行发送的指令
                    String respData = response.body().string();
                    JSONObject jsonObject = new JSONObject(respData);

                    expense = jsonObject.getDouble("data");
                    classExpense.setText("   班费:" + expense);
                    Log.i("expense",expense+"");
                }catch (Exception e){
                    e.printStackTrace();

                    Toast.makeText(getActivity(),Constants.NETERR,Toast.LENGTH_SHORT).show();
                }
            }
        }).start();

        if(userInfo.getRole() == 1){
            role.setText("老师");
        }else if(userInfo.getRole() == 2) {
            role.setText("记账员");
        }else {
            role.setText("学生");
        }
        if(userInfo.getRole() == 3) {
            updateExpenseBtn.setVisibility(View.GONE);
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
        updateExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(getActivity());
                AlertDialog.Builder inputDialog =
                        new AlertDialog.Builder(getActivity());
                inputDialog.setTitle("请输入班费").setView(editText);
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Toast.makeText(getActivity(),
                                        editText.getText().toString(),
                                        Toast.LENGTH_SHORT).show();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            FormBody.Builder params = new FormBody.Builder();
                                            params.add("className",userInfo.getClassname());
                                            params.add("expense",editText.getText().toString());
                                            OkHttpClient client = new OkHttpClient();
                                            Request request = new Request.Builder()
                                                    .url(Constants.IP+"/class/updateExpense")
                                                    .post(params.build())
                                                    .build();
                                            Response response = client.newCall(request).execute();//执行发送的指令

                                            classExpense.setText("   班费:" + editText.getText().toString());
                                            Log.i("expense",expense+"");
                                        }catch (Exception e){
                                            e.printStackTrace();
                                            Toast.makeText(getActivity(),Constants.NETERR,Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).start();
                            }
                        }).show();
            }
        });

        return view;
    }
}