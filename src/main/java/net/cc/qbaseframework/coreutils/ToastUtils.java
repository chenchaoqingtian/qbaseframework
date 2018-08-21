package net.cc.qbaseframework.coreutils;

import android.content.Context;
import android.widget.Toast;

/**
 * @ClassName ToastUtils
 * @Description toast提示工具
 * @author chen chao
 * @date 2016-4-25
 */

public class ToastUtils
{
	private static Toast mToast;

	public static void showShortToast(Context context, int strResId)
	{
		if (mToast != null)
		{
			mToast.cancel();
		}
		mToast = Toast.makeText(context, strResId, Toast.LENGTH_SHORT);
		mToast.show();
	}

	public static void showShortToast(Context context, String str)
	{
		if (mToast != null)
		{
			mToast.cancel();
		}
		mToast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
		mToast.show();
	}

	public static void showLongToast(Context context, int strResId)
	{
		if (mToast != null)
		{
			mToast.cancel();
		}
		mToast = Toast.makeText(context, strResId, Toast.LENGTH_LONG);
		mToast.show();
	}

	public static void showLongToast(Context context, String str)
	{
		if (mToast != null)
		{
			mToast.cancel();
		}
		mToast = Toast.makeText(context, str, Toast.LENGTH_LONG);
		mToast.show();
	}

	public static void cancelToast()
	{
		if (mToast != null)
		{
			mToast.cancel();
		}
	}
}
