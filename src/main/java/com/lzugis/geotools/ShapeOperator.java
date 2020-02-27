package com.lzugis.geotools;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geotools.data.FeatureWriter;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class ShapeOperator {
	private static String rootPath = System.getProperty("user.dir");

	/**
	 * 写入shape文件
	 * @param filepath
	 */
	public void writeShape(String filepath) {
		try {
			//创建shape文件对象
			File file = new File(filepath);
			Map<String, Serializable> params = new HashMap<String, Serializable>();
			params.put( ShapefileDataStoreFactory.URLP.key, file.toURI().toURL() );
			ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);
			//定义图形信息和属性信息
			SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
			tb.setCRS(DefaultGeographicCRS.WGS84);
			tb.setName("shapefile");
			tb.add("the_geom", Point.class);
			tb.add("POIID", Long.class);
			tb.add("NAMEC", String.class);
			ds.createSchema(tb.buildFeatureType());
			//设置编码
            Charset charset = Charset.forName("GBK");
            ds.setCharset(charset);
			//设置Writer
			FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);
			//写下一条
			SimpleFeature feature = writer.next();
			feature.setAttribute("the_geom", new GeometryFactory().createPoint(new Coordinate(116.123, 39.345)));
			feature.setAttribute("POIID", 1234567890l);
			feature.setAttribute("NAMEC", "某兴趣点1");
			feature = writer.next();
			feature.setAttribute("the_geom", new GeometryFactory().createPoint(new Coordinate(116.456, 39.678)));
			feature.setAttribute("POIID", 1234567891l);
			feature.setAttribute("NAMEC", "某兴趣点2");
			writer.write();
			writer.close();
			ds.dispose();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void copyShape(String srcfilepath, String destfilepath){
		try {
			//源shape文件
			ShapefileDataStore shapeDS = (ShapefileDataStore) new ShapefileDataStoreFactory().createDataStore(new File(srcfilepath).toURI().toURL());
			//创建目标shape文件对象
			Map<String, Serializable> params = new HashMap<String, Serializable>();
	        FileDataStoreFactorySpi factory = new ShapefileDataStoreFactory();
	        params.put(ShapefileDataStoreFactory.URLP.key, new File(destfilepath).toURI().toURL());
	        ShapefileDataStore ds = (ShapefileDataStore) factory.createNewDataStore(params);
	        // 设置属性
	        SimpleFeatureSource fs = shapeDS.getFeatureSource(shapeDS.getTypeNames()[0]);
	        //下面这行还有其他写法，根据源shape文件的simpleFeatureType可以不用retype，而直接用fs.getSchema设置
	        ds.createSchema(SimpleFeatureTypeBuilder.retype(fs.getSchema(), DefaultGeographicCRS.WGS84));
	        
	        //设置writer
	        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);
	        
	        //写记录
	        SimpleFeatureIterator it = fs.getFeatures().features();
	        try {
	            while (it.hasNext()) {
	                SimpleFeature f = it.next();
	                SimpleFeature fNew = writer.next();
	                fNew.setAttributes(f.getAttributes());
	            }
	        } 
	        finally {
	            it.close();
	        }
	        writer.write();
	        writer.close();
	        ds.dispose();
	        shapeDS.dispose();
		} 
		catch (Exception e) { 
			e.printStackTrace();	
		}
	}
	/**
	 * 读取shape文件
	 * @param shpfile
	 * @return
	 */
	public List<Map<String, Object>> readShape(String shpfile){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try{
        	File file = new File(shpfile);
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
            while (itertor.hasNext())
            {
                Map<String,Object> data  = new HashMap<String, Object>();
                SimpleFeature feature = itertor.next();
                Collection<Property> p = feature.getProperties();
                Iterator<Property> it = p.iterator();
                while(it.hasNext()) {
                    Property pro = it.next();
                    String field = pro.getName().toString();
                    if(field.equals("the_geom")){
                    	String value = pro.getValue().toString();
                    	System.out.println(value);
                    }
//                    System.out.println(field);
//                    System.out.println(value);
//                    field = field.equals("the_geom")?"wkt":field;
//                    data.put(field, value);
                }
                list.add(data);
            }
            itertor.close();
        }
        catch(Exception e){
        	e.printStackTrace();
        }
		return list;
	}
	/**
	 * 工具类测试方法
	 * @param args
	 */
	public static void main(String[] args) {
		ShapeOperator shpOperator =new ShapeOperator();
        try{
    		long start = System.currentTimeMillis();

			String srcPath = rootPath + "/data/shp/bou1_4p.shp";
			String desPath = rootPath + "/out/bou1_4p_copy.shp";
			shpOperator.copyShape(srcPath, desPath);

//    		String shpPath = rootPath+"/out/ProjTrans.shp";
//    		shpOperator.writeShape(shpPath);
//    		List<Map<String, Object>> list = shpOperator.readShape(shpPath);
//    		System.out.println(list.toString());
    		System.out.println("共耗时"+(System.currentTimeMillis() - start)+"ms");
        }
        catch(Exception e){
        	e.printStackTrace();
        }
	}
}
