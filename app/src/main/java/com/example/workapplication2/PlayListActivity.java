package com.example.workapplication2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapplication2.adapter.PlayListAdapter;
import com.example.workapplication2.entity.Music;

import java.util.ArrayList;
import java.util.List;

public class PlayListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_list);



        List<Music> listData=new ArrayList<>();
        for(int i=0;i<10;i++){
            Music music =new Music();
            music.setMusicTitle("歌曲"+i);
            music.setArtist("歌手"+i);
            music.setMusicImg(null);
            music.setOptionImg(R.drawable.ic_music_list_icon_more);
            listData.add(music);
        }

        RecyclerView recyclerView=findViewById(R.id.playlist);
        PlayListAdapter pla=new PlayListAdapter(PlayListActivity.this,R.layout.playlist_item,listData);
        recyclerView.setAdapter(pla);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(PlayListActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecoration=new DividerItemDecoration(PlayListActivity.this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);


//        返回首页
        ImageView imageView1=findViewById(R.id.returnMain);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PlayListActivity.this, com.example.workapplication2.MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
