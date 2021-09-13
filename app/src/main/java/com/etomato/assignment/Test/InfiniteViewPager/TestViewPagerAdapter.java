package com.etomato.assignment.Test.InfiniteViewPager;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.etomato.assignment.Main.Data.NewsAdapter;
import com.etomato.assignment.Main.Data.NewsModel;
import com.etomato.assignment.R;

import java.util.ArrayList;
import java.util.List;

public class TestViewPagerAdapter extends PagerAdapter {

    private ArrayList<NewsModel> models;
    private Activity activity;

    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null ;

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }
    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

    public TestViewPagerAdapter() {

    }

    // Context를 전달받아 mContext에 저장하는 생성자 추가.
    public TestViewPagerAdapter(Activity activity, ArrayList<NewsModel> models) {
        this.activity = activity;
        this.models = models;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null ;

        int modelPosition = mapPagerPositionToModelPosition(position);

        if (activity != null) {
            // LayoutInflater를 통해 "/res/layout/page.xml"을 뷰로 생성.
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_view_pager, container, false);

            TextView textView = (TextView) view.findViewById(R.id.textView_title) ;
            ImageView imageView = (ImageView) view.findViewById(R.id.image) ;
            textView.setText(models.get(modelPosition).getContent());
            Glide.with(activity)
                    .load(models.get(modelPosition).getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.newstong)
                    .override(800,500)
                    .centerCrop()
                    .into(imageView);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onItemClick(view, modelPosition) ;
                    }
                }
            });
        }

        // 뷰페이저에 추가.
        container.addView(view) ;

        return view ;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 뷰페이저에서 삭제.
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        // Since we want to put 2 additional pages at left & right,
        // the actual size will plus 2.
        return models.size() == 0 ? 0 : models.size() + 2;
    }

    public int getRealCount() {
        return models.size();
    }

    private int mapPagerPositionToModelPosition(int pagerPosition) {
        // Put last page model to the first position.
        if (pagerPosition == 0) {
            return getRealCount() - 1;
        }
        // Put first page model to the last position.
        if (pagerPosition == getRealCount() + 1) {
            return 0;
        }
        return pagerPosition - 1;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View)object);
    }


}