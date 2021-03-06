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

//        ????????????????????????id???binder??????
        Bundle bundle=getIntent().getExtras();
        listID=bundle.getInt("listID");
        myBinder=(MusicService.MyBinder) bundle.get("myBinder");
        mService=(MusicService) myBinder.getService();



//        ??????????????????
        try {
            send("http://music.eleuu.com/playlist/detail?id="+listID);
        } catch (Exception e) {
            e.printStackTrace();
        }

//    ???????????????
        initView();

//        ????????????????????????UI
        setPlayMusicUI(myBinder);

//        ??????RecyclerView
        setRecyclerView();

//        ????????????
        returnMain();

//    ????????????????????????????????????
        setPlayListener();

//      ?????????????????????
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TopListActivity.this,PlayMusicActivity.class);
//                ??????????????????
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
//    ?????????????????????????????????????????????
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



//                ??????UI
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
//    ???????????????
    public void initView(){
        constraintLayout=findViewById(R.id.topList_bottom);//?????????????????????
        imageView=findViewById(R.id.imageView10);//????????????
        textView1=findViewById(R.id.textView7);//?????????
        textView2=findViewById(R.id.textView16);//????????????
        imageView1=findViewById(R.id.returnMain3);//?????????
        textView3=findViewById(R.id.songName);//?????????
        textView4=findViewById(R.id.singer);//?????????
        imageView2=findViewById(R.id.web_albumImg);//????????????
        playBtn=findViewById(R.id.play);//?????????
        nextBtn=findViewById(R.id.playNext);//?????????

    }
//    ===================================
//    ??????RecyclerView
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
//    ????????????
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
//    ????????????????????????????????????
    public void setPlayListener(){
    //      ????????????????????????
    pla.setClickListener(new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
//                ??????????????????url
            int musicID=musicList.get(position).getMusicID();
            myBinder.setWebMusic(true);
            mService.setPosition(position);
            WebUtil.setMusicUrl(musicID,myBinder);
            //setMusicUrl(musicID,myBinder);
            setPlayMusicUI(myBinder);
            playBtn.setImageResource(R.drawable.ic_play_bar_btn_pause);
        }
    });

    //        ???????????????
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

    //        ?????????
    nextBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//                ?????????????????????id???????????????url
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
//    ???????????????UI
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

    //    ???????????????????????????URL
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
