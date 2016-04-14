package com.bohua.graphics;

import java.util.List;

import com.qinxiaoyu.multgas.R;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WifiListAdapter extends BaseAdapter{

	LayoutInflater inflater;
	public List<ScanResult> list;
	
	public WifiListAdapter (Context context ,List<ScanResult> list){
		// TODO 自动生成的构造函数存根
		this.inflater=LayoutInflater.from(context);
		this.list=list;
	}
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		if(this.list != null)
		{
		    return list.size();
		}
		else{
			return 1;
		}
		
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		
		View view = null;
		view = inflater.inflate(R.layout.item_wifi_list, null);

		TextView textView = (TextView) view.findViewById(R.id.textView);

		TextView signalStrenth = (TextView) view.findViewById(R.id.signal_strenth);
		if (list != null) 
		{
			ScanResult scanResult = list.get(position);
			textView.setTextColor(android.graphics.Color.BLACK);
			textView.setTextSize(17);
			textView.setText(scanResult.SSID);
			signalStrenth.setText(String.valueOf(Math.abs(scanResult.level)));
			ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
			LayoutParams para;
			para = imageView.getLayoutParams();
			para.height = 60;
			para.width = 80;
			imageView.setLayoutParams(para);

			Log.i("WifiListAdapter","123--"+scanResult.SSID+"   "+scanResult.SSID.length());
			
			if ((Math.abs(scanResult.level) < 40 )&&(Math.abs(scanResult.level) >= 0)) 
			{
				imageView.setImageResource(R.drawable.wifi_above_100);
			} 
			else if ((Math.abs(scanResult.level) < 60 )&&(Math.abs(scanResult.level) >= 40)) 
			{
				imageView.setImageResource(R.drawable.wifi_above_70);
			} 
			else if ((Math.abs(scanResult.level) < 80 )&&(Math.abs(scanResult.level) >= 60)) 
			{
				imageView.setImageResource(R.drawable.wifi_above_50);
			} 
			else 
			{
				imageView.setImageResource(R.drawable.wifi_below_50);
			}
		}
		else{
			textView.setText("当前无网络");
		}
		return view;
		
	}}