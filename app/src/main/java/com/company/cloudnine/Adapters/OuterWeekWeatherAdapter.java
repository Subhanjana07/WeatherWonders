package com.company.cloudnine.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.cloudnine.ModelClass.InnerWeekWeatherModel;
import com.company.cloudnine.ModelClass.OuterWeekWeatherModel;
import com.company.cloudnine.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OuterWeekWeatherAdapter extends RecyclerView.Adapter<OuterWeekWeatherAdapter.OuterWeekViewHolder>{

    Context context;
    ArrayList<OuterWeekWeatherModel> outerDatesList;
    ArrayList<InnerWeekWeatherModel> innerWeekWeatherModelArrayList = new ArrayList<>();


    public OuterWeekWeatherAdapter(Context context, ArrayList<OuterWeekWeatherModel> outerDatesList) {
        this.context = context;
        this.outerDatesList = outerDatesList;

    }

    @NonNull
    @Override
    public OuterWeekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.week_card,parent,false);
        return new OuterWeekViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OuterWeekViewHolder holder, int position) {

        OuterWeekWeatherModel model = outerDatesList.get(position);
        Log.e("Adaptertime",""+model.getDate());
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat output = new SimpleDateFormat("dd MMMM, yyyy");
        try{
            Date date = input.parse(model.getDate());
            String formattedDate = output.format(date);
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


            holder.textViewOuterDate.setText(formattedDate);
        }catch (ParseException e){
            e.printStackTrace();
            Log.e("TimeError",""+e);
        }

        boolean isExpandable = model.getExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);

        if(isExpandable){
            holder.mArrowImage.setImageResource(R.drawable.baseline_keyboard_arrow_up_24);
        }
        else{
            holder.mArrowImage.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
        }
        InnerWeekWeatherAdapter innerWeekWeatherAdapter = new InnerWeekWeatherAdapter(innerWeekWeatherModelArrayList);
        holder.recyclerViewInnerWeek.setHasFixedSize(true);
        holder.recyclerViewInnerWeek.setAdapter(innerWeekWeatherAdapter);
        holder.linearLayoutInner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setExpandable(!model.getExpandable());
                innerWeekWeatherModelArrayList = model.getInnerWeekWeatherModelArrayList();
                notifyItemChanged(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return outerDatesList.size();
    }

    public class OuterWeekViewHolder extends RecyclerView.ViewHolder{

        TextView textViewOuterDate;
        RelativeLayout expandableLayout;
        LinearLayout linearLayoutInner;
        ImageView mArrowImage;
        RecyclerView recyclerViewInnerWeek;
        public OuterWeekViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewOuterDate = itemView.findViewById(R.id.textViewWeekDate);
            recyclerViewInnerWeek = itemView.findViewById(R.id.rvInnerWeek);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            mArrowImage = itemView.findViewById(R.id.arro_imageview);
            linearLayoutInner = itemView.findViewById(R.id.linear_layout);
        }
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

}

