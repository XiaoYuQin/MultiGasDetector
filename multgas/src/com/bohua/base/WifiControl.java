package com.bohua.base;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiControl{
	
	private WifiManager myWifiManager;
	
	public static final int WIFICIPHER_NOPASS = 0;  //代表加密类型为无密码
	
	public static final int WIFICIPHER_WEP = 1; //代表加密类型为WEP
	
	public static final int WIFICIPHER_WPA = 2; //代表加密类型为WPA
	
	/*本类的构造函数*/
	
	public WifiControl(WifiManager wifiManager) {
		// TODO Auto-generated constructor stub
		this.myWifiManager=wifiManager;
	}
	/*用于打开WIFI*/
	public void openWifi(){
		if(!myWifiManager.isWifiEnabled() && 
				myWifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLING)
		{
			myWifiManager.setWifiEnabled(true);
		}
	}
	
	public void closeWifi(){
		if(myWifiManager.isWifiEnabled() &&
				myWifiManager.getWifiState() != WifiManager.WIFI_STATE_DISABLING)
		{
			myWifiManager.setWifiEnabled(false);
		}
	}
	/*本方法用于配置可以连接的网络
	 * SSID 为WIFI 热点的SSID
	 * password 为该热点的密码
	 * type 表示加密的类型
	 * return 配置好的wifi热点
	 * 注： 本方法只有无密码类型和WPA类型通过验证*/
	public WifiConfiguration CreateWifiInfo(String SSID, String Password,
			int Type) {
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.allowedGroupCiphers.clear();
		config.allowedPairwiseCiphers.clear();
		config.SSID = "\"" + SSID + "\"";
		if (Type == WIFICIPHER_NOPASS) {
			config.wepKeys[0] = "";
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == WIFICIPHER_WEP) {
			config.hiddenSSID = true;
			config.wepKeys[0] = "\"" + Password + "\"";
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers
					.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == WIFICIPHER_WPA) {
			Log.v("my_tag", "开始配置WPA热点");
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.status = WifiConfiguration.Status.ENABLED;
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);

			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
			config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

		}
		return config;
	}
	
	/*该方法用于连接已经配置好的WIFI热点
	 * wifiConfiguration 是配置好的WIFI热点
	 * return 是否连接成功*/

	public boolean connectToConfiguredWifi(WifiConfiguration wifiConfiguration) {
		int wcgID = myWifiManager.addNetwork(wifiConfiguration);
		boolean netFlag = myWifiManager.enableNetwork(wcgID, false);
		return netFlag;
	}
}