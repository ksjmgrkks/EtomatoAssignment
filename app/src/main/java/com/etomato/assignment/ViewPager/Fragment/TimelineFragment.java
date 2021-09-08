package com.etomato.assignment.ViewPager.Fragment;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.Toast;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
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
import com.etomato.assignment.Main.ViewType;
import com.etomato.assignment.MainActivity;
import com.etomato.assignment.R;
import com.github.vipulasri.timelineview.TimelineView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TimelineFragment extends Fragment {
    //객체 초기화 설정
    ArrayList<TimeLineModel> dataList;
    TimeLineAdapter myAdapter;
    DatabaseReference mDatabase;
    Query myMostViewedPostsQuery;
    Button buttonSort;
    int filter=1;

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
        myMostViewedPostsQuery =  mDatabase.child("timeline").orderByChild("title");
//        if(filter == 2){
//            myMostViewedPostsQuery =  mDatabase.child("timeline").orderByChild("title");
//        }else if (filter == 3){
//            myMostViewedPostsQuery =  mDatabase.child("timeline").orderByChild("contents");
//        }else{
//            myMostViewedPostsQuery =  mDatabase.child("timeline");
//        }
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
                    filter = 1;
                }else if(selectedText.equals("제목순")){
                    buttonSort.setText("제목순");
                    filter = 2;
                }else{
                    buttonSort.setText("내용순");
                    filter = 3;
                }
            }
        });
        builder.show();
    }
}