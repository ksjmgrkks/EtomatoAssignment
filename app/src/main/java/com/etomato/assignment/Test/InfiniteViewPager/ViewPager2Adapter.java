package com.etomato.assignment.Test.InfiniteViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.etomato.assignment.R;

import java.util.List;

public class ViewPager2Adapter extends RecyclerView.Adapter<ViewPager2Adapter.ViewHolder> {
    private List<ViewPagerItem> mData;
    private ViewPager2 viewPager2;

    ViewPager2Adapter(List<ViewPagerItem> mData, ViewPager2 viewPager2) {
        this.mData = mData;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_pager, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setHeader(mData.get(position));
        if (position == mData.size() - 2) {
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageHeader;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageHeader = itemView.findViewById(R.id.image);
        }

        void setHeader(ViewPagerItem ViewPagerItem) {
            imageHeader.setImageResource(ViewPagerItem.getImage());
        }

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(0, true);
        }
    };
}
