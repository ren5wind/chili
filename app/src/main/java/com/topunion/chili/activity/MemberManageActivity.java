package com.topunion.chili.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.topunion.chili.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_member_manage)
public class MemberManageActivity extends AppCompatActivity {

    @ViewById
    TextView txt_title, txt_name, txt_verify1, txt_verify2, txt_company, txt_position, txt_position_name, txt_department;

    @ViewById
    ImageButton btn_operation;

    @ViewById
    Button btn_send;

    @ViewById
    ImageView img_header, img_sex;

    @Click
    void btn_back() {
        this.finish();
    }

    @Click
    void txt_department() {
        ChooseDepartmentActivity_.intent(this).title("所在组织").data(new String[]{"技术部","财务部门"}).choose(new int[]{1,0}).startForResult(1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Toast.makeText(this, "group choose index " + resultCode, Toast.LENGTH_SHORT).show();
        } else if (requestCode == 2) {
            Toast.makeText(this, "member choose index " + resultCode, Toast.LENGTH_SHORT).show();
        }
    }

    @Click
    void btn_delete() {
        //TODO
    }

    @Click
    void txt_position_name() {
        ChooseDepartmentActivity_.intent(this).title("角色分配").data(new String[]{"标准管理员","普通员工","外包人员"}).choose(new int[]{0,1,0}).startForResult(2);
    }

    @AfterViews
    void init() {
        txt_title.setText("员工管理");
        txt_name.setText("鸣人");
        btn_operation.setVisibility(View.GONE);
        txt_company.setText("易投科技有限公司");
        txt_position.setText("普通职员");
    }

}
