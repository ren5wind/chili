package com.topunion.chili.base.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.topunion.chili.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author      : renxiaoming
 * Date        : 2017-07-12
 * Description :
 */
public class UiLibDialogList extends UiLibDialogImp {

    private ListView mListView;

    private SimpleAdapter mAdapter;

    protected UiLibDialogList(@NonNull Context context, int themeResId, int layoutResID) {
        super(context, themeResId, layoutResID);
    }

    private void setListTitle(@Nullable CharSequence title) {
        setTextViewText(R.id.uilib_dialog_tv_title, title);
    }

    private void setListMessage(@Nullable CharSequence message) {
        setTextViewText(R.id.uilib_dialog_tv_message, message);
    }

    private void initListView(String[] arrText, final ListOnItemClickListener listener) {

        List<Map<String, String>> items = new ArrayList<>();
        for (int i = 0; i < arrText.length; i++) {
            Map<String, String> item = new HashMap<>();
            item.put("text", arrText[i]);
            items.add(item);
        }
        mAdapter = new SimpleAdapter(mContext, items, R.layout.uilib_layout_dialog_item,
                new String[]{"text"}, new int[]{R.id.uilib_dialog_item_text});

        mListView = (ListView) findViewById(R.id.uilib_dialog_list);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onItemClick(view, position, (String) ((TextView) view).getText());
                dismiss();
            }
        });

    }

    @Override
    protected void applyDialog(UiLibDialogParams params) {
        if (!TextUtils.isEmpty(params.mTitle)) {
            setListTitle(params.mTitle);
        }

        if (!TextUtils.isEmpty(params.mMessage)) {
            setListMessage(params.mMessage);
        }
        if (params.mItems != null && params.mItems.length > 0) {
            initListView(params.mItems, params.mListOnItemListener);
        }
    }
}
