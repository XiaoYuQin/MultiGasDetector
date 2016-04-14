/********************************************************************************
 Author        : CAST 

 Date          : 2011-3-21

 Hardware      : ADuC7026+AD7794	
********************************************************************************/
#include <linux/delay.h>
#include <mach/gpio.h> /* for new api */
#include <linux/gpio.h> /* for new api */
#include <asm/io.h>
#include <linux/kthread.h>


//#include "ADuC7026.h"
//#include "ADuC7026Driver.h"
#include "AD7794.h"
#include "private.h"


unsigned long int AD7794Registers[9]; 	


unsigned long int AIN1Data[AD7794_DATA_SAVE];
unsigned long int AIN2Data[AD7794_DATA_SAVE];
unsigned long int AIN3Data[AD7794_DATA_SAVE];
unsigned long int AIN4Data[AD7794_DATA_SAVE];
unsigned long int AIN5Data[AD7794_DATA_SAVE];
unsigned long int AIN6Data[AD7794_DATA_SAVE];


extern unsigned char AD7790_SPI2_CS2;
extern unsigned char AD7790_SPI2_CS1;
extern unsigned char AD7790_SPI2_CLK;
extern unsigned char AD7790_SPI2_MOSI;
extern unsigned char AD7790_SPI2_MISO;

unsigned char AD7794_DIN;
unsigned char AD7794_CS;
unsigned char AD7794_DOUTRDY;
unsigned char AD7794_SCLK;


static void setGpio(unsigned char gpio, int value)
{
	__gpio_set_value(gpio,value);
}
static int getGpio(unsigned char gpio)
{
	return __gpio_get_value(gpio);
}

#define ADuC7026_OutputBit(x,y) setGpio(x,y)
#define ADuC7026_InputBit(x) getGpio(x)


void AD7794InitGpio(void)
{
	AD7794_DOUTRDY = AD7790_SPI2_MISO;
	AD7794_SCLK = AD7790_SPI2_CS1;
	AD7794_DIN = AD7790_SPI2_MOSI;
	AD7794_CS = AD7790_SPI2_CLK;
	
	ADuC7026_OutputBit(AD7794_DOUTRDY,1);
	ADuC7026_OutputBit(AD7794_CS,1);
	ADuC7026_InputBit(AD7794_DIN);
	ADuC7026_OutputBit(AD7794_SCLK,1);
}

void AD7794WriteRegister(unsigned char RegisterAddress, unsigned long int *RegisterBuffer)
{
	unsigned char i;
	unsigned long int temp;

	temp=RegisterBuffer[RegisterAddress];

	ADuC7026_OutputBit(AD7794_CS,0);

	switch(RegisterAddress)
	{
		case COMMUNICATIONS:			 			 		
		case IO:
			for(i=0; i<8; i++)
			{
				
				
				if((temp&0x80)==0x80)	
					ADuC7026_OutputBit(AD7794_DOUTRDY,1);
				else
					ADuC7026_OutputBit(AD7794_DOUTRDY,0);
				ADuC7026_OutputBit(AD7794_SCLK,0);
				temp<<=1;
		
				ADuC7026_OutputBit(AD7794_SCLK,1);	
			}
			break;

		case MODE:					
		case CONFIGURATION:	
			for(i=0; i<16; i++)
			{
				
				
				if((temp&0x8000)==0x8000)	
					ADuC7026_OutputBit(AD7794_DOUTRDY,1);
				else
					ADuC7026_OutputBit(AD7794_DOUTRDY,0);
				ADuC7026_OutputBit(AD7794_SCLK,0);
				temp<<=1;
		
				ADuC7026_OutputBit(AD7794_SCLK,1);	
			}
			break;
			 			
		case OFFSET:			
		case FULLSCALE:		
			for(i=0; i<16; i++)
			{
				
				
				if((temp&0x800000)==0x800000)	
					ADuC7026_OutputBit(AD7794_DOUTRDY,1);
				else
					ADuC7026_OutputBit(AD7794_DOUTRDY,0);
				ADuC7026_OutputBit(AD7794_SCLK,0);
				temp<<=1;
		
				ADuC7026_OutputBit(AD7794_SCLK,1);	
			}
			break;

		default:
			break;
	}
	
	ADuC7026_OutputBit(AD7794_CS,1);	
}

void AD7794ReadRegister(unsigned char RegisterAddress, unsigned long int *RegisterBuffer)
{
	unsigned char i;
	unsigned long int temp;

	temp=0;

	ADuC7026_OutputBit(AD7794_CS,0);

	switch(RegisterAddress)
	{
		case STATUS:			 			 		
		case ID:
			for(i=0; i<8; i++)
			{
				temp<<=1;

				ADuC7026_OutputBit(AD7794_SCLK,0);
				
				temp+=ADuC7026_InputBit(AD7794_DIN);
		
				ADuC7026_OutputBit(AD7794_SCLK,1);	
			}

			RegisterBuffer[RegisterAddress]=temp;

			break;

		case MODE:					
		case CONFIGURATION:	
			for(i=0; i<16; i++)
			{
				temp<<=1;

				ADuC7026_OutputBit(AD7794_SCLK,0);
				
				temp+=ADuC7026_InputBit(AD7794_DIN);
		
				ADuC7026_OutputBit(AD7794_SCLK,1);	
			}
			
			RegisterBuffer[RegisterAddress]=temp;

			break;
			 			
		case OFFSET:			
		case FULLSCALE:
		case DATA:		
			for(i=0; i<16; i++)
			{
				temp<<=1;

				ADuC7026_OutputBit(AD7794_SCLK,0);
				
				temp+=ADuC7026_InputBit(AD7794_DIN);
		
				ADuC7026_OutputBit(AD7794_SCLK,1);	
			}

			RegisterBuffer[RegisterAddress]=temp;

			break;

		default:
			break;
	}
	
	ADuC7026_OutputBit(AD7794_CS,1);
}

void AD7794ReadResultForSingleConversion(unsigned char DataIndex, unsigned long int *DataBuffer)
{
	while(ADuC7026_InputBit(AD7794_DIN)!=0) 
	{
		if( kthread_should_stop())  break; 
	}

	AD7794Registers[COMMUNICATIONS]=WRITE_ENABLE|READ_DATA|RS_DATA|CREAD_DISABLE;
	AD7794WriteRegister(COMMUNICATIONS, AD7794Registers);
	AD7794ReadRegister(DATA, AD7794Registers);

	DataBuffer[DataIndex]=AD7794Registers[DATA];
}

void AD7794ReadResultForContinuousConversion(unsigned char StartIndex, unsigned char NumberOfData, unsigned long int *DataBuffer)
{
	unsigned char i;


	for(i=0; i<NumberOfData; i++)
	{
		ADuC7026_OutputBit(AD7794_CS,0);

		while(ADuC7026_InputBit(AD7794_DIN)!=0) {
			if( kthread_should_stop())  break; 
		}
	
		AD7794Registers[COMMUNICATIONS]=WRITE_ENABLE|READ_DATA|RS_DATA|CREAD_DISABLE;
		AD7794WriteRegister(COMMUNICATIONS, AD7794Registers);
		AD7794ReadRegister(DATA, AD7794Registers);
	
		DataBuffer[StartIndex+i]=AD7794Registers[DATA];
	}
}

void AD7794ReadResultForContinuousRead(unsigned char StartIndex, unsigned char NumberOfData, unsigned long int *DataBuffer)
{
	unsigned char i;

	AD7794Registers[COMMUNICATIONS]=WRITE_ENABLE|READ_DATA|RS_DATA|CREAD_ENABLE;
	//AD7794_DEBUG(AD7794_DEBUG_BH "AD7794Registers[0] = %lx \n",AD7794Registers[COMMUNICATIONS]);
	
	AD7794WriteRegister(COMMUNICATIONS, AD7794Registers);

	for(i=0; i<NumberOfData; i++)
	{
		ADuC7026_OutputBit(AD7794_CS,0);

		while(ADuC7026_InputBit(AD7794_DIN)!=0) {
			if( kthread_should_stop())  break; 
		}
	
		AD7794ReadRegister(DATA, AD7794Registers);
	
		DataBuffer[StartIndex+i]=AD7794Registers[DATA];
	}

}

void AD7794ExitContinuousRead(void)
{
	while(ADuC7026_InputBit(AD7794_DIN)!=0) {
		if( kthread_should_stop())  break; 
	}
	AD7794Registers[COMMUNICATIONS]=WRITE_ENABLE|READ_DATA|RS_DATA|CREAD_DISABLE;
	AD7794WriteRegister(COMMUNICATIONS, AD7794Registers);
}

void AD7794SoftwareReset(void)
{
	unsigned char i;

	ADuC7026_OutputBit(AD7794_CS,0);
	
	for(i=0;i<32; i++)
	{
		ADuC7026_OutputBit(AD7794_SCLK,0);	
		ADuC7026_OutputBit(AD7794_DOUTRDY,1);
		ADuC7026_OutputBit(AD7794_SCLK,1);
	}	

	ADuC7026_OutputBit(AD7794_CS,1);
}


void AD7794GetAllAdcData(unsigned long int channel,unsigned count)
{
	int i = 0;
	if(count > AD7794_DATA_SAVE)	return;
	
	AD7794InitGpio();
	AD7794SoftwareReset();
	ndelay(100);
	
	AD7794Registers[COMMUNICATIONS] = WRITE_ENABLE|WRITE_DATA|RS_CONFIGURATION|CREAD_DISABLE;/*ÅäÖÃ¼Ä´æÆ÷*/
	AD7794WriteRegister(COMMUNICATIONS, AD7794Registers);						 
	AD7794Registers[CONFIGURATION] = SFR_CFG_ADC_RANGE_2_5V|channel;
	AD7794WriteRegister(CONFIGURATION, AD7794Registers);

	AD7794Registers[COMMUNICATIONS]=WRITE_ENABLE|WRITE_DATA|RS_MODE|CREAD_DISABLE;
	AD7794WriteRegister(COMMUNICATIONS, AD7794Registers);
	AD7794Registers[MODE]=SFR_MODE_INTER_64K_CRYSTAL_DIS_CLK|SFR_MODE_FADC_16_7HZ|SFR_MODE_CONTINUOUS_CONVERSION;
	AD7794WriteRegister(MODE, AD7794Registers);

	switch(channel)
	{
		case SFR_CFG_CHANNEL_AIN1:
			AD7794ReadResultForContinuousRead(0,count,AIN1Data);
			AD7794ExitContinuousRead();
			for(i =0 ;i<count ; i++)		AD7794_DEBUG(AD7794_DEBUG_BH "AIN1Data = %lx \n",AIN1Data[i]);
		  break;
		case SFR_CFG_CHANNEL_AIN2:
			AD7794ReadResultForContinuousRead(0,count,AIN2Data);
			AD7794ExitContinuousRead();
			for(i =0 ;i<count ; i++)		AD7794_DEBUG(AD7794_DEBUG_BH "AIN2Data = %lx \n",AIN2Data[i]);
		  break;
		case SFR_CFG_CHANNEL_AIN3:
			AD7794ReadResultForContinuousRead(0,count,AIN3Data);
			AD7794ExitContinuousRead();
			for(i =0 ;i<count ; i++)		AD7794_DEBUG(AD7794_DEBUG_BH "AIN3Data = %lx \n",AIN3Data[i]);
		  break;
		case SFR_CFG_CHANNEL_AIN4:
			AD7794ReadResultForContinuousRead(0,count,AIN4Data);
			AD7794ExitContinuousRead();
			for(i =0 ;i<count ; i++)		AD7794_DEBUG(AD7794_DEBUG_BH "AIN4Data = %lx \n",AIN4Data[i]);
	      break;
		case SFR_CFG_CHANNEL_AIN5:
			AD7794ReadResultForContinuousRead(0,count,AIN5Data);
			AD7794ExitContinuousRead();
			for(i =0 ;i<count ; i++)		AD7794_DEBUG(AD7794_DEBUG_BH "AIN5Data = %lx \n",AIN5Data[i]);
		  break;
		case SFR_CFG_CHANNEL_AIN6:
			AD7794ReadResultForContinuousRead(0,count,AIN6Data);
			AD7794ExitContinuousRead();
			for(i =0 ;i<count ; i++)		AD7794_DEBUG(AD7794_DEBUG_BH "AIN6Data = %lx \n",AIN6Data[i]);
		  break;
				
	}



}




