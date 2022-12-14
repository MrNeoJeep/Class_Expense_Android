package com.future;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.future.common.Constants;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity {

    //组件
    private EditText userName;
    private EditText passWord;
    private EditText passWordAgain;
    private EditText className;
    private Button signUpBtn;
    private Button backLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //组件关联视图
        userName = this.findViewById(R.id.UserNameEdit);
        passWord = this.findViewById(R.id.PassWordEdit);
        passWordAgain = this.findViewById(R.id.PassWordAgainEdit);
        className = this.findViewById(R.id.ClassEdit);
        signUpBtn = this.findViewById(R.id.SignUpButton);
        backLoginBtn = this.findViewById(R.id.BackLoginButton);

        //给按钮添加监听器
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            String pwd = passWord.getText().toString();
                            String rePwd = passWordAgain.getText().toString();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(!pwd.equals(rePwd)){
                                        Toast.makeText(SignUpActivity.this,"两次密码不一致",Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                            if(!pwd.equals(rePwd)){
                                return;
                            }
                            String json = "{\n" +
                                    "    \"username\":\""+userName.getText()+"\",\n" +
                                    "    \"password\":\""+passWord.getText()+"\"\n" +
                                    "}";
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url(Constants.IP+"/user/register")
                                    .post(RequestBody.create(MediaType.parse("application/json"),json))
                                    .build();
                            Response response = client.newCall(request).execute();

                            String res = response.body().string();
                            JSONObject jsonObject = new JSONObject(res);
                            int code = jsonObject.getInt("code");
                            String msg = jsonObject.getString("msg");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SignUpActivity.this,msg,Toast.LENGTH_SHORT).show();
                                    if(code == 200){
                                        Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SignUpActivity.this,Constants.NETERR,Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();

            }
        });

        backLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转至登录界面
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}