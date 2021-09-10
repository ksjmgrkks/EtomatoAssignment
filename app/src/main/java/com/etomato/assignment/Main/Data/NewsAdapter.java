package com.etomato.assignment.Main.Data;

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
import com.bumptech.glide.request.target.Target;
import com.etomato.assignment.R;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final ArrayList<NewsModel> dataList;
    private final Activity activity;

    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null ;

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }
    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

    public NewsAdapter(Activity activity, ArrayList<NewsModel> dataList)
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

        if(viewType == NewsViewType.BASIC_VIEW)
        {
            view = inflater.inflate(R.layout.basic_view, parent, false);
            return new BasicViewHolder(view);
        }
        else if(viewType == NewsViewType.AD_VIEW)
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
                    .placeholder(R.drawable.newstong)
                    .override(800,500)
                    .centerCrop()
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
                    .placeholder(R.drawable.newstong)
                    .override(500,400)
                    .centerCrop()
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

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition() ;
                    if (position != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener.onItemClick(view, position) ;
                        }
                    }
                }
            });

            content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition() ;
                    if (position != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener.onItemClick(view, position) ;
                        }
                    }
                }
            });
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

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition() ;
                    if (position != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener.onItemClick(view, position) ;
                        }
                    }
                }
            });

            content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition() ;
                    if (position != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener.onItemClick(view, position) ;
                        }
                    }
                }
            });
        }
    }

}
