package com.otaserver.android.util;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.otaserver.android.dao.DeviceInfo;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

//import java.time.LocalDateTime;


/**
 * 将android设备信息以默认的文本格式保存在SharedPreference中。
 *
 * @author scott
 */
public class DeviceInfoTextUtil extends DeviceInfoUtil {

    private static final String TAG = "DeviceInfoTextUtil";

    /**
     * 保存DeviceInfo信息到SharedPerference中。
     *
     * @param deviceInfo
     * @param pref
     */
    @Override
    public void save(DeviceInfo deviceInfo, SharedPreferences pref) {

        SharedPreferences.Editor editor = pref.edit();
        //检查appInstallGuid是否存在
        boolean isContains = pref.contains("appInstallGuid");
        if (!isContains) {
            String uniqueID = UUID.randomUUID().toString();
            deviceInfo.setAppInstallGuid(uniqueID);

//            LocalDateTime和DateTimeFormatter在jdk8中新增，尽管解决了线程安全问题，但Android6以前使用的是jdk7，并不包含此类。故改用
//            故改用SimpleDateFormat替代即可。每次转换时间都new SimpleDateFormat也不会有线程问题。
//            LocalDateTime date = LocalDateTime.now();
//            deviceInfo.setAppInstallDate(DeviceInfoTextUtil.appInstallDateFomatter.format(date));

            String currentDateTimeString = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US).format(new Date());
            deviceInfo.setAppInstallDate(currentDateTimeString);


            Log.i(TAG, "create new  appInstallGuid!");
        }

        //遍历不为空的字段，并保存到SharedPreferences中。
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            try {
                Log.v(TAG, "属性名:" + field.getName() + " 属性值:" + field.get(deviceInfo));
                if (!TextUtils.isEmpty((String) field.get(deviceInfo))) {
                    editor.putString(field.getName(), (String) field.get(deviceInfo));
                }
            } catch (IllegalAccessException e) {
                Log.e(TAG, "Save to SharedPreference Fail!", e);
            }
        }
        editor.apply();
    }

    /**
     * 遍历SharedPreferences，组装为javabean.
     *
     * @param pref
     * @return
     */
    @Override
    public DeviceInfo load(SharedPreferences pref) {
        DeviceInfo deviceInfo = new DeviceInfo();

        //使用getAll可以返回所有可用的键值
        Map<String, ?> allMaps = pref.getAll();
        //打印保存的值
        for (Map.Entry<String, ?> a : allMaps.entrySet()) {
            Log.v(TAG, a.getKey() + ":" + a.getValue());

            try {
                setValue(deviceInfo, a.getKey(), allMaps.get(a.getKey()));
                Log.d(TAG, "update obj:" + deviceInfo);
            } catch (Exception e) {
                Log.e(TAG, "Save to SharedPreference Fail!", e);
            }
        }
        return deviceInfo;
    }


}
