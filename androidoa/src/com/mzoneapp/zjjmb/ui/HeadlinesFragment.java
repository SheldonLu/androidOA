package com.mzoneapp.zjjmb.ui;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.github.ignition.core.tasks.IgnitedAsyncTask;
import com.github.ignition.support.http.IgnitedHttp;
import com.github.ignition.support.http.IgnitedHttpResponse;
import com.mzoneapp.ui.widget.PullToRefreshListView;
import com.mzoneapp.zjjmb.R;
import com.mzoneapp.zjjmb.adapter.ArticleAdapter;
import com.mzoneapp.zjjmb.api.ApiConstants;
import com.mzoneapp.zjjmb.api.Article;

/**
 * Fragment that displays the news headlines for a particular news category.
 *
 * This Fragment displays a list with the news headlines for a particular news category.
 * When an item is selected, it notifies the configured listener that a headlines was selected.
 */
public class HeadlinesFragment extends SherlockListFragment implements OnItemClickListener,OnScrollListener {
	
    // The list adapter for the list we are displaying
    ArticleAdapter adapter;
    
    private IgnitedHttp http;
    
    private String type;
//    private boolean isLoaded = false;
    private boolean isNull = false;

    // The listener we are to notify when a headline is selected
    OnHeadlineSelectedListener mHeadlineSelectedListener = null;
    
    OnRefreshCallBack onRefeshCallBack;
    
	public interface OnRefreshCallBack {
		void setProgressBar(boolean refresh);
	}

    /**
     * Represents a listener that will be notified of headline selections.
     */
    public interface OnHeadlineSelectedListener {
        /**
         * Called when a given headline is selected.
         * @param index the index of the selected headline.
         */
        public void onHeadlineSelected(int index);
    }
    
    public void setRefreshCallBack(OnRefreshCallBack onRefeshCallBack) {
    	this.onRefeshCallBack = onRefeshCallBack;
    }

    /**
     * Default constructor required by framework.
     */
    public HeadlinesFragment() {
        super();
    }
    
//    public HeadlinesFragment(Activity activity){
//    }
//
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.main_list, null);
    	
    	return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
//    	if(isLoaded) return;
    	
    	type = getArguments().getString(ApiConstants.TYPE);
    	http = new IgnitedHttp(getActivity());
    	ListView listView = getListView();
//    	listView.setCacheColorHint(0);
//    	listView.setDivider(null);
    	adapter = new ArticleAdapter(getActivity(),getListView());
    	
    	setListAdapter(adapter);
    	listView.setOnItemClickListener(this);
    	listView.setOnScrollListener(this);
    	((PullToRefreshListView) listView).setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			@Override
        	public void onRefresh() {
				refresh();
            }
        });
        loadNextPage(true);
        
//        isLoaded = true;
    }
    
    
    private void loadNextPage(final boolean clearBefore) {
    	if(!clearBefore)
    		adapter.setIsLoadingData(true);
    	else{
    		if(null != this.onRefeshCallBack){
    			this.onRefeshCallBack.setProgressBar(true);
    		}
    	}
    	
        IgnitedAsyncTask<MainActivity, Void, Void, Void> task = new IgnitedAsyncTask<MainActivity, Void, Void, Void>() {
            @Override
            public Void run(Void... params) throws Exception {
            	int start = adapter.getItemCount();
            	int pageno = start / ApiConstants.DEFAULT_SIZE + 1;
            	// refresh when clearBefore is true
            	if(clearBefore) pageno = 1;
            	String url = ApiConstants.instance().
            			getListUrl(type, pageno);
            	IgnitedHttpResponse response =  http.get(url).retries(3).expecting(200).send();
            	String responseBody = response.getResponseBodyAsString();
            	
//            	Object result = null;
         		responseBody = responseBody.trim();
//         		if(responseBody.startsWith("{") || responseBody.startsWith("[")) {
//         			result = new JSONTokener(responseBody).nextValue();
//         		}
         		
         		JSONObject tmp = new JSONObject(responseBody);
         		JSONArray jsonArray = tmp.getJSONArray("items");
         		
                int length = jsonArray.length();
                // reset isNull;
                isNull = false;
                // no result, not load next time
                if( 0 == length) {
                	isNull = true;
                	return null;
                }
//                if( length < ApiConstants.DEFAULT_SIZE) isNull = true;
                ArrayList<Article> list = new ArrayList<Article>();
                for(int i = 0; i < length; i++){
                	JSONObject jsonObject = jsonArray.getJSONObject(i);
                	Article line = new Article();
                	line.id = jsonObject.getString("id");
                	line.author = jsonObject.getString("author");
                	line.issuedate = jsonObject.getString("issuedate");
                	line.title = jsonObject.getString("title");
                	line.type = jsonObject.getString("type");
                	String imgurl = jsonObject.getString("imgurl");
                	
                	if( null != imgurl &&imgurl.startsWith("http"))
                		line.images = new String[]{imgurl};
                	
                	line.desc = URLDecoder.decode(jsonObject.getString("content"), "utf-8");
                	list.add(line);
                }
                
                if(clearBefore)
                	adapter.getData().clear();
                
                adapter.getData().addAll(list);
             // TODO 更完善的异常处理
//                isNull = true;
//            	throw new JSONException("Unexpected type " + result.getClass().getName());
         		
                return null;
            }

            @Override
            public boolean onTaskCompleted(Void result) {
            	onRefreshComplete();
                adapter.setIsLoadingData(false);
                adapter.notifyDataSetChanged();
                if(clearBefore){ 
                	getListView().setSelection(0);
            		if(null != onRefeshCallBack){
            			onRefeshCallBack.setProgressBar(false);
            		}
                }
                return true;
            }
            
            @Override
            public boolean onTaskFailed(Exception ex){
            	// TODO
            	Article line = new Article();
        		line.id = "1";
        		line.title = "Error";
        		line.author = "";
        		line.issuedate = "";
        		line.desc = "网络数据加载错误";
        		line.type = type;
            	
            	adapter.getData().add(line);
            	return true;
            }
        };
        task.execute();
    }
    
    private void onRefreshComplete(){
//    	if(getListView().isEnabled())
//    		((PullToRefreshListView)getListView()).onRefreshComplete("最近更新："+new Date().toLocaleString());
    }
    
    public void refresh(){
    	loadNextPage(true);
    }

    /**
     * Sets the listener that should be notified of headline selection events.
     * @param listener the listener to notify.
     */
    public void setOnHeadlineSelectedListener(OnHeadlineSelectedListener listener) {
        mHeadlineSelectedListener = listener;
    }

    /**
     * Handles a click on a headline.
     *
     * This causes the configured listener to be notified that a headline was selected.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mHeadlineSelectedListener) {
            mHeadlineSelectedListener.onHeadlineSelected(position);
        }
        if(adapter.getData().size() > position -1){
        	Article article = adapter.getData().get(position-1);
        	Intent i = new Intent(getActivity(), ArticleActivity.class);
        	i.putExtras(Article.convertArticleToBundle(article));
//        	startActivity(i);
        	startActivityForResult(i, 1);
        	getActivity().overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_down);  
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if(requestCode ==1 && resultCode ==1 && data != null){
    		String mArticleId = data.getStringExtra("id");		
    		SharedPreferences settings = getActivity().getSharedPreferences("articel_read_ids", 0);
    		boolean flag = settings.getBoolean(mArticleId, false);
    		if(!flag){
    			settings.edit().putBoolean(mArticleId, true).commit();
    			adapter.notifyDataSetChanged();
    		}
    		
    	}
    }

    /** Sets choice mode for the list
     *
     * @param selectable whether list is to be selectable.
     */
    public void setSelectable(boolean selectable) {
        if (selectable) {
        	getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
        else {
        	getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);
        }
    }

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		((PullToRefreshListView)getListView()).onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        if (!isNull && adapter.shouldRequestNextPage(firstVisibleItem, visibleItemCount, totalItemCount)) {
            loadNextPage(false);
        }
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		((PullToRefreshListView)getListView()).onScrollStateChanged(view, scrollState);
		
	}

}
