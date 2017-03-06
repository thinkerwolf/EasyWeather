package org.lgd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.lgd.entry.AddCityBean;
import org.lgd.entry.CityEntry;

import java.util.List;

import lgd.org.R;

/**
 * 添加城市顶栏RecyclerView的Adapter
 * Created by wukai on 2016/5/7.
 */
public class TopCitiesAdapter extends RecyclerView.Adapter<TopCitiesAdapter.MyViewHolder> {

    private List<AddCityBean> hotCitiesList;
    private LayoutInflater mInflater;
    private Context context;
    private String choosedStr;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClick(View v, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public TopCitiesAdapter(Context context, List<AddCityBean> hotCitiesList, List<CityEntry> choosedCitiesList) {
        this.hotCitiesList = hotCitiesList;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        if (choosedCitiesList != null) {
            this.choosedStr = choosedCitiesList.toString();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.topview_recycler_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (holder != null) {
            String hotName = hotCitiesList.get(position).getName();
            holder.textView.setText(hotName);
            if (choosedStr != null && choosedStr.contains(hotName)) {
//                holder.textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_hot_city_choosed));
                holder.itemView.setSelected(true);
            } else {
//                holder.textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_hot_city_normal));
                holder.itemView.setSelected(false);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onClick(v, position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return hotCitiesList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.id_top_hot_item_cityname);
        }
    }
}
