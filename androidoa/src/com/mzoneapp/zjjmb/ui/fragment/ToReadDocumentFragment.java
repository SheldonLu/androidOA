package com.mzoneapp.zjjmb.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.mzoneapp.zjjmb.R;
//待阅公文
public class ToReadDocumentFragment extends Fragment {
	private Context context;

	public ToReadDocumentFragment() {
	}

	public ToReadDocumentFragment(Context context) {
		this.context = context;
	}

	private ListView mListView;
	private SimpleAdapter mAdapter;
	private String[] mFrom;
	private int[] mTo;
	private List<Map<String, String>> mData;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tododocument, null);
		mListView = (ListView) view.findViewById(R.id.listview);
		mListView.setDividerHeight(0);
		mListView.setCacheColorHint(0);
		mTo = new int[] { R.id.title_txt, R.id.datetime_txt, R.id.content_txt };
		mFrom = new String[] { "title_txt", "datetime_txt", "content_txt" };
		mData = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();

		map.put(mFrom[0], "关于网点标准化改造工作进展情况的汇报");
		map.put(mFrom[1], "2012-10-21 10:12:44");
		map.put(mFrom[2], "");
		mData.add(map);
		map = new HashMap<String, String>();
		map.put(mFrom[0], "在全县信用社旺季工作大会上的讲话");
		map.put(mFrom[1], "2012-10-21 10:12:44");
		map.put(mFrom[2], "阅");
		mData.add(map);
		map = new HashMap<String, String>();
		map.put(mFrom[0], "钢铁企业劳务外包存在的问题及解决对策");
		map.put(mFrom[1], "2012-10-21 10:12:44");
		map.put(mFrom[2], "");
		mData.add(map);

		mAdapter = new MySimpleAdapter(context, mData,
				R.layout.tododocument_item, mFrom, mTo);
		mListView.setAdapter(mAdapter);
		return view;
	}

	 private class MySimpleAdapter extends SimpleAdapter{
			
		public MySimpleAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView=super.getView(position, convertView, parent);
			if(convertView!=null){
				if(mData.get(position).get(mFrom[2])==null || mData.get(position).get(mFrom[2]).length()==0 ){
					convertView.findViewById(R.id.rawcontentlayout).setVisibility(View.GONE);
				}else{
					convertView.findViewById(R.id.rawcontentlayout).setVisibility(View.VISIBLE);
				}
			}
			return convertView;
		}
		
	} 
}
