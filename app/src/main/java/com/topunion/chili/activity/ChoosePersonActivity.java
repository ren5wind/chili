package com.topunion.chili.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.topunion.chili.R;
import com.topunion.chili.base.RxBus;
import com.topunion.chili.business.AccountManager;
import com.topunion.chili.data.SortModel;
import com.topunion.chili.net.HttpHelper_;
import com.topunion.chili.net.request_interface.AddGroup;
import com.topunion.chili.net.request_interface.GetFriends;
import com.topunion.chili.net.request_interface.GetGroupDetails;
import com.topunion.chili.util.StringUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_choose_person)
public class ChoosePersonActivity extends AppCompatActivity {
    private List<SortModel> mDateList;
    //选择的人
    private List<SortModel> mChooseDateList;
    @ViewById
    TextView txt_title;
    @ViewById
    Button btn_confirm;
    @ViewById
    ListView mListView;
    @ViewById
    RelativeLayout search;
    @Extra
    int viewType;
    @Extra
    Serializable group;

    private Adapter mAdapter;
    public final static int TYPE_CREATE_GROUP = 0;
    public final static int TYPE_ADD_GROUP_MEMBER = 1;
    public final static int TYPE_DELETE_GROUP_MEMBER = 2;
    private ProgressDialog progressDialog;
    public final static String RXBUS_GROUP_ADD_MEMBER = "rxbus_group_add_member";
    public final static String RXBUS_GROUP_DELETE_MEMBER = "rxbus_group_delete_member";

    @Click
    void btn_back() {
        this.finish();
    }

    @Click
    void btn_confirm() {
        switch (viewType) {
            case TYPE_CREATE_GROUP:
                progressDialog = ProgressDialog.show(ChoosePersonActivity.this, "提示", "正在创建群组...", true, false);
                createGroup();
                break;
            case TYPE_ADD_GROUP_MEMBER:
                progressDialog = ProgressDialog.show(ChoosePersonActivity.this, "提示", "正在添加群成员...", true, false);
                addMember();
                break;
            case TYPE_DELETE_GROUP_MEMBER:
                progressDialog = ProgressDialog.show(ChoosePersonActivity.this, "提示", "正在删除群成员...", true, false);
                removeMember();
                break;
        }
    }

    @AfterViews
    void init() {
        mDateList = new ArrayList<>();
        mChooseDateList = new ArrayList<>();
        btn_confirm.setVisibility(View.VISIBLE);
        String title = "";
        switch (viewType) {
            case TYPE_CREATE_GROUP:
                title = "选择联系人";
                getFriends();
                break;
            case TYPE_ADD_GROUP_MEMBER:
                title = "添加群成员";
                getFriends();
                break;
            case TYPE_DELETE_GROUP_MEMBER:
                title = "删除群成员";
                showMembers();
                break;
        }
        search.setVisibility(View.GONE);
        txt_title.setText(title);

        mAdapter = new Adapter(this, mDateList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SortModel data = mAdapter.getItem(position);
                data.setChecked(!data.isChecked());
                if (data.isChecked()) {
                    mChooseDateList.add(data);
                } else {
                    mChooseDateList.remove(data);
                }
            }
        });
    }

    private void showMembers() {
        if (StringUtil.isEmpt(AccountManager.getInstance().getUserId())) {
            return;
        }
        mDateList = ((GetGroupDetails.GetGroupDetailsResponse.Group) group).membersTofilledData();
        updateAdapter(true, "");
    }

    @Background
    void addMember() {
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < mChooseDateList.size(); i++) {
            ids.add(mChooseDateList.get(i).getId());
        }
        boolean b = HttpHelper_.getInstance_(this).addGroupMember(((GetGroupDetails.GetGroupDetailsResponse.Group) group).id, ids);
        progressDialog.dismiss();
        if (b) {
            showToast("添加群成员成功");
            RxBus.getInstance().post(RXBUS_GROUP_ADD_MEMBER, true);
            finish();
        } else {
            showToast("网络异常、添加群成员失败");
        }
    }

    @Background
    void removeMember() {
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < mChooseDateList.size(); i++) {
            ids.add(mChooseDateList.get(i).getId());
        }
        boolean b = HttpHelper_.getInstance_(this).removeGroupMember(((GetGroupDetails.GetGroupDetailsResponse.Group) group).id, ids);
        progressDialog.dismiss();
        if (b) {
            showToast("删除群成员成功");
            RxBus.getInstance().post(RXBUS_GROUP_DELETE_MEMBER, true);
            finish();
        } else {
            showToast("网络异常、删除群成员失败");
        }
    }

    @Background
    void getFriends() {
        if (StringUtil.isEmpt(AccountManager.getInstance().getUserId())) {
            return;
        }
        GetFriends.GetFriendsResponse friends = HttpHelper_.getInstance_(this).getFriends(AccountManager.getInstance().getUserId(), 1, 100);
        mDateList = friends.friendsTofilledData();
        updateAdapter(true, "网络异常，获取好友失败");
    }

    @UiThread
    void showToast(String msg) {
        Toast.makeText(ChoosePersonActivity.this, msg, Toast.LENGTH_SHORT).show();

    }

    @UiThread
    void updateAdapter(final boolean isUpdate, final String toast) {
        if (isUpdate) {
            mAdapter.updateListView(mDateList);
        } else {
            Toast.makeText(ChoosePersonActivity.this, toast, Toast.LENGTH_SHORT).show();
        }
    }


    @Background
    void createGroup() {
        JSONArray jsonArray = new JSONArray();
        String groupName = "";
        for (int i = 0; i < mChooseDateList.size(); i++) {
            jsonArray.put(mChooseDateList.get(i).getId());
            //群名称拼接，最多4名成员，最后一个是自己，SO，i = 3
            if (i == 3) {
                continue;
            }
            groupName += mChooseDateList.get(i).getName() + "、";
        }
        groupName += AccountManager.getInstance().getNickName();

        AddGroup.AddGroupResponse data = HttpHelper_.getInstance_(this).
                addGroup(AccountManager.getInstance().getUserId(), groupName, jsonArray);
        progressDialog.dismiss();
        if (data.data != null) {
            showToast("创建群组成功");
            //去群聊
            jumpToGroupTalking(Long.valueOf(data.data.pushGroupId), groupName);
        } else {
            showToast("创建群组失败");
        }
    }


    //跳转群聊界面
    private void jumpToGroupTalking(long groupId, String title) {
        TalkingActivity_.intent(this).groupId(groupId).title(title).start();
    }

    class Adapter extends BaseAdapter {
        private List<SortModel> mList;
        private Context mContext;

        public Adapter(Context mContext, List<SortModel> list) {
            this.mContext = mContext;
            this.mList = list;
        }

        public void updateListView(List<SortModel> list) {
            this.mList = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return (mList == null) ? 0 : mList.size();
        }

        @Override
        public SortModel getItem(int i) {
            return (mList == null) ? null : mList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.choose_person_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.txt_name = (TextView) view.findViewById(R.id.txt_name);
                viewHolder.img_choose = (ImageView) view.findViewById(R.id.img_choose);
                viewHolder.img_header = (SimpleDraweeView) view.findViewById(R.id.img_header);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            SortModel data = mList.get(i);
            if (data.isChecked()) {
                viewHolder.img_choose.setVisibility(View.VISIBLE);
            } else {
                viewHolder.img_choose.setVisibility(View.GONE);
            }
            viewHolder.img_header.setImageURI(data.getIconUrl());
            viewHolder.txt_name.setText(data.getName());

            return view;
        }

        class ViewHolder {
            TextView txt_name;
            ImageView img_choose;
            SimpleDraweeView img_header;
        }
    }
}
