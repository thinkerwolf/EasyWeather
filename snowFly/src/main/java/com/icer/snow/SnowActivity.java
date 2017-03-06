package com.icer.snow;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class SnowActivity extends Activity {
	SnowSurfaceView snowSurfaceView;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i("icer", "-->onCreate(");
		snowSurfaceView=new SnowSurfaceView(this);
		setContentView(snowSurfaceView);
	}

	@Override
	public void closeContextMenu() {
		// TODO Auto-generated method stub
		super.closeContextMenu();
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("icer", "-->onDestroy(");
		//snowSurfaceView.
	
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("icer", "-->onPause(");
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("icer", "-->onResume(");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i("icer", "-->onStart(");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("icer", "-->onStop(");
	}
	
	

}
