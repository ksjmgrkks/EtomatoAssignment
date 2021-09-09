package com.etomato.assignment.Main.View.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.etomato.assignment.Main.View.Fragment.NewsFragment;
import com.etomato.assignment.Main.View.Fragment.TimelineFragment;
import com.etomato.assignment.Main.View.Fragment.WriteFragment;
import com.etomato.assignment.R;
import com.etomato.assignment.Main.Data.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class ViewPagerActivity extends AppCompatActivity {

    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        setTitle("뉴스통");
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
    public void setupViewPager(ViewPager viewPager) {
        adapter.addFragment(new WriteFragment(), "글쓰기");
        adapter.addFragment(new TimelineFragment(), "타임라인");
        adapter.addFragment(new NewsFragment(), "뉴스");
        viewPager.setAdapter(adapter);
    }
}