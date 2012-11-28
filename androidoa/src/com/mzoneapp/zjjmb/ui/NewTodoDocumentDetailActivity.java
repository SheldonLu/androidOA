package com.mzoneapp.zjjmb.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.mzoneapp.zjjmb.R;
import com.mzoneapp.zjjmb.util.FileUtil;

public class NewTodoDocumentDetailActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.newtododoc_detail);
		
		final ActionBar ab = getSupportActionBar();
		// set defaults for logo & home up
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayUseLogoEnabled(true);
		ab.setDisplayShowTitleEnabled(true);
		setTitle("待办事宜详情");
		
		TextView attan = (TextView) findViewById(R.id.btn_attachment);
		// attan.setText(Html.fromHtml("<u>富阳市人民政府关于富阳市农村村民建房管理的若干意见.docx</u>"));
		attan.setText("富阳市人民政府关于富阳市农村村民建房管理的若干意见.docx");
		attan.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
		attan.getPaint().setAntiAlias(true);// 抗锯齿
		attan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean sdCardExist = Environment.getExternalStorageState()
						.equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
				if (sdCardExist) {
					File dir = Environment.getExternalStorageDirectory();
					String path = dir.getAbsolutePath();
					File file = new File(path
							+ "/富阳市人民政府关于富阳市农村村民建房管理的若干意见.docx");
					try {
						if (!file.exists()) {
							file.createNewFile();
							inputstreamtofile(
									getResources().openRawResource(R.raw.test),
									file);
						}
						FileUtil.openFile(NewTodoDocumentDetailActivity.this,
								file);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});

	}
	
	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			// Intent intent = new Intent(this, MainActivity.class);
			// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// startActivity(intent);
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	public void inputstreamtofile(InputStream ins, File file) throws Exception {
		OutputStream os = new FileOutputStream(file);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();
		ins.close();
	}
}
