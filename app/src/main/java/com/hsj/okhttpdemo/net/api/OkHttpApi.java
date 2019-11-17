package com.hsj.okhttpdemo.net.api;

import com.hsj.okhttpdemo.net.NetConstants;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Create by hsj55
 * 2019/11/17
 */
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
