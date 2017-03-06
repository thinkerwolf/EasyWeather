package org.lgd.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.lgd.entry.AddCityBean;
import org.lgd.entry.CityEntry;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lgd.org.R;

/**
 * 添加城市的Adapter
 * Created by wukai on 2016/5/7.
 */
public class CitiesAddAdapter extends BaseAdapter {
    /**
     * 所有的Cities
     */
    private List<AddCityBean> cityBeanList;
    /**
     * 所有的已经选择的城市
     */
    private List<CityEntry> choosedCity;
    private Context mContext;
    private LayoutInflater mInflater;

    private String[] cityChar = new String[]{"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z"};

    /**
     * 用来存储Letter出现位置的position
     */
    private HashMap<String, Integer> letterPosMap = new HashMap<>();

    public HashMap<String, Integer> getLetterPosMap() {
        return letterPosMap;
    }

    public CitiesAddAdapter(Context mContext, List<CityEntry> choosedCity, final List<AddCityBean> cityBeanList) {
        this.choosedCity = choosedCity;
        this.cityBeanList = cityBeanList;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < cityBeanList.size(); i++) {
                    if (i == 0) {
                        letterPosMap.put(cityChar[i], i);
                    } else {
                        if (i < cityBeanList.size() - 1) {
                            char currentChar = cityBeanList.get(i).getPinyin().charAt(0);
                            char perviousChar = cityBeanList.get(i - 1).getPinyin().charAt(0);
                            char nestChar = cityBeanList.get(i + 1).getPinyin().charAt(0);
                            if (currentChar != perviousChar && currentChar == nestChar) {
                                String l = cityChar[cityBeanList.get(i).getPos() - 1];
                                letterPosMap.put(l, i);
                            }
                        }
                    }
                }
            }
        }.start();
    }

    @Override
    public int getCount() {
        return cityBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return cityBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        if (convertView == null) {
            holder = new MyViewHolder();
            convertView = mInflater.inflate(R.layout.fragment_city_add_item_all, parent, false);
            holder.textViewLetter = (TextView) convertView.findViewById(R.id.id_city_add_item_currentLetter);
            holder.textCity = (TextView) convertView.findViewById(R.id.id_city_add_item_cityname);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        String name = cityBeanList.get(position).getName();
        if (choosedCity != null) {
            String cities = choosedCity.toString();
            if (cities.contains(name)) {
                holder.textCity.setTextColor(Color.parseColor("#FF4AB0E2"));
            } else {
                holder.textCity.setTextColor(Color.parseColor("#FFDEDEDE"));
            }
        }
        holder.textCity.setText(name);
        setLetter(holder.textViewLetter, position);
        return convertView;
    }

    private void setLetter(TextView textView, int position) {
        if (position == 0) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(cityChar[position]);
        } else if (position == cityBeanList.size() - 1) {
            textView.setVisibility(View.GONE);
        } else {
            char currentChar = cityBeanList.get(position).getPinyin().charAt(0);
            char perviousChar = cityBeanList.get(position - 1).getPinyin().charAt(0);
            char nestChar = cityBeanList.get(position + 1).getPinyin().charAt(0);
            if (currentChar != perviousChar && currentChar == nestChar) {
                String l = cityChar[cityBeanList.get(position).getPos() - 1];
                textView.setVisibility(View.VISIBLE);
                textView.setText(l);
            } else {
                textView.setVisibility(View.GONE);
            }
        }
    }

    class MyViewHolder {
        TextView textViewLetter;
        TextView textCity;
    }
}
