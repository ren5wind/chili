package com.topunion.chili.business;


import com.topunion.chili.MyApplication;
import com.topunion.chili.util.SPUtil;

/**
 * Author      : renxiaoming
 * Date        : 2017/7/24
 * Description :
 */
public class AccountManager {

    public static final String KEY_SP_ACCOUNT_TOKEN = "key_sp_account_token";

    public static final String KEY_SP_ACCOUNT_USERID = "key_sp_account_userId";

    public static final String KEY_SP_ACCOUNT_USER = "key_sp_account_user";

    public void saveAccount(AccountBean accountBean) {
        if (accountBean == null) {
            return;
        }
        SPUtil.setSharedStringData(MyApplication.getAppContext(), KEY_SP_ACCOUNT_TOKEN, accountBean.getToken());
        SPUtil.setSharedIntData(MyApplication.getAppContext(), KEY_SP_ACCOUNT_USERID, accountBean.getUid());
        (MyApplication.getInstance()).setToken(accountBean.getToken());
        ( MyApplication.getInstance()).setMyUserId(accountBean.getUid());
        //优化-------把我的用户信息持久化，而不是在内存中
        UserBean user = accountBean.getUser();
        //缓存到sp中
        SPUtil.setObject(MyApplication.getAppContext(), AccountManager.class.getName(), KEY_SP_ACCOUNT_USER, user);
        //内存中也放一份
        userApi.saveUser(user);
    }

    public AccountBean getAccount() {
        AccountBean accountBean = new AccountBean();
        accountBean.setUid(((VideoShareApplication) VideoShareApplication.getInstance()).getMyUserId());
        accountBean.setToken(((VideoShareApplication) VideoShareApplication.getInstance()).getToken());
        //先去内存中取
        UserBean user = userApi.getUserById(accountBean.getUid());
        if (user == null) {//去缓存中取
            user = SPUtil.getObject(VideoShareApplication.getAppContext(), AccountManager.class.getName(), KEY_SP_ACCOUNT_USER, UserBean.class);
        }
        accountBean.setUser(user);
        return accountBean;
    }

    public String getToken() {
        return ((VideoShareApplication) VideoShareApplication.getInstance()).getToken();
    }

    public int getUserId() {
        return ((VideoShareApplication) VideoShareApplication.getInstance()).getMyUserId();
    }
}
