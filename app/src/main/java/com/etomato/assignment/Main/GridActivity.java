package com.etomato.assignment.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.etomato.assignment.databinding.ActivityGridBinding;

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

public class GridActivity extends AppCompatActivity {

    //뷰바인딩 초기화 설정
    private ActivityGridBinding binding;

    //객체 초기화 설정
    ArrayList<Model> dataList;
    MyAdapter myAdapter;

    //API 주소
    String baseURL = "http://dev.newstong.co.kr/";
    //필요파라미터 설정
    String CateName = "속보";
    int c_id = 2;
    int deskid = 0;
    String order = "spot";
    int userid = 0;
    int page = 1;
    int perPageCount = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inflate로 xml에 있는 뷰들을 객체화해줌
        binding = ActivityGridBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //리사이클러뷰 레이아웃매니저, 어댑터 설정
        GridLayoutManager gridManager = new GridLayoutManager(this, 2);
        dataList = new ArrayList<>();
        myAdapter = new MyAdapter(GridActivity.this, dataList);
        binding.RecyclerView.setLayoutManager(gridManager);
        binding.RecyclerView.setAdapter(myAdapter);

        //뷰타입에 따라 GridLayout의 SpanSize를 다르게 설정하는 부분
        gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int type = myAdapter.getItemViewType(position);
                if (type == ViewType.GRID_VIEW)
                    return 1;
                else
                    return 2;
            }
        });
        getData(CateName, c_id, deskid, order, userid, page, perPageCount);

        binding.scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    //스크롤을 최대로 내렸을때 page 파라미터가 증가하며 다음페이지에 해당하는 내용을 불러온다.
                    page++;
                    binding.progressBar.setVisibility(View.VISIBLE);
                    getData(CateName, c_id, deskid, order, userid, page, perPageCount);
                }
            }
        });
    }

    //Retrofit 라이브러리를 통해 JSONArray를 호출하는 메서드
    private void getData(String CateName, int c_id, int deskid, String order, int userid, int page, int perPageCount)
    {
        // 레트로핏 초기화
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //레트로핏 라이브러리가 인터페이스를 해석해 HTTP 통신을 처리하는 과정
        MainInterface mainInterface = retrofit.create(MainInterface.class);
        Call<String> call = mainInterface.string_call(CateName, c_id, deskid, order, userid, page, perPageCount);
        call.enqueue(new Callback<String>()
        {
            @Override
            public void onResponse(Call<String> call, Response<String> response)
            {
                if (response.isSuccessful() && response.body() != null)
                {
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

    //JSONArray를 parsing한 후 리사이클러뷰에 데이터들을 담아 뿌려주는 메서드
    @SuppressLint("NotifyDataSetChanged")
    private void parseResult(JSONArray jsonArray)
    {
        for (int formerNewsItem = 0; formerNewsItem < 6 ; formerNewsItem++)
        {
            try
            {
                Model basicData = new Model();
                JSONObject jsonObject = jsonArray.getJSONObject(formerNewsItem);
                //이미지
                JSONArray imageJSONArray = jsonObject.getJSONArray("NewsTongImageList");
                JSONObject imageJSONObject = imageJSONArray.getJSONObject(0);
                basicData.setImage(imageJSONObject.getString("ImageUrl"));
                //제목
                basicData.setContent(jsonObject.getString("Title"));
                //뷰타입
                basicData.setViewType(ViewType.BASIC_VIEW);
                dataList.add(basicData);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        Model adData = new Model();
        adData.setViewType(ViewType.AD_VIEW);
        adData.setContent("광고글 페이지 ("+page+")");
        dataList.add(adData);

        for (int postNewsItem = 6 ; postNewsItem < 16 ; postNewsItem++)
        {
            try
            {
                Model gridData = new Model();
                JSONObject jsonObject = jsonArray.getJSONObject(postNewsItem);

                JSONArray imageJSONArray = jsonObject.getJSONArray("NewsTongImageList");
                JSONObject imageJSONObject = imageJSONArray.getJSONObject(0);
                gridData.setImage(imageJSONObject.getString("ImageUrl"));
                gridData.setContent(jsonObject.getString("Title"));
                gridData.setViewType(ViewType.GRID_VIEW);
                dataList.add(gridData);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        myAdapter.notifyDataSetChanged();
    }
}