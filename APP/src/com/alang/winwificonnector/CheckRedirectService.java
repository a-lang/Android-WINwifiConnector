package com.alang.winwificonnector;

import com.alang.winwificonnector.util.WifiTools;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

public class CheckRedirectService extends IntentService {

	public CheckRedirectService(String name) {
		super(name);
	}

	public CheckRedirectService() {
		super("CheckRedirectService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
        if (intent.getAction().equals(getString(R.string.action_check_redirect))) {
       		if (WifiTools.isWifiConnected(this)){
       			URLRedirectChecker checker = new URLRedirectChecker (Constants.TAG, getApplicationContext());
       			checker.setSaveLogFile (null);
       			checker.checkHttpConnection ();
        	}
        }
	}
	
	public static void startService (Context context) {
		Intent actionIntent = new Intent(context, CheckRedirectService.class);
		actionIntent.setAction (context.getString(R.string.action_check_redirect));
		context.startService (actionIntent);
	}

}
