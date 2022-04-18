package com.kofze.eventman.adapters;

import android.content.Context;
import android.widget.Adapter;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.kofze.eventman.ui.events.EventActivity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EventPagerAdapter extends FragmentStateAdapter {

    private final Context context;
    private List<Fragment> fragmentList = new ArrayList<>();

    public EventPagerAdapter(Context context, FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.context = context;
    }

    public void addFragment(Fragment fragment) {
        fragmentList.add(fragment);
    }


    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}
