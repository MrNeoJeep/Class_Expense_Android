package com.future.student;

public class UserInfo {
    private String id;

    private String username;

    private int role;

    private String classname;

    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserInfo() {
    }

    public UserInfo(String id, String username, int role, String classname, String token) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.classname = classname;
        this.token = token;
    }
}
