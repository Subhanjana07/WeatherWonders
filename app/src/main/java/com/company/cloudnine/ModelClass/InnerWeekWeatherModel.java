package com.company.cloudnine.ModelClass;

public class InnerWeekWeatherModel {
    String innerRVtime;
    String innerRVtemp;
    String innerRVicon;
    String innerRVcondition;

    public InnerWeekWeatherModel(String innerRVtime, String innerRVtemp, String innerRVicon, String innerRVcondition) {
        this.innerRVtime = innerRVtime;
        this.innerRVtemp = innerRVtemp;
        this.innerRVicon = innerRVicon;
        this.innerRVcondition = innerRVcondition;
    }

    public String getInnerRVtime() {
        return innerRVtime;
    }

    public void setInnerRVtime(String innerRVtime) {
        this.innerRVtime = innerRVtime;
    }

    public String getInnerRVtemp() {
        return innerRVtemp;
    }

    public void setInnerRVtemp(String innerRVtemp) {
        this.innerRVtemp = innerRVtemp;
    }

    public String getInnerRVicon() {
        return innerRVicon;
    }

    public void setInnerRVicon(String innerRVicon) {
        this.innerRVicon = innerRVicon;
    }

    public String getInnerRVcondition() {
        return innerRVcondition;
    }

    public void setInnerRVcondition(String innerRVcondition) {
        this.innerRVcondition = innerRVcondition;
    }
}
