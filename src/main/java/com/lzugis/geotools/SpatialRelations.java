package com.lzugis.geotools;

import com.vividsolutions.jts.geom.*;
import org.geotools.geometry.jts.JTSFactoryFinder;

import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

public class SpatialRelations {
	/**
	 * 空间关系判断
	 * Disjoint、Intersects、Touches、Crosses、Within、Contains、Overlaps
	 * @param geom1
	 * @param geom2
	 * @param relation
	 * @return
	 */
	public boolean getGeomsRelation(Geometry geom1, Geometry geom2, String relation){
		boolean r = false;
		switch(relation){
			case "Intersects":
				r = geom1.intersects(geom2);
			case "Disjoint":
				r = geom1.disjoint(geom2);
			case "Touches":
				r = geom1.touches(geom2);
			case "Crosses":
				r = geom1.crosses(geom2);
			case "Within":
				r = geom1.within(geom2);
			case "Contains":
				r = geom1.contains(geom2);
			case "Overlaps":
				r = geom1.overlaps(geom2);
		}
		return r;
	}
	/**
	 * 计算空间关系
	 * Intersection、Union、Difference、SymDifference
	 * @param geom1
	 * @param geom2
	 * @param relation
	 * @return
	 */
	public Geometry calGeomsRelation(Geometry geom1, Geometry geom2, String relation){
		Geometry geom = null;
		switch(relation){
			case "Intersects":
				geom = geom1.intersection(geom2);
			case "Union":
				geom = geom1.union(geom2);
			case "Difference":
				geom = geom1.difference(geom2);
			case "SymDifference":
				geom = geom1.symDifference(geom2);
		}
		return geom;
	}
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
		SpatialRelations geoR = new SpatialRelations();
		String wktPoint = "POINT(116.240041 39.8592)";
		String wktLine = "MULTILINESTRING((116.240466 39.859252,116.240041 39.8592,116.236872 39.85798))";
		String wktPolygon = "POLYGON((100.02715479879 32.168082192159,102.76873121104 37.194305614622,107.0334056301 34.909658604412,105.96723702534 30.949603786713,100.02715479879 32.168082192159))";
		String wktPolygon1 = "POLYGON((96.219409781775 32.777321394882,96.219409781775 40.240501628236,104.82491352023001 40.240501628236,104.82491352023001 32.777321394882,96.219409781775 32.777321394882))";

		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( null );
		WKTReader reader = new WKTReader( geometryFactory );
		WKTWriter writer = new WKTWriter();

		try{
			Geometry point = (Geometry) reader.read(wktPoint);
			Geometry line = (Geometry) reader.read(wktLine);
			Geometry polygon = (Geometry) reader.read(wktPolygon);
			Geometry polygon1 = (Geometry) reader.read(wktPolygon1);

			Geometry geomBuffer = geoR.calBuffer(point, 0.0001);
			System.out.println(writer.write(geomBuffer));
			System.out.println(point.intersects(line));


			String wkt = "POLYGON((116.62101162299277 41.79671719380045,128.04679287299277 41.23734749350149,125.14640224799275 15.528862374421749,105.85441006049275 15.698157072452446,105.15128506049275 23.07064756331151,115.56632412299275 23.393702670825633,115.56632412299275 24.398145812403115,117.54386318549275 24.358118609657467,117.93937099799275 33.4965251558623,116.70890224799275 33.97162028732113,116.62101162299277 41.79671719380045))";
			String wktPoint22 = "POINT(116.240041 39.8592)";
			Geometry polygon22 = (Geometry) reader.read(wkt);
			Geometry point22 = (Geometry) reader.read(wktPoint22);
			if(polygon22.contains(point22)) {

			}
//			//空间关系
//			System.out.println("Within:"+geoR.getGeomsRelation(point, polygon, "Within"));
//
//			//缓冲区计算
//			Geometry geomBuffer = geoR.calBuffer(point, 0.1);
//			System.out.println("Buffer Result："+writer.write(geomBuffer));
//
//			//空间计算
//			Geometry geomUnion = geoR.calGeomsRelation(polygon, polygon1, "Union");
//			System.out.println("Union Result："+writer.write(geomUnion));
		}
		catch(Exception e){
			e.printStackTrace();
		}

	}
}
