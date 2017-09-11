package com.topunion.chili.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.topunion.chili.R;
import com.topunion.chili.business.AccountManager;
import com.topunion.chili.net.HttpHelper_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_remark)
public class RemarkActivity extends AppCompatActivity {

    @Extra
    String logicNickname;
    @Extra
    String uid;
    @ViewById
    TextView txt_title;
    @ViewById
    EditText mEditText;
    @ViewById
    Button btn_confirm;
    @ViewById
    RelativeLayout search;
    String remark;

    @Click
    void btn_back() {
        if (remark != null && remark.length() > 0) {
            Intent intent = new Intent();
            intent.putExtra("remark", remark);
            setResult(1, intent);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        if (remark != null && remark.length() > 0) {
            Intent intent = new Intent();
            intent.putExtra("remark", remark);
            setResult(1, intent);
        }
        finish();
    }

    @Click
    void btn_delete() {
        deleteRequest(AccountManager.getInstance().getUserId(), uid);
    }

    @Click
    void btn_confirm() {
        updateRequest(AccountManager.getInstance().getUserId(), uid, mEditText.getText().toString());
    }

    @Background
    void updateRequest(String userId, String friendId, String nickname) {
        boolean isSuccess = HttpHelper_.getInstance_(this).updateETFriendNickname(userId, friendId, nickname);
        if(isSuccess){
            remark = nickname;
        }
        updateUi(isSuccess, nickname);
    }

    @Background
    void deleteRequest(String userId, String friendId) {
        boolean isSuccess = HttpHelper_.getInstance_(this).removeETFriend(userId, friendId);
        setResult(2);
        finish();
    }


    @UiThread
    void updateUi(final boolean isSuccess, final String nickname) {
        if (isSuccess) {
//            mEditText.setText(nickname);
            Toast.makeText(RemarkActivity.this, "修改备注成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(RemarkActivity.this, "修改备注失败", Toast.LENGTH_SHORT).show();
        }
    }

    @AfterViews
    void init() {
        txt_title.setText("资料设置");
        mEditText.setText(logicNickname);
        remark = logicNickname;
        btn_confirm.setVisibility(View.VISIBLE);
        btn_confirm.setText("确定");
        search.setVisibility(View.VISIBLE);

    }

}
