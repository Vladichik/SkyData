package com.example.skydata;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;

public class DeviceData {

	public static BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context ctxt, Intent intent) {

			//BATTERY STATE
			int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
			String batteryState = String.valueOf(level) + "%";
			int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
			boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;

			//CONNECTIVITY MANAGER
			ConnectivityManager connMgr = (ConnectivityManager) ctxt.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


			MainActivity.batteryStatus.setText(batteryState);
			if(level >= 80 ){
				MainActivity.batIcon.setImageResource(R.drawable.battery_full);
			}
			if(level < 80 && level > 50){
				MainActivity.batIcon.setImageResource(R.drawable.battery_allmost_full);
			}
			if(level < 50 && level > 30){
				MainActivity.batIcon.setImageResource(R.drawable.battery_half);
			}
			if(level < 30 && level > 10){
				MainActivity.batIcon.setImageResource(R.drawable.battery_allmost_empty);
			}
			if(level < 10){
				MainActivity.batIcon.setImageResource(R.drawable.battery_critical);
				MainActivity.batIcon.startAnimation(MainActivity.lowBat);
				MainActivity.batteryStatus.setTextColor(Color.parseColor("#ff0000"));
			}
			if(isCharging){
				MainActivity.batIcon.clearAnimation();
				MainActivity.batIcon.setImageResource(R.drawable.battery_charging);
				MainActivity.batteryStatus.setTextColor(Color.parseColor("#ffffff"));
			}


			if(wifi != null && wifi.isAvailable()){
				MainActivity.connectivityIcon.setImageResource(R.drawable.wifi_icon);
			}else if(mobile != null && mobile.isAvailable()){
				MainActivity.connectivityIcon.setImageResource(R.drawable.gsm_icon);
			}else{
				MainActivity.connectivityIcon.setImageResource(android.R.color.transparent);
			}
		}
	};
}
