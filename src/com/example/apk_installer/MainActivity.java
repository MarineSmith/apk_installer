package com.example.apk_installer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends ActionBarActivity {
	
	private final static String TAG = MainActivity.class.getName();
	private ListView lv1;
	private Button bt1,bt2;
	private ArrayList<Apk> mApk;
	private boolean latch = true;
	private Apk_adapter mApk_adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		item_initial();
		
	}
	
	protected void onResume(){
		super.onResume();
		//requireROOT();
		search_file();
		latch = false;
	}
	
	private void item_initial(){
		bt1 = (Button)this.findViewById(R.id.bt1);
		bt2 = (Button)this.findViewById(R.id.bt2);
		lv1 = (ListView)this.findViewById(R.id.lv1);
		bt1.setOnClickListener(mOnClickListener);
		lv1.setOnItemClickListener(mOnItemClickListener);
		lv1.setClickable(true);
		mApk = new ArrayList<Apk>();
		mApk_adapter = new Apk_adapter(mApk,MainActivity.this,lv1);
	}
	
	private View.OnClickListener mOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int id = v.getId();
			switch(id){
			case R.id.bt1:
				finish();
				break;
			case R.id.bt2:
				search_file();
				break;
			}
		}
		
	};
	
	private OnItemClickListener mOnItemClickListener = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Log.e(TAG,"click");
			mApk_adapter.ask4require(position);
		}
		
	};
	
	private void search_file(){
		File mFile[] = Environment.getExternalStorageDirectory().listFiles();
		mApk.clear();
		for(File file:mFile){
			String path = file.getAbsolutePath();
			int dot = path.lastIndexOf(".");
			String ext = path.substring(dot+1);
			if(ext.equals("apk")){
				mApk.add(new Apk(file.getName(),file.getAbsolutePath()));
				//Log.e(TAG,""+file.getName()+file.getAbsolutePath());
			}
		}
		if(latch==true){
			lv1.setAdapter(mApk_adapter);
		}else{
			mApk_adapter.refresh(mApk);
			lv1.setAdapter(mApk_adapter);
		}
	}
	
	@Override
	protected void onStop(){
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch(id){
		case R.id.item1:
			finish();
			break;
		case R.id.item2:
			search_file();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
