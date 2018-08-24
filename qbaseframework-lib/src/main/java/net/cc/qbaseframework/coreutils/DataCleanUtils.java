package net.cc.qbaseframework.coreutils;

import android.content.Context;

import java.io.File;

/**
 * <p/>Description：数据缓存清理管理
 * <p/>author： chen chao
 */
public class DataCleanUtils {
    /**
     * @Title: cleanInternalCache
     * @Description: 清除应用内部缓存
     * @param  context
     * @return void
     */
    public static void cleanInternalCache(Context context) {
        FileUtils.deleteFile(context.getCacheDir());
    }

    /**
     * @Title: cleanInternalCache
     * @Description: 清除应用外部缓存
     * @param  context
     * @return void
     */
    public static void cleanExternalCache(Context context) {
        if(SdCardUtils.exist()){
            FileUtils.deleteFile(context.getExternalCacheDir());
        }
    }

    /**
     * @Title: cleanDatabases
     * @Description: 清除应用所有数据库
     * @param  context
     * @return void
     */
    public static void cleanDatabases(Context context){
        File file = new File(context.getFilesDir().getParentFile().getPath()+"/databases");
        FileUtils.deleteFile(file);
    }

    /**
     * @Title: cleanSharedPreference
     * @Description: 清除应用所有属性配置
     * @param  context
     * @return void
     */
    public static void cleanSharedPreference(Context context) {
        File file = new File(context.getFilesDir().getParentFile().getPath()+"/shared_prefs");
        FileUtils.deleteFile(file);
    }

    /**
     * @Title: cleanDatabaseByName
     * @Description: 按名字清除本应用数据库
     * @param  context
     * @param  dbName 数据库名称
     * @return void
     */
    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    /**
     * @Title: cleanCustomCache
     * @Description: 清除自定义路径下的文件
     * @param  filePath 自定义缓存目录
     * @return void
     */
    public static void cleanCustomCache(String filePath) {
        FileUtils.deleteFile(new File(filePath));
    }

    /** * 清除本应用所有的数据 * * @param context * @param filepath */
    public static void cleanApplicationData(Context context, String... filepath) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        for (String filePath : filepath) {
            cleanCustomCache(filePath);
        }
    }
}
