package com.example.workapplication2.utils;


import android.media.MediaPlayer;
import android.widget.ImageView;

import com.example.workapplication2.R;


public class PlayMusicUtil {
    public static int startMusic(MediaPlayer mediaPlayer,ImageView imageView){
        imageView.setImageResource(R.drawable.ic_play_bar_btn_pause);
        mediaPlayer.start();
        return 1;
    }

    public static int pauseMusic(MediaPlayer mediaPlayer,ImageView imageView){
        imageView.setImageResource(R.drawable.ic_play_bar_btn_play);
        mediaPlayer.pause();
        return 0;
    }

    public void stopMusic(){

    }
}
