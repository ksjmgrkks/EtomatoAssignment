package com.etomato.assignment.ViewPager.Fragment;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.etomato.assignment.Main.Model;
import com.etomato.assignment.R;

import java.util.ArrayList;
import java.util.List;

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder> {
    private List<TimeLineModel> dataList = new ArrayList<>();
    public TimeLineAdapter(ArrayList<TimeLineModel> dataList)
    {
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public TimeLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_line_list, parent, false);
        final TimeLineViewHolder viewHolder = new TimeLineViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull TimeLineViewHolder holder, int position) {
        TimeLineModel item = dataList.get(position);
        // TODO : 데이터를 뷰홀더에 표시하시오
        holder.date.setText(item.getDate());
        holder.title.setText(item.getTitle());
        holder.contents.setText(item.getContents());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class TimeLineViewHolder extends RecyclerView.ViewHolder {
        // TODO : 뷰홀더 코드를 입력하여 주세요
        TextView date;
        TextView title;
        TextView contents;

        public TimeLineViewHolder(@NonNull View itemView) {
            super(itemView);
            // TODO : 뷰홀더 코드를 입력하여 주세요
            date = itemView.findViewById(R.id.textView_date);
            title = itemView.findViewById(R.id.textView_title);
            contents = itemView.findViewById(R.id.textView_contents);
        }
    }
}
