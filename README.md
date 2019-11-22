[同步blog链接：基于okhttp3+retrofit2封装的网络库(含websocket封装)](https://blog.csdn.net/hshuaijun55/article/details/103109630)

# 背景
封装的网络库基于**okhttp3+retrofit2+rxandroid+rxjava**。目的是单独封装处理网络请求，可供项目中多个module使用，所以Demo中代码模块拆的比较细。包含**hsjokhttp(网络库封装)、hsjlogger(日志打印)、bean(对象模块)**，只需要关注**hsjokhttp(网络库封装)** 模块，其余两模块都比较简单。库中包含websocket封装部分，详细信息请点击跳转至 [基于OKHttp的websocket封装使用](https://blog.csdn.net/hshuaijun55/article/details/103206152) 。

# 接入方式
## 1.导入hsjokhttp module。
1. 注意module中gradle文件里，对okhttp等一系列第三方库的依赖不要丢。
```java
implementation 'com.squareup.okhttp3:okhttp:4.2.2'
    implementation "io.reactivex.rxjava2:rxjava:2.2.0"
    implementation "io.reactivex.rxjava2:rxandroid:2.1.0"
    implementation 'com.squareup.retrofit2:retrofit:2.6.2'
    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.6.2'
    implementation 'com.squareup.retrofit2:converter-gson:2.6.2'
```
其中依赖的版本号，可结合自己项目的实际情况更新。日志打印module可以去掉，或者自己调整。我在网络库中依赖它是因为这个logger支持长文本及格式化打印网络请求等信息，查看非常方便。
  
2. 因为网络库中封装了CacheInterceptor，这个Interceptor里面有判断网络连接状态，所以需要在module中AndroidManifest.xml文件添加ACCESS_NETWORK_STATE权限。
```java
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.hsj.okhttp" >
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
</manifest>
```
## 2.接入使用
1. 在项目的app(主) module的gradle文件中依赖网络库及其他相关库。
```java
implementation "io.reactivex.rxjava2:rxjava:2.2.0"
    implementation "io.reactivex.rxjava2:rxandroid:2.1.0"
    implementation 'com.squareup.okhttp3:okhttp:4.2.2'
    implementation 'com.squareup.retrofit2:retrofit:2.6.2'
    implementation 'com.squareup.retrofit2:converter-gson:2.6.2'
    implementation project(path: ':hsjokhttp')
    implementation project(path: ':bean')
    implementation project(path: ':hsjlogger')
```
2. 新建OkHttpApi接口文件，定义各网络请求。Demo中的示例支持多种请求方式。**Get、Post(body为JSON格式、body为Form形式)、文件上传** 等示例。
```java
public interface OkHttpApi {

    //get请求方式
    @GET(NetConstants.URL_GET_TOKEN)
    Observable<ResponseBody> getToken();

    //获取短信验证码
    @POST("code/sms")
    @FormUrlEncoded
    Observable<ResponseBody> getCodeSms(@FieldMap Map<String, String> bean);

    //某维度下的标签
    @POST("story/tag/dimension/notlogin")
    Observable<ResponseBody> getTag(@Body RequestBody bean);

    //上传头像图片
    @POST("helper/upload/file/resource")
    Observable<ResponseBody> uploadImgFile(@Body MultipartBody multipartBody);
}
```
3. 新建BaseSubscribe文件，主要是基于网络库创建client，生成实例化网络请求的API。具体请看完整示例代码。
```java
public class BaseSubscribe {
    public static MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TOKEN = "";
    private static final String USERID = "";

    private static OkHttpApi okHttpApi;

    public static OkHttpApi getOkHttpApi() {
        if (okHttpApi==null) {
            okHttpApi = newApi(NetConstants.BASE_URL, getHeaderParams(TOKEN, USERID), OkHttpApi.class);
        }
        return okHttpApi;
    }

    /**
     * 关键方法，可以根据需求产出不同的api
     * @param baseUrl
     * @param params
     * @param apiClass
     * @param <T>
     * @return
     */
    public static <T>T newApi(String baseUrl, Map<String, String> params, Class<T> apiClass) {
        NetBuilder builder = new NetBuilder()
                .setConnectTimeout(HsjNetConstants.DEFAULT_CONNECT_TIMEOUT)
                .setReadTimeout(HsjNetConstants.DEFAULT_READ_TIMEOUT)
                .setWriteTimeout(HsjNetConstants.DEFAULT_WRITE_TIMEOUT)
                .setRetryOnConnectionFailure(true);
        //添加header
        if (params != null && params.size()>0) {
            HeaderInterceptor headerInterceptor = new HeaderInterceptor(params);
            builder.addInterceptor(headerInterceptor);
        }
        //添加缓存
        File cacheFile = new File(App.getAppContext().getExternalCacheDir(), HsjNetConstants.CACHE_NAME);
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        builder.addCache(cache);

        //添加缓存拦截器
        CacheInterceptor cacheInterceptor = new CacheInterceptor(App.getAppContext());
        builder.addInterceptor(cacheInterceptor);

//        builder.addNetInterceptor(new NetInterceptor());

        return builder.build(baseUrl).create(apiClass);
    }

    private static Map<String, String> getHeaderParams(String token, String userId) {
        String timestamp, signature;

        timestamp = String.valueOf(System.currentTimeMillis() / 1000);

        Map<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(token)) {
            params.put("token", token);
        }
        if (!TextUtils.isEmpty(userId) && !"null".equals(userId)) {
            params.put("userId", userId);
        }
        params.put("nounce", "");
        params.put("timestamp", timestamp);
        signature = "";
        params.put("signature", signature);

        return params;
    }

    /**
     * 设置订阅 和 所在的线程环境
     * @param o
     * @param s
     */
    public static void toSubscribe(Observable<ResponseBody> o, DisposableObserver<ResponseBody> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retry(1)//请求失败重连次数
                .subscribe(s);
    }
```
需要注意的是newApi方法，这个方法里面定义了网络请求中各种参数添加设置。比如**连接等超时、通过拦截器方式添加header信息、缓存文件及大小、缓存拦截器、网络拦截器**等。这个地方可以根据自己的实际需求设置。
若有多个api文件，则需要在此注册多个api。
header信息，我单独封装了一个方法，以Map形式传入拦截器，一般可以在此添加一些signature信息。

4. 封装HsjSubscribe文件，这个是基于api，定义自己的网络请求方法，一般与api中网络请求接口一一对应。
```java
public class HsjSubscribe {
    /**
     * 获取标签
     * @param subscriber
     */
    public static void getTag(DisposableObserver<ResponseBody> subscriber) {
        Map<String,String> map = new HashMap<>();
        map.put("dimension", "4");
        map.put("language", "zh_CN");
        Observable<ResponseBody> observable = BaseSubscribe.getOkHttpApi().getTag(RequestBody.create(new Gson().toJson(map), BaseSubscribe.JSON));
        BaseSubscribe.toSubscribe(observable, subscriber);
    }

    public static void getCodeSms(String mobile, DisposableObserver<ResponseBody> subscriber) {
        Map<String,String> map = new HashMap<>();
        map.put("mobile", mobile);
        Observable<ResponseBody> observable = BaseSubscribe.getOkHttpApi().getCodeSms(map);
        BaseSubscribe.toSubscribe(observable, subscriber);
    }

    /**
     * 获取标签
     * @param subscriber
     */
    public static void getToken(DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = BaseSubscribe.getOkHttpApi().getToken();
        BaseSubscribe.toSubscribe(observable, subscriber);
    }

    /**
     * 上传头像接口
     * type 1=image 2=audio
     */
    public static void uploadFileToGetUrl(int type, File file, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = BaseSubscribe.getOkHttpApi().uploadImgFile(filesToMultipartBody(file, type));
        BaseSubscribe.toSubscribe(observable, subscriber);
    }

    private static MultipartBody filesToMultipartBody(File file, int type) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody requestBody = RequestBody.create(file, MediaType.parse(type == 1 ? "image/*" : "audio/*"));
        builder.addFormDataPart("file", file.getName(), requestBody);
        return builder.build();
    }
}
```
5. 定义网络请求回调类BaseCallback，此类继承自网路库中NetCallback，已对请求返回回的参数类型做泛型处理。一般一些需要统一处理的逻辑可以在此处理，比如token失效后需要退出登录等。
```java
public abstract class BaseCallback<T> extends NetCallback<T> {
    /**
     * 统一处理的逻辑，都在类似这样的方法中处理。暴露在每个最外层的callback只处理指定场景下的业务逻辑。
     */
    @Override
    public void logout() {

    }
}
```
6. 网络请求。
```java
	public void getToken(View view) {
        HsjLogger.d("getToken");
        HsjSubscribe.getToken(new BaseCallback<Token>() {
            @Override
            public void onSuccess(Token result) {
                HsjLogger.d("--getToken--onSuccess--");
            }

            @Override
            public void onFault(int code, String errorMsg) {
                HsjLogger.d("--getToken--onFault--");
            }

            @Override
            public void netError() {
                HsjLogger.d("--getToken--netError--");
            }
        });
    }

    public void uploadFile(View view) {
        File file = new File(getExternalFilesDir(null) + File.separator + "image/icon_weixin_kefu.png");
        HsjSubscribe.uploadFileToGetUrl(1, file, new BaseCallback<FileUploadResponse>() {
            @Override
            public void onSuccess(FileUploadResponse result) {
                HsjLogger.d("--upload--onSuccess--");
            }

            @Override
            public void onFault(int code, String errorMsg) {
                HsjLogger.d("--upload--onFault--");
            }

            @Override
            public void netError() {
                HsjLogger.d("--upload--netError--");
            }
        });
    }

    public void jsonRequest(View view) {
        HsjSubscribe.getTag(new BaseCallback<List<Tag>>() {

            @Override
            public void onSuccess(List<Tag> result) {
                HsjLogger.d("--onSuccess--");
            }

            @Override
            public void onFault(int code, String errorMsg) {
                HsjLogger.d("--onFault--" + errorMsg);
            }

            @Override
            public void netError() {
                HsjLogger.d("--netError--");
            }
        });
    }

    public void formRequest(View view) {
        HsjSubscribe.getCodeSms("13079079998", new BaseCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                HsjLogger.d("--login--onSuccess--");
            }

            @Override
            public void onFault(int code, String errorMsg) {
                HsjLogger.d("--login--onFault--");
            }

            @Override
            public void netError() {
                HsjLogger.d("--login--netError--");
            }
        });
    }
```
以上示例分别是Demo中各类请求示例。如果一个统一的返回体格式是** ** ，目前的封装已处理到data层级，所以直接传入回调需要的类型即可。

7. 记得在主工程AndroidManifest.xml文件中添加网络请求等权限。
```java
<uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```
 
