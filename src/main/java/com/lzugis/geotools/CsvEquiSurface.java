package com.lzugis.geotools;

import com.amazonaws.util.json.JSONObject;
import com.lzugis.CommonMethod;
import com.lzugis.geotools.utils.FeaureUtil;
import com.lzugis.geotools.utils.GeoJSONUtil;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import wContour.Contour;
import wContour.Global.Border;
import wContour.Global.PointD;
import wContour.Global.PolyLine;
import wContour.Global.Polygon;
import wContour.Interpolate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by admin on 2017/8/29.
 */
public class CsvEquiSurface {
    private static String rootPath = System.getProperty("user.dir");

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        EquiSurface equiSurface = new EquiSurface();
        CommonMethod cm = new CommonMethod();

        String csvpath = rootPath + "/data/xls/tem.csv";
        File csv = new File(csvpath);  // CSV文件路径
        BufferedReader br = null;

        String boundryFile = rootPath + "/data/shp/bou1_4p.shp";
        boolean isclip = true;
        double[] dataInterval = new double[]{8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40};

        try {
            List<Double> lon = new ArrayList<Double>(),
                    lat = new ArrayList<Double>(),
                    heat = new ArrayList<Double>();
            br = new BufferedReader(new FileReader(csv));
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] lineData = line.split(",");
                lon.add(Double.parseDouble(lineData[1]));
                lat.add(Double.parseDouble(lineData[2]));
                heat.add(Double.parseDouble(lineData[3]));
            }

            double[][] trainData = new double[3][lon.size()];
            for (int i = 0; i < lon.size(); i++) {
                trainData[0][i] = lon.get(i);
                trainData[1][i] = lat.get(i);
                trainData[2][i] = heat.get(i);
            }
            int[] size = new int[]{lon.size(), lon.size()};
            String strJson = equiSurface.calEquiSurface(trainData, dataInterval, size, boundryFile, isclip);
            String strFile = rootPath + "/out/chinaheat.json";
            cm.append2File(strFile, strJson);
            System.out.println(strFile + "差值成功, 共耗时" + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
