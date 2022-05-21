package com.example.workapplication2.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapplication2.R;
import com.example.workapplication2.adapter.LocalMusicListAdapter;
import com.example.workapplication2.entity.Music;
import com.example.workapplication2.utils.OnItemClickListener;

import java.util.List;


public class LocalMusicFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Music> listData;
    private Context context;
    private LocalMusicListAdapter pla;

    CallBackValue callBackValue;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // 当前fragment从activity重写了回调接口 得到接口的实例化对象
        callBackValue = (CallBackValue) getActivity();
    }




    public void setListData(List<Music> listData) {
        this.listData = listData;
    }

    public LocalMusicListAdapter getPla() {
        return pla;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.localmusic_fragment, container, false);
        context = view.getContext();


        //      本地音乐列表

//      设置RecyclerView
        recyclerView = view.findViewById(R.id.lmList);
        pla = new LocalMusicListAdapter(context, R.layout.lmlist_item, listData);
        recyclerView.setAdapter(pla);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
//        设置分割线
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);


        pla.setListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                callBackValue.SendMessageValue(position);
            }
        });

        return view;

    }






    //==============================================
// 1.定义一个接口回调

    public interface CallBackValue {
        public void SendMessageValue(int index);
    }



}
