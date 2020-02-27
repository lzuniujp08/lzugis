package com.lzugis.geotools;

import java.io.File;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.*;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONObject;
import com.lzugis.CommonMethod;

public class FileFormat {
	private static CommonMethod cm = new CommonMethod();
	private static String rootPath = System.getProperty("user.dir");

	/**
	 * geojson转换为shp文件
	 * @param jsonPath
	 * @param shpPath
	 * @return
	 */
	public Map geojson2Shape(String jsonPath, String shpPath){
		Map map = new HashMap();
		GeometryJSON gjson = new GeometryJSON();
		try{
			String strJson = cm.getFileContent(jsonPath);
			JSONObject json = new JSONObject(strJson);
			JSONArray features = (JSONArray) json.get("features");
			JSONObject feature0 = new JSONObject(features.get(0).toString());
			System.out.println(feature0.toString());
			String strType = ((JSONObject)feature0.get("geometry")).getString("type").toString();
			
			Class<?> geoType = null;
			switch(strType){
				case "Point":
					geoType = Point.class;
				case "MultiPoint":
					geoType = MultiPoint.class;
				case "LineString":
					geoType = LineString.class;
				case "MultiLineString":
					geoType = MultiLineString.class;
				case "Polygon":
					geoType = Polygon.class;
				case "MultiPolygon":
					geoType = MultiPolygon.class;
			}
			//创建shape文件对象
			File file = new File(shpPath);
			Map<String, Serializable> params = new HashMap<String, Serializable>();
			params.put( ShapefileDataStoreFactory.URLP.key, file.toURI().toURL() );
			ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);
			//定义图形信息和属性信息
			SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
			tb.setCRS(DefaultGeographicCRS.WGS84);
			tb.setName("shapefile");
			tb.add("the_geom", geoType);
			tb.add("POIID", Long.class);
			ds.createSchema(tb.buildFeatureType());
			//设置编码
            Charset charset = Charset.forName("GBK");
            ds.setCharset(charset);
			//设置Writer
			FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);
			
			for(int i=0,len=features.length();i<len;i++){
				String strFeature = features.get(i).toString();
				Reader reader = new StringReader(strFeature);
				SimpleFeature feature = writer.next();
				feature.setAttribute("the_geom",gjson.readMultiPolygon(reader));
				feature.setAttribute("POIID",i);
				writer.write();
			}
			writer.close();
			ds.dispose();
			map.put("status", "success");
			map.put("message", shpPath);
		}
		catch(Exception e){
			map.put("status", "failure");
			map.put("message", e.getMessage());
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * shp转换为Geojson
	 * @param shpPath
	 * @return
	 */
	public Map shape2Geojson(String shpPath, String jsonPath){
		Map map = new HashMap();
		
		FeatureJSON fjson = new FeatureJSON();
		
		try{
			StringBuffer sb = new StringBuffer();
			sb.append("{\"type\": \"FeatureCollection\",\"features\": ");
			
			File file = new File(shpPath);
    		ShapefileDataStore shpDataStore = null;
    		
        	shpDataStore = new ShapefileDataStore(file.toURL());
            //设置编码
            Charset charset = Charset.forName("GBK");
            shpDataStore.setCharset(charset);
            String typeName = shpDataStore.getTypeNames()[0];
            SimpleFeatureSource featureSource = null;
            featureSource =  shpDataStore.getFeatureSource (typeName);
            SimpleFeatureCollection result = featureSource.getFeatures();
            SimpleFeatureIterator itertor = result.features();
            JSONArray array = new JSONArray();
            while (itertor.hasNext())
            {
                SimpleFeature feature = itertor.next();
				Geometry geo = (Geometry) feature.getAttribute("the_geom");

                StringWriter writer = new StringWriter();
                fjson.writeFeature(feature, writer);
                JSONObject json = new JSONObject(writer.toString());
                array.put(json);
            }
            itertor.close();
            sb.append(array.toString());
            sb.append("}");
            
            //写入文件
            cm.append2File(jsonPath, sb.toString());
            
			map.put("status", "success");
			map.put("message", sb.toString());
		}
		catch(Exception e){
			map.put("status", "failure");
			map.put("message", e.getMessage());
			e.printStackTrace();
			
		}
		return map;
	}

	public List<File> getFiles(File fileDir, String fileType) {
		List<File> lfile = new ArrayList<File>();
		File[] fs = fileDir.listFiles();
		for (File f : fs) {
			if (f.isFile()) {
				if (fileType
						.equals(f.getName().substring(
								f.getName().lastIndexOf(".") + 1,
								f.getName().length())))
					lfile.add(f);
			} else {
				List<File> ftemps = getFiles(f,fileType);
				lfile.addAll(ftemps);
			}
		}
		return lfile;
	}

	/**
	 * 工具类测试方法
	 * @param args
	 */
	public static void main(String[] args){
		FileFormat fileFormat = new FileFormat();
		long start = System.currentTimeMillis();

//		String shpPath = rootPath + "/data/shp/globedata_temp.shp";
//		String jsonPath = rootPath + "/out/globedata_temp.geojson";

//		Map map = fileFormat.geojson2Shape(jsonPath, shpPath);
//		Map map = fileFormat.shape2Geojson("D:\\data\\nmg\\station.shp", "D:\\data\\nmg\\station.json");
		File shpRoot = new File("D:\\data\\bound_china\\henan\\county");
		List<File> shpFiles = fileFormat.getFiles(shpRoot, "shp");
		for(int i=0;i<shpFiles.size();i++){
			File shpFile = shpFiles.get(i);
			String shpPath = shpFile.getAbsolutePath();
			String jsonPath = shpPath.replaceAll("shp", "json");
			System.out.println(jsonPath+";"+shpPath);
			fileFormat.shape2Geojson(shpPath, jsonPath);
		}
		System.out.println("共耗时"+(System.currentTimeMillis() - start)+"ms");
	}
}
