package com.icer.snow;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

public class MyFragment extends Fragment {
	SnowSurfaceView view;


	@Override
	public void onPause() {
		Log.i("SnowFly", "-->onPause()");
		super.onPause();
		// thread.stop();
	}

	@Override
	public void onResume() {
		Log.i("SnowFly", "-->onResume()");
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("SnowFly", "-->onCreateView()");
		view = new SnowSurfaceView(getActivity());
		return view;
	}

}
