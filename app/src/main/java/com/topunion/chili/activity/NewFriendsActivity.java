package com.topunion.chili.activity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.topunion.chili.R;
import com.topunion.chili.business.AccountManager;
import com.topunion.chili.net.HttpHelper_;
import com.topunion.chili.net.request_interface.GetFriednApplies;
import com.topunion.chili.util.StringUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Author      : renxiaoming
 * Date        : 2017/8/20
 * Description :
 */

@EActivity(R.layout.activity_new_friends)
public class NewFriendsActivity extends BaseAppCompatActivity {
    private MyAdapter adapter;
    private List<GetFriednApplies.GetFriednAppliesResponse.Data.Apply> mDataList; // 数据

    @ViewById
    ListView mListView;

    @Click
    void btn_back() {
        this.finish();
    }

    @AfterViews
    void init() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                appliesRequest(mDataList.get(position));
            }
        });

        dataRequest();
        adapter = new MyAdapter(mDataList);
        mListView.setAdapter(adapter);
    }

    @Background
    void appliesRequest(GetFriednApplies.GetFriednAppliesResponse.Data.Apply data) {
        boolean b = HttpHelper_.getInstance_(this).updateETFriendApplyAgree(data.id);
        if (b) {
            data.agree = true;
        }
        updateAdapter();
    }

    @Background
    void dataRequest() {
        if (StringUtil.isEmpt(AccountManager.getInstance().getUserId())) {
            return;
        }
        GetFriednApplies.GetFriednAppliesResponse friends = HttpHelper_.getInstance_(this).getFriednApplies(AccountManager.getInstance().getUserId(), 1, 100);
        mDataList = friends.data.result;
        updateAdapter();
    }

    void updateAdapter() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.setData(mDataList);
                adapter.notifyDataSetChanged();
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        private List<GetFriednApplies.GetFriednAppliesResponse.Data.Apply> dataList;

        public MyAdapter(List<GetFriednApplies.GetFriednAppliesResponse.Data.Apply> dataList) {
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            return (dataList == null) ? 0 : dataList.size();
        }

        @Override
        public GetFriednApplies.GetFriednAppliesResponse.Data.Apply getItem(int i) {
            return (dataList == null) ? null : dataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public void setData(List<GetFriednApplies.GetFriednAppliesResponse.Data.Apply> dataList) {
            this.dataList = dataList;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(NewFriendsActivity.this).inflate(R.layout.item_new_friends, null);
                viewHolder = new ViewHolder();
                viewHolder.txt_name = (TextView) view.findViewById(R.id.txt_name);
                viewHolder.txt_sub_name = (TextView) view.findViewById(R.id.txt_sub_name);
                viewHolder.btn_add = (TextView) view.findViewById(R.id.btn_add);
                viewHolder.img_header = (SimpleDraweeView) view.findViewById(R.id.img_header);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            GetFriednApplies.GetFriednAppliesResponse.Data.Apply data = dataList.get(i);

            viewHolder.txt_name.setText(data.senderNickname);
            viewHolder.txt_sub_name.setText("我是" + data.senderFriendNickName);
            viewHolder.img_header.setImageURI(data.headImgSender);
            if (data.agree) {
                viewHolder.btn_add.setText("已接受");
            } else {
                viewHolder.btn_add.setText("接受");
            }
            return view;
        }

        class ViewHolder {
            TextView txt_name, txt_sub_name, btn_add;
            SimpleDraweeView img_header;
        }
    }
}