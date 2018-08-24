
package net.cc.qbaseframework.coreannotation;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName InjectHandler
 * @Description 注解实现
 */

public class InjectHandler
{

	public static void inject(Object obj, Activity activity) {
		try {
			injectView(obj, activity);
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	public static void inject(Object obj, Dialog dialog) {
		try {
			injectView(obj, dialog);
			return;
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	public static void inject(Object obj, View view) {
		try {
			injectView(obj, view);
		} catch (Exception exception) {

			throw new RuntimeException(exception);
		}
	}

	/**
	 * 将所有已标注的字段，赋予对应视图的引用
	 * 
	 * @param view
	 * @param container
	 * @throws Exception
	 */
	private static void injectView(Object view, Object container) throws Exception {
		Iterator<Field> localIterator = getAnnotationFields(view.getClass()).iterator();
		while (localIterator.hasNext()) {
			Field field = localIterator.next();

			ViewInject inject = field.getAnnotation(ViewInject.class);
			if (inject != null) {
				View v = null;
				if ((container instanceof Activity)) {
					v = ((Activity) container).findViewById(inject.value());
				} else if ((container instanceof View)) {
					v = ((View) container).findViewById(inject.value());
				} else if (container instanceof Dialog) {
					v = ((Dialog) container).findViewById(inject.value());
				}
				if (v == null) {
					continue;
				}

				field.setAccessible(true);
				field.set(view, v);
			}
		}
	}

	public static List<Field> getAnnotationFields(Class<?> clasz) {
		ArrayList<Field> result = new ArrayList<Field>();
		Field fields[] = clasz.getDeclaredFields();
		int i = fields.length;
		for (int j = 0; j < i; j++) {
			Field f = fields[j];
			ViewInject viewId = f.getAnnotation(ViewInject.class);
			if (viewId != null) {
				result.add(fields[j]);
			}
		}

		// 从效率上考虑，不递归到父类
		if (clasz != Object.class && clasz.getSuperclass() != null) {
			result.addAll(getAnnotationFields(clasz.getSuperclass()));
		}

		return result;
	}
}
