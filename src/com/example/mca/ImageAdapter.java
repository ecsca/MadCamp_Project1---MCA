package com.example.mca;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter{
	private Context mContext;
	public static ArrayList<Drawable> mThumbIds;
	public static ArrayList<String> urlList = new ArrayList<String>();
	public final String getImageurl(int position)
	{
		return urlList.get(position);
	}
	public ImageAdapter(Context c){
		mContext = c;
		try {
			urlList = new JsonParser().execute().get();
			Log.v("sample", urlList.get(0));
			Log.v("sample", urlList.get(0));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	/*
		try {
			Log.v("def", "before worker execute");
			mThumbIds = new worker().execute("http://bit.sparcs.org/~argon/photo_list.txt").get();
			Log.v("def", "after worker execute");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		}
	public int getCount(){
		Log.v("getCount", ""+urlList.size());
		return urlList.size();
	}
	public Object getItem(int position){
		return null;
	}
	public long getItemId(int position){
		return 0;
	}
	public View getView(int position, View convertView, ViewGroup parent){
		ImageView imageView;
		if(convertView == null){
			imageView=new ImageView(mContext);
			DisplayMetrics metrics  = new DisplayMetrics();
			WindowManager windowManager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
			windowManager.getDefaultDisplay().getMetrics(metrics);
			int screenWidth = metrics.widthPixels;
			int screenHeight = metrics.heightPixels;
			   imageView.setLayoutParams(new GridView.LayoutParams(screenWidth/3, screenHeight/5));            
			   imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);            
			     } else {            
			   imageView = (ImageView) convertView;        
			  } 
		new worker(imageView, urlList.get(position), mContext).execute();
		//imageView.setImageDrawable(mThumbIds.get(position));
		return imageView;
		}
}