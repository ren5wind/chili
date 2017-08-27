package com.topunion.chili.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    @Click
    void btn_back() {
        String remark = mEditText.getText().toString();
        if (remark != null && remark.length() > 0) {
            Intent intent = new Intent();
            intent.putExtra("remark", remark);
            setResult(1, intent);
        }
        this.finish();
    }

    @Click
    void btn_delete() {
        deleteRequest(AccountManager.getInstance().getUserId(), uid);
    }

    @Click
    void btn_confirm() {
        updateRequest(Integer.valueOf(AccountManager.getInstance().getUserId()), uid, mEditText.getText().toString());
    }

    @Background
    void updateRequest(int userId, String friendId, String nickname) {
        boolean isSuccess = HttpHelper_.getInstance_(this).updateETFriendNickname(userId, friendId, nickname);
        updateUi(isSuccess, nickname);
    }

    @Background
    void deleteRequest(String userId, String friendId) {
        boolean isSuccess = HttpHelper_.getInstance_(this).removeETFriend(userId, friendId);
        setResult(2);
        finish();
    }


    private void updateUi(final boolean isSuccess, final String nickname) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isSuccess) {
                    mEditText.setText(nickname);
                } else {
                    Toast.makeText(RemarkActivity.this, "网络不稳，修改备注失败", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @AfterViews
    void init() {
        txt_title.setText("资料设置");
        mEditText.setText(logicNickname);
    }

}
