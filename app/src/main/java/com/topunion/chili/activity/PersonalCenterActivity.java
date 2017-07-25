package com.topunion.chili.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.topunion.chili.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_personal_center)
public class PersonalCenterActivity extends AppCompatActivity {

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

    @Click
    void btn_back() {
        this.finish();
    }

    @Click
    void btn_operation() {
        startActivityForResult(new Intent(this, RemarkActivity_.class), 0);
    }

    @AfterViews
    void init() {
        txt_title.setText("个人主页");
        txt_name.setText("鸣人");
        btn_operation.setImageResource(R.mipmap.more);
        btn_operation.setVisibility(View.VISIBLE);
        txt_company.setText("易投科技有限公司");
        txt_position.setText("普通职员");
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
