package com.example.websocketapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.CompletableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private SimpleAdapter mAdapter;
    private List<String> mDataSet = new ArrayList<>();
    private StompClient mStompClient;
    private Disposable mRestPingDisposable;
    private final SimpleDateFormat mTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    private RecyclerView mRecyclerView;
    private Gson mGson = new GsonBuilder().create();

    private CompositeDisposable compositeDisposable;

//    public static final String SERVER_IP = "10.19.36.16";
    public static final String SERVER_IP = "192.168.0.42";
    public static final String SERVER_PORT = "28080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recycler_view);
        mAdapter = new SimpleAdapter(mDataSet);
        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
    }

    public void disconnectStomp(View view) {
        mStompClient.disconnect();
    }

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String USER_AUTH_HEADER = "Authorization";
    @SuppressLint("CheckResult")
    public void connectStomp(View view) {

        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader(USERNAME, "compinstaller"));
        headers.add(new StompHeader(PASSWORD, "Password1!"));
        headers.add(new StompHeader(USER_AUTH_HEADER, "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJIa19EZUlSRkpiMjVuNjJyUzBnZHE0eUVmc0l2bElmaXBsMzNEcTY1cU1vIn0.eyJleHAiOjE2NjUxNzk2MDQsImlhdCI6MTY2MjU4NzYwNCwianRpIjoiMGMwNzlhMjctMzU5OS00M2VhLWJkNjctODkxM2U3ZWFhMjQ3IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDoyODE4MS9yZWFsbXMvZ292c2hpZWxkLXJlYWxtIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjQ3YTg2NTMyLTgwNTItNGJjMS1hMjQ5LTcyMmNkYmRiZTEzNyIsInR5cCI6IkJlYXJlciIsImF6cCI6ImdvdnNoaWVsZC1zZXJ2aWNlLWNsaWVudCIsInNlc3Npb25fc3RhdGUiOiI2YzEyMmZmMS02Yzg4LTRkZGYtYmRlOC1kNTQwN2Y5MjA4YTQiLCJhY3IiOiIxIiwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtZ292c2hpZWxkLXJlYWxtIiwib2ZmbGluZV9hY2Nlc3MiLCJBRE1JTiIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgcHJvZmlsZSBlbWFpbCBvZmZsaW5lX2FjY2VzcyIsInNpZCI6IjZjMTIyZmYxLTZjODgtNGRkZi1iZGU4LWQ1NDA3ZjkyMDhhNCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwicHJlZmVycmVkX3VzZXJuYW1lIjoiY29tcGluc3RhbGxlciJ9.P9kFMIxhs_D-HchdYDCQqGjeIVpnDu7gwFrVlej_d-c_0aqOMCVl-2oRoFdBSzOlL51r1cKIEjb1mP09h6NY72aFmxgXmx3TRJhpAXs21hGtdHQTe_dc_ZPGirq6zae9FT5AXWh3r0UJrb3vubT9YcnINqWgB73PeusGXutKB0kuAY2cv-sGFjDAORQcTwP3Y7N_bChEYMWZ3RZI26bFjUx0wsbOAgROSc4GFmJTJKU1gu9D-EQXJQQNm2lBuqf7M91i41r5No0uYuMAifsxuI4Mmr-OHqkygxJwsnKJqDYqx8T1B0NRAP1asYpn2LQAm82FnrJT3VqFne68wZsBkQ"));

        Map<String, String> headers2 = new HashMap<>();
        headers2.put(USERNAME, "compinstaller");
        headers2.put(PASSWORD, "Password1!");
        headers2.put(USER_AUTH_HEADER, "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJIa19EZUlSRkpiMjVuNjJyUzBnZHE0eUVmc0l2bElmaXBsMzNEcTY1cU1vIn0.eyJleHAiOjE2NjUxNzk2MDQsImlhdCI6MTY2MjU4NzYwNCwianRpIjoiMGMwNzlhMjctMzU5OS00M2VhLWJkNjctODkxM2U3ZWFhMjQ3IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDoyODE4MS9yZWFsbXMvZ292c2hpZWxkLXJlYWxtIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjQ3YTg2NTMyLTgwNTItNGJjMS1hMjQ5LTcyMmNkYmRiZTEzNyIsInR5cCI6IkJlYXJlciIsImF6cCI6ImdvdnNoaWVsZC1zZXJ2aWNlLWNsaWVudCIsInNlc3Npb25fc3RhdGUiOiI2YzEyMmZmMS02Yzg4LTRkZGYtYmRlOC1kNTQwN2Y5MjA4YTQiLCJhY3IiOiIxIiwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtZ292c2hpZWxkLXJlYWxtIiwib2ZmbGluZV9hY2Nlc3MiLCJBRE1JTiIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgcHJvZmlsZSBlbWFpbCBvZmZsaW5lX2FjY2VzcyIsInNpZCI6IjZjMTIyZmYxLTZjODgtNGRkZi1iZGU4LWQ1NDA3ZjkyMDhhNCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwicHJlZmVycmVkX3VzZXJuYW1lIjoiY29tcGluc3RhbGxlciJ9.P9kFMIxhs_D-HchdYDCQqGjeIVpnDu7gwFrVlej_d-c_0aqOMCVl-2oRoFdBSzOlL51r1cKIEjb1mP09h6NY72aFmxgXmx3TRJhpAXs21hGtdHQTe_dc_ZPGirq6zae9FT5AXWh3r0UJrb3vubT9YcnINqWgB73PeusGXutKB0kuAY2cv-sGFjDAORQcTwP3Y7N_bChEYMWZ3RZI26bFjUx0wsbOAgROSc4GFmJTJKU1gu9D-EQXJQQNm2lBuqf7M91i41r5No0uYuMAifsxuI4Mmr-OHqkygxJwsnKJqDYqx8T1B0NRAP1asYpn2LQAm82FnrJT3VqFne68wZsBkQ");

        if (mStompClient != null && mStompClient.isConnected()) {
            mStompClient.disconnect();
            Log.i("Info", "Disconnected from server");
        }

//        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://" + RestClient.SERVER_IP
//                + ":" + RestClient.SERVER_PORT + "/mdm-communication/websocket");

        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://" + RestClient.SERVER_IP
                + ":" + RestClient.SERVER_PORT + "/MDM-ws-war/mdm-communication/websocket", headers2);

        resetSubscriptions();

        mStompClient.withClientHeartbeat(1000).withServerHeartbeat(1000);
        Disposable dispLifecycle = mStompClient.lifecycle()
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Log.e(TAG, "Stomp connection opened");
                            Disposable dispTopic = mStompClient.topic("/topic/messages")
                                    .subscribe(topicMessage -> {
                                        Log.d(TAG, "Received " + topicMessage.getPayload());
                                        addItem(mGson.fromJson(topicMessage.getPayload(), ContentModel.class));
                                    }, throwable -> {
                                        Log.e(TAG, "Error on subscribe topic", throwable);
                                    });
                            compositeDisposable.add(dispTopic);
                            break;
                        case ERROR:
                            Log.e(TAG, "Stomp connection error", lifecycleEvent.getException());
                            break;
                        case CLOSED:
                            Log.e(TAG, "Stomp connection closed");
                            resetSubscriptions();
                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            Log.e(TAG, "Stomp failed server heartbeat");
                            break;
                    }
                });
        compositeDisposable.add(dispLifecycle);

        mStompClient.connect(headers);
    }

    public void sendEchoViaStomp(View v) {
        if (!mStompClient.isConnected()) return;

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("messageContent", "Echo STOMP " + mTimeFormat.format(new Date()));
        mStompClient.send("/app/message", jsonObject.toString())
                .compose(applySchedulers())
                .subscribe(() -> {
                    Log.d(TAG, "STOMP echo send successfully");
                }, throwable -> {
                    Log.e(TAG, "Error send STOMP echo", throwable);
                    toast(throwable.getMessage());
                });
    }

    public void sendEchoViaRest(View v) {
        mRestPingDisposable = RestClient.getInstance().getExampleRepository()
                .sendRestEcho("Echo REST " + mTimeFormat.format(new Date()))
                .compose(applySchedulers())
                .subscribe(() -> {
                    Log.d(TAG, "REST echo send successfully");
                }, throwable -> {
                    Log.e(TAG, "Error send REST echo", throwable);
                    toast(throwable.getMessage());
                });
    }

    private void addItem(ContentModel contentModel) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Stuff that updates the UI
                mDataSet.add(contentModel.getContent() + " - " + mTimeFormat.format(new Date()));
                mAdapter.notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(mDataSet.size() - 1);
            }
        });

    }

    private void toast(String text) {
        Log.i(TAG, text);
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    protected CompletableTransformer applySchedulers() {
        return upstream -> upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onDestroy() {
        mStompClient.disconnect();

        if (mRestPingDisposable != null) mRestPingDisposable.dispose();
        if (compositeDisposable != null) compositeDisposable.dispose();
        super.onDestroy();
    }
}
