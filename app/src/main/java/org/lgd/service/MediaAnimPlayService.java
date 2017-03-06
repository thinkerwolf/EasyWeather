package org.lgd.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * 后台天气播放Service
 * Created by wukai on 2016/5/21.
 */
public class MediaAnimPlayService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
