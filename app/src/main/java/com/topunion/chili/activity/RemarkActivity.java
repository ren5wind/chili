package com.topunion.chili.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.topunion.chili.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_remark)
public class RemarkActivity extends AppCompatActivity {

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
        setResult(2);
        finish();
    }


    @AfterViews
    void init() {
        txt_title.setText("资料设置");
    }

}
