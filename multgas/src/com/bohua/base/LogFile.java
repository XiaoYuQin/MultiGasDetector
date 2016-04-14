package com.bohua.base;

public class LogFile extends FileHandler{

	
	private String dealMsg(String className, String function ,String msg)
	{
		String log;
		Date date = new Date();
		String sys_time = date.getSystemDateToString();
		log = 	sys_time+"\n"
				+"类名："+className+":\n"
				+"方法："+function+":\n"
				+"信息："+msg+"\n";
		return log;
	}
	/**
	 * 	LogSave log = new LogSave();
	 * 	log.logWriteToFile(tag, "onCreate", "1233333211");
	 * **/
	public void writeLogToFile(String className ,String function , String msg)
	{
		Date date = new Date();
		writeToFile("/MultGasDetecter/logs/"+date.getSystemDateToStringWithoutHMS()+".txt"
					,dealMsg(className ,function , msg)
					,true);
	}
	/**
	 * LogSave log = new LogSave();
	 * debug(log.logReadFromFile());
	 * **/
	public String logReadFromFile()
	{
		Date date = new Date();
		return readFromFile("/MultGasDetecter/logs/"+date.getSystemDateToStringWithoutHMS()+".txt");
	}
	public String ReadlogByListViewItemFileName(String fileName)
	{
		return readFromFile("/MultGasDetecter/logs/"+fileName);
	}
	
	
}
