package com.topunion.chili.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.topunion.chili.R;
import com.topunion.chili.business.AccountManager;
import com.topunion.chili.net.HttpHelper_;
import com.topunion.chili.net.request_interface.GetCorpOrDeptUsers;
import com.topunion.chili.net.request_interface.GetFriends;
import com.topunion.chili.net.request_interface.GetGroupDetails;
import com.topunion.chili.view.PinyinComparator;
import com.topunion.chili.wight.SideBar;
import com.topunion.chili.view.SortAdapter;
import com.topunion.chili.data.SortModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;


@EActivity(R.layout.activity_friends)
public class FriendsActivity extends Activity {
    private SortAdapter adapter; // 排序的适配器

    private List<SortModel> SourceDateList; // 数据

    private PinyinComparator pinyinComparator;
    private int lastFirstVisibleItem = -1;

    public final static int TYPE_SHOW_FRIENDS = 0;
    public final static int TYPE_SHOW_DEPT_NUMBERS = 1;
    public final static int TYPE_SHOW_GROUP_MEMBER = 2;

    @ViewById
    LinearLayout top_layout;
    @ViewById
    TextView top_char, dialog,btn_newFriend,txt_title;
    @ViewById
    SideBar sideBar;
    @ViewById
    ListView mListView;

    @Extra
    int showType;
    @Extra
    int deptId;
    @Extra
    String deptName;
    @Extra
    Serializable group;

    @Click
    void btn_newFriend() {
        startActivity(new Intent(this, NewFriendsActivity_.class));
    }

    @Click
    void btn_back() {
        this.finish();
    }

    @AfterViews
    void init() {
        if(showType == TYPE_SHOW_FRIENDS){
            btn_newFriend.setVisibility(View.VISIBLE);
            txt_title.setText("易投好友");
        }else if(showType == TYPE_SHOW_DEPT_NUMBERS){
            txt_title.setText(deptName);
        }else if(showType == TYPE_SHOW_GROUP_MEMBER){
            txt_title.setText("全部群成员");
        }

        pinyinComparator = new PinyinComparator();
        sideBar.setTextView(dialog);
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListView.setSelection(position);
                }

            }
        });

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TalkingActivity_.intent(FriendsActivity.this).targetId(SourceDateList.get(position).getImName())
                        .title(SourceDateList.get(position).getName()).start();
            }

        });

        dataRequest();
        adapter = new SortAdapter(this, SourceDateList);
        mListView.setAdapter(adapter);

        mListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                int section = adapter.getSectionForPosition(firstVisibleItem);
                int nextSecPosition = adapter
                        .getPositionForSection(section + 1);
                if (firstVisibleItem != lastFirstVisibleItem) {
                    MarginLayoutParams params = (MarginLayoutParams) top_layout
                            .getLayoutParams();
                    params.topMargin = 0;
                    top_layout.setLayoutParams(params);
                    top_char.setText(String.valueOf((char) section));
                }
                if (nextSecPosition == firstVisibleItem + 1) {
                    View childView = view.getChildAt(0);
                    if (childView != null) {
                        int titleHeight = top_layout.getHeight();
                        int bottom = childView.getBottom();
                        MarginLayoutParams params = (MarginLayoutParams) top_layout
                                .getLayoutParams();
                        if (bottom < titleHeight) {
                            float pushedDistance = bottom - titleHeight;
                            params.topMargin = (int) pushedDistance;
                            top_layout.setLayoutParams(params);
                        } else {
                            if (params.topMargin != 0) {
                                params.topMargin = 0;
                                top_layout.setLayoutParams(params);
                            }
                        }
                    }
                }
                lastFirstVisibleItem = firstVisibleItem;
            }
        });
    }

    @Background
    void dataRequest() {
        switch (showType) {
            case TYPE_SHOW_FRIENDS:
                GetFriends.GetFriendsResponse friends = HttpHelper_.getInstance_(this).getFriends(AccountManager.getInstance().getUserId(), 1, 20);
                SourceDateList = friends.friendsTofilledData();
                break;
            case TYPE_SHOW_DEPT_NUMBERS:
                GetCorpOrDeptUsers.GetCorpOrDeptUsersResponse deptNumbs = HttpHelper_.getInstance_(this).getDeptUsers(1, 20, deptId, deptName);
                SourceDateList = deptNumbs.deptTofilledData();
                break;
            case TYPE_SHOW_GROUP_MEMBER:
                SourceDateList = ((GetGroupDetails.GetGroupDetailsResponse.Group)group).membersTofilledData();
                break;
        }

        if (SourceDateList != null) {
            Collections.sort(SourceDateList, pinyinComparator);
        }
        update();
    }

    @UiThread
    void update() {
        adapter.updateListView(SourceDateList);
    }


}
