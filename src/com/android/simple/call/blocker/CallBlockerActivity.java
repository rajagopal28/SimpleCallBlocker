package com.android.simple.call.blocker;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.simple.call.blocker.constants.CallBlockerConstants.BLOCK_TYPE;
import com.android.simple.call.blocker.dao.CallBlockerDAO;
import com.android.simple.call.blocker.util.CallLogsUtil;

public class CallBlockerActivity extends Activity {
	private List<String> callLogList = null;
	private List<String> removeList = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_block_unwantedcalls);
		if (callLogList == null) {
			callLogList = CallLogsUtil.getAllNumbers(this);
		}
		readAndDisplayBlockList();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.select_dialog_item, callLogList);
		// read the contacts and create a list
		AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
		actv.setThreshold(1);// will start working from first character
		actv.setAdapter(adapter);// setting the adapter data into the
									// AutoCompleteTextView
		actv.setTextColor(Color.BLUE);

		Button addButton = (Button) findViewById(R.id.addButton);
		addButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String numberToAdd = "";
				String prefixBlockNumber = "";
				EditText plainNumber = (EditText) findViewById(R.id.manualNumber);
				AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
				if (plainNumber.getText().toString().trim().length() > 0) {
					prefixBlockNumber = plainNumber.getText().toString().trim();
				} else if (actv.getText().toString().trim().length() > 0) {
					numberToAdd = actv.getText().toString().trim();
				}
				CallBlockerDAO dao = new CallBlockerDAO(
						getApplicationContext());
				List<String> allBlockedList = dao.getAllBlockedNumbers();
				if(prefixBlockNumber.length() > 0) {
					if(!allBlockedList.contains(prefixBlockNumber)) {
						dao.addToBlockedList(prefixBlockNumber, BLOCK_TYPE.PREFIX_REJECT);
						readAndDisplayBlockList();
					}
					
				} else if (numberToAdd.length() > 0) {	
					if(!allBlockedList.contains(numberToAdd)) {
						dao.addToBlockedList(CallLogsUtil.getStandardMobileFormat(numberToAdd), BLOCK_TYPE.REJECT);
						readAndDisplayBlockList();
					}
				} else {
					Toast.makeText(getBaseContext(), "Fields Empty!!!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		Button removeButton = (Button) findViewById(R.id.removeButton);
		removeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				CallBlockerDAO dao = new CallBlockerDAO(getApplicationContext());
				dao.removeSelectedNumbers(removeList);
				readAndDisplayBlockList();
			}
		});
	}

	private void readAndDisplayBlockList() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayout2);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.removeAllViews();
		TextView showText = new TextView(this);
		showText.setText("List of blocked Numbers");
		ll.addView(showText);
		CallBlockerDAO dao = new CallBlockerDAO(getApplicationContext());
		List<String> blockedList = dao.getAllBlockedNumbers();
		for (String blockedItem : blockedList) {

			CheckBox ch = new CheckBox(this);
			ch.setText(blockedItem);
			ch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					String entireText = buttonView.getText().toString();
					if(isChecked) {
						removeList.add(entireText);
					} else {
						removeList.remove(entireText);
					}
					
				}
			});
			ll.addView(ch);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.block_unwantedcalls, menu);
		return true;
	}

}
