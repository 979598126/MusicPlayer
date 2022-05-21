package com.example.workapplication2.utils;

import androidx.annotation.NonNull;

import com.example.workapplication2.service.MusicService;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WebUtil {
    static OkHttpClient client=new OkHttpClient();

    //    获取并设置网络歌曲URL
    public static void setMusicUrl(int musicID, MusicService.MyBinder myBinder){

        Request request=new Request.Builder()
                .url("http://music.eleuu.com/song/url?id="+musicID)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                String json=response.body().string();
                String musicUrl=GetOnlineMusicUtil.getMusicUrl(json);
                myBinder.setWebMusicUrl(musicUrl);
                myBinder.startPlay();
            }
        });

    }
}
