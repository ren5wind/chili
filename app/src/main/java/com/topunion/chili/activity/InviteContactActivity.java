package com.topunion.chili.activity;


import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.topunion.chili.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_choose_person)
public class InviteContactActivity extends AppCompatActivity {

    @ViewById
    TextView txt_title;

    @Click
    void btn_back() {
        this.finish();
    }

    @ViewById
    ListView mListView;

    @ViewById
    EditText et_search;

    private List<Contact> contacts = new ArrayList<>();
    private List<Contact> mSreachList = new ArrayList<>();
    private Adapter mAdapter;

    @Click
    void btn_search() {
        mSreachList.clear();
        String keyword = et_search.getText().toString().trim();
        int size = (contacts == null) ? 0 : contacts.size();
        for (int i = 0; i < size; i++) {
            if (contacts.get(i).name.contains(keyword) || contacts.get(i).phone.contains(keyword)) {
                mSreachList.add(contacts.get(i));
            }
        }
        mAdapter.setData(mSreachList);
    }

    @AfterViews
    void init() {
        getContact();
        txt_title.setText("电话本");

        mAdapter = new Adapter(contacts);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    class Contact {
        String name;
        String phone;
    }

    private String getSortkey(String sortKeyString) {
        String key = sortKeyString.substring(0, 1).toUpperCase();
        if (key.matches("[A-Z]")) {
            return key;
        } else
            return "#";   //获取sort key的首个字符，如果是英文字母就直接返回，否则返回#。
    }

    public void getContact() {
        try {
            Uri contactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            Cursor cursor = getContentResolver().query(contactUri,
                    new String[]{"display_name", "sort_key", "contact_id", "data1"},
                    null, null, "sort_key");
            String contactSortKey;
            int contactId;
            while (cursor.moveToNext()) {
                Contact contact = new Contact();
                contact.name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                contact.phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactId = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                contactSortKey = getSortkey(cursor.getString(1));
                if (contact.name != null)
                    contacts.add(contact);
            }
            cursor.close();//使用完后一定要将cursor关闭，不然会造成内存泄露等问题

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    class Adapter extends BaseAdapter {
        private List<Contact> mDataList;

        public Adapter(List<Contact> list) {
            mDataList = list;
        }

        public void setData(List<Contact> list) {
            mDataList = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return (mDataList == null) ? 0 : mDataList.size();
        }

        @Override
        public Object getItem(int i) {
            return (mDataList == null) ? null : mDataList.get(i);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(InviteContactActivity.this).inflate(R.layout.person_detail_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.txt_name = (TextView) view.findViewById(R.id.txt_name);
                viewHolder.txt_phone = (TextView) view.findViewById(R.id.txt_phone);
                viewHolder.txt_nickname = (TextView) view.findViewById(R.id.txt_nickname);

                viewHolder.btn_invite = (Button) view.findViewById(R.id.btn_invite);
                viewHolder.img_header = (SimpleDraweeView) view.findViewById(R.id.img_header);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.txt_name.setText(mDataList.get(i).name);
            viewHolder.txt_phone.setText(mDataList.get(i).phone);
            viewHolder.btn_invite.setVisibility(View.VISIBLE);
            viewHolder.btn_invite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(InviteContactActivity.this, "邀请已发送", Toast.LENGTH_SHORT).show();
                }
            });
            return view;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        class ViewHolder {
            TextView txt_name, txt_nickname, txt_phone;
            Button btn_invite;
            SimpleDraweeView img_header;
        }
    }
}
