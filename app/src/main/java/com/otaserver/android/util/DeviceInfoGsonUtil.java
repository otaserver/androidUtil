package com.otaserver.android.util;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.otaserver.android.dao.DeviceInfo;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

//import java.time.LocalDateTime;


/**
 * 将android设备信息以json格式保存在SharedPreference中。
 *
 * @author scott
 */
public class DeviceInfoGsonUtil extends DeviceInfoUtil {

    private static final String TAG = "DeviceInfoGsonUtil";

    private static final String GSON_KEY = "gson";

    private Gson gson = new Gson();

    /**
     * 以gson为关键字保存在sharedPreference中。
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
//            deviceInfo.setAppInstallDate(DeviceInfoGsonUtil.appInstallDateFomatter.format(date));

            String currentDateTimeString = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US).format(new Date());
            deviceInfo.setAppInstallDate(currentDateTimeString);

            Log.i(TAG, "create new  appInstallGuid!");
        }


        //和已保存的对象做一个对象合并后再保存。
        DeviceInfo deviceInfoSaved = load(pref);

        //遍历不为空的字段，并保存到SharedPreferences中。
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            try {
                Log.v(TAG, "属性名:" + field.getName() + " 属性值:" + field.get(deviceInfoSaved));
                if (!TextUtils.isEmpty((String) field.get(deviceInfoSaved))) {
                    setValue(deviceInfo, field.getName(), field.get(deviceInfoSaved));
                }
            } catch (IllegalAccessException e) {
                Log.e(TAG, "Save to SharedPreference Fail!", e);
            } catch (Exception e) {
                Log.e(TAG, "Save to SharedPreference Fail!", e);
            }
        }
        editor.putString(GSON_KEY, gson.toJson(deviceInfo));
        editor.apply();
    }


    /**
     * 用Gson转换为对象。
     *
     * @param pref
     * @return
     * @
     */
    @Override
    public DeviceInfo load(SharedPreferences pref) {
        String gsonInSharedP = pref.getString(GSON_KEY, " ");
        return gson.fromJson(gsonInSharedP, DeviceInfo.class);
    }

}
