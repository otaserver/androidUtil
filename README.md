# androidUtil
总结的一些android常用的工具类

DeviceInfoUtil：采集设备手机号，app安装的unid，app安装时间等设备信息，并以gson或文本格式保存。



## 集成方法
此项目在gradle中定义task makeReleaseJar(),可以在'build/libs'目录下生成com.otaserver.androidUtil.jar。供其他项目引用。
集成示例参考,第一行代码第6章。



### Android版本支持

支持android 6.0以上版本，即jdk7以上。



###  修改历史

0.0.5

1.MIN_SDK_VERSION由26改为23，取消使用jdk8中新加入的java.time.LocalDateTime和java.time.format.DateTimeFormatter而改为使用java.util.Date和java.text.SimpleDateFormat。以支持android 6.0版本。
