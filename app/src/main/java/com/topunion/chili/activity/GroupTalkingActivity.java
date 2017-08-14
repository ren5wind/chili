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

import com.facebook.drawee.view.SimpleDraweeView;
import com.topunion.chili.R;
import com.topunion.chili.business.ImInfoManager;
import com.topunion.chili.util.TimeFormat;

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
                mConv = Conversation.createGroupConversation(groupId);
            }
        }

        //获取消息列表
        List<Message> messageList = mConv.getMessagesFromNewest(0, mOffset);
        int size = messageList.size();
        for (int i = 0; i < size; i++) {
            Message message = messageList.get(i);
            if (message.getContentType() == ContentType.text) {
                int direct = message.getDirect() == MessageDirect.send ? TYPE_SEND_TXT
                        : TYPE_RECEIVE_TXT;
                if (direct == TYPE_SEND_TXT) {//发送方
                    adapter.addDataToAdapter(new MsgInfo(message, null));
                } else if (direct == TYPE_RECEIVE_TXT) {//接收方
                    adapter.addDataToAdapter(new MsgInfo(null, message));
                }
            }
        }

        txt_title.setText(title);
        btn_operation.setVisibility(View.VISIBLE);
        btn_operation.setImageResource(R.mipmap.more);
        adapter = new ListViewAdapter(this);
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

    class MsgInfo {
        public Message left_msg;
        public Message right_msg;

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
            Message left = datas.get(position).left_msg;
            Message right = datas.get(position).right_msg;


//            String left = ((TextContent) datas.get(position).left_msg.getContent()).getText();
//            String right = ((TextContent) datas.get(position).right_msg.getContent()).getText();

            //如果数据为空，则将数据设置给右边，同时显示右边，隐藏左边
            if (right != null) {
                viewHolder.text_right.setText(((TextContent) right.getContent()).getText());
                viewHolder.right.setVisibility(View.VISIBLE);
                viewHolder.left.setVisibility(View.GONE);
                viewHolder.middle.setVisibility(View.GONE);
                viewHolder.text_time.setText(TimeFormat.getDetailTime(GroupTalkingActivity.this, left.getCreateTime()));
                viewHolder.img_header_other.setImageURI(ImInfoManager.getInstance().getFriendById((int) left.getFromUser().getUserID()).headImg);
                viewHolder.tv_name_other.setText(ImInfoManager.getInstance().getFriendById((int) left.getFromUser().getUserID()).nickname);

            } else if (left != null) {
                viewHolder.text_left.setText(((TextContent) left.getContent()).getText());
                viewHolder.left.setVisibility(View.VISIBLE);
                viewHolder.right.setVisibility(View.GONE);
                viewHolder.middle.setVisibility(View.GONE);
                viewHolder.text_time.setText(TimeFormat.getDetailTime(GroupTalkingActivity.this, right.getCreateTime()));
                viewHolder.img_header.setImageURI(ImInfoManager.getInstance().getFriendById((int) right.getFromUser().getUserID()).headImg);
                viewHolder.tv_name.setText(ImInfoManager.getInstance().getFriendById((int) right.getFromUser().getUserID()).nickname);
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
            public LinearLayout right;
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
