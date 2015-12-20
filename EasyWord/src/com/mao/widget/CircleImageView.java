package com.mao.widget;

import com.mao.utils.ImageUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircleImageView extends ImageView {
	
	private Rect rect;
	private Paint paint;
	
	private Drawable mPreDrawable;
	private Bitmap mBitmap;
	
	public CircleImageView(Context context) {
		this(context, null);
	}
	
	public CircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		rect = new Rect();
		paint = new Paint();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		//super.onDraw(canvas);
		Drawable mDrawable = getDrawable();
		if(mDrawable != null && mDrawable != mPreDrawable) {
			mPreDrawable = mDrawable;
			mBitmap = ((BitmapDrawable) mDrawable).getBitmap();
			mBitmap = ImageUtils.createCircleBitmap(mBitmap);
			mBitmap = Bitmap.createScaledBitmap(mBitmap, getWidth(), getHeight(), false);
		}
		if(mBitmap != null) {
			rect.left = 0;
			rect.top = 0;
			rect.right = getWidth();
			rect.bottom = getHeight();
			paint.reset();
			
			canvas.drawBitmap(mBitmap, null, rect, paint);
		} else {
			super.onDraw(canvas);
		}
	}
}
