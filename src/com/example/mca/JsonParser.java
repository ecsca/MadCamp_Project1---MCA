package com.example.mca;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class JsonParser extends AsyncTask<Void, Void, ArrayList<String>>{

		private ArrayList<String> urlList;
		protected void onPreExecute(){
			super.onPreExecute();
		}
		@Override
		protected ArrayList<String> doInBackground(Void... strs) {
			Log.v("abc","before getJsonText()");
			urlList = getJsonText();
			Log.v("abc",urlList.get(0));
			return urlList;
		}
		
		@Override
		protected void onPostExecute(ArrayList<String> result) {
			Log.v("abc", "In PostExecute");
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
		/*
		protected void onPostExecute(){

			Log.v("abc", "In PostExecute");
			super.onPostExecute(urlList);
			Log.v("abc", "after PostExecute");
		}
		*/

	public ArrayList<String> getJsonText(){
		StringBuffer sb = new StringBuffer();
		ArrayList<String> urlList = new ArrayList<String>();
		Log.v("abc", "in get Json Text");
		try {
			String line = getStringFromUrl("http://bit.sparcs.org/~argon/photo_list.txt");
			Log.v("abc", line);
			try {
					Log.v("abc", "before JSONArray");
				JSONArray JArray = new JSONArray(line);
					Log.v("abc", "before for loop");
				for (int i =0;i<JArray.length();i++)
				{
					JSONObject insideObject = JArray.getJSONObject(i);
					Log.v("abc", "in for loop");
					urlList.add(insideObject.getString("url"));
				}
				
					Log.v("abc", "after for loop");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return urlList;
	}
	public String getStringFromUrl(String url) throws UnsupportedEncodingException{
		BufferedReader br = new BufferedReader(new InputStreamReader(getInputStreamFromUrl(url),"UTF-8"));
		
		StringBuffer sb = new StringBuffer();
		
		String line = null;
		
		try {
			while((line = br.readLine()) != null)
					{
				sb.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public static InputStream getInputStreamFromUrl(String url){
		InputStream contentStream = null;
		
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(new HttpGet(url));
			contentStream = response.getEntity().getContent();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return contentStream;
	}
	public class JsonLoadingTask extends AsyncTask<Void, Void, ArrayList<String>>{

		private ArrayList<String> urlList;
		@Override
		protected ArrayList<String> doInBackground(Void... strs) {
			urlList = getJsonText();
			return urlList;
		}
		protected void onPostExecute(){
			super.onPostExecute(urlList);
		}
	}
	}