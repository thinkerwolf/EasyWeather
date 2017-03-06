package org.lgd.entry;


import org.lgd.activity.MainActivity;

/**
 * <p>天气被观察者</p>
 * Created by wukai on 2016/6/4.
 */
public interface WeatherCodeWatched {

    void addWatcher(MainActivity watcher);

    void removeWatcher(MainActivity watcher);

    void notifyWatcherUpdate(int code, int position);
}
