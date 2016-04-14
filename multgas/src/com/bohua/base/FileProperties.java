package com.bohua.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.qinxiaoyu.multgas.R;

import android.content.Context;

public class FileProperties {

	/**配置文件的路径**/
	public String cfgfilepath = "/sdcard/MultGasDetecter/parameter/gasparam.dat";
	
	public String readProperties(String key)
	{
		String prop_string;
		
		Properties prop = new Properties();
		prop = loadConfig(cfgfilepath);
		prop_string = (String) prop.get(key);
		return prop_string;
	}
	
	public Properties loadConfig(String file) {  
		Properties properties = new Properties();  
		try {  
			FileInputStream s = new FileInputStream(file);  
			properties.load(s);  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
		return properties;  
	}  

    /** 
     *写入properties信息 
     * @param parameterName 配置文件属性名 
     * @param parameterValue 需要写入的配置文件的信息 
     */  
    
    public void writeProperties(String parameterName, String parameterValue){
        
    	File file = new File(cfgfilepath);
    	if(!file.exists())
		{ 
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	Properties prop = new Properties();  
        try {  
            InputStream fis = new FileInputStream(cfgfilepath);  
            //从输入流中读取属性列表（键和元素对）  
            prop.load(fis);  
            //调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。  
            //强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。  
            OutputStream fos = new FileOutputStream(cfgfilepath);  
            prop.put(parameterName, parameterValue);  
            //以适合使用 load 方法加载到 Properties 表中的格式，  
            //将此 Properties 表中的属性列表（键和元素对）写入输出流  
            prop.store(fos, " Update '" + parameterName + "' value");  
           
        }  
        catch (IOException e) {  
//	        	Print.print("ConfigInfoError","Visit "+filePath+" for updating "+parameterName+" value error");  
            System.err.println("**********************");  
            System.err.println("\r\n write BalanceStat configuration failed,please check "+cfgfilepath+" is writer . thank you \n\n");  
            System.err.println("**********************");  
//	            throw e;  
        }  
    }  
    
    public void checkPropertiesKey(Context context)
    {
    	int i;
    	File file = new File(cfgfilepath);
    	if(!file.exists())
		{ 
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	
    	String gas_param_list[] = context.getResources().getStringArray(R.array.gas_param);
    	for(i = 0;i<gas_param_list.length;i++ )
    	{
    		if(readProperties(gas_param_list[i])==null)/*检测每一个config是否有值*/
    		{
    			if(i == 17)
    				writeProperties(gas_param_list[i],EnumDataSaveFreq.DATA_SAVE_FREQ_5S+"");/*如果没有值则填充0*/
    			else
    				writeProperties(gas_param_list[i],"0");/*如果没有值则填充0*/
    		}
    	}
    	 
    }
	 
	
	
	
}
