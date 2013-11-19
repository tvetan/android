package com.cai.workhourstracker.helper;

import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;

public class EditTextUtils {
	public static String getEditTextValueById(Activity act, int resId) {
		TextView te =  (TextView) act.findViewById(resId);
		
		if (te != null) {
			return te.getText().toString();
		} else {
			return "";
		}
	}
	
	public static void setEditTextValueById(Activity act, int resId, String str) {
		TextView te =  (TextView) act.findViewById(resId);
		
		if (te != null) {
			te.setText(str);
		}
	}

	public static void getEditTextValue(EditText editText){
		
	}
}
