package org.lgd.util;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import org.lgd.util.listener.OnLocateFinsihListener;

/**
 * 天气定位工具
 * Created by wukai on 2016/4/2.
 */
public class LocationUtil implements AMapLocationListener {

    /**
     * 定位客户
     */
    private AMapLocationClient locationClient = null;
    /**
     * 定位客户选项
     */
    private AMapLocationClientOption locationOption = null;
    /**
     * 开始定位
     */
    public final static int MSG_LOCATION_START = 0;
    /**
     * 定位完成
     */
    public final static int MSG_LOCATION_FINISH = 1;
    /**
     * 停止定位
     */
    public final static int MSG_LOCATION_STOP = 2;
    /**
     * 监听器
     */
    private OnLocateFinsihListener mListener;

    public final static String KEY_URL = "URL";
    public final static String URL_H5LOCATION = "file:///android_asset/location.html";

    /**
     * 默认定位为省电模式
     *
     * @param context
     */
    public LocationUtil(Context context) {
        locationClient = new AMapLocationClient(context.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        locationClient.setLocationListener(this);
    }

    /**
     * @param context 上下文
     * @param mode    AMapLocationClientOption.AMapLocationMode.Battery_Saving  Hight_Accuracy  Device_Sensors
     */
    public LocationUtil(Context context, AMapLocationClientOption.AMapLocationMode mode) {
        locationClient = new AMapLocationClient(context.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        locationOption.setLocationMode(mode);
        locationClient.setLocationListener(this);
    }

    public AMapLocationClientOption getLocationOption() {
        return locationOption;
    }

    public AMapLocationClient getLocationClient() {
        return locationClient;
    }

    public void setLocationClient(AMapLocationClient locationClient) {
        this.locationClient = locationClient;
    }

    public void setLocationOption(AMapLocationClientOption locationOption) {
        this.locationOption = locationOption;
    }

    private static LocationUtil mInstence;

    public static LocationUtil getInstence(Context context, AMapLocationClientOption.AMapLocationMode mode) {
        if (mInstence == null) {
            synchronized (mInstence) {
                if (mInstence == null) {
                    mInstence = new LocationUtil(context, mode);
                }
            }
        }
        return mInstence;
    }

    /**
     * @param needAddress
     */
    public void startLocation(boolean needAddress) {
        locationOption.setNeedAddress(needAddress);
        locationOption.setInterval(2000);
        locationClient.setLocationOption(locationOption);
        locationClient.startLocation();
    }

    /**
     * 设置定位间隔
     *
     * @param l
     */
    public void setLocationInterval(long l) {
        locationOption.setInterval(l);
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        locationClient.stopLocation();
    }

    /**
     * 定位监听
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        mListener.onLocateFinish(aMapLocation);
    }

    public void setOnLocateFinishListener(OnLocateFinsihListener listener) {
        this.mListener = listener;
    }

    /**
     * 根据定位结果返回定位信息的字符串
     *
     * @param
     * @return
     */
    public synchronized static String getLocationStr(AMapLocation location) {
        if (null == location) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
        if (location.getErrorCode() == 0) {
            sb.append("定位成功" + "\n");
            sb.append("定位类型: " + location.getLocationType() + "\n");
            sb.append("经    度    : " + location.getLongitude() + "\n");
            sb.append("纬    度    : " + location.getLatitude() + "\n");
            sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
            sb.append("提供者    : " + location.getProvider() + "\n");

            if (location.getProvider().equalsIgnoreCase(
                    android.location.LocationManager.GPS_PROVIDER)) {
                // 以下信息只有提供者是GPS时才会有
                sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                sb.append("角    度    : " + location.getBearing() + "\n");
                // 获取当前提供定位服务的卫星个数
                sb.append("星    数    : "
                        + location.getSatellites() + "\n");
            } else {
                // 提供者是GPS时是没有以下信息的
                sb.append("国    家    : " + location.getCountry() + "\n");
                sb.append("省            : " + location.getProvince() + "\n");
                sb.append("市            : " + location.getCity() + "\n");
                sb.append("城市编码 : " + location.getCityCode() + "\n");
                sb.append("区            : " + location.getDistrict() + "\n");
                sb.append("区域 码   : " + location.getAdCode() + "\n");
                sb.append("地    址    : " + location.getAddress() + "\n");
                sb.append("兴趣点    : " + location.getPoiName() + "\n");
            }
        } else {
            //定位失败
            sb.append("定位失败" + "\n");
            sb.append("错误码:" + location.getErrorCode() + "\n");
            sb.append("错误信息:" + location.getErrorInfo() + "\n");
            sb.append("错误描述:" + location.getLocationDetail() + "\n");
        }
        return sb.toString();
    }
}
