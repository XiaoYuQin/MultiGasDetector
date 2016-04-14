/**
 * GeneralSatuation 界面中数据 TextView 的刷新
 * 
 * **/

package com.bohua.graphics;

import static com.bohua.algorithm.GasAlgorithm.getCH4precent;
import static com.bohua.algorithm.GasAlgorithm.getCOppm;
import static com.bohua.algorithm.GasAlgorithm.getNO2ppm;
import static com.bohua.algorithm.GasAlgorithm.getO2precent;
import static com.bohua.base.DS18B20.getTemperature;

import com.bohua.algorithm.AlarmPointAlgorithm;
import com.bohua.algorithm.DatabaseAlgorithm;
import com.bohua.algorithm.GasAlgorithm;
import com.bohua.base.DS18B20;
import com.bohua.base.Debug;
import com.qinxiaoyu.multgas.R;

import android.content.Context;
import android.widget.TextView;

public class GenerSatuTextView {

	static String tag = "GenerSatuTextView";
	private static void debug(String string){if(Debug.CalibrationChooseDebug() == 1) Debug.debugi(tag,string);}
	
	
	/**
	 * 设置 CO TextView 的颜色和数值
	 * **/
	public void setCOTextView(Context appContext,TextView textViewCoValue,TextView textViewCoUnit)
	{
		if(getCOppm(appContext) == -1)
		{
			textViewCoValue.setText("错误");
			textViewCoValue.setTextColor(android.graphics.Color.RED);
			textViewCoUnit.setText("");
		}
		else
		{
			debug("setCOTextView");
			textViewCoUnit.setText(R.string.generalSatuationTextviewUnitPpm);

			double _value = getCOppm(appContext);
			GasAlgorithm gas = new GasAlgorithm(); 
			int gas_original_value = gas.getGasData(appContext);
			double gas_valtage_value = gas.ConvertNumberToVoltage(gas_original_value);
			double gas_actual_value = _value;
			int templeture = DS18B20.getTemperature();
			
			if(AlarmPointAlgorithm.alarmingHandler("CO",_value)== -1)
			{
				textViewCoUnit.setTextColor(android.graphics.Color.RED);
				textViewCoValue.setTextColor(android.graphics.Color.RED);
			}
			else if(AlarmPointAlgorithm.alarmingHandler("CO",_value)== 0)
			{
				textViewCoUnit.setTextColor(android.graphics.Color.BLACK);
				textViewCoValue.setTextColor(android.graphics.Color.BLACK);
			}
			DatabaseAlgorithm.saveDatabaseInOneFiles("co", gas_original_value, gas_valtage_value, gas_actual_value, templeture);
			
			textViewCoValue.setText(String.format("%.2f",_value));
		}
	}
	/**
	 * 设置 CH4 TextView 的颜色和数值
	 * **/
	public void setCH4TextView(Context appContext,TextView textViewCh4Value,TextView textViewCh4Unit)
	{
		if(getCH4precent(appContext) == -1)
		{
			textViewCh4Value.setText("错误");
			textViewCh4Value.setTextColor(android.graphics.Color.RED);
			textViewCh4Unit.setText("");
		}
		else
		{
			debug("setCH4TextView");
			textViewCh4Unit.setText(R.string.generalSatuationTextviewUnitPrecent);
			
			double _value = getCH4precent(appContext);
			GasAlgorithm gas = new GasAlgorithm(); 
			int gas_original_value = gas.getGasData(appContext);
			double gas_valtage_value = gas.ConvertNumberToVoltage(gas_original_value);
			double gas_actual_value = _value;
			int templeture = DS18B20.getTemperature();
			
			if(AlarmPointAlgorithm.alarmingHandler("CH4",_value)== -1)
			{
				textViewCh4Unit.setTextColor(android.graphics.Color.RED);
				textViewCh4Value.setTextColor(android.graphics.Color.RED);
			}
			else if(AlarmPointAlgorithm.alarmingHandler("CH4",_value)== 0)	
			{
				textViewCh4Unit.setTextColor(android.graphics.Color.BLACK);
				textViewCh4Value.setTextColor(android.graphics.Color.BLACK);
			}
			
			DatabaseAlgorithm.saveDatabaseInOneFiles("co", gas_original_value, gas_valtage_value, gas_actual_value, templeture);
			textViewCh4Value.setText(String.format("%.2f",_value));
		}
	}
	/**
	 * 设置 NO2 TextView 的颜色和数值
	 * **/
	public void setNO2TextView(Context appContext,TextView textViewNo2Value,TextView textViewNo2Unit)
	{
		if(getNO2ppm(appContext) == -1)
		{
			textViewNo2Value.setText("错误");
			textViewNo2Value.setTextColor(android.graphics.Color.RED);
			textViewNo2Unit.setText("");
		}
		else
		{
			debug("setNO2TextView");
			textViewNo2Unit.setText(R.string.generalSatuationTextviewUnitPpm);
			
			double _value = getNO2ppm(appContext);
			GasAlgorithm gas = new GasAlgorithm(); 
			int gas_original_value = gas.getGasData(appContext);
			double gas_valtage_value = gas.ConvertNumberToVoltage(gas_original_value);
			double gas_actual_value = _value;
			int templeture = DS18B20.getTemperature();
			
			if(AlarmPointAlgorithm.alarmingHandler("NO2",_value)== -1)
			{
				textViewNo2Unit.setTextColor(android.graphics.Color.RED);
				textViewNo2Value.setTextColor(android.graphics.Color.RED);
			}
			else if(AlarmPointAlgorithm.alarmingHandler("NO2",_value)== 0)
			{
				textViewNo2Unit.setTextColor(android.graphics.Color.BLACK);
				textViewNo2Value.setTextColor(android.graphics.Color.BLACK);
			}
			
			DatabaseAlgorithm.saveDatabaseInOneFiles("co", gas_original_value, gas_valtage_value, gas_actual_value, templeture);
			textViewNo2Value.setText(String.format("%.2f",_value));
		}
		
	}
	
	public void setO2TextView(Context appContext,TextView textViewO2Value,TextView textViewO2Unit)
	{
		if(getO2precent(appContext) == -1)
		{
			textViewO2Value.setText("错误");
			textViewO2Value.setTextColor(android.graphics.Color.RED);
			textViewO2Unit.setText("");
		}
		else
		{
			debug("setO2TextView");
			textViewO2Unit.setText(R.string.generalSatuationTextviewUnitPrecent);

			double _value = getO2precent(appContext);
			GasAlgorithm gas = new GasAlgorithm(); 
			int gas_original_value = gas.getGasData(appContext);
			double gas_valtage_value = gas.ConvertNumberToVoltage(gas_original_value);
			double gas_actual_value = _value;
			int templeture = DS18B20.getTemperature();
			
			if(AlarmPointAlgorithm.alarmingHandler("O2",_value)== -1)
			{
				textViewO2Value.setTextColor(android.graphics.Color.RED);
				textViewO2Value.setTextColor(android.graphics.Color.RED);
			}
			else if(AlarmPointAlgorithm.alarmingHandler("O2",_value)== 0)
			{
				textViewO2Value.setTextColor(android.graphics.Color.BLACK);
				textViewO2Value.setTextColor(android.graphics.Color.BLACK);
			}
			
			DatabaseAlgorithm.saveDatabaseInOneFiles("co", gas_original_value, gas_valtage_value, gas_actual_value, templeture);
			textViewO2Value.setText(String.format("%.2f",_value));
		}
		
	}
	
	public void setTemperatureTextView(TextView textViewTempValue)
	{
		if(getTemperature() == -255)
		{
			textViewTempValue.setTextColor(android.graphics.Color.RED);
			textViewTempValue.setText("错误");
		}
		else
		{
			debug("setTemperatureTextView");
			textViewTempValue.setTextColor(android.graphics.Color.BLACK);
			if(getTemperature() != 255)
			{
				textViewTempValue.setText(""+getTemperature()+"度");
			}
			
			
		}
		
	}
	
	
	
}
