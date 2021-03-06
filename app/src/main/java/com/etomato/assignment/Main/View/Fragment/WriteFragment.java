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

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteFragment extends Fragment {

    //FCM ??????
    private static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_KEY = "AAAAN67g20Q:APA91bGcykO1ZDHNhmPE-4csGEzzKMjL0iZDDwaUBtePQEO1yAooxf9HSN2IyKdU9ZEgEBfJ9fOw-nZicXAnNazbKVElKgUyO5mFpfHn45MyZS3KDR5TpBhV7KvRWsdrkX1LE-gi0xfO";
    // DB ???????????? ??????
    private DatabaseReference mDatabase;
    //????????????????????? id??? ????????? ??????
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


        //???????????? ?????? UI??? ???????????? ???????????? ??????
        //?????? : https://medium.com/@madalinnita/android-how-to-move-views-above-keyboard-when-its-opened-quick-secure-solution-90188c4d7b15
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams. SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams. SOFT_INPUT_ADJUST_RESIZE );

        //db ???????????????
        mDatabase = FirebaseDatabase.getInstance().getReference().child("timeline");
        //db ??????
        dbRead();

        Button buttonWrite = (Button) view.findViewById(R.id.button_write);
        EditText editTextTitle = (EditText) view.findViewById(R.id.editText_title);
        EditText editTextContents = (EditText) view.findViewById(R.id.editText_contents);
        ConstraintLayout bottomMenu = (ConstraintLayout) view.findViewById(R.id.bottom_menu);
        Button buttonPrevious = (Button) view.findViewById(R.id.button_move_previous);
        Button buttonNext = (Button) view.findViewById(R.id.button_move_next);
        Button buttonConfirm = (Button) view.findViewById(R.id.button_confirm);

        bottomMenu.setVisibility(View.INVISIBLE);



        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //????????? ?????????
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
                //???????????? ???????????? ???
                if (gainFocus) {
                    //to do
                    buttonPrevious.setVisibility(View.GONE);
                    buttonNext.setVisibility(View.VISIBLE);
                    bottomMenu.setVisibility(View.VISIBLE);
                    buttonConfirm.setVisibility(View.GONE);
//                    bottomMenu.setFocusable(true);
                }
                //???????????? ????????? ???
                else {
                    bottomMenu.setVisibility(View.INVISIBLE);
                }
            }
        });
        editTextContents.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean gainFocus) {
                //???????????? ???????????? ???
                if (gainFocus) {
                    //to do
                    bottomMenu.setVisibility(View.VISIBLE);
                    buttonPrevious.setVisibility(View.VISIBLE);
                    buttonNext.setVisibility(View.GONE);
                    buttonConfirm.setVisibility(View.VISIBLE);
//                    bottomMenu.setFocusable(true);
                }
                //???????????? ????????? ???
                else {
                    bottomMenu.setVisibility(View.INVISIBLE);
                }
            }
        });


        // ?????? ????????? ????????? ???
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
                                  Toast.makeText(getActivity(),"fcm ??????",Toast.LENGTH_SHORT).show();
                                  return;
                              }
                              // Get new FCM registration token
                              String token = task.getResult();
                              // Log and toast
                              String msg = getString(R.string.msg_token_fmt, token);
                              Log.d("??????", msg);
                              sendPostToFCM("??????????????? ?????? ?????????????????????.", token);
                          }
                      });


              editTextTitle.setText("");
              editTextContents.setText("");

          }
        });
        return view;
    }

    public void dbRead(){
        // ?????????????????? ??????
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //?????? ?????? ????????????
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

    //?????? ?????????????????? Realtime database??? ????????? ??????
    public void addTimeline(String date, String title, String contents) {
        TimeLineModel timeline = new TimeLineModel(date,title,contents);
        //child??? ?????? ??? ????????? ???????????? ???????????????.
        //?????? ????????? "timeline"??? name?????? ?????? ????????? ?????? ???????????? ???????????????.
        mDatabase.child(String.valueOf(maxID+1)).setValue(timeline);
    }

    private void sendPostToFCM(String message, String token) {
        //?????? ?????? : https://anhana.tistory.com/7?category=701004
        // ????????? ????????? ?????? ???????????? ??????????????????!
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // FMC ????????? ?????? start
                    JSONObject root = new JSONObject();
                    JSONObject notification = new JSONObject();
                    notification.put("body", message);
                    notification.put("title", getString(R.string.app_name));
                    root.put("notification", notification);
                    root.put("to", token);
                    // FMC ????????? ?????? end
                    URL Url = new URL(FCM_MESSAGE_URL);
                    HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.addRequestProperty("Authorization", "key=" + SERVER_KEY);
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestProperty("Content-type", "application/json");
                    OutputStream os = conn.getOutputStream();
                    os.write(root.toString().getBytes("utf-8"));
                    os.flush();
                    conn.getResponseCode();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



    }