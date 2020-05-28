package com.otaserver.android.util;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;

import com.otaserver.android.dao.DeviceInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.format.DateTimeFormatter;

public abstract class DeviceInfoUtil {

    public abstract void save(DeviceInfo deviceInfo, SharedPreferences pref);

    public abstract DeviceInfo load(SharedPreferences pref);

    static DateTimeFormatter appInstallDateFomatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss");

    Field[] fields = DeviceInfo.class.getDeclaredFields();

    private static final String TAG = "DeviceInfoUtil";

    /**
     * 使用反射机制动态调用dto的set方法
     *
     * @param dto
     * @param name
     * @param value
     * @throws Exception
     */
    void setValue(Object dto, String name, Object value) throws Exception {
        Method[] m = dto.getClass().getMethods();
        for (int i = 0; i < m.length; i++) {
            if (("set" + name).toLowerCase().equals(m[i].getName().toLowerCase())) {
                m[i].invoke(dto, value);
                break;
            }
        }
    }

    /**
     * 不需要运行时权限获取android属性的例子，
     * 可以获得androidID，appInstallDate，appInstallGuid三个属性。
     */
    public DeviceInfo getDeviceInfoNoNeedPermission(ContentResolver contentResolver) {

        /**
         * 在设备首次启动时，系统会随机生成一个64位的数字，并把这个数字以16进制字符串的形式保存下来，这个16进制的字符串就是ANDROID_ID，当设备被wipe后该值会被重置。
         * ANDROID_ID可以作为设备标识，但需要注意：
         * 1. 厂商定制系统的Bug：不同的设备可能会产生相同的ANDROID_ID：9774d56d682e549c。
         * 2. 厂商定制系统的Bug：有些设备返回的值为null。
         * 3. 设备差异：对于CDMA设备，ANDROID_ID和TelephonyManager.getDeviceId() 返回相同的值。
         */
        String androidId = Settings.System.getString(contentResolver, Settings.System.ANDROID_ID);
        Log.d(TAG, "ANDROID_ID:" + androidId);

        String serial = android.os.Build.SERIAL;
        Log.d(TAG, "serial:" + serial);

        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setAndroidId(androidId);
        deviceInfo.setSerial(serial);
        return deviceInfo;
    }

}