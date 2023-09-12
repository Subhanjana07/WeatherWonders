package com.company.cloudnine;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.company.cloudnine.Adapters.DayWeatherAdapter;
import com.company.cloudnine.ModelClass.DayWeatherModel;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RelativeLayout rlHome,relativeLayout,rlCardView;
    ProgressBar progressBar;
    TextView textViewCityName, textViewTemp, textViewCondition, textViewCurrentDate, textViewFeelsLike, textViewCurrentWind, textViewHumidity, textViewWeekWeather;
    TextInputEditText editTextCityName;
    ImageView imageViewWeatherIcon, imageViewSearch,imageViewBack;
    RecyclerView rvTodayWeather;
    ArrayList<DayWeatherModel> dayWeatherModelArrayList;
    DayWeatherAdapter dayWeatherAdapter;
    LocationManager locationManager;
    ConstraintLayout constraintLayout;
    int PERMISSION_CODE = 1;
    LocationManagers locationManagers;
    WeatherManager weatherManager;
    SwipeRefreshLayout swipeRefreshLayout;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        rlHome = findViewById(R.id.relativeLayoutHome);
        relativeLayout = findViewById(R.id.relativeLayout);
        rlCardView = findViewById(R.id.rlCardView);
        textViewCityName = findViewById(R.id.textViewCityName);
        textViewTemp = findViewById(R.id.textViewTemperature);
        textViewCondition = findViewById(R.id.textViewConditon);
        editTextCityName = findViewById(R.id.editTextCity);
        imageViewWeatherIcon = findViewById(R.id.imageViewWeatherIcon);
        imageViewSearch = findViewById(R.id.imageViewSearch);
        constraintLayout = findViewById(R.id.constraintMain);
        rvTodayWeather = findViewById(R.id.rvTodayWeather);
        textViewCurrentDate = findViewById(R.id.textViewDate);
        textViewFeelsLike = findViewById(R.id.textViewFeelsLike);
        textViewCurrentWind = findViewById(R.id.textViewCurrentWindSpeed);
        textViewHumidity = findViewById(R.id.textViewHumidity);
        textViewWeekWeather = findViewById(R.id.textViewWeekWeather);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);

        dayWeatherModelArrayList = new ArrayList<>();
        dayWeatherAdapter = new DayWeatherAdapter(this, dayWeatherModelArrayList);
        rvTodayWeather.setAdapter(dayWeatherAdapter);

        locationManagers = new LocationManagers(this);
        weatherManager = new WeatherManager(this, dayWeatherAdapter);

        locationManagers.requestLocationPermissions();
        locationManagers.updateLocation();

        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = editTextCityName.getText().toString();
                if (city.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a city name", Toast.LENGTH_SHORT).show();
                } else {
                    editTextCityName.setText("");
                    weatherManager.getWeatherInfo(city);
                }
            }
        });

        textViewWeekWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String weekCityName = textViewCityName.getText().toString();
                Intent intent = new Intent(MainActivity.this, WeekWeatherActivity.class);
                intent.putExtra("CityName",weekCityName);
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String refreshCityName = textViewCityName.getText().toString();
                weatherManager.getWeatherInfo(refreshCityName);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        List<String> testDeviceIds = Arrays.asList("DB690F1254CF3D3F20B3C210DCF2C12F");
        RequestConfiguration configuration = new RequestConfiguration.Builder()
                .setTestDeviceIds(testDeviceIds)
                .build();
        MobileAds.setRequestConfiguration(configuration);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = findViewById(R.id.adViewBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        InterstitialAd.load(this,getString(R.string.interstitial_ad_unit_id), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdClicked() {
                                // Called when a click is recorded for an ad.
                                Log.d("Adclicked", "Ad was clicked.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                Log.d("Addismissed", "Ad dismissed fullscreen content.");
                                mInterstitialAd = null;
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                Log.e("Adfailed", "Ad failed to show fullscreen content.");
                                mInterstitialAd = null;
                            }

                            @Override
                            public void onAdImpression() {
                                // Called when an impression is recorded for an ad.
                                Log.d("Adrecorded", "Ad recorded an impression.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d("Adshowed", "Ad showed fullscreen content.");
                                mInterstitialAd = null;
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        //Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
            }
        },10000);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted..", Toast.LENGTH_SHORT).show();

                // Check for GPS Availability
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
                }
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Location location = locationManager.getLastKnownLocation(LocationManager.FUSED_PROVIDER);
                weatherManager.getWeatherInfoLatLon(location.getLatitude(), location.getLongitude());
            }
        }
    }

    public TextView getTextViewCityName() {
        return textViewCityName;
    }

    // Getter for WeatherManager
    public WeatherManager getWeatherManager() {
        return weatherManager;
    }

}