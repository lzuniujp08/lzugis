package com.lzugis.geotools;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.triangulate.VoronoiDiagramBuilder;

public class TsdbxTest {
	static TsdbxTest tsdbx = new TsdbxTest();
	private static String rootPath = System.getProperty("user.dir");


	public void voronoiTest(){
	    VoronoiDiagramBuilder voronoiDiagramBuilder = new VoronoiDiagramBuilder();
	    List<Coordinate> coords = new ArrayList<Coordinate>();
	    Envelope clipEnvelpoe = new Envelope();
	    int xmin = 0, xmax=180;
        int ymin = 0, ymax=90;
        Random random = new Random();
        List<Geometry> geomsPoints = new ArrayList<Geometry>();
	    for(int i=0;i<100;i++){
	    	int x = random.nextInt(xmax)%(xmax-xmin+1) + xmin,
	            y = random.nextInt(ymax)%(ymax-ymin+1) + ymin;
	        Coordinate coord = new Coordinate(x,y,i);
	        coords.add(coord);
	        clipEnvelpoe.expandToInclude(coord);
	        geomsPoints.add(new GeometryFactory().createPoint(coord));
	    }
		String pointpath = rootPath + "/out/tsdbxpt.shp";
		tsdbx.writeShape(pointpath,"Point", geomsPoints);

	    voronoiDiagramBuilder.setSites(coords);
	    voronoiDiagramBuilder.setClipEnvelope(clipEnvelpoe);
	    Geometry geom = voronoiDiagramBuilder.getDiagram(JTSFactoryFinder.getGeometryFactory());
	    List<Geometry> geoms = new ArrayList<Geometry>();
	    for(int i=0;i<geom.getNumGeometries();i++){
	        geoms.add(geom.getGeometryN(i));
	    }

		String polygonpath = rootPath + "/out/tsdbx.shp";
		tsdbx.writeShape(polygonpath,"Polygon", geoms);
	}
	/**
	 * 
	 * @param filepath
	 * @param geoType
	 * @param geoms
	 */
	public void writeShape(String filepath, String geoType, List<Geometry> geoms) {
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
			if(geoType=="Point"){
				tb.add("the_geom", Point.class);
			}
			else{
				tb.add("the_geom", Polygon.class);
			}
			
			ds.createSchema(tb.buildFeatureType());
			//设置编码
            Charset charset = Charset.forName("GBK");
            ds.setCharset(charset);
			//设置Writer
			FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);
			for(int i=0,len=geoms.size();i<len;i++){
				//写下一条
				SimpleFeature feature = writer.next();
				Geometry geom = geoms.get(i);
				feature.setAttribute("the_geom", geom);
			}
			writer.write();
			writer.close();
			ds.dispose();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		long start = System.currentTimeMillis();
		tsdbx.voronoiTest();
		System.out.println("共耗时"+(System.currentTimeMillis() - start)+"ms");
	}
}
