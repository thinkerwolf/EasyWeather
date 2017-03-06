package org.lgd.util.listener;

import com.amap.api.location.AMapLocation;

/**
 * 定位结束监听
 * Created by Bruce Wu on 2016/4/2.
 */
public interface OnLocateFinsihListener {
    public void onLocateFinish(AMapLocation location);
}
