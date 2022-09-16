package com.example.httpconnect;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MyAPI {

    //https://run.mocky.io/v3/21cec39f-4443-496a-a8a3-058008643d7b

    @GET("v3/21cec39f-4443-496a-a8a3-058008643d7b")
    Call<DataModel>getData();

}
