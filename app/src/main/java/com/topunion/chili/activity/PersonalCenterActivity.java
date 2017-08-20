package com.topunion.chili.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.topunion.chili.R;
import com.topunion.chili.business.AccountManager;
import com.topunion.chili.net.HttpHelper_;
import com.topunion.chili.net.request_interface.GetETMemberDetails;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_personal_center)
public class PersonalCenterActivity extends AppCompatActivity {
    @Extra
    String uid;

    @ViewById
    TextView txt_title, txt_name, txt_verify1, txt_verify2, txt_company, txt_position;

    @ViewById
    ImageButton btn_operation;

    @ViewById
    Button btn_send;

    @ViewById
    ImageView img_sex;

    @ViewById
    SimpleDraweeView img_header;

    private GetETMemberDetails.GetETMemberDetailsResponse.Member mData;

    @Click
    void btn_back() {
        this.finish();
    }

    @Click
    void btn_send() {
        if ("0".equals(mData.isFriend)) {//不是好友
            addETFriend();
        } else {

        }
    }

    @Click
    void btn_operation() {
        startActivityForResult(new Intent(this, RemarkActivity_.class), 0);
    }

    @AfterViews
    void init() {
        txt_title.setText("个人主页");
        getUserDeetailRequest();
    }

    @Background
    void addETFriend() {
        boolean b = HttpHelper_.getInstance_(this).addETFriend(AccountManager.getInstance().getUserId(), uid, AccountManager.getInstance().getNickName());
        if (b) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(PersonalCenterActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Background
    void getUserDeetailRequest() {
        GetETMemberDetails.GetETMemberDetailsResponse response = HttpHelper_.getInstance_(this).getETMemberDetails(uid);
        mData = response.data;
        updateUi();
    }

    private void updateUi() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txt_name.setText(mData.nickname);
                img_header.setImageURI(mData.headImg);
                btn_operation.setImageResource(R.mipmap.more);
                btn_operation.setVisibility(View.VISIBLE);
                txt_company.setText(mData.corpName);
                txt_position.setText(mData.corpTitleName);
                if ("male".equals(mData.gender)) {
                    img_sex.setImageResource(R.mipmap.sex_boy);
                } else if ("female".equals(mData.gender)) {
                    img_sex.setImageResource(R.mipmap.sex_girl);
                } else {
                    img_sex.setVisibility(View.GONE);
                }
                if (mData.hasIdentify) {
                    txt_verify1.setText("已实名认证");
                } else {
                    txt_verify1.setVisibility(View.GONE);
                }
                if (mData.hasCorp) {
                    txt_verify2.setText("已企业认证");
                } else {
                    txt_verify2.setVisibility(View.GONE);
                }
                if ("0".equals(mData.isFriend)) {//不是好友
                    btn_send.setText("添加好友");
                } else {
                    btn_send.setText("发消息");
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            txt_name.setText(data.getStringExtra("remark"));
        } else if (resultCode == 2) {
            btn_send.setText("添加好友");
        }
    }
}
