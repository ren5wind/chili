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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Date;

@EActivity(R.layout.activity_notifycation)
public class NotificationActivity extends AppCompatActivity {

    @ViewById
    TextView txt_title;

    @ViewById
    ListView mListView;

    @Click
    void btn_back() {
        this.finish();
    }

    @AfterViews
    void init() {
        txt_title.setText("易投通知");

        mListView.setAdapter(new NotificationAdapter());
    }


    class NotificationAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object getItem(int i) {
            return null;
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

            switch (i) {
                case 0:
                    viewHolder.title.setText("张三回答了你的问题");
                    viewHolder.content.setText("抢占沙发");
                    viewHolder.time.setText(new Date().toLocaleString());
                    break;

                case 1:

                    viewHolder.title.setText("易投问答：");
                    viewHolder.content.setText("阿斯顿发送到付阿斯顿发送到付阿斯顿发" +
                            "送到付阿斯顿发送到付阿斯顿发送到付阿斯顿发送到付阿斯顿发送" +
                            "到付阿斯顿发送到付阿斯顿发送到付阿斯顿发送到付阿斯顿发送到" +
                            "付阿斯顿发送到付阿斯顿发送到付阿斯顿发送到付阿斯顿发送到付" +
                            "阿斯顿发送到付阿斯顿发送到付阿斯顿发送到付阿斯顿发送到付阿" +
                            "斯顿发送到付阿斯顿发送到付阿斯顿发送到付");
                    viewHolder.time.setText(new Date().toLocaleString());
                    break;
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
