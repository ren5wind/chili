package com.topunion.chili.data;

/**
 * Author      : renxiaoming
 * Date        : 2017/7/15
 * Description :
 */
public class AccountBean extends BaseData {

    private String userId;
    private String token;
    private String mobile;
    private String nickName;

    public String getUid() {
        return userId;
    }

    public void setUid(String uid) {
        this.userId = uid;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return null;
    }
}
