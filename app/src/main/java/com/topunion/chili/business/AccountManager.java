package com.topunion.chili.business;


import com.topunion.chili.MyApplication_;
import com.topunion.chili.data.AccountBean;
import com.topunion.chili.util.SPUtil;

/**
 * Author      : renxiaoming
 * Date        : 2017/7/24
 * Description :
 */
public class AccountManager {
    public static final String KEY_SP_ACCOUNT_TOKEN = "key_sp_account_token";
    public static final String KEY_SP_ACCOUNT_USERID = "key_sp_account_userId";
    private static AccountManager instance;
    private final static Object syncLock = new Object();

    public static AccountManager getInstance() {
        if (instance == null) {
            synchronized (syncLock) {
                if (instance == null) {
                    instance = new AccountManager();
                }
            }
        }
        return instance;
    }
    public void saveAccount(AccountBean accountBean) {
        if (accountBean == null) {
            return;
        }
        SPUtil.setSharedStringData(MyApplication_.getAppContext(), KEY_SP_ACCOUNT_TOKEN, accountBean.getToken());
        SPUtil.setSharedStringData(MyApplication_.getAppContext(), KEY_SP_ACCOUNT_USERID, accountBean.getUid());
        MyApplication_.getInstance().setToken(accountBean.getToken());
        MyApplication_.getInstance().setMyUserId(accountBean.getUid());
    }

    public AccountBean getAccount() {
        AccountBean accountBean = new AccountBean();
        accountBean.setUid(MyApplication_.getInstance().getMyUserId());
        accountBean.setToken(MyApplication_.getInstance().getToken());
        return accountBean;
    }

    public String getToken() {
        return MyApplication_.getInstance().getToken();
    }

    public String getUserId() {
        return MyApplication_.getInstance().getMyUserId();
    }
}
