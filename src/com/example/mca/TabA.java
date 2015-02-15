package com.example.mca;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.provider.Contacts.Intents;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class TabA extends Activity {
	ProgressDialog mProgress;
	ListView listView;
	JSONArray ja;
	ImageView img;
	Bitmap bm;
	JSONObject order;
	Bundle bundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();

		ImageLoader.getInstance().init(config);
		mProgress = ProgressDialog.show(TabA.this, "Wait", "Downloading...");
		DownThread thread = new DownThread(
				"http://bit.sparcs.org/~argon/temp.html");
		thread.start();
	}

	class DownThread extends Thread {
		String mAddr;

		DownThread(String addr) {
			mAddr = addr;
		}

		public void run() {
			String result = DownloadHtml(mAddr);
			Message message = mAfterDown.obtainMessage();
			message.obj = result;
			mAfterDown.sendMessage(message);
		}

		String DownloadHtml(String addr) {
			StringBuilder html = new StringBuilder();
			try {
				URL url = new URL(addr);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				if (conn != null) {
					conn.setConnectTimeout(1000);
					conn.setUseCaches(false);
					if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
						BufferedReader br = new BufferedReader(
								new InputStreamReader(conn.getInputStream(),
										"EUC-KR"));
						for (;;) {
							String line = br.readLine();
							if (line == null)
								break;
							html.append(line + '\n');
						}
						br.close();
					}
					conn.disconnect();
				}
			} catch (NetworkOnMainThreadException e) {
				return "Error : 메인 스레드 네트워크 작업 에러 ";
			} catch (Exception e) {
				return "Error : " + e.getMessage();
			}
			return html.toString();
		}
	}

	Handler mAfterDown = new Handler() {
		public void handleMessage(Message msg) {
			mProgress.dismiss();

			try {
				String Json = (String) msg.obj;
				ja = new JSONArray(Json);
				// 데이터 원본 준비
				List<MyItem> listContents = new ArrayList<MyItem>(ja.length());
				for (int i = 0; i < ja.length(); i++) {
					JSONObject order = ja.getJSONObject(i);
					MyItem mi;
					mi = new MyItem(R.drawable.ic_launcher,
							order.getString("name"));
					listContents.add(mi);
				}

				MyListAdapter MyAdapter = new MyListAdapter(TabA.this,
						R.layout.icontext, listContents);

				listView = (ListView) findViewById(R.id.listView);
				listView.setAdapter(MyAdapter);

				listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						try {
							order = ja.getJSONObject(position);
							ImageLoader imageLoader = ImageLoader.getInstance();
							String imageUri = "http://bit.sparcs.org/~argon/Mad_pictures/pic"
									+ (position + 1) + ".PNG";
							imageLoader.loadImage(imageUri,
									new SimpleImageLoadingListener() {

										@Override
										public void onLoadingComplete(
												String imageUri, View view,
												Bitmap loadedImage) {
											super.onLoadingComplete(imageUri,
													view, loadedImage);
											bm = loadedImage;
											Drawable drawable = new BitmapDrawable(bm);
											try {
								                bundle = new Bundle();
								                bundle.putString(Intents.Insert.NAME, order.getString("name"));
								                bundle.putString(Intents.Insert.PHONE, order.getString("phone"));
								                bundle.putString(Intents.Insert.EMAIL, order.getString("email"));
								                bundle.putString(Intents.Insert.COMPANY, order.getString("school"));
												new AlertDialog.Builder(TabA.this)								
												.setTitle(order.getString("name"))
												.setMessage(
														"Birth : "
																+ order.getString("birth")
																+ "\n"
																+ order.getString("school")
																+ "\n"
																+ order.getString("major")
																+ " "
																+ order.getInt("grade")
																+ "학년("
																+ order.getInt("sid")
																+ "학번)\n"
																+ order.getString("phone")
																+ "\n"
																+ order.getString("email"))
												.setIcon(drawable)
												.setNeutralButton("전화번호 등록하기", (new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog, int whichButton) {

                Intent intent = new Intent(Intent.ACTION_INSERT,Uri.parse("content://contacts/people"));
                
                intent.putExtras(bundle);
                startActivity(intent);
        	}
        })).show();
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									});
						} catch (JSONException e) {
						}
					}
				});

			} catch (JSONException e) {
			}
		}
	};

	class MyItem {
		MyItem(int aIcon, String aName) {
			Icon = aIcon;
			Name = aName;
		}

		int Icon;
		String Name;
	}

	class MyListAdapter extends BaseAdapter {
		Context maincon;
		LayoutInflater Inflater;
		List<MyItem> arSrc;
		int layout;

		public MyListAdapter(Context context, int alayout, List<MyItem> aarSrc) {
			maincon = context;
			Inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			arSrc = aarSrc;
			layout = alayout;
		}

		public int getCount() {
			return arSrc.size();
		}

		public String getItem(int position) {
			return arSrc.get(position).Name;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = Inflater.inflate(layout, parent, false);
			}

			// int loader = R.drawable.loader;
			img = (ImageView) convertView.findViewById(R.id.img);
			// img.setImageResource(arSrc.get(position).Icon);
			String imageUri = "http://bit.sparcs.org/~argon/Mad_pictures/pic"
					+ (position + 1) + ".PNG";
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage(imageUri, img);
			TextView txt = (TextView) convertView.findViewById(R.id.text);
			txt.setText(arSrc.get(position).Name);
			return convertView;
		}
	}

}
