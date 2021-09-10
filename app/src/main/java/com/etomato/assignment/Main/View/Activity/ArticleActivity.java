package com.etomato.assignment.Main.View.Activity;
import com.bumptech.glide.Glide;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.etomato.assignment.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class ArticleActivity extends AppCompatActivity {

    WebView mWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        String ImageUrl = getIntent().getStringExtra("ImageUrl");
        String Title = getIntent().getStringExtra("Title");
        String NewsLink = getIntent().getStringExtra("NewsLink");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView imageView = (ImageView) findViewById(R.id.expandedImage);
        Glide.with(this) //해당 환경의 Context나 객체 입력
            .load(ImageUrl) //URL, URI 등등 이미지를 받아올 경로
            .into(imageView); //받아온 이미지를 받을 공간(ex. ImageView)
        setSupportActionBar(toolbar);

        //뒤로가기 버튼
        //https://stackoverflow.com/questions/40127031/how-to-enable-back-button-when-collapsingtoolbarlayout-is-collapsed
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        CollapsingToolbarLayout coll_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        coll_toolbar.setExpandedTitleGravity(10);
        coll_toolbar.setExpandedTitleColor(Color.MAGENTA);//확장시 글자색
        coll_toolbar.setContentScrimColor(Color.BLACK);//축소시 배경색
        coll_toolbar.setTitle(Title);

        // 웹뷰 셋팅
        mWebView = (WebView) findViewById(R.id.webView);//xml 자바코드 연결
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.getSettings().setJavaScriptEnabled(true);//자바스크립트 허용

        mWebView.loadUrl("http://dev.newstong.co.kr/newstongsecond/newsDescription.aspx?ntSeq="+NewsLink);//웹뷰 실행

    }



}