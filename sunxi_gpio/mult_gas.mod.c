#include <linux/module.h>
#include <linux/vermagic.h>
#include <linux/compiler.h>

MODULE_INFO(vermagic, VERMAGIC_STRING);

struct module __this_module
__attribute__((section(".gnu.linkonce.this_module"))) = {
 .name = KBUILD_MODNAME,
 .init = init_module,
#ifdef CONFIG_MODULE_UNLOAD
 .exit = cleanup_module,
#endif
 .arch = MODULE_ARCH_INIT,
};

MODULE_INFO(intree, "Y");

static const struct modversion_info ____versions[]
__used
__attribute__((section("__versions"))) = {
	{ 0x708a9a2b, "module_layout" },
	{ 0xf9a482f9, "msleep" },
	{ 0x2e5810c6, "__aeabi_unwind_cpp_pr1" },
	{ 0x597e7b79, "dev_set_drvdata" },
	{ 0x31b5d6b6, "malloc_sizes" },
	{ 0x5f97bc7b, "device_destroy" },
	{ 0x432fd7f6, "__gpio_set_value" },
	{ 0x132a7a5b, "init_timer_key" },
	{ 0x91715312, "sprintf" },
	{ 0x30b1f4af, "kthread_create_on_node" },
	{ 0x7d11c268, "jiffies" },
	{ 0x6f0036d9, "del_timer_sync" },
	{ 0x65d6d0f0, "gpio_direction_input" },
	{ 0x1a03a7f4, "dev_err" },
	{ 0x27e1a049, "printk" },
	{ 0x6270ed8e, "kthread_stop" },
	{ 0xe08e0429, "script_get_pio_list" },
	{ 0xa8f59416, "gpio_direction_output" },
	{ 0xc8573e53, "device_create" },
	{ 0xc8fd727e, "mod_timer" },
	{ 0x3e273c48, "platform_device_unregister" },
	{ 0xd9605d4c, "add_timer" },
	{ 0xb14c9454, "platform_driver_register" },
	{ 0x7f98e274, "delay_fn" },
	{ 0x279fb16d, "kmem_cache_alloc" },
	{ 0xb687a464, "platform_device_register" },
	{ 0x69ead5a6, "script_get_item" },
	{ 0x3bd1b1f6, "msecs_to_jiffies" },
	{ 0x727b2c97, "wake_up_process" },
	{ 0xd2965f6f, "kthread_should_stop" },
	{ 0x37a0cba, "kfree" },
	{ 0x6c8d5ae8, "__gpio_get_value" },
	{ 0x7e74e833, "class_destroy" },
	{ 0xefd6cf06, "__aeabi_unwind_cpp_pr0" },
	{ 0x32c5ddf6, "platform_driver_unregister" },
	{ 0xa15ae7ca, "__class_create" },
	{ 0xaa5ff8b1, "dev_get_drvdata" },
	{ 0x32bb0455, "sw_gpio_setall_range" },
};

static const char __module_depends[]
__used
__attribute__((section(".modinfo"))) =
"depends=";


MODULE_INFO(srcversion, "F7F7B14DD9980B63A08FB0C");
