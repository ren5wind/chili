package com.topunion.chili.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topunion.chili.R;
import com.topunion.chili.business.AccountManager;
import com.topunion.chili.net.HttpHelper_;
import com.topunion.chili.net.request_interface.GetCorpOrDeptUsers;
import com.topunion.chili.net.request_interface.GetFriends;
import com.topunion.chili.view.CharacterParser;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@EActivity(R.layout.activity_sort_person)
public class FriendsActivity extends Activity {
    private SortAdapter adapter; // 排序的适配器

    private CharacterParser characterParser;
    private List<SortModel> SourceDateList; // 数据

    private PinyinComparator pinyinComparator;
    private int lastFirstVisibleItem = -1;

    public final static int TYPE_SHOW_FRIENDS = 0;
    public final static int TYPE_SHOW_DEPT_NUMBERS = 1;

    @ViewById
    LinearLayout top_layout;
    @ViewById
    RelativeLayout search_layout;
    @ViewById
    TextView top_char, dialog;
    @ViewById
    SideBar sideBar;
    @ViewById
    ListView mListView;
    @ViewById
    TextView txt_title;
    @ViewById
    ImageButton btn_operation;

    @Extra
    int showType;
    @Extra
    int deptId;
    @Extra
    String deptName;
    @Click
    void btn_operation() {
        //TODO 搜索
    }

    @Click
    void btn_back() {
        this.finish();
    }

    @AfterViews
    void init() {
        search_layout.setVisibility(View.VISIBLE);
        txt_title.setVisibility(View.GONE);
        btn_operation.setVisibility(View.VISIBLE);
        btn_operation.setImageResource(R.mipmap.add_friends);
        characterParser = CharacterParser.getInstance();

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
                SortModel shortModel = SourceDateList.get(position);
                Intent intent = new Intent();
                intent.putExtra(TalkingActivity_.INTENT_KEY_USER_ID,shortModel.getId());
                intent.putExtra(TalkingActivity_.INTENT_KEY_USER_NICKNAME,shortModel.getName());
                intent.setClass(FriendsActivity.this, TalkingActivity_.class);
                startActivity(intent);
            }

        });

        dataRequest();
        adapter = new SortAdapter(this, SourceDateList);
        mListView.setAdapter(adapter);

        mListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

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
        switch (showType){
            case TYPE_SHOW_FRIENDS:
                GetFriends.GetFriendsResponse friends = HttpHelper_.getInstance_(this).getFriends(AccountManager.getInstance().getUserId(),1, 20);
                SourceDateList = friendsTofilledData(friends.result);
                break;
            case TYPE_SHOW_DEPT_NUMBERS:
                GetCorpOrDeptUsers.GetCorpOrDeptUsersResponse deptNumbs = HttpHelper_.getInstance_(this).getDeptUsers(1, 20, deptId, deptName);
                SourceDateList = deptTofilledData(deptNumbs.result);
                break;
        }

        if(SourceDateList != null) {
            Collections.sort(SourceDateList, pinyinComparator);
        }
        updata();
    }

    @UiThread
    void updata(){
        adapter.updateListView(SourceDateList);
    }
    private List<SortModel> friendsTofilledData(List<GetFriends.GetFriendsResponse.Friend> data) {
        List<SortModel> mSortList = new ArrayList<SortModel>();
        if(data == null){
            return null;
        }
        for (int i = 0; i < data.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(data.get(i).nickname);
            sortModel.setId(data.get(i).userId);
            sortModel.setIconUrl(data.get(i).headImg);
            String pinyin = characterParser.getSelling(data.get(i).nickname);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }
            mSortList.add(sortModel);
        }
        return mSortList;
    }

    private List<SortModel> deptTofilledData(List<GetCorpOrDeptUsers.GetCorpOrDeptUsersResponse.User> data) {
        List<SortModel> mSortList = new ArrayList<SortModel>();
        if(data == null){
            return null;
        }
        for (int i = 0; i < data.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(data.get(i).nickname);
            sortModel.setId(data.get(i).userId);
            sortModel.setIconUrl(data.get(i).headImg);
            String pinyin = characterParser.getSelling(data.get(i).nickname);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;
    }
}
