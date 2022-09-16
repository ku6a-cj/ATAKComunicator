package com.example.atakcomunicator;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.atakcomunicator.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static final int SERVER_PORT = 3003;

    // Get this IP from the Device WiFi Settings
    // Make sure you have the devices in same WiFi if testing locally
    // Or Make sure the port specified is open for connections.
    public String SERVER_IP = "192.168.137.8";
    public int AllyCounter;
    public int EnemyCounter;
    private ClientThread clientThread;
    private Thread thread;
    private LinearLayout msgList;
    private Handler handler;
    private int clientTextColor;
    private EditText edMessage;
    public boolean changedTemplate;
    public View TemplateView;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.atakcomunicator.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        AllyCounter = 0;
        EnemyCounter = 0;
        changedTemplate = false;
        setTitle("ATAKMessanger");
        clientTextColor = ContextCompat.getColor(this, R.color.green);
        handler = new Handler();
        msgList = findViewById(R.id.msgList);
        edMessage = findViewById(R.id.edMessage);
        edMessage.setHint("Tu wpisz IP serwer np." + "\"" + SERVER_IP + "\"");
        Button connectToServer = findViewById(R.id.btnConnectServer);
        Button sendMessage = findViewById(R.id.send_data);
        Button sendData2 = findViewById(R.id.send_data2);
        Button AllyButton = findViewById(R.id.buttonAlly);
        Button buttonAllyChange = findViewById(R.id.buttonAllyChange);
        Button buttonenemyChange = findViewById(R.id.send_data2Change);


        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clientMessage = edMessage.getText().toString().trim();
                showMessage(clientMessage, Color.BLUE);
                if (null != clientThread) {
                    clientThread.sendMessage(clientMessage);
                }
            }
        });


        connectToServer.setOnClickListener(view -> {
            SERVER_IP = edMessage.getText().toString();
            if (SERVER_IP.isEmpty()) {
                Toast.makeText(MainActivity.this, "Podales wartosc spoza przedzialu!", Toast.LENGTH_SHORT).show();
            }
            //Log.e("Podane IP",SERVER_IP);
            removeAllViews();
            showMessage("Connecting to Server...", clientTextColor);
            clientThread = new ClientThread();
            thread = new Thread(clientThread);
            thread.start();
            // showMessage("Connected to Server...", clientTextColor);
            hideConnectServerBtn();
            edMessage.setText("");
            edMessage.setHint("Tu wpisz wiadomosc do wysłania");
            edMessage.setInputType(InputType.TYPE_CLASS_TEXT);
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    //ta funcjkacja dziala wewnatrz przez co nie mg modyfikowac czasu jaki wysylam a sluzy jedynie do wyswietlania
    public TextView textView(String message, int color) {
        if (null == message || message.trim().isEmpty()) {
            message = "<Empty Message>";
        }
        String m = message + " [" + getTime() + "]";
        TextView tv = new TextView(this);
        tv.setTextColor(color);
        tv.setText(m);
        tv.setTextSize(20);
        tv.setPadding(0, 5, 0, 0);
        return tv;
    }

    private void hideConnectServerBtn() {
        handler.post(() -> findViewById(R.id.btnConnectServer).setVisibility(View.GONE));
    }


    public void showMessage(final String message, final int color) {
        handler.post(() -> msgList.addView(textView(message, color)));
    }

    private void removeAllViews() {
        handler.post(() -> msgList.removeAllViews());
    }

    public void changeEnemy(){
        TextView lat = findViewById(R.id.LatChange);
        TextView lonng = findViewById(R.id.LongChange);
        String isW = "0";
        String isS = "0";
        String LaT = lat.getText().toString();
        String LoNg = lonng.getText().toString();
        if(LaT.isEmpty() || LoNg.isEmpty()){
            Toast.makeText(this, "Nie wpisano tekstu do przeslania, uzupełnij pola tekstowe!",Toast.LENGTH_SHORT).show();
        }else if(Integer.parseInt(LaT)<=90 && Integer.parseInt(LaT)>=-90 && Integer.parseInt(LoNg)>=-180 && Integer.parseInt(LoNg)<=180 ){
            if (Integer.parseInt(LaT)<0 ){
                isS="1";
                LaT = String.valueOf(Integer.parseInt(LaT)*-1);
            }

            if (Integer.parseInt(LoNg)<0 ){
                isW="1";
                LoNg = String.valueOf(Integer.parseInt(LoNg)*-1);
            }
            String Message = "1-EFlag-"+LaT+"-"+LoNg+"-"+isS+"-"+isW+"-0";

                TextView edMessage = findViewById(R.id.edMessageChange);
                String id = edMessage.getText().toString();
                if(id.isEmpty()){
                    Toast.makeText(this, "Nie podales wartosci ID, wiec ustawiono na 0!",Toast.LENGTH_SHORT).show();
                    id="0";
                }
                edMessage.setText("");
                if(Integer.parseInt(id)>EnemyCounter){
                    Toast.makeText(this, "Podales wartosc spoza przedzialu!",Toast.LENGTH_SHORT).show();
                    Message = "Uzytkownik podal bledne dane";
                }else{
                    Message = "2-EFlag-"+LaT+"-"+LoNg+"-"+isS+"-"+isW+"-"+id;
                }


            String clientMessage = Message;

            if (null != clientThread) {
                clientThread.sendMessage(clientMessage);
            }
        }else{
            Toast.makeText(this, "Podales wartosc spoza przedzialu!",Toast.LENGTH_SHORT).show();
        }

        edMessage.setText("");

    }

    public void changeAlly(){
        TextView lat = findViewById(R.id.LatChange);
        TextView lonng = findViewById(R.id.LongChange);
        String isW = "0";
        String isS = "0";
        String LaT = lat.getText().toString();
        String LoNg = lonng.getText().toString();
        if(LaT.isEmpty() || LoNg.isEmpty()){
            Toast.makeText(this, "Nie wpisano tekstu do przeslania, uzupełnij pola tekstowe!",Toast.LENGTH_SHORT).show();
        }else if(Integer.parseInt(LaT)<=90 && Integer.parseInt(LaT)>=-90 && Integer.parseInt(LoNg)>=-180 && Integer.parseInt(LoNg)<=180 ){
            if (Integer.parseInt(LaT)<0 ){
                isS="1";
                LaT = String.valueOf(Integer.parseInt(LaT)*-1);
            }

            if (Integer.parseInt(LoNg)<0 ){
                isW="1";
                LoNg = String.valueOf(Integer.parseInt(LoNg)*-1);
            }
            String Message = "1-Flag-"+LaT+"-"+LoNg+"-"+isS+"-"+isW+"-0";

                TextView edMessage = findViewById(R.id.edMessageChange);
                String id = edMessage.getText().toString();
                if(id.isEmpty()){
                    Toast.makeText(this, "Nie podales wartosci ID, wiec ustawiono na 0!",Toast.LENGTH_SHORT).show();
                    id="0";
                }
                edMessage.setText("");
                if(Integer.parseInt(id)>AllyCounter){
                    Toast.makeText(this, "Podales wartosc spoza przedzialu!",Toast.LENGTH_SHORT).show();
                    Message = "Uzytkownik podal bledne dane";
                }else{
                    Message = "2-Flag-"+LaT+"-"+LoNg+"-"+isS+"-"+isW+"-"+id;
                }


            String clientMessage = Message;

            if (null != clientThread) {
                clientThread.sendMessage(clientMessage);
            }
        }else{
            Toast.makeText(this, "Podales wartosc spoza przedzialu!",Toast.LENGTH_SHORT).show();
        }

        edMessage.setText("");
    }



    public void setAlly(){
        TextView lat = findViewById(R.id.Lat);
        TextView lonng = findViewById(R.id.Long);
        String isW = "0";
        String isS = "0";
        String LaT = lat.getText().toString();
        String LoNg = lonng.getText().toString();
        if(LaT.isEmpty() || LoNg.isEmpty()){
            Toast.makeText(this, "Nie wpisano tekstu do przeslania, uzupełnij pola tekstowe!",Toast.LENGTH_SHORT).show();
        }else if(Integer.parseInt(LaT)<=90 && Integer.parseInt(LaT)>=-90 && Integer.parseInt(LoNg)>=-180 && Integer.parseInt(LoNg)<=180 ){
            if (Integer.parseInt(LaT)<0 ){
                isS="1";
                LaT = String.valueOf(Integer.parseInt(LaT)*-1);
            }

            if (Integer.parseInt(LoNg)<0 ){
                isW="1";
                LoNg = String.valueOf(Integer.parseInt(LoNg)*-1);
            }
            String Message = "1-Flag-"+LaT+"-"+LoNg+"-"+isS+"-"+isW+"-0";

            edMessage.setText(Message);
            String clientMessage = edMessage.getText().toString().trim();
            showMessage(clientMessage, Color.BLUE);
            if (null != clientThread) {
                clientThread.sendMessage(clientMessage);
            }
        }else{
            Toast.makeText(this, "Podales wartosc spoza przedzialu!",Toast.LENGTH_SHORT).show();
        }

        edMessage.setText("");

            AllyCounter = AllyCounter + 1;


    }

    public void sendData22() {
        TextView lat = findViewById(R.id.Lat);
        TextView lonng = findViewById(R.id.Long);
        String isW = "0";
        String isS = "0";
        String LaT = lat.getText().toString();
        String LoNg = lonng.getText().toString();
        if(LaT.isEmpty() || LoNg.isEmpty()){
            Toast.makeText(this, "Nie wpisano tekstu do przeslania, uzupełnij pola tekstowe!",Toast.LENGTH_SHORT).show();
        }else if(Integer.parseInt(LaT)<=90 && Integer.parseInt(LaT)>=-90 && Integer.parseInt(LoNg)>=-180 && Integer.parseInt(LoNg)<=180 ){
            if (Integer.parseInt(LaT)<0 ){
                isS="1";
                LaT = String.valueOf(Integer.parseInt(LaT)*-1);
            }

            if (Integer.parseInt(LoNg)<0 ){
                isW="1";
                LoNg = String.valueOf(Integer.parseInt(LoNg)*-1);
            }
            String Message = "1-EFlag-"+LaT+"-"+LoNg+"-"+isS+"-"+isW+"-0";
            edMessage.setText(Message);
            String clientMessage = edMessage.getText().toString().trim();
            showMessage(clientMessage, Color.BLUE);
            if (null != clientThread) {
                clientThread.sendMessage(clientMessage);
            }
        }else{
            Toast.makeText(this, "Podales wartosc spoza przedzialu!",Toast.LENGTH_SHORT).show();
        }

        edMessage.setText("");

            EnemyCounter=EnemyCounter+1;

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    class ClientThread implements Runnable {

        private Socket socket;

        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                socket = new Socket(serverAddr, SERVER_PORT);
                while (!Thread.currentThread().isInterrupted()) {
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String message = input.readLine();
                    if (null == message || "Disconnect".contentEquals(message)) {
                        boolean interrupted = Thread.interrupted();
                        message = "Server Disconnected: " + interrupted;
                        showMessage(message, Color.RED);
                        break;
                    }
                    showMessage("Server: " + message, clientTextColor);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        void sendMessage(final String message) {
            new Thread(() -> {
                try {
                    if (null != socket) {
                        PrintWriter out = new PrintWriter(new BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream())),
                                true);
                        out.println(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

    }

    @SuppressLint("SimpleDateFormat")
    String getTime() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != clientThread) {
            clientThread.sendMessage("Disconnect");
            clientThread = null;
        }
    }
}