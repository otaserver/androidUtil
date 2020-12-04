package com.otaserver.android.dao;

import lombok.Data;

/**
 * 定义能够采集的设备信息条目。
 *
 * @version 0.1
 * @date 2020-05-23
 */
@Data
public class DeviceInfo {

    //无需权限，一定存在,是由函数产生的，并非读取系统设置值。
    private String appInstallGuid;
    private String appInstallDate;

    //用户当前主机的android版本。
    private int androidApiLevel;

    //无需权限，基本保证存在
    private String androidId;
    private String serial;

    //需要权限，
    private String imei0;
    private String imei1;
    private String meid;
    private String tel1;
    private String tel2;
    private String simSerialNumber;
    private String imsi;

    //已过时,注意是大小写敏感的
    @Deprecated
    private String deviceId;

}
