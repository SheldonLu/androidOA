package com.mzoneapp.zjjmb.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mzoneapp.zjjmb.R;
import com.mzoneapp.zjjmb.ui.EmailDetailActivity;

public class EmailFragment extends Fragment {

	private ListView mListview;

	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.meetingnotification_fragment,
				null);
		mListview = (ListView) view.findViewById(R.id.listview);

		List<Email> list = new ArrayList<EmailFragment.Email>();
		list.add(new Email("管理员", "关于安全生产监督的会议", "关于浙江省安全生产监督的会议决定，请勿回复此邮件",
				"2012-11-26"));
		EmailAdapter adapter = new EmailAdapter(container.getContext(), R.layout.list_item_email, list);
		mListview.setAdapter(adapter);
		mListview.setCacheColorHint(0);
		mListview.setDividerHeight(0);
		mListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent  = new Intent(container.getContext(), EmailDetailActivity.class);
				startActivity(intent);
			}
		});
		return view;
	}

	public class Email {

		public String recipient;
		public String title;
		public String body;
		public String datetime;

		public Email(String recipient, String title, String body,
				String datetime) {
			super();
			this.recipient = recipient;
			this.title = title;
			this.body = body;
			this.datetime = datetime;
		}

	}

	public class EmailAdapter extends ArrayAdapter<Email> {

		
		public EmailAdapter(Context context, int textViewResourceId, List<Email> emails) {
			super(context, textViewResourceId, emails);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					inflater);

			if (convertView == null) {
				convertView = vi.inflate(
						R.layout.list_item_email, parent, false);
			}

			TextView zj = (TextView) convertView.findViewById(R.id.txt_zj);
			TextView sj = (TextView) convertView.findViewById(R.id.txt_sj);
			TextView body = (TextView) convertView
					.findViewById(R.id.txt_body);

			Email email = getItem(position);
			zj.setText(email.title);
			sj.setText(email.datetime);
			body.setText(email.body);

			return convertView;
		}

	}

}
