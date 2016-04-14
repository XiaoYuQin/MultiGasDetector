package com.bohua.algorithm;

import com.bohua.base.DS18B20;

public class OxySenTempCompensation {
	
	DS18B20  ds18b20 = new DS18B20();
	
	/*计算温度补偿系数*/
	public float calculationCoefficient()
	{
		int temperature = 0;
		temperature = DS18B20.getTemperature();
		if((temperature >= -20)&&(temperature <= -10))/*温度在-20~-10之间*/
		{
			return (float)((((92.5-90)/10)*(-(temperature+10))+90)/100);
		}
		else if((temperature > -10)&&(temperature <= 0))
		{
			return (float)((((95-92.5)/10)*(-(temperature+0))+92.5)/100);
		}
		else if((temperature > 0)&&(temperature <= 10))
		{
			return (float)((((97.5-95)/10)*(temperature+0)+95)/100);
		}
		else if((temperature > 10)&&(temperature <= 20))
		{
			return (float)((((100-97.5)/10)*(temperature-10)+97.5)/100);
		}
		else if((temperature > 20)&&(temperature <= 30))/*温度在20~30之间*/
		{
			return (float)((((102.2-100)/10)*(temperature-20)+100)/100);
		}
		else if((temperature > 30)&&(temperature <= 40))
		{
			return (float)((((104-102.2)/10)*(temperature-30)+102)/100);
		}
		else if((temperature > 40)&&(temperature <= 50))
		{
			return (float)(((0.1)*(temperature-40)+104)/100);
		}
		else
		{
			return -1;
		}
	}	
}
