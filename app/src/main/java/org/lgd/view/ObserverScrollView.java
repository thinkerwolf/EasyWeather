package org.lgd.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ScrollView;


/**
 * 自定义的ScrollView 带有滑动监控功能  最新的只有在API23上面才有
 * 为了兼容API14以上的设备。。所以才自定义this ScrollView
 * Created by wukai on 2016/5/21.
 */
public class ObserverScrollView extends ScrollView {


    private OnObScrollChangeListener listener;

    public interface OnObScrollChangeListener {
        void onScrollChange(int l, int t, int oldl, int oldt);
    }


    public ObserverScrollView(Context context) {
        super(context);
    }

    public ObserverScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObserverScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (this.listener != null) {
            listener.onScrollChange(l, t, oldl, oldt);
        }
    }

    /**
     * @param listener
     */
    public void setOnScrollChangeListener(@NonNull OnObScrollChangeListener listener) {
        this.listener = listener;
    }
}
