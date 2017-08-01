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
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.topunion.chili.MyApplication;
import com.topunion.chili.MyApplication_;
import com.topunion.chili.R;
import com.topunion.chili.data.UserEntry;
import com.topunion.chili.net.HttpHelper;
import com.topunion.chili.net.HttpHelper_;
import com.topunion.chili.net.request_interface.GetCorpDepts;
import com.topunion.chili.net.request_interface.GetCorps;
import com.topunion.chili.util.SortConvList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.HTTP;

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

    private List<Conversation> mMsgDatas = new ArrayList<Conversation>();


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
        ArrayList<Object> data = new ArrayList<>();
        data.add("好友");
        data.add("群组");
        data.add("电话本");
        GetCorps.GetCorpsResponse corps = HttpHelper_.getInstance_(getActivity()).getCorps("17070500000003");
        if (corps.data.result.size() != 0) {
            for (int i = 0; i < corps.data.result.size(); i++) {
                data.add(corps.data.result.get(i));
                GetCorpDepts.GetCorpDeptsResponse depts = HttpHelper_.getInstance_(getActivity()).getCorpDepts(1, 20, Integer.parseInt(corps.data.result.get(0).id));
                data.addAll(depts.data.result);
            }
        }
        contactAdapter.setData(data);
        this.validList();
    }

    @Background
    void initIm() {
        //检测账号是否登陆
//        UserInfo myInfo = JMessageClient.getMyInfo();
//        if (myInfo == null) {//去登陆
        JMessageClient.login("17070500000003", "YiTou123", new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage) {
                if (responseCode == 0) {
//                        //获取user
//                        UserInfo myInfo = JMessageClient.getMyInfo();
//                        File avatarFile = myInfo.getAvatarFile();
//                        String username = myInfo.getUserName();
//                        String appKey = myInfo.getAppKey();
//                        UserEntry user = UserEntry.getUser(username, appKey);
//                        if (null == user) {
//                            user = new UserEntry(username, appKey);
//                            user.save();
//                        }
                } else {

                }
            }
        });
//        }
    }

    @MainThread
    void validList() {
//        contactAdapter.notifyDataSetChanged();
    }

    //得到会话列表
    private void initConvListAdapter() {
        mDatas = JMessageClient.getConversationList();
        if (mDatas != null && mDatas.size() > 0) {
            SortConvList sortConvList = new SortConvList();
            Collections.sort(mDatas, sortConvList);
        }
        msgAdapter = new MsgAdapter();
        msg_list.setAdapter(msgAdapter);
    }

    @AfterViews
    protected void afterViews() {
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
            }
        });

        initCorps();
        initIm();
    }

    class MsgAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return (mMsgDatas == null || mMsgDatas.size() == 0) ? 0 : mMsgDatas.size();
        }

        @Override
        public Object getItem(int i) {
            return (mMsgDatas == null || mMsgDatas.size() == 0) ? 0 : mMsgDatas.get(i);
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
                    if (getItem(i) instanceof GetCorps.GetCorpsResponse.Data.Corp) { //公司
                        final GetCorps.GetCorpsResponse.Data.Corp corp = (GetCorps.GetCorpsResponse.Data.Corp) getItem(i);
                        view = LayoutInflater.from(getActivity()).inflate(R.layout.company_list_item, null);
                        txt_name = (TextView) view.findViewById(R.id.txt_name);
                        txt_name.setText(corp.name);
                        Button btn_manage = (Button) view.findViewById(R.id.btn_manage);
                        btn_manage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CompanyManageActivity_.intent(getActivity()).companyId(corp.id).start();
                            }
                        });
                    } else if (getItem(i) instanceof GetCorpDepts.GetCorpDeptsResponse.Data.Dept) { //部门
                        final GetCorpDepts.GetCorpDeptsResponse.Data.Dept dept = (GetCorpDepts.GetCorpDeptsResponse.Data.Dept) getItem(i);
                        view = LayoutInflater.from(getActivity()).inflate(R.layout.department_list_item, null);
                        txt_name = (TextView) view.findViewById(R.id.txt_name);
                        txt_name.setText(dept.name);
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
