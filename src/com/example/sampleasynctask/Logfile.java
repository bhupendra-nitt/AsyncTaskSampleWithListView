package com.example.sampleasynctask;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Logfile {

	/**
	 * @param args
	 */
	public static void main(String msg) {
		// TODO Auto-generated method stub
Log.d("AsynctaskSample", msg);
	}
	
	public static void s1(Context context, String message)
	{
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		
	}

}
