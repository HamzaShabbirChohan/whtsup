package com.example.whatsup.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.whatsup.Fragment.Calls;
import com.example.whatsup.Fragment.Chat;
import com.example.whatsup.Fragment.Status;

public class FragmentAdapter extends FragmentPagerAdapter {

    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:return new Chat();
            case 1: return new Status();
            case 3: return new Calls();
            default:return new Chat();
        }

    }

    @Override
    public int getCount() {

        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title= null;
        if(position==0) {
           title="Chat";
        }
        if (position==1)

        {
            title="Status";
        }
        if (position==2)
        {
            title="Calls";
        }
        return title;
    }
}
