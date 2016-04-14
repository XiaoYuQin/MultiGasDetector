#include <linux/kernel.h>
#include <linux/init.h>
#include <linux/interrupt.h>
#include <linux/platform_device.h>
#include <linux/device.h>
#include <linux/list.h>
#include <linux/slab.h>
#include <linux/module.h> /* for THIS_MODULE */
#include <mach/sys_config.h>
#include <mach/gpio.h> /* for new api */
#include <linux/gpio.h> /* for new api */
#include <asm/io.h>
#include <asm/uaccess.h>
#include <linux/compat.h>
#include <linux/mutex.h>
#include <linux/kthread.h>
#include <linux/delay.h>
#include <linux/timer.h>

#include "multGasDetectDriver.h"

#include <linux/ioctl.h>
#include <linux/fs.h>
#include <asm/uaccess.h>

#include <linux/delay.h>
#include <linux/ctype.h>

#include "DS18B20.h"
#include "AD7794.h"
#include "AD7790.h"
#include "AD8400.h"
#include "private.h"

#define hello_major 	153
#define GPIODEV_MAJOR	153
#define GPIO_IRQ  28





typedef enum
{
	cmd_get_temperature = 1

}cmd_list;

#define REG_RD(fmt...)	__raw_readl(fmt)

#define ON_LINE  1
#define OFF_LINE 0
#define SECONDS_COUNT  10
#define GET_AD7790_TIMEOUT		msecs_to_jiffies(1000)
#define ds18b20_timeout_base	usecs_to_jiffies(1)

static struct platform_device mult_gas_dev;
static struct mult_gas_platdata gas_datesw;
static struct timer_list timer;
 
struct mult_gas {
	struct mult_gas_classdev		cdev;
	struct mult_gas_platdata		*pdata;
};
static struct class *mult_gas_class;
static struct task_struct *multgas_kthread;
static struct task_struct *ds18b20_kthread;

unsigned char AD7790_SPI2_CS2=0;
unsigned char AD7790_SPI2_CS1=0;
unsigned char AD7790_SPI2_CLK=0;
unsigned char AD7790_SPI2_MOSI=0;
unsigned char AD7790_SPI2_MISO=0;
unsigned char DS18B20_DQ=0;
unsigned char POWER_EN = 0;

extern unsigned char AD7794_DIN;
extern unsigned char AD7794_CS;
extern unsigned char AD7794_DOUTRDY;
extern unsigned char AD7794_SCLK;



unsigned short ADC_CH1_DATA;
unsigned char  DS18B20_TEMPERATURE_DATA = 0;
unsigned int   pd_data;
unsigned int   pd_data_old;
unsigned int   ad7790device_status=OFF_LINE;
unsigned int   up_seconds;
unsigned int   get_ad7790_data_flag=0;
//#define AD7790_SPI2_CS1	0x26	/*PB14*/
//#define AD7790_SPI2_CLK	0x27	/*PB15*/
//#define AD7790_SPI2_MOSI	0x28	/*PB16*/
//#define AD7790_SPI2_MISO	0x29	/*PB17*/


extern unsigned long int AD7794Registers[9]; 	


extern unsigned long int AIN1Data[AD7794_DATA_SAVE];
extern unsigned long int AIN2Data[AD7794_DATA_SAVE];
extern unsigned long int AIN3Data[AD7794_DATA_SAVE];
extern unsigned long int AIN4Data[AD7794_DATA_SAVE];
extern unsigned long int AIN5Data[AD7794_DATA_SAVE];
extern unsigned long int AIN6Data[AD7794_DATA_SAVE];

void __aeabi_fadd(void){} 
void __aeabi_dadd(void){} 
void __aeabi_dsub(void){} 
void __aeabi_i2d(void){} 
void __aeabi_d2uiz(void){} 
void __aeabi_f2d(void){}
void __aeabi_fmul(void){}
void __aeabi_ui2f(void){}
void __aeabi_dmul(void){}
void __aeabi_d2iz(void){}
void __aeabi_d2f(void){}
void __aeabi_ui2d(void){}
void __aeabi_i2f(void){}


static void setGpio(unsigned char gpio, int value)
{
	__gpio_set_value(gpio,value);
}
/*static int getGpio(unsigned char gpio)
{
	return __gpio_get_value(gpio);
}*/
/*static int setGpioInput(unsigned gpio)
{
	return gpio_direction_input(gpio);
}
static int setGpioOutput(unsigned gpio)
{
	return gpio_direction_output(gpio,0);
}*/






static int getCmdByString(const char *buf,size_t size)
{
	int app_cmd = 0;
	if(size==3)
	{
		app_cmd=(buf[0]-'0')*100+(buf[1]-'0')*10+(buf[2]-'0')*1;
		if(app_cmd>=256)
		{
			app_cmd=256;
		}
	}
	else if(size == 2)
	{
		app_cmd=(buf[0]-'0')*10+(buf[1]-'0')*1;
	}
	else if(size == 1)
	{
		app_cmd=buf[0]-'0';
	}
	else if(size>3)
	{
		app_cmd =  -1;
	}
	return app_cmd;
}
static ssize_t ch4_get(struct device *dev,struct device_attribute *attr, char *buf)
{
	unsigned short ret = AIN6Data[0];
	AD7794_DEBUG(AD7794_DEBUG_BH "ch4_get = 0x%02x",ret);
	return sprintf(buf, "%d\n",ret);
} 
static ssize_t ch4_set(struct device *dev,struct device_attribute *attr, const char *buf, size_t size)
{
	AD7794_DEBUG(AD7794_DEBUG_BH "ch4_set");
	return 0;
}
static ssize_t o2_get(struct device *dev,struct device_attribute *attr, char *buf)
{
	unsigned short ret = AIN1Data[0];
	AD7794_DEBUG(AD7794_DEBUG_BH "o2_get = 0x%02x",ret);
	return sprintf(buf, "%d\n",ret);
} 
static ssize_t o2_set(struct device *dev,struct device_attribute *attr, const char *buf, size_t size)
{
	AD7794_DEBUG(AD7794_DEBUG_BH "o2_set");
	return 0;
}
static ssize_t no2_get(struct device *dev,struct device_attribute *attr, char *buf)
{
	unsigned short ret = AIN2Data[0];
	AD7794_DEBUG(AD7794_DEBUG_BH "no2_get = 0x%02x",ret);
	return sprintf(buf, "%d\n",ret);
} 
static ssize_t no2_set(struct device *dev,struct device_attribute *attr, const char *buf, size_t size)
{
	AD7794_DEBUG(AD7794_DEBUG_BH "no2_set");
	return 0;
}
static ssize_t co_get(struct device *dev,struct device_attribute *attr, char *buf)
{
	unsigned short ret = AIN3Data[0];
	AD7794_DEBUG(AD7794_DEBUG_BH "co_get = 0x%02x",ret);
	return sprintf(buf, "%d\n",ret);
} 
static ssize_t co_set(struct device *dev,struct device_attribute *attr, const char *buf, size_t size)
{
	AD7794_DEBUG(AD7794_DEBUG_BH "co_set");
	return 0;
}


static ssize_t data_show(struct device *dev,struct device_attribute *attr, char *buf)
{
	unsigned char data_xxx[10] = {1,2,3,4,5,6,7,8,9,0};

	//MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::data_show \n");
	//ad7790device_status=ON_LINE;
	//return sprintf(buf, "%d\n",ADC_CH1_DATA);
	//return *data_xxx;
	return sprintf(buf, "%s\n",data_xxx);
} 
static ssize_t data_store(struct device *dev,struct device_attribute *attr, const char *buf, size_t size)
{
	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::data_store %s , %d\n",buf,size);
	return 0;
}
static ssize_t cmd_show(struct device *dev,struct device_attribute *attr, char *buf)
{	
	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::cmd_show \n");
	
	return sprintf(buf, "%d\n",DS18B20_TEMPERATURE_DATA);
}
static ssize_t cmd_store(struct device *dev,struct device_attribute *attr, const char *buf, size_t size)
{
	int cmd;
	cmd = getCmdByString(buf,size);
	switch(cmd)
	{
		case -1:
			MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::cmd_store error\n");
		  break;
		case cmd_get_temperature:
			MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::cmd_store cmd_get_temperature\n");
		  break;
	}

	
	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::cmd_store \n");
	return 0;
}
static ssize_t dp_show(struct device *dev,struct device_attribute *attr, char *buf)
{
	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::pd_show \n");
	return 0;
}
static ssize_t dp_store(struct device *dev,struct device_attribute *attr, const char *buf, size_t size)
{
	//MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::pd_store %s  %d\n",buf,size);
	if(size==3)
	{
		pd_data=(buf[0]-'0')*100+(buf[1]-'0')*10+(buf[2]-'0')*1;
		if(pd_data>=256)
		{
			pd_data=256;
		}
	}
	else if(size == 2)
	{
		pd_data=(buf[0]-'0')*10+(buf[1]-'0')*1;
	}
	else if(size == 1)
	{
		pd_data=buf[0]-'0';
	}
	return 0;
}



static void multGasTimerFunction(unsigned long data)
{
	unsigned int	*data_ptr = (unsigned int*)data;
	*data_ptr += 0;
	//MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::up for %u seconds\n",up_seconds);
	mod_timer( &timer, jiffies + GET_AD7790_TIMEOUT );	
	get_ad7790_data_flag=1;
}

static int ds18b20ThreadHandle(void *arg)
{
	DS18B20_DEBUG(DS18B20_DEBUG_BH"mult-gas::ds18b20ThreadHandle\n");
	while(1)
	{
		if( kthread_should_stop())  return -1; 
		msleep(1000);
		DS18B20_TEMPERATURE_DATA = DS18B20readOneTemperature();
		DS18B20_DEBUG(DS18B20_DEBUG_BH " DS18B20_TEMPERATURE_DATA = %d",DS18B20_TEMPERATURE_DATA);
	}
	return 0;
}


static int multgas_getData_threadHandle(void *arg) 
{
	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::multgas_getData_threadHandle\n");
#ifdef AD7790_ENABLE
	AD7790Read(1);
	while(1)
	{
		if( kthread_should_stop())  return -1; 
		//MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::thread test!!\n");
		msleep(1);
		
		if(get_ad7790_data_flag==1)
		{
			get_ad7790_data_flag=0;
			AD7790Read(5);
		}
		if(pd_data_old!=pd_data)
		{
			pd_data_old=pd_data;
			//MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::pd_data = %d \n",pd_data);
			AD8400WriteReg(pd_data);
		}
	}
#endif	
#ifdef AD7794_ENABLE
	while(1)
	{

	
		if( kthread_should_stop())  return -1; 
		//AD7794_DEBUG(AD7794_DEBUG_BH "mult-gas::thread test!!\n");
		msleep(10);
		AD7794GetAllAdcData(SFR_CFG_CHANNEL_AIN1,1);
		AD7794GetAllAdcData(SFR_CFG_CHANNEL_AIN2,1);
		AD7794GetAllAdcData(SFR_CFG_CHANNEL_AIN3,1);
		AD7794GetAllAdcData(SFR_CFG_CHANNEL_AIN6,1);
		/*AD7794_DEBUG(AD7794_DEBUG_BH "temp1 = 0x%02x , %d",temp1,temp1);
		AD7794_DEBUG(AD7794_DEBUG_BH "temp1/32768 = %f",(float)(temp1/32768));
		AD7794_DEBUG(AD7794_DEBUG_BH "temp1/32768-1 = 0x%02x",((temp1/32768)-1));
		AD7794_DEBUG(AD7794_DEBUG_BH "(((temp1/32768)-1)*2.5) = 0x%02x",(((temp1/32768)-1)*2.5));
		AD7794_DEBUG(AD7794_DEBUG_BH "(((temp1/32768)-1)*2.5) = %.2f",(((temp1/32768)-1)*2.5));*/

		/*AD7794_DEBUG(AD7794_DEBUG_BH "temp1 = 0x%02x , %d",temp1,temp1);
		AD7794_DEBUG(AD7794_DEBUG_BH "temp1/32768 = %d",(long)((temp1-32768)*2.5));
		AD7794_DEBUG(AD7794_DEBUG_BH "temp1/32768-1 = %f",(float)(1840/32768));

		

		ret = (float)(((temp1/32768)-1)*2.5);
		AD7794_DEBUG(AD7794_DEBUG_BH "123 = %.6f");*/

		/*volteage_val=(temp1-32768)*2.5;
		MULTGAS_DEBUG(AD7790_DEBUG_BH "%02X-->>",volteage_val);
		ret=(float)volteage_val/32768;
		MULTGAS_DEBUG(AD7790_DEBUG_BH "%.6f",ret);*/

		
		/*AD7794_DEBUG(AD7794_DEBUG_BH "AIN1 = %.6lf \n",ret);
		volteage_val=(AIN2Data[0]-32768)*2.5;
		ret=(float)volteage_val/32768;
		AD7794_DEBUG(AD7794_DEBUG_BH "AIN2 = %.6lf \n",ret);
		volteage_val=(AIN3Data[0]-32768)*2.5;
		ret=(float)volteage_val/32768;
		AD7794_DEBUG(AD7794_DEBUG_BH "AIN3 = %.6lf \n",ret);
		volteage_val=(AIN6Data[0]-32768)*2.5;
		ret=(float)volteage_val/32768;
		AD7794_DEBUG(AD7794_DEBUG_BH "AIN6 = %.6lf \n",ret);*/
		



		
		/*
		测试管脚用
		*/
		#if 0
		AD7794_DOUTRDY = AD7790_SPI2_MISO;
		AD7794_SCLK = AD7790_SPI2_CS1;
		AD7794_DIN = AD7790_SPI2_MOSI;
		AD7794_CS = AD7790_SPI2_CLK;
		
		AD7794_DEBUG(AD7794_DEBUG_BH "AD7794_DIN = %d \n",getGpio(AD7794_DIN));
		AD7794_DEBUG(AD7794_DEBUG_BH "AD7794_CS = %d \n",getGpio(AD7794_CS));
		AD7794_DEBUG(AD7794_DEBUG_BH "AD7794_DOUTRDY = %d \n",getGpio(AD7794_DOUTRDY));
		AD7794_DEBUG(AD7794_DEBUG_BH "AD7794_SCLK = %d \n",getGpio(AD7794_SCLK));
		setGpio(AD7794_DOUTRDY ,1);
		msleep(1000);
		setGpio(AD7794_DOUTRDY ,0);
		#endif
		
				

	}
	
#endif
	return 0;
}
irqreturn_t mult_gas_interrupt(int irq,void *dev_id)
{
	unsigned int PIC, PIS,tmp;
	int i = 0;
	PIC = REG_RD(GPIO_TEST_BASE + 0x210 ) ;
	PIS = REG_RD(GPIO_TEST_BASE + 0x214 ) ;
	tmp	= PIS;

	while(tmp){
		if(tmp & 0x1){
		/*if (tmp & 0x1) is true, the i represent NO.i EINT interrupt take place.
		you can through the value of i to decide to do what*/
		MULTGAS_DEBUG(MULTGAS_DEBUG_BH "this is NO.%d gpio INT \n",i);
		}
		tmp >>= 1;
		i++;
	}

	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "0 PIC is %x \n PIS is %x \n",PIC,PIS);
	__raw_writel(PIS, GPIO_TEST_BASE + 0x214);
	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "1 PIC is %x \n PIS is %x \n",PIC,PIS);

	/*this is a interface to connect interrupt top half and bottom half,if want to use bottom half,you can open fanctions sunxi_gpio_do_tasklet and tasklet_schedule*/

	/*
	tasklet_schedule(&sunxi_tasklet);
	*/
	return IRQ_HANDLED;
}

static int mult_gas_cfg_set(struct mult_gas_classdev *mult_gas_cdev,int  cfg_sel)
{
	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::mult_gas_cfg_set \n");
	return 0;
}
static int mult_gas_data_get(struct mult_gas_classdev *mult_gas_cdev,int  pull)
{
	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::mult_gas_data_get \n");
	return 0;
}
static void mult_gas_release (struct device *dev)
{
	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::mult_gas_release \n");
}
static int mult_gas_resume(struct platform_device *dev)
{
	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::mult_gas_resume \n");
	return 0;
}
static int mult_gas_suspend(struct platform_device *dev, pm_message_t state)
{
	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::mult_gas_suspend \n");
	return 0;
}
static int __devexit mult_gas_remove(struct platform_device *dev)
{
	struct mult_gas *multgas=platform_get_drvdata(dev);
	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::mult_gas_remove \n");
	//device_unregister(multgas->cdev.dev);
	
	device_destroy(mult_gas_class, MKDEV(GPIODEV_MAJOR,0));

	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::gpio_sw_classdev_unregister ok !\n");
	kfree(multgas);
	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::kfree ok !\n");
	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::mult_gas_remove \n");
	return 0;
}


static struct device_attribute mult_gas_class_attrs[] = {
	__ATTR(get_data,	0666, data_show, 	data_store),
	__ATTR(set_cmd,		0666, cmd_show, 	cmd_store),
	__ATTR(dp_cmd,		0666, dp_show, 		dp_store),
	/*获取各通道ADC 数字值*/
	__ATTR(get_ch4, 	0666, ch4_get, 		ch4_set),
	__ATTR(get_o2, 		0666, o2_get, 		o2_set),
	__ATTR(get_no2, 	0666, no2_get, 		no2_set),
	__ATTR(get_co, 		0666, co_get, 		co_set),

	__ATTR_NULL,
};
static int __devinit mult_gas_probe(struct platform_device *dev)
{
	struct mult_gas *multgas;
	struct mult_gas_platdata *pdata = dev->dev.platform_data;
	unsigned int irq_ctl;
	int ret=0;
	/*char io_area[16];*/
    script_item_value_type_e type;
    script_item_u item;
    //int gpio_key_count = 0;
	int i=1;

	multgas = kzalloc(sizeof(struct mult_gas), GFP_KERNEL);
	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "kzalloc ok !\n");

	if (multgas == NULL) 
	{
		dev_err(&dev->dev, "No memory for device\n");
		return -ENOMEM;
	}

	platform_set_drvdata(dev, multgas);
	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "platform_set_drvdata ok !\n");
	//multgas->pdata = pdata;

	/*\BB\F1取GPIO  \BF\DA*/

	for(i=1;i<8;i++)
	{
	    sprintf(pdata->name, "gpio_pin_%d", i+1);
		
		MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas:: %s \n",pdata->name);
	    type = script_get_item("gpio_para", pdata->name, &item);
	    if (SCIRPT_ITEM_VALUE_TYPE_PIO != type) 
		{
			/*GPIO \BB\F1取失\B0\DC*/
	        MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::script_get_item return type err\n");
	        return -EFAULT;
	    }
		MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::pio_hdle = %x \n",item.gpio.gpio);
		/*if(i==1)		AD7790_SPI2_CS1=item.gpio.gpio;
		else if(i==2)	AD7790_SPI2_CLK=item.gpio.gpio;
		else if(i==3)	AD7790_SPI2_MOSI=item.gpio.gpio;
		else if(i==4)	AD7790_SPI2_MISO=item.gpio.gpio;*/
		if(i == 1)		AD7790_SPI2_CS2  = item.gpio.gpio;	/*I2S_BCLK*/
		else if(i== 2)	AD7790_SPI2_MISO = item.gpio.gpio;		/*I2S_DAO*/
		else if(i== 3)	AD7790_SPI2_MOSI = item.gpio.gpio;		/*I2S_LRCK*/
		else if(i== 4)	AD7790_SPI2_CLK  = item.gpio.gpio;	/*I2S_DAI*/
		else if(i== 5)	AD7790_SPI2_CS1  = item.gpio.gpio;		/*I2S_MCLK*/
		else if(i== 6)	DS18B20_DQ = item.gpio.gpio;
		else if(i== 7)  POWER_EN = item.gpio.gpio;

		/*if (0 != gpio_request_one(item.gpio.gpio, GPIOF_OUT_INIT_HIGH, NULL))
		{
			MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::gpio_request gpio:%d failed!\n",item.gpio.gpio);
	        	return -EFAULT;
	   	}*/
		/* add by cjcheng start {----------------------------------- */
		/* support Internal 3G 2013-04-24 */
		if (0 != sw_gpio_setall_range(&item.gpio, 1)) 
		{
			MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::gpio setall_range err!\n");
		    return -EFAULT;
		}

		if(item.gpio.mul_sel == 6)
		{
			if(item.gpio.port== 'H' - 'A' + 1)
			{
				if((item.gpio.port_num >= 0) && (item.gpio.port_num <= 21))
				{
					irq_ctl	=	REG_RD(GPIO_TEST_BASE + 0x210);
					__raw_writel((1 << item.gpio.port_num) | irq_ctl, GPIO_TEST_BASE + 0x210);
				}
				else
				{
					MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::this pin don`t have EINT FUNCTION!\n");
					kfree(multgas);
					return 1;
				}
			}
			else if(item.gpio.port== 'I' - 'A' + 1)
			{
				if((item.gpio.port_num >= 10) && (item.gpio.port_num <= 19))
				{
					irq_ctl	=	REG_RD(GPIO_TEST_BASE + 0x210);
					__raw_writel((1 << (item.gpio.port_num + 12)) | irq_ctl, GPIO_TEST_BASE + 0x210);
				}
				else
				{
					MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::this pin don`t have EINT FUNCTION!\n");
					kfree(multgas);
					return 1;
				}
			}
			else
			{
				MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::this area don`t have EINT FUNCTION\n");
				kfree(multgas);
				return 1;
			}
		}
	}

	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::AD7790_SPI2_CS1 = %x \n",AD7790_SPI2_CS1);
	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::AD7790_SPI2_CLK = %x \n",AD7790_SPI2_CLK);
	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::AD7790_SPI2_MOSI = %x \n",AD7790_SPI2_MOSI);
	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::AD7790_SPI2_MISO = %x \n",AD7790_SPI2_MISO);
	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::AD7790_SPI2_CS2 = %x \n",AD7790_SPI2_CS2);
	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::DS18B20_DQ = %x \n",DS18B20_DQ);
	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::POWER_EN = %x \n",POWER_EN);

	setGpio(POWER_EN,1);/**上电拉高采集板电源使能脚*/

	multgas->cdev.mult_gas_cfg_set=mult_gas_cfg_set;
	multgas->cdev.mult_gas_data_get=mult_gas_data_get;
	multgas->cdev.name="mult_gas";


	//ret = device_create(mult_gas_class, &dev->dev, 0, &multgas->cdev,"%s", &multgas->cdev.name);
	mult_gas_class->dev_attrs 	= mult_gas_class_attrs;
	device_create(mult_gas_class, NULL, MKDEV(GPIODEV_MAJOR,0),&multgas->cdev, "mult_gas");
	if (ret < 0) 
	{
		dev_err(&dev->dev, "gpio_sw_classdev_register failed\n");
		kfree(multgas);
		return ret;
	}
	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "gpio_sw_classdev_register ok !\n");

	/*打开一个定时器*/
	init_timer(&timer);
	timer.expires = jiffies + GET_AD7790_TIMEOUT;
	timer.data = (unsigned int)&up_seconds;
	timer.function = multGasTimerFunction;
	add_timer(&timer);

/*	init_timer(&ds18b20_timer);
	ds18b20_timer.expires = jiffies + ds18b20_timeout_base;
	timer.function = multGasTimerFunction;*/


	multgas_kthread = kthread_create(multgas_getData_threadHandle, NULL, "mult_gas");
    if(IS_ERR(multgas_kthread))
	{  
		MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::creat multgas kthread fail !!!!\n");
		multgas_kthread = NULL;  
    }
	else 
	{
		MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::creat multgas kthread and run  !!!!\n");
		wake_up_process(multgas_kthread);
	}

	ds18b20_kthread = kthread_create(ds18b20ThreadHandle, NULL, "ds18b20");
    if(IS_ERR(ds18b20_kthread))
	{  
		MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::creat ds18b20 kthread fail !!!!\n");
		ds18b20_kthread = NULL;  
    }
	else 
	{
		MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::creat ds18b20 kthread and run  !!!!\n");
		wake_up_process(ds18b20_kthread);
	}
	
	return 0;
}

static struct platform_driver mult_gas_driver = {
	.probe		= mult_gas_probe,
	.remove		= mult_gas_remove,
	.suspend	= mult_gas_suspend,
	.resume		= mult_gas_resume,
	.driver		= {
		.name		= "mult_gas_driver",
		.owner		= THIS_MODULE,
	},
};


static int __init mult_gas_detect_init(void)
{
	/*int i, ret;*/
    int gpio_key_count = 0;
	/*int ret1;*/
    script_item_u gpio_used, *list = NULL;
    script_item_value_type_e type;

	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::mult_gas_detect_init\r\n");

	/*\BC\EC\B2\E9GPIO \CA欠\F1\B6\A8\D2\E5*/
    type = script_get_item("gpio_para", "gpio_used", &gpio_used);
    if (SCIRPT_ITEM_VALUE_TYPE_INT != type) 
	{
        MULTGAS_DEBUG(MULTGAS_DEBUG_BH "[gpio_para] gpio_used err!\n");
        return -1;
    }
    if (1 == gpio_used.val) 
	{
        gpio_key_count = script_get_pio_list("gpio_para", &list);
        if (0 == gpio_key_count) 
		{
            MULTGAS_DEBUG(MULTGAS_DEBUG_BH "[gpio_para] get gpio list failed\n");
            return -1;
        }
    }


	
    gas_datesw.flags = 0;
	gas_datesw.name="mult_gas_dev";

    mult_gas_dev.name = "mult_gas_driver";
    mult_gas_dev.id   = 0;
    mult_gas_dev.dev.platform_data= &gas_datesw;
    mult_gas_dev.dev.release = mult_gas_release;
    platform_device_register(&mult_gas_dev);

	mult_gas_class = class_create(THIS_MODULE, "mult_gas");
	/*注\B2\E1platform \C7\FD\B6\AF*/
    platform_driver_register(&mult_gas_driver);
	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::mult_gas_detect_init - over\r\n");

    return 0;
}

static void __exit mult_gas_detect_exit(void)
{
	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::mult_gas_detect_exit\r\n");

	
	platform_driver_unregister(&mult_gas_driver);
	class_destroy(mult_gas_class);
    platform_device_unregister(&mult_gas_dev);

	kthread_stop(multgas_kthread);
	multgas_kthread=NULL;
	kthread_stop(ds18b20_kthread);
	ds18b20_kthread=NULL;
	del_timer_sync(&timer);

    MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::platform_device_unregister finish !  \n");
}
module_init(mult_gas_detect_init);
module_exit(mult_gas_detect_exit);

MODULE_AUTHOR("qinxiaoyu <qinxiaoyu@163.com>");
MODULE_DESCRIPTION("MultGasDetect driver");
MODULE_LICENSE("GPL");
MODULE_ALIAS("platform:mult_gas_detect");
