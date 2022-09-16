package com.example.httpconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class activity_retrofit_1 extends AppCompatActivity {

    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit1);

        txt = findViewById(R.id.textView2);

        //Retrofit Builder

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://run.mocky.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Instance for interface
        MyAPI MyAPICall = retrofit.create(MyAPI.class);

        Call<DataModel> call = MyAPICall.getData();

        call.enqueue(new Callback<DataModel>() {
            @Override
            public void onResponse(Call<DataModel> call, Response<DataModel> response) {
                // that will be called on response
                if (response.code()!=200){
                    txt.setText("Check out the connection");
                    return;
                }
                String jsony="";

                jsony = "ID: "+ response.body().getId()+
                        "\nUser ID: "+ response.body().getUserId()+
                        "\nTitle: "+ response.body().getTitle()+
                        "\nCompleted: "+ response.body().isCompleted();
                txt.append(jsony);


            }

            @Override
            public void onFailure(Call<DataModel> call, Throwable t) {
                // that will be called on failure
            }
        });

    }
}