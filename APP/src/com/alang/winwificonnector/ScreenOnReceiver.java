package com.alang.winwificonnector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class ScreenOnReceiver extends BroadcastReceiver {
	
	static ScreenOnReceiver instance = null;
	
	private ScreenOnReceiver() {
		super();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
			try {
				// we may get onReceive after we already unregistered ourselves!
				context.unregisterReceiver (this); // we only do one-off thing
			} catch(IllegalArgumentException e) {}
			CheckRedirectService.startService (context);
			// will only get here when notification received in background,
			// and receiver got disabled as the result. 
			// So re-enable it!
			WifiBroadcastReceiver.setEnabled (context, true);			
        }
	}
	
	public static synchronized ScreenOnReceiver getInstance() {
		if (instance == null)
			instance = new ScreenOnReceiver();
		return instance;
	}
	
	public static boolean register(Context context) {
		if (context != null) {
			IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
			try {
				context.getApplicationContext().registerReceiver(getInstance(), intentFilter);
				return true;
			} catch(IllegalArgumentException e) 
			{	// we are already registered
			}
		}
		return false;
	}

}