package com.topunion.chili.activity;


import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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

    @AfterViews
    void init() {
        try {
            getContact();
        } catch (Exception e) {

        }
        txt_title.setText("电话本");

        final Adapter mAdapter = new Adapter();
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    ArrayList<Contact> contacts = new ArrayList<>();

    class Contact {
        String name;
        String phone;
    }

    public void getContact() throws Exception {
        Uri uri = Uri.parse("content://com.android.contacts/contacts");
        ContentResolver resolver = this.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{"_id"}, null, null, null);
        while (cursor.moveToNext()) {
            int contractID = cursor.getInt(0);

            StringBuilder sb = new StringBuilder("contractID=");

            sb.append(contractID);
            uri = Uri.parse("content://com.android.contacts/contacts/" + contractID + "/data");
            Cursor cursor1 = resolver.query(uri, new String[]{"mimetype", "data1", "data2"}, null, null, null);
            while (cursor1.moveToNext()) {
                Contact contact = new Contact();
                String data1 = cursor1.getString(cursor1.getColumnIndex("data1"));
                String mimeType = cursor1.getString(cursor1.getColumnIndex("mimetype"));
                if ("vnd.android.cursor.item/name".equals(mimeType)) { //是姓名
                    sb.append(",name=" + data1);
                    contact.name = data1;
                } else if ("vnd.android.cursor.item/email_v2".equals(mimeType)) { //邮箱
                    sb.append(",email=" + data1);
                } else if ("vnd.android.cursor.item/phone_v2".equals(mimeType)) { //手机
                    sb.append(",phone=" + data1);
                    contact.phone = data1;
                }
                contacts.add(contact);
            }
            cursor1.close();
            Log.i("Shawn", sb.toString());
        }
        cursor.close();
    }

    class Adapter extends BaseAdapter {
        @Override
        public int getCount() {
            return contacts.size();
        }

        @Override
        public Object getItem(int i) {
            return contacts.get(i);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null ){
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
            viewHolder.txt_name.setText(contacts.get(i).name);
            viewHolder.txt_phone.setText(contacts.get(i).phone);
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
