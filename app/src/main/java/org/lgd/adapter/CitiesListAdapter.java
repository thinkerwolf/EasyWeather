package org.lgd.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;
import org.lgd.activity.MyAppllication;
import org.lgd.entry.CityEntry;
import org.lgd.entry.Contacts;
import org.lgd.entry.RealTime;
import org.lgd.entry.Weather;
import org.lgd.util.DBUtils;
import org.lgd.util.NumberUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lgd.org.R;

/**
 * Created by wukai on 2016/4/22.
 * alter by wukai on 2016/5/15.  repair the bugs of swipe delete
 */
public class CitiesListAdapter extends RecyclerView.Adapter<CitiesListAdapter.MyViewHolder> implements ItemTouchHelperCallback.OnItemTouchHelperListener {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<CityEntry> cities;
    private List<String> mResults;
    private Gson mGson;

    private ItemTouchHelperCallback.OnItemTouchHelperListener touchHelperListener;

    private List<View> views;

    public interface OnItemClickListener {
        void onClick(View v, int position);

        void onLongClick(View v, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public CitiesListAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mGson = new Gson();
        views = new ArrayList<>();
        this.cities = MyAppllication.cities;
        init();
    }

    public CitiesListAdapter(Context context, List<String> jsons) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mGson = new Gson();
        views = new ArrayList<>();
        this.cities = MyAppllication.cities;
        this.mResults = jsons;
        init();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    private void init() {
        //Log.i("CitiesAdapter-initMResults:", mResults.toString());
        for (int i = 0; i < mResults.size(); i++) {
            CityEntry city = cities.get(i);
            if (TextUtils.isEmpty(mResults.get(i))) {
                final int in = i;
                StringRequest request = new StringRequest(String.format(Contacts.WEATHER_INFO_URL, city.getCityName()),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                //请求成功,通知Adapter更新视图
                                mResults.add(in, s);
                                MyAppllication.saveData(s, in);   //整体json数据更新
                                CitiesListAdapter.this.notifyDataSetChanged();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
                MyAppllication.getRequestQueue(mContext).add(request);
            }
        }


    }


    @Override
    public void onItemMove(int from, int to) {
        //todo 调换顺序 :以后需要优化
        /**
         * 先将就着，暂时没有想到好的解决办法
         * alter by wukai on 20160511 修改城市排序逻辑
         * 数据库不太方便解决,数据库还没更新完，前台页面就已经转换完成
         */
        //2.先调换数据的顺序,通知页面刷新
        Collections.swap(mResults, from, to);
        //Collections.swap(MyAppllication.weatherDatas, from, to);
        Collections.swap(MyAppllication.cities, from, to);
        //resetItemClickListener(from, to);
        this.notifyItemMoved(from, to);
    }

    /**
     * 功能:滑动删除一个城市
     * add by wukai on 20160512
     *
     * @param position
     */
    @Override
    public void onItemSwipe(int position) {
        Log.i("TAG", "onItemSwipe Postion:" + position);
        //alter by wukai on 20160512 解决城市管理滑动删除问题
        //alter by wukai on 20160531 滑动删除bug
        if (position != 0) {
            mResults.remove(position);
            notifyItemRemoved(position);
            MyAppllication.cities.remove(position);
//            Log.i("MyAppllication.weatherDatas:", MyAppllication.weatherDatas.toString());
//            Log.i("MyAppllication.cities:", MyAppllication.cities.toString());
        } else {
            notifyItemChanged(0);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cities_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        CityEntry entry = cities.get(position);
        views.add(holder.itemView);
        if (position != 0) {
            holder.setIsRecyclable(false);
        } else {
            holder.itemView.setClickable(true);
        }
        if (holder.itemView != null && mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(v, holder.getAdapterPosition());
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(v, holder.getAdapterPosition());
                    return false;
                }
            });
        }

        holder.textCity.setText(entry.getCityName());
        if (entry.getLocationOrNot() != 1) {
            holder.imageLocation.setVisibility(View.GONE);
        }
        holder.imageDelete.setVisibility(View.GONE);
        if (mResults.size() <= 0 || position >= mResults.size()) {
            return;
        }
        String res = mResults.get(position);
        //绑定视图数据
        setDatas(holder, res);
    }

    /**
     * 为每一条数据设置数据
     *
     * @param holder
     * @param res
     */
    private void setDatas(MyViewHolder holder, String res) {
        try {
            if (!TextUtils.isEmpty(res)) {
                JSONObject jsonObject = new JSONObject(res);
                if (0 == jsonObject.optInt(Contacts.ERROR_CODE)) {
                    //获取到了正确数据
                    JSONObject jsonObject1 = jsonObject.optJSONObject(Contacts.RESULT);
                    JSONObject jsonObject2 = jsonObject1.optJSONObject(Contacts.REALTIME);
                    //天气信息概括
                    RealTime realTime = mGson.fromJson(jsonObject2.toString(), RealTime.class);
                    holder.textTemp.setText(realTime.getWeather().getTemperature() + "℃");
                    holder.textWeather.setText(realTime.getWeather().getInfo());

                    List<Weather> weathers = mGson.fromJson(jsonObject1.optJSONArray(Contacts.WEATHER).toString(),
                            new TypeToken<List<Weather>>() {
                            }.getType());
                    Weather.InfoBean infoBean = weathers.get(0).getInfo();
                    holder.textWeather.setText(realTime.getWeather().getInfo() + " " +
                            infoBean.getDay().get(NumberUtils.WEATHER_TEMP_INDEX) + "°~" +
                            infoBean.getNight().get(NumberUtils.WEATHER_TEMP_INDEX) + "°C");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageLocation;
        TextView textCity;
        TextView textWeather;
        TextView textTemp;
        ImageButton imageDelete;
        LinearLayout linearBg;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageLocation = (ImageView) itemView.findViewById(R.id.cities_list_item_location);
            textCity = (TextView) itemView.findViewById(R.id.cities_list_item_cityName);
            textWeather = (TextView) itemView.findViewById(R.id.cities_list_item_weather);
            textTemp = (TextView) itemView.findViewById(R.id.cities_list_item_temp);
            imageDelete = (ImageButton) itemView.findViewById(R.id.cities_list_item_delete);
            linearBg = (LinearLayout) itemView.findViewById(R.id.cities_list_bg);
        }
    }
}
