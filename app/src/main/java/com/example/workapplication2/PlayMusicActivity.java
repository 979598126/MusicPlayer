package com.example.workapplication2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.workapplication2.adapter.ViewPagerAdapter1;
import com.example.workapplication2.adapter.ViewPagerAdapter2;
import com.example.workapplication2.entity.Music;
import com.example.workapplication2.fragment.AlbumImgFragment;
import com.example.workapplication2.fragment.SongWordsFragment;
import com.example.workapplication2.service.MusicService;
import com.example.workapplication2.utils.GetLocalMusicUtil;
import com.example.workapplication2.utils.WebUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PlayMusicActivity extends AppCompatActivity {
    AlbumImgFragment albumImgFragment;
    SongWordsFragment songWordsFragment;
    ViewPager2 vp;
    ViewPagerAdapter2 viewPagerAdapter2;

    SeekBar seekBar;
    TextView textView1, textView2, textView3, textView4;
    ImageView imageView1, imageView2, imageView3, imageView4, preBtn, playBtn, nextBtn;

    MusicService.MyBinder myBinder;
    Music music;
    int listID;


    private Handler handler = new Handler();
    //更新线程用于更新进度条
    private Runnable updateThread = new Runnable() {
        @Override
        public void run() {
            if (myBinder != null) {
                try {
                    if (myBinder.isPlaying()) {
                        // 设置当前播放时间
                        String currentTime=formatTime(myBinder.getCurrentPosition());
                        textView3.setText(currentTime);

                        int duration = myBinder.getDuration();
                        int currentPosition = myBinder.getCurrentPosition();
                        seekBar.setMax(duration);
                        seekBar.setProgress(currentPosition);
                        int nowTime = currentPosition / 1000;
                        int maxTime = duration / 1000;
                        if (nowTime == maxTime) {
                            myBinder.playNext();
                            updateUI();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            handler.post(updateThread);
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_music);
        Bundle bundle = getIntent().getExtras();
        myBinder = (MusicService.MyBinder) bundle.get("myBinder");
        listID=bundle.getInt("listID");

        //初始化视图
        initView();


//      设置专辑图片
        music = myBinder.getCurrentMusic();
        if(myBinder.isWeb()){
            albumImgFragment = new AlbumImgFragment(music.getImgUrl());
        }
        else{
            albumImgFragment = new AlbumImgFragment(music.getMusicImg());
        }


//      设置音乐信息
        textView1.setText(music.getMusicTitle());
        textView2.setText(music.getArtist());
//        格式化日期
        String time=formatTime(myBinder.getDuration());
        textView4.setText(time);

        if (myBinder.isPlaying()) {
            playBtn.setImageResource(R.drawable.ic_play_btn_pause);
        } else {
            playBtn.setImageResource(R.drawable.ic_play_btn_play);
        }
        handler.post(updateThread);

//        返回上一级页面
        returnMain();


//        设置ViewPager2
        setViewPager2();




//      音乐播放相关监听
        setMusicListener();

//        设置进度条变化监听
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged (SeekBar seekBar,int progress, boolean fromUser){

            }

            @Override
            public void onStartTrackingTouch (SeekBar seekBar){

            }

            @Override
            public void onStopTrackingTouch (SeekBar seekBar){
                if (myBinder != null) {
                    try {
                        myBinder.seekTo(seekBar.getProgress());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }


    //    =====================================
    //        初始化视图
    public void initView() {
        seekBar = findViewById(R.id.seekBar);//歌曲进度条

        textView1 = findViewById(R.id.textView10);//歌曲名
        textView2 = findViewById(R.id.textView11);//歌手名
        textView3 = findViewById(R.id.textView8);//播放时间
        textView4 = findViewById(R.id.textView9);//总时长

        imageView1 = findViewById(R.id.returnMain2);//返回键
        imageView2 = findViewById(R.id.imageView14);//左边的点
        imageView3 = findViewById(R.id.imageView15);//右边的点
        preBtn = findViewById(R.id.imageView13);//上一首
        playBtn = findViewById(R.id.imageView12);//播放键
        nextBtn = findViewById(R.id.imageView11);//下一首

    }

    //=============================================
    //        设置ViewPager2
    public void setViewPager2() {
        vp = findViewById(R.id.viewPage2);

        songWordsFragment = new SongWordsFragment();
        //        创建list，将Fragment添加到list中
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(albumImgFragment);
        fragments.add(songWordsFragment);

        vp.setOffscreenPageLimit(fragments.size() - 1);
        viewPagerAdapter2 = new ViewPagerAdapter2(this, fragments);
        vp.setAdapter(viewPagerAdapter2);

        //        设置页面滑动事件
        vp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    imageView2.setAlpha(1F);
                    imageView3.setAlpha(0.5F);
                } else {
                    imageView2.setAlpha(0.5F);
                    imageView3.setAlpha(1F);
                }
            }
        });

//        设置圆点点击切换Fragment
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vp.setCurrentItem(0);
                imageView2.setAlpha(1F);
                imageView3.setAlpha(0.5F);
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vp.setCurrentItem(1);
                imageView2.setAlpha(0.5F);
                imageView3.setAlpha(1F);
            }
        });
    }

//    ===================================
//      音乐播放相关监听
    public void setMusicListener(){
        //      播放和暂停歌曲
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myBinder.isPlaying()) {
                    playBtn.setImageResource(R.drawable.ic_play_btn_play);
                } else {
                    playBtn.setImageResource(R.drawable.ic_play_btn_pause);
                }
                myBinder.play();
            }
        });

//        上一首
        preBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=myBinder.playPre();
                if(myBinder.isWeb()){
                    WebUtil.setMusicUrl(myBinder.getWebMusicID(position),myBinder);
                }
//                更新UI
                playBtn.setImageResource(R.drawable.ic_play_btn_pause);
                updateUI();
            }
        });
//        下一首
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=myBinder.playNext();
                if(myBinder.isWeb()){
                    WebUtil.setMusicUrl(myBinder.getWebMusicID(position),myBinder);
                }
//                更新UI
                playBtn.setImageResource(R.drawable.ic_play_btn_pause);
                updateUI();
            }
        });
    }

//    =================================
//    格式化日期
    public String formatTime(int time){
        Date date=new Date();
        date.setTime(time);
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String formatTime=sdf.format(date);
        return formatTime;
    }
//    =================================
//    更新正在播放的音乐信息
    public void updateUI(){
        music=myBinder.getCurrentMusic();
        if(myBinder.isWeb()){
            albumImgFragment.setWebMusicUrl(music.getImgUrl());
        }
        else {
            albumImgFragment.setBm(music.getMusicImg());
        }
        textView1.setText(music.getMusicTitle());
        textView2.setText(music.getArtist());
        //        格式化日期
        String time=formatTime(myBinder.getDuration());
        textView4.setText(time);
    }

//  =================================
//    返回上一级页面
    public void returnMain(){
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                Bundle bundle=new Bundle();
                bundle.putBinder("myBinder",myBinder);
                if(myBinder.isWeb()){
                    intent = new Intent(PlayMusicActivity.this,TopListActivity.class);
                    bundle.putInt("listID",listID);
                }
                else{
                    intent = new Intent(PlayMusicActivity.this, MainActivity.class);
                }
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
