package com.topunion.chili.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.topunion.chili.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_group_list)
public class GroupListActivity extends AppCompatActivity {

    @ViewById
    TextView txt_title, txt_name;

    @ViewById
    ImageButton btn_operation;

    @ViewById
    ListView mListView;

    @Click
    void btn_operation() {
        ChoosePersonActivity_.intent(this)
                .choose(new int[]{0,0,0,0,0,0,0})
                .data(new String[]{"张三","李四","王五","赵六","田七","猴八","牛二"})
                .title("选择联系人").startForResult(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 200) {
            Toast.makeText(this, "result: " + data.getIntArrayExtra("result").toString() , Toast.LENGTH_SHORT).show();
        }
    }

    @Click
    void btn_back() {
        this.finish();
    }

    @AfterViews
    void init() {
        txt_title.setText("群组");
        btn_operation.setVisibility(View.VISIBLE);
        btn_operation.setImageResource(R.mipmap.add_friends);

        mListView.setAdapter(new Adapter());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GroupTalkingActivity_.intent(GroupListActivity.this).title("张三，李四，王五...").start();
            }
        });
    }

    class Adapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(GroupListActivity.this).inflate(R.layout.group_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.txt_name = (TextView) view.findViewById(R.id.txt_name);
                viewHolder.txt_count = (TextView) view.findViewById(R.id.txt_count);
                viewHolder.img_header = (SimpleDraweeView) view.findViewById(R.id.img_header);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.txt_name.setText("易投科技有限公司群");
            viewHolder.txt_count.setText("22人");
            return view;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        class ViewHolder {
            TextView txt_name, txt_count;
            SimpleDraweeView img_header;
        }
    }
}
