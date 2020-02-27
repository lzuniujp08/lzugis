package com.lzugis.geohash;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class GeoHash {
    private static int numbits = 6 * 5;

    private static double minLat;//每格纬度的单位大小
    private static double minLng;//每个经度的倒下
    final static char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p',
            'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    final static HashMap<Character, Integer> lookup = new HashMap<Character, Integer>();

    final static double MAXLAT = 90, MINLAT = -90,
            MAXLNG = 180, MINLNG = -180;

    static {
        int k = 0;
        for (char c : digits)
            lookup.put(c, k++);
        minLat = MAXLAT - MINLAT;
        for (int i = 0; i < numbits; i++) {
            minLat /= 2.0;
        }
        minLng = MAXLNG - MINLNG;
        for (int i = 0; i < numbits; i++) {
            minLng /= 2.0;
        }
    }
    /**
     * 解码
     * @param geohash
     * @return
     */
    public Map decode(String geohash) {
        StringBuilder buffer = new StringBuilder();
        for (char c : geohash.toCharArray()) {

            int i = lookup.get(c) + 32;
            buffer.append(Integer.toString(i, 2).substring(1));
        }

        BitSet lonset = new BitSet();
        BitSet latset = new BitSet();

        int j = 0;
        for (int i = 0; i < numbits * 2; i += 2) {
            boolean isSet = false;
            if (i < buffer.length())
                isSet = buffer.charAt(i) == '1';
            lonset.set(j++, isSet);
        }

        j = 0;
        for (int i = 1; i < numbits * 2; i += 2) {
            boolean isSet = false;
            if (i < buffer.length())
                isSet = buffer.charAt(i) == '1';
            latset.set(j++, isSet);
        }
        double lon = _decode(lonset, -180, 180);
        double lat = _decode(latset, -90, 90);
        Map map = new HashMap();
        map.put("lon", lon);
        map.put("lat", lat);
        return map;
    }

    private double _decode(BitSet bs, double min, double max) {
        double mid = 0;
        for (int i = 0; i < bs.length(); i++) {
            mid = (min + max) / 2;
            if (bs.get(i))
                min = mid;
            else
                max = mid;
        }
        return mid;
    }
    /**
     * 编码
     * @param lat
     * @param lon
     * @param precision
     * @return
     */
    public String encode(double lat, double lon, int precision) {
        BitSet latbits = _getBits(lat, -90, 90);
        BitSet lonbits = _getBits(lon, -180, 180);
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < numbits; i++) {
            buffer.append((lonbits.get(i)) ? '1' : '0');
            buffer.append((latbits.get(i)) ? '1' : '0');
        }
        String _encode = _base32(Long.parseLong(buffer.toString(), 2));
        if(precision>0)_encode = _encode.substring(0, precision);
        return _encode;
    }

    private BitSet _getBits(double lat, double min, double max) {
        BitSet buffer = new BitSet(numbits);
        for (int i = 0; i < numbits; i++) {
            double mid = (min + max) / 2;
            if (lat >= mid) {
                buffer.set(i);
                min = mid;
            } else {
                max = mid;
            }
        }
        return buffer;
    }

    private String _base32(long i) {
        char[] buf = new char[65];
        int charPos = 64;
        boolean negative = (i < 0);
        if (!negative)
            i = -i;
        while (i <= -32) {
            buf[charPos--] = digits[(int) (-(i % 32))];
            i /= 32;
        }
        buf[charPos] = digits[(int) (-i)];

        if (negative)
            buf[--charPos] = '-';
        return new String(buf, charPos, (65 - charPos));
    }

    public Map bounds(String geohash){
        Map mapSw = decode(adjacent(geohash, "sw")),
                mapNe = decode(adjacent(geohash, "ne"));
        Map map = new HashMap();
        map.put("sw", mapSw);
        map.put("ne", mapNe);
        return map;
    }

    public Map neighbours(String geohash){
        Map map = new HashMap();
        map.put("s", adjacent(geohash, "s"));
        map.put("n", adjacent(geohash, "n"));
        map.put("e", adjacent(geohash, "e"));
        map.put("w", adjacent(geohash, "w"));
        map.put("sw", adjacent(geohash, "sw"));
        map.put("se", adjacent(geohash, "se"));
        map.put("nw", adjacent(geohash, "nw"));
        map.put("ne", adjacent(geohash, "ne"));
        return map;
    }

    public String adjacent(String geohash, String direction){
        Map lonlat = decode(geohash);
        double lat = (Double) lonlat.get("lat"),
                lon = (Double) lonlat.get("lon");
        switch (direction){
            case "n":{
                lat+=minLat;
                break;
            }
            case "s":{
                lat-=minLat;
                break;
            }
            case "e":{
                lon+=minLng;
                break;
            }
            case "w":{
                lon-=minLng;
                break;
            }
            case "nw":{
                lon-=minLng;
                lat+=minLat;
                break;
            }
            case "sw":{
                lon-=minLng;
                lat-=minLat;
                break;
            }
            case "ne":{
                lat+=minLat;
                lon+=minLng;
                break;
            }
            case "se":{
                lat-=minLat;
                lon+=minLng;
                break;
            }
            default:{
                break;
            }

        }
        return encode(lat, lon, 0);
    }

    /**
     * 获取距离有效位数
     * @param radius
     * @return
     */
    public int effectnum(double radius){
        int result = 0;
        if (radius <= 0) result = 0;
        else if (radius < 1) result = 10;
        else if (radius < 5) result = 9;
        else if (radius < 20) result = 8;
        else if (radius < 77) result = 7;
        else if (radius < 610) result = 6;
        else if (radius < 2400) result = 5;
        else if (radius < 20000) result = 4;
        else if (radius < 78000) result = 3;
        else if (radius < 630000) result = 2;
        else result = 0;
        return result;
    }

    public static void main(String[] args) throws Exception {
        GeoHash geohash = new GeoHash();
        String geoCode = geohash.encode(30.280245, 120.027162, 12);
        System.out.println(geoCode);
        Map geoDecode = geohash.decode(geoCode);
        System.out.println("120.027162, 30.280245");
        System.out.println(geoDecode.toString());
//        Map bounds = geohash.bounds(geoCode);
//        System.out.println(bounds.toString());
//
//        Map neighbours = geohash.neighbours(geoCode);
//        System.out.println(neighbours.toString());
//        System.out.println(minLat);
//        System.out.println(minLng);
    }
}
