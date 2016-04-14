package com.bohua.algorithm;

import com.bohua.base.Database;
import com.bohua.base.Date;
import com.bohua.base.Debug;
import com.bohua.base.GlobalData;

public class DatabaseAlgorithm {


	static String tag = "DatabaseAlgorithm";
	private static void debug(String string){if(Debug.DatabaseAlgorithmDebug() == 1) Debug.debugi(tag,string);}
	
	public static int co_timer = 0;
	public static int ch4_timer = 0;
	public static int no2_timer = 0;
	public static int o2_timer = 0;
	
	
	public static void saveDatabaseInOneFiles(String gas_type ,int gas_original_value ,double gas_valtage_value,double gas_actual_value,int templeture)
	{
		if("o2".equals(gas_type))
		{
			o2_timer++;
			debug("o2_timer = "+o2_timer);
			o2_timer = differenceByTimer(gas_type ,gas_original_value ,gas_valtage_value,gas_actual_value,templeture,o2_timer);
		}
		else if("no2".equals(gas_type))
		{
			no2_timer++;
			debug("no2_timer = "+no2_timer);
			no2_timer = differenceByTimer(gas_type ,gas_original_value ,gas_valtage_value,gas_actual_value,templeture,no2_timer);
		}
		else if("co".equals(gas_type))
		{
			co_timer++;
			debug("co_timer = "+co_timer);
			co_timer = differenceByTimer(gas_type ,gas_original_value ,gas_valtage_value,gas_actual_value,templeture,co_timer);
		}
		else if("ch4".equals(gas_type))
		{
			ch4_timer++;
			debug("ch4_timer = "+ch4_timer);
			ch4_timer = differenceByTimer(gas_type ,gas_original_value ,gas_valtage_value,gas_actual_value,templeture,ch4_timer);
		}
	}
	protected static int differenceByTimer(String gas_type ,int gas_original_value ,double gas_valtage_value,double gas_actual_value,int templeture,int time)
	{
		int ret;
		ret = time;
		Date date = new Date();
		String date_now = date.getSystemDateToString();
		switch(GlobalData.getDataSaveFreqence())
		{
			case DATA_SAVE_FREQ_5S:
				if(ret >= 5)
				{
					debug("5秒保存"+gas_type);
					Database database = new Database();
					database.insertData("gas_data", "gas_data", gas_type, gas_original_value, gas_valtage_value, gas_actual_value, templeture,date_now);
					ret = 0;
				} 
				break;
			case DATA_SAVE_FREQ_10S:
				if(ret >= 10)
				{
					debug("10秒保存"+gas_type);
					Database database = new Database();
					database.insertData("gas_data", "gas_data", gas_type, gas_original_value, gas_valtage_value, gas_actual_value, templeture,date_now);
					ret = 0;
				}
				break;
			case DATA_SAVE_FREQ_20S:
				if(ret >= 20)
				{
					debug("20秒保存"+gas_type);
					Database database = new Database();
					database.insertData("gas_data", "gas_data", gas_type, gas_original_value, gas_valtage_value, gas_actual_value, templeture,date_now);
					ret = 0;
				}
				break;
			case DATA_SAVE_FREQ_1M:
				if(ret >= 60)
				{
					debug("1分钟保存"+gas_type);
					Database database = new Database();
					database.insertData("gas_data", "gas_data", gas_type, gas_original_value, gas_valtage_value, gas_actual_value, templeture,date_now);
					ret = 0;
				}
				break;
			case DATA_SAVE_FREQ_3M:
				if(ret >= 180)
				{
					debug("3分钟保存"+gas_type);
					Database database = new Database();
					database.insertData("gas_data", "gas_data", gas_type, gas_original_value, gas_valtage_value, gas_actual_value, templeture,date_now);
					ret = 0;
				}
				break;
			case DATA_SAVE_FREQ_5M:
				if(ret >= 300)
				{
					debug("5分钟保存"+gas_type);
					Database database = new Database();
					database.insertData("gas_data", "gas_data", gas_type, gas_original_value, gas_valtage_value, gas_actual_value, templeture,date_now);
					ret = 0;
				}
				break;
		}
		return ret;
	}
	
	
}
