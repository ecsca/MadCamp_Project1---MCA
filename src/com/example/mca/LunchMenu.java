package com.example.mca;

public class LunchMenu {
	public String menu;
	public int count;
	
	public LunchMenu(String m, int c){
		this.menu = m;
		this.count = c;
	}
	
	public String getMenu()
	{
		return this.menu;
	}
	public int getCount()
	{
		return this.count;
	}
	public void setMenu(String m)
	{
		this.menu=m;
	}
	public void setCount(int c)
	{
		this.count=c;
	}
}

