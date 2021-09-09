package com.etomato.assignment.ViewPager.Fragment;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etomato.assignment.Main.MainInterface;
import com.etomato.assignment.Main.Model;
import com.etomato.assignment.Main.MyAdapter;
import com.etomato.assignment.Main.ViewType;
import com.etomato.assignment.ArticleActivity;
import com.etomato.assignment.R;

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
public class NewsFragment extends Fragment {
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
    int page;
    int perPageCount = 16;
    public NewsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //onCreate 는 액티비티가 destroy 되지 않는 한 한번만 호출된다.
        //하지만 onCreateView 는 프래그먼트가 다시 호출되면 계속 호출된다.
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        //1페이지로 초기화 -> 프래그먼트 생명주기를 고려한 설정
        page =1;
        //리사이클러뷰 레이아웃매니저, 어댑터 설정
        GridLayoutManager gridManager = new GridLayoutManager(getActivity(), 2);
        dataList = new ArrayList<>();
        myAdapter = new MyAdapter(getActivity(), dataList);

        RecyclerView newsRecyclerView = (RecyclerView) view.findViewById(R.id.news_rv);
        NestedScrollView nestedScrollView = (NestedScrollView) view.findViewById(R.id.news_sv);

        newsRecyclerView.setLayoutManager(gridManager);
        newsRecyclerView.setAdapter(myAdapter);

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

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    //스크롤을 최대로 내렸을때 page 파라미터가 증가하며 다음페이지에 해당하는 내용을 불러온다.
                    page++;
                    getData(CateName, c_id, deskid, order, userid, page, perPageCount);
                }
            }
        });

        myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int itemPosition) {
                // 아이템에 있는 메뉴를 클릭했을 때 실행하는 메소드
                Intent intent = new Intent(getActivity(), ArticleActivity.class);
                intent.putExtra("ImageUrl",dataList.get(itemPosition).getImage());
                intent.putExtra("Title",dataList.get(itemPosition).getContent());
                intent.putExtra("NewsLink",dataList.get(itemPosition).getLink());
                startActivity(intent);

                Toast.makeText(getActivity(),dataList.get(itemPosition).getLink(),Toast.LENGTH_SHORT).show();
            }
        }) ;
        return view;
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
                //링크
                basicData.setLink(jsonObject.getString("NewsLink"));
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
                gridData.setLink(jsonObject.getString("NewsLink"));
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