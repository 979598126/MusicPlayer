package com.example.workapplication2.utils;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Size;

import androidx.annotation.RequiresApi;

import com.example.workapplication2.R;
import com.example.workapplication2.entity.Music;

import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.List;

public class GetLocalMusicUtil {
//    获取本地音乐列表
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static List<Music> getLocalSongs(Context context){
        List<Music> localMusics =new ArrayList<>();
//        查找sdcard中的音乐文件
        Cursor cursor=context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,null,null,MediaStore.Audio.Media.IS_MUSIC);
        System.out.println(cursor.getCount());
        if(cursor.moveToFirst()){
            for(int i=0;i<cursor.getCount();i++){
//                初始化一个Music对象
                Music music =new Music();
//                获取参数
                int musicID=cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));//音乐ID
                String displayName=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));//音乐文件名
                int duration=cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));//音乐时长
                String album=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));//专辑名
                String year=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR));//年代
                String mimeType=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));//音乐类型
                String size=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));//文件大小
                String isMusic=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC));//是否为音乐
                String data=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));//文件路径
                long albumID= cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));//专辑ID
                String title=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));//歌曲名
                String artist=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));//歌手名

//                获取专辑图片
                Bitmap bm=getImg(musicID,context,albumID);
//                显示到页面的部分
                music.setMusicTitle(title);//歌曲名
                music.setArtist(artist);//歌手名
                music.setMusicImg(bm);//歌曲图片
                music.setOptionImg(R.drawable.ic_music_list_icon_more);//选项图片
//                不显示到页面的信息，保存以备用于其他功能实现
                music.setData(data);
                music.setSize(size);
                music.setMimeType(mimeType);
                music.setYear(year);
                music.setAlbum(album);
                music.setDuration(duration);
                music.setDisplayName(displayName);
                music.setMusicID(musicID);
                music.setIsMusic(isMusic);
                music.setAlbumID(albumID);
//                添加到list集合中
                localMusics.add(music);
//                获取下一个音乐文件的信息
                cursor.moveToNext();
            }
        }
        cursor.close();
        return localMusics;
    }

//    获取专辑图片
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static Bitmap getImg(int musicID, Context context,long albumID){
//        Uri trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, musicID);
//        ContentResolver resolver=context.getContentResolver();

        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Bitmap bm=null;
        FileDescriptor fd=null;
        try{
            if (albumID < 0) {
                Uri uri = Uri.parse("content://media/external/audio/media/"
                        + musicID + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver()
                        .openFileDescriptor(uri, "r");
                if (pfd != null) {
                    fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }
            } else {
                Uri uri = ContentUris.withAppendedId(sArtworkUri, albumID);
                ParcelFileDescriptor pfd = context.getContentResolver()
                        .openFileDescriptor(uri, "r");
                if (pfd != null) {
                     fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                } else {
                    return null;
                }
            }

//                ParcelFileDescriptor pfd=resolver.openFileDescriptor(trackUri,"r");
//                if(pfd!=null){
//                    fd=pfd.getFileDescriptor();
//
//                    bm=BitmapFactory.decodeFileDescriptor(fd);
//                }
//                bm = resolver.loadThumbnail(trackUri, new Size(512,512), null);
        }
        catch (Exception e){
            //从歌曲文件读取不出来专辑图片时用来代替的默认专辑图片
            bm=BitmapFactory.decodeResource(context.getResources(), R.drawable.default_cover);
        }

        return bm;
    }




}
