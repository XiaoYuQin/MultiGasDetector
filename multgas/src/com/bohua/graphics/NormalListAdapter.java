package com.bohua.graphics;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NormalListAdapter extends BaseAdapter{

	@SuppressWarnings("unused")
	private Context context;  
    private List<? extends Map<String, ?>> mData;  
    private int mResource;  
    private String[] mFrom;  
    private int[] textViewId;  
    private LayoutInflater mLayoutInflater; 
	private int textSize = 15;
	private int colors = android.graphics.Color.BLACK;
	
	@SuppressWarnings("static-access")
	public NormalListAdapter(Context context,List<? extends Map<String, ?>> data,int resource, String[] from, int[] textViewId)
	{
        this.context = context;  
        this.mData = data;  
        this.mResource = resource;  
        this.mFrom = from;  
        this.textViewId = textViewId;  
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);  
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.mData.size();  
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;  
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = mLayoutInflater.inflate(mResource, parent, false);    
        
        // 设置contentView的内容和样式，这里重点是设置contentView中文字的大小  
        for(int i=0; i<textViewId.length; i++)
        {  
            TextView textView = (TextView) convertView.findViewById(textViewId[i]);  
            textView.setText(mData.get(position).get(mFrom[i]).toString());  
            textView.setTextColor(colors);
            switch(i)
            {
	            case 0:
	            	textView.setTextSize(textSize);  
	            	break;
	            case 1:
	            	textView.setTextSize(textSize);  
	            	break;
	            default:
	            	textView.setTextSize(textSize);  
	            	break; 
            }
        }  
        return convertView;  
	}

	public void setTextSize(int textSize)
	{
		this.textSize = textSize;
	}
	 
	public void setTextColor(int color)
	{
		this.colors = android.graphics.Color.BLACK;
	}
}
