package net.cc.qbaseframework.coreutils;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;

/**
 * @Description app UI工具类
 */

public class AppUIUtils
{
	/**
	 * px转为 sp
	 */
	public static int px2sp(Context context, float pxValue)
	{
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * dip转为 px
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 *  px 转为 dip
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 代码构造seletor
	 */
	public static StateListDrawable addStateDrawable(Context context,  int idNormal, int idPressed, int idFocused) { 
		StateListDrawable sd = new StateListDrawable(); 
		Drawable normal = idNormal == -1 ? null : context.getResources().getDrawable(idNormal); 
		Drawable pressed = idPressed == -1 ? null : context.getResources().getDrawable(idPressed); 
		Drawable focus = idFocused == -1 ? null : context.getResources().getDrawable(idFocused); 
		//注意该处的顺序，只要有一个状态与之相配，背景就会被换掉 
		//所以不要把大范围放在前面了，如果sd.addState(new[]{},normal)放在第一个的话，就没有什么效果了 
		sd.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, focus); 
		sd.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed); 
		sd.addState(new int[]{android.R.attr.state_focused}, focus); 
		sd.addState(new int[]{android.R.attr.state_pressed}, pressed); 
		sd.addState(new int[]{android.R.attr.state_enabled}, normal); 
		sd.addState(new int[]{}, normal); 
		return sd; 
	}

	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	public static int getScreenHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	public static int getDensityDpi(Context context) {
		return context.getResources().getDisplayMetrics().densityDpi;
	}

	public static float getDensity(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}

	public static int getMeasurHeigt(View view) {
		int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);

		int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

		view.measure(expandSpec, height);

		return view.getMeasuredHeight();
	}

	public static int getMeasurWidth(View view) {
		int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

		int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);

		view.measure(width, expandSpec);

		return view.getMeasuredWidth();
	}

	/**
	 * 
	 * @param context
	 * @param expectedWidth
	 * @param expectedHeight
	 * @return width=size[0],height=size[1]
	 */
	public static int[] getActuallyViewSize(Context context, int expectedWidth, int expectedHeight) {
		int width = getScreenWidth(context);
		int height = width * expectedHeight / expectedWidth;

		int[] size = new int[2];
		size[0] = width;
		size[1] = height;
		return size;

	}

	public static int getStateBarHeight(View view) {
		Rect rect = new Rect();

		view.getWindowVisibleDisplayFrame(rect);

		return rect.top;
	}

	/**
	 * 获得状态栏的高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getStatusHeight(Context context) {
		int statusHeight = 0;

		try {
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
			statusHeight = context.getResources().getDimensionPixelSize(height);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return statusHeight;
	}
}
