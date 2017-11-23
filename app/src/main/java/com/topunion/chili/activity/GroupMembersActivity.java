package com.topunion.chili.activity;

import android.app.Activity;
import android.text.TextUtils;
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
import android.widget.Toast;

import com.topunion.chili.R;
import com.topunion.chili.view.CharacterParser;
import com.topunion.chili.view.PinyinComparator;
import com.topunion.chili.wight.SideBar;
import com.topunion.chili.view.SortAdapter;
import com.topunion.chili.data.SortModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@EActivity(R.layout.activity_sort_person)
public class GroupMembersActivity extends BaseAppCompatActivity {
	private SortAdapter adapter; // 排序的适配器

	private CharacterParser characterParser;
	private List<SortModel> SourceDateList; // 数据

	private PinyinComparator pinyinComparator;
	private int lastFirstVisibleItem = -1;

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
				Toast.makeText(getApplication(),
						((SortModel) adapter.getItem(position)).getName(),
						Toast.LENGTH_SHORT).show();
			}
		});

		SourceDateList = filledData(new String[]{"xx群成员","bb群成员","ll群成员","aa群成员","ii群成员","zz群成员","oo群成员","cc群成员",
				"bb群成员","ll群成员","aa群成员","ii群成员","zz群成员","bb群成员","ll群成员","aa群成员","ii群成员","zz群成员"});

		Collections.sort(SourceDateList, pinyinComparator);
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

	private List<SortModel> filledData(String[] date) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for (int i = 0; i < date.length; i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			sortModel.setSex(i % 2);
			String pinyin = characterParser.getSelling(date[i]);
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

	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			for (SortModel sortModel : SourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

}
