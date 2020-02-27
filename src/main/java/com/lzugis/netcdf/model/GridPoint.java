package com.lzugis.netcdf.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 根据经纬度找格点
 */
public class GridPoint {
    private int num;
    private int nx;
    private int ny;
    private double hgt;
    private double dis;
    private double lat;
    private double lon;

    private Map<String, Float> values;

    public GridPoint(int nx, int ny, double lat, double lon) {
        this.nx = nx;
        this.ny = ny;
        this.lat = lat;
        this.lon = lon;
        this.values = new HashMap<>();
    }

    public void put(String key, Float value) {
        this.values.put(key, value);
    }

    public Float get(String key) {
        return this.values.get(key);
    }

    public void printf() {
        String format = "    %d    %d    %d    %f    %f    %f    %f\n";
        System.out.printf(format, num, nx, ny, hgt, dis, lat, lon);
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getNx() {
        return nx;
    }

    public int getNy() {
        return ny;
    }

    public double getDis() {
        return dis;
    }

    public void setDis(double dis) {
        this.dis = dis;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public static void main(String[] args) {
        GridPoint lp = new GridPoint(433, 289, 39.578f, 116.779f);
        lp.printf();
    }
}
