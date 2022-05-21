package com.example.workapplication2.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workapplication2.R;
import com.example.workapplication2.entity.Music;
import com.example.workapplication2.entity.MusicTopList;
import com.example.workapplication2.utils.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class OnlineMusicListAdapter extends RecyclerView.Adapter<OnlineMusicListAdapter.OMViewHolder>{
    private Context context;
    private int resource;
    private List<MusicTopList> musicList;
    private OnItemClickListener mClickListener;

    public OnlineMusicListAdapter(Context context, int resource, List<MusicTopList> musicList) {
        this.context = context;
        this.resource = resource;
        this.musicList = musicList;
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mClickListener = listener;
    }

    public void setMusicList(List<MusicTopList> musicList) {
        this.musicList = musicList;
    }

    @NonNull
    @Override
    public OMViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.omlist_item,parent,false);
         OnlineMusicListAdapter.OMViewHolder viewHolder=new OnlineMusicListAdapter.OMViewHolder(view,mClickListener);
         return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OMViewHolder holder, int position) {

//        设置RecyclerView中Item的显示内容
        MusicTopList mtl=musicList.get(position);
//        解析榜单图片的网络url
        Glide.with(context).load(mtl.getImgUrl()).into(holder.imageView1);

        String[] topMusic= mtl.getTopMusic();
        if(topMusic[0]!=null){

            holder.textView1.setText("1."+topMusic[0]);
            holder.textView2.setText("2."+topMusic[1]);
            holder.textView3.setText("3."+topMusic[2]);
        }
        else{
            holder.textView1.setText("1.暂无数据");
            holder.textView2.setText("2.暂无数据");
            holder.textView3.setText("3.暂无数据");
        }


    }

    @Override
    public int getItemCount() {
        if(musicList==null){
            return 0;
        }
        return musicList.size();
    }


     class OMViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
         private OnItemClickListener mListener;// 声明自定义的接口

        private ImageView imageView1;
        private TextView  textView1,textView2,textView3;

         public OMViewHolder(@NonNull View itemView,OnItemClickListener listener) {
             super(itemView);
             imageView1=itemView.findViewById(R.id.imageView);
             textView1=itemView.findViewById(R.id.textView6);
             textView2=itemView.findViewById(R.id.textView14);
             textView3=itemView.findViewById(R.id.textView15);

             mListener=listener;
             // 为ItemView添加点击事件
             itemView.setOnClickListener(this);


         }


         @Override
         public void onClick(View view) {
             // getPosition()为ViewHolder自带的一个方法，用来获取RecyclerView当前的位置，将此作为参数，传出去
             mListener.onItemClick(view, getPosition());
         }
     }

}
