package com.lzugis.geotools;

import com.lzugis.CommonMethod;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.*;

public class ShpQuery {
	private static String rootPath = System.getProperty("user.dir");
	private SimpleFeatureSource featureSource = null;

	public void readShape(String shpfile){
		try {
			File file = new File(shpfile);
			ShapefileDataStore shpDataStore = null;

			shpDataStore = new ShapefileDataStore(file.toURL());
			//设置编码
			Charset charset = Charset.forName("GBK");
			shpDataStore.setCharset(charset);
			String typeName = shpDataStore.getTypeNames()[0];
			featureSource =  shpDataStore.getFeatureSource (typeName);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	public void queryPoints(double[][] points){
		CommonMethod cm = new CommonMethod();
		try {
			FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
			SimpleFeatureType schema = featureSource.getSchema();
			String geometryAttributeName = schema.getGeometryDescriptor().getLocalName();
			System.out.println(geometryAttributeName);
			for(int i=0;i<points.length;i++){
				double[] point = points[i];
				Geometry refGeo = new GeometryFactory().createPoint(new Coordinate(point[0], point[1]));
				Filter filterSpat = getGeoFilter(ff, geometryAttributeName, refGeo);

				SimpleFeatureCollection result = featureSource.getFeatures(filterSpat);
				SimpleFeatureIterator itertor = result.features();
				while (itertor.hasNext()) {
					SimpleFeature feature = itertor.next();
					cm.append2File("d:/points.txt", feature.getAttribute("NAME").toString());
				}
				itertor.close();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public String queryPoints(double lat, double lon){
		String out = "";
		try {
			FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
			SimpleFeatureType schema = featureSource.getSchema();
			String geometryAttributeName = schema.getGeometryDescriptor().getLocalName();

			Geometry refGeo = new GeometryFactory().createPoint(new Coordinate(lon, lat));
			Filter filterSpat = getGeoFilter(ff, geometryAttributeName, refGeo);

			SimpleFeatureCollection result = featureSource.getFeatures(filterSpat);
			SimpleFeatureIterator itertor = result.features();
			if(itertor.hasNext()){
				SimpleFeature feature = itertor.next();
				out = feature.getAttribute("NAME").toString() + "," + feature.getAttribute("DIST_CODE").toString();
			}
			itertor.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		return out;
	}
	/**
	 * 读取shape文件
	 * @param shpfile
	 * @return
	 */
	public void queryShape(String shpfile, Map paras){
		try{
            /*属性查询*/
			//filterStr形式 如  name='武汉大学' or code like 'tt123%'
			String filterStr = "NAME='北京市'";
			Filter filterAttr = CQL.toFilter(filterStr);

			/*空间查询*/
			FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
			SimpleFeatureType schema = featureSource.getSchema();
			String geometryAttributeName = schema.getGeometryDescriptor().getLocalName();
			System.out.println(geometryAttributeName);
			Geometry refGeo = new GeometryFactory().createPoint(new Coordinate(116.176, 39.978));
			Filter filterSpat= getGeoFilter(ff,geometryAttributeName, refGeo);	//上面的方法

			/*属性和空间联合查询*/
			List<Filter> match = new ArrayList<Filter>();
			match.add(filterAttr);
			match.add(filterSpat);
			Filter filter = ff.and(match);

			SimpleFeatureCollection result = featureSource.getFeatures(filter);
            SimpleFeatureIterator itertor = result.features();
            while (itertor.hasNext()){
                SimpleFeature feature = itertor.next();
                System.out.println(feature.getAttribute("NAME"));
            }
            itertor.close();
        }
        catch(Exception e){
        	e.printStackTrace();
        }
	}

	public Filter getGeoFilter(FilterFactory2 ff,
			String geometryAttributeName, Geometry refGeo) {
		return ff.intersects(ff.property("the_geom"), ff
				.literal(refGeo));
	}
	/**
	 * 工具类测试方法
	 * @param args
	 */
	public static void main(String[] args) {
		ShpQuery shpQuery =new ShpQuery();
        try{
    		long start = System.currentTimeMillis();
			String shpPath = rootPath + "/data/shp/bou2_4p.shp";
			shpQuery.readShape(shpPath);
			double[][] points = new double[][]{
					{122.53233, 52.96887},
					{124.70580, 52.34033}
			};
			shpQuery.queryPoints(points);
    		System.out.println("共耗时"+(System.currentTimeMillis() - start)+"ms");
        }
        catch(Exception e){
        	e.printStackTrace();
        }
	}
}
