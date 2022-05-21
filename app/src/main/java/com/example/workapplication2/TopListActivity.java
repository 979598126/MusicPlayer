package com.example.workapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workapplication2.adapter.PlayListAdapter;
import com.example.workapplication2.entity.Music;
import com.example.workapplication2.entity.MusicTopList;
import com.example.workapplication2.service.MusicService;
import com.example.workapplication2.utils.GetOnlineMusicUtil;
import com.example.workapplication2.utils.OnItemClickListener;
import com.example.workapplication2.utils.WebUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TopListActivity extends AppCompatActivity {
    private List<Music> musicList;
    private int listID;
    private MusicTopList mtl;
    OkHttpClient client=new OkHttpClient();
    TextView textView1,textView2,textView3,textView4;
    ImageView imageView,imageView1,imageView2,playBtn,nextBtn;
    ConstraintLayout constraintLayout;
    PlayListAdapter pla;
    MusicService.MyBinder myBinder;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    MusicService mService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toplist);

//        获取点击的榜单的id和binder对象
        Bundle bundle=getIntent().getExtras();
        listID=bundle.getInt("listID");
        myBinder=(MusicService.MyBinder) bundle.get("myBinder");
        mService=(MusicService) myBinder.getService();



//        发送网络请求
        try {
            send("http://music.eleuu.com/playlist/detail?id="+listID);
        } catch (Exception e) {
            e.printStackTrace();
        }

//    初始化视图
        initView();

//        获取之前的播放器UI
        setPlayMusicUI(myBinder);

//        设置RecyclerView
        setRecyclerView();

//        返回首页
        returnMain();

//    设置在线音乐播放相关监听
        setPlayListener();

//      跳转播放器页面
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TopListActivity.this,PlayMusicActivity.class);
//                获取歌曲信息
                Bundle bundle=new Bundle();
                bundle.putBinder("myBinder",myBinder);
                System.out.println(listID);
                bundle.putInt("listID",listID);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }


    //    ===========================================================
//    发送网络请求以及处理获取的数据
    public void send(String url) throws Exception{
        Request request=new Request.Builder()
                .url(url)
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
                mtl=GetOnlineMusicUtil.getMusicList(json);
                musicList=mtl.getList();
                mService.setWebMusicList(musicList);



//                更新UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(TopListActivity.this).load(mtl.getImgUrl()).into(imageView);
                        textView1.setText(mtl.getListName());
                        textView2.setText(mtl.getDescription());
                        pla.setPlayList(musicList);
                        pla.notifyDataSetChanged();
                    }
                });
            }
        });

    }

//    ===================================
//    初始化视图
    public void initView(){
        constraintLayout=findViewById(R.id.topList_bottom);//底部播放器部分
        imageView=findViewById(R.id.imageView10);//榜单图片
        textView1=findViewById(R.id.textView7);//榜单名
        textView2=findViewById(R.id.textView16);//榜单介绍
        imageView1=findViewById(R.id.returnMain3);//返回键
        textView3=findViewById(R.id.songName);//歌曲名
        textView4=findViewById(R.id.singer);//歌手名
        imageView2=findViewById(R.id.web_albumImg);//专辑图片
        playBtn=findViewById(R.id.play);//播放键
        nextBtn=findViewById(R.id.playNext);//下一首

    }
//    ===================================
//    设置RecyclerView
    public void setRecyclerView(){
        recyclerView=findViewById(R.id.topMusicList);
        pla=new PlayListAdapter(TopListActivity.this,R.layout.playlist_item,musicList);
        recyclerView.setAdapter(pla);
        linearLayoutManager=new LinearLayoutManager(TopListActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecoration=new DividerItemDecoration(TopListActivity.this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
    }

//    =================================
//    返回首页
    public void returnMain(){
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TopListActivity.this, MainActivity.class);
                Bundle bundle=new Bundle();
                bundle.putBinder("myBinder",myBinder);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
//===================================================
//    设置在线音乐播放相关监听
    public void setPlayListener(){
    //      选择歌曲列表播放
    pla.setClickListener(new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
//                设置在线音乐url
            int musicID=musicList.get(position).getMusicID();
            myBinder.setWebMusic(true);
            mService.setPosition(position);
            WebUtil.setMusicUrl(musicID,myBinder);
            //setMusicUrl(musicID,myBinder);
            setPlayMusicUI(myBinder);
            playBtn.setImageResource(R.drawable.ic_play_bar_btn_pause);
        }
    });

    //        播放和暂停
    playBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(myBinder.isPlaying()){
                playBtn.setImageResource(R.drawable.ic_play_bar_btn_play);
            }
            else{
                playBtn.setImageResource(R.drawable.ic_play_bar_btn_pause);
            }
            myBinder.play();
        }
    });

    //        下一首
    nextBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//                获取歌曲位置和id，设置歌曲url
            int position=myBinder.playNext();
            if(myBinder.isWeb()){
                int musicID=myBinder.getWebMusicID(position);
                WebUtil.setMusicUrl(musicID,myBinder);
                //setMusicUrl(musicID,myBinder);
                setPlayMusicUI(myBinder);
                playBtn.setImageResource(R.drawable.ic_play_bar_btn_pause);
            }

        }
    });
}
//    ===============================================
//    更新播放栏UI
    public void setPlayMusicUI(MusicService.MyBinder myBinder){
        Music music=myBinder.getCurrentMusic();
        if(myBinder.isWeb()){
            Glide.with(TopListActivity.this).load(music.getImgUrl()).into(imageView2);
        }
        else {
            imageView2.setImageBitmap(music.getMusicImg());
        }

        textView3.setText(music.getMusicTitle());
        textView4.setText(music.getArtist());
        if(myBinder.isPlaying()){
            playBtn.setImageResource(R.drawable.ic_play_bar_btn_pause);
        }
        else {
            playBtn.setImageResource(R.drawable.ic_play_bar_btn_play);
            }

    }



//

    //    获取并设置网络歌曲URL
    public void setMusicUrl(int musicID, MusicService.MyBinder myBinder){

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
