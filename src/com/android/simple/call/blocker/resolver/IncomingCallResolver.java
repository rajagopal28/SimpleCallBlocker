package com.android.simple.call.blocker.resolver;

import java.lang.reflect.Method;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.android.simple.call.blocker.constants.CallBlockerConstants;
import com.android.simple.call.blocker.constants.CallBlockerConstants.BLOCK_TYPE;
import com.android.simple.call.blocker.dao.CallBlockerDAO;
import com.android.simple.call.blocker.util.AirplaneModeUtil;
import com.android.simple.call.blocker.util.CallLogsUtil;

public class IncomingCallResolver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("IncomingCallResolver", "OnRecieve");
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		try {
			Class<?> c = Class.forName(tm.getClass().getName());
			Method m = c.getDeclaredMethod("getITelephony");
			m.setAccessible(true);
			ITelephony telephonyService = (ITelephony) m.invoke(tm);
			Bundle bundle = intent.getExtras();
			String phoneNumber = bundle.getString("incoming_number");
			String state = bundle.getString("state");

			CallBlockerDAO dao = new CallBlockerDAO(context);						
			List<String> prefixBlockedList = dao.getBlockedCallsofType(BLOCK_TYPE.PREFIX_REJECT);
			Log.i("IncomingCallResolver", "Number : " + phoneNumber + " State : " + state);
			String formattedNumber = CallLogsUtil.getStandardMobileFormat(phoneNumber);
			if(phoneNumber != null && 
					CallBlockerConstants.PHONE_STATE_RINGING.equalsIgnoreCase(state)) {
				for(String prefixBlock : prefixBlockedList) {
					if(phoneNumber.startsWith(prefixBlock) || 
							formattedNumber.startsWith(prefixBlock)) {									
						Log.i("IncomingCallResolver", "PREFIX HANG UP : " + phoneNumber);
						invokeEndCall(telephonyService, context, phoneNumber);						
						break;					
					}
				}
			}
			if(phoneNumber != null && 
					CallBlockerConstants.PHONE_STATE_RINGING.equalsIgnoreCase(state)) {
				List<String> blockedList = dao.getBlockedCallsofType(BLOCK_TYPE.REJECT);
				if (blockedList.contains(phoneNumber) || 
						blockedList.contains(formattedNumber)) {	
					invokeEndCall(telephonyService, context, phoneNumber);					
					Log.i("IncomingCallResolver", "HANG UP : " + phoneNumber);
				}
			}
		

		} catch (Exception e) {
			Toast.makeText(context, e.getMessage(),
				Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}

	}
	
	/** This reusable method is use to end call in one way or the other
	 * 
	 * @param telephonyService
	 * @param context
	 * @param phoneNumber
	 * @throws InterruptedException
	 * @throws RemoteException 
	 */
	private void invokeEndCall(ITelephony telephonyService, Context context, String phoneNumber) throws InterruptedException, RemoteException {
		String model = Build.MODEL;
		if(!model.equalsIgnoreCase("LG-P500")) {
			telephonyService.endCall();
		} else {
			// read the airplane mode setting
			boolean isEnabled = AirplaneModeUtil.isAirplaneModeEnabled(context);
			if (!isEnabled) {
				// Switch on first
				AirplaneModeUtil.toggleAirplaneMode(isEnabled, context);				
				// toggle off in the next step
				AirplaneModeUtil.toggleAirplaneMode(!isEnabled, context);
				ServiceState ser = new ServiceState();
                ser.setState(AirplaneModeUtil.STATE_IN_SERVICE);
			}							
		}
		Thread.sleep(1000);
		CallLogsUtil.deleteLogsOfNumber(context.getContentResolver(), phoneNumber);		
	}
	

}
