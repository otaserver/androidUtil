package com.otaserver.android.util;

import android.content.SharedPreferences;

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

}
