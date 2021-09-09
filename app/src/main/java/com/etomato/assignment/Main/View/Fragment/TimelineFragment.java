package com.etomato.assignment.Main.View.Fragment;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.Toast;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etomato.assignment.R;
import com.etomato.assignment.Main.Data.TimeLineAdapter;
import com.etomato.assignment.Main.Data.TimeLineModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TimelineFragment extends Fragment {
    //객체 초기화 설정
    ArrayList<TimeLineModel> dataList;
    TimeLineAdapter myAdapter;
    DatabaseReference mDatabase;
    Query myMostViewedPostsQuery;
    Button buttonSort;

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

        RecyclerView recyclerViewTimeLine = (RecyclerView) view.findViewById(R.id.recyclerView_timeline);
        buttonSort = (Button) view.findViewById(R.id.button_sort) ;

        buttonSort.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              showDialog();
          }
        });

        //리사이클러뷰 레이아웃매니저, 어댑터 설정
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        dataList = new ArrayList<>();
        myAdapter = new TimeLineAdapter(dataList);

        recyclerViewTimeLine.setLayoutManager(manager);
        recyclerViewTimeLine.setAdapter(myAdapter);

        //db 인스턴스화
        mDatabase = FirebaseDatabase.getInstance().getReference();
        myMostViewedPostsQuery =  mDatabase.child("timeline");

        //db 읽기 (실시간 반영)
        dbRead();

        return view;
    }

    public void dbRead(){
        // 데이터베이스 읽기
        myMostViewedPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    dataList.clear();

                    //대입받을 변수정의 : 배열명
                    //참고 : https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=highkrs&logNo=220276708627
                    for(DataSnapshot timelineSnapshot : dataSnapshot.getChildren()){
                        TimeLineModel data = timelineSnapshot.getValue(TimeLineModel.class);
                        dataList.add(data);
                    }
                    myAdapter.notifyDataSetChanged();

                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(getActivity(),""+error+"",Toast.LENGTH_SHORT).show();
            }
        });
    }

    void showDialog(){
        final List<String> ListItems = new ArrayList<>();
        ListItems.add("최신순");
        ListItems.add("제목순");
        ListItems.add("내용순");
        final CharSequence[] items =  ListItems.toArray(new String[ ListItems.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("타임라인 정렬");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                String selectedText = items[pos].toString();
                if(selectedText.equals("최신순")){
                      buttonSort.setText("최신순");
                    myMostViewedPostsQuery =  mDatabase.child("timeline");
                }else if(selectedText.equals("제목순")){
                      buttonSort.setText("제목순");
                      myMostViewedPostsQuery =  mDatabase.child("timeline").orderByChild("title");
                }else{
                    buttonSort.setText("내용순");
                    myMostViewedPostsQuery =  mDatabase.child("timeline").orderByChild("contents");
                }
            }
        });
        builder.show();
    }
}