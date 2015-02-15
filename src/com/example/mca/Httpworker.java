package com.example.mca;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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

public class Httpworker extends AsyncTask<String,Void, String> {
	
	public Httpworker()
	{
		
	}

	@Override
	protected String doInBackground(String... params) {
		String request = params[0];
		URL purl;
		HttpClient httpclient = new DefaultHttpClient();
		InputStream content = null;
		String result = null;
		try {
			Log.v("response", request);
			HttpResponse response = httpclient.execute(new HttpGet(request));
			Log.v("response", request);
			content = response.getEntity().getContent();
			StringBuffer sb = new StringBuffer();
			byte[] b= new byte[4096];
			for(int n; (n=content.read(b))!=-1;)
			{
				sb.append(new String(b, 0, n));
			}
			result = sb.toString();
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}
	
	
	
	
	

	
}
