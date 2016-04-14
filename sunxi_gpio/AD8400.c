#include <linux/delay.h>
#include <mach/gpio.h> /* for new api */
#include <linux/gpio.h> /* for new api */
#include <asm/io.h>
#include <linux/kthread.h>

#include "AD8400.h"
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

void AD8400WriteReg(unsigned int byteword)
{
	//unsigned char temp;
	int i;
	MULTGAS_DEBUG(MULTGAS_DEBUG_BH "mult-gas::AD8400WriteReg %d !!\n",byteword);
	setGpio(AD7790_SPI2_CLK,0);
	setGpio(AD7790_SPI2_CS2,0);
	ndelay(AD7790_SPI_DELAY);
//	setGpio(AD7790_SPI2_CLK,1);
	//temp=0x80;
	setGpio(AD7790_SPI2_MOSI,0);
	setGpio(AD7790_SPI2_CLK,1);
	ndelay(AD7790_SPI_DELAY);
	setGpio(AD7790_SPI2_CLK,0);
	ndelay(AD7790_SPI_DELAY);
	setGpio(AD7790_SPI2_CLK,1);
	ndelay(AD7790_SPI_DELAY);
	setGpio(AD7790_SPI2_CLK,0);

	for(i=0;i<8;i++)
	{
		if((byteword&0x80)==0)
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
		byteword<<=1;
	}
	setGpio(AD7790_SPI2_CLK,0);
	setGpio(AD7790_SPI2_CS2,1);
//	setGpio(AD7790_SPI2_CLK,1);
	ndelay(AD7790_SPI_DELAY);

}