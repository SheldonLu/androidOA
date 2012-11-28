package com.mzoneapp.zjjmb.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.mzoneapp.zjjmb.R;
import com.mzoneapp.zjjmb.bean.TodoDocumentBean;
import com.mzoneapp.zjjmb.ui.MainActivity;
import com.mzoneapp.zjjmb.ui.NewTodoDocumentDetailActivity;

// 待办公文
public class TodoDocumentFragment extends Fragment implements
		OnItemClickListener {
	private Context context;

	public TodoDocumentFragment() {
	}

	public TodoDocumentFragment(Context context) {
		this.context = context;
	}

	private ListView mListView;
	private SimpleAdapter mAdapter;
	private String[] mFrom;
	private int[] mTo;
	private List<Map<String, String>> mData = new ArrayList<Map<String, String>>();
	
	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tododocument, null);
		mListView = (ListView) view.findViewById(R.id.listview);
		mTo = new int[] { R.id.title_txt, R.id.datetime_txt, R.id.content_txt };
		mFrom = new String[] { "title_txt", "datetime_txt", "content_txt" };
		


//		map.put(mFrom[0], "政协换届以来工作总结");
//		map.put(mFrom[1], "12-10-21 10:12:44");
//		map.put(mFrom[2], "已阅");
//		mData.add(map);
//		map = new HashMap<String, String>();
//		map.put(mFrom[0], "在党校学习贯彻十八大精神会议上的讲话");
//		map.put(mFrom[1], "12-10-22 11:13:41");
//		map.put(mFrom[2], "组织学习");
//		mData.add(map);
//		map = new HashMap<String, String>();
//		map.put(mFrom[0], "信用社主任完成全年工作任务表态发言");
//		map.put(mFrom[1], "12-10-21 10:12:44");
//		map.put(mFrom[2], "总结发言");
//		mData.add(map);
		Map<String, String> map = null;
	
		int length = MainActivity.docBeans.size();
		for(int i = 0;i<length;i++){
			map = new HashMap<String, String>();
			TodoDocumentBean tb = MainActivity.docBeans.get(i);
			map.put(mFrom[0], tb.title);
			map.put(mFrom[1], tb.time);
			map.put(mFrom[2], tb.suggess);
			mData.add(map);
		}
		
		mAdapter = new MySimpleAdapter(context, mData,
				R.layout.tododocument_item, mFrom, mTo);
		mListView.setAdapter(mAdapter);
		mListView.setCacheColorHint(0);
		mListView.setDividerHeight(0);
		mListView.setOnItemClickListener(this);
		mAdapter.notifyDataSetChanged();
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long arg3) {

		if(position<mData.size()){
			
			//MainActivity.clickBean = MainActivity.docBeans.get(position);
			
			Intent intent = new Intent(context,NewTodoDocumentDetailActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right); 
			
		}
	}
	
	
	public ArrayList<TodoDocumentBean> testData(){
		TodoDocumentBean bean=new TodoDocumentBean();
		return null;
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
