package com.topunion.chili.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.topunion.chili.R;
import com.topunion.chili.business.AccountManager;
import com.topunion.chili.net.HttpHelper_;
import com.topunion.chili.net.request_interface.GetGroups;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_group_list)
public class GroupListActivity extends AppCompatActivity {

    @ViewById
    TextView txt_title, txt_name;

    @ViewById
    ImageButton btn_operation;

    @ViewById
    ListView mListView;

    private Adapter mAdapter;
    private List<GetGroups.GetGroupsResponse.Group> mDataList;

    @Click
    void btn_operation() {
        ChoosePersonActivity_.intent(this).viewType(ChoosePersonActivity_.TYPE_CREATE_GROUP).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 200) {
            Toast.makeText(this, "result: " + data.getIntArrayExtra("result").toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Click
    void btn_back() {
        this.finish();
    }

    @AfterViews
    void init() {
        txt_title.setText("群组");
        btn_operation.setVisibility(View.VISIBLE);
        btn_operation.setImageResource(R.mipmap.add_friends);

        mDataList = new ArrayList<>();
        mAdapter = new Adapter(mDataList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TalkingActivity_.intent(GroupListActivity.this).title(mAdapter.getItem(i).name).groupId(Long.valueOf(mAdapter.getItem(i).pushGroupId)).start();
            }
        });
        initGroup();
    }

    //获取群组列表
    @Background
    void initGroup() {
        GetGroups.GetGroupsResponse groups = HttpHelper_.getInstance_(this).getGroups(1, 100, AccountManager.getInstance().getUserId());
        if (groups == null) {
            return;
        }
//        //test data
//        GetGroups.GetGroupsResponse.Group group = new GetGroups.GetGroupsResponse.Group();
//        group.count = "20";
//        group.name = "测试";
//        groups.result = new ArrayList<>();
//        groups.result.add(group);
        mDataList = groups.result;
        updateAdapter();
    }

    private void updateAdapter() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.setData(mDataList);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    class Adapter extends BaseAdapter {

        private List<GetGroups.GetGroupsResponse.Group> groupList;

        public Adapter(List<GetGroups.GetGroupsResponse.Group> groupList) {
            this.groupList = groupList;
        }

        @Override
        public int getCount() {
            return (groupList == null) ? 0 : groupList.size();
        }

        @Override
        public GetGroups.GetGroupsResponse.Group getItem(int i) {
            return (groupList == null) ? null : groupList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public void setData(List<GetGroups.GetGroupsResponse.Group> groupList) {
            this.groupList = groupList;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(GroupListActivity.this).inflate(R.layout.group_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.txt_name = (TextView) view.findViewById(R.id.txt_name);
                viewHolder.txt_count = (TextView) view.findViewById(R.id.txt_count);
                viewHolder.img_header = (SimpleDraweeView) view.findViewById(R.id.img_header);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            GetGroups.GetGroupsResponse.Group data = groupList.get(i);

            viewHolder.txt_name.setText(data.name);
            viewHolder.txt_count.setText(data.count + "人");
            return view;
        }

        class ViewHolder {
            TextView txt_name, txt_count;
            SimpleDraweeView img_header;
        }
    }
}
