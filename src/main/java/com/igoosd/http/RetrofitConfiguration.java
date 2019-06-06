package com.igoosd.http;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * 2017/8/24.
 */
@Configuration
public class RetrofitConfiguration {

    @Value("${retrofit.base-url}")
    private String retrofitBaseUrl;

    @Value("${retrofit.timeout}")
    private long timeout;

    @Bean
    public Retrofit buildRetrofitInstance() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(timeout,TimeUnit.SECONDS);
        builder.readTimeout(timeout, TimeUnit.SECONDS);
        builder.writeTimeout(timeout,TimeUnit.SECONDS);
        OkHttpClient client =builder.build();
        return new Retrofit.Builder()
                .baseUrl(retrofitBaseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Bean
    public HttpService createHttpService(){
        return buildRetrofitInstance().create(HttpService.class);
    }

}
