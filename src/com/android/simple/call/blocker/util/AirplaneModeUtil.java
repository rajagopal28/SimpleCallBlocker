package com.android.simple.call.blocker.util;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

public class AirplaneModeUtil {
	
	public static int AIRPLANE_MODE_OFF = 0;
	public static int AIRPLANE_MODE_ON = 1;
	public static int STATE_IN_SERVICE =  0;
    public static boolean isAirplaneModeEnabled(Context context) {
    	boolean isEnabled = Settings.System.getInt(
			      context.getContentResolver(), 
			      Settings.System.AIRPLANE_MODE_ON, 0) == AIRPLANE_MODE_ON;
		return isEnabled;
    }
    
    public static void toggleAirplaneMode(boolean currentState, Context context) {
    	// toggle airplane mode
    	Settings.System.putInt(
    		context.getContentResolver(),
    	      Settings.System.AIRPLANE_MODE_ON, currentState ? AIRPLANE_MODE_OFF : AIRPLANE_MODE_ON);

    	// Post an intent to reload
    	Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
    	intent.putExtra("state", !currentState);
    	context.sendBroadcast(intent);
    }
}


