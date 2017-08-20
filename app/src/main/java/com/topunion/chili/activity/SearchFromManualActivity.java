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
import com.topunion.chili.net.HttpHelper_;
import com.topunion.chili.net.request_interface.GetUsers;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_search_from_manual)
public class SearchFromManualActivity extends AppCompatActivity {

    public static final int TYPE_SEARCH = 1;
    public static final int TYPE_NORMAL = 0;
    @Extra
    int viewType;
    @ViewById
    ListView mListView;

    @ViewById
    EditText mSearchInput;

    private MyAdapter mAdapter;

    private List<GetUsers.GetUsersResponse.Data.User> mDataList;

    @Click
    void btn_cancel() {
        search();
    }

    @AfterViews
    void init() {
        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (viewType == TYPE_NORMAL) {
                    setResult(i);
                    finish();
                } else if (viewType == TYPE_SEARCH) {
                    PersonalCenterActivity_.intent(SearchFromManualActivity.this).
                            uid(mAdapter.getItem(i).cId).start();
                }
            }
        });
    }

    @Background
    void search() {
        GetUsers.GetUsersResponse result = HttpHelper_.getInstance_(this).getUsers(1, 100, mSearchInput.getText().toString().trim());
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
                view = LayoutInflater.from(SearchFromManualActivity.this).inflate(R.layout.person_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.txt_name = (TextView) view.findViewById(R.id.txt_name);
                viewHolder.img_header = (SimpleDraweeView) view.findViewById(R.id.img_header);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            GetUsers.GetUsersResponse.Data.User user = dataList.get(i);
            viewHolder.img_header.setImageURI(user.imgUrl);
            viewHolder.txt_name.setText(user.logicNickname);

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
