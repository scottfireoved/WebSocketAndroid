package com.example.websocketapp;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Naik on 24.02.17.
 */
public class RestClient {

//    public static final String SERVER_IP = "10.19.36.16";
    public static final String SERVER_IP = "192.168.0.42";
    public static final String SERVER_PORT = "28080";

    private static RestClient instance;
    private static final Object lock = new Object();

    public static RestClient getInstance() {
        RestClient instance = RestClient.instance;
        if (instance == null) {
            synchronized (lock) {
                instance = RestClient.instance;
                if (instance == null) {
                    RestClient.instance = instance = new RestClient();
                }
            }
        }
        return instance;
    }

    private final ExampleRepository mExampleRepository;

    private RestClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://" + SERVER_IP + ":" + SERVER_PORT + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mExampleRepository = retrofit.create(ExampleRepository.class);
    }

    public ExampleRepository getExampleRepository() {
        return mExampleRepository;
    }
}
