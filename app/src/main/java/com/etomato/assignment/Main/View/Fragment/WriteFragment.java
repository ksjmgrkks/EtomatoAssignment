package com.etomato.assignment.Main.View.Fragment;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.Button;

import android.os.Bundle;

import androidx.annotation.NonNull;
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

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // 알람을 보여주기위한 채널 생성
//            String channelId  = getString(R.string.default_notification_channel_id);
//            String channelName = getString(R.string.default_notification_channel_name);
//            NotificationManager notificationManager =
//                    getActivity().getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
//                    channelName, NotificationManager.IMPORTANCE_LOW));
//        }
//
//        // 알림 메시지를 탭하면 알림에 수반되는 모든 데이터
//        // 메시지는 의도 엑스트라에서 사용할 수 있습니다. 이 샘플에서 런처
//        // 알림을 탭하면 인텐트가 실행되므로 수반되는 모든 데이터는
//        // 여기에서 처리합니다. 다른 인텐트를 실행하려면 click_action을 설정하세요.
//        // 원하는 인텐트에 대한 알림 메시지의 필드입니다. 런처 의도
//        // click_action이 지정되지 않은 경우에 사용됩니다.
//        // //
//        // 알림 메시지와 함께 가능한 데이터를 처리합니다.
//        // [START 핸들_데이터_엑스트라]
//        if (getActivity().getIntent().getExtras() != null) {
//            for (String key : getActivity().getIntent().getExtras().keySet()) {
//                Object value = getActivity().getIntent().getExtras().get(key);
//                Log.d("WF", "Key: " + key + " Value: " + value);
//            }
//        }
        // [END 핸들_데이터_엑스트라]

        //db 인스턴스화
        mDatabase = FirebaseDatabase.getInstance().getReference().child("timeline");
        //db 읽기
        dbRead();

        Button buttonWrite = (Button) view.findViewById(R.id.button_write);
        EditText editTextTitle = (EditText) view.findViewById(R.id.editText_title);
        EditText editTextContents = (EditText) view.findViewById(R.id.editText_contents);

        // 쓰기 버튼을 눌렀을 때
        buttonWrite.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Toast.makeText(getActivity(),"글이 등록되었습니다",Toast.LENGTH_SHORT).show();

              long now = System.currentTimeMillis();
              Date mDate = new Date(now);
              SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
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
                              Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
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