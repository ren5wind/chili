package com.topunion.chili.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.topunion.chili.R;
import com.topunion.chili.business.AccountManager;
import com.topunion.chili.data.Company;
import com.topunion.chili.data.Employee;
import com.topunion.chili.net.HttpHelper_;
import com.topunion.chili.net.request_interface.GetFriends;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_search_from_contact)
public class SearchFromFriendsActivity extends AppCompatActivity {

    @ViewById
    ListView mListView;

    @ViewById
    TextView txt_title;

    @ViewById
    Button btn_confirm;

    @ViewById
    ImageButton btn_operation;

    private List<Employee> chooseEmployee;

    @Extra
    String companyId;
    @Extra
    Company company;


    private List<GetFriends.GetFriendsResponse.Friend> mDataList;
    private MyAdapter myAdapter;

    @Click
    void btn_confirm() {
        this.finish();
//        Intent intent = new Intent(this, CompanyManageActivity.class);
//        intent.putExtra("employees", (Serializable) chooseEmployee);
//        intent.putExtra("role", role);
//        intent.putExtra("company", company);
//        setResult(RESULT_OK, intent);

        Intent intent = new Intent(this, CompanyManageAddEmployessActivity_.class);
        intent.putExtra("employees", (Serializable) chooseEmployee);
        intent.putExtra("company", company);
        startActivity(intent);
    }

    //    {
//        this.finish();
//        Intent intent = new Intent(this, CompanyManageActivity_.class);
//        List<String> chooseEmployeeStr = new ArrayList<>();
//        for (int i = 0; i < chooseEmployee.size(); i++) {
//            String value = chooseEmployee.get(i).getId() + "," + chooseEmployee.get(i).getName();
//            chooseEmployeeStr.add(value);
//        }
//
//        intent.putStringArrayListExtra("employees", (ArrayList<String>) chooseEmployeeStr);
//        intent.putExtra("role", role);
//        intent.putExtra("companyId", companyId);
//        intent.putExtra("deparmentId", deparmentId);
//        setResult(RESULT_OK, intent);
//    }
    @Click
    void btn_back() {
        this.finish();
    }

    @AfterViews
    void init() {
        btn_operation.setVisibility(View.GONE);
        btn_confirm.setVisibility(View.VISIBLE);
        txt_title.setText("通过易友添加");
        myAdapter = new MyAdapter(mDataList);
        mListView.setAdapter(myAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MyAdapter.ViewHolder viewHolder = (MyAdapter.ViewHolder) view.getTag();
                if (viewHolder.img_choose.getVisibility() == View.GONE) {
                    viewHolder.img_choose.setVisibility(View.VISIBLE);
                    Employee employee = new Employee();
                    employee.setId(myAdapter.getItem(i).friendId);
                    employee.setName(myAdapter.getItem(i).nickname);
                    chooseEmployee.add(employee);
                } else if (viewHolder.img_choose.getVisibility() == View.VISIBLE) {
                    viewHolder.img_choose.setVisibility(View.GONE);
                    chooseEmployee.remove(myAdapter.getItem(i).friendId);
                }
            }
        });
        GetFriends();
        chooseEmployee = new ArrayList<>();
    }

    @Background
    void GetFriends() {
        GetFriends.GetFriendsResponse friends = HttpHelper_.getInstance_(this).getFriends(AccountManager.getInstance().getUserId(), 1, 100);
        if (friends == null) {
            return;
        }
        mDataList = friends.result;
        updateAdapter();
    }

    void updateAdapter() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myAdapter.setData(mDataList);
                myAdapter.notifyDataSetChanged();
            }
        });
    }

    class MyAdapter extends BaseAdapter {
        private List<GetFriends.GetFriendsResponse.Friend> dataList;

        MyAdapter(List<GetFriends.GetFriendsResponse.Friend> dataList) {
            this.dataList = dataList;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public void setData(List<GetFriends.GetFriendsResponse.Friend> dataList) {
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            return (dataList == null) ? 0 : dataList.size();
        }

        @Override
        public GetFriends.GetFriendsResponse.Friend getItem(int i) {
            return (dataList == null) ? null : dataList.get(i);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(SearchFromFriendsActivity.this).inflate(R.layout.person_detail_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.txt_name = (TextView) view.findViewById(R.id.txt_name);
                viewHolder.txt_nickname = (TextView) view.findViewById(R.id.txt_nickname);
                viewHolder.txt_phone = (TextView) view.findViewById(R.id.txt_phone);
                viewHolder.txt_phone.setVisibility(View.GONE);
                viewHolder.img_header = (SimpleDraweeView) view.findViewById(R.id.img_header);
                viewHolder.img_choose = (ImageView) view.findViewById(R.id.img_choose);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            GetFriends.GetFriendsResponse.Friend data = dataList.get(i);
            viewHolder.txt_name.setText(data.nickname);
            viewHolder.img_header.setImageURI(data.headImg);
//            switch (i) {
//                case 0:
//                    viewHolder.txt_name.setText("张三");
//                    break;
//                case 1:
//                    viewHolder.txt_name.setText("李四");
//                    break;
//                case 2:
//                    viewHolder.txt_name.setText("王五");
//                    break;
//                case 3:
//                    viewHolder.txt_name.setText("赵六");
//                    break;
//            }
            return view;
        }

        class ViewHolder {
            TextView txt_name;
            TextView txt_nickname;
            TextView txt_phone;
            SimpleDraweeView img_header;
            ImageView img_choose;
        }
    }

}
