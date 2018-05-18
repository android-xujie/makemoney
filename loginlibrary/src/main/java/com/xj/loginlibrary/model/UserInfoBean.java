package com.xj.loginlibrary.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by yunwen on 2018/5/15.
 */

public class UserInfoBean extends BmobObject {

    private String username;
    private String password;
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
