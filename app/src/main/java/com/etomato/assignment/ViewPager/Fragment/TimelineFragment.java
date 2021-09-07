package com.etomato.assignment.ViewPager.Fragment;
import android.widget.Toast;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.autofill.AutofillValue;

import com.etomato.assignment.Main.Model;
import com.etomato.assignment.Main.MyAdapter;
import com.etomato.assignment.R;
import com.github.vipulasri.timelineview.TimelineView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TimelineFragment extends Fragment {
    //객체 초기화 설정
    ArrayList<TimeLineModel> dataList;
    TimeLineAdapter myAdapter;

    public TimelineFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);

        //db 객체 참조
        DatabaseReference Ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference timelineReference = Ref.child("timeline");

        //리사이클러뷰 레이아웃매니저, 어댑터 설정
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        dataList = new ArrayList<>();
        myAdapter = new TimeLineAdapter(dataList);

        RecyclerView recyclerViewTimeLine = (RecyclerView) view.findViewById(R.id.recyclerView_timeline);

        recyclerViewTimeLine.setLayoutManager(manager);
        recyclerViewTimeLine.setAdapter(myAdapter);


        for (int i = 1; i <= 5; i++){
            TimeLineModel data = new TimeLineModel();
            data.setDate(String.valueOf(i));
            data.setTitle(String.valueOf(i));
            data.setContents(String.valueOf(i));
            dataList.add(data);
            myAdapter.notifyItemInserted(0);
        }
        // Read from the database
        timelineReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                TimeLineModel data = new TimeLineModel();
                data.setDate(value);
                data.setTitle(value);
                data.setContents(value);
                dataList.add(data);
                myAdapter.notifyItemInserted(0);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(getActivity(),"1",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}