package com.etomato.assignment;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.etomato.assignment.Main.MainInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity
{
    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    ArrayList<MainData> dataArrayList = new ArrayList<>();
    MainAdapter adapter;

    //필요파라미터
    String CateName = "속보";
    int c_id = 2;
    int deskid = 0;
    String order = "spot";
    int userid = 0;
    int page = 1;
    int perPageCount = 15;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nestedScrollView = findViewById(R.id.scroll_view);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);

        adapter = new MainAdapter(MainActivity.this, dataArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        getData(CateName, c_id, deskid, order, userid, page, perPageCount);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener()
        {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY)
            {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())
                {
                    Toast.makeText(getApplicationContext(), page+1+"페이지",Toast.LENGTH_SHORT).show();
                    page++;
                    progressBar.setVisibility(View.VISIBLE);
                    getData(CateName, c_id, deskid, order, userid, page, perPageCount);
                }
            }
        });
    }

    private void getData(String CateName, int c_id, int deskid, String order, int userid, int page, int perPageCount)
    {
        // 레트로핏 초기화
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://dev.newstong.co.kr/")
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .build();
//        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://dev.newstong.co.kr/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MainInterface mainInterface = retrofit.create(MainInterface.class);
        Call<String> call = mainInterface.string_call(CateName, c_id, deskid, order, userid, page, perPageCount);
        call.enqueue(new Callback<String>()
        {
            @Override
            public void onResponse(Call<String> call, Response<String> response)
            {
                if (response.isSuccessful() && response.body() != null)
                {
                    progressBar.setVisibility(View.GONE);
                    try
                    {
                        JSONArray jsonArray = new JSONArray(response.body());
                        parseResult(jsonArray);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t)
            {
                Log.e("에러 : ", t.getMessage());
            }
        });
    }

    private void parseResult(JSONArray jsonArray)
    {
        for (int i = 0; i < jsonArray.length(); i++)
        {
            try
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                MainData data = new MainData();

                JSONArray imageJA = jsonObject.getJSONArray("NewsTongImageList");
                JSONObject imgJSONObject = imageJA.getJSONObject(0);
                data.setImage(imgJSONObject.getString("ImageUrl"));

                data.setName(jsonObject.getString("Title"));
                dataArrayList.add(data);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            adapter = new MainAdapter(MainActivity.this, dataArrayList);
            recyclerView.setAdapter(adapter);
        }
    }

}