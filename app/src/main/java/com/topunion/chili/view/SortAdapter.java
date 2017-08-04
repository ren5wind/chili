package com.topunion.chili.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.topunion.chili.R;
import com.topunion.chili.data.SortModel;

import java.util.List;

/**
 * @author J 适配器
 */
public class SortAdapter extends BaseAdapter implements SectionIndexer {
	private List<SortModel> list = null;
	private Context mContext;

	public SortAdapter(Context mContext, List<SortModel> list) {
		this.mContext = mContext;
		this.list = list;
	}

	public void updateListView(List<SortModel> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return (list == null)?0:list.size();
	}

	public Object getItem(int position) {
		return (list == null)?null:list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if(list == null){
			return null;
		}
		final SortModel mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.friends_sort_list_item, null);
			viewHolder.tvTitle = (TextView) view
					.findViewById(R.id.txt_name);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.icon = (ImageView) view
					.findViewById(R.id.img_header);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

//		int section = getSectionForPosition(position);

//		if (position == getPositionForSection(section)) {
//			viewHolder.tvLetter.setVisibility(View.VISIBLE);
//			viewHolder.tvLetter.setText(mContent.getSortLetters());
//		} else {
			viewHolder.tvLetter.setVisibility(View.GONE);
//		}
		SortModel model = list.get(position);

		viewHolder.tvTitle.setText(model.getName());
		return view;

	}

	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
		ImageView icon;
	}

	/**
	 * 得到首字母的ascii值
	 */
	public int getSectionForPosition(int position) {
		if(list == null){
			return 0;
		}

		return list.get(position).getSortLetters().charAt(0);
	}

	public int getPositionForSection(int section) {
		if(list == null){
			return -1;
		}
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	public String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}