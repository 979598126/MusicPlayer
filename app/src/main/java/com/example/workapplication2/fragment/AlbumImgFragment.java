package com.example.workapplication2.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.workapplication2.R;

public class AlbumImgFragment extends Fragment {
    Bitmap bm;
    String webMusicUrl;
    ImageView imageView;

    public void setWebMusicUrl(String webMusicUrl) {
        this.webMusicUrl = webMusicUrl;
        Glide.with(this).load(webMusicUrl).into(imageView);
    }

    public void setBm(Bitmap bm) {
        this.bm=bm;
        imageView.setImageBitmap(bm);
    }

    public AlbumImgFragment(Bitmap bm){
        this.bm=bm;
    }

    public AlbumImgFragment(String webMusicUrl){
        this.webMusicUrl=webMusicUrl;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.albumimg_fragment,container,false);
        imageView=view.findViewById(R.id.albumImg);
        if(bm!=null){
            imageView.setImageBitmap(bm);
        }
        else{
            Glide.with(this).load(webMusicUrl).into(imageView);
        }
        return view;
    }
}
