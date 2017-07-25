package com.topunion.chili.activity;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
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

@EActivity(R.layout.activity_choose_person)
public class ChoosePersonActivity extends AppCompatActivity {

    @ViewById
    TextView txt_title;

    @Click
    void btn_back() {
        this.finish();
    }

    @Click
    void btn_confirm() {
        Intent data = new Intent();
        SparseBooleanArray array = mListView.getCheckedItemPositions();

        int[] result = new int[array.size()];
        for (int i=0;i<array.size();i++){
            result[i] = array.valueAt(i) == true ? 1 : 0;
        }
        data.putExtra("result", result);
        setResult(200, data);
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
                view = LayoutInflater.from(ChoosePersonActivity.this).inflate(R.layout.choose_person_list_item, null);
            }
            TextView txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_name.setText(data[i]);
            return view;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }
    }
}
