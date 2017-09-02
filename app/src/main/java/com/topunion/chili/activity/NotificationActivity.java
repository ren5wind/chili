package com.topunion.chili.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.topunion.chili.R;
import com.topunion.chili.data.Notifiy;

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

    private List<Notifiy> mDataList;
    private NotificationAdapter mNotificationAdapter;

    @Click
    void btn_back() {
        this.finish();
    }

    @AfterViews
    void init() {
        txt_title.setText("易投通知");
        mNotificationAdapter = new NotificationAdapter();
        mListView.setAdapter(mNotificationAdapter);
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
                viewHolder.time.setText(data.time + "");
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
