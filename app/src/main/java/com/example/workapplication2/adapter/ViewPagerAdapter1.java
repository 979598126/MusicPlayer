package com.example.workapplication2.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

//主界面的ViewPagerAdapter
public class ViewPagerAdapter1 extends FragmentStateAdapter {
    private ArrayList<Fragment> fragmentArrayList;

    public ViewPagerAdapter1(@NonNull FragmentActivity fragmentActivity,ArrayList<Fragment> fragmentArrayList) {
        super(fragmentActivity);
        this.fragmentArrayList=fragmentArrayList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentArrayList.size();
    }
}
