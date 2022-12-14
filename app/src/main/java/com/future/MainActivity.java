package com.future;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.future.common.Constants;
import com.future.student.RecordListActivity;
import com.future.student.UserInfo;
import com.future.util.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    //组件
    private EditText userName;
    private EditText passWord;
    private Button loginBtn;
    private Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //组件关联视图
        userName = this.findViewById(R.id.UserNameEdit);
        passWord = this.findViewById(R.id.PassWordEdit);
        loginBtn = this.findViewById(R.id.LoginButton);
        signUpBtn = this.findViewById(R.id.SignUpButton);

        //给按钮添加监听器
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //向后端发起请求
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.i("username", String.valueOf(userName.getText()));
                            Log.i("password", String.valueOf(passWord.getText()));
                            if(userName.getText() == null || passWord.getText() == null){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this,"用户名或密码未填写",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            String json = "{\n" +
                                    "    \"username\":\""+userName.getText()+"\",\n" +
                                    "    \"password\":\""+passWord.getText()+"\"\n" +
                                    "}";
                            //创建http客户端
                            OkHttpClient client = new OkHttpClient();
                            //创建http请求
                            Request request = new Request.Builder()
                                    .url(Constants.IP+"/user/login")
                                    .post(RequestBody.create(MediaType.parse("application/json"),json))
                                    .build();
                            Response response = client.newCall(request).execute();//执行
                            String res = response.body().string();
                            JSONObject jsonObject = new JSONObject(res);
                            int code = jsonObject.getInt("code");
                            String msg = jsonObject.getString("msg");
                            String data = jsonObject.getString("data");
                            JSONObject jsonObject1 = new JSONObject(data);
                            //获取请求头信息
                            Headers headers = response.headers();
                            for(int i = 0;i < headers.size();i++) {
                                Log.i("login ",headers.name(i) + ":" + headers.value(i));
                                if(headers.name(i).contains("Authorization")){
                                    //保存token信息以及用户信息
                                    String token = headers.value(i);
                                    UserInfo userInfo = new UserInfo();
                                    userInfo.setId(jsonObject1.getString("id"));
                                    userInfo.setUsername(jsonObject1.getString("username"));
                                    userInfo.setClassname(jsonObject1.getString("classname"));
                                    userInfo.setRole(jsonObject1.getInt("role"));
                                    userInfo.setToken(token);
                                    Log.i("userInfo id",userInfo.getId());
                                    Log.i("userInfo username",userInfo.getUsername());
                                    Log.i("userInfo classname",userInfo.getClassname());
                                    Log.i("userInfo role",userInfo.getRole()+"");
                                    Log.i("userInfo token",userInfo.getToken());
                                    SharedPreferencesUtils.setParam(MainActivity.this,"userInfo",userInfo);
                                }
                            }


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                                    if(code == 200) {
                                        Intent intent = new Intent(MainActivity.this, RecordListActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this,Constants.NETERR,Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 跳转到注册界面
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}