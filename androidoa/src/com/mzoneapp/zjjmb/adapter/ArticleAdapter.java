package com.mzoneapp.zjjmb.adapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ignition.core.adapters.EndlessListAdapter;
import com.github.ignition.core.widgets.RemoteImageView;
import com.mzoneapp.zjjmb.R;
import com.mzoneapp.zjjmb.api.Article;

public class ArticleAdapter extends EndlessListAdapter<Article> {
	private static final String LOG_TAG = ArticleAdapter.class.getName();
	private final LayoutInflater inflater;
	private long lastUpdate = -1;
	private Activity activity;
	private Resources rs;
	private SharedPreferences settings;

	public ArticleAdapter(Activity activity,AbsListView listView) {
		super(activity, listView, R.layout.loading_item);
		this.activity = activity;
		rs = activity.getResources();
		settings = activity.getSharedPreferences("articel_read_ids", 0);
		inflater = LayoutInflater.from(activity);
	}

	private List<Article> headlines;
	private boolean endReached = false;

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
//			if (msg.what >= 0) {
//				if (headlines != null) {
//					lastUpdate = System.currentTimeMillis();
//					remove(null);
//					for (Headline s : headlines) {
//						if (getPosition(s) < 0) {
//							add(s);
//						}
//					}
//					if (!endReached) {
//						add(null);
//					}
//				}
//				// TODO:callback
//
//			} else {
//				// TODO:error
//			}
		}
	};

	public void addMoreHeadlines(final String url, final int count) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (getMoreHeadlines(url, count)) {
					handler.sendEmptyMessage(0);
				} else {
					handler.sendEmptyMessage(-1);
				}
			}
		}).start();
	}

	private boolean getMoreHeadlines(String url, int count) {
		// TODO
		return true;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	protected View doGetView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_headline, parent,
					false);
		}

		Article article = getItem(position);

		RemoteImageView image = (RemoteImageView) convertView.findViewById(R.id.image);
		LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.layout_image);
		if(article.images != null){
			layout.setVisibility(View.VISIBLE);
			if(!image.isLoaded()){
				image.setImageUrl(article.images[0]);
				image.loadImage();
			}
		}else{
			layout.setVisibility(View.GONE);
		}
		
		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView desc = (TextView) convertView.findViewById(R.id.desc);
		TextView date = (TextView) convertView.findViewById(R.id.dateText);

		title.setText(article.title);
//		desc.setText(headline.desc);
		desc.setText(article.desc);
		String id = article.id;
		boolean flag = settings.getBoolean(id, false);
		title.setTextColor(rs.getColor(R.color.color_list_title));
		if(flag){
			// 已读
			title.setTextColor(rs.getColor(R.color.list_title_text_selector_read));
		}else{
			// 最新
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				long l = (new Date().getTime()) - df.parse(article.issuedate).getTime();
				if(l/1000/24/3600 < 10){
					title.setTextColor(rs.getColor(R.color.list_title_text_selector_new));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			
		}
		date.setText(article.issuedate.split(" ")[0]);

		return convertView;
	}
}
