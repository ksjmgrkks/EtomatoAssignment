package com.etomato.assignment.Main.Data;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsInterface
{
    @GET("newstongsecond/NewsList.aspx")
    Call<String> string_call(
            @Query("CateName") String CateName,
            @Query("c_id") int c_id,
            @Query("deskid") int deskid,
            @Query("order") String order,
            @Query("userid") int userid,
            @Query("page") int page,
            @Query("perPageCount") int perPageCount
    );
}
