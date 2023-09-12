package com.company.cloudnine.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.cloudnine.ModelClass.DayWeatherModel;
import com.company.cloudnine.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DayWeatherAdapter extends RecyclerView.Adapter<DayWeatherAdapter.ViewHolder> {

    Context context;
    ArrayList<DayWeatherModel> dayWeatherModelArrayList;

    public DayWeatherAdapter(Context context, ArrayList<DayWeatherModel> dayWeatherModelArrayList) {
        this.context = context;
        this.dayWeatherModelArrayList = dayWeatherModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.day_weather_rv,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DayWeatherModel model = dayWeatherModelArrayList.get(position);
        holder.textViewTemp.setText(model.getTemp()+" °C");
        Picasso.get().load("http:".concat(model.getIcon())).into(holder.imageViewCondition);
        holder.textViewWind.setText(model.getWindSpeed());

        Log.e("time",model.getTime());
        Log.e("temp",model.getTemp());
        Log.e("img",model.getIcon());
        Log.e("wind",model.getWindSpeed());

        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm a");
        try{
            Date date = input.parse(model.getTime());
            holder.textViewTime.setText(output.format(date));
        }catch (ParseException e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return dayWeatherModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewWind,textViewTemp,textViewTime;
        ImageView imageViewCondition;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewWind = itemView.findViewById(R.id.textViewrvWindSpeed);
            textViewTemp = itemView.findViewById(R.id.rvTemp);
            textViewTime = itemView.findViewById(R.id.rvTime);
            imageViewCondition = itemView.findViewById(R.id.imageViewrvCondition);
        }
    }
}
