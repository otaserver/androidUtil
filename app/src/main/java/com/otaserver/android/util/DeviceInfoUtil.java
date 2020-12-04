package com.otaserver.android.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.otaserver.android.dao.DeviceInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;

//import java.time.format.DateTimeFormatter;

public abstract class DeviceInfoUtil {

    public abstract void save(DeviceInfo deviceInfo, SharedPreferences pref);

    public abstract DeviceInfo load(SharedPreferences pref);

//    static DateTimeFormatter appInstallDateFomatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
            if (("set" + name).toLowerCase(Locale.getDefault()).equals(m[i].getName().toLowerCase(Locale.getDefault()))) {
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
        String androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID);
        Log.d(TAG, "ANDROID_ID:" + androidId);

        String serial = android.os.Build.SERIAL;
        Log.d(TAG, "SERIAL:" + serial);

        // Build.VERSION.SDK_INT：当前硬件上运行的android版本。
        // The SDK version of the software currently running on this hardware
        // device. This value never changes while a device is booted, but it may
        // increase when the hardware manufacturer provides an OTA update.
        // 以下是一个比较版本的例子
        //  if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
        int androidApiLevel = Build.VERSION.SDK_INT;
        Log.d(TAG, "ANDROID_API_LEVEL: " + androidApiLevel);

        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setAndroidId(androidId);
        deviceInfo.setSerial(serial);
        deviceInfo.setAndroidApiLevel(androidApiLevel);
        return deviceInfo;
    }

    //@SuppressLint 为代表抑制android的权限申请的提示。
    @SuppressLint("MissingPermission")
    /**
     * 获取需要运行时权限才能获得的设备属性，
     * 目前电话号码tel1属性是可以获得的。
     */
    public DeviceInfo getDeviceInfoPermission(Context context) {

        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        String deviceId = getDeviceId(tm);

        // 在android8.0(O,level26)才支持，但测试android10(Q,level29)是无法取得imei的。
        //IMEI for GSM.
        String imei_0 = getImei(tm, 0);
        String imei_1 = getImei(tm, 1);
        //MEID for CDMA.
        String meid = getMeid(tm);

        String tel1 = tm.getLine1Number();
        String simSerialNumber = tm.getSimSerialNumber();
        String imsi = tm.getSubscriberId();

        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setImei0(imei_0);
        deviceInfo.setImei1(imei_1);
        deviceInfo.setMeid(meid);
        deviceInfo.setTel1(tel1);
        deviceInfo.setSimSerialNumber(simSerialNumber);
        deviceInfo.setImsi(imsi);
        deviceInfo.setDeviceId(deviceId);

        return deviceInfo;
    }

    //@SuppressLint 为代表抑制android的权限申请的提示。
    @SuppressLint("MissingPermission")
    //此方法在android8.0(O,apiLevel26)中才支持。
    @TargetApi(Build.VERSION_CODES.O)
    private String getImei(TelephonyManager tm, int index) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return tm.getImei(index);
        } else {
            return "";
        }
    }

    //@SuppressLint 为代表抑制android的权限申请的提示。
    @SuppressLint("MissingPermission")
    //此方法在android8.0(O,apiLevel26)中才支持。
    @TargetApi(Build.VERSION_CODES.O)
    private String getMeid(TelephonyManager tm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return tm.getMeid();
        } else {
            return "";
        }
    }

    //@SuppressLint 为代表抑制android的权限申请的提示。
    @SuppressLint("MissingPermission")
    private String getDeviceId(TelephonyManager tm) {
        //在api26已经过时。官方建议使用getImei()和getMeid()这两个方法得到相应的值。
        return tm.getDeviceId();
    }


}
