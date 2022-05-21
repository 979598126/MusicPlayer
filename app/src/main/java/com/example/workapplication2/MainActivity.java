package com.example.workapplication2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.workapplication2.adapter.ViewPagerAdapter1;
import com.example.workapplication2.entity.Music;
import com.example.workapplication2.fragment.LocalMusicFragment;
import com.example.workapplication2.fragment.OnlineMusicFragment;
import com.example.workapplication2.service.MusicService;
import com.example.workapplication2.utils.GetLocalMusicUtil;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements LocalMusicFragment.CallBackValue,OnlineMusicFragment.CallBackValue2{


    LocalMusicFragment localMusicFragment;
    OnlineMusicFragment onlineMusicFragment;
    ViewPager2  vp;
    TextView textView1,textView2,textView3,textView4;
    ImageView imageView1,imageView2,imageView3,imageView4;
    ConstraintLayout layout;

    Intent serviceIntent;
    MusicService.MyBinder myBinder;
    private MusicService mService;
    private MyServiceConnection conn;
    private List<Music> musicList;

//    获取被点击的歌曲在list中的位置
    @Override
    public void SendMessageValue(int position) {
        myBinder.setWebMusic(false);
        if(mService==null){
            mService= (MusicService) myBinder.getService();
        }
        mService.setPosition(position);

        myBinder.startPlay();
//        更新UI
        imageView2.setImageResource(R.drawable.ic_play_bar_btn_pause);
        setPlayingSong(myBinder);
    }



//    获取网络榜单点击的位置,跳转到榜单详情页面
    @Override
    public void SendMessageValue2(int index,int listID) {
        Bundle bundle=new Bundle();
        bundle.putInt("listID",listID);
        bundle.putBinder("myBinder",myBinder);
        Intent intent=new Intent(MainActivity.this, TopListActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //    定义服务连接
    private class MyServiceConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            myBinder=(MusicService.MyBinder)iBinder;
            mService=(MusicService) myBinder.getService();
            mService.setMusicList(musicList);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            System.out.println("服务连接失败");
        }
    };

    @Override
    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            myBinder=(MusicService.MyBinder)bundle.get("myBinder");
            System.out.println("bundle!=null");
        }

        //初始化视图
        initView();
        //获取读写权限
        getPermissions();

        // 跳转页面按钮监听
        transferView();

//        设置ViewPager
        setViewPager2();


        //        获取本地音乐列表
        musicList= GetLocalMusicUtil.getLocalSongs(this);
        localMusicFragment.setListData(musicList);


        //     设置播放器的默认歌曲（本地歌曲列表的第一首）
        if(myBinder==null){
            imageView3.setImageBitmap(musicList.get(0).getMusicImg());
            textView3.setText(musicList.get(0).getMusicTitle());
            textView4.setText(musicList.get(0).getArtist());
        }
        else{
            setPlayingSong(myBinder);

            if(myBinder.isPlaying()){
                imageView2.setImageResource(R.drawable.ic_play_bar_btn_pause);
            }
            else{
                imageView2.setImageResource(R.drawable.ic_play_bar_btn_play);
            }
        }


        //绑定service;
        if(myBinder==null){
            serviceIntent = new Intent(this , MusicService.class);
            conn=new MyServiceConnection();
            bindService(serviceIntent, conn,Context.BIND_AUTO_CREATE);
        }


//       歌曲相关监听
        setMusicListener();;



    }


//================================================================
//    初始化视图
    public void initView(){
        textView1=findViewById(R.id.textView2);//本地音乐
        textView2=findViewById(R.id.textView3);//在线音乐
        imageView1=findViewById(R.id.toPlayList);//右下角列表图标
        layout=findViewById(R.id.main_bottom);//bottom部分

        //        播放栏部分
        imageView2 = findViewById(R.id.imageView7);//播放和暂停键
        imageView3 = findViewById(R.id.imageView6);//专辑图片
        imageView4 = findViewById(R.id.imageView8);//下一首按钮
        textView3 =  findViewById(R.id.textView4);//歌曲名
        textView4 =  findViewById(R.id.textView5);//歌手名
    }

//    ===========================================================
// 跳转页面按钮监听
    public void transferView(){
        //        跳转播放列表
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,PlayListActivity.class);
                startActivity(intent);
            }
        });

//        跳转播放器页面
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,PlayMusicActivity.class);
//                获取歌曲信息

                Bundle bundle=new Bundle();
                bundle.putBinder("myBinder",myBinder);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }

//    =====================================================================
// 获取读写权限
    public void getPermissions(){
        //检查当前权限（若没有该权限，值为-1；若有该权限，值为0）
        int hasReadStoragePermission= ContextCompat.checkSelfPermission(getApplication(),Manifest.permission.READ_EXTERNAL_STORAGE);
        Log.e("PERMISSION_CODE",hasReadStoragePermission+"***");
        if(hasReadStoragePermission== PackageManager.PERMISSION_GRANTED){
            //用户同意授权，执行读取文件的代码
            obtainMediaInfo();
        }
        else{
            //若没有授权，会弹出一个对话框，用户选择是否授权应用使用系统权限
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
    }

// 回调函数
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户同意授权，执行读取文件的代码
                obtainMediaInfo();
            } else {
                //若用户不同意授权，直接退出应用。
                finish();
            }
        }
    }
    @SuppressLint("Range")
    private void obtainMediaInfo() {
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        cursor.moveToFirst();
        do {
            String title = cursor.getString(cursor.getColumnIndex("title"));
            Log.e("TITLE",title);
        }while (cursor.moveToNext());
    }


//    ============================================================================
//                                   设置ViewPager2
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void setViewPager2(){
        vp=findViewById(R.id.viewPage1);
        //        创建本地音乐和在线音乐的Fragment
        localMusicFragment=new LocalMusicFragment();
        onlineMusicFragment=new OnlineMusicFragment();

//        创建list，将Fragment添加到list中
        ArrayList<Fragment> fragmentArrayList=new ArrayList<>();
        fragmentArrayList.add(localMusicFragment);
        fragmentArrayList.add(onlineMusicFragment);
        //        设置边界
        vp.setOffscreenPageLimit(fragmentArrayList.size()-1);
//        将Fragment数组添加到适配器中
        ViewPagerAdapter1 viewPagerAdapter1=new ViewPagerAdapter1(this,fragmentArrayList);
        vp.setAdapter(viewPagerAdapter1);




        //        设置页面滑动事件
        vp.registerOnPageChangeCallback(new  ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
//                设置页面切换时的文字效果
                if(position==0){
                    textView1.setAlpha(1F);
                    textView2.setAlpha(0.5F);
                }
                else{
                    textView1.setAlpha(0.5F);
                    textView2.setAlpha(1F);
                }
            }
        });

        //        点击文字切换fragment
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vp.setCurrentItem(0,false);
                textView1.setAlpha(1F);
                textView2.setAlpha(0.5F);
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vp.setCurrentItem(1,false);
                textView1.setAlpha(0.5F);
                textView2.setAlpha(1F);
            }
        });
    }

//=====================================================================================
// 设置正在的播放歌曲的视图
    public void setPlayingSong(MusicService.MyBinder myBinder){
        Music music=myBinder.getCurrentMusic();
        if(myBinder.isWeb()){
            Glide.with(this).load(music.getImgUrl()).into(imageView3);
        }
        else{
            imageView3.setImageBitmap(music.getMusicImg());
        }

        textView3.setText(music.getMusicTitle());
        textView4.setText(music.getArtist());
    }
    public void isPlaying(){
        if(myBinder.isPlaying()){
            imageView2.setImageResource(R.drawable.ic_play_bar_btn_play);
        }
        else{
            imageView2.setImageResource(R.drawable.ic_play_bar_btn_pause);
        }
    }

// =======================================================
//    设置歌曲相关监听
    public void setMusicListener(){
        //        播放和暂停歌曲
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               播放和暂停歌曲
                isPlaying();
                myBinder.play();
            }
        });

//        下一首歌曲
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myBinder.playNext();
                imageView2.setImageResource(R.drawable.ic_play_bar_btn_pause);
                setPlayingSong(myBinder);
            }
        });
    }
}
