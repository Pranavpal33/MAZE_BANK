package com.example.maze_bank;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.maze_bank.Retrofit.ApiClient;
import com.example.maze_bank.Retrofit.ApiInterface;
import com.example.maze_bank.Retrofit.Example;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class weather extends AppCompatActivity {

    EditText wSearchbar;
    ImageView wSearch;
    TextView  wTemp,wDesc,wHumidity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_weather);


        //variable initialisation

        wSearch=findViewById(R.id.searchbtn);
        wTemp=findViewById(R.id.Temperature);
        wDesc=findViewById(R.id.description);
        wHumidity=findViewById(R.id.humidity);
        wSearchbar=findViewById(R.id.searchbar);


        //setting onclick functionality


        wSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getWeatherData(wSearchbar.getText().toString().trim());


            }
        });




    }
    private void getWeatherData(String name)
    {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Example> call=apiInterface.getWeatherData(name);
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {

                if(response!=null) {
                    wTemp.setText("Temp" +" "+response.body().getMain().getTemp()+" C");
                    wDesc.setText("Feels Like"+" "+response.body().getMain().getFeels_like());
                    wHumidity.setText("Humidity"+" " +response.body().getMain().getHumidity());
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

            }
        });

    }
}
