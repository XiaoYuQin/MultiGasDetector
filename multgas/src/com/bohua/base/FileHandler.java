package com.bohua.base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import com.qinxiaoyu.multgas.R;

import android.content.Context;
import android.os.Environment;

public class FileHandler {
	
	static String tag = "FileHandler";
	private static void debug(String string){if(Debug.FileHandlerDebug() == 1) Debug.debugi(tag,string);}
	
	
	
	/**
	 * 检查卡状态 
	 * 插入SD卡并且具有访问权则返回0
	 * 未插入SD卡或者访问权限不够则返回-1
	 **/
	public int checkSDcardStatus()
	{
		try {
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			{
				debug("SD卡插入并且具有访问权限");
				return 0;
			}
		}
		catch(Exception e){
			debug("SD卡未插入或者访问权限不够");
			e.printStackTrace();
			return -1;
		}
		return 0;
	}
	
	/**
	 * 获取SD卡路径
	 * 返回SD卡的路径
	 **/
	public String getSdcardPath()
	{
		File sd = Environment.getExternalStorageDirectory();
		String file_path= sd.getPath();
		return file_path;
	}
	
	/**
	 * 在SD卡中创建一个文件夹，如果该文件已经存在则返回-1
	 * path 表示在/sdcard/...... 
	 * name 表示文件名字
	 **/
	public int creatFile(String name,String path){
		File sd = Environment.getExternalStorageDirectory();
		String file_path= sd.getPath()+path+"/"+name;
		File file = new File(file_path);
		if(!file.exists()){
			file.mkdir();
			debug("在SD卡中创建文件成功");
			debug("文件路径为"+file_path);
			return 0;
		}
		else
		{
			debug("在SD卡中已有文件夹"+file_path);
			return -1;
		}
	}
	
	/**
	 * 创建app所需要的所有文件夹
	 **/
	public int creatAllAppFiles(Context context)
	{
		int flag;

		String fileList[] = context.getResources().getStringArray(R.array.folder_list);
		debug("文件个数为"+fileList.length);
		if(checkSDcardStatus()==0)
		{
			for(int i=0;i<fileList.length;i++)
			{
				flag=creatFile(fileList[i],"");
				if(flag==0)
				{
					/**创建文件成功*/
					debug("创建文件"+fileList[i]+"成功");
				}
				else if(flag == -1)
				{
					//debug("创建文件"+fileList[i]+"失败！！");
					//return -1;
				}
				else
				{
					debug("创建文件"+fileList[i]+"失败！！");
				}
			}
			return 0;
		}
		else
		{
			return -1;
		}
	}
	/**
	 * 创建app所需要的所有文件
	 **/
	public int creatAppFiles()
	{
		File sd = Environment.getExternalStorageDirectory();
		String file_path= sd.getPath();

		String fileList[]={
				"/MultGasDetecter/config.cfg",
				"/MultGasDetecter/config1.cfg",
				"/MultGasDetecter/ratio.cfg"
//				"/MultGasDetecter/strength.cfg"
			   };
		if(checkSDcardStatus()==0)
		{ 
			try
			{
				for(int i=0;i<fileList.length;i++)
				{
					File file = new File(file_path+fileList[i]);
					debug("创建文件"+file_path+fileList[i]);
					if(!file.exists())
					{
						if(file.createNewFile())
						{
							debug("创建文件"+fileList[i]+"成功！！");
							return 0;
						}
						else
						{
							debug("创建文件"+fileList[i]+"失败！！");
							return -1;
						}
					}
					else
					{
						debug("已存在文件"+file_path+fileList[i]);
					}
				}
			}
			catch(IOException  e)
			{
				e.printStackTrace();  
				return -1;
			}
		}
		return 0;
	}
	/**
	 * 写入数据到文件中
	 * **/
	public int writeToFile(String path, String string, boolean type)
	{
		/**获取SD卡中文件路径*/
		String filePath=getSdcardPath()+path;
		debug(filePath);
		File file = new File(filePath);
    	if(!file.exists())
		{ 
			try 
			{
				file.createNewFile();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		FileWriter fw = null;
		BufferedWriter bw = null;
		try
		{
			fw = new FileWriter(filePath,type);	// 创建FileWriter对象，用来写入字符流  
			bw = new BufferedWriter(fw); // 将缓冲对文件的输出  
			bw.write(string + "\n"); // 写入文件  
			bw.newLine();  
            bw.flush(); // 刷新该流的缓冲  
            bw.close();  
            fw.close();  
            debug("写文件成功");
			
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			try
			{  
				bw.close();  
	            fw.close();  
	        } 
			catch (IOException e1) 
	        {  
                // TODO Auto-generated catch block  
	        }  
		}
		return 0;
	}
	/**
	 * 从文件中读出数据  
	 * **/
	public String readFromFile(String path)
	{
		String ret= "";
		/**获取SD卡中文件路径*/
		String filePath=getSdcardPath()+path;
		
		File file = new File(filePath);
    	if(!file.exists())
		{ 
			try 
			{
				file.createNewFile();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	
		FileReader fr = null;
		BufferedReader br = null;
		try 
		{
			fr = new FileReader(filePath);
			br = new BufferedReader(fr);
			String s = null;
			try 
			{

				while((s = br.readLine()) != null)
				{
					s = s+"\n";
					ret += s;
				}
				
				
		        br.close();  
		        fr.close();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			debug("读文件"+ret);
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public String getFileNameByPath(String filePath)
	{
		String fileName = filePath.substring(filePath.lastIndexOf("/")+1);
		return fileName;
	}
	public String getFileSize(File file)
	{
		   long fileSize = file.length();
           if(fileSize > 1024*1024) 
           {
		       float size = fileSize /(1024f*1024f);
		       return new DecimalFormat("#.00").format(size) + "MB";
           } 
           else if(fileSize >= 1024) 
           {
              float size = fileSize/1024;
              return new DecimalFormat("#.00").format(size) + "KB";
           } 
           else 
           {
              return fileSize + "B";
           }
	}
	
	public String getFileSuffix(File file) 
	{
		String type = "*/*";
		String fileName = file.getName();
		int dotIndex = fileName.indexOf('.');
		debug("indexOf "+dotIndex);
		if((dotIndex >-1) && (dotIndex < (fileName.length() - 1))) 
		{    
            return fileName.substring(dotIndex + 1);    
        }    
		return type;
	}
	
	
	
}
