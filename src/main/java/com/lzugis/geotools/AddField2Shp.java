package com.lzugis.geotools;

import com.vividsolutions.jts.geom.Point;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

public class AddField2Shp {
	private static String rootPath = System.getProperty("user.dir");
	private static ShapefileDataStore shpDataStore = null;

	public AddField2Shp(){
		super();
		try{
			String shpfile = rootPath + "/data/shp/res1_4m.shp";
			File file = new File(shpfile);
			shpDataStore = new ShapefileDataStore(file.toURL());
			//设置编码
			Charset charset = Charset.forName("GBK");
			shpDataStore.setCharset(charset);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void copyShape(String destfile, String[] fields, Class[] types){
		try {
			//创建目标shape文件对象
			Map<String, Serializable> params = new HashMap<String, Serializable>();
			FileDataStoreFactorySpi factory = new ShapefileDataStoreFactory();
			params.put(ShapefileDataStoreFactory.URLP.key, new File(destfile).toURI().toURL());
			ShapefileDataStore ds = (ShapefileDataStore) factory.createNewDataStore(params);

			//设置属性
			SimpleFeatureSource fs = shpDataStore.getFeatureSource(shpDataStore.getTypeNames()[0]);
			SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
			tb.setCRS(DefaultGeographicCRS.WGS84);
			tb.setName("shapefile");
			for(int i=0;i<fields.length;i++){
				tb.add(fields[i], types[i]);
			}
			ds.createSchema(tb.buildFeatureType());
			//设置编码
			Charset charset = Charset.forName("GBK");
			ds.setCharset(charset);

			//设置writer
			FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);
			//写记录
			SimpleFeatureIterator it = fs.getFeatures().features();
			try {
				while (it.hasNext()) {
					SimpleFeature f = it.next();
					SimpleFeature fNew = writer.next();
					for(int i=0;i<fields.length;i++){
						tb.add(fields[i], types[i]);
						String _filed = fields[i];
						Object _value = "level".equals(_filed.toLowerCase())?
								getLevel(1, 4):f.getAttribute(fields[i]);
						fNew.setAttribute(fields[i], _value);
					}
				}
			}
			finally {
				it.close();
			}
			writer.write();
			writer.close();
			ds.dispose();
			shpDataStore.dispose();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getLevel(int min, int max){
		int number = new Random().nextInt(max) + min;
		return String.valueOf(number);
	}

	/**
	 * 工具类测试方法
	 * @param args
	 */
	public static void main(String[] args) {
		AddField2Shp shpOperator =new AddField2Shp();
		try{
			long start = System.currentTimeMillis();

			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			String currentDate = formatter.format(new Date());
			String desPath = rootPath + "/out/res1_4m_" + currentDate + ".shp";
			String[] fields = new String[]{"the_geom", "NAME", "GBCODE", "LEVEL"};
			Class[] types = new Class[]{Point.class, String.class, String.class,String.class};
			shpOperator.copyShape(desPath, fields, types);
			System.out.println("shp生成完成，共耗时"+(System.currentTimeMillis() - start)+"ms");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}