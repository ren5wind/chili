package com.topunion.chili.activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.topunion.chili.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_group_center)
public class GroupCenterActivity extends AppCompatActivity {

    @ViewById
    TextView txt_title, txt_name, txt_count, txt_member1, txt_member2, txt_member3, txt_member4, txt_member_add, txt_member_remove;

    @ViewById
    ImageButton btn_operation;

    @ViewById
    SimpleDraweeView img_header;

    @Click
    void img_edit() {
        final AlertDialog alertDialog = new AlertDialog.Builder(GroupCenterActivity.this).create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.dialog_bg);
        window.setGravity(Gravity.CENTER);
        window.setContentView(R.layout.dialog_group_edit);
        final EditText edit_alert = (EditText) window.findViewById(R.id.edit_alert);
        TextView txt_alert_title = (TextView) window.findViewById(R.id.txt_alert_title);
        txt_alert_title.setText("修改群名称");
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        };
        window.findViewById(R.id.btn_close).setOnClickListener(listener);
        window.findViewById(R.id.btn_cancel).setOnClickListener(listener);
        window.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edit_alert.getText().toString();

                if (name != null && name.length() > 0) {
                    //TODO
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(GroupCenterActivity.this,"群名称不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Click
    void btn_back() {
        this.finish();
    }

    @Click
    void btn_operation() {
        startActivityForResult(new Intent(this, RemarkActivity_.class), 0);
    }

    @Click
    void btn_all_member() {
        GroupMembersActivity_.intent(this).start();
    }

    @Click
    void txt_member_add() {
        ChoosePersonActivity_.intent(this)
                .choose(new int[]{0,0,0,0,0,0,0})
                .data(new String[]{"张三","李四","王五","赵六","田七","猴八","牛二"})
                .title("选择联系人").startForResult(0);
    }

    @Click
    void txt_member_remove() {
        ChoosePersonActivity_.intent(this)
                .choose(new int[]{0,0,0,0,0,0,0})
                .data(new String[]{"张三","李四","王五","赵六","田七","猴八","牛二"})
                .title("删除联系人").startForResult(0);
    }


    @AfterViews
    void init() {
        txt_title.setText("群资料设置");
        txt_name.setText("张三，李四，王五...");
        txt_member1.setText("张三");
        txt_member2.setText("李四");
        txt_member3.setText("王五");
        txt_member3.setVisibility(View.VISIBLE);
        txt_member4.setText("赵六");
        txt_member4.setVisibility(View.VISIBLE);
        btn_operation.setImageResource(R.mipmap.more);
        btn_operation.setVisibility(View.VISIBLE);
        txt_count.setText("共33人");
    }

}
