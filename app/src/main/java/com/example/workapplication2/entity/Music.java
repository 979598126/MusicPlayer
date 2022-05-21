package com.example.workapplication2.entity;


import android.graphics.Bitmap;

public class Music {
    private int musicID;//音乐ID
    private String displayName;//音乐文件名
    private String musicTitle;//歌曲名
    private int duration;//音乐时长
    private String artist;//歌手名
    private String album;//专辑名
    private String year;//年代
    private String mimeType;//音乐类型
    private String size;//文件大小
    private String isMusic;//是否为音乐
    private String data;//文件路径
    private long albumID;//专辑ID
    private Bitmap musicImg;//歌曲图片
    private int optionImg;//选项图片
    private String imgUrl;//歌曲图片url

    public Music(int musicID, String displayName, String musicTitle, int duration, String artist, String album, String year, String mimeType, String size, String isMusic, String data, long albumID, Bitmap musicImg, int optionImg,String imgUrl ) {
        this.musicID = musicID;
        this.displayName = displayName;
        this.musicTitle = musicTitle;
        this.duration = duration;
        this.artist = artist;
        this.album = album;
        this.year = year;
        this.mimeType = mimeType;
        this.size = size;
        this.isMusic = isMusic;
        this.data = data;
        this.albumID = albumID;
        this.musicImg = musicImg;
        this.optionImg = optionImg;
        this.imgUrl=imgUrl;
    }
    public Music(){}

    public int getMusicID() {
        return musicID;
    }

    public void setMusicID(int musicID) {
        this.musicID = musicID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMusicTitle() {
        return musicTitle;
    }

    public void setMusicTitle(String musicTitle) {
        this.musicTitle = musicTitle;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getIsMusic() {
        return isMusic;
    }

    public void setIsMusic(String isMusic) {
        this.isMusic = isMusic;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getAlbumID() {
        return albumID;
    }

    public void setAlbumID(long albumID) {
        this.albumID = albumID;
    }

    public Bitmap getMusicImg() {
        return musicImg;
    }

    public void setMusicImg(Bitmap musicImg) {
        this.musicImg = musicImg;
    }

    public int getOptionImg() {
        return optionImg;
    }

    public void setOptionImg(int optionImg) {
        this.optionImg = optionImg;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
