package com.mao.dialog;

import java.util.Locale;

import com.mao.easyword.R;
import com.mao.widget.ColorPickerView;
import com.mao.widget.ColorPickerView.OnColorPickerListener;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class ColorPicker extends Dialog{
	
	private ColorPickerView color_picker_view;
	private TextView color_picker_current_color;
	private Button color_picker_cancel;
	private Button color_picker_ok;
	
	private OnColorSelectedListener mOnColorSelectedListener;
	
	/**
	 * 构造函数
	 * 
	 * @param context 上下文,必须是Activity对象
	 * @param initColor 初始颜色值
	 */
    public ColorPicker(Context context) {
        super(context);
        init();
    }
    
    private void init() {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	//设置自定义View
    	setContentView(R.layout.color_picker_layout);
    	
    	color_picker_view = (ColorPickerView) findViewById(R.id.color_picker_view);
    	color_picker_current_color = (TextView) findViewById(R.id.color_picker_current_color);
    	color_picker_cancel = (Button) findViewById(R.id.color_picker_cancel);
    	color_picker_ok = (Button) findViewById(R.id.color_picker_ok);
    	
    	setEvent();
    }
    
    private void setEvent() {
    	//取消
    	color_picker_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cancel();
			}
		});
    	//确定
    	color_picker_ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mOnColorSelectedListener != null) {
					int color = color_picker_view.getColor();
					mOnColorSelectedListener.OnColorSelected(color);
				}
				cancel();
			}
		});
    	//设置监听器
    	color_picker_view.setOnColorPickerListener(new OnColorPickerListener() {
			
			@Override
			public void onColorChanged(int preColor, int newColor) {
				setCurrentColorText(newColor);
			}
		});
    }
    
    //设置当前颜色文本
    private void setCurrentColorText(int color) {
    	if(color_picker_view != null) {
	    	StringBuilder sb = new StringBuilder();
			String tip = getContext().getResources().getString(R.string.current_color);
			sb.append(tip);
			sb.append(":#");
			sb.append(Integer.toHexString(color).toUpperCase(Locale.getDefault()));
			color_picker_current_color.setText(sb.toString());
    	}
    }
    
    /**
     * 设置颜色选择监听器
     * 
     * @param listener 要设置的监听器
     */
    public void setOnColorSelectedListener(OnColorSelectedListener listener) {
    	mOnColorSelectedListener = listener;
    }
    
    /**
     * 获取颜色选择监听器
     * 
     * @return 返回颜色选择监听器
     */
    public OnColorSelectedListener getOnColorSelectedListener() {
    	return mOnColorSelectedListener;
    }
    
    /**
     * 设置颜色
     * 
     * @param color 要设置的颜色值
     */
    public void setColor(int color) {
    	if(color_picker_view != null) {
    		color_picker_view.setColor(color);
    	}
    	setCurrentColorText(color);
    }
    
    /**
     * 颜色选择监听器
     * 
     * @author mao
     *
     */
    public interface OnColorSelectedListener {
    	
    	/**
    	 * 当确定选择某一颜色时回调该方法
    	 * 
    	 * @param color 选择的颜色值
    	 */
    	void OnColorSelected(int color);
    }
}
