package com.lzugis.geotools.utils;

public class BdProjTransform {

    private final double pi = 3.14159265358979324;
    private final double a = 6378245.0;
    private final double ee = 0.00669342162296594323;
    private final double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    //是否超出国界
    public boolean outOfChina(double lat,double lon){
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }

    public double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public double transformLon(double x,  double y){
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 地球坐标转换为火星坐标
     * @param wgLat
     * @param wgLon
     * @return
     */
    public double[] transform2Mars(double wgLat, double wgLon){
        if (outOfChina(wgLat, wgLon)){
            double mgLat  = wgLat;
            double mgLon = wgLon;
            double[] Points=new double[]{mgLon, mgLat};
            return Points;
        }
        double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
        double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
        double radLat = wgLat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);

        double mgLat = wgLat + dLat;
        double mgLon = wgLon + dLon;
        double[] Points=new double[]{mgLon, mgLat};
        return Points;
    }

    /**
     * 火星（谷歌）坐标转换为百度坐标
     * @param gg_lat
     * @param gg_lon
     */
    public double[] bd_encrypt(double gg_lat, double gg_lon){
        double x = gg_lon, y = gg_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        double[] Points=new double[]{bd_lon, bd_lat};
        return Points;
    }

    /**
     * 百度转火星（谷歌）
     * @param bd_lat
     * @param bd_lon
     */
    public double[] bd_decrypt(double bd_lat, double bd_lon){
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gg_lon = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        double[] Points=new double[]{gg_lon, gg_lat};
        return Points;
    }

    public double[] wgs84ToBd09(double lon, double lat){
        double[] point = transform2Mars(lat, lon);
        return bd_encrypt(point[1], point[0]);
    }


    public static void main(String[] args){
        BdProjTransform proj = new BdProjTransform();
        double[] point = proj.wgs84ToBd09(73.6770723800199, 39.13769693730567);
        System.out.println(point[0]+","+point[1]);

    }
}
