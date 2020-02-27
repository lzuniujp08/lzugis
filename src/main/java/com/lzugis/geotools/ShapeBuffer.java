package com.lzugis.geotools;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

public class ShapeBuffer {
	private static String rootPath = System.getProperty("user.dir");

	/**
	 * 缓冲区分析
	 * @param geom
	 * @param distance
	 * @return
	 */
	public Geometry calBuffer(Geometry geom, double distance){
		return geom.buffer(distance);
	}
	
	public static void main(String[] args){
		long start = System.currentTimeMillis();
		
		ShapeBuffer geoR = new ShapeBuffer();
		String shpfile = rootPath + "/data/shp/bou1_4p.shp";
		String buffile = rootPath + "/out/bou1_4p_buffer.shp";
		
		try{
			//读取shp文件
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
            
            //创建shape文件对象
			File fileBuf = new File(buffile);
			Map<String, Serializable> params = new HashMap<String, Serializable>();
			params.put( ShapefileDataStoreFactory.URLP.key, fileBuf.toURI().toURL() );
			ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);
			
			SimpleFeatureType sft = featureSource.getSchema();
			List<AttributeDescriptor> attrs = sft.getAttributeDescriptors();
			
			//定义图形信息和属性信息
			SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
			tb.setCRS(DefaultGeographicCRS.WGS84);
			tb.setName("shapefile");
			for(int i=0;i<attrs.size();i++){
				AttributeDescriptor attr = attrs.get(i);
				String fieldName = attr.getName().toString();
				if(fieldName=="the_geom"){
					tb.add(fieldName, Polygon.class);
				}
				else{
					tb.add(fieldName, String.class);
				}
			}
			ds.createSchema(tb.buildFeatureType());
			//设置编码
            ds.setCharset(charset);
	        
			//设置Writer
			FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);
            
            while (itertor.hasNext())
            {
                SimpleFeature feature = itertor.next();
                SimpleFeature featureBuf = writer.next();
                featureBuf.setAttributes(feature.getAttributes());
                
                Geometry geo = (Geometry)feature.getAttribute("the_geom");
                Geometry geoBuffer = geoR.calBuffer(geo, 0.01);
                featureBuf.setAttribute("the_geom", geoBuffer);
            } 
            writer.write();
			writer.close();
            itertor.close();
            shpDataStore.dispose();
            ds.dispose();
        }
        catch(Exception e){
        	e.printStackTrace();
        }
		System.out.println("共耗时"+(System.currentTimeMillis() - start)+"ms");
	}
}
