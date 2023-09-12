package com.company.cloudnine;

import static com.company.cloudnine.WeatherManager.latitude;
import static com.company.cloudnine.WeatherManager.longitude;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    static String currentCityName;
    static String temperature;
    static String condition;
    static String conditionIcon;
    private static final long INTERVAL_MILLISECONDS = 1 * 60 * 1000; // 30 minutes
    private static Handler handler = new Handler(Looper.getMainLooper());
    private static Context context;
    private static AppWidgetManager appWidgetManager;
    private static int[] appWidgetIds;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Store the context, appWidgetManager, and appWidgetIds for later use
        NewAppWidget.context = context;
        NewAppWidget.appWidgetManager = appWidgetManager;
        NewAppWidget.appWidgetIds = appWidgetIds;

        // Call updateAppWidget to refresh data
        for(int appWidgetId: appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

        // Schedule the periodic updates
        handler.postDelayed(updateRunnable, INTERVAL_MILLISECONDS);
    }

    @Override
    public void onEnabled(Context context) {
        // Schedule the initial update
        handler.post(updateRunnable);
    }

    @Override
    public void onDisabled(Context context) {
        // Cancel the periodic updates
        handler.removeCallbacks(updateRunnable);
    }

    private static final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            // Call updateAppWidget to refresh data
            for(int appWidgetId : appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId);
            }

            // Schedule the next update
            handler.postDelayed(this, INTERVAL_MILLISECONDS);
        }
    };

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int widgetIds) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        //Log.e("Latitude",""+latitude);
        String url = "http://api.weatherapi.com/v1/forecast.json?key=8a0b12c806ac4173852163428230409&q=" + latitude+","+longitude+ "&days=1&aqi=no&alerts=no";

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            currentCityName = response.getJSONObject("location").getString("name");
                            temperature = response.getJSONObject("current").getString("temp_c");
                            condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                            conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");

                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                            String currentTime = timeFormat.format(calendar.getTime());


                            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
                            views.setTextViewText(R.id.textViewWidgetLocation, ""+currentCityName);
                            views.setTextViewText(R.id.textViewWidgetTemp,""+temperature+"°C");
                            views.setTextViewText(R.id.textViewWidgetCondition,""+condition);
                            views.setTextViewText(R.id.textViewWidgetTime,""+currentTime);
                            Log.e("currentCityName",""+currentCityName);
                            ImageView conditionIconImageView = new ImageView(context);
                            Picasso.get().load("http:".concat(conditionIcon)).into(conditionIconImageView);
                            views.setImageViewBitmap(R.id.imageViewWidgetIcon, getBitmapFromImageView(conditionIconImageView));
                            // Instruct the widget manager to update the widget
                            appWidgetManager.updateAppWidget(widgetIds, views);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("WidgetApiError",""+e);

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("JSONerror",""+error);
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private static Bitmap getBitmapFromImageView(ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        if (drawable != null) {
            return drawable.getBitmap();
        }
        return null;
    }

}