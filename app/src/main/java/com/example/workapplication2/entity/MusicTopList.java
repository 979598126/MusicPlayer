package com.example.workapplication2.entity;


import android.graphics.Bitmap;

import java.util.List;

public class MusicTopList {
    private int listID;//榜单ID，用于之后请求榜单中的音乐信息
    private String listName;//榜单名
    private String imgUrl;//榜单图片url
    private String description;//榜单介绍
    private String[] topMusic;//榜单前三音乐
    private List<Music> list;//榜单音乐列表

    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID = listID;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public List<Music> getList() {
        return list;
    }

    public void setList(List<Music> list) {
        this.list = list;
    }

    public String[] getTopMusic() {
        return topMusic;
    }

    public void setTopMusic(String[] topMusic) {
        this.topMusic = topMusic;
    }
}
