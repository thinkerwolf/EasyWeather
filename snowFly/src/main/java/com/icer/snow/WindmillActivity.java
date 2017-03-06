package com.icer.snow;


import android.app.Activity;
import android.os.Bundle;

public class WindmillActivity extends Activity {
	LoadWeatherView view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		view=new LoadWeatherView(this);
		setContentView(view);
	}

}
