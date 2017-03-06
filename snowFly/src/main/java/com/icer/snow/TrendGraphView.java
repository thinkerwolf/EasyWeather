package com.icer.snow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.R.color;
import android.R.integer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class TrendGraphView extends View {

	/**
	 * 趋势图的高度
	 */
	private int viewHeight;

	/**
	 * 趋势图的宽度
	 */
	private int viewWidth;

	/**
	 * 最低温度
	 */
	private int minTemp;

	/**
	 * 最高温度
	 */
	private int maxTemp;

	/**
	 * 每度的单位长度
	 */
	int mSpace;

	int[] dx = new int[6];

	private ArrayList<Integer> mTopTemp;
	private ArrayList<Integer> mLowTemp;
	// int[] mlowTemp=new int[]{4,7,8,1,3,4};
	// int[] mTopTemp=new int[]{14,17,8,11,13,8};

	private Paint mPointPaint;
	private Paint mPoint2Paint;
	private Paint mLine1Paint;
	private Paint mline2Paint;
	private Paint mTextPaint;
	private Paint mPath1Paint;
	private Paint mPath2Paint;

	public TrendGraphView(Context context) {
		super(context);
	}

	public TrendGraphView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mTopTemp = new ArrayList<Integer>();
		mTopTemp.add(10);
		mTopTemp.add(11);
		mTopTemp.add(12);
		mTopTemp.add(15);
		mTopTemp.add(11);
		mTopTemp.add(9);
		mLowTemp = new ArrayList<Integer>();
		mLowTemp.add(4);
		mLowTemp.add(5);
		mLowTemp.add(6);
		mLowTemp.add(9);
		mLowTemp.add(5);
		mLowTemp.add(3);
		init();
	}

	private void init() {

		//#CD8500
		mPointPaint = new Paint();
		mPointPaint.setAntiAlias(true);
		mPointPaint.setColor(Color.argb(255, 205, 85, 00));
		
		//#6495ED
		mPoint2Paint = new Paint();
		mPoint2Paint.setAntiAlias(true);
		mPoint2Paint.setColor(Color.argb(255, 64, 95, 237));

		mLine1Paint = new Paint();
		mLine1Paint.setAntiAlias(true);
		mLine1Paint.setStrokeWidth(5);
		mLine1Paint.setColor(Color.argb(255, 170, 170, 170));

		mline2Paint = new Paint();
		mline2Paint.setColor(Color.argb(255, 170, 170, 170));
		mline2Paint.setAntiAlias(true);
		mline2Paint.setStrokeWidth(5);

		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setColor(Color.argb(255, 170, 170, 170));
		mTextPaint.setTextSize(25F);
		mTextPaint.setTypeface(Typeface.create("宋体",Typeface.BOLD));
		mTextPaint.setTextAlign(Align.CENTER);
		// postInvalidate();

		mPath1Paint = new Paint();
		// mPathPaint.setColor(color);
		// 838B8B
		mPath1Paint.setColor(Color.argb(20, 255, 255, 255));
		
		mPath2Paint = new Paint();
		// mPathPaint.setColor(color);
		// 838B8B
		mPath2Paint.setColor(Color.argb(10, 255, 255, 255));
		

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(measureWidth(widthMeasureSpec),
				measureHeight(heightMeasureSpec));
	}

	private int measureHeight(int heightMeasureSpec) {

		int result = 0;
		int specMode = MeasureSpec.getMode(heightMeasureSpec);
		int specSize = MeasureSpec.getSize(heightMeasureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = getPaddingBottom() + getPaddingTop();
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}

		return result;
	}

	private int measureWidth(int widthMeasureSpec) {
		int result = 0;
		int size = MeasureSpec.getSize(widthMeasureSpec); // 每次调用此方法，测量用到的size会发生变化
		int mode = MeasureSpec.getMode(widthMeasureSpec); // 根据定义的Layout_width,Layout_height，会对此值产生影响
		if (mode == MeasureSpec.EXACTLY) {
			result = size;
		} else if (mode == MeasureSpec.UNSPECIFIED) {
			result = getPaddingLeft() + getPaddingRight();
		} else {
			result = Math.min(result, size);
		}
		System.out.println("Height size:" + size);
		System.out.println("Height mode:" + mode);
		return result;

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		viewHeight = getHeight();
		viewWidth = getWidth();
		Log.i("icer", viewHeight + "");
		Log.i("icer", viewWidth + "");

		spaceHeightWidth();

		FontMetrics fontMetrics = mTextPaint.getFontMetrics();
		// 计算文字高度
		float fontHeight = fontMetrics.bottom - fontMetrics.top;
		Path path = new Path();
		Path path2=new Path();
		

		for (int i = 0; i < mTopTemp.size(); i++) {
			int _hTop = (maxTemp - mTopTemp.get(i) + 2) * mSpace + 30;
			if (i < mTopTemp.size() - 1) {
				canvas.drawLine(dx[i], _hTop, dx[i + 1],
						(maxTemp - mTopTemp.get(i + 1) + 2) * mSpace + 30,
						mLine1Paint);
			}
			if(i==0){
				path.moveTo(dx[0], _hTop);
				path2.moveTo(dx[0], _hTop);
			}
			if(i==1){
				path2.lineTo(dx[i], _hTop);
			}
			
			path.lineTo(dx[i], _hTop);

			canvas.drawText(mTopTemp.get(i) + "°", dx[i], _hTop - fontHeight
					, mTextPaint);
			canvas.drawCircle(dx[i], _hTop, 10, mPointPaint);

		}

		for (int i = mLowTemp.size() - 1; i >= 0; i--) {
			int _hLow = (maxTemp - mLowTemp.get(i) + 2) * mSpace + 30;
			if (i > 0) {
				canvas.drawLine(dx[i], _hLow, dx[i - 1],
						(maxTemp - mLowTemp.get(i - 1) + 2) * mSpace + 30,
						mline2Paint);
				
			}
			if(i==1){
				path2.lineTo(dx[i], _hLow);
			}
			if(i==0){
				path2.lineTo(dx[i], _hLow);
			}
			
			path.lineTo(dx[i], _hLow);
			canvas.drawText(mLowTemp.get(i) + "°", dx[i], _hLow + fontHeight*3/2,
					mTextPaint);
			canvas.drawCircle(dx[i], _hLow, 10, mPoint2Paint);
		}
		
		path.close();
		path2.close();
		

		canvas.drawPath(path, mPath1Paint);
		canvas.drawPath(path2, mPath2Paint);


	}

	private void spaceHeightWidth() {

		minTemp = Collections.min(mLowTemp);
		maxTemp = Collections.max(mTopTemp);

		int h = maxTemp - minTemp;
		System.out.println("温差：" + h);
		if (h < 20) {
			mSpace = (viewHeight - 30) / (h + 4);

		} else {
			mSpace = (viewHeight - 30) / (h + 10);
		}

		dx[0] = viewWidth * 1 / 12;
		dx[1] = viewWidth * 3 / 12;
		dx[2] = viewWidth * 5 / 12;
		dx[3] = viewWidth * 7 / 12;
		dx[4] = viewWidth * 9 / 12;
		dx[5] = viewWidth * 11 / 12;
	}

	/**
	 * 得到最高温度
	 * 
	 * @param topTemp
	 * @return
	 */
	private int getMaxiTemperature(int[] topTemp) {
		return getMaxByBubbleSort(topTemp);
	}

	/**
	 * 得到最低温度
	 * 
	 * @param lowTemp
	 * @return
	 */
	private int getMiniTemperature(int[] lowTemp) {
		return getMinByBubbleSort(lowTemp);

	}

	/**
	 * 冒泡排序取最大值
	 * 
	 * @param args
	 * @return
	 */
	public static int getMaxByBubbleSort(int[] args) {
		int[] _array = args;
		int max = 0;
		for (int i = 0; i < _array.length; i++) {
			for (int j = i + 1; j < _array.length; j++) {
				if (_array[i] < _array[j]) {
					int temp = _array[i];
					_array[i] = _array[j];
					_array[j] = temp;
				}
			}
		}
		max = args[0];
		return max;

	}

	/**
	 * 冒泡排序取最大值
	 * 
	 * @param args
	 * @return
	 */
	public static int getMinByBubbleSort(int[] args) {
		int min = 0;
		for (int i = 0; i < args.length; i++) {
			for (int j = i + 1; j < args.length; j++) {
				if (args[i] > args[j]) {
					int temp = args[i];
					args[i] = args[j];
					args[j] = temp;
				}
			}
		}
		min = args[0];
		return min;

	}
	

}
