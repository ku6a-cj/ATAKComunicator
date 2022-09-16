package com.example.httpconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        Button button1 = findViewById(R.id.button);
        button1.setOnClickListener(view -> goToAnActivity());

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(view -> goToAnActivity2());

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(view -> goToAnActivity3());

    }

    public void goToAnActivity() {
        Intent intent = new Intent(this, HttpConnection.class);
        startActivity(intent);
    }

    public void goToAnActivity2() {
        Intent intent = new Intent(this, activity_retrofit_1.class);
        startActivity(intent);
    }

    public void goToAnActivity3() {
        Intent intent = new Intent(this, POST.class);
        startActivity(intent);
    }

}