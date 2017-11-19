package com.topunion.chili.activity;


import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.topunion.chili.MyApplication;
import com.topunion.chili.MyReceiver;
import com.topunion.chili.R;
import com.topunion.chili.base.RxBus;
import com.topunion.chili.business.AccountManager;
import com.topunion.chili.data.Company;
import com.topunion.chili.data.Department;
import com.topunion.chili.data.Notifiy;
import com.topunion.chili.data.Organization;
import com.topunion.chili.greendao.GreenDaoManager;
import com.topunion.chili.net.HttpHelper_;
import com.topunion.chili.net.request_interface.GetCorpDepts;
import com.topunion.chili.net.request_interface.GetCorpOrDeptUsers;
import com.topunion.chili.net.request_interface.GetCorps;
import com.topunion.chili.net.request_interface.GetETMemberDetails;
import com.topunion.chili.net.request_interface.GetGroupDetails;
import com.topunion.chili.util.SortConvList;
import com.topunion.chili.util.StringUtil;
import com.topunion.chili.util.TimeFormat;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import static cn.jpush.im.android.api.enums.ContentType.eventNotification;

@EFragment(R.layout.fragment_message_main)
public class MessageMainFragment extends Fragment {
    public static int sequence = 1;
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
    TextView mSearchInput, txt_notifiy_name, txt_notifiy_msg, txt_notifiy_time;

    @ViewById
    LinearLayout popMenu;

    @ViewById
    RelativeLayout rl_notifiy;

    @ViewById
    TextView btn_imlogin;

    private Organization mOrganization;
    private List<Object> dataList;
    private List<Conversation> msgList;
    private MsgAdapter msgAdapter;
    private ContactAdapter contactAdapter;

    @Click
    void btn_add_friend() {
        popMenu.setVisibility(View.GONE);
        SearchFromManualActivity_.intent(getActivity()).viewType(SearchFromManualActivity_.TYPE_SEARCH).start();
    }

    @Click
    void btn_create_group() {
        popMenu.setVisibility(View.GONE);
        ChoosePersonActivity_.intent(this).viewType(ChoosePersonActivity_.TYPE_CREATE_GROUP).start();
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
        rl_notifiy.setVisibility(View.VISIBLE);
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
        rl_notifiy.setVisibility(View.GONE);
    }

    @Click
    void btn_menu() {
        popMenu.setVisibility(popMenu.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    @Click
    void rl_notifiy() {
        NotificationActivity_.intent(this).start();
    }

    @Click
    void btn_imlogin() {
        btn_imlogin.setText("正在登陆......");
        loginIm();
    }

    @UiThread
    void updateImLogin(boolean isLogin, int code, boolean hasUserId) {
        if (!hasUserId) {
//            btn_imlogin.setText("您还没有登录，无法获取消息,请到“我的”里登录");
//            btn_imlogin.setVisibility(View.VISIBLE);
//            LoginActivity_.intent(this).start();
            return;
        }
        if (isLogin) {
            btn_imlogin.setVisibility(View.GONE);
        } else {
            btn_imlogin.setText("登陆失败,错误码" + code + "，请点击重试或联系客服");
            btn_imlogin.setVisibility(View.VISIBLE);
        }
    }

    @Background
    void initCorps() {
        dataList = null;
        dataList = new ArrayList<>();
        //获取部门列表
        dataList.add("易投好友");
        dataList.add("群组");
        dataList.add("电话本");
        contactAdapter.setData(dataList);

        //获取企业列表
        GetCorps.GetCorpsResponse corps = HttpHelper_.getInstance_(getActivity()).getCorps(AccountManager.getInstance().getUserId());
        mOrganization = null;
        mOrganization = new Organization();
        List<Company> companyList = mOrganization.analysisCompany(corps);
        List<Department> departmentList = null;
        for (int i = 0; companyList != null && i < companyList.size(); i++) {
//            if (!companyList.get(i).isAdministrators())
//                continue;
            dataList.add(companyList.get(i));
            GetCorpDepts.GetCorpDeptsResponse depts = HttpHelper_.getInstance_(getActivity()).getCorpDepts(1, 20, Integer.parseInt(companyList.get(i).getId()));
            departmentList = companyList.get(i).analysisDepartment(depts);
            dataList.addAll(departmentList);
        }

        contactAdapter.setData(dataList);
        validList();
        //获取人员列表
        for (int j = 0; departmentList != null && j < departmentList.size(); j++) {
            GetCorpOrDeptUsers.GetCorpOrDeptUsersResponse deptNumbs = HttpHelper_.getInstance_(getActivity()).
                    getDeptUsers(1, 20, Integer.parseInt(departmentList.get(j).getId()), departmentList.get(j).getName());
            departmentList.get(j).analysisEmployee(deptNumbs);
        }
    }

    @Background
    void loginIm() {
        if (StringUtil.isEmpt(AccountManager.getInstance().getUserId())) {
            updateImLogin(true, 0, false);
            return;
        }
        //设置 Alias
        sequence++;
        JPushInterface.setAlias(MyApplication.getContext(), sequence, AccountManager.getInstance().getUserId());
        JMessageClient.login(AccountManager.getInstance().getUserId(), "YiTou123", new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage) {
                if (responseCode == 0) {
                    updateImLogin(true, responseCode, true);
                    if (msgList != null) {
                        msgList.clear();
                    }
                    initConvListAdapter();
                } else {
                    updateImLogin(false, responseCode, true);
                }
            }
        });
        JMessageClient.registerEventReceiver(this);
    }

    public void onEventMainThread(MessageEvent event) {
        initConvListAdapter();
    }

    @UiThread
    void showToast(String msg) {
        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @UiThread
    void validList() {
        contactAdapter.notifyDataSetChanged();
    }


//    @Background
//    void getGroup() {
//        String uid = AccountManager.getInstance().getUserId();
//        if (!StringUtil.isEmpt(uid)) {
//            GetGroups.GetGroupsResponse response = HttpHelper_.getInstance_(getActivity()).getGroups(1, 1000, uid);
//            ImInfoManager.getInstance().addGroupList(response.result);
//        }
//    }
//
//    @Background
//    void getFriend() {
//        String uid = AccountManager.getInstance().getUserId();
//        if (!StringUtil.isEmpt(uid)) {
//            GetFriends.GetFriendsResponse response = HttpHelper_.getInstance_(getActivity()).getFriends(uid, 1, 1000);
//            ImInfoManager.getInstance().addFriendList(response.result);
//        }
//    }


    //得到会话列表
    private void initConvListAdapter() {
        if (msgList != null) {
            msgList.clear();
        }
        if (StringUtil.isEmpt(AccountManager.getInstance().getUserId())) {
            return;
        }
        List<Conversation> tempMsgList = JMessageClient.getConversationList();
        if (tempMsgList != null && tempMsgList.size() > 0) {
            for (int i = 0; i < tempMsgList.size(); i++) {
                Conversation conversation = tempMsgList.get(i);
                if (conversation == null) {
                    continue;
                }
                Message message = conversation.getLatestMessage();
                if (message == null) {
                    continue;
                }
                ContentType contentType = message.getContentType();
                if (contentType == null) {
                    continue;
                }
                if (contentType == ContentType.text || contentType == eventNotification) {
                    msgList.add(conversation);
                }
            }
            SortConvList sortConvList = new SortConvList();
            Collections.sort(msgList, sortConvList);
        } else {
//            mConvListView.setNullConversation(false);
        }
        msgAdapter = new MsgAdapter(msgList);
        msg_list.setAdapter(msgAdapter);
    }

    /**
     * 收到消息后将会话置顶
     *
     * @param conv 要置顶的会话
     */
    public void setToTop(Conversation conv) {
        for (Conversation conversation : msgList) {
            if (conv.getId().equals(conversation.getId())) {
                msgList.remove(conversation);
                msgList.add(0, conv);
                return;
            }
        }
        //如果是新的会话
        msgList.add(0, conv);
    }

    private void pushReceiverDateChange(Notifiy data) {
        txt_notifiy_name.setText(data.title);
        txt_notifiy_msg.setText(data.msg);
        txt_notifiy_time.setText(data.time + "");
    }

    @AfterViews
    protected void afterViews() {
        msgList = new ArrayList<>();
        msgAdapter = new MsgAdapter(msgList);
        contactAdapter = new ContactAdapter();
        contact_list.setAdapter(contactAdapter);
        msg_list.setAdapter(msgAdapter);
        mSearchInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchFriendsActivity_.intent(getActivity()).start();
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
                if (msgAdapter.getItem(i).getTargetInfo() instanceof UserInfo) {
                    String tagerId = ((UserInfo) msgAdapter.getItem(i).getTargetInfo()).getUserName();
                    TalkingActivity_.intent(getActivity()).targetId(tagerId).title(((MsgAdapter.ViewHolder) view.getTag()).txt_name.getText().toString().trim()).start();
                } else {
                    long groupId = ((GroupInfo) msgAdapter.getItem(i).getTargetInfo()).getGroupID();
                    TalkingActivity_.intent(getActivity()).groupId(groupId).title(((MsgAdapter.ViewHolder) view.getTag()).txt_name.getText().toString().trim()).start();
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

        initCorps();
        loginIm();
//        getGroup();
//        getFriend();
        //监听登录
        Observable<Boolean> loginCallBackobservable = RxBus.getInstance().register(AccountManager.RXBUS_ACCOUNT_LOGIN);
        loginCallBackobservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean b) {
                        initCorps();
                        loginIm();
                    }
                });
        //监听push
        Observable<Notifiy> pushCallBackobservable = RxBus.getInstance().register(MyReceiver.RXBUS_PUSH);
        pushCallBackobservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Notifiy>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Notifiy data) {
                        pushReceiverDateChange(data);
                    }
                });
        //监听企业管理
        Observable<Boolean> companyCallBackobservable = RxBus.getInstance().register(CompanyManageActivity_.RXBUS_UPDATE_COMPANY);
        companyCallBackobservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean b) {
                        initCorps();
                    }
                });
        //监听登出
        RxBus.getInstance().register(AccountManager.RXBUS_ACCOUNT_LOGOUT);
        Observable<Boolean> logOutCallBackobservable = RxBus.getInstance().register(AccountManager.RXBUS_ACCOUNT_LOGOUT);
        logOutCallBackobservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean b) {
                        initCorps();
                        loginIm();
                    }
                });
    }

    @Background
    void getGroup(GroupInfo groupInfo, final TextView txt_name) {
        if (groupInfo == null || txt_name == null) {
            return;
        }
        final GetGroupDetails.GetGroupDetailsResponse response =
                HttpHelper_.getInstance_(getActivity()).getGroupDetails(groupInfo.getGroupID());
        if (response == null || response.data == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txt_name.setText(response.data.name);
            }
        });
    }

    @Background
    void getETMember(UserInfo userInfo, final SimpleDraweeView img_header, final TextView txt_name) {
        if (userInfo == null || img_header == null || txt_name == null) {
            return;
        }
        final GetETMemberDetails.GetETMemberDetailsResponse response =
                HttpHelper_.getInstance_(getActivity()).getETMemberDetails(userInfo.getUserName(), AccountManager.getInstance().getUserId());
        if (response != null && response.data != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    img_header.setImageURI(response.data.headImg);
                    txt_name.setText(response.data.logicNickname);
                }
            });
        }
    }

    @Background
    void getETMember(final String msg, final TextView txt_msg) {

        List<String> con = StringUtil.extractMessageByRegular(msg);
        String userId = "";
        if (con != null && con.size() > 0) {
            userId = con.get(0);
//            userId.replace("[", "");
//            userId.replace("]", "");
        }

        if (StringUtil.isEmpt(userId)) {
            return;
        }
        String[] userIdStr = userId.split(",");
        String message = msg;
        for (int i = 0; i < userIdStr.length; i++) {
            final GetETMemberDetails.GetETMemberDetailsResponse response =
                    HttpHelper_.getInstance_(getActivity()).getETMemberDetails(userIdStr[i], AccountManager.getInstance().getUserId());
            if (response != null && response.data != null) {
                message = message.replace(userIdStr[i], response.data.logicNickname);
            }
        }
        final String finalMessage = message;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txt_msg.setText(finalMessage);
            }
        });
    }


    class MsgAdapter extends BaseAdapter {
        private List<Conversation> mDataList;

        MsgAdapter(List<Conversation> dataList) {
            mDataList = dataList;
        }

        @Override
        public int getCount() {
            return (mDataList == null) ? 0 : mDataList.size();
        }

        @Override
        public Conversation getItem(int i) {
            return (mDataList == null || mDataList.size() <= i) ? null : mDataList.get(i);
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            Conversation data = mDataList.get(position);
//            if (data.getType().equals(ConversationType.single)) {
//                UserInfo feedBack = (UserInfo) data.getTargetInfo();
//                if (feedBack.getUserName().equals("feedback_Android")) {
//                    JMessageClient.deleteSingleConversation("feedback_Android", feedBack.getAppKey());
//                    mDataList.remove(position);
//                    notifyDataSetChanged();
//                }
//            }

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

            //聊天内容

            Message lastMsg = data.getLatestMessage();
            if (lastMsg != null) {
                //会话界面时间
                holder.txt_time.setText(TimeFormat.getTime(getContext(), lastMsg.getCreateTime()));
                String contentStr;
                switch (lastMsg.getContentType()) {
                    case image:
                        contentStr = getContext().getString(R.string.type_picture);
                        break;
                    case voice:
                        contentStr = getContext().getString(R.string.type_voice);
                        break;
                    case location:
                        contentStr = getContext().getString(R.string.type_location);
                        break;
                    case file:
                        String extra = lastMsg.getContent().getStringExtra("video");
                        if (extra != null && extra.equals("mp4")) {
                            contentStr = getContext().getString(R.string.type_smallvideo);
                        } else {
                            contentStr = getContext().getString(R.string.type_file);
                        }
                        break;
                    case video:
                        contentStr = getContext().getString(R.string.type_video);
                        break;
                    case eventNotification:
                        EventNotificationContent eventNotificationContent = (EventNotificationContent) lastMsg.getContent();
                        contentStr = eventNotificationContent.getEventText();
//                        switch (eventNotificationContent.getEventNotificationType()) {
//                            case group_member_added:
//                                //群成员加群事件
//                                break;
//                            case group_member_removed:
//                                //群成员被踢事件
//                                break;
//                            case group_member_exit:
//                                //群成员退群事件
//                                break;
//                        }
                        break;
                    case custom:
                        CustomContent customContent = (CustomContent) lastMsg.getContent();
                        Boolean isBlackListHint = customContent.getBooleanValue("blackList");
                        if (isBlackListHint != null && isBlackListHint) {
                            contentStr = getContext().getString(R.string.jmui_server_803008);
                        } else {
                            contentStr = getContext().getString(R.string.type_custom);
                        }
                        break;
                    default:
                        contentStr = ((TextContent) lastMsg.getContent()).getText();
                }

                if (lastMsg.getTargetType() == ConversationType.group && lastMsg.getContentType() == ContentType.text) {
                    UserInfo info = lastMsg.getFromUser();
                    String fromName = info.getNotename();
                    if (TextUtils.isEmpty(fromName)) {
                        fromName = info.getNickname();
                        if (TextUtils.isEmpty(fromName)) {
                            fromName = info.getUserName();
                        }
                    }
                    getETMember("[" + fromName + "]" + ": " + contentStr, holder.txt_msg);
//                    holder.txt_msg.setText(fromName + ": " + contentStr);
                } else if (lastMsg.getContentType() == eventNotification) {
                    getETMember(contentStr, holder.txt_msg);
                } else {
                    holder.txt_msg.setText(contentStr);
                }
            } else {
                holder.txt_time.setText(TimeFormat.getTime(getContext(), data.getLastMsgDate()));
                holder.txt_msg.setText("");
            }

            if (data.getType().equals(ConversationType.single)) {//单聊头像
                UserInfo userInfo = (UserInfo) data.getTargetInfo();
                getETMember(userInfo, holder.img_header, holder.txt_name);

            } else {//群聊头像
                GroupInfo groupInfo = (GroupInfo) data.getTargetInfo();
                getGroup(groupInfo, holder.txt_name);
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

        private List<Object> data = new ArrayList<>();

        public void setData(List<Object> data) {
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

                        LinearLayout btn_manage = (LinearLayout) view.findViewById(R.id.layout_manage);
                        if (company.isAdministrators()) {
                            btn_manage.setVisibility(View.VISIBLE);
                            btn_manage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    CompanyManageActivity_.intent(getActivity()).company(company).start();
                                }
                            });
                        } else {
                            btn_manage.setVisibility(View.GONE);
                        }
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


    @Override
    public void onResume() {
        initConvListAdapter();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);
        super.onDestroy();
    }
}
