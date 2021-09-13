package com.etomato.assignment.Test.InfiniteViewPager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Handler;

import com.etomato.assignment.Main.Data.ViewPagerAdapter;
import com.etomato.assignment.R;

import java.util.ArrayList;
import java.util.List;

public class InfiniteActivity extends AppCompatActivity {

    ViewPager2 viewPager2;
    private Handler headerHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infinite);

        viewPager2 = findViewById(R.id.view_pager);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        List<ViewPagerItem> list = new ArrayList<>();

        list.add(new ViewPagerItem(R.drawable.image));
        list.add(new ViewPagerItem(R.drawable.basicnewstong));
        list.add(new ViewPagerItem(R.drawable.newstongsmall));
        list.add(new ViewPagerItem(R.drawable.ic_sun));
        list.add(new ViewPagerItem(R.drawable.ic_launcher_foreground));
        /*for looping*/
        list.add(new ViewPagerItem(R.drawable.image));
        list.add(new ViewPagerItem(R.drawable.basicnewstong));
        list.add(new ViewPagerItem(R.drawable.newstongsmall));
        viewPager2.setAdapter(new ViewPager2Adapter(list, viewPager2));
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                headerHandler.removeCallbacks(headerRunnable);
                headerHandler.postDelayed(headerRunnable, 3000); // Slide duration 3 seconds
            }
        });
    }

    private Runnable headerRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1, true);

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        headerHandler.removeCallbacks(headerRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        headerHandler.postDelayed(headerRunnable, 3000); // Slide duration 3 seconds
    }
}