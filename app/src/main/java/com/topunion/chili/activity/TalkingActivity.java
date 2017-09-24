package com.topunion.chili.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.topunion.chili.R;
import com.topunion.chili.business.AccountManager;
import com.topunion.chili.net.HttpHelper_;
import com.topunion.chili.net.request_interface.GetETMemberDetails;
import com.topunion.chili.net.request_interface.GetGroupDetails;
import com.topunion.chili.util.TimeFormat;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.api.options.MessageSendingOptions;

@EActivity(R.layout.activity_talking)
public class TalkingActivity extends AppCompatActivity {

    @ViewById
    ListView mListView;
    @ViewById
    TextView txt_title;
    @ViewById
    ImageButton btn_operation;
    @ViewById
    EditText mEditText;
    @Extra
    String title;
    @Extra
    String targetId;
    @Extra
    long groupId;
    //文本
    private final int TYPE_SEND_TXT = 0;
    private final int TYPE_RECEIVE_TXT = 1;

    private boolean mIsSingle = true;
    private Conversation mConv;

    int mOffset = 1000;
    MessageSendingOptions options;
    private List<MsgInfo> mDataList;

    @Click
    void btn_back() {
        this.finish();
    }

    @Click
    void btn_send() {

        String mcgContent = mEditText.getText().toString();
        mEditText.setText("");
        if (mcgContent.equals("")) {
            Toast.makeText(TalkingActivity.this, "不能发送空消息", Toast.LENGTH_SHORT).show();
            return;
        }
        Message msg;
        TextContent content = new TextContent(mcgContent);
        if (mConv == null) {
            Toast.makeText(TalkingActivity.this, "请登陆", Toast.LENGTH_SHORT).show();
            return;
        }
        msg = mConv.createSendMessage(content);
        mDataList.add(new MsgInfo(null, msg, null));
        adapter.notifyDataSetChanged();
        mListView.setSelection(mListView.getBottom());
        options.setRetainOffline(true);//是否当对方用户不在线时让后台服务区保存这条消息的离线消息
        options.setShowNotification(true);//是否让对方展示sdk默认的通知栏通知
        options.setCustomNotificationEnabled(true);
        options.setNotificationTitle("易投");
        options.setNotificationText(AccountManager.getInstance().getNickName() + "：" + mcgContent);
        JMessageClient.sendMessage(msg, options);
    }

    private ListViewAdapter adapter;

    @Click
    void btn_operation() {

        if (mIsSingle) {
            PersonalCenterActivity_.intent(this).uid(targetId).start();
        } else {
            GroupCenterActivity_.intent(this).groupImId(groupId).start();
        }
    }

    @AfterViews
    void init() {
        mDataList = new ArrayList<>();
        txt_title.setText(title);
        btn_operation.setVisibility(View.VISIBLE);
        btn_operation.setImageResource(R.mipmap.more);
        initImMessage();
        JMessageClient.registerEventReceiver(this);
        options = new MessageSendingOptions();

        mListView.setAdapter(adapter);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_NULL) {
                    TalkingActivity.this.btn_send();
                    return true;
                }
                return false;
            }
        });

        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                closeKeyboard();
                return false;
            }
        });
    }

    private void initImMessage() {
        if (!TextUtils.isEmpty(targetId)) {
            //单聊
            mIsSingle = true;
            mConv = JMessageClient.getSingleConversation(targetId);
            if (mConv == null) {
                mConv = Conversation.createSingleConversation(targetId);
            }
        } else {
            //群聊
            mIsSingle = false;
            mConv = JMessageClient.getGroupConversation(groupId);
            if (mConv == null) {
                mConv = Conversation.createGroupConversation(groupId);
            }
        }
        //获取消息列表
        if (mConv != null) {
            List<Message> messageList = mConv.getMessagesFromOldest(0, mOffset);
            int size = (messageList == null) ? 0 : messageList.size();
            for (int i = 0; i < size; i++) {
                Message message = messageList.get(i);
                if (message.getContentType() == ContentType.text) {
                    int direct = message.getDirect() == MessageDirect.send ? TYPE_SEND_TXT
                            : TYPE_RECEIVE_TXT;
                    if (direct == TYPE_SEND_TXT) {//发送方
                        mDataList.add(new MsgInfo(null, message, null));
                    } else if (direct == TYPE_RECEIVE_TXT) {//接收方
                        mDataList.add(new MsgInfo(message, null, null));
                    }
                } else if (message.getContentType() == ContentType.eventNotification) {
                    mDataList.add(new MsgInfo(null, null, message));
                }
            }
        }
        if (adapter == null) {
            adapter = new ListViewAdapter(this, mDataList);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    public void onEventMainThread(MessageEvent event) {
        initImMessage();
    }

    class MsgInfo {
        public Message left_msg;
        public Message right_msg;
        public Message mid_msg;


        public MsgInfo(Message left_msg, Message right_msg, Message mid_msg) {
            this.left_msg = left_msg;
            this.right_msg = right_msg;
            this.mid_msg = mid_msg;
        }
    }

    @Background
    void getGroup(GroupInfo groupInfo, final TextView txt_name) {
        final GetGroupDetails.GetGroupDetailsResponse response =
                HttpHelper_.getInstance_(this).getGroupDetails(groupInfo.getGroupID());
        if (response.data == null) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txt_name.setText(response.data.name);
            }
        });
    }

    @Background
    void getETMember(UserInfo userInfo, final SimpleDraweeView img_header, final TextView txt_name) {
        final GetETMemberDetails.GetETMemberDetailsResponse response =
                HttpHelper_.getInstance_(this).getETMemberDetails(userInfo.getUserName(), AccountManager.getInstance().getUserId());
        if (response != null && response.data != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    img_header.setImageURI(response.data.headImg);
                    txt_name.setText(response.data.logicNickname);
                }
            });
        }
    }

    public class ListViewAdapter extends BaseAdapter {

        private Context context;
        private List<MsgInfo> dataList = new ArrayList<>();

        private ViewHolder viewHolder;

//        //给adapter添加数据
//        public void addDataToAdapter(MsgInfo e) {
//            datas.add(e);
//        }

        public ListViewAdapter(Context context, List<MsgInfo> datas) {
            this.context = context;
            dataList = datas;
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public MsgInfo getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {

                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.message_item, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //获取adapter中的数据
            Message left = dataList.get(position).left_msg;
            Message right = dataList.get(position).right_msg;
            Message mid = dataList.get(position).mid_msg;


            //如果数据为空，则将数据设置给右边，同时显示右边，隐藏左边
            if (right != null) {
                viewHolder.text_right.setText(((TextContent) right.getContent()).getText());
                viewHolder.right.setVisibility(View.VISIBLE);
                viewHolder.left.setVisibility(View.GONE);
                viewHolder.text_info.setVisibility(View.GONE);

                viewHolder.text_time.setText(TimeFormat.getDetailTime(TalkingActivity.this, right.getCreateTime()));
                getETMember(right.getFromUser(), viewHolder.img_header, viewHolder.tv_name);
                viewHolder.img_header.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UserInfo userInfo = null;
                        Message message = null;
                        message = getItem(position).right_msg;
                        userInfo = message.getFromUser();
                        PersonalCenterActivity_.intent(TalkingActivity.this).uid(userInfo.getUserName()).start();
                    }
                });
            } else if (left != null) {
                viewHolder.text_left.setText(((TextContent) left.getContent()).getText());
                viewHolder.left.setVisibility(View.VISIBLE);
                viewHolder.right.setVisibility(View.GONE);
                viewHolder.text_info.setVisibility(View.GONE);

                viewHolder.text_time.setText(TimeFormat.getDetailTime(TalkingActivity.this, left.getCreateTime()));
//                viewHolder.img_header.setImageURI(ImInfoManager.getInstance().getFriendById((int) left.getFromUser().getUserID()).headImg);
//                viewHolder.tv_name.setText(ImInfoManager.getInstance().getFriendById((int) left.getFromUser().getUserID()).nickname);
                getETMember(left.getFromUser(), viewHolder.img_header_other, viewHolder.tv_name_other);
                viewHolder.img_header_other.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UserInfo userInfo = null;
                        Message message = null;
                        message = getItem(position).left_msg;
                        userInfo = message.getFromUser();
                        PersonalCenterActivity_.intent(TalkingActivity.this).uid(userInfo.getUserName()).start();
                    }
                });
            } else if (mid != null) {
                viewHolder.left.setVisibility(View.GONE);
                viewHolder.right.setVisibility(View.GONE);
                viewHolder.text_info.setVisibility(View.VISIBLE);
                if (mid.getContent() != null) {
                    EventNotificationContent eventNotificationContent = (EventNotificationContent) mid.getContent();
                    viewHolder.text_info.setText(eventNotificationContent.getEventText());
                }
                viewHolder.text_time.setText(TimeFormat.getDetailTime(TalkingActivity.this, mid.getCreateTime()));
            }
//            else {
//                viewHolder.left.setVisibility(View.GONE);
//                viewHolder.right.setVisibility(View.GONE);
//                viewHolder.middle.setVisibility(View.VISIBLE);
//                if (position == 0) {
//                    viewHolder.text_info.setVisibility(View.GONE);
//                }
//            }

            return convertView;

        }

        class ViewHolder {
            public View rootView;
            public TextView text_left;
            public LinearLayout left;
            public TextView text_right;
            public RelativeLayout right;
            public LinearLayout middle;
            public TextView text_time;
            public TextView text_info;
            public SimpleDraweeView img_header_other;
            public TextView tv_name_other;
            public SimpleDraweeView img_header;
            public TextView tv_name;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.text_left = (TextView) rootView.findViewById(R.id.text_left);
                this.left = (LinearLayout) rootView.findViewById(R.id.left);
                this.text_right = (TextView) rootView.findViewById(R.id.text_right);
                this.right = (RelativeLayout) rootView.findViewById(R.id.right);
                this.middle = (LinearLayout) rootView.findViewById(R.id.layout_info);
                this.text_time = (TextView) rootView.findViewById(R.id.text_time);
                this.text_info = (TextView) rootView.findViewById(R.id.text_info);
                this.img_header_other = (SimpleDraweeView) rootView.findViewById(R.id.img_header_other);
                this.img_header = (SimpleDraweeView) rootView.findViewById(R.id.img_header);
                this.tv_name_other = (TextView) rootView.findViewById(R.id.tv_name_other);
                this.tv_name = (TextView) rootView.findViewById(R.id.tv_name);
            }

        }
    }

    private void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);
        super.onDestroy();
    }
}
