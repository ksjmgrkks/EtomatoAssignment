package com.etomato.assignment.Main.View.Activity;

import static androidx.core.content.ContextCompat.getSystemService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.etomato.assignment.Main.View.Fragment.NewsFragment;
import com.etomato.assignment.Main.View.Fragment.TimelineFragment;
import com.etomato.assignment.Main.View.Fragment.WriteFragment;
import com.etomato.assignment.R;
import com.etomato.assignment.Main.Data.ViewPagerAdapter;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.tabs.TabLayout;

public class ViewPagerActivity extends AppCompatActivity {
    NewsFragment newsFragment;
    int tab;

    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        setTitle("뉴스통");

        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);

        String tabSetting = getIntent().getStringExtra("TabSetting");
        if(tabSetting != null){
            tab = Integer.parseInt(tabSetting);
        }else{
            tab = 0;
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.selectTab(tabLayout.getTabAt(tab));



    }
    public void setupViewPager(ViewPager viewPager) {
        newsFragment = new NewsFragment();
        adapter.addFragment(new WriteFragment(), "글쓰기");
        adapter.addFragment(new TimelineFragment(), "타임라인");
        adapter.addFragment(newsFragment, "뉴스");
        viewPager.setAdapter(adapter);
    }

    //현재 Focus를 받고 있는 View의 영역이 아닌 다른 곳에 터치 이벤트가 일어나면 InputMethodManager을 통해 키보드를 내리는 코드입니다.
    //참고 : https://ohdbjj.tistory.com/7
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }


}