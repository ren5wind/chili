package com.topunion.chili.activity;


import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.topunion.chili.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_choose_department)
public class ChooseDepartmentActivity extends AppCompatActivity {

    @ViewById
    TextView txt_title;

    @Click
    void btn_back() {
        this.finish();
    }

    @Click
    void btn_confirm() {
        setResult(mListView.getCheckedItemPosition());
        finish();
    }

    @ViewById
    Button btn_confirm;

    @ViewById
    ListView mListView;

    @Extra
    String title;
    @Extra
    String[] data;
    @Extra
    int[] choose;

    @AfterViews
    void init() {
        btn_confirm.setVisibility(View.VISIBLE);
        txt_title.setText(title);

        final Adapter mAdapter = new Adapter();

        mListView.setAdapter(mAdapter);
    }

    class Adapter extends BaseAdapter {
        @Override
        public int getCount() {
            return choose.length;
        }

        @Override
        public Object getItem(int i) {
            return choose[i];
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null ){
                view = LayoutInflater.from(ChooseDepartmentActivity.this).inflate(R.layout.member_setting_list_item, null);
            }
            TextView txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_name.setText(data[i]);
            if (choose[i] == 1) {
                mListView.setItemChecked(i, true);
            }
            return view;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }
    }
}
