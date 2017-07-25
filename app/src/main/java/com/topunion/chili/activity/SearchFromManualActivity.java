package com.topunion.chili.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.topunion.chili.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_search_from_manual)
public class SearchFromManualActivity extends AppCompatActivity {

    @ViewById
    ListView mListView;

    @ViewById
    EditText mSearchInput;

    @Click
    void btn_cancel() {
        this.finish();
    }

    @AfterViews
    void init() {
        mListView.setAdapter(new MyAdapter());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setResult(i);
                finish();
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
                view = LayoutInflater.from(SearchFromManualActivity.this).inflate(R.layout.person_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.txt_name = (TextView) view.findViewById(R.id.txt_name);
                viewHolder.img_header = (SimpleDraweeView) view.findViewById(R.id.img_header);
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
            }

            return view;
        }


        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        class ViewHolder {
            TextView txt_name;
            SimpleDraweeView img_header;
        }
    }

}
