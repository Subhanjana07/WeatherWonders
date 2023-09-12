package com.company.cloudnine.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.cloudnine.ModelClass.InnerWeekWeatherModel;
import com.company.cloudnine.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InnerWeekWeatherAdapter extends RecyclerView.Adapter<InnerWeekWeatherAdapter.InnerViewHolder> {

    ArrayList<InnerWeekWeatherModel> innerWeekWeatherModelArrayList;

    public InnerWeekWeatherAdapter(ArrayList<InnerWeekWeatherModel> innerWeekWeatherModelArrayList) {
        this.innerWeekWeatherModelArrayList = innerWeekWeatherModelArrayList;
    }

    @NonNull
    @Override
    public InnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_inner_weather,parent,false);
        return new InnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerViewHolder holder, int position) {

        InnerWeekWeatherModel model = innerWeekWeatherModelArrayList.get(position);

        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm a");
        try{
            Date date = input.parse(model.getInnerRVtime());
            holder.textViewInnerTime.setText(output.format(date));
        }catch (ParseException e){
            e.printStackTrace();
        }

        holder.textViewInnerTemp.setText(model.getInnerRVtemp()+" °C");
        holder.textViewInnerCondition.setText(model.getInnerRVcondition());
        Picasso.get().load("http:".concat(model.getInnerRVicon())).into(holder.imageViewInnerIcon);
    }

    @Override
    public int getItemCount() {
        return innerWeekWeatherModelArrayList.size();
    }

    public class InnerViewHolder extends RecyclerView.ViewHolder{

        TextView textViewInnerTime,textViewInnerTemp,textViewInnerCondition;
        ImageView imageViewInnerIcon;
        public InnerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewInnerTime = itemView.findViewById(R.id.rvInnerTime);
            textViewInnerTemp = itemView.findViewById(R.id.rvInnerTemp);
            imageViewInnerIcon = itemView.findViewById(R.id.imageViewrvInnerCondition);
            textViewInnerCondition = itemView.findViewById(R.id.textViewrvInnerCondition);
        }
    }
}
