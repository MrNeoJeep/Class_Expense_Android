package com.future.util;

import android.content.Context;

import com.future.student.UserInfo;

public class UserInfoUtil {
    public static UserInfo getUserInfo(Context context) {
        UserInfo userInfo ;
        String id = null;
        String username = null;
        String classname = null;
        int role = 3;
        String token = null;
        try {
            id = (String) SharedPreferencesUtils.getParam(context, "id", "");
            username = (String) SharedPreferencesUtils.getParam(context, "username", "");
            classname = (String) SharedPreferencesUtils.getParam(context, "classname", "");
            String role1 = (String) SharedPreferencesUtils.getParam(context, "role", "");
            role = Integer.parseInt(role1);
            token = (String) SharedPreferencesUtils.getParam(context, "token", "");
        }catch (Exception e){
            e.printStackTrace();
        }
        userInfo = new UserInfo(id,username,role,classname,token);

        return userInfo;
    }

    public static void deleteUserInfo(Context context) {
        SharedPreferencesUtils.clear(context);
    }
}
