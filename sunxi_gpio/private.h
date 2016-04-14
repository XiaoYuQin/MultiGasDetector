#ifndef PRIVATE_H
#define PRIVATE_H


#if 0
#define DS18B20_DEBUG(fmt...) printk(fmt)
#else
#define DS18B20_DEBUG(fmt...) do{} while (0)
#endif


#if 1
#define MULTGAS_DEBUG(fmt...) printk(fmt)
#else
#define MULTGAS_DEBUG(fmt...) do{} while (0)
#endif

#if 1
#define AD7794_DEBUG(fmt...) printk(fmt)
#else
#define AD7794_DEBUG(fmt...) do{} while (0)
#endif


#define MULTGAS_DEBUG_BH		KERN_ALERT
#define DS18B20_DEBUG_BH		KERN_ALERT/*KERN_DEBUG*/
#define AD7790_DEBUG_BH			KERN_ALERT/*KERN_DEBUG*/
#define AD7794_DEBUG_BH			KERN_ALERT/*KERN_DEBUG*/


#define AD7790_SPI_DELAY 3 //2000




#define AD7790_ENABLEx
#define AD7794_ENABLE

#endif
