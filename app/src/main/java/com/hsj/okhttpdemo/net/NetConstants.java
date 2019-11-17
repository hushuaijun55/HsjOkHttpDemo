package com.hsj.okhttpdemo.net;

/**
 * Create by hsj55
 * 2019/11/17
 */
public class NetConstants {
    //是否用线上连接
    public static final boolean IS_ONLINE_URL = false;

    //版本号
    public static final String VERSION_CODE = "v1";

    public static final String BASE_URL = "http://192.168.1.23:9401/"+VERSION_CODE+"/";

    //获取token的参数clientId
    public static final String CLIENT_ID = IS_ONLINE_URL ? "2xxxxxx" : "1exxxxxx";
    //获取token的参数clientSecret
    public static final String CLIENT_SECRET = IS_ONLINE_URL ? "0xxxxxx" : "3xxxxx";
    //获取token的url
    public static final String URL_GET_TOKEN = "oauth/2.0/token?grant_type=client_credentials&client_secret=" + CLIENT_SECRET + "&client_id=" + CLIENT_ID;
}
