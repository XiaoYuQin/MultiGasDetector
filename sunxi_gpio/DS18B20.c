#include <linux/delay.h>
#include <mach/gpio.h> /* for new api */
#include <linux/gpio.h> /* for new api */
#include <asm/io.h>
#include <linux/kthread.h>


#include "DS18B20.h"
#include "private.h"

#define DS18B20_DELAY	 /*1000*//*500*//*250*//*125*/6

extern unsigned char DS18B20_DQ;
extern unsigned char DS18B20_TEMPERATURE_DATA;




static int getGpio(unsigned char gpio)
{
	return __gpio_get_value(gpio);
}
static int setDQ(unsigned char volue)
{
	if(DS18B20_DQ!=0)
	{
		gpio_direction_output(DS18B20_DQ,volue);	
		return 0;
	}
	else
		return -1;
}
static int getDQ(void)
{
	gpio_direction_input(DS18B20_DQ);
	return getGpio(DS18B20_DQ);
}

void DS18B20init(void)
{
	char x=1;
#if 1	
	setDQ(1);
	udelay(DS18B20_DELAY*8);		/*稍作延时*/
	setDQ(0);
	udelay(DS18B20_DELAY*80);		/*延时>480us 540us*/
	setDQ(1); 						/*拉高总线 15-60us*/
	udelay(DS18B20_DELAY*10);
	x=getDQ();						/*读总线状态 为0复位成功，为1则不成功 */
	//MULTGAS_DEBUG(DS18B20_DEBUG_BH "x = %d ",x);
	udelay(DS18B20_DELAY*35);
	setDQ(1);						/*释放总线*/
#endif	
}
static unsigned char DS18B20readOneChar(void)
{

	unsigned char i=0;
	unsigned char dat = 0;
//	udelay(1);
	for (i=8;i>0;i--)
	{
		setDQ(0); // 给脉冲信号
		dat>>=1;
//		udelay(1);
		setDQ(1); // 给脉冲信号
		if(getDQ())
			dat|=0x80;
		udelay(DS18B20_DELAY*5/*5*/);
	}
	 return(dat);
}
static void DS18B20writeOneChar(unsigned char dat)
{
#if 1
	unsigned char i=0;
//	udelay(1);
	for (i=8; i>0; i--)
	{
		setDQ(0);
		setDQ(dat&0x01);
		udelay(DS18B20_DELAY*5/*5*/);
		setDQ(1);
		dat>>=1;
	}
	udelay(DS18B20_DELAY*5/*5*/);
#endif	
}
/* 读取温度值*/
/* 每次读写均要先复位*/
int DS18B20readOneTemperature(void) 
{
#if 1
	unsigned char a=0;
	unsigned char b=0;
	unsigned char t=0;
	
	float tt=0;
	DS18B20init();
	DS18B20writeOneChar(0xCC); // 跳过读序号列号的操作
	DS18B20writeOneChar(0x44); // 启动温度转换
	udelay(DS18B20_DELAY*200);
	DS18B20init();
	DS18B20writeOneChar(0xCC); //跳过读序号列号的操作 
	DS18B20writeOneChar(0xBE); //读取温度寄存器等（共可读9个寄存器） 前两个就是温度
	a=DS18B20readOneChar();
	b=DS18B20readOneChar();
	//MULTGAS_DEBUG(DS18B20_DEBUG_BH " 0x%02X ",a);
	//MULTGAS_DEBUG(DS18B20_DEBUG_BH " 0x%02X ",b);
	b<<=4;
	b+=(a&0xf0)>>4;
	t=b;
	tt=t*0.0625;
	t =tt*10+0.5; //放大10倍输出并四舍五入
	//MULTGAS_DEBUG(DS18B20_DEBUG_BH " t = %d",t);
	return t;
#endif
}




