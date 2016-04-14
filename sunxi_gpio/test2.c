#if 1
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>


#define GPIO_SW_DEBUG(fmt...) printf(fmt)

#define sleep_time 	1000*7
#define detect_count 50


#define AD7790_SPI2_CLK 	0x01
#define AD7790_SPI2_CS1 	0x02
#define AD7790_SPI2_MOSI 	0x03

#define AD7790_SPI_DELAY 5


static int fd;


static int getGpio(void)
{
	char readgpiobuf[1]={0};
	read(fd,readgpiobuf,sizeof(readgpiobuf)/sizeof(readgpiobuf[0]));
	return readgpiobuf[0];
}
static void setGpio(char gpio,int vol)
{
	char write_gpio_buf[2]={0};

	write_gpio_buf[0]=gpio;
	write_gpio_buf[1]=vol;
	write(fd,write_gpio_buf,sizeof(write_gpio_buf)/sizeof(write_gpio_buf[0]));
}




static void AD7790WirteReg(unsigned char byteword)
{
	unsigned char temp;
	int i;
	setGpio(AD7790_SPI2_CS1,0);
	usleep(AD7790_SPI_DELAY);
	temp=0x80;
	for(i=0;i<8;i++)
	{
		if((temp&byteword)==0)
		{
			setGpio(AD7790_SPI2_MOSI,0);
			usleep(AD7790_SPI_DELAY);
		}
		else 
		{
			setGpio(AD7790_SPI2_MOSI,1);
			usleep(AD7790_SPI_DELAY);
		}
		setGpio(AD7790_SPI2_CLK,0);
		usleep(AD7790_SPI_DELAY);
		setGpio(AD7790_SPI2_CLK,1);
		usleep(AD7790_SPI_DELAY);
		temp=temp>>1;
	}
	setGpio(AD7790_SPI2_CS1,1);
	usleep(AD7790_SPI_DELAY);
}

static void AD7790ReadFromReg(int bytenumber)
{
	int j;
	unsigned char temp1;
	setGpio(AD7790_SPI2_MOSI,0);
	usleep(AD7790_SPI_DELAY);
	setGpio(AD7790_SPI2_CS1,0);
	usleep(AD7790_SPI_DELAY);
	temp1=0x00;

	for(j=0;j<bytenumber;j++)
	{
		setGpio(AD7790_SPI2_CLK,0);
		usleep(AD7790_SPI_DELAY);
		if(getGpio()==0)
			temp1=temp1<<1;
		else
		{
			temp1=temp1<<1;
			temp1=temp1+0x01;
		}
		setGpio(AD7790_SPI2_CLK,1);
		usleep(AD7790_SPI_DELAY);
		if(j==7||j==15||j==23)
		{ 
			GPIO_SW_DEBUG( "0x%02X \n",temp1);
			temp1=0x00;
		}
	}
	setGpio(AD7790_SPI2_CS1,1);
	usleep(AD7790_SPI_DELAY);
  }

static void AD7790ReadData(int readtime)
{
	unsigned short temp1;
	unsigned long  volteage_val=0;
	float ret;
	int i,j;
	setGpio(AD7790_SPI2_MOSI,0);
	usleep(AD7790_SPI_DELAY);
	setGpio(AD7790_SPI2_CS1,0);
	usleep(AD7790_SPI_DELAY);
	for(i=0;i<readtime;i++)
	{
		temp1=0;
		volteage_val=0;
		ret=0;
		while(getGpio());

		for(j=0;j<16;j++)
		{
			setGpio(AD7790_SPI2_CLK,0);
			usleep(AD7790_SPI_DELAY);
			if(getGpio()==0)
				temp1=temp1<<1;
			else
			{
				temp1=temp1<<1;
				temp1=temp1+0x01;
			}
			setGpio(AD7790_SPI2_CLK,1);
			usleep(AD7790_SPI_DELAY);
#if 0			
			if(j==15)
			{
				GPIO_SW_DEBUG( "0x%02X->>",temp1);
				volteage_val=(temp1-32768)*2.5;
				GPIO_SW_DEBUG( "%02X-->>",volteage_val);
				ret=(float)volteage_val/32768;
				GPIO_SW_DEBUG( "%.6f",ret);

			}
#endif			
#if 1			
			if(j==7||j==15||j==23)
			{ 
				//volteage_val=volteage_val<<8;
				//volteage_val=volteage_val|temp1;
				//printf("%02X   ",temp1);
				GPIO_SW_DEBUG( "%02X",temp1);

				//printf("%02X",volteage_val);
				temp1=0x00;
			}
#endif			
		}
		GPIO_SW_DEBUG( "\r\n");
	}

	while(getGpio());
	AD7790WirteReg(0x38); //stop continuous read mode
	GPIO_SW_DEBUG( "AD7790::read reg over!\r\n");
	setGpio(AD7790_SPI2_CS1,1);
	usleep(AD7790_SPI_DELAY);
}

static int AD7790Read(int ret)
{
	int k;
	k=32;
	setGpio(AD7790_SPI2_CLK,1);
	usleep(AD7790_SPI_DELAY);
	GPIO_SW_DEBUG( "\r\n");
	setGpio(AD7790_SPI2_CS1,0);
	usleep(AD7790_SPI_DELAY);
	setGpio(AD7790_SPI2_MOSI,1);
	usleep(AD7790_SPI_DELAY);

	while(k--)
	{
		setGpio(AD7790_SPI2_CLK,0);
		usleep(AD7790_SPI_DELAY);
		setGpio(AD7790_SPI2_CLK,1);
		usleep(AD7790_SPI_DELAY);
	}
	setGpio(AD7790_SPI2_CS1,1);
	usleep(AD7790_SPI_DELAY);
	AD7790WirteReg(0x10); //write to communication register. The next step is writing to mode REGISTER
	AD7790WirteReg(0x02); //set the mode register as continuous conversion mode. bipolar mode.buffered


	GPIO_SW_DEBUG( "read from mode REGISTER\r\n");
	AD7790WirteReg(0x18); //write to communication register. The next step is read from mode REGISTER
	AD7790ReadFromReg(8);
	GPIO_SW_DEBUG( "\r\n");
	
	GPIO_SW_DEBUG( "read from filter REGISTER\r\n");
	AD7790WirteReg(0x28); //write to communication register. The next step is read from filter REGISTER
	AD7790ReadFromReg(8);
	GPIO_SW_DEBUG( "\r\n");

	GPIO_SW_DEBUG( "read from status REGISTER\r\n");
	AD7790WirteReg(0x08); //write to communication register. The next step is read from status REGISTER
	AD7790ReadFromReg(8);
	GPIO_SW_DEBUG( "\r\n");


	GPIO_SW_DEBUG( "read from data REGISTER\r\n");
	AD7790WirteReg(0x3C); //write to communication register. The next step is read from data register continuously
	AD7790ReadData(ret);

	AD7790WirteReg(0x10); //write to communication register. The next step is writing to mode register
	AD7790WirteReg(0x0C2); //put ADC into powerdown mode.
	AD7790WirteReg(0x18); //write to communication register. The next step is read from mode REGISTER
	AD7790ReadFromReg(8);
	GPIO_SW_DEBUG( "\r\nfinished");
	return ret;
}

int main(int argc, char *argv[])
{
	int i=0;
    int count=0;
    char buf[]={0x10,0xB2/*0x82*/};
	char read_buf[2]={0};
	const char read_spi_cmd[]={0x38};
	const char read_mod_cmd[]={0x18};
	char reset_spi_cmd[]={0x01,0x02,0x03,0x04,0x05,0x06,0xff,0xff,0xff,0xff,0xff};

    fd = open("/dev/gpio_sw", O_RDWR);

	//ioctl(fd,SPI_IOC_RD_MAX_SPEED_HZ,100000);
	//ioctl(fd,SPI_IOC_WR_MAX_SPEED_HZ,100000);

	printf("app::open device gpio "); 
    if (fd < 0) 
	{
        printf("-- fail!!\r\n"); 
        exit(1);
    }
	else
	{
        printf("-- success!!\r\n"); 
	}

	for(i=0;i<argc;i++)
	{  
		printf("argv[%d] is:%s \n",i,argv[i]);      
	}  

	if(!memcmp(argv[1],"read",(sizeof("read")-1)))
	{
		printf("read AD7990!!!\r\n"); 
	    read(fd,read_buf,sizeof(read_buf)/sizeof(read_buf[0]));
		//printf("app::read byte \r\n"); 
	}
	else if(!memcmp(argv[1],"test",(sizeof("test")-1)))
	{
		AD7790Read(0);
	}
	else
	{
        printf("no command\r\n"); 
	
	}
    close(fd);
    return 0;
}
#endif
/*#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>



int main(int argc ,char **argv)
{
	printf("asdfasdfasdfas\r\n");
	return 0;
}*/
