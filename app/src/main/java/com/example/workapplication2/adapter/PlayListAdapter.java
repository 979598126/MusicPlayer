package com.example.workapplication2.adapter;

import android.content.Context;
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
import com.example.workapplication2.utils.OnItemClickListener;

import java.util.List;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.PlayListViewHolder> {
    private Context context;
    private int resource;
    List<Music> playList;
    private OnItemClickListener mClickListener;


    public PlayListAdapter(Context context,int resource,List<Music> playList){
        this.context=context;
        this.resource=resource;
        this.playList=playList;
    }

    public void setPlayList(List<Music> playList) {
        this.playList = playList;
    }

    public void setClickListener(OnItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    @NonNull
    @Override
    public PlayListAdapter.PlayListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item,parent,false);
        PlayListAdapter.PlayListViewHolder viewHolder=new PlayListAdapter.PlayListViewHolder(view,mClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlayListAdapter.PlayListViewHolder holder, int position) {
        Music music =playList.get(position);
//        加载网络图片
        Glide.with(context).load(music.getImgUrl()).into(holder.imageView1);

        holder.textView1.setText(music.getMusicTitle());
        holder.textView2.setText(music.getArtist());
        holder.imageView2.setImageResource(R.drawable.ic_music_list_icon_more);
    }

    @Override
    public int getItemCount() {
        if (playList!=null){
            return playList.size();
        }
        else {
            return 0;
        }
    }

//    创建ViewHolder
    class PlayListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private OnItemClickListener mListener;// 声明自定义的接口
        ImageView imageView1,imageView2,imageView3;
        TextView textView1,textView2;
        public PlayListViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            imageView1=itemView.findViewById(R.id.playlist_img);
            imageView2=itemView.findViewById(R.id.songOption);
            imageView3=itemView.findViewById(R.id.imageView5);
            textView1=itemView.findViewById(R.id.playlist_songName);
            textView2=itemView.findViewById(R.id.playlist_singerName);

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
