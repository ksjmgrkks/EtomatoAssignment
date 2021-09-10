package com.etomato.assignment.Main.View.Fragment;
import static android.content.Context.INPUT_METHOD_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import android.widget.Button;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.etomato.assignment.Main.Service.MyFirebaseMessagingService;
import com.etomato.assignment.R;
import com.etomato.assignment.Main.Data.TimeLineModel;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteFragment extends Fragment {

    // DB 레퍼런스 설정
    private DatabaseReference mDatabase;
    //데이터베이스의 id값 설정을 위함
    long maxID=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_write, container, false);

        //키보드에 따라 UI가 바뀌도록 설정하는 코드
       getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        //db 인스턴스화
        mDatabase = FirebaseDatabase.getInstance().getReference().child("timeline");
        //db 읽기
        dbRead();

        Button buttonWrite = (Button) view.findViewById(R.id.button_write);
        EditText editTextTitle = (EditText) view.findViewById(R.id.editText_title);
        EditText editTextContents = (EditText) view.findViewById(R.id.editText_contents);
        ConstraintLayout bottomMenu = (ConstraintLayout) view.findViewById(R.id.bottom_menu);
        Button buttonPrevious = (Button) view.findViewById(R.id.button_move_previous);
        Button buttonNext = (Button) view.findViewById(R.id.button_move_next);
        Button buttonConfirm = (Button) view.findViewById(R.id.button_confirm);
        NestedScrollView sv = (NestedScrollView) view.findViewById(R.id.scroll_view);

        bottomMenu.setVisibility(View.INVISIBLE);

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //키보드 내리기
                InputMethodManager immhide = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                bottomMenu.setVisibility(View.INVISIBLE);
            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextContents.requestFocus();
            }
        });
        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextTitle.requestFocus();
            }
        });
        editTextContents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomMenu.setVisibility(View.VISIBLE);
            }
        });

        editTextTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean gainFocus) {
                //포커스가 주어졌을 때
                if (gainFocus) {
                    //to do
                    buttonPrevious.setVisibility(View.GONE);
                    buttonNext.setVisibility(View.VISIBLE);
                    bottomMenu.setVisibility(View.VISIBLE);
                    buttonConfirm.setVisibility(View.GONE);
                }
                //포커스를 잃었을 때
                else {
                    bottomMenu.setVisibility(View.INVISIBLE);
                }
            }
        });
        editTextContents.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean gainFocus) {
                //포커스가 주어졌을 때
                if (gainFocus) {
                    //to do
                    bottomMenu.setVisibility(View.VISIBLE);
                    buttonPrevious.setVisibility(View.VISIBLE);
                    buttonNext.setVisibility(View.GONE);
                    buttonConfirm.setVisibility(View.VISIBLE);
                }
                //포커스를 잃었을 때
                else {
                    bottomMenu.setVisibility(View.INVISIBLE);
                }
            }
        });


        // 쓰기 버튼을 눌렀을 때
        buttonWrite.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              long now = System.currentTimeMillis();
              Date mDate = new Date(now);
              @SuppressLint("SimpleDateFormat")
              SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");

              String getTime = simpleDate.format(mDate);

              String title = editTextTitle.getText().toString();
              String contents = editTextContents.getText().toString();

              addTimeline(getTime, title, contents);

              FirebaseMessaging.getInstance().getToken()
                      .addOnCompleteListener(new OnCompleteListener<String>() {
                          @Override
                          public void onComplete(@NonNull Task<String> task) {
                              if (!task.isSuccessful()) {
                                  Toast.makeText(getActivity(),"fcm 실패",Toast.LENGTH_SHORT).show();
                                  return;
                              }
                              // Get new FCM registration token
                              String token = task.getResult();
                              // Log and toast
                              String msg = getString(R.string.msg_token_fmt, token);
                              Log.d("토큰", msg);
                          }
                      });

              editTextTitle.setText("");
              editTextContents.setText("");

          }
        });
        return view;
    }

    public void dbRead(){
        // 데이터베이스 읽기
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //행의 개수 불러오기
                    maxID = (dataSnapshot.getChildrenCount());
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(getActivity(),""+error+"",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //값을 파이어베이스 Realtime database로 넘기는 함수
    public void addTimeline(String date, String title, String contents) {
        TimeLineModel timeline = new TimeLineModel(date,title,contents);
        //child는 해당 키 위치로 이동하는 함수입니다.
        //키가 없는데 "timeline"와 name같이 값을 지정한 경우 자동으로 생성합니다.
        mDatabase.child(String.valueOf(maxID+1)).setValue(timeline);
    }



}