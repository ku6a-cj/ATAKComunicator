package com.example.atakcomunicator;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    private FusedLocationProviderClient client;
    public  String url = "https://run.mocky.io/";
    public boolean connected = false;
    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.atakcomunicator.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_info, R.id.nav_otherfunctions,R.id.nav_api)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationView.getMenu().performIdentifierAction(R.id.nav_info, 0);
            }
        });
        AllyCounter = 0;
        EnemyCounter = 0;
        changedTemplate = false;
        setTitle("ATAKMessanger");
        clientTextColor = ContextCompat.getColor(this, R.color.green);
        handler = new Handler();
        msgList = findViewById(R.id.msgList);
        edMessage = findViewById(R.id.edMessage);
        edMessage.setHint("Tu wpisz IP serwer np." + "\"" + SERVER_IP + "\"");
        Button connectToServerr = findViewById(R.id.btnConnectServer);
        Button sendMessagee = findViewById(R.id.send_data);
        Button sendData2 = findViewById(R.id.send_data2);
        Button AllyButton = findViewById(R.id.buttonAlly);
        Button buttonAllyChange = findViewById(R.id.buttonAllyChange);
        Button buttonenemyChange = findViewById(R.id.send_data2Change);
        Button atackAircraft = findViewById(R.id.AddAtackAircraft);
        Button strikeAirctaft = findViewById(R.id.AddStrikeAircraft);
        Button markMyLocation = findViewById(R.id.MarkMyLocation);
        EditText id= findViewById(R.id.getId);
        EditText title= findViewById(R.id.title);
        EditText lat= findViewById(R.id.getLat);
        EditText lon= findViewById(R.id.getLong);
        Button ButtonApi =findViewById(R.id.buttonApi);
        TextView responseTV = findViewById(R.id.textViewResponse);

    }

//    public void changeInputType(){
//        edMessage = findViewById(R.id.edMessage);
//        if(connected==true){
//            edMessage.setInputType(InputType.TYPE_CLASS_TEXT);
//        }else{
//            edMessage.setHint("Tu wpisz IP serwer np." + "\"" + "192.168.137.4" + "\"");
//        }
//    }

    public void ApiGetInfo(){
        EditText id= findViewById(R.id.getId);
        EditText title= findViewById(R.id.title);
        EditText lat= findViewById(R.id.getLat);
        EditText lon= findViewById(R.id.getLong);
        Button ButtonApi =findViewById(R.id.buttonApi);
        // checking if the edit text is empty or not.
        if (TextUtils.isEmpty(id.getText().toString()) && TextUtils.isEmpty(title.getText().toString())&& TextUtils.isEmpty(lat.getText().toString())&& TextUtils.isEmpty(lon.getText().toString())) {

            // displaying a toast message if the edit text is empty.
            Toast.makeText(MainActivity.this, "Please enter your data..", Toast.LENGTH_SHORT).show();
            return;
        }
        String value= id.getText().toString();
        int finalValue=Integer.parseInt(value);
        String value1= lat.getText().toString();
        int finalValue1=Integer.parseInt(value);
        String value2= lon.getText().toString();
        int finalValue2=Integer.parseInt(value);
        // calling a method to update data in our API.
        callPUTDataMethod(finalValue, title.getText().toString(),finalValue1, finalValue2 );

    }



    public void callPUTDataMethod(int id, String title, int lat, int lon) {
        TextView responseTV = findViewById(R.id.textViewResponse);


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
        DataModel2 modal = new DataModel2(id, title, lat, lon);

        // calling a method to create an update and passing our modal class.
        Call<DataModel2> call = retrofitAPI.updateData(modal);

        // on below line we are executing our method.
        call.enqueue(new Callback<DataModel2>() {
            @Override
            public void onResponse(Call<DataModel2> call, Response<DataModel2> response) {
                // this method is called when we get response from our api.
                Toast.makeText(MainActivity.this, "Data updated to API", Toast.LENGTH_SHORT).show();





                // we are getting a response from our body and
                // passing it to our modal class.
                DataModel2 responseFromAPI = response.body();

                // on below line we are getting our data from modal class
                // and adding it to our string.
                String responsee= " ";
                if (response.code()==200)
                {
                    responsee="request has succeeded";
                }else{
                    responsee="request failed";
                }

                String responseString = "Response Code : " + response.code()+ " "+ responsee + "\nid : " + responseFromAPI.getid() + "\n" + "title : " + responseFromAPI.getTitle()+"\n" + "lat : " + responseFromAPI.getlat()+"\n" + "lon : " + responseFromAPI.getlon();

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


    public void sendMessage(){
        String clientMessage = edMessage.getText().toString().trim();
        showMessage(clientMessage, Color.BLUE);
        if (null != clientThread) {
            clientThread.sendMessage(clientMessage);
        }
    }

    public void connectToServer(){
        msgList = findViewById(R.id.msgList);
        edMessage = findViewById(R.id.edMessage);
        SERVER_IP = edMessage.getText().toString();
        if (SERVER_IP.isEmpty()) {
            Toast.makeText(MainActivity.this, "You entered a value out of range!", Toast.LENGTH_SHORT).show();
        }else{
            removeAllViews();
            showMessage("Connecting to Server...", clientTextColor);
            clientThread = new ClientThread();
            thread = new Thread(clientThread);
            thread.start();
            showMessage("Connecting", clientTextColor);
        }
    }


public boolean isConnected(){
    if(connected=true){
        return true;
    }else{
        return false;
    }
}

    public void checkConnection(){
        if(connected){
            hideConnectServerBtn();
            edMessage.setText("");
            edMessage.setHint("Enter the message here");
            edMessage.setInputType(InputType.TYPE_CLASS_TEXT);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    //this function works inside, so I cannot modify the time I send and it serves only to display
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
        if(message.equals("Connected to Server!!")){
            connected=true;
            checkConnection();
        }
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
            Toast.makeText(this, "There is no text to be sent, please fill in the text fields!",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, "You did not enter an ID value, so it is set by default to 0!",Toast.LENGTH_SHORT).show();
                    id="0";
                }
                edMessage.setText("");
                if(Integer.parseInt(id)>EnemyCounter){
                    Toast.makeText(this, "You entered a value out of the range!",Toast.LENGTH_SHORT).show();
                    Message = "User inserted an incorrect data";
                }else{
                    Message = "2-EFlag-"+LaT+"-"+LoNg+"-"+isS+"-"+isW+"-"+id;
                }


            String clientMessage = Message;

            if (null != clientThread) {
                clientThread.sendMessage(clientMessage);
            }
        }else{
            Toast.makeText(this, "You entered a value out of the range!",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "\n" + "There is no text to be sent, please fill in the text fields!",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, "\n" + "You did not enter an ID value, so it is set to 0!",Toast.LENGTH_SHORT).show();
                    id="0";
                }
                edMessage.setText("");
                if(Integer.parseInt(id)>AllyCounter){
                    Toast.makeText(this, "You entered a value out of the range!",Toast.LENGTH_SHORT).show();
                    Message = "User inserted an incorrect data";
                }else{
                    Message = "2-Flag-"+LaT+"-"+LoNg+"-"+isS+"-"+isW+"-"+id;
                }


            String clientMessage = Message;

            if (null != clientThread) {
                clientThread.sendMessage(clientMessage);
            }
        }else{
            Toast.makeText(this, "You entered a value out of the range!",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "There is no text for upload, please fill in the text fields!",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "You entered a value out of range!",Toast.LENGTH_SHORT).show();
        }

        edMessage.setText("");
        Toast.makeText(this, "\n" + "An ally tag has been added with ID:"+ AllyCounter,Toast.LENGTH_SHORT).show();

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
            Toast.makeText(this, "\n" + "There is no text to be sent, please fill in the text fields!",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "You entered a value out of range!",Toast.LENGTH_SHORT).show();
        }

        edMessage.setText("");
        Toast.makeText(this, "\n" + "An enemy marker has been added with an ID:"+ EnemyCounter,Toast.LENGTH_SHORT).show();

            EnemyCounter=EnemyCounter+1;

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    public void setMyLocation(){
        Toast.makeText(this, "You chose to put a marker at my location",Toast.LENGTH_SHORT).show();
        client = LocationServices.getFusedLocationProviderClient(this);
        requestPermission();
        if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            Log.e("nie masz uparawnien GPS", "bark uprawnien");
        }
        client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Log.e("Set location", "1");
                if (location != null) {
                    double LatiTude = round(Double.parseDouble(String.valueOf(location.getLatitude())),3);
                    double LonGitude=  round(Double.parseDouble(String.valueOf(location.getLongitude())),3);
                    String MyMessageGPS= "1-Flag-"+String.valueOf(LatiTude)+"-"+String.valueOf(LonGitude)+"-0"+"-0"+"-0";
                    Log.e("My LaT", String.valueOf(LatiTude));
                    Log.e("My LaT", String.valueOf(LonGitude));
                    Log.e("message", MyMessageGPS);
                    String clientMessage2 = MyMessageGPS;
                    if (null != clientThread) {
                        clientThread.sendMessage(clientMessage2);
                    }
                    edMessage.setText("");
                     //Log.e("My location",location.toString());
                }
            }
        });
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void addStrikeAircraft(){
        Toast.makeText(this, "You have chosen to add an attack plane",Toast.LENGTH_SHORT).show();
        String clientMessage1 = "1-DSS-0-0-0";
        if (null != clientThread) {
            clientThread.sendMessage(clientMessage1);
        }
    }

    public void addAtackAircraft(){
        Toast.makeText(this, "You have chosen to add a fighter plane",Toast.LENGTH_SHORT).show();
        String clientMessage = "1-DSM-0-0-0";
        //showMessage(clientMessage, Color.BLUE);
        if (null != clientThread) {
            clientThread.sendMessage(clientMessage);
        }
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
                    if(message.equals("Connected to Server!!")){
                        connected=true;
                    }
                    checkConnection();
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