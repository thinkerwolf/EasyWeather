package org.lgd.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

/**
 * 自定义手势滑动的Callback
 * Created by wukai on 2016/4/23.
 */
public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {


    private int position;

    public interface OnItemTouchHelperListener {
        void onItemMove(int form, int to);

        void onItemSwipe(int position);
    }

    private OnItemTouchHelperListener mListener;

    private CitiesListAdapter adapter;

    public ItemTouchHelperCallback(CitiesListAdapter adapter) {
        this.adapter = adapter;
    }

    public ItemTouchHelperCallback() {
        //this.adapter = adapter;
    }

    public void setOnItemTouchHelperListener(OnItemTouchHelperListener listener) {
        this.mListener = listener;
    }


    /**
     * @param recyclerView : 当前的RecyclerView视图
     * @param viewHolder   : 当前的viewHolder
     * @return : 手势方向
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.LEFT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /**
     * 滑动排序需要操作数据库,对两个滑动的目标进行排序
     *
     * @param recyclerView :需要拖动的RecycleView
     * @param viewHolder   ：当前的ViewHolder
     * @param target       ：目标ViewHolder
     * @return
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int from = viewHolder.getAdapterPosition();
        int to = target.getAdapterPosition();
        if (to == 0 || from == 0) {
            return false;
        }
        mListener.onItemMove(from, to);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        this.position = position;
        mListener.onItemSwipe(position);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

}
