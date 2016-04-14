/*
 * 2011-2012
 * panlong <panlong@reuuimllatech.com>
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation.
 */
#include <linux/kernel.h>
#include <linux/init.h>
#include <linux/interrupt.h>
#include <linux/platform_device.h>
#include <linux/device.h>
#include <linux/slab.h>
#include <linux/module.h> /* for THIS_MODULE */
#include <mach/sys_config.h>
#include <mach/gpio.h> /* for new api */
#include <linux/gpio.h> /* for new api */
#include <asm/io.h>
#include "gpio_sw.h"

#include <linux/ioctl.h>
#include <linux/fs.h>
#include <asm/uaccess.h>

#include <linux/delay.h>



#define mul_sel_1	1
#define pull_2 		2
#define drv_level_3	3
#define data_4		4
#define port_5		5
#define port_num_6	6
#define REG_RD(fmt...)	__raw_readl(fmt)

#define GPIO_DEBUG_BH		KERN_ALERT

#if 1
#define GPIO_SW_DEBUG(fmt...) printk(fmt)
#else
#define GPIO_SW_DEBUG(fmt...) do{} while (0)
#endif
#define support_ad7790x

#ifdef support_ad7790x


#define AD7790_SPI2_CS2		0
#define AD7790_SPI2_CS1		0x26	/*PB14*/
#define AD7790_SPI2_CLK		0x27	/*PB15*/
#define AD7790_SPI2_MOSI	0x28	/*PB16*/
#define AD7790_SPI2_MISO	0x29	/*PB17*/


#define AD7790_SPI_DELAY	1
#endif


void __aeabi_fadd(void){} 
void __aeabi_dadd(void){} 
void __aeabi_dsub(void){} 
void __aeabi_i2d(void){} 
void __aeabi_d2uiz(void){} 
void __aeabi_f2d(void){}
void __aeabi_fmul(void){}
void __aeabi_ui2f(void){}
void __aeabi_dmul(void){}


#define GPIODEV_MAJOR			154	/* assigned */
static struct class *gpiodev_class;


#define GPIO_IRQ  28

int all_irq_enable	= 0;
static int led_flag=0;
static struct platform_device gpio_sw_dev[256];
static struct gpio_sw_platdata pdatesw[256];
/*
void sunxi_gpio_do_tasklet(unsigned long data)
{
	printk("this is irp donw dispuse !\n");
}
DECLARE_TASKLET(sunxi_tasklet,sunxi_gpio_do_tasklet,0);
*/
irqreturn_t sunxi_interrupt(int irq,void *dev_id)
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
		GPIO_SW_DEBUG(GPIO_DEBUG_BH "this is NO.%d gpio INT \n",i);
		}
		tmp >>= 1;
		i++;
	}

	GPIO_SW_DEBUG(GPIO_DEBUG_BH "0 PIC is %x \n PIS is %x \n",PIC,PIS);
	__raw_writel(PIS, GPIO_TEST_BASE + 0x214);
	GPIO_SW_DEBUG(GPIO_DEBUG_BH "1 PIC is %x \n PIS is %x \n",PIC,PIS);

	/*this is a interface to connect interrupt top half and bottom half,if want to use bottom half,you can open fanctions sunxi_gpio_do_tasklet and tasklet_schedule*/

	/*
	tasklet_schedule(&sunxi_tasklet);
	*/
	return IRQ_HANDLED;
}
#ifdef support_ad7790x


static void setGpio(unsigned char gpio, int value)
{
	__gpio_set_value(gpio,value);
}
static int getGpio(unsigned char gpio)
{
	return __gpio_get_value(gpio);
}

#if 0
static void AD7790WirteReg(unsigned char byteword)
{
	unsigned char temp;
	int i;
	setGpio(AD7790_SPI2_CS1,0);
	msleep(AD7790_SPI_DELAY);
	temp=0x80;
	for(i=0;i<8;i++)
	{
		if((temp&byteword)==0)
		{
			setGpio(AD7790_SPI2_MOSI,0);
			msleep(AD7790_SPI_DELAY);
		}
		else 
		{
			setGpio(AD7790_SPI2_MOSI,1);
			msleep(AD7790_SPI_DELAY);
		}
		setGpio(AD7790_SPI2_CLK,0);
		msleep(AD7790_SPI_DELAY);
		setGpio(AD7790_SPI2_CLK,1);
		msleep(AD7790_SPI_DELAY);
		temp=temp>>1;
	}
	setGpio(AD7790_SPI2_CS1,1);
	msleep(AD7790_SPI_DELAY);
}

static void AD7790ReadFromReg(int bytenumber)
{
	int j;
	unsigned char temp1;
	setGpio(AD7790_SPI2_MOSI,0);
	msleep(AD7790_SPI_DELAY);
	setGpio(AD7790_SPI2_CS1,0);
	msleep(AD7790_SPI_DELAY);
	temp1=0x00;

	for(j=0;j<bytenumber;j++)
	{
		setGpio(AD7790_SPI2_CLK,0);
		msleep(AD7790_SPI_DELAY);
		if(getGpio(AD7790_SPI2_MISO)==0)
			temp1=temp1<<1;
		else
		{
			temp1=temp1<<1;
			temp1=temp1+0x01;
		}
		setGpio(AD7790_SPI2_CLK,1);
		msleep(AD7790_SPI_DELAY);
		if(j==7||j==15||j==23)
		{ 
			GPIO_SW_DEBUG(GPIO_DEBUG_BH "0x%02X \n",temp1);
			temp1=0x00;
		}
	}
	setGpio(AD7790_SPI2_CS1,1);
	msleep(AD7790_SPI_DELAY);
  }

static void AD7790ReadData(int readtime)
{
	unsigned short temp1;
	unsigned long  volteage_val=0;
	float ret;
	int i,j;
	setGpio(AD7790_SPI2_MOSI,0);
	msleep(AD7790_SPI_DELAY);
	setGpio(AD7790_SPI2_CS1,0);
	msleep(AD7790_SPI_DELAY);
	for(i=0;i<readtime;i++)
	{
		temp1=0;
		volteage_val=0;
		ret=0;
		while(getGpio(AD7790_SPI2_MISO));

		for(j=0;j<16;j++)
		{
			setGpio(AD7790_SPI2_CLK,0);
			msleep(AD7790_SPI_DELAY);
			if(getGpio(AD7790_SPI2_MISO)==0)
				temp1=temp1<<1;
			else
			{
				temp1=temp1<<1;
				temp1=temp1+0x01;
			}
			setGpio(AD7790_SPI2_CLK,1);
			msleep(AD7790_SPI_DELAY);
#if 0			
			if(j==15)
			{
				GPIO_SW_DEBUG(GPIO_DEBUG_BH "0x%02X->>",temp1);
				volteage_val=(temp1-32768)*2.5;
				GPIO_SW_DEBUG(GPIO_DEBUG_BH "%02X-->>",volteage_val);
				ret=(float)volteage_val/32768;
				GPIO_SW_DEBUG(GPIO_DEBUG_BH "%.6f",ret);

			}
#endif			
#if 1			
			if(j==7||j==15||j==23)
			{ 
				//volteage_val=volteage_val<<8;
				//volteage_val=volteage_val|temp1;
				//printf("%02X   ",temp1);
				GPIO_SW_DEBUG(GPIO_DEBUG_BH "%02X",temp1);

				//printf("%02X",volteage_val);
				temp1=0x00;
			}
#endif			
		}
		GPIO_SW_DEBUG(GPIO_DEBUG_BH "\r\n");
	}

	while(getGpio(AD7790_SPI2_MISO));
	AD7790WirteReg(0x38); //stop continuous read mode
	GPIO_SW_DEBUG(GPIO_DEBUG_BH "AD7790::read reg over!\r\n");
	setGpio(AD7790_SPI2_CS1,1);
	msleep(AD7790_SPI_DELAY);
}

#if 1
static int AD7790Read(int ret)
{
	int k;
	k=32;
	setGpio(AD7790_SPI2_CLK,1);
	msleep(AD7790_SPI_DELAY);
	GPIO_SW_DEBUG(GPIO_DEBUG_BH "\r\n");
	setGpio(AD7790_SPI2_CS1,0);
	msleep(AD7790_SPI_DELAY);
	setGpio(AD7790_SPI2_MOSI,1);
	msleep(AD7790_SPI_DELAY);

	while(k--)
	{
		setGpio(AD7790_SPI2_CLK,0);
		msleep(AD7790_SPI_DELAY);
		setGpio(AD7790_SPI2_CLK,1);
		msleep(AD7790_SPI_DELAY);
	}
	setGpio(AD7790_SPI2_CS1,1);
	msleep(AD7790_SPI_DELAY);
	AD7790WirteReg(0x10); //write to communication register. The next step is writing to mode REGISTER
	AD7790WirteReg(0x02); //set the mode register as continuous conversion mode. bipolar mode.buffered


	GPIO_SW_DEBUG(GPIO_DEBUG_BH "read from mode REGISTER\r\n");
	AD7790WirteReg(0x18); //write to communication register. The next step is read from mode REGISTER
	AD7790ReadFromReg(8);
	GPIO_SW_DEBUG(GPIO_DEBUG_BH "\r\n");
	
	GPIO_SW_DEBUG(GPIO_DEBUG_BH "read from filter REGISTER\r\n");
	AD7790WirteReg(0x28); //write to communication register. The next step is read from filter REGISTER
	AD7790ReadFromReg(8);
	GPIO_SW_DEBUG(GPIO_DEBUG_BH "\r\n");

	GPIO_SW_DEBUG(GPIO_DEBUG_BH "read from status REGISTER\r\n");
	AD7790WirteReg(0x08); //write to communication register. The next step is read from status REGISTER
	AD7790ReadFromReg(8);
	GPIO_SW_DEBUG(GPIO_DEBUG_BH "\r\n");


	GPIO_SW_DEBUG(GPIO_DEBUG_BH "read from data REGISTER\r\n");
	AD7790WirteReg(0x3C); //write to communication register. The next step is read from data register continuously
	AD7790ReadData(20);

	AD7790WirteReg(0x10); //write to communication register. The next step is writing to mode register
	AD7790WirteReg(0x0C2); //put ADC into powerdown mode.
	AD7790WirteReg(0x18); //write to communication register. The next step is read from mode REGISTER
	AD7790ReadFromReg(8);
	GPIO_SW_DEBUG(GPIO_DEBUG_BH "\r\nfinished");
	return ret;
}
#endif

#endif



#endif
struct gpio_sw {
	struct gpio_sw_classdev		cdev;
	struct gpio_sw_platdata		*pdata;
};

static inline struct gpio_sw *pdev_to_gpio(struct platform_device *dev)
{
	return platform_get_drvdata(dev);
}

static inline struct gpio_sw *to_gpio(struct gpio_sw_classdev *gpio_sw_cdev)
{
	return container_of(gpio_sw_cdev, struct gpio_sw, cdev);
}

static int	gpio_sw_cfg_set(struct gpio_sw_classdev *gpio_sw_cdev,int  mul_sel)
{
	struct gpio_sw *gpio = to_gpio(gpio_sw_cdev);
	int ret ;

	GPIO_SW_DEBUG(GPIO_DEBUG_BH "attending gpio_sw_cfg_set \n");
	GPIO_SW_DEBUG(GPIO_DEBUG_BH "pio_hdle is %u \n",gpio->cdev.pio_hdle);

    ret = sw_gpio_setcfg(gpio->cdev.pio_hdle, mul_sel);
	if ( !ret )
	gpio_sw_cdev->mul_sel=mul_sel;
	GPIO_SW_DEBUG(GPIO_DEBUG_BH "left gpio_sw_cfg_set \n");
	return ret;
}

static int	gpio_sw_pull_set(struct gpio_sw_classdev *gpio_sw_cdev,int  pull)
{
	struct gpio_sw *gpio = to_gpio(gpio_sw_cdev);
	int ret ;

	GPIO_SW_DEBUG(GPIO_DEBUG_BH "attending gpio_sw_pull_set \n");
	GPIO_SW_DEBUG(GPIO_DEBUG_BH "pio_hdle is %u \n",gpio->cdev.pio_hdle);

	ret =   sw_gpio_setpull(gpio->cdev.pio_hdle, pull);
	if ( !ret )
	gpio_sw_cdev->pull=pull;
	GPIO_SW_DEBUG(GPIO_DEBUG_BH "left gpio_sw_pull_set \n");
	return ret ;
}

static int	gpio_sw_data_set(struct gpio_sw_classdev *gpio_sw_cdev,int  data)
{
	struct gpio_sw *gpio = to_gpio(gpio_sw_cdev);

	GPIO_SW_DEBUG(GPIO_DEBUG_BH "attending gpio_sw_data_set \n");
	GPIO_SW_DEBUG(GPIO_DEBUG_BH "pio_hdle is %u \n",gpio->cdev.pio_hdle);

    __gpio_set_value(gpio->cdev.pio_hdle, data);
	gpio_sw_cdev->data=data;
	GPIO_SW_DEBUG(GPIO_DEBUG_BH "left gpio_sw_data_set \n");
	return 0;
}

static int	gpio_sw_drv_level_set(struct gpio_sw_classdev *gpio_sw_cdev,int  drv_level)
{
	struct gpio_sw *gpio = to_gpio(gpio_sw_cdev);
	int ret ;

	GPIO_SW_DEBUG(GPIO_DEBUG_BH "attending gpio_sw_drv_level_set \n");
	GPIO_SW_DEBUG(GPIO_DEBUG_BH "pio_hdle is %u \n",gpio->cdev.pio_hdle);

    ret = sw_gpio_setdrvlevel(gpio->cdev.pio_hdle, drv_level);
	if ( !ret )
	gpio_sw_cdev->drv_level=drv_level;
	GPIO_SW_DEBUG(GPIO_DEBUG_BH "left gpio_sw_drv_level_set \n");
	return ret ;
}

static int	gpio_sw_cfg_get(struct gpio_sw_classdev *gpio_sw_cdev)
{
	struct gpio_sw *gpio = to_gpio(gpio_sw_cdev);
	int ret;
	ret =   sw_gpio_getcfg(gpio->cdev.pio_hdle);
	return ret;
}

static int	gpio_sw_pull_get(struct gpio_sw_classdev *gpio_sw_cdev)
{
	struct gpio_sw *gpio = to_gpio(gpio_sw_cdev);
	int ret;
	ret =   sw_gpio_getpull(gpio->cdev.pio_hdle);
	return ret;
}

static int	gpio_sw_data_get(struct gpio_sw_classdev *gpio_sw_cdev)
{
	struct gpio_sw *gpio = to_gpio(gpio_sw_cdev);
	int ret;
    ret = __gpio_get_value(gpio->cdev.pio_hdle);
	return ret;
}

static int	gpio_sw_drv_level_get(struct gpio_sw_classdev *gpio_sw_cdev)
{
	struct gpio_sw *gpio = to_gpio(gpio_sw_cdev);
	int ret;
	ret =   sw_gpio_getdrvlevel(gpio->cdev.pio_hdle);
	GPIO_SW_DEBUG(GPIO_DEBUG_BH "in gpio_sw_drv_level_get ret is %d",ret);
	return ret;
}

static int  gpio_sw_put_resource(struct gpio_sw *gpio)
{
	GPIO_SW_DEBUG(GPIO_DEBUG_BH "attending gpio_sw_put_resource \n");
	GPIO_SW_DEBUG(GPIO_DEBUG_BH "pio_hdle is %u \n",gpio->cdev.pio_hdle);
    gpio_free(gpio->cdev.pio_hdle);
	return 0;
}

static int __devexit gpio_sw_remove(struct platform_device *dev)
{

	struct gpio_sw *gpio = pdev_to_gpio(dev);
		GPIO_SW_DEBUG(GPIO_DEBUG_BH "pio_hdle is %x \n",gpio->cdev.pio_hdle);
	gpio_sw_put_resource(gpio);
		GPIO_SW_DEBUG(GPIO_DEBUG_BH "gpio_sw_put_resource ok !\n");
	gpio_sw_classdev_unregister(&gpio->cdev);
		GPIO_SW_DEBUG(GPIO_DEBUG_BH "gpio_sw_classdev_unregister ok !\n");
	kfree(gpio);
		GPIO_SW_DEBUG(GPIO_DEBUG_BH "kfree ok !\n");		
	return 0;
}

static int __devinit gpio_sw_probe(struct platform_device *dev)
{
	struct gpio_sw *gpio;
	struct gpio_sw_platdata *pdata = dev->dev.platform_data;
	unsigned int irq_ctl;
	int ret;
	char io_area[16];
    script_item_value_type_e type;
    script_item_u item;

	gpio = kzalloc(sizeof(struct gpio_sw), GFP_KERNEL);
		GPIO_SW_DEBUG(GPIO_DEBUG_BH "kzalloc ok !\n");

	if (gpio == NULL) {
		dev_err(&dev->dev, "No memory for device\n");
		return -ENOMEM;
	}

	platform_set_drvdata(dev, gpio);
		GPIO_SW_DEBUG(GPIO_DEBUG_BH "platform_set_drvdata ok !\n");
	gpio->pdata = pdata;

    type = script_get_item("gpio_para", pdata->name, &item);
    if (SCIRPT_ITEM_VALUE_TYPE_PIO != type) {
        GPIO_SW_DEBUG(GPIO_DEBUG_BH "script_get_item return type err\n");
        return -EFAULT;
    }
    gpio->cdev.pio_hdle = item.gpio.gpio;

    if (0 != gpio_request_one(item.gpio.gpio, GPIOF_OUT_INIT_HIGH, NULL)){
        GPIO_SW_DEBUG(GPIO_DEBUG_BH "[gpio_para] gpio_request gpio:%d failed\n", item.gpio.gpio);
        return -EFAULT;
    }
#if 1
    /* add by cjcheng start {----------------------------------- */
    /* support Internal 3G 2013-04-24 */
    if (0 != sw_gpio_setall_range(&item.gpio, 1)) {
        GPIO_SW_DEBUG(GPIO_DEBUG_BH "[gpio_para] gpio setall_range err!");
        return -EFAULT;
    }
    /* add by cjcheng end   -----------------------------------} */

	device_create(gpiodev_class, NULL, MKDEV(GPIODEV_MAJOR,0),NULL, "gpio_sw");



    GPIO_SW_DEBUG(GPIO_DEBUG_BH "pio_hdle is %x \n",gpio->cdev.pio_hdle);
    GPIO_SW_DEBUG(GPIO_DEBUG_BH "gpio_num = %s\n",pdata->name);
    GPIO_SW_DEBUG(GPIO_DEBUG_BH "pd->name = %s\n",gpio->pdata->name);

    gpio->cdev.port = item.gpio.port;
    gpio->cdev.port_num = item.gpio.port_num;
    gpio->cdev.mul_sel = item.gpio.mul_sel;
    gpio->cdev.pull = item.gpio.pull;
    gpio->cdev.drv_level = item.gpio.drv_level;
    gpio->cdev.data = item.gpio.data;
    gpio->cdev.irq_type = 0x0;
	sprintf(io_area,"P%c%d",gpio->cdev.port+'A'-1,gpio->cdev.port_num);
	GPIO_SW_DEBUG(GPIO_DEBUG_BH "io_area is %s \n",io_area);

	gpio->cdev.gpio_sw_cfg_set = gpio_sw_cfg_set;
	gpio->cdev.gpio_sw_pull_set = gpio_sw_pull_set;
	gpio->cdev.gpio_sw_data_set = gpio_sw_data_set;
	gpio->cdev.gpio_sw_drv_level_set = gpio_sw_drv_level_set;

	gpio->cdev.gpio_sw_cfg_get = gpio_sw_cfg_get;
	gpio->cdev.gpio_sw_pull_get = gpio_sw_pull_get;
	gpio->cdev.gpio_sw_data_get = gpio_sw_data_get;
	gpio->cdev.gpio_sw_drv_level_get = gpio_sw_drv_level_get;

	gpio->cdev.name = io_area;
	gpio->cdev.flags |= pdata->flags;

	if(gpio->cdev.mul_sel == 6){
		if(gpio->cdev.port== 'H' - 'A' + 1){
			if((gpio->cdev.port_num >= 0) && (gpio->cdev.port_num <= 21)){
			irq_ctl	=	REG_RD(GPIO_TEST_BASE + 0x210);
			__raw_writel((1 << gpio->cdev.port_num) | irq_ctl, GPIO_TEST_BASE + 0x210);
			gpio->cdev.irq_num = gpio->cdev.port_num + 1;
			all_irq_enable = 1;
			}else{
			GPIO_SW_DEBUG(GPIO_DEBUG_BH "[gpio]: this pin don`t have EINT FUNCTION\n");
			kfree(gpio);
			return 1;
			}
		}else if(gpio->cdev.port== 'I' - 'A' + 1){
			if((gpio->cdev.port_num >= 10) && (gpio->cdev.port_num <= 19)){
			irq_ctl	=	REG_RD(GPIO_TEST_BASE + 0x210);
			__raw_writel((1 << (gpio->cdev.port_num + 12)) | irq_ctl, GPIO_TEST_BASE + 0x210);
			gpio->cdev.irq_num = gpio->cdev.port_num + 12 + 1;
			all_irq_enable = 1;
			}else{
			GPIO_SW_DEBUG(GPIO_DEBUG_BH "[gpio]: this pin don`t have EINT FUNCTION\n");
			kfree(gpio);
			return 1;
			}
		}
		else{
		GPIO_SW_DEBUG(GPIO_DEBUG_BH "[gpio]: this area don`t have EINT FUNCTION\n");
		kfree(gpio);
		return 1;
		}
	}
	gpio->cdev.irq=all_irq_enable;
	ret = gpio_sw_classdev_register(&dev->dev, &gpio->cdev);
		GPIO_SW_DEBUG(GPIO_DEBUG_BH "gpio_sw_classdev_register ok !\n");
	if (ret < 0) {
		dev_err(&dev->dev, "gpio_sw_classdev_register failed\n");
		kfree(gpio);
		return ret;
	}
#endif	

		GPIO_SW_DEBUG(GPIO_DEBUG_BH "pio_hdle is %x \n",gpio->cdev.pio_hdle);
		GPIO_SW_DEBUG(GPIO_DEBUG_BH "gpio_sw_classdev_register good !\n");
	return 0;
}

static void gpio_sw_release (struct device *dev)
{
	GPIO_SW_DEBUG(GPIO_DEBUG_BH "gpio_sw_release good !\n");
}

static int gpio_sw_suspend(struct platform_device *dev, pm_message_t state)
{
	GPIO_SW_DEBUG(GPIO_DEBUG_BH "gpio driver gpio_sw_suspend \n");
	return 0;
}

static int gpio_sw_resume(struct platform_device *dev)
{
	GPIO_SW_DEBUG(GPIO_DEBUG_BH "gpio driver gpio_sw_resume \n");
	return 0;
}

static struct platform_driver gpio_sw_driver = {
	.probe		= gpio_sw_probe,
	.remove		= gpio_sw_remove,
	.suspend	= gpio_sw_suspend,
	.resume		= gpio_sw_resume,
	.driver		= {
		.name		= "gpio_sw",
		.owner		= THIS_MODULE,
	},
};

static int gpiodev_open(struct inode *inode, struct file *filp)
{

	GPIO_SW_DEBUG(GPIO_DEBUG_BH "[gpio_fops] gpiodev_open !!\n");
	return 0;
}
static int gpiodev_release(struct inode *inode, struct file *filp)
{
	GPIO_SW_DEBUG(GPIO_DEBUG_BH "[gpio_fops] gpiodev_release !!\n");
	return 0;
}
static ssize_t gpiodev_write(struct file *filp, const char __user *buf,size_t count, loff_t *f_pos)
{
	if(buf[0]==0x01)		
	{
		if(buf[1]==1)	setGpio(AD7790_SPI2_CLK,1);
		else 			setGpio(AD7790_SPI2_CLK,0);
	}
	if(buf[0]==0x02)		
	{
		if(buf[1]==1)	setGpio(AD7790_SPI2_CS1,1);
		else 			setGpio(AD7790_SPI2_CS1,0);
	}
	if(buf[0]==0x03)		
	{
		if(buf[1]==1)	setGpio(AD7790_SPI2_MOSI,1);
		else 			setGpio(AD7790_SPI2_MOSI,0);
	}		
	return 0;
}
static ssize_t gpiodev_read(struct file *filp, char __user *buf, size_t count, loff_t *f_pos)
{
	char bufx[2]={0};
	bufx[0]=__gpio_get_value(AD7790_SPI2_MISO);
	
#ifdef support_ad7790x
	//AD7790Read(1);
#endif
	return copy_to_user(buf, &bufx, 1);	;
}
static long gpio_ioctl(struct file *filp, unsigned int cmd, unsigned long arg)
{
	switch(cmd) 
	{
		case 0:
		case 1:
			if(led_flag)
			{
				led_flag=0;
			}
			else
			{
				led_flag=1;
			}

				setGpio(AD7790_SPI2_CLK,led_flag);
			GPIO_SW_DEBUG(GPIO_DEBUG_BH "[gpio_fops] gpio ioctl:  %d %d!!\n",arg, cmd);
			//printk (DEVICE_NAME": %d %d\n", arg, cmd);
			return 0;
		default:
			return -EINVAL;
	}
}







static const struct file_operations gpiodev_fops = {
	.owner =	THIS_MODULE,
	/* REVISIT switch to aio primitives, so that userspace
	 * gets more complete API coverage.  It'll simplify things
	 * too, except for the locking.
	 */
	.write =	gpiodev_write,
	.read =		gpiodev_read,
	/*.unlocked_ioctl = spidev_ioctl,*/
	.unlocked_ioctl=gpio_ioctl,
	/*.compat_ioctl = spidev_compat_ioctl,*/
	.open =		gpiodev_open,
	.release =	gpiodev_release,
	/*.llseek =	no_llseek,*/
};



static int __init gpio_sw_init(void)
{
	int i, ret;
    int gpio_key_count = 0;
	int ret1;
    script_item_u gpio_used, *list = NULL;
    script_item_value_type_e type;



	ret1=register_chrdev(GPIODEV_MAJOR, "gpio_sw", &gpiodev_fops);
	if(ret1<0)
	{
		GPIO_SW_DEBUG(GPIO_DEBUG_BH "[gpio_fops] register chrdev fail !!\n");
		return ret1;
	}
	GPIO_SW_DEBUG(GPIO_DEBUG_BH "[gpio_fops] register chrdev success !!\n");

	gpiodev_class = class_create(THIS_MODULE, "gpiodev");
	if (IS_ERR(gpiodev_class)) 
	{
		unregister_chrdev(GPIODEV_MAJOR, "gpio_sw");
		return PTR_ERR(gpiodev_class);
	}
	GPIO_SW_DEBUG(GPIO_DEBUG_BH "SPIDEV::creat class ok\r\n");


	//device_create(gpiodev_class, NULL, MKDEV(GPIODEV_MAJOR,0),NULL, "gpio_sw");



    type = script_get_item("gpio_para", "gpio_used", &gpio_used);
    if (SCIRPT_ITEM_VALUE_TYPE_INT != type) {
        GPIO_SW_DEBUG(GPIO_DEBUG_BH "[gpio_para] gpio_used err!\n");
        return -1;
    }
    if (1 == gpio_used.val) {
        gpio_key_count = script_get_pio_list("gpio_para", &list);
        if (0 == gpio_key_count) {
            GPIO_SW_DEBUG(GPIO_DEBUG_BH "[gpio_para] get gpio list failed\n");
            return -1;
        }
    }

    for(i=0;i<gpio_key_count;i++)
    {
        pdatesw[i].flags = 0;
        sprintf(pdatesw[i].name, "gpio_pin_%d", i+1);

        gpio_sw_dev[i].name = "gpio_sw";
        gpio_sw_dev[i].id   = i;
        gpio_sw_dev[i].dev.platform_data= &pdatesw[i];
        gpio_sw_dev[i].dev.release		= gpio_sw_release;

        GPIO_SW_DEBUG(GPIO_DEBUG_BH "pdatesw[%d].gpio_name = %s\n",i,pdatesw[i].name);
//        GPIO_SW_DEBUG(GPIO_DEBUG_BH "pdatesw[%d] 1addr = %x \n",i,&pdatesw[i]);
//        GPIO_SW_DEBUG(GPIO_DEBUG_BH "gpio_sw_dev[%d] addr = %x \n",i,&gpio_sw_dev[i]);
        platform_device_register(&gpio_sw_dev[i]);
    }

    platform_driver_register(&gpio_sw_driver);
    if(all_irq_enable) {
        ret =  request_irq(GPIO_IRQ, sunxi_interrupt, IRQF_DISABLED, "gpio_sw", NULL);
        if (ret) {
            GPIO_SW_DEBUG(GPIO_DEBUG_BH "gpio: request irq failed\n");
            return ret;
        }
    }
//    GPIO_SW_DEBUG(GPIO_DEBUG_BH "gpio_sw_driver addr = %x \n",&gpio_sw_driver);

    return 0;
}

static void __exit gpio_sw_exit(void)
{
	int i, gpio_used/*,ret*/;
    int gpio_cnt;
    script_item_u val;
    script_item_value_type_e  type;

    type = script_get_item("gpio_para", "gpio_used", &val);
    if (SCIRPT_ITEM_VALUE_TYPE_INT != type) {
        GPIO_SW_DEBUG(GPIO_DEBUG_BH "[gpio_para] type err! gpio_para is disable\n");
        goto EXIT_END;
    }
    gpio_used = val.val;
    if(gpio_used == 0){
        GPIO_SW_DEBUG(GPIO_DEBUG_BH "gpio module is used not  \n");
        goto EXIT_END;
    }

    if(all_irq_enable){
        __raw_writel(0x00, GPIO_TEST_BASE + 0x210);
        free_irq(GPIO_IRQ,NULL);
    }

    script_item_u *list = NULL;
    gpio_cnt = script_get_pio_list("gpio_para", &list);
    for(i = 0; i < gpio_cnt; i++){
        platform_device_unregister(&gpio_sw_dev[i]);
    }

    GPIO_SW_DEBUG(GPIO_DEBUG_BH "platform_device_unregister finish !  \n");
//    GPIO_SW_DEBUG(GPIO_DEBUG_BH "gpio_sw_driver addr = %x \n",&gpio_sw_driver);
	class_destroy(gpiodev_class);
	unregister_chrdev(GPIODEV_MAJOR, "gpio_sw");

	platform_driver_unregister(&gpio_sw_driver);

EXIT_END:
	GPIO_SW_DEBUG(GPIO_DEBUG_BH "gpio_exit finish !  \n");

}

module_init(gpio_sw_init);
module_exit(gpio_sw_exit);

MODULE_AUTHOR("panlong <panlong@reuuimllatech.com>");
MODULE_DESCRIPTION("SW GPIO driver");
MODULE_LICENSE("GPL");
MODULE_ALIAS("platform:gpio_sw");
