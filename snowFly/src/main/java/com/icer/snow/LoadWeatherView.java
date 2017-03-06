package com.icer.snow;

import java.lang.reflect.Field;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;

public class LoadWeatherView extends SurfaceView implements
		SurfaceHolder.Callback, Runnable {

	private SurfaceHolder holder;

	private boolean isRunning = true;

	private int statusBarHeight;

	/**
	 * 屏幕的像素
	 */
	private int screenWidth;
	private int screenHeiht;

	private Bitmap windPoint;

	/**
	 * 风车图片
	 */
	private Bitmap Windmill;

	/**
	 * 背景图片
	 */
	private Bitmap viewBg;

	public LoadWeatherView(Context context) {
		super(context);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		holder = getHolder();
		holder.addCallback(this);
		holder.setFormat(PixelFormat.RGBA_8888); // 顶层绘制SurfaceView设成透明
		getViewSize(context);
		LoadWindmillImage();

	}

	private void LoadWindmillImage() {
		viewBg = BitmapFactory.decodeResource(getResources(), R.drawable.bg_na);
		Windmill = BitmapFactory.decodeResource(getResources(),
				R.drawable.na_windmill);
		windPoint = BitmapFactory.decodeResource(getResources(),
				R.drawable.na_point);
		float percent = percentum();
		Log.v("icers", screenWidth + "");

		int _witdh = (int) ((screenWidth) / percent);
		Log.v("icers", _witdh + "");
		Windmill = Bitmap.createScaledBitmap(Windmill, _witdh * 2, _witdh * 2,
				true);

	}

	/**
	 * 用于获取状态栏的高度。
	 * 
	 * @return 返回状态栏高度的像素值。
	 */
	private int getStatusBarHeight() {
		if (statusBarHeight == 0) {
			try {
				Class<?> c = Class.forName("com.android.internal.R$dimen");
				Object o = c.newInstance();
				Field field = c.getField("status_bar_height");
				int x = (Integer) field.get(o);
				statusBarHeight = getResources().getDimensionPixelSize(x);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return statusBarHeight;
	}

	// 获取屏幕的分辨率
	private void getViewSize(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(metrics);
		this.screenHeiht = metrics.heightPixels;
		this.screenWidth = metrics.widthPixels;
	}

	/**
	 * 获取背景图和风车的比率 ，从而根据这个比例改变各个手机上面的风车图片大小
	 * 
	 * @return
	 */
	private float percentum() {
		float bg_height = viewBg.getWidth();
		// float wind_width=Windmill.getHeight();
		return bg_height / 250;
	}

	@Override
	public void run() {

		float rotate = 0;// 旋转角度变量

		while (isRunning) {
			Log.i("icer", "Running");
			Canvas canvas = null;
			synchronized (this) {
				try {
					canvas = holder.lockCanvas();
					if (canvas != null) {
						Paint paint = new Paint();
						paint.setAntiAlias(true);

						// 对图片抗锯齿
						paint.setFilterBitmap(true);
						RectF rect = new RectF(0, 0, screenWidth, screenHeiht
								- statusBarHeight);
						canvas.drawBitmap(viewBg, null, rect, paint);
						Matrix matrix = new Matrix();
						matrix.postRotate((rotate += 2) % 360f,
								Windmill.getWidth() / 2,
								Windmill.getHeight() / 2);
						matrix.postTranslate(0, Windmill.getHeight() / 2);
						canvas.drawBitmap(Windmill, matrix, paint);

						Thread.sleep(5);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (canvas != null) {
						holder.unlockCanvasAndPost(canvas);
					}
				}

			}

		}

	}

	public void setRunning(boolean state) {
		isRunning = state;

	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {

		new Thread(this).start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		isRunning = false;

	}

}
