package com.otaserver.android.dao;

/**
 * 定义能够采集的设备信息条目。
 * @version 0.1
 * @date   2020-05-23
 */
public class DeviceInfo {

    //无需权限，一定存在,是由函数产生的，并非读取系统设置值。
    private String appInstallGuid;
    private String appInstallDate;

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

    //已过时
    private String deviceId;

    public String getAppInstallDate() {
        return appInstallDate;
    }

    public void setAppInstallDate(String appInstallDate) {
        this.appInstallDate = appInstallDate;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getAppInstallGuid() {
        return appInstallGuid;
    }

    public void setAppInstallGuid(String appInstallGuid) {
        this.appInstallGuid = appInstallGuid;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getImei0() {
        return imei0;
    }

    public void setImei0(String imei0) {
        this.imei0 = imei0;
    }

    public String getImei1() {
        return imei1;
    }

    public void setImei1(String imei1) {
        this.imei1 = imei1;
    }

    public String getMeid() {
        return meid;
    }

    public void setMeid(String meid) {
        this.meid = meid;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public String getSimSerialNumber() {
        return simSerialNumber;
    }

    public void setSimSerialNumber(String simSerialNumber) {
        this.simSerialNumber = simSerialNumber;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    /**
     * @return
     * @deprecated
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * @param deviceId
     * @deprecated
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "appInstallGuid='" + appInstallGuid + '\'' +
                ", appInstallDate='" + appInstallDate + '\'' +
                ", androidId='" + androidId + '\'' +
                ", serial='" + serial + '\'' +
                ", imei0='" + imei0 + '\'' +
                ", imei1='" + imei1 + '\'' +
                ", meid='" + meid + '\'' +
                ", tel1='" + tel1 + '\'' +
                ", tel2='" + tel2 + '\'' +
                ", simSerialNumber='" + simSerialNumber + '\'' +
                ", imsi='" + imsi + '\'' +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }

}
