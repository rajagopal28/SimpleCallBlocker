package com.android.simple.call.blocker.util;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.CallLog;

public class CallLogsUtil {

	public static List<String> getAllNumbers(Activity callingActivity) {
		List<String> allNumbers = new ArrayList<String>();
		Cursor managedCursor = callingActivity.managedQuery(
				CallLog.Calls.CONTENT_URI, null, null, null, null);
		int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
		int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
		int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
		int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
		StringBuilder sb = new StringBuilder();
		sb.append("Call Details :");
		while (managedCursor.moveToNext()) {
			String phNumber = managedCursor.getString(number);
			String callType = managedCursor.getString(type);
			String callDate = managedCursor.getString(date);
			Date callDayTime = new Date(Long.valueOf(callDate));
			String callDuration = managedCursor.getString(duration);
			String dir = null;
			if(!allNumbers.contains(phNumber)) {
				allNumbers.add(phNumber);
			}
			
			int dircode = Integer.parseInt(callType);
			switch (dircode) {
			case CallLog.Calls.OUTGOING_TYPE:
				dir = "OUTGOING";
				break;

			case CallLog.Calls.INCOMING_TYPE:
				dir = "INCOMING";
				break;

			case CallLog.Calls.MISSED_TYPE:
				dir = "MISSED";
				break;
			}
			sb.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- "
					+ dir + " \nCall Date:--- " + callDayTime
					+ " \nCall duration in sec :--- " + callDuration);
			sb.append("\n----------------------------------");
			// Log.i("getAllNumbers", sb.toString());
		}
		managedCursor.close();

		return allNumbers;
	}
	public static boolean deleteLogsOfNumber(ContentResolver contentResolver, String numberToDelete) {
		boolean isSuccess = false;
		isSuccess = contentResolver.delete(CallLog.Calls.CONTENT_URI, CallLog.Calls.NUMBER +"= ?", new String[]{numberToDelete})>0;
		return isSuccess;
	}
	
	public static String getStandardMobileFormat(String actualNumber) {
		String formattedNumber = null;
		if(actualNumber != null) {
			formattedNumber = actualNumber.replace("-", "");
			if(!formattedNumber.startsWith("+91")) {
				if(formattedNumber.startsWith("+1")) {
					formattedNumber.replace("+", "+91");
				} else if(formattedNumber.startsWith("0")){
					formattedNumber = "+91" + formattedNumber.substring(1);// ignore 0 and start with +91
	 			} else {
		 			formattedNumber = "+91" + formattedNumber;
		 		}
			}
		}
		
		return formattedNumber;
	}
}
