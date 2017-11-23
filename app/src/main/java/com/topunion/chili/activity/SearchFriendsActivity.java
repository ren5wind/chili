package com.topunion.chili.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.topunion.chili.R;
import com.topunion.chili.business.AccountManager;
import com.topunion.chili.data.Company;
import com.topunion.chili.data.Employee;
import com.topunion.chili.net.HttpHelper_;
import com.topunion.chili.net.request_interface.GetFriends;
import com.topunion.chili.net.request_interface.GetUsers;
import com.topunion.chili.wight.refresh.UiLibRefreshLayout;
import com.topunion.chili.wight.refresh.UiLibRefreshOnLoadMoreListener;
import com.topunion.chili.wight.refresh.UiLibRefreshOnRefreshListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_search_from_manual)
public class SearchFriendsActivity extends BaseAppCompatActivity {
    @ViewById
    ListView mListView;

    @ViewById
    EditText mSearchInput;

    private MyAdapter mAdapter;
    @Extra
    Company company;
    @Extra
    String deparmentId;
    @ViewById
    UiLibRefreshLayout refresh;
    private List<GetUsers.GetUsersResponse.Data.User> mDataList;
    int page = 1;

    @Click
    void btn_cancel() {
        search(1);
    }

    @AfterViews
    void init() {
        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                PersonalCenterActivity_.intent(SearchFriendsActivity.this).
                        uid(mAdapter.getItem(i).cId).start();
            }
        });

        refresh.setOnRefreshListener(new UiLibRefreshOnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(false);
                search(1);
            }
        });

        refresh.setOnLoadMoreListener(new UiLibRefreshOnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                refresh.setLoadingMore(false);
                search(page);
            }
        });
    }

    @Background
    void search(int page) {
//        GetFriends.GetFriendsResponse friends = HttpHelper_.getInstance_(this).getFriends(AccountManager.getInstance().getUserId(), page, 20);
//        if (friends != null)
//            mDataList = friends.result;

        GetUsers.GetUsersResponse result = HttpHelper_.getInstance_(this).getUsers(page, 20, mSearchInput.getText().toString().trim());
        mDataList = result.data.result;


        updateAdapter();
    }

    void updateAdapter() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.setData(mDataList);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    class MyAdapter extends BaseAdapter {
        private List<GetUsers.GetUsersResponse.Data.User> dataList;

        public void setData(List<GetUsers.GetUsersResponse.Data.User> dataList) {
            this.dataList = dataList;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(SearchFriendsActivity.this).inflate(R.layout.person_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.txt_name = (TextView) view.findViewById(R.id.txt_name);
                viewHolder.img_header = (SimpleDraweeView) view.findViewById(R.id.img_header);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            GetUsers.GetUsersResponse.Data.User friend = dataList.get(i);
            viewHolder.img_header.setImageURI(friend.imgUrl);
            viewHolder.txt_name.setText(friend.logicNickname);

            return view;
        }


        @Override
        public int getCount() {
            return (dataList == null) ? 0 : dataList.size();
        }

        @Override
        public GetUsers.GetUsersResponse.Data.User getItem(int i) {
            return (dataList == null) ? null : dataList.get(i);
        }

        class ViewHolder {
            TextView txt_name;
            SimpleDraweeView img_header;
        }
    }

}
