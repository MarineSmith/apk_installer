package com.example.apk_installer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Apk_adapter extends BaseAdapter {
	
	private ArrayList<Apk> mApk;
	private Context mContext;
	private ListView lv1;
	private final static String TAG = Apk_adapter.class.getName();
	private Apk apk;
	private boolean requirelatch = false;
	
	public Apk_adapter(ArrayList<Apk> mApk,Context mContext,ListView lv1){
		this.mApk=mApk;
		this.mContext=mContext;
		this.lv1 = lv1;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mApk.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void refresh(ArrayList<Apk> mApk){
		this.mApk=mApk;
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		if(convertView==null){
			LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
			convertView = mLayoutInflater.inflate(R.layout.lv1, null);
			holder = new Holder();
			holder.lv_tv1 = (TextView)convertView.findViewById(R.id.lv_tv1);
			holder.lv_tv2 = (TextView)convertView.findViewById(R.id.lv_tv2);
			holder.lv1_bt1 = (Button)convertView.findViewById(R.id.lv1_bt1);
			holder.lv1_bt1.setOnClickListener(mOnClickListener);
			convertView.setTag(holder);
		}else{
			holder = (Holder)convertView.getTag();
		}
		apk = mApk.get(position);
		holder.lv_tv1.setText(apk.getName());
		holder.lv_tv2.setText(apk.getPath());
		
		return convertView;
	}
	
	private View.OnClickListener mOnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final int position  = lv1.getPositionForView((View) v.getParent());
			ask4require(position);
		}
	};
	
	private void ask4require(final int position){
		Builder mBuilder = new AlertDialog.Builder(mContext);
		mBuilder.setTitle("Warning");
		mBuilder.setMessage("This program may destroy your Android device.Are you sure??");
		
		
		DialogInterface.OnClickListener mOnClickListener = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				int id = which;
				switch(id){
				case AlertDialog.BUTTON_POSITIVE:
					launchShell(position);
					break;
				case AlertDialog.BUTTON_NEGATIVE:
					break;
				}
			}
		};
		
		mBuilder.setPositiveButton("sure", mOnClickListener);
		mBuilder.setNegativeButton("cancel",mOnClickListener);
		mBuilder.create().show();
	}
	
	private void launchShell(int position){
		try {
			Process mProcess = Runtime.getRuntime().exec("su");
			DataOutputStream mDataOutputStream = new DataOutputStream(mProcess.getOutputStream());
			mDataOutputStream.writeBytes("pm install "+mApk.get(position).getPath()+"\n");
			mDataOutputStream.writeBytes("exit\n");
			mDataOutputStream.flush();
			try{
				Toast.makeText(mContext, "Wait a minute until the next information",Toast.LENGTH_SHORT).show();
				mProcess.waitFor();
				int id = mProcess.exitValue();
				switch(id){
				case 0:
					Toast.makeText(mContext, "successed!!", Toast.LENGTH_SHORT).show();
					break;
				case 1:
					Toast.makeText(mContext, "require denied", Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(mContext, ""+id, Toast.LENGTH_SHORT).show();
					break;
				}
			}catch(Exception e){
				Toast.makeText(mContext, "sorry this program is only available on ROOTed device!!", Toast.LENGTH_SHORT).show();
			}
			/*StringBuilder mStringBuilder = new StringBuilder();
			BufferedReader mBufferedReader = new BufferedReader(new InputStreamReader(mProcess.getInputStream()));
			while(mBufferedReader.read()!=-1){
				mStringBuilder.append(mBufferedReader.readLine());
			}*/
			mDataOutputStream.close();
			mProcess.destroy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(mContext, "sorry this program is only available on ROOTed device!!", Toast.LENGTH_SHORT).show();
		}
	
	}
	
	class Holder{
		TextView lv_tv1,lv_tv2;
		Button lv1_bt1;
	}

}
