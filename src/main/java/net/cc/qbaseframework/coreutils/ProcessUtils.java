package net.cc.qbaseframework.coreutils;

import android.app.ActivityManager;
import android.content.Context;

/**
 * <p/>Description：进程管理工具
 * <p/>author： chen chao
 */
public class ProcessUtils {

    /**
     * 获取进程名
     * @param context
     * @return
     */
    public static String getProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }

}
