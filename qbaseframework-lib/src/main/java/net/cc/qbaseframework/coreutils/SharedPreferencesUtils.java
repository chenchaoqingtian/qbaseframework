package net.cc.qbaseframework.coreutils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * <p/>Description：SharedPreferences管理工具
 * <p/>author： chen chao
 */
public class SharedPreferencesUtils {

    private static SharedPreferences DEFAULT_SHARE_PREFERENCE;

    private static SharedPreferences getSharedPreferences(Context context)
    {
        if (DEFAULT_SHARE_PREFERENCE == null)
        {
            DEFAULT_SHARE_PREFERENCE = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return DEFAULT_SHARE_PREFERENCE;
    }

   public static SharedPreferences getSharedPreferences(Context context, String name){
       return context.getSharedPreferences(name, Context.MODE_PRIVATE);
   }

    /**
     * 存储对象到文件
     */
    public static void saveSharedPreferenceValue(Context context, String key, Object val){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();

        if(val instanceof Boolean){
            editor.putBoolean(key, (Boolean) val);
        }
        else if(val instanceof Float){
            editor.putFloat(key, (Float) val);
        }
        else if(val instanceof Long){
            editor.putLong(key, (Long) val);
        }
        else if(val instanceof Integer){
            editor.putInt(key, (Integer) val);
        }
        else if(val instanceof String){
            editor.putString(key, (String) val);
        }
        else{
            return;
        }
        editor.commit();
    }

    public static void saveSharedPreferenceValue(Context context, String name, String key, Object val){
        SharedPreferences.Editor editor = getSharedPreferences(context, name).edit();

        if(val instanceof Boolean){
            editor.putBoolean(key, (Boolean) val);
        }
        else if(val instanceof Float){
            editor.putFloat(key, (Float) val);
        }
        else if(val instanceof Long){
            editor.putLong(key, (Long) val);
        }
        else if(val instanceof Integer){
            editor.putInt(key, (Integer) val);
        }
        else if(val instanceof String){
            editor.putString(key, (String) val);
        }
        else{
            return;
        }
        editor.commit();
    }

    public static String getString(Context context, String key, String defValue){
        return getSharedPreferences(context).getString(key, defValue);
    }

    public static String getString(Context context, String name, String key, String defValue){
        return getSharedPreferences(context, name).getString(key, defValue);
    }

    public static float getFloat(Context context, String key, float defValue){
        return getSharedPreferences(context).getFloat(key, defValue);
    }

    public static float getFloat(Context context, String name, String key, float defValue){
        return getSharedPreferences(context, name).getFloat(key, defValue);
    }

    public static long getLong(Context context, String key, long defValue){
        return getSharedPreferences(context).getLong(key, defValue);
    }

    public static long getLong(Context context, String name, String key, long defValue){
        return getSharedPreferences(context, name).getLong(key, defValue);
    }

    public static int getInteger(Context context, String key, int defValue){
        return getSharedPreferences(context).getInt(key, defValue);
    }

    public static int getInteger(Context context, String name, String key, int defValue){
        return getSharedPreferences(context, name).getInt(key, defValue);
    }

    public static boolean getBoolean(Context context, String key, boolean defValue){
        return getSharedPreferences(context).getBoolean(key, defValue);
    }

    public static boolean getBoolean(Context context, String name, String key, boolean defValue){
        return getSharedPreferences(context, name).getBoolean(key, defValue);
    }

    public static void removeSharedPreferenceValue(Context context, String key){
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().remove(key).commit();
    }

    public static void removeSharedPreferenceValue(Context context, String name, String key){
        SharedPreferences preferences = getSharedPreferences(context, name);
        preferences.edit().remove(key).commit();
    }
}
