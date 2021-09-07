package com.etomato.assignment.ViewPager.Fragment;
import android.widget.Toast;
import android.widget.Button;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.etomato.assignment.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WriteFragment extends Fragment {

    // 파이어베이스 데이터베이스 연동
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    //DatabaseReference는 데이터베이스의 특정 위치로 연결하는 거라고 생각하면 된다.
    //현재 연결은 데이터베이스에만 딱 연결해놓고
    //키값(테이블 또는 속성)의 위치 까지는 들어가지는 않은 모습이다.
    private DatabaseReference databaseReference = database.getReference();

    // TODO: 매개변수와 인수 이름 변경하기, 일치하는 이름 선택하기
    // 프래그먼트 초기화 매개변수, ex) ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: 매개변수 이름 변경 및 데이터 타입 변경하기
    private String mParam1;
    private String mParam2;

    public WriteFragment() {
        // 비어있는 public 생성자가 필요함
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * 제공된 매개변수를 이용한 이 프래그먼트의 새로운 인스턴스를 생성하기 위해
     * 이 팩토리 메서드를 사용하세요.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WriteFragment.
     */
    // TODO: 매개변수 변경해주기
    public static WriteFragment newInstance(String param1, String param2) {
        WriteFragment fragment = new WriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_write, container, false);

        Button buttonWrite = (Button) view.findViewById(R.id.button_write);
        EditText editTextTitle = (EditText) view.findViewById(R.id.editText_title);
        EditText editTextContents = (EditText) view.findViewById(R.id.editText_contents);

        buttonWrite.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              long now = System.currentTimeMillis();
              Date mDate = new Date(now);
              SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
              String getTime = simpleDate.format(mDate);

              String title = editTextTitle.getText().toString();
              String contents = editTextContents.getText().toString();

              addTimeline(getTime, title, contents);

              editTextTitle.setText("");
              editTextContents.setText("");
              Toast.makeText( getActivity(),"글이 등록되었습니다",Toast.LENGTH_SHORT).show();
          }
        });


        RecyclerView recyclerViewTimeLine = (RecyclerView) view.findViewById(R.id.recyclerView_timeline);



        return view;
    }


    //값을 파이어베이스 Realtime database로 넘기는 함수
    public void addTimeline(String date, String title, String contents) {

        //여기에서 직접 변수를 만들어서 값을 직접 넣는것도 가능합니다.
        // ex) 갓 태어난 동물만 입력해서 int age=1; 등을 넣는 경우

        //animal.java에서 선언했던 함수.
        TimeLineModel timeline = new TimeLineModel(date,title,contents);

        //child는 해당 키 위치로 이동하는 함수입니다.
        //키가 없는데 "timeline"와 name같이 값을 지정한 경우 자동으로 생성합니다.
        databaseReference.child("timeline").push().setValue(timeline);

    }
}