package com.etomato.assignment.ViewPager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.etomato.assignment.R;
import com.etomato.assignment.ViewPager.Fragment.NewsFragment;
import com.etomato.assignment.ViewPager.Fragment.TimelineFragment;
import com.etomato.assignment.ViewPager.Fragment.WriteFragment;
import com.google.android.material.tabs.TabLayout;

public class ViewPagerActivity extends AppCompatActivity {

    SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());
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