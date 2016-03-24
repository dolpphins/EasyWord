package com.mao.widget;

import android.R.integer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 颜色选择View
 * 
 * @author mao
 *
 */
public class ColorPickerView extends View {

	private Paint mPaint;
	private Paint mCenterPaint;
	
	private int innerRadius;//环形内半径
	private int outerRadius;//环形外半径
	
	private int[] mColors;
	
	private int mCurrentColor;//当前选择的颜色
	
	//监听器
	private OnColorPickerListener mOnColorPickerListener;
	
	public ColorPickerView(Context context) {
		this(context, null);
	}
	
	public ColorPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		mPaint = new Paint();
        mColors = new int[] {0xFF000000, 0xFFFFFFFF, 0xFFFF0000,
        						  0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF,
        		                  0xFF00FF00, 0xFFFFFF00, 0xFFFF0000};			
		SweepGradient sweepGradient = new SweepGradient(0, 0, mColors, null);
		mPaint.setShader(sweepGradient);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setAntiAlias(true);
		
		mCenterPaint = new Paint();
		mCurrentColor = Color.TRANSPARENT;
		mCenterPaint.setColor(mCurrentColor);
		mCenterPaint.setAntiAlias(true);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
		int measuredHeight = measuredWidth; 
		setMeasuredDimension(measuredWidth, measuredHeight);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		//平移画布
		int translateX = getWidth() / 2;
		int translateY = translateX;
		canvas.translate(translateX, translateY);
		//设置线宽
		mPaint.setStrokeWidth(getWidth() / 5);
		//画圆环
		int radius = getWidth() * 3 / 10;
		canvas.drawCircle(0, 0, radius, mPaint);//这里半径的终点为线宽的中点
		//画中心圆
		canvas.drawCircle(0, 0, getWidth() / 8, mCenterPaint);
		//保存圆环内半径和外半径信息
		innerRadius = (int)(radius - mPaint.getStrokeWidth() / 2);
		outerRadius = (int)(innerRadius + mPaint.getStrokeWidth());
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			break;
		case MotionEvent.ACTION_UP:
			updateColor(event);
			break;
		}
		return true;
	}
	
	private void updateColor(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		
		//转换坐标,以View中心为原点
		x -= getWidth() / 2;
		y -= getHeight() / 2;
		//如果是点击环形
		float distance2 = x * x + y * y;
		if(distance2 > innerRadius * innerRadius 
				&& distance2 < outerRadius * outerRadius) {
			int preColor = mCurrentColor;
			mCurrentColor = calculateColor(x, y);
			mCenterPaint.setColor(mCurrentColor);
			invalidate();
			
			if(mOnColorPickerListener != null) {
				mOnColorPickerListener.onColorChanged(preColor, mCurrentColor);
			}
		}
	}
	
	private int calculateColor(float x, float y) {
		//计算角度,这里以x正方向为角的一条边,逆时针旋转
		double radian = Math.atan2(y, x);
		if(radian < 0) {
			radian += 2 * Math.PI;
		}
		double angle = Math.toDegrees(radian);
		int index = Math.round((float)angle) / 45;
		int remainder = Math.round((float)angle)  % 45;
		
		int startColor = mColors[index];
		int endColor = mColors[index + 1];
		float delta = remainder / 45.0f;
		int alpha = calculateLinearGradient(Color.alpha(startColor), Color.alpha(endColor), delta);
		int red = calculateLinearGradient(Color.red(startColor), Color.red(endColor), delta);
		int green = calculateLinearGradient(Color.green(startColor), Color.green(endColor), delta);
		int blue = calculateLinearGradient(Color.blue(startColor), Color.blue(endColor), delta);
		
		return Color.argb(alpha, red, green, blue);
	}
	
	//delta范围0..1
	private int calculateLinearGradient(int start, int end, float delta) {
		return start + Math.round((end - start) * delta);
	}
	
	/**
	 * 获取当前选择的颜色,注意如果没有选择返回{@link Color#TRANSPARENT}
	 * 
	 * @return  返回当前选择的颜色
	 */
	public int getColor() {
		return mCurrentColor;
	}
	
	/**
	 * 设置颜色
	 * 
	 * @param color 要设置的颜色
	 */
	public void setColor(int color) {
		int preColor = mCurrentColor;
		mCurrentColor = color;
		//
		mCenterPaint.setColor(mCurrentColor);
		invalidate();
		if(mOnColorPickerListener != null) {
			mOnColorPickerListener.onColorChanged(preColor, mCurrentColor);
		}
	}
	
	/**
	 * 设置颜色监听器
	 * 
	 * @param listener 要设置的监听器
	 */
	public void setOnColorPickerListener(OnColorPickerListener listener) {
		mOnColorPickerListener = listener;
	}
	
	/**
	 * 获取颜色监听器
	 * 
	 * @return 返回颜色监听器
	 */
	public OnColorPickerListener getOnColorPickerListener() {
		return mOnColorPickerListener;
	}
	
	/**
	 * 监听接口
	 * 
	 */
	public interface OnColorPickerListener {
		
		/**
		 * 当颜色发生改变时回调该方法
		 * 
		 * @param oldColor 改变之前的颜色值
		 * @param newColor 改变之后的颜色值
		 */
		void onColorChanged(int preColor, int newColor);
	}
}
