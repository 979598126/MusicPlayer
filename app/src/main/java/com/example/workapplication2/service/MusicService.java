package com.example.workapplication2.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;


import androidx.annotation.Nullable;

import com.example.workapplication2.entity.Music;

import java.io.IOException;
import java.util.List;

public class MusicService extends Service {
    private List<Music> musicList;//本地音乐列表
    private List<Music> webMusicList;//网络音乐列表
    private int position=0;//当前歌曲在音乐列表中的位置
    private MediaPlayer player;//音乐播放器

    public void setWebMusicList(List<Music> webMusicList) {
        this.webMusicList = webMusicList;
    }
    public void setMusicList(List<Music> musicList) {
        this.musicList = musicList;
    }
    public void setPosition(int position) {
        this.position = position;
    }
    public int getPosition() {
        return position;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }
//    初始化
    public void init(){
        player=new MediaPlayer();
        try {
            player.setDataSource("/storage/08F0-1401/Music/不能说的秘密 - First Kiss [mqms2].mp3");
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class MyBinder extends Binder{
        private int seekLength;//进度条长度
        private String webMusicUrl;//网络音乐url
        private boolean isWebMusic=false;//是否为网络音乐

        public void setWebMusicUrl(String webMusicUrl) {
            this.webMusicUrl = webMusicUrl;
        }
        public void setWebMusic(boolean webMusic) {
            isWebMusic = webMusic;
        }

        //获取所属的Service对象
        public Service getService(){
            return MusicService.this;
        }

//        判断当前播放音乐是否为网络音乐
        public boolean isWeb(){
            return isWebMusic;
        }

        //判断歌曲是否播放
        public boolean isPlaying(){
        return player.isPlaying();
    }

        //根据歌曲列表位置播放歌曲
        public void startPlay() {
            player.reset();
            try {
                if(isWebMusic){
                    player.setDataSource(webMusicUrl);
                }
                else{
                    player.setDataSource(musicList.get(position).getData());
                }
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            seekLength=0;
            player.seekTo(seekLength);
            player.start();
        }

//        上一首
        public int playPre() {
            position -= 1;
            seekLength=0;
            if(isWebMusic){
                if (position < 0){
                    position = webMusicList.size()-1;
                }
            }
            else{
                if(position<0){
                    position = musicList.size()-1;
                }

                startPlay();

            }
            return position;
        }

//        下一首
        public int playNext() {
            position += 1;
            seekLength=0;
            if(isWebMusic){
                if (position >= webMusicList.size()){
                    position = 0;
                }
            }
            else{
                if(position >= musicList.size()){
                    position=0;
                }
                startPlay();
            }
            return position;
        }

        //        播放和暂停歌曲
        public void play(){
            if(player.isPlaying()){
                player.pause();
                seekLength = player.getCurrentPosition();
            }
            else {
                player.seekTo(seekLength);
                player.start();
            }
        }

        public int getWebMusicID(int position){
            return webMusicList.get(position).getMusicID();
        }

        //返回歌曲的长度，单位为毫秒
        public int getDuration(){
            return player.getDuration();
        }

        //返回歌曲目前的进度，单位为毫秒
        public int getCurrentPosition(){
            return player.getCurrentPosition();
        }

        //设置歌曲播放的进度，单位为毫秒
        public void seekTo(int length){
            seekLength=length;
            player.seekTo(length);
        }

        //获取当前播放的音乐信息
        public Music getCurrentMusic(){
            if(isWebMusic){
                return webMusicList.get(position);
            }
                return musicList.get(position);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 在activity结束的时候回收资源
        if (player != null && player.isPlaying()) {
            player.stop();
            player.release();
            player = null;
        }
    }
}
