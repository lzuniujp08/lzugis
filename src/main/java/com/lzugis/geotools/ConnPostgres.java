//package com.lzugis.geotools;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.vividsolutions.jts.geom.MultiPolygon;
//import org.geotools.data.DataStore;
//import org.geotools.data.DataStoreFinder;
//import org.geotools.data.postgis.PostgisNGDataStoreFactory;
//import org.geotools.data.simple.SimpleFeatureCollection;
//import org.geotools.data.simple.SimpleFeatureSource;
//import org.geotools.factory.CommonFactoryFinder;
//import org.geotools.factory.GeoTools;
//import org.geotools.feature.FeatureIterator;
//import org.geotools.filter.text.cql2.CQL;
//import org.geotools.geometry.jts.ReferencedEnvelope;
//import org.geotools.referencing.CRS;
//import org.opengis.feature.simple.SimpleFeature;
//import org.opengis.feature.type.FeatureType;
//import org.opengis.filter.Filter;
//import org.opengis.filter.FilterFactory2;
//import org.opengis.referencing.crs.CoordinateReferenceSystem;
//
//public class ConnPostgres {
//	static DataStore pgDatastore;
//
//	public void conPostgres(){
//		try{
//			//数据库连接参数配置
//			Map<String, Object> params = new HashMap<String, Object>();
//			params.put( PostgisNGDataStoreFactory.DBTYPE.key, "postgis");
//			params.put( PostgisNGDataStoreFactory.HOST.key, "localhost");
//			params.put( PostgisNGDataStoreFactory.PORT.key, 5432);
//			//数据库名
//			params.put( PostgisNGDataStoreFactory.DATABASE.key, "lzugis");
//			//用户名和密码
//			params.put( PostgisNGDataStoreFactory.USER.key, "postgres");
//			params.put( PostgisNGDataStoreFactory.PASSWD.key, "root");
//			pgDatastore = DataStoreFinder.getDataStore(params);
//			if(pgDatastore!=null){
//				System.out.println("数据库连接成功");
//			}else{
//				System.out.println("数据库连接失败");
//			}
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
//	}
//
//	public void getLayerInfo(String layerName) throws Exception{
//		//图层名
//		SimpleFeatureSource featureSource =pgDatastore.getFeatureSource(layerName);
//
//		//属性过滤
////		Filter filter = CQL.toFilter("name like '%甘%' or name like '%北%'");
//
//		//空间过滤
//		FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
//		FeatureType schema = featureSource.getSchema();
//		String geometryPropertyName = schema.getGeometryDescriptor().getLocalName();
//		CoordinateReferenceSystem targetCRS =
//				schema.getGeometryDescriptor().getCoordinateReferenceSystem();
//		ReferencedEnvelope bbox = new ReferencedEnvelope(0, 0, 180, 90, targetCRS);
//		Filter filter = ff.bbox(ff.property(geometryPropertyName), bbox);
//
//		SimpleFeatureCollection result = featureSource.getFeatures(filter);
//		FeatureIterator<SimpleFeature> itertor = result.features();
//		while (itertor.hasNext()) {
//			SimpleFeature feature = itertor.next();
//			System.out.println(feature.getAttribute("name").toString());
//		}
//		itertor.close();
//	}
//
//	public void getAllLayers() throws IOException{
//		String[] typeName  =  pgDatastore.getTypeNames();
//		for(int i=0;i<typeName.length;i++){
//			System.out.println(typeName[i]);
//		}
//	}
//
//
//	public static void main(String[] args){
//		ConnPostgres conpg = new ConnPostgres();
//		conpg.conPostgres();
//		try{
////			conpg.getAllLayers();
//			conpg.getLayerInfo("capital");
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//	}
//}
