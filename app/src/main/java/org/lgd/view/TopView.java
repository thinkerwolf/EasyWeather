package org.lgd.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.lgd.activity.MyAppllication;
import org.lgd.adapter.TopCitiesAdapter;
import org.lgd.entry.AddCityBean;
import org.lgd.entry.CityEntry;
import org.lgd.entry.Contacts;
import org.lgd.util.DBUtils;
import org.lgd.util.FileUtils;

import java.util.List;

import lgd.org.R;

/**
 * 所有城市列表顶栏的热门城市选择
 * Created by wukai on 2016/5/7.
 */
public class TopView extends FrameLayout {

    //首页城市展示
    private RecyclerView recyclerView;
    //首页热门城市Adapter
    private TopCitiesAdapter adapter;
    //所有热门城市集合
    private List<AddCityBean> hotCities;
    //所有已选城市集合
    private List<CityEntry> choCities;

    public TopView(Context context) {
        this(context, null);
    }

    public TopView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.add_city_list_topview, this, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.id_topview_recycleview);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        initRecycle(context);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        this.addView(view, lp);
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }


    private void initRecycle(final Context context) {
        choCities = MyAppllication.cities;
        String hotJson = FileUtils.fromFile(context, R.raw.hot_cities_data);
        Gson gson = new Gson();
        hotCities = gson.fromJson(hotJson, new TypeToken<List<AddCityBean>>() {
        }.getType());
        adapter = new TopCitiesAdapter(context, hotCities, choCities);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new TopCitiesAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int pos) {
                //TODO 回到主界面。。。表明其为增加一个城市。。。并显示选择的城市天气
                String choStrs = choCities.toString();
                AddCityBean bean = hotCities.get(pos);
                String name = bean.getName();
                Activity activity = (Activity) context;
                if (choStrs.contains(name)) {
                    activity.finish();
                } else {
                    CityEntry entry = new CityEntry();
                    entry.setCityName(bean.getName());
                    entry.setSortId(0);
                    entry.setLocationOrNot(0);
                    MyAppllication.cities.add(entry);
                    MyAppllication.weatherDatas.add(null);
                    Intent intent = new Intent();
                    activity.setResult(Contacts.ADD_CITY, intent);
                    activity.finish();
                }
            }
        });
    }


}
