package com.topunion.chili.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.topunion.chili.R;
import com.topunion.chili.base.dialog.DialogTemplate;
import com.topunion.chili.base.dialog.UiLibDialog;
import com.topunion.chili.base.dialog.UiLibDialogInterface;
import com.topunion.chili.data.Notifiy;
import com.topunion.chili.greendao.GreenDaoManager;
import com.topunion.chili.util.TimeFormat;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.Date;
import java.util.List;

@EActivity(R.layout.activity_notifycation)
public class NotificationActivity extends AppCompatActivity {

    @ViewById
    TextView txt_title;

    @ViewById
    ListView mListView;
    @ViewById
    Button btn_confirm;
    private List<Notifiy> mDataList;
    private NotificationAdapter mNotificationAdapter;

    @Click
    void btn_back() {
        this.finish();
    }

    @Click
    void btn_confirm() {
        UiLibDialog.Builder alertBuilder = new UiLibDialog.Builder(NotificationActivity.this, DialogTemplate.TEMPLATE_NORMAL);
        UiLibDialog deleteDialog = alertBuilder.setTitle("确认删除所有消息？")
                .setLeftButton("取消")
                .setRightButtonTextColor(R.color.main)
                .setRightButton("确认", new UiLibDialogInterface.NormalOnClickListener() {
                    @Override
                    public void onClick(View v, boolean isChecked, String editText) {
                        GreenDaoManager.getInstance().getSession().getNotifiyDao().deleteAll();
                        mDataList.clear();
                        mNotificationAdapter.notifyDataSetChanged();
                    }
                }, true).setMidButtonTextColor(R.color.uilib_btn_text_color_red)
                .create();
        deleteDialog.show();
    }

    @AfterViews
    void init() {
        txt_title.setText("易投通知");
        btn_confirm.setText("清空");
        btn_confirm.setVisibility(View.VISIBLE);
        getData();
        mNotificationAdapter = new NotificationAdapter();
        mListView.setAdapter(mNotificationAdapter);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Notifiy data = (Notifiy) parent.getAdapter().getItem(position);
                UiLibDialog.Builder builder = new UiLibDialog.Builder(NotificationActivity.this, DialogTemplate.TEMPLATE_LIST);

                UiLibDialog uiLibDialog = builder
                        .setItems(new String[]{"删除"}, new UiLibDialogInterface.ListOnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position, String itemText) {
                                GreenDaoManager.getInstance().getSession().getNotifiyDao().deleteByKey(data.getId());
                                mDataList.remove(data);
                                mNotificationAdapter.notifyDataSetChanged();
                            }
                        })
                        .create();
                uiLibDialog.show();

                return false;
            }
        });
    }

    void getData() {
        mDataList = GreenDaoManager.getInstance().getSession().getNotifiyDao().queryBuilder().list();
    }


    class NotificationAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return (mDataList == null) ? 0 : mDataList.size();
        }

        @Override
        public Notifiy getItem(int i) {
            return (mDataList == null) ? null : mDataList.get(i);
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (convertView == null) {

                LayoutInflater inflater = LayoutInflater.from(NotificationActivity.this);
                convertView = inflater.inflate(R.layout.notify_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.btn_open = (ImageButton) convertView.findViewById(R.id.btn_open);
                viewHolder.title = (TextView) convertView.findViewById(R.id.txt_notify_title);
                viewHolder.content = (TextView) convertView.findViewById(R.id.txt_content);
                viewHolder.time = (TextView) convertView.findViewById(R.id.txt_time);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Notifiy data = mDataList.get(i);
            if (data != null) {
                viewHolder.title.setText(data.title);
                viewHolder.content.setText(data.msg);
                viewHolder.time.setText(TimeFormat.getTime(NotificationActivity.this, data.time));
            }
            return convertView;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        class ViewHolder {
            TextView title;
            TextView content;
            TextView time;
            ImageButton btn_open;
        }
    }

}
