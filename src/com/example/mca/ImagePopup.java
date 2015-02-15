package com.example.mca;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;

public class ImagePopup extends Activity implements OnClickListener{
	private Context mContext = null;
	private Drawable img;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_popup);
		mContext = this;
		
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		String imgPath = extras.getString("filename");
		

		/*
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int screenWidth = metrics.widthPixels;
		int screenHeight = metrics.heightPixels; 
		
		URL purl;
		try {
			purl = new URL(imgPath);
			Bitmap bm = getRemoteImage(purl);
			Bitmap resized = Bitmap.createScaledBitmap(bm,  screenWidth, screenHeight*3/4, true);
			Drawable temp = new BitmapDrawable(bm);
			img = temp;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		ImageView iv = (ImageView)findViewById(R.id.imageView);
		iv.setImageDrawable(img);
		new worker(iv, imgPath, mContext).execute();
		
		Button btn = (Button)findViewById(R.id.btn_back);
		btn.setOnClickListener(this);
	}
	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn_back:
			android.os.Process.killProcess(android.os.Process.myPid());
			break;
		}
	}
	
	/*
	private static Bitmap getRemoteImage(URL url){
		HttpURLConnection conn;
		try{
			conn=(HttpURLConnection)url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			Bitmap bm = BitmapFactory.decodeStream(is);
			is.close();
			return bm;
		}catch (IOException e){
			e.printStackTrace();
			return null;
		}
	}
	*/
	
}