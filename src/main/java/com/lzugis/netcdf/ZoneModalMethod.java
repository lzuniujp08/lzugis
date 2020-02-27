package com.lzugis.netcdf;

import com.lzugis.CommonMethod;
import com.vividsolutions.jts.geom.*;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.json.simple.JSONObject;
import org.opengis.feature.simple.SimpleFeature;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nunu
 * @date 2017年11月16日 下午1:59:58
 * @Description 经纬度取下标
 */
public class ZoneModalMethod {
    private static List provinces;
    private static CommonMethod cm = new CommonMethod();

    private String shpfile = "D:\\project\\2017年\\railway\\data\\base_province.shp";

    public ZoneModalMethod() {
        try {
            System.out.println("开始初始化边界。");
            initBoundryShp();
            System.out.println("完成初始化边界。");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化边界shp
     */
    private void initBoundryShp() {
        try {
            //初始化shape
            File file = new File(shpfile);
            ShapefileDataStore shpDataStore = null;
            shpDataStore = new ShapefileDataStore(file.toURL());
            //设置编码
            Charset charset = Charset.forName("GBK");
            shpDataStore.setCharset(charset);
            String typeName = shpDataStore.getTypeNames()[0];
            SimpleFeatureSource featureSource = null;
            featureSource = shpDataStore.getFeatureSource(typeName);
            SimpleFeatureCollection result = featureSource.getFeatures();
            SimpleFeatureIterator itertor = result.features();

            provinces = new ArrayList();

            while (itertor.hasNext()) {
                SimpleFeature feature = itertor.next();
                String name = (String) feature.getAttribute("areaname"),
                        code = cm.getPingYin(name);
                Geometry geom = (Geometry) feature.getAttribute("the_geom");
                Map map = new HashMap();
                Envelope envelope = geom.getEnvelopeInternal();
                map.put("name", name);
                map.put("code", code);
                map.put("geom", geom);
                map.put("min", new double[]{envelope.getMinX(), envelope.getMinY()});
                map.put("max", new double[]{envelope.getMaxX(), envelope.getMaxY()});
                provinces.add(map);
            }
            itertor.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取区域蒙板xian
     *
     * @param zoneData
     * @return
     */
    public Map getZoneModalJson(Map zoneData) {
        Map map = new HashMap();
        double[] min = (double[]) zoneData.get("min"),
                max = (double[]) zoneData.get("max");
        MultiPolygon geom = (MultiPolygon) zoneData.get("geom");
        String name = zoneData.get("name").toString(),
                code = zoneData.get("code").toString();
        map.put("cname", name);
        map.put("ename", code);

        System.out.println("正在计算" + name + "......");

        int[] indexMin = getIndexByLatlon((float) min[1], (float) min[0]),
                indexMax = getIndexByLatlon((float) max[1], (float) max[0]);
        int xmin = indexMin[1],
                ymin = indexMin[0],
                xmax = indexMax[1],
                ymax = indexMax[0];
        map.put("xmin", xmin - 1);
        map.put("ymin", ymin - 1);
        map.put("xmax", xmax + 1);
        map.put("ymax", ymax + 1);
        System.out.println(JSONObject.toJSONString(map));
        List list = new ArrayList();
        for (int i = xmin - 1; i <= xmax + 1; i++) {
            List _list = new ArrayList();
            for (int j = ymin - 1; j <= ymax + 1; j++) {
                float[] latlon = getLatlonByIndex(j, i);
                Geometry gLonlat = new GeometryFactory().createPoint(new Coordinate(latlon[1], latlon[0]));
                int isIn = gLonlat.within(geom) ? 1 : -1;
                _list.add(isIn);
            }
            System.out.println(i);
            list.add(_list);
        }
        map.put("data", list);
        return map;
    }

    int[] getIndexByLatlon(float lat, float lon) {
        int latIndex = (int) ((lat - 18.160f) / 0.01f);
        int lonIndex = (int) ((lon - 73.446f) / 0.01f);
        return new int[]{latIndex, lonIndex};
    }

    float[] getLatlonByIndex(int x, int y) {
        float lon = 73.446f + (y * 0.01f);
        float lat = 18.160f + (x * 0.01f);
        return new float[]{lat, lon};
    }

    /**
     * 测试类
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        ZoneModalMethod china = new ZoneModalMethod();
        long start = System.currentTimeMillis();
        Map province = (Map) provinces.get(8);
        Map map = china.getZoneModalJson(province);
        String txtPath = "C:\\Users\\admin\\Desktop\\boundry\\" + province.get("cname") + ".json";
        cm.append2File(txtPath, JSONObject.toJSONString(map));
        System.out.println("\r\n共耗时" + (System.currentTimeMillis() - start) + "ms");
        /*for (int i = 0; i < provinces.size(); i++) {
            Map province = (Map) provinces.get(i);
            Map map = china.getZoneModalJson(province);
            String txtPath = "C:\\Users\\admin\\Desktop\\boundry\\" + map.get("cname") + ".json";
            cm.append2File(txtPath, JSONObject.toJSONString(map));
            System.out.println("\r\n共耗时" + (System.currentTimeMillis() - start) + "ms");
        }*/
    }
}
