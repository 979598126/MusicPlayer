package com.example.workapplication2.utils;

import com.example.workapplication2.entity.Music;
import com.example.workapplication2.entity.MusicTopList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetOnlineMusicUtil {

//    获取在线榜单列表
    public static List getTopList(String json){
        List<MusicTopList> listData=new ArrayList<>();
        MusicTopList mtl;

        try {
//                获取每个榜单信息摘要
            //将json字符串转为JSON对象
            JSONObject jsonObject=new JSONObject(json);
            JSONArray jsonArray=jsonObject.getJSONArray("list");

            for(int i=0;i<10;i++){
                mtl=new MusicTopList();
                String[] topMusic=new String[3];
                //List<Music> musicList=new ArrayList<>();
//                            获取JSON数组中的每一个对象
                JSONObject list=jsonArray.getJSONObject(i);
//                            榜单id
                mtl.setListID(list.getInt("id"));
//                            榜单名
                mtl.setListName(list.getString("name"));
//                            榜单图片url
                mtl.setImgUrl(list.getString("coverImgUrl"));
//                                 榜单描述
                mtl.setDescription(list.getString("description"));

//                            榜单前三歌曲
                JSONArray jsonArray2=list.getJSONArray("tracks");
                for(int j=0;j<jsonArray2.length();j++){

                    //Music music=new Music();
                    JSONObject tracks=jsonArray2.getJSONObject(j);
                    topMusic[j]=tracks.getString("first")+"-"+tracks.getString("second");
//                    music.setMusicTitle(tracks.getString("first"));
//                    music.setArtist(tracks.getString("second"));
//                    musicList.add(music);

                }
                mtl.setTopMusic(topMusic);
                //mtl.setList(musicList);
                listData.add(mtl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }
//    获取在线音乐列表
    public static MusicTopList getMusicList(String json){
        MusicTopList mtl=new MusicTopList();
        List<Music> musicList;
        try {

            //将json字符串转为JSON对象
            JSONObject jsonObject=new JSONObject(json);
            JSONObject playlist=jsonObject.getJSONObject("playlist");
//                            榜单名
            mtl.setListName(playlist.getString("name"));
//                            榜单图片url
            String imgUrl=playlist.getString("coverImgUrl");
            mtl.setImgUrl(imgUrl);
//                                 榜单描述
            mtl.setDescription(playlist.getString("description"));
            musicList=new ArrayList<>();

            JSONArray jsonArray=playlist.getJSONArray("tracks");
            for(int i=0;i<20;i++){
                Music music=new Music();
                JSONObject tracks=jsonArray.getJSONObject(i);
                music.setMusicID(tracks.getInt("id"));
                music.setMusicTitle(tracks.getString("name"));

                JSONArray jsonArray2=tracks.getJSONArray("ar");
                String artist="";
                for(int j=0;j<jsonArray2.length();j++){
                    JSONObject ar=jsonArray2.getJSONObject(j);
                    if(j==jsonArray2.length()-1){
                        artist=artist.concat(ar.getString("name"));
                    }
                    else{
                        artist=artist.concat(ar.getString("name")+"/");
                    }
                }
                music.setArtist(artist);
                JSONArray jsonArray3=tracks.getJSONArray("alia");
                if (jsonArray3.length()!=0){
                    music.setAlbum((String) jsonArray3.get(0));
                }
                JSONObject al=tracks.getJSONObject("al");
                String picUrl=al.getString("picUrl");
                music.setImgUrl(picUrl);
                musicList.add(music);
            }
            mtl.setList(musicList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mtl;
    }

//    获取网络音乐URL
    public static String getMusicUrl(String json){
        String musicUrl=null;
        //将json字符串转为JSON对象
        try {
            JSONObject jsonObject=new JSONObject(json);
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            JSONObject jsonObject1=jsonArray.getJSONObject(0);
            musicUrl=jsonObject1.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return musicUrl;
    }




}
