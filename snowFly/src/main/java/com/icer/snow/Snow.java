package com.icer.snow;

import android.graphics.Bitmap;

public class Snow {
	/*
	 * 雪花的图片 
	 */
	Bitmap bitmap;
	
	/**
	 * 雪花开始飘落的横坐标
	 */
	float x;
	
	/**
	 * 雪花开始飘落的纵坐标
	 */
	float y;
	
	/**
	 * 雪花下落的速度
	 */
	float speed;
	
	/**
	 * 雪花下落时偏移的值
	 */
	float offset;

	public Snow(Bitmap bitmap, float x, float y, float speed, float offset) {
		this.bitmap = bitmap;
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.offset = offset;
	}


	
	
	

}
