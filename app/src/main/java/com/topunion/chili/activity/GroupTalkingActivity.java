package com.topunion.chili.activity;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.topunion.chili.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupInfoCallback;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

@EActivity(R.layout.activity_talking)
public class GroupTalkingActivity extends AppCompatActivity {
    @Extra
    String title;

    @ViewById
    ListView mListView;

    @ViewById
    TextView txt_title;

    @ViewById
    ImageButton btn_operation;

    @ViewById
    EditText mEditText;
    @Extra
    String targetId;
    @Extra
    long groupId;
    //文本
    private final int TYPE_SEND_TXT = 0;
    private final int TYPE_RECEIVE_TXT = 1;

    @Click
    void btn_back() {
        this.finish();
    }

    @Click
    void btn_send() {

        String mcgContent = mEditText.getText().toString();
        mEditText.setText("");
        if (mcgContent.equals("")) {
            Toast.makeText(GroupTalkingActivity.this, "不能发送空消息", Toast.LENGTH_SHORT).show();
            return;
        }
        Message msg;
        TextContent content = new TextContent(mcgContent);
        msg = mConv.createSendMessage(content);
        adapter.addDataToAdapter(new MsgInfo(null, msg));
        adapter.notifyDataSetChanged();
        mListView.setSelection(mListView.getBottom());
        JMessageClient.sendMessage(msg);


//        final String text = mEditText.getText().toString();
//        if (text != null && text.length() > 0) {
//            adapter.addDataToAdapter(new MsgInfo(null, text));
//            adapter.notifyDataSetChanged();
//            mListView.setSelection(mListView.getBottom());
//        } else {
//            Toast.makeText(GroupTalkingActivity.this, "不能发送空消息", Toast.LENGTH_SHORT).show();
//        }
//        mEditText.setText("");
    }

    private ListViewAdapter adapter;

    @Click
    void btn_operation() {
        GroupCenterActivity_.intent(this).start();
    }

    @AfterViews
    void init() {
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
            if (mConv != null) {
                GroupInfo groupInfo = (GroupInfo) mConv.getTargetInfo();
//                UserInfo userInfo = groupInfo.getGroupMemberInfo(mMyInfo.getUserName());
            } else {
                mConv = Conversation.createGroupConversation(mGroupId);
            }
        }

        //获取消息列表
        List<Message> messageList = mConv.getMessagesFromNewest(0, mOffset);
        int size = messageList.size();
        for (int i = 0; i < size; i++) {
            Message message = messageList.get(i);
            long time = message.getCreateTime();
            if (message.getContentType() == ContentType.text) {
                int direct = message.getDirect() == MessageDirect.send ? TYPE_SEND_TXT
                        : TYPE_RECEIVE_TXT;
                if(direct == TYPE_SEND_TXT){//发送方

                }else if(direct == TYPE_RECEIVE_TXT){//接收方

                }

            }
        }

        txt_title.setText(title);
        btn_operation.setVisibility(View.VISIBLE);
        btn_operation.setImageResource(R.mipmap.more);
//        MsgInfo msg1 = new MsgInfo("Asdfasdf", null);
//        MsgInfo msg2 = new MsgInfo(null, "Asdfasdf");
        adapter = new ListViewAdapter(this);
//        adapter.addDataToAdapter(new MsgInfo(null, null));
//        adapter.addDataToAdapter(msg1);
//        adapter.addDataToAdapter(msg2);
//        adapter.addDataToAdapter(new MsgInfo(null, null));
        mListView.setAdapter(adapter);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_NULL) {
                    GroupTalkingActivity.this.btn_send();
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

    private boolean mIsSingle = true;
    private Conversation mConv;
    long mGroupId;
    int mOffset = 1000;

    private void initMsg() {
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
            if (mConv != null) {
                GroupInfo groupInfo = (GroupInfo) mConv.getTargetInfo();
//                UserInfo userInfo = groupInfo.getGroupMemberInfo(mMyInfo.getUserName());
            } else {
                mConv = Conversation.createGroupConversation(mGroupId);
            }
        }
    }

    class MsgInfo {
        //        public String left_text;
//        public String right_text;
        public Message left_msg;
        public Message right_msg;

        //        public MsgInfo(String left_text, String right_text) {
//            this.left_text = left_text;
//            this.right_text = right_text;
//        }
        public MsgInfo(Message left_msg, Message right_msg) {
            this.left_msg = left_msg;
            this.right_msg = right_msg;
        }
    }

    public class ListViewAdapter extends BaseAdapter {

        private Context context;
        private List<MsgInfo> datas = new ArrayList<>();

        private ViewHolder viewHolder;

        //给adapter添加数据
        public void addDataToAdapter(MsgInfo e) {
            datas.add(e);
        }

        public ListViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {

                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.message_item, null);
                convertView.findViewById(R.id.img_header_other).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(GroupTalkingActivity.this, PersonalCenterActivity_.class));
//                        PersonalCenterActivity_.intent(GroupTalkingActivity.this).uid().start();
                    }
                });
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //获取adapter中的数据
            String left = ((TextContent) datas.get(position).left_msg.getContent()).getText();
            String right = ((TextContent) datas.get(position).right_msg.getContent()).getText();

            //如果数据为空，则将数据设置给右边，同时显示右边，隐藏左边
            if (right != null) {
                viewHolder.text_right.setText(right);
                viewHolder.right.setVisibility(View.VISIBLE);
                viewHolder.left.setVisibility(View.GONE);
                viewHolder.middle.setVisibility(View.GONE);
            } else if (left != null) {
                viewHolder.text_left.setText(left);
                viewHolder.left.setVisibility(View.VISIBLE);
                viewHolder.right.setVisibility(View.GONE);
                viewHolder.middle.setVisibility(View.GONE);
            } else {
                viewHolder.left.setVisibility(View.GONE);
                viewHolder.right.setVisibility(View.GONE);
                viewHolder.middle.setVisibility(View.VISIBLE);
                if (position == 0) {
                    viewHolder.text_info.setVisibility(View.GONE);
                }
            }

            return convertView;

        }

        class ViewHolder {
            public View rootView;
            public TextView text_left;
            public LinearLayout left;
            public TextView text_right;
            public LinearLayout right;
            public LinearLayout middle;
            public TextView text_time;
            public TextView text_info;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.text_left = (TextView) rootView.findViewById(R.id.text_left);
                this.left = (LinearLayout) rootView.findViewById(R.id.left);
                this.text_right = (TextView) rootView.findViewById(R.id.text_right);
                this.right = (LinearLayout) rootView.findViewById(R.id.right);
                this.middle = (LinearLayout) rootView.findViewById(R.id.layout_info);
                this.text_time = (TextView) rootView.findViewById(R.id.txt_time);
                this.text_info = (TextView) rootView.findViewById(R.id.txt_info);
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
}
