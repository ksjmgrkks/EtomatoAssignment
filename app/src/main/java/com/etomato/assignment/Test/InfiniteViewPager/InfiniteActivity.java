package com.etomato.assignment.Test.InfiniteViewPager;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.etomato.assignment.Main.Data.NewsInterface;
import com.etomato.assignment.Main.Data.NewsModel;
import com.etomato.assignment.Main.Data.NewsViewType;
import com.etomato.assignment.Main.Data.ViewPagerAdapter;
import com.etomato.assignment.Main.View.Activity.ArticleActivity;
import com.etomato.assignment.R;
import com.google.android.material.slider.Slider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class InfiniteActivity extends AppCompatActivity {
    // 참고: https://toyknight.net/looping-view-pager/

    private ViewPager viewPager ;
    private TestViewPagerAdapter pagerAdapter ;
    ArrayList<NewsModel> newsDataList;

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
        setContentView(R.layout.activity_infinite);

//        newsDataList = new ArrayList<>();
//        for (int i = 0; i < 7; i++){
//            NewsModel basicData = new NewsModel();
//            basicData.setContent(""+i+"");
//            newsDataList.add(basicData);
//        }


        viewPager = (ViewPager) findViewById(R.id.viewPager) ;
        newsDataList = new ArrayList<>();
        pagerAdapter = new TestViewPagerAdapter(this, newsDataList);
        viewPager.setAdapter(pagerAdapter) ;
        viewPager.setCurrentItem(1,false);
        viewPager.addOnPageChangeListener(listener);

        getData(CateName, c_id, deskid, order, userid, page, perPageCount);

        pagerAdapter.setOnItemClickListener(new TestViewPagerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                // 아이템에 있는 메뉴를 클릭했을 때 실행하는 메소드
                Intent intent = new Intent(InfiniteActivity.this, ArticleActivity.class);
                intent.putExtra("ImageUrl", newsDataList.get(position).getImage());
                intent.putExtra("Title", newsDataList.get(position).getContent());
                intent.putExtra("NewsLink", newsDataList.get(position).getLink());
                startActivity(intent);
//                Toast.makeText(getActivity(), newsDataList.get(itemPosition).getLink(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        private int jumpPosition = -1;

        @Override
        public void onPageScrolled(int position,
                                   float positionOffset,
                                   int positionOffsetPixels) {
            // We do nothing here.
        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                // prepare to jump to the last page
                jumpPosition = pagerAdapter.getRealCount();

                //TODO: indicator.setActive(adapter.getRealCount() - 1)
            } else if (position == pagerAdapter.getRealCount() + 1) {
                //prepare to jump to the first page
                jumpPosition = 1;

                //TODO: indicator.setActive(0)
            } else {
                //TODO: indicator.setActive(position - 1)
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //Let's wait for the animation to complete then do the jump.
            if (jumpPosition >= 0
                    && state == ViewPager.SCROLL_STATE_IDLE) {
                // Jump without animation so the user is not
                // aware what happened.
                viewPager.setCurrentItem(jumpPosition, false);
                // Reset jump position.
                jumpPosition = -1;
            }
        }
    };


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
        NewsInterface newsInterface = retrofit.create(NewsInterface.class);
        Call<String> call = newsInterface.string_call(CateName, c_id, deskid, order, userid, page, perPageCount);
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
                NewsModel basicData = new NewsModel();
                JSONObject jsonObject = jsonArray.getJSONObject(formerNewsItem);
                //이미지
                JSONArray imageJSONArray = jsonObject.getJSONArray("NewsTongImageList");
                JSONObject imageJSONObject = imageJSONArray.getJSONObject(0);
                basicData.setImage(imageJSONObject.getString("ImageUrl"));
                //제목
                basicData.setContent(jsonObject.getString("Title"));
                //링크
                basicData.setLink(jsonObject.getString("Seq"));
                //뷰타입
                basicData.setViewType(NewsViewType.BASIC_VIEW);
                newsDataList.add(basicData);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        pagerAdapter.notifyDataSetChanged();
    }

}