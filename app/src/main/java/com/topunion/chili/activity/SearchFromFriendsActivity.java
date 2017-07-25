package com.topunion.chili.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.topunion.chili.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_search_from_contact)
public class SearchFromFriendsActivity extends AppCompatActivity {

    @ViewById
    ListView mListView;

    @ViewById
    TextView txt_title;

    @ViewById
    Button btn_confirm;

    @ViewById
    ImageButton btn_operation;

    @Click
    void btn_confirm() {
        //TODO  setResult
        this.finish();
    }

    @Click
    void btn_back() {
        this.finish();
    }

    @AfterViews
    void init() {
        btn_operation.setVisibility(View.GONE);
        btn_confirm.setVisibility(View.GONE);
        txt_title.setText("通过易友添加");
        mListView.setAdapter(new MyAdapter());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MyAdapter.ViewHolder viewHolder = (MyAdapter.ViewHolder) view.getTag();
                viewHolder.img_choose.setVisibility(View.VISIBLE);
            }
        });
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(SearchFromFriendsActivity.this).inflate(R.layout.person_detail_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.txt_name = (TextView) view.findViewById(R.id.txt_name);
                viewHolder.txt_nickname = (TextView) view.findViewById(R.id.txt_nickname);
                viewHolder.txt_phone = (TextView) view.findViewById(R.id.txt_phone);
                viewHolder.txt_phone.setVisibility(View.GONE);
                viewHolder.img_header = (SimpleDraweeView) view.findViewById(R.id.img_header);
                viewHolder.img_choose = (ImageView) view.findViewById(R.id.img_choose);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            switch (i) {
                case 0:
                    viewHolder.txt_name.setText("张三");
                    break;
                case 1:
                    viewHolder.txt_name.setText("李四");
                    break;
                case 2:
                    viewHolder.txt_name.setText("王五");
                    break;
                case 3:
                    viewHolder.txt_name.setText("赵六");
                    break;
            }


            return view;
        }


        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        class ViewHolder {
            TextView txt_name;
            TextView txt_nickname;
            TextView txt_phone;
            SimpleDraweeView img_header;
            ImageView img_choose;
        }
    }

}
