package com.topunion.chili.activity;


import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.MainThread;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.topunion.chili.MyApplication;
import com.topunion.chili.R;
import com.topunion.chili.business.AccountManager;
import com.topunion.chili.data.Company;
import com.topunion.chili.data.Department;
import com.topunion.chili.data.Organization;
import com.topunion.chili.net.HttpHelper_;
import com.topunion.chili.net.request_interface.GetCorpDepts;
import com.topunion.chili.net.request_interface.GetCorpOrDeptUsers;
import com.topunion.chili.net.request_interface.GetCorps;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_message_main)
public class MessageMainFragment extends Fragment {

    @App
    MyApplication application;

    @FragmentArg
    String mParam1;
    @FragmentArg
    String mParam2;

    @ViewById
    Button btn_msg, btn_contact, btn_add_friend, btn_create_group;

    @ViewById
    View bottom_msg, bottom_contact;

    @ViewById
    ListView msg_list, contact_list;

    @ViewById
    TextView mSearchInput;

    @ViewById
    LinearLayout popMenu;

    private Organization mOrganization;
    private ArrayList<Object> dataList;

    @Click
    void btn_add_friend() {
        popMenu.setVisibility(View.GONE);
        //TODO
    }

    @Click
    void btn_create_group() {
        popMenu.setVisibility(View.GONE);
        //TODO
    }

    @Click
    void btn_msg() {
        btn_msg.setTextColor(getResources().getColor(R.color.main));
        bottom_msg.setBackgroundColor(getResources().getColor(R.color.main));
        btn_contact.setTextColor(getResources().getColor(R.color.textGray));
        bottom_contact.setBackgroundColor(Color.TRANSPARENT);
        msg_list.setVisibility(View.VISIBLE);
        contact_list.setVisibility(View.GONE);
        popMenu.setVisibility(View.GONE);
        //TODO
    }

    @Click
    void btn_contact() {
        btn_msg.setTextColor(getResources().getColor(R.color.textGray));
        bottom_msg.setBackgroundColor(Color.TRANSPARENT);
        btn_contact.setTextColor(getResources().getColor(R.color.main));
        bottom_contact.setBackgroundColor(getResources().getColor(R.color.main));
        msg_list.setVisibility(View.GONE);
        contact_list.setVisibility(View.VISIBLE);
        popMenu.setVisibility(View.GONE);
        //TODO
    }

    @Click
    void btn_menu() {
        popMenu.setVisibility(popMenu.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    MsgAdapter msgAdapter;
    ContactAdapter contactAdapter;

    @Background
    void initCorps() {
        //获取组织结构
        //获取企业列表
        GetCorps.GetCorpsResponse corps = HttpHelper_.getInstance_(getActivity()).getCorps(AccountManager.getInstance().getUserId());
        mOrganization = new Organization();
        List<Company> companyList = mOrganization.analysisCompany(corps);
        List<Department> departmentList = null;
        //获取部门列表

        for (int i = 0; companyList != null && i < companyList.size(); i++) {
            dataList.add(companyList.get(i));
            GetCorpDepts.GetCorpDeptsResponse depts = HttpHelper_.getInstance_(getActivity()).getCorpDepts(1, 20, Integer.parseInt(companyList.get(i).getId()));
            departmentList = companyList.get(i).analysisDepartment(depts);
            dataList.addAll(departmentList);
        }

        contactAdapter.setData(dataList);
        this.validList();
        //获取人员列表
        for (int j = 0; departmentList != null && j < departmentList.size(); j++) {
            GetCorpOrDeptUsers.GetCorpOrDeptUsersResponse deptNumbs = HttpHelper_.getInstance_(getActivity()).
                    getDeptUsers(1, 20, Integer.parseInt(departmentList.get(j).getId()), departmentList.get(j).getName());
            departmentList.get(j).analysisEmployee(deptNumbs);
        }
    }

    @MainThread
    void validList() {
//        contactAdapter.notifyDataSetChanged();
    }


    @AfterViews
    protected void afterViews() {
        msgAdapter = new MsgAdapter();
        contactAdapter = new ContactAdapter();
        contact_list.setAdapter(contactAdapter);
        msg_list.setAdapter(msgAdapter);
        mSearchInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchFromManualActivity_.intent(getActivity()).start();
            }
        });

        msg_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popMenu.setVisibility(View.GONE);
                return false;
            }
        });
        contact_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popMenu.setVisibility(View.GONE);
                return false;
            }
        });
        msg_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i) {
                    case 0:
                        startActivity(new Intent(getActivity(), NotificationActivity_.class));
                        break;
                    default:
                        startActivity(new Intent(getContext(), TalkingActivity_.class));
                        break;
                }
            }
        });
        contact_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0://我的好友
                        FriendsActivity_.intent(getActivity()).start();
                        break;
                    case 1://群组
                        GroupListActivity_.intent(getActivity()).start();
                        break;
                    case 2://电话本
                        InviteContactActivity_.intent(getActivity()).start();
                        break;
                }

                if (contactAdapter.getItem(i) instanceof Department) {//部门
                    Department dept = (Department) contactAdapter.getItem(i);
                    FriendsActivity_.intent(getActivity()).deptId(Integer.parseInt(dept.getId()))
                            .deptName(dept.getName()).showType(FriendsActivity_.TYPE_SHOW_DEPT_NUMBERS).start();
                }
            }
        });
        dataList = new ArrayList<>();
        dataList.add("好友");
        dataList.add("群组");
        dataList.add("电话本");
        contactAdapter.setData(dataList);

        initCorps();
    }

    class MsgAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.message_list_item, null);
                holder = new ViewHolder();
                holder.img_header = (SimpleDraweeView) view.findViewById(R.id.img_header);
                holder.txt_name = (TextView) view.findViewById(R.id.txt_name);
                holder.txt_msg = (TextView) view.findViewById(R.id.txt_msg);
                holder.txt_time = (TextView) view.findViewById(R.id.txt_time);
//                Uri uri = Uri.parse("https://raw.githubusercontent.com/facebook/fresco/gh-pages/static/logo.png");
//                holder.img_header.setImageURI(uri);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            switch (i) {
                case 0:
                    holder.txt_name.setText("易投通知");
                    holder.txt_msg.setText("有人回答了你的问题");
                    holder.txt_time.setText("08:45");
                    break;
                case 1:
                    holder.txt_name.setText("张三");
                    holder.txt_msg.setText("吃饭了吗？");
                    holder.txt_time.setText("18:11");
                    break;

            }

            return view;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        class ViewHolder {
            SimpleDraweeView img_header;
            TextView txt_name;
            TextView txt_msg;
            TextView txt_time;
        }
    }


    class ContactAdapter extends BaseAdapter {

        private ArrayList<Object> data = new ArrayList<>();

        public void setData(ArrayList<Object> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            SimpleDraweeView img_header;
            TextView txt_name;
            switch (i) {
                case 0:
                    view = LayoutInflater.from(getActivity()).inflate(R.layout.person_list_item, null);
                    img_header = (SimpleDraweeView) view.findViewById(R.id.img_header);
                    img_header.setImageResource(R.mipmap.friends);
                    txt_name = (TextView) view.findViewById(R.id.txt_name);
                    txt_name.setText("好友");
                    break;
                case 1:
                    view = LayoutInflater.from(getActivity()).inflate(R.layout.person_list_item, null);
                    img_header = (SimpleDraweeView) view.findViewById(R.id.img_header);
                    img_header.setImageResource(R.mipmap.group);
                    txt_name = (TextView) view.findViewById(R.id.txt_name);
                    txt_name.setText("群组");
                    break;
                case 2:
                    view = LayoutInflater.from(getActivity()).inflate(R.layout.person_list_item, null);
                    img_header = (SimpleDraweeView) view.findViewById(R.id.img_header);
                    img_header.setImageResource(R.mipmap.tel_friends);
                    txt_name = (TextView) view.findViewById(R.id.txt_name);
                    txt_name.setText("电话本");
                    break;

                default:
                    if (getItem(i) instanceof Company) { //公司
                        final Company company = (Company) getItem(i);
                        view = LayoutInflater.from(getActivity()).inflate(R.layout.company_list_item, null);
                        txt_name = (TextView) view.findViewById(R.id.txt_name);
                        txt_name.setText(company.getName());
                        Button btn_manage = (Button) view.findViewById(R.id.btn_manage);
                        btn_manage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CompanyManageActivity_.intent(getActivity()).organization(mOrganization).start();
                            }
                        });
                    } else if (getItem(i) instanceof Department) { //部门
                        final Department dept = (Department) getItem(i);
                        view = LayoutInflater.from(getActivity()).inflate(R.layout.department_list_item, null);
                        txt_name = (TextView) view.findViewById(R.id.txt_name);
                        txt_name.setText(dept.getName());
                    }
                    break;
            }

            return view;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }
    }

}
