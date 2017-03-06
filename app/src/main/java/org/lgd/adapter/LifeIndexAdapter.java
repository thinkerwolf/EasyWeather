package org.lgd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import org.lgd.entry.LifeInfoData;

import lgd.org.R;

/**
 * RecycleView的Adapter
 * Created by Bruce Wu on 2016/4/15.
 */
public class LifeIndexAdapter extends RecyclerView.Adapter<LifeIndexAdapter.MyViewHolder> {
    /**
     * 生活指数数据集合类
     */
    private LifeInfoData data;
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 布局加载器
     */
    private LayoutInflater mInflater;

    public interface OnItemClickListener {
        void onItemClickListener(View v, int position);

        void onLongItemClickListener(View v, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public LifeIndexAdapter(Context context, LifeInfoData data) {
        this.data = data;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(mInflater.inflate(R.layout.fragment_main_life_recycle_modual, parent, false));
        return holder;
    }

    @Override
    public int getItemCount() {
        return data.getLifeIndexNames().size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.imageView.setImageResource(data.getLifeIndexImgs().get(position));
        holder.textView1.setText(data.getLifeIndexNames().get(position));
        holder.textView2.setText(data.getLifeIndexRes().get(position));

        if (holder.itemView != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClickListener(holder.itemView, position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongItemClickListener(holder.itemView, position);
                    return false;
                }
            });
        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView1;
        TextView textView2;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.life_index_img);
            textView1 = (TextView) itemView.findViewById(R.id.life_index_name);
            textView2 = (TextView) itemView.findViewById(R.id.life_index_res);
        }

    }
}
