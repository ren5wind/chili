package com.topunion.chili.data;

import java.io.Serializable;

/**
 * Author      : renxiaoming
 * Date        : 2017/7/15
 * Description :
 */
public class AccountBean implements Serializable {

    private String uid;
    private String token;
    private String mobile;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return null;
    }
}
