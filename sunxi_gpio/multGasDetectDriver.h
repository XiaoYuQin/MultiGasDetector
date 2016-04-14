#ifndef MULT_GASE_DETECT_DRIVER_H
#define MULT_GASE_DETECT_DRIVER_H
#include <linux/list.h>
#include <linux/spinlock.h>
#include <linux/rwsem.h>
#include <linux/timer.h>
#define GPIO_TEST_BASE 0xf1c20800
#define GPIO_INT_BASE 0xf1c20400



struct mult_gas_platdata {

	unsigned int		flags;
	char				*name;

};
struct mult_gas_classdev{
	const char	*name;

	int		(*mult_gas_cfg_set)(struct mult_gas_classdev *mult_gas_cdev, int  cfg_sel);		
	int		(*mult_gas_data_get)(struct mult_gas_classdev *mult_gas_cdev,int  pull);		

	
	struct device		*dev;
};

extern void mult_gas_classdev_suspend(struct mult_gas_classdev *mult_gas_cdev);
extern void mult_gas_classdev_resume(struct mult_gas_classdev *mult_gas_cdev);

extern int mult_gas_classdev_register(struct device *parent,struct mult_gas_classdev *mult_gas_cdev);
extern void mult_gas_classdev_unregister(struct mult_gas_classdev *mult_gas_cdev);


#endif

