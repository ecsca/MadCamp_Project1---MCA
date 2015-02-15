package com.example.mca;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Vote extends Activity {

	public LunchMenu[] lMenu;
	public LunchAdapter Adapter;
	public Context Mainc;
	public ListView VoteView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Mainc = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote);
        String result=null;
		try {
			result = new Httpworker().execute("http://bit.sparcs.org:23232/").get();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response_parser Parser = new response_parser();
		this.lMenu = Parser.getmenu(result);
		
		Adapter = new LunchAdapter(this, R.layout.pbar, lMenu);
		//ListView VoteView;
		VoteView=(ListView)findViewById(R.id.Lview);
		VoteView.setAdapter(Adapter);
		VoteView.setOnItemClickListener(new ListViewItemClickListener());
		VoteView.setOnItemLongClickListener(new ListViewItemLongClickListener());
		
		Button button = (Button)findViewById(R.id.add);
		button.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v("btnlog", "click!");
				final LinearLayout linear = (LinearLayout) View.inflate(Mainc,  R.layout.addmenuformat, null);
				
				new AlertDialog.Builder(Mainc)
				.setTitle("뭐 먹고 싶어요?")
				.setView(linear)
				.setPositiveButton("이걸로 할래요~",  new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						EditText newmenu = (EditText)linear.findViewById(R.id.newMenu);
						response_parser Parser = new response_parser();
						String temp = URLEncoder.encode(newmenu.getText().toString());
							String converted = temp.replace("+", "%20");
							String req;
							if(converted=="")
								req="http://bit.sparcs.org:23232/";
							else
								req = "http://bit.sparcs.org:23232/addMenu/"+converted;
							Log.v("utf", converted);
							String result;
							try {
								result = new Httpworker().execute(req).get();
								lMenu= Parser.getmenu(result);
								Adapter = new LunchAdapter(Mainc, R.layout.pbar, lMenu);
								VoteView.setAdapter(Adapter);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ExecutionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

					}
				})
				.setNegativeButton("취소",  new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						response_parser Parser = new response_parser();
						String req="http://bit.sparcs.org:23232/";
						String result;
						try {
							result = new Httpworker().execute(req).get();
							lMenu= Parser.getmenu(result);
							Adapter = new LunchAdapter(Mainc, R.layout.pbar, lMenu);
							VoteView.setAdapter(Adapter);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					
				}).show();
				/*
				Intent intent = new Intent(MainActivity.this, AddNewMenu.class);
				startActivity(intent);*/
			}
			
		});
		Button refbtn = (Button)findViewById(R.id.refresh);
		refbtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				response_parser Parser = new response_parser();
				// TODO Auto-generated method stub
				String req = "http://bit.sparcs.org:23232/";
				String result;
				try {
					result = new Httpworker().execute(req).get();
					lMenu= Parser.getmenu(result);
					Adapter = new LunchAdapter(Mainc, R.layout.pbar, lMenu);
					VoteView.setAdapter(Adapter);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
		});
    }
    
    
    
   private class ListViewItemClickListener implements AdapterView.OnItemClickListener
   {

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		response_parser Parser = new response_parser();
		try {
			Log.v("response", ""+position);
			Log.v("response", lMenu[position].menu);
			String temp = URLEncoder.encode(lMenu[position].menu);
			String converted = temp.replace("+", "%20");
			//String temp = java.net.URLEncoder.encode(new String(lMenu[position].menu.getBytes("UTF-8")));
			String req = "http://bit.sparcs.org:23232/addCount/"+converted;
			String result = new Httpworker().execute(req).get();
			lMenu= Parser.getmenu(result);
			Adapter = new LunchAdapter(Mainc, R.layout.pbar, lMenu);
			VoteView.setAdapter(Adapter);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
   }
	
	class ListViewItemLongClickListener implements AdapterView.OnItemLongClickListener
	{

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) 
		{
			new AlertDialog.Builder(Mainc)
			.setTitle("삭제할까요?")
			.setPositiveButton("삭제할래요", new DialogInterface.OnClickListener() 
			{
				
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					response_parser Parser = new response_parser();
					String temp = URLEncoder.encode(lMenu[position].menu);
					String converted = temp.replace("+", "%20");
					String req = "http://bit.sparcs.org:23232/deleteMenu/"+converted;
					String result;
					try {
						result = new Httpworker().execute(req).get();
						lMenu= Parser.getmenu(result);
						Adapter = new LunchAdapter(Mainc, R.layout.pbar, lMenu);
						VoteView.setAdapter(Adapter);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	
				}
			})
				.setNegativeButton("지우지 말아요",  new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
					
				}).show();
			return false;
		}
		
	}
    
    public class LunchAdapter extends ArrayAdapter<LunchMenu>{
    	private LunchMenu[] mmenu;
    	private int fullVoter=0;
    	
    	public LunchAdapter(Context context, int textViewResourceId, LunchMenu[] menu) 
    	{
    		super(context, textViewResourceId, menu);
    		this.mmenu = menu;

    		// TODO Auto-generated constructor stub
    	}

    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) 
    	{
    		fullVoter=0;
    		for(int i=0;i<mmenu.length;i++)
    		{
    			fullVoter+=mmenu[i].count;
    		}
    		
    		// TODO Auto-generated method stub
    		View v = convertView;
    		if (v==null)
    		{
    			LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    			v = vi.inflate(R.layout.pbar,  null);
    		}
    		LunchMenu l = mmenu[position];
    		if(l!=null){
    			TextView ll = (TextView) v.findViewById(R.id.PBarIn);
    			
    			if(ll!=null)
    			{
    				ll.setText(l.menu+"("+l.count+")");
    			}
    			Log.v("response", "fullVoter: "+fullVoter);
    			int progress=0;
    			if(fullVoter!=0)
    				progress = (l.count*100)/fullVoter;
    			Log.v("response", l.menu);
    			Log.v("response", "Progress: "+progress);
    			Log.v("response", "l.count: "+l.count);
    			ProgressBar progressBar= (ProgressBar) v.findViewById(R.id.progressBar1);
    			progressBar.setProgress(progress);
    		}
    		return v;
    	}
    }
}
