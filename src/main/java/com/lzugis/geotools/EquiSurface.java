package com.lzugis.geotools;

import com.amazonaws.util.json.JSONObject;
import com.lzugis.CommonMethod;
import com.lzugis.geotools.utils.FeaureUtil;
import com.lzugis.geotools.utils.GeoJSONUtil;
import com.vividsolutions.jts.geom.Geometry;
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

import java.io.File;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by admin on 2017/8/29.
 */
public class EquiSurface {
    private static String rootPath = System.getProperty("user.dir");

    /**
     * 生成等值面
     *
     * @param trainData    训练数据
     * @param dataInterval 数据间隔
     * @param size         大小，宽，高
     * @param boundryFile  四至
     * @param isclip       是否裁剪
     * @return
     */
    public String calEquiSurface(double[][] trainData,
                                 double[] dataInterval,
                                 int[] size,
                                 String boundryFile,
                                 boolean isclip) {
        String geojsonpogylon = "";
        try {
            double _undefData = -9999.0;
            SimpleFeatureCollection polygonCollection = null;
            List<PolyLine> cPolylineList = new ArrayList<PolyLine>();
            List<Polygon> cPolygonList = new ArrayList<Polygon>();

            int width = size[0],
                    height = size[1];
            double[] _X = new double[width];
            double[] _Y = new double[height];

            File file = new File(boundryFile);
            ShapefileDataStore shpDataStore = null;

            shpDataStore = new ShapefileDataStore(file.toURL());
            //设置编码
            Charset charset = Charset.forName("GBK");
            shpDataStore.setCharset(charset);
            String typeName = shpDataStore.getTypeNames()[0];
            SimpleFeatureSource featureSource = null;
            featureSource = shpDataStore.getFeatureSource(typeName);
            SimpleFeatureCollection fc = featureSource.getFeatures();

            double minX = fc.getBounds().getMinX();
            double minY = fc.getBounds().getMinY();
            double maxX = fc.getBounds().getMaxX();
            double maxY = fc.getBounds().getMaxY();

            Interpolate.CreateGridXY_Num(minX, minY, maxX, maxY, _X, _Y);
            double[][] _gridData = new double[width][height];

            int nc = dataInterval.length;

            _gridData = Interpolate.Interpolation_IDW_Neighbor(trainData,
                    _X, _Y, 12, _undefData);// IDW插值

            int[][] S1 = new int[_gridData.length][_gridData[0].length];
            /**
             * double[][] S0,
             * double[] X,
             * double[] Y,
             * int[][] S1,
             * double undefData
             */
            List<Border> _borders = Contour.tracingBorders(_gridData, _X, _Y,
                    S1, _undefData);

            /**
             * double[][] S0,
             * double[] X,
             * double[] Y,
             * int nc,
             * double[] contour,
             * double undefData,
             * List<Border> borders,
             * int[][] S1
             */
            cPolylineList = Contour.tracingContourLines(_gridData, _X, _Y, nc,
                    dataInterval, _undefData, _borders, S1);// 生成等值线

            cPolylineList = Contour.smoothLines(cPolylineList);// 平滑

            cPolygonList = Contour.tracingPolygons(_gridData, cPolylineList,
                    _borders, dataInterval);

            geojsonpogylon = getPolygonGeoJson(cPolygonList);

            if (isclip) {
                polygonCollection = GeoJSONUtil.readGeoJsonByString(geojsonpogylon);
                FeatureSource dc = clipFeatureCollection(fc, polygonCollection);
                geojsonpogylon = getPolygonGeoJson(dc.getFeatures());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return geojsonpogylon;
    }

    public FeatureSource clipFeatureCollection(FeatureCollection fc,
                                               SimpleFeatureCollection gs) {
        FeatureSource cs = null;
        try {
            List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
            FeatureIterator contourFeatureIterator = gs.features();
            FeatureIterator dataFeatureIterator = fc.features();
            while (dataFeatureIterator.hasNext()) {
                Feature dataFeature = dataFeatureIterator.next();
                Geometry dataGeometry = (Geometry) dataFeature.getProperty(
                        "the_geom").getValue();
                while (contourFeatureIterator.hasNext()) {
                    Feature contourFeature = contourFeatureIterator.next();
                    Geometry contourGeometry = (Geometry) contourFeature
                            .getProperty("geometry").getValue();
                    double lv = (Double) contourFeature.getProperty("lvalue")
                            .getValue();
                    double hv = (Double) contourFeature.getProperty("hvalue")
                            .getValue();
                    if (dataGeometry.intersects(contourGeometry)) {
                        Geometry geo = dataGeometry
                                .intersection(contourGeometry);
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("the_geom", geo);
                        map.put("lvalue", lv);
                        map.put("hvalue", hv);
                        values.add(map);
                    }

                }

            }

            contourFeatureIterator.close();
            dataFeatureIterator.close();

            SimpleFeatureCollection sc = FeaureUtil
                    .creatSimpleFeatureByFeilds(
                            "polygons",
                            "crs:4326,the_geom:MultiPolygon,lvalue:double,hvalue:double",
                            values);
            cs = FeaureUtil.creatFeatureSourceByCollection(sc);

        } catch (Exception e) {
            e.printStackTrace();
            return cs;
        }

        return cs;
    }

    public String getPolygonGeoJson(FeatureCollection fc) {
        FeatureJSON fjson = new FeatureJSON();
        StringBuffer sb = new StringBuffer();
        try {
            sb.append("{\"type\": \"FeatureCollection\",\"features\": ");
            FeatureIterator itertor = fc.features();
            List<String> list = new ArrayList<String>();
            while (itertor.hasNext()) {
                SimpleFeature feature = (SimpleFeature) itertor.next();
                StringWriter writer = new StringWriter();
                fjson.writeFeature(feature, writer);
                list.add(writer.toString());
            }
            itertor.close();
            sb.append(list.toString());
            sb.append("}");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String getPolygonGeoJson(List<Polygon> cPolygonList) {
        String geo = null;
        String geometry = " { \"type\":\"Feature\",\"geometry\":";
        String properties = ",\"properties\":{ \"hvalue\":";

        String head = "{\"type\": \"FeatureCollection\"," + "\"features\": [";
        String end = "  ] }";
        if (cPolygonList == null || cPolygonList.size() == 0) {
            return null;
        }
        try {
            for (Polygon pPolygon : cPolygonList) {

                List<Object> ptsTotal = new ArrayList<Object>();
                List<Object> pts = new ArrayList<Object>();

                PolyLine pline = pPolygon.OutLine;

                for (PointD ptD : pline.PointList) {
                    List<Double> pt = new ArrayList<Double>();
                    pt.add(ptD.X);
                    pt.add(ptD.Y);
                    pts.add(pt);
                }

                ptsTotal.add(pts);

                if (pPolygon.HasHoles()) {
                    for (PolyLine cptLine : pPolygon.HoleLines) {
                        List<Object> cpts = new ArrayList<Object>();
                        for (PointD ccptD : cptLine.PointList) {
                            List<Double> pt = new ArrayList<Double>();
                            pt.add(ccptD.X);
                            pt.add(ccptD.Y);
                            cpts.add(pt);
                        }
                        if (cpts.size() > 0) {
                            ptsTotal.add(cpts);
                        }
                    }
                }

                JSONObject js = new JSONObject();
                js.put("type", "Polygon");
                js.put("coordinates", ptsTotal);
                double hv = pPolygon.HighValue;
                double lv = pPolygon.LowValue;

                if (hv == lv) {
                    if (pPolygon.IsClockWise) {
                        if (!pPolygon.IsHighCenter) {
                            hv = hv - 0.1;
                            lv = lv - 0.1;
                        }

                    } else {
                        if (!pPolygon.IsHighCenter) {
                            hv = hv - 0.1;
                            lv = lv - 0.1;
                        }
                    }
                } else {
                    if (!pPolygon.IsClockWise) {
                        lv = lv + 0.1;
                    } else {
                        if (pPolygon.IsHighCenter) {
                            hv = hv - 0.1;
                        }
                    }

                }

                geo = geometry + js.toString() + properties + hv
                        + ", \"lvalue\":" + lv + "} }" + "," + geo;

            }
            if (geo.contains(",")) {
                geo = geo.substring(0, geo.lastIndexOf(","));
            }

            geo = head + geo + end;
        } catch (Exception e) {
            e.printStackTrace();
            return geo;
        }
        return geo;
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        EquiSurface equiSurface = new EquiSurface();
        CommonMethod cm = new CommonMethod();

        double[] bounds = {73.4510046356223, 18.1632471876417,
                134.976797646506, 53.5319431522236};

        double[][] trainData = new double[3][100];

        for (int i = 0; i < 100; i++) {
            double x = bounds[0] + new Random().nextDouble() * (bounds[2] - bounds[0]),
                    y = bounds[1] + new Random().nextDouble() * (bounds[3] - bounds[1]),
                    v = 0 + new Random().nextDouble() * (45 - 0);
            trainData[0][i] = x;
            trainData[1][i] = y;
            trainData[2][i] = v;
        }

        double[] dataInterval = new double[]{20, 25, 30, 35, 40, 45};

        String boundryFile = rootPath + "/data/shp/bou1_4p.shp";

        int[] size = new int[]{100, 100};

        boolean isclip = true;

        try {
            String strJson = equiSurface.calEquiSurface(trainData, dataInterval, size, boundryFile, isclip);
            String strFile = rootPath + "/out/china1.json";
            cm.append2File(strFile, strJson);
            System.out.println(strFile + "差值成功, 共耗时" + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
