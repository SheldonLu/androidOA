package com.mzoneapp.zjjmb.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.mzoneapp.zjjmb.R;
import com.mzoneapp.zjjmb.ui.ArticleFragment.ArticleTaskListener;

public class ArticleActivity extends SherlockFragmentActivity implements
		ArticleTaskListener {

	private boolean useLogo = true;
	private boolean showHomeUp = true;
	
	ArticleFragment mAf = null;
	boolean mRefresh = false; 

	// The news the article index for the article we are to
	// display
	int mArtIndex;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// This has to be called before setContentView and you must use the
		// class in com.actionbarsherlock.view and NOT android.view
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		// If we are in two-pane layout mode, this activity is no longer
		// necessary
		if (getResources().getBoolean(R.bool.has_two_panes)) {
			finish();
			return;
		}
		final ActionBar ab = getSupportActionBar();
		// set defaults for logo & home up
		ab.setDisplayHomeAsUpEnabled(showHomeUp);
		ab.setDisplayUseLogoEnabled(useLogo);
		ab.setDisplayShowTitleEnabled(true);
		
		// Place an ArticleFragment as our content pane
		mAf = new ArticleFragment();
		mAf.setOnArticleTaskListener(this);
		getSupportFragmentManager().beginTransaction()
				.add(android.R.id.content, mAf).commit();
		// Display the correct news article on the fragment
		Bundle bd = getIntent().getExtras();
		String type = bd.getString("type");
		if (null != type) {
			try {
				switch (Integer.parseInt(type)) {
				case 1:
					setTitle("通知公告");
					break;
				case 2:
					setTitle("局内动态");
					break;
				case 3:
					setTitle("工作动态");
					break;
				default:
					finish();
					return;
				}
				mAf.setArguments(bd);
			} catch (Exception e) {
				Log.d("d", e.getMessage());
				finish();
				return;
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.putExtra("id", mAf.mArticleId);
		this.setResult(1, intent);
		finish();
		overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_down);  
//		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.activity_article, menu);
		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
//			Intent intent = new Intent(this, MainActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
			onBackPressed();
			return true;
		case R.id.menu_reflesh:
			mAf.reflersh();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	@Override
	public boolean onPrepareOptionsMenu (Menu menu) {
	    if (mRefresh)
	        menu.getItem(0).setVisible(false);
	    else
	    	menu.getItem(0).setVisible(true);
	    return true;
	}

	@Override
	public void onTaskStarted() {
		 setSupportProgress(Window.PROGRESS_END);
		 setProgressBar(true);
	}

	@Override
	public void onTaskCompleted() {
		setProgressBar(false);
	}

	@Override
	public void onTaskFailed() {
		setProgressBar(false);
	}

	private void setProgressBar(boolean refresh) {
		setSupportProgressBarIndeterminateVisibility(mRefresh = refresh);
		 invalidateOptionsMenu();
	}

}
