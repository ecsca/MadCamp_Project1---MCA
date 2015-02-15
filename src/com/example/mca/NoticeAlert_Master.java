package com.example.mca;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.example.mca.Vote.LunchAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

public class NoticeAlert_Master extends Activity {
	String[] titles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice_alert_master);
		try {
			response_parser Parser = new response_parser();
			String response = new Httpworker().execute("http://bit.sparcs.org:23232/showAllNotice").get();
			this.titles = Parser.getTitle(response);
			Message message = mAfterDown.obtainMessage();
			message.obj = "";
			mAfterDown.sendMessage(message);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Button button = (Button)findViewById(R.id.button_notice_assign);
		button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final LinearLayout linear = (LinearLayout) View.inflate(NoticeAlert_Master.this,  R.layout.addnotice, null);
				
				new AlertDialog.Builder(NoticeAlert_Master.this)
				.setTitle("새 공지사항")
				.setView(linear)
				.setPositiveButton("등록",  new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						EditText Title = (EditText)linear.findViewById(R.id.newTitle);
						EditText content = (EditText)linear.findViewById(R.id.newNotice);
						String STitle= URLEncoder.encode(Title.getText().toString());
						String SContent= URLEncoder.encode(content.getText().toString());
						if(STitle=="" || SContent=="")
						{
						}
						else
						{
							String req = "http://bit.sparcs.org:23232/addNotice/"+STitle+"/"+SContent;
							req = req.replace("+", "%20");
							String result;
							try {
								result = new Httpworker().execute(req).get();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ExecutionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								response_parser Parser = new response_parser();
								String response = new Httpworker().execute("http://bit.sparcs.org:23232/showAllNotice").get();
								titles = Parser.getTitle(response);
								Message message = mAfterDown.obtainMessage();
								message.obj = "";
								mAfterDown.sendMessage(message);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ExecutionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}
				}).show();
			}
			
		});
	}

	Handler mAfterDown = new Handler() {
		public void handleMessage(Message msg) {
			ArrayList<String> Items = new ArrayList<String>();
			for (int i = 0; i < titles.length; i++) {
				Items.add(titles[i]);
			}
			ArrayAdapter<String> Adapter = new ArrayAdapter<String>(
					NoticeAlert_Master.this,
					android.R.layout.simple_list_item_1, Items);

			ListView list = (ListView) findViewById(R.id.list_notice);
			list.setAdapter(Adapter);
			Log.v("qqqq", "bac");
			list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Log.v("qqqq", "basc");
					Log.v("qqqq", titles[position].split("\\.")[0]);
					String num = (titles[position].split("\\.")[0]);
					//Log.v("qqqq", ""+num);
					String msg=null;
					try {
						//Log.v("qqqq", ""+num);
						msg= new Httpworker().execute("http://bit.sparcs.org:23232/showNotice/"+num).get();
						//Log.v("qqqq", msg);
						//msg = URLDecoder.decode(msg);
						//Log.v("qqqq", msg);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					/* get content text in "String msg" here! */

					new AlertDialog.Builder(NoticeAlert_Master.this)
							.setTitle(titles[position])
							.setMessage(msg)
							.show();
				}
			});

		}
	};
}
