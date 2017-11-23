package com.topunion.chili.activity;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.topunion.chili.MainActivity_;
import com.topunion.chili.R;
import com.topunion.chili.base.RxBus;
import com.topunion.chili.business.AccountManager;
import com.topunion.chili.net.HttpHelper_;
import com.topunion.chili.net.request_interface.GetGroupDetails;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

@EActivity(R.layout.activity_group_center)
public class GroupCenterActivity extends BaseAppCompatActivity {

    @ViewById
    TextView txt_title, txt_name, txt_count, txt_member1, txt_member2, txt_member3, txt_member4, txt_member_add, txt_member_remove;
    @ViewById
    LinearLayout ll_member1, ll_member2, ll_member3, ll_member4;
    @ViewById
    SimpleDraweeView img_member1, img_member2, img_member3, img_member4;
    @ViewById
    ImageButton btn_operation;

    @ViewById
    SimpleDraweeView img_header;

    @Extra
    long groupImId;

    private GetGroupDetails.GetGroupDetailsResponse.Group mGroup;

    @Click
    void img_edit() {
        final AlertDialog alertDialog = new AlertDialog.Builder(GroupCenterActivity.this).create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setBackgroundDrawableResource(R.drawable.dialog_bg);
        window.setGravity(Gravity.CENTER);
        window.setContentView(R.layout.dialog_group_edit);
        final EditText edit_alert = (EditText) window.findViewById(R.id.edit_alert);
//        edit_alert.setFocusable(true);
//        edit_alert.setFocusableInTouchMode(true);
//        edit_alert.requestFocus();
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
                String name = edit_alert.getText().toString().trim();
                if (name != null && name.length() > 0) {
                    alertDialog.dismiss();
                    updateGroupName(name);
                } else {
                    Toast.makeText(GroupCenterActivity.this, "群名称不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Click
    void btn_back() {
        this.finish();
    }

    @Click
    void btn_all_member() {
        FriendsActivity_.intent(this).group(mGroup).showType(FriendsActivity_.TYPE_SHOW_GROUP_MEMBER).start();
    }

    @Click
    void txt_member_add() {
        ChoosePersonActivity_.intent(this).viewType(ChoosePersonActivity_.TYPE_ADD_GROUP_MEMBER).group(mGroup).start();
        RxBus.getInstance().register(ChoosePersonActivity_.RXBUS_GROUP_ADD_MEMBER);
        Observable<Boolean> addGroupMemberCallBackobservable = RxBus.getInstance().register(ChoosePersonActivity_.RXBUS_GROUP_ADD_MEMBER);
        addGroupMemberCallBackobservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean b) {
                        RxBus.getInstance().unregister(ChoosePersonActivity_.RXBUS_GROUP_ADD_MEMBER);
                        getGroupDetails();
                    }
                });

    }

    @Click
    void txt_member_remove() {
        ChoosePersonActivity_.intent(this).viewType(ChoosePersonActivity_.TYPE_DELETE_GROUP_MEMBER).group(mGroup).start();
        RxBus.getInstance().register(ChoosePersonActivity_.RXBUS_GROUP_DELETE_MEMBER);
        Observable<Boolean> deleteGroupMemberCallBackobservable = RxBus.getInstance().register(ChoosePersonActivity_.RXBUS_GROUP_DELETE_MEMBER);
        deleteGroupMemberCallBackobservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean b) {
                        RxBus.getInstance().unregister(ChoosePersonActivity_.RXBUS_GROUP_DELETE_MEMBER);
                        getGroupDetails();
                    }
                });
    }

    @Click
    void tv_exit() {
        exitGroup();
    }

    @AfterViews
    void init() {
        getGroupDetails();
    }

    @UiThread
    void updateUi() {
        if (mGroup == null) {
            return;
        }
        txt_title.setText("群资料设置");
        txt_name.setText(mGroup.name);
        ll_member1.setVisibility(View.GONE);
        ll_member2.setVisibility(View.GONE);
        ll_member3.setVisibility(View.GONE);
        ll_member4.setVisibility(View.GONE);

        for (int i = 0; i < mGroup.members.size(); i++) {
            switch (i) {
                case 0:
                    txt_member1.setText(mGroup.members.get(i).logicNickname);
                    ll_member1.setVisibility(View.VISIBLE);
                    img_member1.setImageURI(mGroup.members.get(i).headImg);
                    break;
                case 1:
                    txt_member2.setText(mGroup.members.get(i).logicNickname);
                    ll_member2.setVisibility(View.VISIBLE);
                    img_member2.setImageURI(mGroup.members.get(i).headImg);
                    break;
                case 2:
                    txt_member3.setText(mGroup.members.get(i).logicNickname);
                    ll_member3.setVisibility(View.VISIBLE);
                    img_member3.setImageURI(mGroup.members.get(i).headImg);
                    break;
                case 3:
                    txt_member4.setText(mGroup.members.get(i).logicNickname);
                    ll_member4.setVisibility(View.VISIBLE);
                    img_member4.setImageURI(mGroup.members.get(i).headImg);
                    break;
            }
        }
        btn_operation.setVisibility(View.GONE);
        txt_count.setText("共" + mGroup.count + "人");
    }

    @Background
    void getGroupDetails() {
        GetGroupDetails.GetGroupDetailsResponse data = HttpHelper_.getInstance_(this).getGroupDetails(groupImId);
        mGroup = data.data;
        updateUi();
    }

    @Background
    void updateGroupName(String name) {
        boolean b = HttpHelper_.getInstance_(this).updateGroupName(mGroup.id, name);
        if (b) {
            mGroup.name = name;
            updateUi();
        } else {
            Toast.makeText(GroupCenterActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
        }
    }

    @Background
    void exitGroup() {
        boolean b = HttpHelper_.getInstance_(this).exitGroup(mGroup.id, AccountManager.getInstance().getUserId());
        if (b) {
            MainActivity_.intent(this).start();
        } else {
            Toast.makeText(GroupCenterActivity.this, "网络异常，退出失败", Toast.LENGTH_SHORT).show();
        }
    }
}
