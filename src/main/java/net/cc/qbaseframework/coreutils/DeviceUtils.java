package net.cc.qbaseframework.coreutils;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * <p/>Description：设备相关信息查询工具
 * <p/>author： chen chao
 * <p/>date： 2017/8/23 17:08
 * <p/>update： (date)
 * <p/>version： V1.0
 */
public class DeviceUtils {

    protected static final String PREFS_DEVICE_ID = "device_id";

    protected static UUID uuid;

    public static boolean isCameraUseable() {
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            return false;
        }
        if (mCamera != null) {
            mCamera.release();
        }
        return true;
    }

    public static UUID generateDeviceUUID(Context context) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        if (uuid == null) {

            synchronized (DeviceUtils.class) {

                final String id = prefs.getString(PREFS_DEVICE_ID, null);

                if (id != null) {
                    // Use the ids previously computed and stored in the prefs file
                    uuid = UUID.fromString(id);
                } else {
                    final String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
                    // Use the Android ID unless it's broken, in which case fallback on deviceId,
                    // unless it's not available, then fallback on a random number which we store
                    // to a prefs file
                    try {
                        if (!"9774d56d682e549c".equals(androidId)) {
                            uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                        } else {
                            String deviceId = null;
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                                deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                            }
                            uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
                        }
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                    // Write the value out to the prefs file
                    prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString()).commit();

                }
            }
        }
        return uuid;
    }

    public static String getDeviceId(Context context){
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

}
