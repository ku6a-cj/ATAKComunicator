package com.example.httpconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class POST extends AppCompatActivity {

    String url = "https://run.mocky.io/";
    //https://reqres.in/

    // creating our variables for our views such
    // as text view, button and progress
    // bar and response text view.
    private EditText userNameEdt, jobEdt;
    private Button updateBtn;
    private ProgressBar loadingPB;
    private TextView responseTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
// initializing our views with their ids.
        userNameEdt = findViewById(R.id.idEdtUserName);
        jobEdt = findViewById(R.id.idEdtJob);
        updateBtn = findViewById(R.id.idBtnUpdate);
        loadingPB = findViewById(R.id.idPBLoading);
        responseTV = findViewById(R.id.idTVResponse);

        // adding on click listener for our button.
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking if the edit text is empty or not.
                if (TextUtils.isEmpty(userNameEdt.getText().toString()) && TextUtils.isEmpty(jobEdt.getText().toString())) {

                    // displaying a toast message if the edit text is empty.
                    Toast.makeText(POST.this, "Please enter your data..", Toast.LENGTH_SHORT).show();
                    return;
                }

                // calling a method to update data in our API.
                callPUTDataMethod(userNameEdt.getText().toString(), jobEdt.getText().toString());
            }
        });
    }

    private void callPUTDataMethod(String userName, String job) {

        // below line is for displaying our progress bar.
        loadingPB.setVisibility(View.VISIBLE);

        // on below line we are creating a retrofit
        // builder and passing our base url
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)

                // as we are sending data in json format so
                // we have to add Gson converter factory
                .addConverterFactory(GsonConverterFactory.create())

                // at last we are building our retrofit builder.
                .build();

        // below the line is to create an instance for our retrofit api class.
        RetrofitApi retrofitAPI = retrofit.create(RetrofitApi.class);

        // passing data from our text fields to our modal class.
        DataModel2 modal = new DataModel2(userName, job);

        // calling a method to create an update and passing our modal class.
        Call<DataModel2> call = retrofitAPI.updateData(modal);

        // on below line we are executing our method.
        call.enqueue(new Callback<DataModel2>() {
            @Override
            public void onResponse(Call<DataModel2> call, Response<DataModel2> response) {
                // this method is called when we get response from our api.
                Toast.makeText(POST.this, "Data updated to API", Toast.LENGTH_SHORT).show();

                // below line is for hiding our progress bar.
                loadingPB.setVisibility(View.GONE);

                // on below line we are setting empty
                // text to our both edit text.
                jobEdt.setText("");
                userNameEdt.setText("");

                // we are getting a response from our body and
                // passing it to our modal class.
                DataModel2 responseFromAPI = response.body();

                // on below line we are getting our data from modal class
                // and adding it to our string.
                String responseString = "Response Code : " + response.code() + "\nName : " + responseFromAPI.getName() + "\n" + "Job : " + responseFromAPI.getJob();

                // below line we are setting our string to our text view.
                responseTV.setText(responseString);
            }

            @Override
            public void onFailure(Call<DataModel2> call, Throwable t) {

                // setting text to our text view when
                // we get error response from API.
                responseTV.setText("Error found is : " + t.getMessage());
            }
        });
    }
}