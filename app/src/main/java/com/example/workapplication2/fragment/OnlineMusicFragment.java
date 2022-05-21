package com.example.workapplication2.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapplication2.R;
import com.example.workapplication2.TopListActivity;
import com.example.workapplication2.adapter.OnlineMusicListAdapter;
import com.example.workapplication2.entity.MusicTopList;
import com.example.workapplication2.utils.GetOnlineMusicUtil;
import com.example.workapplication2.utils.OnItemClickListener;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OnlineMusicFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<MusicTopList> listData;
    LinearLayoutManager linearLayoutManager;
    private Context context;
    private OnlineMusicListAdapter pla;
    OkHttpClient client=new OkHttpClient();
    CallBackValue2 callBackValue2;



//    =============================================================

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        发送网络请求
        try {
            send("http://music.eleuu.com/toplist/detail");
        } catch (Exception e) {
            e.printStackTrace();
        }

        View view=inflater.inflate(R.layout.onlinemusic_fragment,container,false);
        context=view.getContext();

//        设置RecyclerView
        setRecyclerView(view);


//        设置每个榜单的点击事件
        pla.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int listID=listData.get(position).getListID();
                callBackValue2.SendMessageValue2(position,listID);
            }
        });
        return view ;
    }

//    =======================================


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // 当前fragment从activity重写了回调接口 得到接口的实例化对象
        callBackValue2 = (CallBackValue2) getActivity();
    }


//    ===========================================================
//    发送网络请求
    public void send(String url) throws Exception{
        Request request=new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String json=response.body().string();
                listData= GetOnlineMusicUtil.getTopList(json);

//                更新UI
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pla.setMusicList(listData);
                        pla.notifyDataSetChanged();
                    }
                });
            }
        });

    }

//    =========================================
//    设置RecyclerView
    public void setRecyclerView(View view){
        recyclerView=view.findViewById(R.id.omList);
        pla=new OnlineMusicListAdapter(context,R.layout.omlist_item,listData);
        recyclerView.setAdapter(pla);

        linearLayoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
//        设置分割线
        DividerItemDecoration itemDecoration=new DividerItemDecoration(context,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
    }
//    =========================================
    // 1.定义一个接口回调

    public interface CallBackValue2 {

        public void SendMessageValue2(int position,int listID);

    }
}
