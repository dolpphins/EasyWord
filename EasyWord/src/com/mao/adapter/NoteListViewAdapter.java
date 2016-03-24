package com.mao.adapter;

import java.util.List;

import com.mao.bean.Note;
import com.mao.easyword.R;
import com.mao.utils.TimeUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class NoteListViewAdapter extends BaseAdapter {

	private Context mContext;
	private List<Note> mNoteList;
	
	public NoteListViewAdapter(Context context, List<Note> noteList) {
		mContext = context;
		mNoteList = noteList;
	}

	@Override
	public int getCount() {
		if(mNoteList != null) {
			return mNoteList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return mNoteList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.note_list_item, null);
			holder = new ViewHolder();
			holder.note_list_item_title = (TextView) convertView.findViewById(R.id.note_list_item_title);
			holder.note_list_item_content = (TextView) convertView.findViewById(R.id.note_list_item_content);
			holder.note_list_item_time = (TextView) convertView.findViewById(R.id.note_list_item_time);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Note note = mNoteList.get(position);
		setItemView(holder, note);
		
		return convertView;
	}
	
	private void setItemView(ViewHolder holder, Note note) {
		holder.note_list_item_title.setText(note.getTitle());
		holder.note_list_item_content.setText(note.getContent());
		holder.note_list_item_time.setText(TimeUtils.timestamp2String(note.getUpdateTime()));
		
	}
	
	private static class ViewHolder {
		
		private TextView note_list_item_title;
		private TextView note_list_item_content;
		private TextView note_list_item_time;
	}
}
