package com.icer.snow;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class WindyActivity extends Activity {
	private FrameLayout flLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_layout);
		flLayout=(FrameLayout) this.findViewById(R.id.fllayout);
		
		
		 DynamicWeatherCloudyView view1=new DynamicWeatherCloudyView(this,
				 R.drawable.yjjc_h_a2, -150, 40,20);
				 DynamicWeatherCloudyView view4=new DynamicWeatherCloudyView(this,
				 R.drawable.yjjc_h_a5, 0, 60,30);
				 DynamicWeatherCloudyView view2=new DynamicWeatherCloudyView(this,
				 R.drawable.yjjc_h_a3, 280, 80,50);
				 DynamicWeatherCloudyView view3=new DynamicWeatherCloudyView(this,
				 R.drawable.yjjc_h_a4, 140, 130,40);
				
				 flLayout.addView(view1);
				 flLayout.addView(view2);
				 flLayout.addView(view3);
				 flLayout.addView(view4);
				 view1.move();
				 view2.move();
				 view3.move();
				 view4.move();
	}

}
