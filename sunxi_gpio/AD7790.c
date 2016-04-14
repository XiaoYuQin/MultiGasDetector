#include <linux/delay.h>
#include <mach/gpio.h> /* for new api */
#include <linux/gpio.h> /* for new api */
#include <asm/io.h>
#include <linux/kthread.h>

#include "AD7790.h"
#include "private.h"


extern unsigned char AD7790_SPI2_CS2;
extern unsigned char AD7790_SPI2_CS1;
extern unsigned char AD7790_SPI2_CLK;
extern unsigned char AD7790_SPI2_MOSI;
extern unsigned char AD7790_SPI2_MISO;
extern unsigned short ADC_CH1_DATA;

static void setGpio(unsigned char gpio, int value)
{
	__gpio_set_value(gpio,value);
}
static int getGpio(unsigned char gpio)
{
	return __gpio_get_value(gpio);
}



void AD7790WirteReg(unsigned char byteword)
{
	unsigned char temp;
	int i;
	setGpio(AD7790_SPI2_CS1,0);
	ndelay(AD7790_SPI_DELAY);
	temp=0x80;
	for(i=0;i<8;i++)
	{
		if((temp&byteword)==0)
		{
			setGpio(AD7790_SPI2_MOSI,0);
			ndelay(AD7790_SPI_DELAY);
		}
		else 
		{
			setGpio(AD7790_SPI2_MOSI,1);
			ndelay(AD7790_SPI_DELAY);
		}
		setGpio(AD7790_SPI2_CLK,0);
		ndelay(AD7790_SPI_DELAY);
		setGpio(AD7790_SPI2_CLK,1);
		ndelay(AD7790_SPI_DELAY);
		temp=temp>>1;
	}
	setGpio(AD7790_SPI2_CS1,1);
	ndelay(AD7790_SPI_DELAY);
}

void AD7790ReadFromReg(int bytenumber)
{
	int j;
	unsigned char temp1;
	setGpio(AD7790_SPI2_MOSI,0);
	ndelay(AD7790_SPI_DELAY);
	setGpio(AD7790_SPI2_CS1,0);
	ndelay(AD7790_SPI_DELAY);
	temp1=0x00;

	for(j=0;j<bytenumber;j++)
	{
		setGpio(AD7790_SPI2_CLK,0);
		ndelay(AD7790_SPI_DELAY);
		if(getGpio(AD7790_SPI2_MISO)==0)
			temp1=temp1<<1;
		else
		{
			temp1=temp1<<1;
			temp1=temp1+0x01;
		}
		setGpio(AD7790_SPI2_CLK,1);
		ndelay(AD7790_SPI_DELAY);
		if(j==7||j==15||j==23)
		{ 
			//GPIO_SW_DEBUG( "0x%02X \n",temp1);
			MULTGAS_DEBUG(AD7790_DEBUG_BH " 0x%02X ",temp1);
			temp1=0x00;
		}
	}
	setGpio(AD7790_SPI2_CS1,1);
	ndelay(AD7790_SPI_DELAY);
}

int AD7790ReadData(int readtime)
{
	unsigned short temp1;
	unsigned long  volteage_val=0;
	float ret;
	int i,j;
	setGpio(AD7790_SPI2_MOSI,0);
	ndelay(AD7790_SPI_DELAY);
	setGpio(AD7790_SPI2_CS1,0);
	ndelay(AD7790_SPI_DELAY);
	for(i=0;i<readtime;i++)
	{
		temp1=0;
		volteage_val=0;
		ret=0;
		while(getGpio(AD7790_SPI2_MISO))
		{
			if( kthread_should_stop())  return -1; 
		}

		for(j=0;j<16;j++)
		{
			setGpio(AD7790_SPI2_CLK,0);
			ndelay(AD7790_SPI_DELAY);
			if(getGpio(AD7790_SPI2_MISO)==0)
				temp1=temp1<<1;
			else
			{
				temp1=temp1<<1;
				temp1=temp1+0x01;
			}
			setGpio(AD7790_SPI2_CLK,1);
			ndelay(AD7790_SPI_DELAY);
#if 1			
			if(j==15)
			{
				MULTGAS_DEBUG(AD7790_DEBUG_BH " 0x%02X ",temp1);
				/*volteage_val=(temp1-32768)*2.5;
				MULTGAS_DEBUG(AD7790_DEBUG_BH "%02X-->>",volteage_val);
				ret=(float)volteage_val/32768;
				MULTGAS_DEBUG(AD7790_DEBUG_BH "%.6f",ret);*/
				ADC_CH1_DATA=temp1;
			}
#endif			
#if 0			
			if(j==7||j==15||j==23)
			{ 
				//volteage_val=volteage_val<<8;
				//volteage_val=volteage_val|temp1;
				//printf("%02X   ",temp1);
				//GPIO_SW_DEBUG( "%02X",temp1);
				MULTGAS_DEBUG(AD7790_DEBUG_BH "%02X",temp1);
				//printf("%02X",volteage_val);
				temp1=0x00;
			}
#endif			
		}
		//MULTGAS_DEBUG(AD7790_DEBUG_BH "\r\n");
	}

	while(getGpio(AD7790_SPI2_MISO))
	{
		if( kthread_should_stop())  return -1; 
	}
	AD7790WirteReg(0x38); //stop continuous read mode
	MULTGAS_DEBUG(AD7790_DEBUG_BH "\r\nmult-gas::AD7790::read reg over!\r\n");
	setGpio(AD7790_SPI2_CS1,1);
	ndelay(AD7790_SPI_DELAY);
	return 0;
}

int AD7790Read(int ret)
{
	int k;
	k=32;
	setGpio(AD7790_SPI2_CLK,1);
	ndelay(AD7790_SPI_DELAY);
	MULTGAS_DEBUG(AD7790_DEBUG_BH "\r\nmult-gas::AD7790::\r\n");
	setGpio(AD7790_SPI2_CS1,0);
	ndelay(AD7790_SPI_DELAY);
	setGpio(AD7790_SPI2_MOSI,1);
	ndelay(AD7790_SPI_DELAY);

	while(k--)
	{
		setGpio(AD7790_SPI2_CLK,0);
		ndelay(AD7790_SPI_DELAY);
		setGpio(AD7790_SPI2_CLK,1);
		ndelay(AD7790_SPI_DELAY);
	}
	setGpio(AD7790_SPI2_CS1,1);
	ndelay(AD7790_SPI_DELAY);
	AD7790WirteReg(0x10); //write to communication register. The next step is writing to mode REGISTER
	AD7790WirteReg(0x02); //set the mode register as continuous conversion mode. bipolar mode.buffered


	MULTGAS_DEBUG(AD7790_DEBUG_BH "mult-gas::AD7790::read from mode REGISTER=  ");
	AD7790WirteReg(0x18); //write to communication register. The next step is read from mode REGISTER
	AD7790ReadFromReg(8);
	MULTGAS_DEBUG(AD7790_DEBUG_BH "\r\n");

	MULTGAS_DEBUG(AD7790_DEBUG_BH "mult-gas::AD7790::read from filter REGISTER=  ");

	AD7790WirteReg(0x28); //write to communication register. The next step is read from filter REGISTER
	AD7790ReadFromReg(8);
	MULTGAS_DEBUG(AD7790_DEBUG_BH "\r\n");

	MULTGAS_DEBUG(AD7790_DEBUG_BH "mult-gas::AD7790::read from status REGISTER=  ");
	AD7790WirteReg(0x08); //write to communication register. The next step is read from status REGISTER
	AD7790ReadFromReg(8);
	MULTGAS_DEBUG(AD7790_DEBUG_BH "\r\n");


	MULTGAS_DEBUG(AD7790_DEBUG_BH "mult-gas::AD7790::read from data REGISTER=  ");
	AD7790WirteReg(0x3C); //write to communication register. The next step is read from data register continuously
	AD7790ReadData(ret);
	MULTGAS_DEBUG(AD7790_DEBUG_BH "\r\n");

	AD7790WirteReg(0x10); //write to communication register. The next step is writing to mode register
	AD7790WirteReg(0x0C2); //put ADC into powerdown mode.
	AD7790WirteReg(0x18); //write to communication register. The next step is read from mode REGISTER
	AD7790ReadFromReg(8);
	MULTGAS_DEBUG(AD7790_DEBUG_BH "mult-gas::AD7790::finished \n");
	return ret;
}

