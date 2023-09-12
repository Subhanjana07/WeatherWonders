package com.company.cloudnine.ModelClass;

import com.company.cloudnine.ModelClass.InnerWeekWeatherModel;

import java.util.ArrayList;

public class OuterWeekWeatherModel {

    String date;
    ArrayList<InnerWeekWeatherModel> innerWeekWeatherModelArrayList;
    Boolean isExpandable;

    public OuterWeekWeatherModel(String date, ArrayList<InnerWeekWeatherModel> innerWeekWeatherModelArrayList, Boolean isExpandable) {
        this.date = date;
        this.innerWeekWeatherModelArrayList = innerWeekWeatherModelArrayList;
        this.isExpandable = isExpandable;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<InnerWeekWeatherModel> getInnerWeekWeatherModelArrayList() {
        return innerWeekWeatherModelArrayList;
    }

    public void setInnerWeekWeatherModelArrayList(ArrayList<InnerWeekWeatherModel> innerWeekWeatherModelArrayList) {
        this.innerWeekWeatherModelArrayList = innerWeekWeatherModelArrayList;
    }

    public Boolean getExpandable() {
        return isExpandable;
    }

    public void setExpandable(Boolean expandable) {
        isExpandable = expandable;
    }
}
