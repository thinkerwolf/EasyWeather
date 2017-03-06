package com.icer.snow;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.R.integer;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTheme(android.R.style.Theme_Light_NoTitleBar);
		List<String> lists = new ArrayList<String>();
		lists.add("下雪");
		lists.add("飘云");
		lists.add("风车");
		lists.add("温度趋势图");
		ListAdapter adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, lists);
		setListAdapter(adapter);

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Intent _intent = null;
		Context context = MainActivity.this;
		switch (position) {
		case 0:
			_intent = new Intent(context, SnowActivity.class);
			break;
		case 1:
			_intent = new Intent(context, WindyActivity.class);
			break;
		case 2:
			_intent = new Intent(context, WindmillActivity.class);
			break;
		case 3:
			_intent = new Intent(context, TrendActivity.class);
			break;

		default:
			break;
		}

		startActivity(_intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
