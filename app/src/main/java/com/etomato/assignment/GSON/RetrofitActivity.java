package com.etomato.assignment.GSON;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.etomato.assignment.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitActivity extends AppCompatActivity {
    String TAG = "Retrofit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        //Retrofit 인스턴스 생성
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("http://dev.newstong.co.kr/")    // baseUrl 등록
                .addConverterFactory(GsonConverterFactory.create())  // Gson 변환기 등록
                .build();

        RetrofitInterface service = retrofit.create(RetrofitInterface.class);   // 레트로핏 인터페이스 객체 구현

        Button btn_sendIdx = findViewById(R.id.btn_sendIdx);    // 전송 버튼
        btn_sendIdx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(TAG, "버튼 클릭");

                // edittext로부터 유저가 입력한 idx값을 가져온다
                EditText editText_idx = findViewById(R.id.editText);
                String idx = editText_idx.getText().toString(); // 서버로 보낼 idx

                Call<DataClass> call = service.getName(idx);

                call.enqueue(new Callback<DataClass>() {
                    @Override
                    public void onResponse(Call<DataClass> call, Response<DataClass> response) {
                        Log.e(TAG, "onResponse");
                        if(response.isSuccessful()){
                            Log.e(TAG, "onResponse success");
                            DataClass result = response.body();

                            // 서버에서 응답받은 데이터를 TextView에 넣어준다.
                            TextView Title = findViewById(R.id.title);
                            TextView NewsLink = findViewById(R.id.body);

                            Title.setText(result.Title);
                            NewsLink.setText(result.NewsLink);

                        }
                        else{
                            // 실패
                            Log.e(TAG, "onResponse fail");
                        }
                    }

                    @Override
                    public void onFailure(Call<DataClass> call, Throwable t) {
                        // 통신 실패
                        Log.e(TAG, "onFailure: " + t.getMessage());
                    }
                });
            }
        });
    }
}