package org.lgd.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.lgd.entry.Contacts;

/**
 * 根据天气代码自动切换播放源
 * 播放类
 * 需要转换天气的情况: 1.城市切换的时候   2.时间改变的时候
 * 在主Activity的
 * 对当前Activity的状体进行监听
 * Created by wukai on 2016/5/19.
 */
public class WeatAnimChangeUtils implements MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    /**
     * 播放View
     */
    private SurfaceView surfaceView;
    /**
     * 多媒体播放类
     */
    private MediaPlayer mediaPlayer;
    /**
     * 天气code
     */
    private int code = -1;
    /**
     * 白天或者是夜晚
     */
    private boolean dayOrNight = true;
    /**
     * 上下文
     */
    private Context context;
    /**
     * 判断是否是从别的界面跳转回来   true：是
     */
    private boolean justBack = false;
    /**
     * 是否进行调试模式
     */
    public boolean Debug = true;
    /**
     * 用来存放界面恢复时存放的code
     */
    private int tempCode;

    public WeatAnimChangeUtils(Context context, SurfaceView surfaceView) {
        this(context, surfaceView, -1);
    }

    public WeatAnimChangeUtils(Context context, SurfaceView surfaceView, int code) {
        this.surfaceView = surfaceView;
        this.code = code;
        this.context = context;
        //设置SurfaceView
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().addCallback(new HolderCallback());
        init();
    }


    /**
     * 初始化设置
     * mediaPlayer设置
     * surfarceView设置
     */
    private void init() {
        mediaPlayer = new MediaPlayer();
        //mediaPlayer设置
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    /**
     * 天气变化和当前的时间
     *
     * @param code
     */
    public void setCode(int code) {
        if (code == this.code || code < 0) {
            //在切换ViewPager时如果code没有发生变化。。则不执行下面的方法
            return;
        }
        if (!justBack) {
            this.code = code;
            setVideoSource();
        } else {
            this.tempCode = code;
        }
    }

    /**
     * @param dayOrNight : true 白天 false 夜晚
     */
    public void setDayOrNight(boolean dayOrNight) {
        if (dayOrNight == this.dayOrNight) {
            return;
        }
        this.dayOrNight = dayOrNight;
        setVideoSource();
    }

    /**
     * 设置天气动画数据源并进行播放
     */
    private void setVideoSource() {
        //TODO :天气播放动画需要设置
        try {
            mediaPlayer.reset();
            AssetFileDescriptor fd = null;  //用来读取安卓文件信息
            AssetManager manager = context.getAssets();
            if (dayOrNight) {   //白天
                switch (code) {
                    case Contacts.WEATHER_SUNNY:  //晴天
                        fd = manager.openFd("day_sunny.mp4");
                        break;
                    case Contacts.WEATHER_CLOUDY:  //多云
                        fd = manager.openFd("day_windy.mp4");
                        break;

                    case Contacts.WEATHER_DULL:   //阴天
                        fd = manager.openFd("day_sunny.mp4");
                        break;
                    case Contacts.WEATHER_RAINDY_SHOWER:  //阵雨
                    case Contacts.WEATHER_RAINDY_SMALL:  //小雨
                        fd = manager.openFd("day_rain.mp4");
                        break;
                    default:
                        fd = manager.openFd("day_sunny.mp4");
                        break;

                }
            } else {       //夜晚
                switch (code) {
                    case Contacts.WEATHER_SUNNY:  //晴天
                        fd = manager.openFd("night_sun.mp4");
                        break;
                    case Contacts.WEATHER_CLOUDY:  //多云
                        fd = manager.openFd("day_windy.mp4");
                        break;

                    case Contacts.WEATHER_DULL:   //阴天
                        fd = manager.openFd("night_sun.mp4");
                        break;
                    case Contacts.WEATHER_RAINDY_SHOWER:  //阵雨
                    case Contacts.WEATHER_RAINDY_SMALL:  //小雨
                        fd = manager.openFd("night_rain.mp4");
                        break;
                    default:
                        fd = manager.openFd("night_sun.mp4");
                        break;
                }
            }
            mediaPlayer.setDataSource(fd.getFileDescriptor(),
                    fd.getStartOffset(), fd.getLength());
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
            play();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 播放视频或者音乐
     */
    public void play() {

        mediaPlayer.prepareAsync();
        mediaPlayer.setLooping(true);             //只有在开始播放有效循环播放
        //播放投射到屏幕上
        mediaPlayer.setDisplay(surfaceView.getHolder());
    }

    /**
     * 销毁方法
     */
    public void destoryMedia() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if (Debug)
            Log.i("Anim", "onBufferingUpdate()");

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //重新播放
        if (Debug)
            Log.i("Anim", "onCompletion()***" + mediaPlayer.isPlaying());
        //destoryMedia();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (Debug)
            Log.i("Anim", "onError()");
        return false;
    }


    private class HolderCallback implements SurfaceHolder.Callback {

        //首先会创建新的MediaPlayer
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (Debug)
                Log.i("Anim", "surfaceCreated()");
            if (justBack) {
                justBack = false;
                play();
                setCode(tempCode);
            } else {
                setCode(0);
            }

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (Debug)
                Log.i("Anim", "surfaceChanged()");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            justBack = true;
            if (Debug)
                Log.i("Anim", "surfaceDestroyed()");
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
