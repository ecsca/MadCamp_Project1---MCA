package com.example.mca;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TabC extends Activity {

	public String loginID=null;
	public boolean MasterF = false;
	public Context CContext;
	
	Button noticeButton, boardButton, voteButton;

	
	public final void callVote()
	{
		Intent i = new Intent(CContext, Vote.class);
		startActivityForResult(i, 1);
	}
	public final void callNotice()
	{
		if(MasterF)
		{
			Intent i = new Intent(CContext, NoticeAlert_Master.class);
			startActivityForResult(i, 1);
		}
		else
		{
			Intent i = new Intent(CContext, NoticeAlert.class);
			startActivityForResult(i, 1);
		}

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_c);
		this.CContext = this;
		Button voteButton = (Button)findViewById(R.id.Button_vote);
		voteButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(loginID==null)
				{
					Toast toast = Toast.makeText(CContext, "로그인 후 사용해주세요!" ,Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
				else
				{
					callVote();	
				}
				
			}
		});
		Button NoticeButton = (Button)findViewById(R.id.Button_notice);
		NoticeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(loginID==null)
				{
					Toast toast = Toast.makeText(CContext, "로그인 후 사용해주세요!" ,Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
				else
				{
					callNotice();	
				}
				
			}
		});
		
		
		
		Button loginButton = (Button) findViewById(R.id.Button_Login);
		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final LinearLayout linear = (LinearLayout) View.inflate(CContext,  R.layout.login, null);
				if(loginID ==null)
				{
				new AlertDialog.Builder(CContext)
				.setTitle("Login하세요")
				.setView(linear)
				.setPositiveButton("로그인!",  new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						// TODO Auto-generated method stub
						EditText editedID = (EditText)linear.findViewById(R.id.editID);
						EditText editedPWD = (EditText)linear.findViewById(R.id.editPassword);
						String SID = editedID.getText().toString();
						String SPWD = editedPWD.getText().toString();
						String req="";
						String temp = URLEncoder.encode(SID);
						SID = temp.replace("+", "%20");
						temp = URLEncoder.encode(SPWD);
						SPWD = temp.replace("+", "%20");
						if(SID=="" || SPWD=="")
						{
							Toast toast = Toast.makeText(CContext, "아이디나 비밀번호가 잘못됬어요!" ,Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
						else
						{
							req = "http://bit.sparcs.org:23232/login/"+SID+"/"+SPWD+"/";
							Log.v("qqqq", req);
							String result;
							try {
								result = new Httpworker().execute(req).get();
								Log.v("qqqq", result);
								if (result.split(",").length==1)
								{
									Toast toast = Toast.makeText(CContext, "아이디나 비밀번호가 잘못됬어요!" ,Toast.LENGTH_SHORT);
									toast.setGravity(Gravity.CENTER, 0, 0);
									toast.show();
								}
								else
								{
									String resultID = result.split(",")[0];
									String resultF = result.split(",")[1];
									loginID = resultID;
									Button btn = (Button)findViewById(R.id.Button_Login);
									TextView txt_log = (TextView)findViewById(R.id.text_log);
									txt_log.setText(loginID+"님 환영합니다!");
	
									btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_logout));
									Toast toast = Toast.makeText(CContext, resultID+"로 로그인 하셨습니다." ,Toast.LENGTH_SHORT);
									toast.setGravity(Gravity.CENTER, 0, 0);
									toast.show();
									Log.v("qqqq", resultF);
									Log.v("qqqq", ""+resultF.length());
									Log.v("qqqq", resultF.substring(0, 4));
									if(resultF.equals("True"))
									{
										Log.v("qqqq", "set");
										MasterF=true;
									}
									else
										MasterF=false;
								}
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ExecutionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}).show();}
				else
				{
					new AlertDialog.Builder(CContext)
					.setTitle("로그아웃 하실거에요?")
					.setPositiveButton("로그아웃",  new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							loginID = null;
							MasterF = false;
							Toast toast = Toast.makeText(CContext, "로그아웃했어요. 안녕히가세요~" ,Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							Button btn = (Button)findViewById(R.id.Button_Login);
							btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_login));
						}
						
						
					})
					.setNegativeButton("로그아웃 안 할래요", new DialogInterface.OnClickListener() {
						
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					}).show();
				}
				
			}
		});
}
}