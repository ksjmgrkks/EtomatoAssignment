package com.etomato.assignment.Main;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.etomato.assignment.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final ArrayList<Model> dataList;
    private final Activity activity;

    public MyAdapter(Activity activity, ArrayList<Model> dataList)
    {
        this.activity = activity;
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(viewType == ViewType.BASIC_VIEW)
        {
            view = inflater.inflate(R.layout.basic_view, parent, false);
            return new BasicViewHolder(view);
        }
        else if(viewType == ViewType.AD_VIEW)
        {
            view = inflater.inflate(R.layout.ad_view, parent, false);
            return new AdViewHolder(view);
        }
        else
        {
            view = inflater.inflate(R.layout.grid_view, parent, false);
            return new GridViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position)
    {
        if(viewHolder instanceof BasicViewHolder)
        {
            ((BasicViewHolder) viewHolder).content.setText(dataList.get(position).getContent());
            Glide.with(activity)
                    .load(dataList.get(position).getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.image)
                    .into(((BasicViewHolder) viewHolder).image);
        }
        else if(viewHolder instanceof AdViewHolder)
        {
            ((AdViewHolder) viewHolder).content.setText(dataList.get(position).getContent());
        }
        else
        {

            ((GridViewHolder) viewHolder).content.setText(dataList.get(position).getContent());
            Glide.with(activity)
                    .load(dataList.get(position).getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.image)
                    .into(((GridViewHolder) viewHolder).image);
        }
    }

    @Override
    public int getItemCount()
    {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getViewType();
    }

    public class BasicViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView content;

        BasicViewHolder(View itemView)
        {
            super(itemView);
            image = itemView.findViewById(R.id.imageView);
            content = itemView.findViewById(R.id.textView_basic);
        }
    }

    public class AdViewHolder extends RecyclerView.ViewHolder{
        TextView content;

        AdViewHolder(View itemView)
        {
            super(itemView);

            content = itemView.findViewById(R.id.textView_ad);
        }
    }

    public class GridViewHolder extends RecyclerView.ViewHolder{
        TextView content;
        ImageView image;

        GridViewHolder(View itemView)
        {
            super(itemView);
            image = itemView.findViewById(R.id.imageView);
            content = itemView.findViewById(R.id.textView_grid);
        }
    }

}
