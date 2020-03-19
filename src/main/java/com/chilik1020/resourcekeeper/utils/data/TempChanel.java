package com.chilik1020.resourcekeeper.utils.data;

public class TempChanel {
    private long number;
    private String name;
    private boolean isEnable;
    private double limitMin;
    private double limitMax;

    public TempChanel(long number, String name, boolean isEnable, double limitMin, double limitMax) {
        this.number = number;
        this.name = name;
        this.isEnable = isEnable;
        this.limitMin = limitMin;
        this.limitMax = limitMax;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public double getLimitMin() {
        return limitMin;
    }

    public void setLimitMin(double limitMin) {
        this.limitMin = limitMin;
    }

    public double getLimitMax() {
        return limitMax;
    }

    public void setLimitMax(double limitMax) {
        this.limitMax = limitMax;
    }

    @Override
    public String toString() {
        return "TempChanel{" +
                "number=" + number +
                ", name='" + name + '\'' +
                ", isEnable=" + isEnable +
                ", limitMin=" + limitMin +
                ", limitMax=" + limitMax +
                '}';
    }
}
