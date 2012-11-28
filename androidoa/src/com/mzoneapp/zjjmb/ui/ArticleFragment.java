/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mzoneapp.zjjmb.ui;

import java.net.URLDecoder;

import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.github.ignition.core.tasks.IgnitedAsyncTask;
import com.github.ignition.core.widgets.RemoteImageView;
import com.github.ignition.support.http.IgnitedHttp;
import com.mzoneapp.zjjmb.R;
import com.mzoneapp.zjjmb.api.ApiConstants;
import com.mzoneapp.zjjmb.api.Article;
import com.mzoneapp.zjjmb.util.DateUtil;
import com.mzoneapp.zjjmb.util.ImageUtil;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * Fragment that displays a news article.
 */
public class ArticleFragment extends SherlockFragment {

	View mView;
	ViewPager mPager;
	CirclePageIndicator mIndicator;
	TextView mTitle;
	TextView mDatetime;
	TextView mContent;
	RelativeLayout mLayoutPager;
	View mLoading;

	// The id we are to execute task
	String mArticleId = null;
	
	// Represents a listener that will be notified of ArticleTask
	ArticleTaskListener mArticleTaskListener = null;
	IgnitedAsyncTask<ArticleActivity, String, Void, Article> mArticleTask = null;

	// Parameterless constructor is needed by framework
	public ArticleFragment() {
		super();
	}
	
	public interface ArticleTaskListener {
		void onTaskStarted();
		void onTaskCompleted();
		void onTaskFailed();
	}
	
	 /**
     * Sets the listener that should be notified of headline selection events.
     * @param listener the listener to notify.
     */
    public void setOnArticleTaskListener(ArticleTaskListener listener) {
    	mArticleTaskListener = listener;
    }

	/**
	 * Sets up the UI. It consists if a single WebView.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.activity_article, null);
		mPager = (ViewPager) mView.findViewById(R.id.pager);
		mIndicator = (CirclePageIndicator) mView.findViewById(R.id.indicator);
		mTitle = (TextView) mView.findViewById(R.id.txt_title);
		mDatetime = (TextView) mView.findViewById(R.id.txt_datetime);
		mContent = (TextView) mView.findViewById(R.id.txt_content);
		mLayoutPager = (RelativeLayout) mView.findViewById(R.id.layout_pager);
		mLoading = mView.findViewById(R.id.view_loading);
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mArticleId = Article.fromBundleToArticle(getArguments()).id;
		Article article = Article.fromBundleToArticle(getArguments());
		article.images = null;
		article.desc = "";
		loadArticleView(article);
		executeArticleTask();
	}
	
	public void reflersh(){
		executeArticleTask();
	}

	/**
	 * Loads article data into the article view.
	 * 
	 * This method is called internally to update the webview's contents to the
	 * appropriate article's text.
	 */
	void loadArticleView(Article article) {
		if (null != mView && null != article) {
			mTitle.setText(article.title);
			mDatetime.setText(DateUtil.covertChinaDatetime(article.issuedate, false));
			mContent.setText(Html.fromHtml(article.desc));
			// show image view pager
			if (article.images != null && article.images.length > 0) {
				mLayoutPager.setVisibility(View.VISIBLE);
				ImageFragmentAdapter adapter = new ImageFragmentAdapter(
						getFragmentManager());
				mPager.setOffscreenPageLimit(article.images.length-1);
				adapter.setImages(article.images);
				mPager.setAdapter(adapter);
				if(article.images.length > 1){
						mIndicator.setViewPager(mPager);
						if(getActivity() !=null &&!getActivity().isFinishing()){
							final float density = getResources().getDisplayMetrics().density;
							//				mIndicator.setBackgroundColor(0x40000000);
							mIndicator.setRadius(4 * density);
							mIndicator.setPageColor(0xFFFFFFFF);
							mIndicator.setFillColor(Color.rgb(50,181,229));
						}
	//				mIndicator.setStrokeColor(0xFFFFFFFF);
	//				mIndicator.setStrokeWidth(1 * density);
				}
			}
		}
	}
	
	/**
	 * get article data from network.
	 */
	void executeArticleTask(){
		if(null != mArticleTask) {
			if (mArticleTask.isPending())
				mArticleTask.cancel(true);
		}
		mArticleTask = new IgnitedAsyncTask<ArticleActivity, String, Void, Article>(){
			
			@Override
			public boolean onTaskStarted() {
				mArticleTaskListener.onTaskStarted();
				mLoading.setVisibility(View.GONE);
				return super.onTaskStarted();
			}

			@Override
			public Article run(String... params) throws Exception {
				IgnitedHttp http = new IgnitedHttp(getActivity());
				String body = http.get(ApiConstants.instance().getInfoUrl(params[0]), true).send().getResponseBodyAsString();
				if(body != null){
					try {
						JSONObject obj = new JSONObject(body);
						String id = obj.getString("id");
						String author = obj.getString("author");
						String content = obj.getString("content");
						String title = obj.getString("title");
						String issuedate = obj.getString("issuedate");
						Article article = new Article();
						article.id = id;
						article.author = author;
						article.desc = URLDecoder.decode(content, "utf-8");
						article.images = ImageUtil.getImgStr(article.desc);
						article.desc = ImageUtil.resetImages(article.desc);
						article.title = title;
						article.issuedate = issuedate;
						return article;
					} catch (Exception e) {
						Log.d("debug", "json parse error!");
					}
				}
				return null;
			}

			@Override
			public boolean onTaskCompleted(Article result) {
				mArticleTaskListener.onTaskCompleted();
				mLoading.setVisibility(View.GONE);
				if(null != result){
					loadArticleView(result);
				}
				return super.onTaskCompleted(result);
			}

			@Override
			public boolean onTaskFailed(Exception error) {
				mArticleTaskListener.onTaskFailed();
				mLoading.setVisibility(View.GONE);
				return super.onTaskFailed(error);
			}
			
		};
		mArticleTask.execute(mArticleId);
	}
	
	public static class ImageFragmentAdapter extends FragmentPagerAdapter {

		private String[] mImages = new String[] {};

		private int mCount = 0;

		public ImageFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return ImageFragment.newInstance(mImages[position]);
		}

		public void setImages(String[] images) {
			mImages = images;
			mCount = mImages.length;
		}

		@Override
		public int getCount() {
			return mCount;
		}

		public static class ImageFragment extends Fragment {

			public static final String IMAGE_URL = "image_url";

			public ImageFragment() {
				super();
			}

			/**
			 * Create a new instance of ImageFragment, initialized to show the
			 * text at 'url'.
			 */
			public static ImageFragment newInstance(String url) {
				ImageFragment f = new ImageFragment();

				// Supply index input as an argument.
				Bundle args = new Bundle();
				args.putString(IMAGE_URL, url);
				f.setArguments(args);

				return f;
			}

			public String getImageUrl() {
				return getArguments().getString(IMAGE_URL);
			}

			@Override
			public View onCreateView(LayoutInflater inflater,
					ViewGroup container, Bundle savedInstanceState) {
				if (container == null) {
					// We have different layouts, and in one of them this
					// fragment's containing frame doesn't exist. The fragment
					// may still be created from its saved state, but there is
					// no reason to try to create its view hierarchy because it
					// won't be displayed. Note this is not needed -- we could
					// just run the code below, where we would create and return
					// the view hierarchy; it would just never be used.
					return null;
				}
				return inflater.inflate(R.layout.image_article, container,
						false);
			}

			@Override
			public void onActivityCreated(Bundle savedInstanceState) {
				super.onActivityCreated(savedInstanceState);
				RemoteImageView rImg = ((RemoteImageView) getView()
						.findViewById(R.id.thumbnail));
				String url = getImageUrl();
				if (null != url && !rImg.isLoaded()) {
					rImg.setImageUrl(url);
					rImg.loadImage();
				}
			}

		}

	}

}
