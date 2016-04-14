ifneq ($(KERNELRELEASE),)
#	obj-m := qxy_test.o
#	qxy_test-objs=test1.o
#	obj-m 	+=test1.o
#	obj-m := gpio_sw.o
	obj-m := mult_gas.o
	mult_gas-objs := multGasDetectDriver.o AD7790.o DS18B20.o AD8400.o AD7794.o
#	gpio-objs=gpio_sw.o  gpio_sw_core.o gpio_sw_class.o
else
	KDIR := /media/515bab37-4e3a-4b51-b900-cd92d1965d96/a20-420-V12/lichee/linux-3.3
all:
	make -C $(KDIR) M=$(PWD) modules ARCH=arm CROSS_COMPILE=/home/smdt/external-toolchain/bin/arm-linux-gnueabi-
	rm -f *.mod.c *.order *.symvers *.mod.o *.mod.c *.o
clean:
	rm -f *.ko *.o *.mod.o *.mod.c *.symvers modul*
endif
