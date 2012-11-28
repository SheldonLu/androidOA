package com.mzoneapp.zjjmb.ui;

import java.util.ArrayList;

import net.simonvt.widget.MenuDrawer;
import net.simonvt.widget.MenuDrawerManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.mzoneapp.zjjmb.R;
import com.mzoneapp.zjjmb.api.ApiConstants;
import com.mzoneapp.zjjmb.bean.TodoDocumentBean;
import com.mzoneapp.zjjmb.ui.HeadlinesFragment.OnRefreshCallBack;
import com.mzoneapp.zjjmb.ui.fragment.EmailFragment;
import com.mzoneapp.zjjmb.ui.fragment.SearchFragment;
import com.mzoneapp.zjjmb.ui.fragment.ToReadDocumentFragment;
import com.mzoneapp.zjjmb.ui.fragment.TodoDocumentFragment;

public class MainActivity extends SherlockFragmentActivity implements
		CompatActionBarNavListener,OnRefreshCallBack,View.OnClickListener{
	public static ArrayList<TodoDocumentBean> docBeans;

	static{
		docBeans = new ArrayList<TodoDocumentBean>();
		TodoDocumentBean tb = new TodoDocumentBean();
		tb.title = " 富阳市人民政府关于富阳市农村村民建房管理的若干意见（试行）";
		tb.time = "2012年11月06日";
		tb.suggess = "请会签！  徐林  2012年11月28日10时32分";
		tb.dengji = "等级1";
		tb.desc = "今年以来，按照县委县政府的总体部署和县政协常委会工作要点安排，县政协认真履行工作";
		tb.jinbanren = "徐林 ";
		
		docBeans.add(tb);
	
	}
	private boolean useLogo = true;
	private boolean showHomeUp = false;

	private HeadlinesFragment mHeadlinesFragment = null;
	private ArticleFragment mArticleFragment = null;

	// Whether or not we are in dual-panel mode
	boolean mIsDualPane = false;

	// The news category and article index currently being displayed
	int mCatIndex;
	int mArtIndex;

	TabsAdapter mTabsAdapter;
//	ViewPager mViewPager;
	
	boolean mRefresh = false; 
	
	 private static final String STATE_MENUDRAWER = "net.simonvt.menudrawer.samples.ContentSample.menuDrawer";
    private static final String STATE_ACTIVE_POSITION = "net.simonvt.menudrawer.samples.ContentSample.activePosition";

    private int mActivePosition = -1;
    private MenuDrawerManager mMenuDrawer;
    
    private ViewPager mViewPager;
	Button bt1 = null;
	Button bt2 = null;
	
	LinearLayout top = null;
	
	void init(Bundle savedInstanceState){
        if (savedInstanceState != null) {
            mActivePosition = savedInstanceState.getInt(STATE_ACTIVE_POSITION);
        }

        mMenuDrawer = new MenuDrawerManager(this, MenuDrawer.MENU_DRAG_WINDOW);
        mMenuDrawer.setContentView(R.layout.activity_viewpagersample);
        
        mMenuDrawer.setMenuView(R.layout.menu_scrollview);
        
		MenuScrollView msv = (MenuScrollView) mMenuDrawer.getMenuView();
		msv.setOnScrollChangedListener(new MenuScrollView.OnScrollChangedListener() {
			@Override
			public void onScrollChanged() {
				mMenuDrawer.getMenuDrawer().invalidate();
			}
		});
		
		findViewById(R.id.item1).setOnClickListener(this);
		findViewById(R.id.item2).setOnClickListener(this);
		findViewById(R.id.item3).setOnClickListener(this);
		findViewById(R.id.item4).setOnClickListener(this);
		findViewById(R.id.item5).setOnClickListener(this);
		findViewById(R.id.item6).setOnClickListener(this);
		findViewById(R.id.item7).setOnClickListener(this);

        mMenuDrawer.getMenuDrawer().setTouchMode(
                MenuDrawer.TOUCH_MODE_FULLSCREEN);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager
                .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(final int position) {
                        mMenuDrawer
                                .getMenuDrawer()
                                .setTouchMode(
                                        (position == 0) ? MenuDrawer.TOUCH_MODE_FULLSCREEN
                                                : MenuDrawer.TOUCH_MODE_NONE);
                        if(position == 0){
                        	bt1.performClick();
                        }else if(position == 1){
                        	bt2.performClick();
                        }
                    }
                });

        
		mTabsAdapter = new TabsAdapter(this, mViewPager);
		
//		final ActionBar ab = getSupportActionBar();
//		
//		Bundle bundle = new Bundle();
//		bundle.putString(ApiConstants.TYPE, ApiConstants.ANNOUNCEMENT);
//		HeadlinesFragment fr1 = new HeadlinesFragment();
//		fr1.setArguments(bundle);
//		mTabsAdapter.addTab(fr1);
////		mTabsAdapter.addTab(ab.newTab().setText("通知公告"),
////				HeadlinesFragment.class, bundle);
//		bundle = new Bundle();
//		bundle.putString(ApiConstants.TYPE, ApiConstants.IN_DYNAMIC);
//		fr1 = new HeadlinesFragment();
//		fr1.setArguments(bundle);
//		mTabsAdapter.addTab(fr1);
//		mTabsAdapter.addTab(ab.newTab().setText("局内动态"),
//				HeadlinesFragment.class, bundle);
		
		
		bt1 = (Button)findViewById(R.id.btn_id1);
		bt2 = (Button)findViewById(R.id.btn_id2);
		top = (LinearLayout)findViewById(R.id.top);
		bt1.setEnabled(false);
		
		bt1.setOnClickListener(frameBtnClick(bt1));
		
		bt2.setOnClickListener(frameBtnClick(bt2));
		
		mMenuDrawer.getMenuDrawer().peekDrawer();
		mMenuDrawer.getMenuDrawer().setDropShadowEnabled(false);
		findViewById(R.id.item2).performClick();
		
//	        mViewPager.setAdapter(mTabsAdapter);
		}
	
	private View.OnClickListener frameBtnClick(final Button btn){
		return new View.OnClickListener(){
			public void onClick(View v) {
				if(v == bt1){
					mViewPager.setCurrentItem(0);
					bt1.setEnabled(false);
				}
				else{
					
					bt1.setEnabled(true);
				}
				
				if(v == bt2){
					mViewPager.setCurrentItem(1);
					bt2.setEnabled(false);
				}
				else
					bt2.setEnabled(true);
			}
		};
	}
		
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		super.onCreate(savedInstanceState);
		ApiConstants.createInstance();
		// TODO: 更改创建时间
		init(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
//		setContentView(R.layout.main_layout);

		// find our fragments
		mArticleFragment = (ArticleFragment) getSupportFragmentManager()
				.findFragmentById(R.id.article);

		// Determine whether we are in single-pane or dual-pane mode by testing
		// the visibility
		// of the article view.
		View articleView = findViewById(R.id.article);
		mIsDualPane = articleView != null
				&& articleView.getVisibility() == View.VISIBLE;
		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		// Register ourselves as the listener for the headlines fragment events.
		// mHeadlinesFragment.setOnHeadlineSelectedListener(this);

		// Set up headlines fragment
		// mHeadlinesFragment.setSelectable(mIsDualPane);
		// restoreSelection(savedInstanceState);

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
	public void setProgressBar(boolean refresh) {
		setSupportProgressBarIndeterminateVisibility(mRefresh = refresh);
		invalidateOptionsMenu();
	}

	/** Restore category/article selection from saved state. */
	void restoreSelection(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			setNewsCategory(savedInstanceState.getInt("catIndex", 0));
			if (mIsDualPane) {
				int artIndex = savedInstanceState.getInt("artIndex", 0);
//				mHeadlinesFragment.setSelection(artIndex);
				// onHeadlineSelected(artIndex);
			}
		}
	}

	/**
	 * Sets the displayed news category.
	 * 
	 * This causes the headlines fragment to be repopulated with the appropriate
	 * headlines.
	 */
	void setNewsCategory(int catIndex) {
		mCatIndex = catIndex;
		// mHeadlinesFragment.loadCategory(catIndex);

		// If we are displaying the article on the right, we have to update that
		// too
		if (mIsDualPane) {
//			mArticleFragment.displayArticle(null);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		setNewsCategory(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private void showTabsNav() {
		ActionBar ab = getSupportActionBar();
		if (ab.getNavigationMode() != ActionBar.NAVIGATION_MODE_TABS) {
			ab.setDisplayShowTitleEnabled(true);
			ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		}
	}

	// @Override
	// public void onHeadlineSelected(int index) {
	// mArtIndex = index;
	// if (mIsDualPane) {
	// // display it on the article fragment
	// mArticleFragment.displayArticle(null);
	// } else {
	// // use separate activity
	// Intent i = new Intent(this, ArticleActivity.class);
	// i.putExtra("catIndex", mCatIndex);
	// i.putExtra("artIndex", index);
	// startActivity(i);
	// }
	// }

	@Override
	public void onCategorySelected(int catIndex) {
		setNewsCategory(catIndex);
	}

	/** Save instance state. Saves current category/article index. */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("catIndex", mCatIndex);
		outState.putInt("artIndex", mArtIndex);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if(mMenuDrawer.getDrawerState() == MenuDrawer.STATE_OPEN){
				mMenuDrawer.closeMenu();
			}else {
				mMenuDrawer.openMenu();
			}
			return true;
		case R.id.menu_reflesh:
			mTabsAdapter.refresh();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public class TabsAdapter extends FragmentPagerAdapter {
		private final Context mContext;
//		private final ActionBar mActionBar;
		private final ViewPager mViewPager;
//		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
		private final ArrayList<Fragment> fragments = new ArrayList<Fragment>();

		final class TabInfo {
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(Class<?> _class, Bundle _args) {
				clss = _class;
				args = _args;
			}
		}

		public TabsAdapter(SherlockFragmentActivity activity, ViewPager pager) {
			super(activity.getSupportFragmentManager());
			mContext = activity;
//			mActionBar = activity.getSupportActionBar();
			mViewPager = pager;
			mViewPager.setAdapter(this);
		}
		
		public void destroyAllItem() {
//	        int mPosition = mViewPager.getCurrentItem();

	            int mPositionMax = getCount();
	            for (int i = 0; i < mPositionMax; i++) {
	                try {
	                    Object objectobject = this.instantiateItem(mViewPager, i);
	                    if (objectobject != null)
	                        destroyItem(mViewPager, i, objectobject);
	                } catch (Exception e) {
	                }
	            }
	            
	            this.fragments.clear();
//	            mViewPager.setOnPageChangeListener(null);
	    }

	    @Override
	    public void destroyItem(ViewGroup container, int position, Object object) {
	        super.destroyItem(container, position, object);

	        FragmentManager manager = ((Fragment) object).getFragmentManager();
	        if (position <= getCount() && manager != null) {
	            FragmentTransaction trans = manager.beginTransaction();
	            trans.remove((Fragment) object);
	            trans.commit();
	        }
	    }
	    
	    public void addTab(Fragment fragment){
	    	fragments.add(fragment);
	    	notifyDataSetChanged();
	    }
		

//		public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
//			TabInfo info = new TabInfo(clss, args);
//			tab.setTag(info);
//			mTabs.add(info);
//			mActionBar.addTab(tab);
//			notifyDataSetChanged();
//		}
		
		public void refresh(){
			Fragment fragment = (Fragment)instantiateItem(mViewPager, mViewPager.getCurrentItem());
			if(fragment instanceof HeadlinesFragment){
				((HeadlinesFragment)fragment).refresh();
				
			}
			//			mViewPager.gett
		}
		
		@Override  
		public int getItemPosition(Object object) {  
		   return POSITION_NONE;  
		}  

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public Fragment getItem(int position) {
//			TabInfo info = mTabs.get(position);
//			Fragment fragment = Fragment.instantiate(mContext, info.clss.getName(),
//					info.args);
			Fragment fragment = fragments.get(position);
			
			if(fragment instanceof HeadlinesFragment){
				((HeadlinesFragment)fragment).setRefreshCallBack(MainActivity.this);
			}
			
			return fragment;
		}

	}

	@Override
	public void onClick(View v) {
		mMenuDrawer.setActiveView(v);
		mMenuDrawer.closeMenu();
		ActionBar ab = getSupportActionBar();
		Bundle bundle = null;
		mTabsAdapter.destroyAllItem();
		switch (v.getId()) {
		case R.id.item1:
			// 公文查询
			setTitle("公文查询");
			SearchFragment search = new SearchFragment();
			search.setArguments(bundle);
			mTabsAdapter.addTab(search);
			bt1.setText("公文查询");
			mRefresh = true;
			invalidateOptionsMenu();
			break;
		case R.id.item2:
			// 通知公告
			setTitle("通知公告");
			
			break;
		case R.id.item3:
		
			// 我的待办
						setTitle("我的待办");
						TodoDocumentFragment todoDocumentFragment = new TodoDocumentFragment(this);
						mTabsAdapter.addTab(todoDocumentFragment);
						bt1.setText("待办事宜");
						ToReadDocumentFragment toReadDocumentFragment=new ToReadDocumentFragment(this);
						mTabsAdapter.addTab(toReadDocumentFragment);
						bt2.setText("待阅通知");
			break;
		case R.id.item4:
			// 动态信息
			setTitle("动态信息");
			
			bundle = new Bundle();
			//局内动态
			bundle.putString(ApiConstants.TYPE, ApiConstants.IN_DYNAMIC);
			HeadlinesFragment fr1 = new HeadlinesFragment();
			fr1.setArguments(bundle);
			mTabsAdapter.addTab(fr1);
			bt1.setText("局内动态");
			
			//工作动态
			bundle = new Bundle();
			bundle.putString(ApiConstants.TYPE, ApiConstants.WORK_DYNAMIC);
			fr1 = new HeadlinesFragment();
			fr1.setArguments(bundle);
			mTabsAdapter.addTab(fr1);
			bt2.setText("工作动态");

			
			break;
		case R.id.item5:
			// 电子邮件
			setTitle("电子邮件");
			EmailFragment email = new EmailFragment();
			email.setArguments(bundle);
			mTabsAdapter.addTab(email);
			bt1.setText("电子邮件");
			
			break;
		case R.id.item6:
			// 信息刊物
			setTitle("信息刊物");
			break;
		case R.id.item7:
			// 通讯录
			setTitle("全市通讯录");
			break;
		default:
			break;
		}
		
		
		if(mTabsAdapter.getCount() == 2){
			top.setVisibility(View.VISIBLE);
		}else{
			top.setVisibility(View.GONE);
		}
		
//		mTabsAdapter.notifyDataSetChanged();
//		getSupportFragmentManager().beginTransaction()
//				.replace(R.id.content_frame, newContent).commit();
	}

}
