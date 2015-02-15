package com.example.mca;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultClientConnection;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class response_parser{
	
	public response_parser()
	{
	}
	
	public LunchMenu[] getmenu(String response)
	{
		String[] menuArray = response.split("<br>");
		Log.v("response", "1");
		LunchMenu[] result = new LunchMenu[menuArray.length-1];
		Log.v("response", "2");
		for (int i=0;i<menuArray.length;i++)
		{	
			Log.v("response", menuArray[i]);
			String[] temp = menuArray[i].split(",");
			Log.v("response", "in for"+temp[0]);
			if(temp.length==2)
			{
				Log.v("kkk", temp[0]);
				Log.v("kkk", ""+temp[0].length());
				result[i]=new LunchMenu(temp[0].substring(2, temp[0].length()),  Integer.parseInt(temp[1]));
				Log.v("kkk", result[i].menu);
				Log.v("kkk", ""+result[i].menu.length());
			}
				//result[i].setCount(Integer.parseInt(temp[1]));
			//result[i].setMenu(temp[0]);
		}
		Log.v("response", "out for");
		return result;
	}

	public String[] getSmenu(String response)
	{
		String[] menuArray = response.split("<br>");
		Log.v("response", "1");
		String[] result = new String[menuArray.length-1];
		Log.v("response", "2");
		for (int i=0;i<menuArray.length-1;i++)
		{	
			Log.v("response", menuArray[i]);
			String[] temp = menuArray[i].split(",");
			Log.v("response", "in for"+temp[0]);
			result[i]=temp[0];
			//result[i].setCount(Integer.parseInt(temp[1]));
			//result[i].setMenu(temp[0]);
		}
		Log.v("response", "out for");
		return result;
	}

	public String[] getTitle(String response)
	{
		String[] Titles = response.split("<br>");
		String[] result = new String[Titles.length-1];
		for (int i=0;i<Titles.length;i++)
		{	
			String[] temp = Titles[i].split(",");
			if(temp.length==2)
			{
				result[i]=temp[0].substring(2, temp[0].length())+"."+temp[1];
			}
		}
		return result;
	}
	
}
