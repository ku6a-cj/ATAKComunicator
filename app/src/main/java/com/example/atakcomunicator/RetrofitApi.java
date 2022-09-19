package com.example.atakcomunicator;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface RetrofitApi {
    // as we are making a put request to update a data
    // so we are annotating it with put
    // and along with that we are passing a parameter as users
    @PUT("v3/b2412242-52d3-4684-af74-0634d8ff04ae")
    // api/users/2
//https://run.mocky.io/v3/b2412242-52d3-4684-af74-0634d8ff04ae
    // on below line we are creating a method to put our data.
    Call<com.example.atakcomunicator.DataModel2> updateData(@Body com.example.atakcomunicator.DataModel2 dataModel2);
}
