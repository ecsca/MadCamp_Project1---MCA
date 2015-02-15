package com.example.mca;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TabHost mTab = getTabHost();

		LayoutInflater inflater = LayoutInflater.from(this);
		inflater.inflate(R.layout.activity_main, mTab.getTabContentView(), true);

		mTab.addTab(mTab.newTabSpec("TabA").setIndicator("")
				.setContent(new Intent(this, TabA.class)));

		mTab.addTab(mTab.newTabSpec("tag").setIndicator("")
				.setContent(new Intent(this, TaskB.class)));

		mTab.addTab(mTab.newTabSpec("tag").setIndicator("")
				.setContent(new Intent(this, TabC.class)));
		setTabColor(mTab);

	}

	public void setTabColor(TabHost tabhost) {
		tabhost.getTabWidget().getChildAt(0)
				.setBackgroundResource(R.drawable.tab_bg); // unselected
		tabhost.getTabWidget().getChildAt(1)
		.setBackgroundResource(R.drawable.tab_bgb); // unselected
		tabhost.getTabWidget().getChildAt(2)
		.setBackgroundResource(R.drawable.tab_bgc); // unselected
	}
}
