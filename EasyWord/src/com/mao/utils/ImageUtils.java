package com.mao.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;

public class ImageUtils {

	/**
	 * 创建圆形位图
	 * 
	 * @param bitmap 
	 * @return
	 */
	public static Bitmap createCircleBitmap(Bitmap bitmap) {
		if(bitmap == null) {
			return null;
		}
		
		int bmWidth = bitmap.getWidth();
		int bmHeight = bitmap.getHeight();
		int side = bmWidth < bmHeight ? bmWidth : bmHeight;
		int x = (bmWidth - side) / 2;
		int y = (bmHeight - side) / 2;
		Bitmap newBm = Bitmap.createBitmap(side, side, Bitmap.Config.ARGB_8888);
		if(newBm != null) {
			Canvas canvas = new Canvas(newBm);
		    Paint paint = new Paint();
		    paint.setAntiAlias(true);
		    paint.setFilterBitmap(true);
		    paint.setDither(true);
		   
		    Rect rect = new Rect(0, 0, newBm.getWidth(), newBm.getHeight());
		    canvas.drawARGB(0, 0, 0, 0);
		    canvas.drawCircle(newBm.getWidth()/2, newBm.getHeight()/2, newBm.getHeight()/2, paint);
		    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		    canvas.drawBitmap(bitmap, rect, rect, paint);
		    
		    return newBm;
		}
		return null;
	}
	
	/**
	 * 创建重叠位图
	 * 
	 * @param width
	 * @param height
	 * @param bmList
	 * @return
	 */
	public static Bitmap createOverlapBitmap(int width, int height, List<Bitmap> bmList) {
		if(width <= 0 || height <= 0 || bmList == null) {
			return null;
		}
		Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(bm);
		for(Bitmap bitmap : bmList) {
			float left = (width - bitmap.getWidth()) / 2;
			float top = (height - bitmap.getHeight()) / 2;
			canvas.drawBitmap(bitmap, left, top, null);
		}
		return bm;
	}
	
	/**
     * <p>获取Bitmap大小,该方法为版本兼容的.</p>
     * 
     * @param bitmap 指定的位图,注意不可为null.
     * 
     * @return 如果bitmap为null那么将返回-1,否则返回指定位图的大小,单位为字节.
     * 
     * @author 麦灿标
     * */
    @SuppressLint("NewApi")
	public static int sizeOfBitmap(Bitmap bitmap) {
    	if(bitmap == null) {
    		return -1;
    	}
    	//API 19及以上版本
    	if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
    		return bitmap.getAllocationByteCount();
    	//API 12 ~ API 18
    	} else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
    		return bitmap.getByteCount();
    	//API 12以下
    	} else {
    		return bitmap.getRowBytes() * bitmap.getHeight();
    	}
    }
    
    /**
     * 创建缩放位图
     * 
     * @param src 要进行缩放的位图
     * @param factor 缩放因子,必须大于0,1表示返回原位图
     * @return 成功返回缩放后的位图,失败返回null.
     */
    public static Bitmap createScaledBitmap(Bitmap src, float factor) {
    	if(src == null || factor <= 0.0f) {
    		return null;
    	}
    	int dstWidth = Math.round(src.getWidth() * factor);
    	int dstHeight = Math.round(src.getHeight() * factor);
    	return Bitmap.createScaledBitmap(src, dstWidth, dstHeight, true);
    }
    
    /**
     * Drawable转Bitmap
     * 
     * @param d 要转换的Drawable对象,不能为null.
     * @return 转换成功返回一幅位图,失败返回null.
     */
    public static Bitmap drawable2Bitmap(Drawable d) {
    	if(d == null) {
    		return null;
    	}
    	Bitmap bm = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
    	Canvas canvas = new Canvas(bm);
    	d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
    	d.draw(canvas);
    	return bm;
    }
    
    /**
     * 保存位图到指定路径下
     * 
     * @param bm 要保存的位图
     * @param quality 要保持的位图质量,范围:0-100
     * @param path 要保存的路径,包括文件名
     * @return 保存成功返回true,失败返回false.
     */
    public static boolean saveBitmap(Bitmap bm, int quality, String path) {
    	if(bm == null || quality < 0 || quality > 100 || TextUtils.isEmpty(path)) {
    		return false;
    	}
    	File f = new File(path);
    	if(!f.exists()) {
    		try {
    			if(!f.createNewFile()) {
    				return false;
    			}
    		} catch(Exception e) {
    			e.printStackTrace();
    			return false;
    		}
    	}
    	OutputStream os = null;
    	try {
    		os = new FileOutputStream(f);
    		return bm.compress(CompressFormat.JPEG, quality, os);
    	} catch (Exception e) {
    		e.printStackTrace();
    		return false;
    	} finally {
    		IoUtils.closeOutputStream(os);
    	}
    	
    }
}
