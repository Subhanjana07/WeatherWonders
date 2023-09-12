package com.company.cloudnine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.company.cloudnine.Adapters.OuterWeekWeatherAdapter;
import com.company.cloudnine.ModelClass.InnerWeekWeatherModel;
import com.company.cloudnine.ModelClass.OuterWeekWeatherModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeekWeatherActivity extends AppCompatActivity {

    RecyclerView recyclerViewOuter;
    ArrayList<OuterWeekWeatherModel> outerWeekWeatherModelArrayList;
    OuterWeekWeatherAdapter outerWeekWeatherAdapter;
    ScrollView scrollViewOuter;
    ProgressBar progressBarOuter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_weather);
        recyclerViewOuter = findViewById(R.id.rvOuterWeekly);
        scrollViewOuter = findViewById(R.id.outerRVScrollView);
        TextView textViewCity = findViewById(R.id.textViewWeekCity);
        progressBarOuter = findViewById(R.id.progressbarOutRV);
        progressBarOuter.setVisibility(View.VISIBLE);

        String cityName = getIntent().getStringExtra("CityName");
        Log.d("WeekCityName",""+cityName);
        textViewCity.setText(""+cityName);

        outerWeekWeatherModelArrayList = new ArrayList<>();
        outerWeekWeatherAdapter = new OuterWeekWeatherAdapter(this,outerWeekWeatherModelArrayList);
        recyclerViewOuter.setAdapter(outerWeekWeatherAdapter);

        getWeatherInfo(cityName);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void getWeatherInfo(String cityName) {
        Log.e("CityNameWeatherInfo", "" + cityName);
        String url = "http://api.weatherapi.com/v1/forecast.json?key=8a0b12c806ac4173852163428230409&q=" + cityName + "&days=7&aqi=no&alerts=no";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        outerWeekWeatherModelArrayList.clear();
                        try{
                            progressBarOuter.setVisibility(View.INVISIBLE);
                            JSONObject forcastObj = response.getJSONObject("forecast");
                            JSONArray dayForecastObj = forcastObj.getJSONArray("forecastday");
                            for(int i = 1;i < dayForecastObj.length();i++)
                            {
                                ArrayList<InnerWeekWeatherModel> nestedList = new ArrayList<>();
                                JSONObject dateForcastObj = dayForecastObj.getJSONObject(i);
                                String date = dateForcastObj.getString("date");
                                Log.e("Date",""+date);
                                JSONArray hourArray = dateForcastObj.getJSONArray("hour");
                                for(int j =0;j<hourArray.length();j++){
                                    JSONObject hourObj = hourArray.getJSONObject(j);
                                    String time = hourObj.getString("time");
                                    String temp = hourObj.getString("temp_c");
                                    String img = hourObj.getJSONObject("condition").getString("icon");
                                    String condition = hourObj.getJSONObject("condition").getString("text");
                                    nestedList.add(new InnerWeekWeatherModel(time,temp,img,condition));
                                }
                                outerWeekWeatherModelArrayList.add(new OuterWeekWeatherModel(date,nestedList,false));

                            }
                            outerWeekWeatherAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WeekWeatherActivity.this, "Please enter valid city name..", Toast.LENGTH_SHORT).show();
                Log.e("JSONerror",""+error);
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}