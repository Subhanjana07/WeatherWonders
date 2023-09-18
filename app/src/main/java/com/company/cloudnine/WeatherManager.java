package com.company.cloudnine;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.company.cloudnine.Adapters.DayWeatherAdapter;
import com.company.cloudnine.ModelClass.DayWeatherModel;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherManager {

    private MainActivity mainActivity;
    private DayWeatherAdapter dayWeatherAdapter;
    static Double latitude,longitude;

    public WeatherManager(MainActivity mainActivity, DayWeatherAdapter dayWeatherAdapter) {
        this.mainActivity = mainActivity;
        this.dayWeatherAdapter = dayWeatherAdapter;
    }

    public void getWeatherInfo(String cityName) {
        Log.e("CityName", "" + cityName);
        String url = "https://api.weatherapi.com/v1/forecast.json?key=8a0b12c806ac4173852163428230409&q=" + cityName + "&days=1&aqi=no&alerts=no";
        mainActivity.getTextViewCityName().setText(cityName);

        RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mainActivity.dayWeatherModelArrayList.clear();
                        try {
                            String currentCityName = response.getJSONObject("location").getString("name");
                            String currentCityRegion = response.getJSONObject("location").getString("region");
                            String currentCityCountry = response.getJSONObject("location").getString("country");
                            if(currentCityRegion.equals(""))
                            {
                                mainActivity.textViewCityName.setText(currentCityName+", "+currentCityCountry);
                            }
                            else{
                                mainActivity.textViewCityName.setText(currentCityName+","+currentCityRegion+",  "+currentCityCountry);
                            }

                            String temperature = response.getJSONObject("current").getString("temp_c");
                            mainActivity.textViewTemp.setText(temperature+" °C");
                            int isDay = response.getJSONObject("current").getInt("is_day");
                            String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                            String conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                            Picasso.get().load("http:".concat(conditionIcon)).into(mainActivity.imageViewWeatherIcon);
                            mainActivity.textViewCondition.setText(condition);
                            String currentDateTime = response.getJSONObject("location").getString("localtime");
                            String feelsLike = response.getJSONObject("current").getString("feelslike_c");
                            String windSpeed = response.getJSONObject("current").getString("wind_kph");
                            String humidity = response.getJSONObject("current").getString("humidity");
                            Log.d("FeelsLike",""+feelsLike);
                            mainActivity.textViewFeelsLike.setText(feelsLike+" °C");
                            mainActivity.textViewCurrentWind.setText(windSpeed+" Kph");
                            mainActivity.textViewHumidity.setText(humidity+" %");


                            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                            SimpleDateFormat output = new SimpleDateFormat("d MMMM, yyyy  h:mm a");

                            try {
                                Date date = input.parse(currentDateTime);
                                String formattedDate = output.format(date);

                                // To remove leading zero from date
                                formattedDate = formattedDate.replaceFirst("^0+(?!$)", "");

                                // To add "st," "nd," "rd," or "th" to the date
                                String[] parts = formattedDate.split(" ");
                                if (parts.length >= 1) {
                                    String day = parts[0];
                                    int dayNumber = Integer.parseInt(day);
                                    String dayWithSuffix = day + getDayOfMonthSuffix(dayNumber);
                                    parts[0] = dayWithSuffix;
                                    formattedDate = TextUtils.join(" ", parts);
                                }

                                // Capitalize the first letter of the month
                                if (parts.length >= 3) {
                                    String month = parts[1];
                                    String capitalizedMonth = month.substring(0, 1).toUpperCase() + month.substring(1).toLowerCase();
                                    parts[1] = capitalizedMonth;
                                    formattedDate = TextUtils.join(" ", parts);
                                }

                                mainActivity.textViewCurrentDate.setText(formattedDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                Log.e("CurrentTime", "" + e);
                            }


                            if(isDay == 1)
                            {
                                switch (condition) {
                                    case "Partly cloudy":
                                    case "Cloudy":
                                    case "Overcast":
                                    case "Mist":
                                    case "Fog":

                                        Glide.with(mainActivity)
                                                .load(R.drawable.cloud)
                                                .into(new CustomTarget<Drawable>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                        // Once the image is loaded, set it as the background of the RelativeLayout
                                                        mainActivity.rlHome.setBackground(resource);
                                                    }

                                                    @Override
                                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                                        // Handle any clearing or cleanup if needed
                                                    }
                                                });
                                        break;
                                    case "Patchy rain possible":
                                    case "Light rain shower":
                                    case "Light rain":
                                    case "Moderate rain at times":
                                    case "Moderate rain":
                                    case "Light drizzle":
                                    case "Moderate or heavy rain shower":
                                    case "Heavy rain":
                                    case "Torrential rain shower":
                                    case "Patchy light rain":

                                        Glide.with(mainActivity)
                                                .load(R.drawable.dayrain)
                                                .into(new CustomTarget<Drawable>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                        // Once the image is loaded, set it as the background of the RelativeLayout
                                                        mainActivity.rlHome.setBackground(resource);
                                                    }

                                                    @Override
                                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                                        // Handle any clearing or cleanup if needed
                                                    }
                                                });
                                        break;
                                    case "Sunny":
                                        Glide.with(mainActivity)
                                                .load(R.drawable.sunny)
                                                .into(new CustomTarget<Drawable>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                        // Once the image is loaded, set it as the background of the RelativeLayout
                                                        mainActivity.rlHome.setBackground(resource);
                                                    }

                                                    @Override
                                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                                        // Handle any clearing or cleanup if needed
                                                    }
                                                });
                                        break;
                                    case "Thundery outbreaks possible":
                                    case "Patchy light rain with thunder":
                                    case "Moderate or heavy rain with thunder":

                                        Glide.with(mainActivity)
                                                .load(R.drawable.thunder)
                                                .into(new CustomTarget<Drawable>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                        // Once the image is loaded, set it as the background of the RelativeLayout
                                                        mainActivity.rlHome.setBackground(resource);
                                                    }

                                                    @Override
                                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                                        // Handle any clearing or cleanup if needed
                                                    }
                                                });
                                        break;

                                    case "Patchy snow possible":
                                    case "Patchy heavy snow":

                                        Glide.with(mainActivity)
                                                .load("https://img.freepik.com/free-photo/snowy-landscape_1048-3709.jpg")
                                                .into(new CustomTarget<Drawable>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                        // Once the image is loaded, set it as the background of the RelativeLayout
                                                        mainActivity.rlHome.setBackground(resource);
                                                    }

                                                    @Override
                                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                                        // Handle any clearing or cleanup if needed
                                                    }
                                                });
                                        break;
                                    default:

                                        Glide.with(mainActivity)
                                                .load(R.drawable.clearday)
                                                .into(new CustomTarget<Drawable>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                        // Once the image is loaded, set it as the background of the RelativeLayout
                                                        mainActivity.rlHome.setBackground(resource);
                                                    }

                                                    @Override
                                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                                        // Handle any clearing or cleanup if needed
                                                    }
                                                });
                                        break;
                                }
                            }
                            else {
                                switch (condition) {
                                    case "Patchy rain possible":
                                    case "Light drizzle":
                                    case "Light rain shower":
                                    case "Moderate rain at times":
                                    case "Moderate rain":
                                    case "Light rain":
                                    case "Moderate or heavy rain shower":
                                    case "Heavy rain":
                                    case "Torrential rain shower":
                                    case "Patchy light rain":

                                        Glide.with(mainActivity)
                                                .load(R.drawable.nightrain)
                                                .into(new CustomTarget<Drawable>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                        // Once the image is loaded, set it as the background of the RelativeLayout
                                                        mainActivity.rlHome.setBackground(resource);
                                                    }

                                                    @Override
                                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                                        // Handle any clearing or cleanup if needed
                                                    }
                                                });
                                        break;
                                    case "Cloudy":
                                    case "Mist":
                                    case "Fog":
                                    case "Overcast":

                                        Glide.with(mainActivity)
                                                .load(R.drawable.overcastnight)
                                                .into(new CustomTarget<Drawable>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                        // Once the image is loaded, set it as the background of the RelativeLayout
                                                        mainActivity.rlHome.setBackground(resource);
                                                    }

                                                    @Override
                                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                                        // Handle any clearing or cleanup if needed
                                                    }
                                                });
                                        break;
                                    case "Thundery outbreaks possible":
                                    case "Patchy light rain with thunder":
                                    case "Moderate or heavy rain with thunder":

                                        Glide.with(mainActivity)
                                                .load(R.drawable.thunder)
                                                .into(new CustomTarget<Drawable>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                        // Once the image is loaded, set it as the background of the RelativeLayout
                                                        mainActivity.rlHome.setBackground(resource);
                                                    }

                                                    @Override
                                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                                        // Handle any clearing or cleanup if needed
                                                    }
                                                });
                                        break;

                                    case "Patchy snow possible":
                                    case "Patchy heavy snow":

                                        Glide.with(mainActivity)
                                                .load(R.drawable.nightsnow)
                                                .into(new CustomTarget<Drawable>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                        // Once the image is loaded, set it as the background of the RelativeLayout
                                                        mainActivity.rlHome.setBackground(resource);
                                                    }

                                                    @Override
                                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                                        // Handle any clearing or cleanup if needed
                                                    }
                                                });
                                        break;
                                    default:

                                        Glide.with(mainActivity)
                                                .load(R.drawable.nightclear)
                                                .into(new CustomTarget<Drawable>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                        // Once the image is loaded, set it as the background of the RelativeLayout
                                                        mainActivity.rlHome.setBackground(resource);
                                                    }

                                                    @Override
                                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                                        // Handle any clearing or cleanup if needed
                                                    }
                                                });
                                        break;
                                }
                            }

                            JSONObject forcastObj = response.getJSONObject("forecast");
                            JSONObject dayForecastObj = forcastObj.getJSONArray("forecastday").getJSONObject(0);
                            JSONArray hourArray = dayForecastObj.getJSONArray("hour");
                            for(int i =0;i<hourArray.length();i++){
                                JSONObject hourObj = hourArray.getJSONObject(i);
                                String time = hourObj.getString("time");
                                String temp = hourObj.getString("temp_c");
                                String img = hourObj.getJSONObject("condition").getString("icon");
                                String wind = hourObj.getJSONObject("condition").getString("text");
                                //if (time.compareTo(currentDateTime) >= 0) {
                                    mainActivity.dayWeatherModelArrayList.add(new DayWeatherModel(time, temp, img, wind));
                                //}
                            }
                            dayWeatherAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainActivity, "Please enter valid city name..", Toast.LENGTH_SHORT).show();
                Log.e("JSONerror",""+error);
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public String getDayOfMonthSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public void getWeatherInfoLatLon(double lat,double lon) {
        latitude = lat;
        longitude = lon;
        String url = "http://api.weatherapi.com/v1/forecast.json?key=8a0b12c806ac4173852163428230409&q=" + lat+","+lon+ "&days=1&aqi=no&alerts=no";
        //mainActivity.getTextViewCityName().setText(cityName);

        RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mainActivity.dayWeatherModelArrayList.clear();
                        try {
                            String currentCityName = response.getJSONObject("location").getString("name");
                            String currentCityRegion = response.getJSONObject("location").getString("region");
                            String currentCityCountry = response.getJSONObject("location").getString("country");
                            if(currentCityRegion.equals(""))
                            {
                                mainActivity.textViewCityName.setText(currentCityName+", "+currentCityCountry);
                            }
                            else{
                                mainActivity.textViewCityName.setText(currentCityName+","+currentCityRegion+",  "+currentCityCountry);
                            }

                            String temperature = response.getJSONObject("current").getString("temp_c");
                            mainActivity.textViewTemp.setText(temperature+" °C");
                            int isDay = response.getJSONObject("current").getInt("is_day");
                            String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                            String conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                            Picasso.get().load("http:".concat(conditionIcon)).into(mainActivity.imageViewWeatherIcon);
                            mainActivity.textViewCondition.setText(condition);
                            String currentDateTime = response.getJSONObject("location").getString("localtime");
                            String feelsLike = response.getJSONObject("current").getString("feelslike_c");
                            String windSpeed = response.getJSONObject("current").getString("wind_kph");
                            String humidity = response.getJSONObject("current").getString("humidity");
                            Log.d("FeelsLike",""+feelsLike);
                            mainActivity.textViewFeelsLike.setText(feelsLike+" °C");
                            mainActivity.textViewCurrentWind.setText(windSpeed+" Kph");
                            mainActivity.textViewHumidity.setText(humidity+" %");


                            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                            SimpleDateFormat output = new SimpleDateFormat("d MMMM, yyyy  h:mm a");

                            try {
                                Date date = input.parse(currentDateTime);
                                String formattedDate = output.format(date);

                                // To remove leading zero from date
                                formattedDate = formattedDate.replaceFirst("^0+(?!$)", "");

                                // To add "st," "nd," "rd," or "th" to the date
                                String[] parts = formattedDate.split(" ");
                                if (parts.length >= 1) {
                                    String day = parts[0];
                                    int dayNumber = Integer.parseInt(day);
                                    String dayWithSuffix = day + getDayOfMonthSuffix(dayNumber);
                                    parts[0] = dayWithSuffix;
                                    formattedDate = TextUtils.join(" ", parts);
                                }

                                // Capitalize the first letter of the month
                                if (parts.length >= 3) {
                                    String month = parts[1];
                                    String capitalizedMonth = month.substring(0, 1).toUpperCase() + month.substring(1).toLowerCase();
                                    parts[1] = capitalizedMonth;
                                    formattedDate = TextUtils.join(" ", parts);
                                }

                                mainActivity.textViewCurrentDate.setText(formattedDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                Log.e("CurrentTime", "" + e);
                            }


                            if(isDay == 1)
                            {
                                switch (condition) {
                                    case "Partly cloudy":
                                    case "Cloudy":
                                    case "Overcast":
                                    case "Mist":
                                    case "Fog":

                                        Glide.with(mainActivity)
                                                .load(R.drawable.cloud)
                                                .into(new CustomTarget<Drawable>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                        // Once the image is loaded, set it as the background of the RelativeLayout
                                                        mainActivity.rlHome.setBackground(resource);
                                                    }

                                                    @Override
                                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                                        // Handle any clearing or cleanup if needed
                                                    }
                                                });
                                        break;
                                    case "Patchy rain possible":
                                    case "Light rain shower":
                                    case "Light rain":
                                    case "Moderate rain at times":
                                    case "Moderate rain":
                                    case "Light drizzle":
                                    case "Moderate or heavy rain shower":
                                    case "Heavy rain":
                                    case "Torrential rain shower":
                                    case "Patchy light rain":

                                        Glide.with(mainActivity)
                                                .load(R.drawable.dayrain)
                                                .into(new CustomTarget<Drawable>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                        // Once the image is loaded, set it as the background of the RelativeLayout
                                                        mainActivity.rlHome.setBackground(resource);
                                                    }

                                                    @Override
                                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                                        // Handle any clearing or cleanup if needed
                                                    }
                                                });
                                        break;
                                    case "Sunny":
                                        Glide.with(mainActivity)
                                                .load(R.drawable.sunny)
                                                .into(new CustomTarget<Drawable>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                        // Once the image is loaded, set it as the background of the RelativeLayout
                                                        mainActivity.rlHome.setBackground(resource);
                                                    }

                                                    @Override
                                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                                        // Handle any clearing or cleanup if needed
                                                    }
                                                });
                                        break;
                                    case "Thundery outbreaks possible":
                                    case "Patchy light rain with thunder":
                                    case "Moderate or heavy rain with thunder":

                                        Glide.with(mainActivity)
                                                .load(R.drawable.thunder)
                                                .into(new CustomTarget<Drawable>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                        // Once the image is loaded, set it as the background of the RelativeLayout
                                                        mainActivity.rlHome.setBackground(resource);
                                                    }

                                                    @Override
                                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                                        // Handle any clearing or cleanup if needed
                                                    }
                                                });
                                        break;

                                    case "Patchy snow possible":
                                    case "Patchy heavy snow":

                                        Glide.with(mainActivity)
                                                .load("https://img.freepik.com/free-photo/snowy-landscape_1048-3709.jpg")
                                                .into(new CustomTarget<Drawable>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                        // Once the image is loaded, set it as the background of the RelativeLayout
                                                        mainActivity.rlHome.setBackground(resource);
                                                    }

                                                    @Override
                                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                                        // Handle any clearing or cleanup if needed
                                                    }
                                                });
                                        break;
                                    default:

                                        Glide.with(mainActivity)
                                                .load(R.drawable.clearday)
                                                .into(new CustomTarget<Drawable>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                        // Once the image is loaded, set it as the background of the RelativeLayout
                                                        mainActivity.rlHome.setBackground(resource);
                                                    }

                                                    @Override
                                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                                        // Handle any clearing or cleanup if needed
                                                    }
                                                });
                                        break;
                                }
                            }
                            else {
                                switch (condition) {
                                    case "Patchy rain possible":
                                    case "Light drizzle":
                                    case "Light rain shower":
                                    case "Moderate rain at times":
                                    case "Moderate rain":
                                    case "Light rain":
                                    case "Moderate or heavy rain shower":
                                    case "Heavy rain":
                                    case "Torrential rain shower":
                                    case "Patchy light rain":

                                        Glide.with(mainActivity)
                                                .load(R.drawable.nightrain)
                                                .into(new CustomTarget<Drawable>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                        // Once the image is loaded, set it as the background of the RelativeLayout
                                                        mainActivity.rlHome.setBackground(resource);
                                                    }

                                                    @Override
                                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                                        // Handle any clearing or cleanup if needed
                                                    }
                                                });
                                        break;
                                    case "Cloudy":
                                    case "Mist":
                                    case "Fog":
                                    case "Overcast":

                                        Glide.with(mainActivity)
                                                .load(R.drawable.overcastnight)
                                                .into(new CustomTarget<Drawable>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                        // Once the image is loaded, set it as the background of the RelativeLayout
                                                        mainActivity.rlHome.setBackground(resource);
                                                    }

                                                    @Override
                                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                                        // Handle any clearing or cleanup if needed
                                                    }
                                                });
                                        break;
                                    case "Thundery outbreaks possible":
                                    case "Patchy light rain with thunder":
                                    case "Moderate or heavy rain with thunder":

                                        Glide.with(mainActivity)
                                                .load(R.drawable.thunder)
                                                .into(new CustomTarget<Drawable>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                        // Once the image is loaded, set it as the background of the RelativeLayout
                                                        mainActivity.rlHome.setBackground(resource);
                                                    }

                                                    @Override
                                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                                        // Handle any clearing or cleanup if needed
                                                    }
                                                });
                                        break;

                                    case "Patchy snow possible":
                                    case "Patchy heavy snow":

                                        Glide.with(mainActivity)
                                                .load(R.drawable.nightsnow)
                                                .into(new CustomTarget<Drawable>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                        // Once the image is loaded, set it as the background of the RelativeLayout
                                                        mainActivity.rlHome.setBackground(resource);
                                                    }

                                                    @Override
                                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                                        // Handle any clearing or cleanup if needed
                                                    }
                                                });
                                        break;
                                    default:

                                        Glide.with(mainActivity)
                                                .load(R.drawable.nightclear)
                                                .into(new CustomTarget<Drawable>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                        // Once the image is loaded, set it as the background of the RelativeLayout
                                                        mainActivity.rlHome.setBackground(resource);
                                                    }

                                                    @Override
                                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                                        // Handle any clearing or cleanup if needed
                                                    }
                                                });
                                        break;
                                }
                            }

                            JSONObject forcastObj = response.getJSONObject("forecast");
                            JSONObject dayForecastObj = forcastObj.getJSONArray("forecastday").getJSONObject(0);
                            JSONArray hourArray = dayForecastObj.getJSONArray("hour");
                            for(int i =0;i<hourArray.length();i++){
                                JSONObject hourObj = hourArray.getJSONObject(i);
                                String time = hourObj.getString("time");
                                String temp = hourObj.getString("temp_c");
                                String img = hourObj.getJSONObject("condition").getString("icon");
                                String wind = hourObj.getJSONObject("condition").getString("text");

                                //if (time.compareTo(currentDateTime) > 0) {
                                    mainActivity.dayWeatherModelArrayList.add(new DayWeatherModel(time, temp, img, wind));
                                //}
                            }
                            dayWeatherAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainActivity, "Please enter valid city name..", Toast.LENGTH_SHORT).show();
                Log.e("JSONerror",""+error);
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}

