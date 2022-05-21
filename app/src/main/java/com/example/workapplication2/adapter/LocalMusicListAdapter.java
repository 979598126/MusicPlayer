package com.example.workapplication2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapplication2.R;
import com.example.workapplication2.entity.Music;
import com.example.workapplication2.utils.OnItemClickListener;

import java.util.List;

public class LocalMusicListAdapter extends RecyclerView.Adapter<LocalMusicListAdapter.LMLViewHolder> {
    private Context context;
    private int resource;
    private List<Music> musicList;
    private int lastPosition=-1;

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public LocalMusicListAdapter(Context context, int resource, List<Music> musicList) {
        this.context = context;
        this.resource = resource;
        this.musicList = musicList;
    }

    @NonNull
    @Override
    public LMLViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.lmlist_item,parent,false);
        LocalMusicListAdapter.LMLViewHolder viewHolder=new LocalMusicListAdapter.LMLViewHolder(view,listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LMLViewHolder holder, int position) {
        Music music = musicList.get(position);
        holder.imageView1.setImageBitmap(music.getMusicImg());
        holder.textView1.setText(music.getMusicTitle());
        holder.textView2.setText(music.getArtist()+"-"+music.getAlbum());

    }


    @Override
    public int getItemCount() {
        return musicList.size();
    }

    class LMLViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OnItemClickListener listener;// 声明自定义的接口
        TextView textView1,textView2;
        ImageView imageView1;
        public LMLViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            imageView1=itemView.findViewById(R.id.lmlist_img);
            textView1=itemView.findViewById(R.id.lmlist_songName);
            textView2=itemView.findViewById(R.id.lmlist_singerName);

            this.listener=listener;
            // 为ItemView添加点击事件
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            // getPosition()为ViewHolder自带的一个方法，用来获取RecyclerView当前的位置，将此作为参数，传出去
            listener.onItemClick(view, getPosition());
        }
    }
}
