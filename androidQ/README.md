### Android 10 分区适配

#### http请求适配
    在程序的targetSdkVersion >= 29时,http请求是被静止的,在访问的时候回抛出如下异常。
    java.net.UnknownServiceException: CLEARTEXT communication to xxx not permitted by network security policy
    
    解决方案1:
        需要在AndroidManifest.xml文件的application节点添加以下代码:
        <application android:usesCleartextTraffic="true">
        
    解决方案2:
        在res文件夹下新建network_config(名字任意取)文件,xml内容配置以下代码:
        <?xml version="1.0" encoding="utf-8"?>
        <network-security-config>
            <base-config cleartextTrafficPermitted="true" />
        </network-security-config>
        
        然后在AndroidManifest.xml文件的application节点添加以下代码:
        android:networkSecurityConfig="@xml/network_config"