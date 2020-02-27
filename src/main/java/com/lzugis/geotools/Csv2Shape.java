package com.lzugis.geotools;

import com.lzugis.CommonMethod;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/9/6.
 */
public class Csv2Shape {
    static Csv2Shape csv2Shp = new Csv2Shape();
    private static String rootPath = System.getProperty("user.dir");
    private CommonMethod cm = new CommonMethod();

    private List<Map<String, Object>> getFields(String linedata) {
        List<Map<String, Object>> list = new ArrayList<>();
        String[] fields = linedata.split(",");
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            Class fieldType = field.toLowerCase().equals("lon") ? Double.class : String.class;
            if (field.toLowerCase().equals("lat")) fieldType = Double.class;
            Map<String, Object> map = new HashMap<>();
            map.put("name", field);
            map.put("type", fieldType);
            list.add(map);
        }
        return list;
    }

    ;

    public void excel2Shape(String csvfile, String shppath) {
        File csv = new File(csvfile);  // CSV文件路径
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(csv));
            String line = "";
            line = br.readLine();
            //创建shape文件对象
            File file = new File(shppath);
            Map<String, Serializable> params = new HashMap<String, Serializable>();
            params.put(ShapefileDataStoreFactory.URLP.key, file.toURI().toURL());
            ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);
            //定义图形信息和属性信息
            SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
            tb.setCRS(DefaultGeographicCRS.WGS84);
            tb.setName("shapefile");
            tb.add("the_geom", Point.class);

            List list = getFields(line);
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = (Map<String, Object>) list.get(i);
                tb.add(map.get("name").toString(), (Class) map.get("type"));
            }
            ds.createSchema(tb.buildFeatureType());
            //设置编码
            Charset charset = Charset.forName("GBK");
            ds.setCharset(charset);
            //设置Writer
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);
            //写下一条
            SimpleFeature feature = null;

            while ((line = br.readLine()) != null) {
                String[] lineData = line.split(",");
                feature = writer.next();
                Map mapLonLat = new HashMap();
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> mapFields = (Map<String, Object>) list.get(i);
                    String fieldName = mapFields.get("name").toString();
                    feature.setAttribute(fieldName, lineData[i]);
                    if (fieldName.toLowerCase().equals("lon") || fieldName.toLowerCase().equals("lat")) {
                        mapLonLat.put(fieldName, Double.parseDouble(lineData[i]));
                    }
                }
                feature.setAttribute("the_geom", new GeometryFactory().createPoint(new Coordinate((double) mapLonLat.get("lon"), (double) mapLonLat.get("lat"))));
            }
            writer.write();
            writer.close();
            ds.dispose();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        String csvpath = rootPath + "/data/xls/capital.txt",
                shppath = rootPath + "/out/capital.shp";
        csv2Shp.excel2Shape(csvpath, shppath);
        System.out.println("共耗时" + (System.currentTimeMillis() - start) + "ms");
    }
}
