package com.example.maze_bank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class IntroViewPagerAdapter extends PagerAdapter {
    Context mContext;
    List<Screenitem> mListScreen;
    public IntroViewPagerAdapter(Context mContext, List<Screenitem> mListScreen) {
        this.mContext = mContext;
        this.mListScreen = mListScreen;
    }
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutscreen=inflater.inflate(R.layout.layout_screen,null);



        ImageView imgSlide=layoutscreen.findViewById(R.id.intro_img);
        TextView title=layoutscreen.findViewById(R.id.intro_title);
        TextView desc=layoutscreen.findViewById(R.id.intro_description);



        title.setText(mListScreen.get(position).getTitle());
        desc.setText(mListScreen.get(position).getDescription());
        imgSlide.setImageResource(mListScreen.get(position).getScreenImg());
        container.addView(layoutscreen);
        return layoutscreen;
    }
    public int getCount() {
        return mListScreen.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }


}
