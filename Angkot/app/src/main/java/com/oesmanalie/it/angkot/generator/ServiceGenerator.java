package com.oesmanalie.it.angkot.generator;

import com.oesmanalie.it.angkot.Constant;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceGenerator {

    private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl( Constant.BASE_URL )
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private ServiceGenerator() {}

    public static <Service> Service createService(Class<Service> serviceClass) {
        if (!httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging);
        }
        builder.client(httpClient.build());
        retrofit = builder.build();
        return retrofit.create(serviceClass);
    }

}
