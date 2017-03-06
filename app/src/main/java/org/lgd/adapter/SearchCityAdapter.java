package org.lgd.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.lgd.activity.MyAppllication;
import org.lgd.entry.AddCityBean;
import org.lgd.entry.CityEntry;

import java.util.ArrayList;
import java.util.List;

import lgd.org.R;

/**
 * Created by wukai on 2016/6/12.
 */
public class SearchCityAdapter extends BaseAdapter implements Filterable {

    private List<AddCityBean> cityBeanList;
    private LayoutInflater mInflater;

    private List<AddCityBean> allList = new ArrayList<>();
    private List<CityEntry> choosedCity;

    public SearchCityAdapter(Context context, List<AddCityBean> cityBeanList, List<CityEntry> choosedCity) {
        this.cityBeanList = new ArrayList<>();
        this.allList = cityBeanList;
        this.choosedCity = choosedCity;
        this.mInflater = LayoutInflater.from(context);
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
        holder.textViewLetter.setVisibility(View.GONE);
        return convertView;
    }

    public List<AddCityBean> getCityBeanList() {
        return cityBeanList;
    }

    @Override
    public Filter getFilter() {
        return new CityFilter();
    }

    class MyViewHolder {
        TextView textViewLetter;
        TextView textCity;
    }

    /**
     * 自定义过滤器
     */
    public class CityFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String input = constraint.toString();
            FilterResults results = new FilterResults();
            List<AddCityBean> list = new ArrayList<>();
            if (input != null) {
                for (AddCityBean bean : allList) {
                    if (bean.getName().toLowerCase().contains(input.toLowerCase()) ||
                            bean.getPinyin().toLowerCase().contains(input.toLowerCase())) {
                        list.add(bean);
                    }
                }
            }
            results.count = list.size();
            results.values = list;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            cityBeanList = (List<AddCityBean>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
