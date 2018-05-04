package com.mgp.common.utils;


import okhttp3.*;
import redis.clients.jedis.Jedis;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import java.io.IOException;
import java.util.Properties;

import com.mgp.commons.beans.easemob.EasemobSignUpBean;

public class FileUploadManager {
    static Retrofit mRetrofit;

    public static Retrofit retrofit() {
    	Properties pro = PropertiesUtils.getInfoConfigProperties();
    	String ORG_NAME = pro.getProperty("easemob.orgname");
    	String APP_NAME = pro.getProperty("easemob.appid");
    	String url = "https://a1.easemob.com/"+ORG_NAME+"/"+APP_NAME+"/";
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(genericClient())
                    .build();
        }
        return mRetrofit;
    }

    public static OkHttpClient genericClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                    	Properties pro = PropertiesUtils.getInfoConfigProperties();
                    	String accessToken = pro.getProperty("easemob.AccessToken");
                    	String bearer = "Bearer "+accessToken;
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Authorization", bearer)
                                .addHeader("restrict-access", "true")
                                .build();
                        return chain.proceed(request);
                    }
                }).build();
        return okHttpClient;
    }


    public interface FileUploadService {

        /**
         * 单文件上传
         * @param file
         * @return
         */
        @Multipart
        @POST("chatfiles")
        Call<ResponseBody> upload(@Part MultipartBody.Part file);

    }

/*    public static void main(String[] args){
        File file = new File("d:/test.jpg");
        if(!file.exists()){
        	System.out.println("no exists");
        }
        FileUploadManager.FileUploadService service = FileUploadManager.retrofit().create(FileUploadManager.FileUploadService.class);

        final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Call<ResponseBody> call = service.upload(body);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
            	try {
					Jedis jedis = new Jedis();
					jedis.auth("Alex2017!");
					jedis.set("key", response.body().string());
					System.out.println("haha: "+jedis.get("key"));
				} catch (IOException e) {
					e.printStackTrace();
				}
                if (response.isSuccessful()) {
                    System.out.print("success");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }*/
}