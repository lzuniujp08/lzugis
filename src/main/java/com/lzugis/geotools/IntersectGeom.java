package com.lzugis.geotools;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.triangulate.VoronoiDiagramBuilder;

public class IntersectGeom {
	static TsdbxTest tsdbx = new TsdbxTest();
	
	public void calIntersect(String shp1, String shp2, String outPoint, String outTsdbx){
		try{
			//创建shape文件对象
			File file = new File(outPoint);
			Map<String, Serializable> params = new HashMap<String, Serializable>();
			params.put( ShapefileDataStoreFactory.URLP.key, file.toURI().toURL() );
			ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);
			//定义图形信息和属性信息
			SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
			tb.setCRS(DefaultGeographicCRS.WGS84);
			tb.setName("shapefile");
			tb.add("the_geom", Point.class);
			ds.createSchema(tb.buildFeatureType());
			//设置编码
	        Charset charset = Charset.forName("GBK");
	        ds.setCharset(charset);
			//设置Writer
			FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);
			
			//构建泰森多边形
			VoronoiDiagramBuilder voronoiDiagramBuilder = new VoronoiDiagramBuilder();
			List<Coordinate> coords = new ArrayList<Coordinate>();
			Envelope clipEnvelpoe = new Envelope();
			
			//读取缓冲区结果
			File fileBuffer = new File(shp1);
    		ShapefileDataStore shpDataStoreBuffer = null;
    		shpDataStoreBuffer = new ShapefileDataStore(fileBuffer.toURL());
            //设置编码
    		shpDataStoreBuffer.setCharset(charset);
            String typeNameBuffer = shpDataStoreBuffer.getTypeNames()[0];
            SimpleFeatureSource featureSourceBuffer = null;
            featureSourceBuffer =  shpDataStoreBuffer.getFeatureSource (typeNameBuffer);
            SimpleFeatureCollection resultBuffer = featureSourceBuffer.getFeatures();
            SimpleFeatureIterator itertorBuffer = resultBuffer.features();
            
            //读取散点结果
			File filePoint = new File(shp2);
    		ShapefileDataStore shpDataStorePoint = null;
        	shpDataStorePoint = new ShapefileDataStore(filePoint.toURL());
            //设置编码
            shpDataStorePoint.setCharset(charset);
            String typeNamePoint = shpDataStorePoint.getTypeNames()[0];
            SimpleFeatureSource featureSourcePoint = null;
            featureSourcePoint =  shpDataStorePoint.getFeatureSource (typeNamePoint);
            SimpleFeatureCollection resultPoint = featureSourcePoint.getFeatures();
            int i = 0;
            while (itertorBuffer.hasNext())
            {
                SimpleFeature featureBuf = itertorBuffer.next();
                Geometry geomBuf = (Geometry)featureBuf.getAttribute("the_geom");
                SimpleFeatureIterator itertorPoint = resultPoint.features();
                while (itertorPoint.hasNext())
                {
                	SimpleFeature featurePt = itertorPoint.next();
                	Geometry geomPt = (Geometry) featurePt.getAttribute("the_geom");
                	if(geomBuf.intersects(geomPt)){
                		SimpleFeature feature = writer.next();
                		feature.setAttribute("the_geom", geomPt);
                		i++;
                		
                		Coordinate coord = geomPt.getCoordinate();
            	        coords.add(coord);
            	        clipEnvelpoe.expandToInclude(coord);
                	}
                }
                itertorPoint.close();
            }
            itertorBuffer.close();
            writer.write();
			writer.close();
			shpDataStoreBuffer.dispose();
			shpDataStorePoint.dispose();
			ds.dispose();
			
			voronoiDiagramBuilder.setSites(coords);
		    voronoiDiagramBuilder.setClipEnvelope(clipEnvelpoe);
		    Geometry geom = voronoiDiagramBuilder.getDiagram(JTSFactoryFinder.getGeometryFactory());
		    List<Geometry> geoms = new ArrayList<Geometry>();
		    for(int j=0;j<geom.getNumGeometries();j++){
		        geoms.add(geom.getGeometryN(j));
		    }
		    tsdbx.writeShape(outTsdbx,"Polygon", geoms);
			System.out.println("共得到"+i+"条数据");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		IntersectGeom intersect = new IntersectGeom();
		long start = System.currentTimeMillis();
		String bufPath = "C:\\Users\\admin\\Desktop\\zztl\\rail_buf.shp";
		String ptPath = "C:\\Users\\admin\\Desktop\\zztl\\zztl_point.shp";
		String outPoint = "C:\\Users\\admin\\Desktop\\zztl\\intc_point.shp";
		String outTsdbx = "C:\\Users\\admin\\Desktop\\zztl\\intc_tsdbx.shp";
		intersect.calIntersect(bufPath, ptPath, outPoint, outTsdbx);
		System.out.println("共耗时"+(System.currentTimeMillis() - start)+"ms");
	}
}
