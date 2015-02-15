package com.example.mca;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
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
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;

public class TaskB extends Activity {
	private Context mContext;
	private ImageAdapter ia;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.taskb);
		mContext= this;
		
		GridView gv = (GridView)findViewById(R.id.grid);//TODO: ImgGridView? Í∑∏Î¶¨?ìú ?òï?ãù?úºÎ°? Î≥¥Ïù¥Í≤? ?ïò?äîÍ≤? ?ïÑ?öî?ï®
		ia = new ImageAdapter(this);
		gv.setAdapter(ia);
		gv.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView parent, View v, int position, long id){
				callImageViewer(position);
			}
		});
	}
	public final void callImageViewer(int selectedIndex)
	{
		Intent i = new Intent(mContext, ImagePopup.class);
		String imgPath = ia.getImageurl(selectedIndex);
		i.putExtra("filename",  imgPath);
		startActivityForResult(i, 1);
	}

}