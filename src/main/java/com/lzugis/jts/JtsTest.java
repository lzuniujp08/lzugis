package com.lzugis.jts;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.osgeo.proj4j.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class JtsTest {
  static GeometryFactory factory = new GeometryFactory();
  static WKTWriter wktWriter = new WKTWriter();
  static WKTReader wktReader = new WKTReader();

  static Coordinate projectionTrans(Coordinate coord,String srcProj, String dstProj) {
    ProjCoordinate srcCoord = new ProjCoordinate(coord.getX(), coord.getY());
    ProjCoordinate dstCoord = new ProjCoordinate();
    CRSFactory factory = new CRSFactory();
    CoordinateReferenceSystem srcCrs = factory.createFromName(srcProj);
    CoordinateReferenceSystem dstCrs = factory.createFromName(dstProj);
    BasicCoordinateTransform transform = new BasicCoordinateTransform(srcCrs, dstCrs);
    transform.transform(srcCoord, dstCoord);
    return new Coordinate(dstCoord.x, dstCoord.y);
  }

  static Geometry projectTrans(Geometry srcGeometry) {
    Geometry dstGeometry = null;

    Coordinate[] coordsSrc = srcGeometry.getCoordinates();
    Coordinate[] coordsDst = new Coordinate[coordsSrc.length];
    for(int i=0;i<coordsSrc.length;i++) {
      coordsDst[i] = projectionTrans(coordsSrc[i]);
    }
    String type = srcGeometry.getGeometryType().toLowerCase();
    switch (type){
      case "linestring": {
        dstGeometry = factory.createLineString(coordsDst);
        break;
      }
      case "polygon": {
        dstGeometry = factory.createPolygon(coordsDst);
        break;
      }
      default: {
        dstGeometry = factory.createPoint(coordsDst[0]);
        break;
      }
    }

    return dstGeometry;
  }

  static Geometry projectTrans(Geometry srcGeometry,String srcProj, String dstProj) {
    Geometry dstGeometry = null;

    Coordinate[] coordsSrc = srcGeometry.getCoordinates();
    Coordinate[] coordsDst = new Coordinate[coordsSrc.length];
    for(int i=0;i<coordsSrc.length;i++) {
      coordsDst[i] = projectionTrans(coordsSrc[i], srcProj, dstProj);
    }
    String type = srcGeometry.getGeometryType().toLowerCase();
    switch (type){
      case "linestring": {
        dstGeometry = factory.createLineString(coordsDst);
        break;
      }
      case "polygon": {
        dstGeometry = factory.createPolygon(coordsDst);
        break;
      }
      default: {
        dstGeometry = factory.createPoint(coordsDst[0]);
        break;
      }
    }

    return dstGeometry;
  }

  static Coordinate projectionTrans(Coordinate coord) {
    ProjCoordinate srcCoord = new ProjCoordinate(coord.getX(), coord.getY());
    ProjCoordinate dstCoord = new ProjCoordinate();
    CRSFactory factory = new CRSFactory();
    CoordinateReferenceSystem srcCrs = factory.createFromName("EPSG:4326");
    CoordinateReferenceSystem dstCrs = factory.createFromName("EPSG:3857");
    BasicCoordinateTransform transform = new BasicCoordinateTransform(srcCrs, dstCrs);
    transform.transform(srcCoord, dstCoord);
    return new Coordinate(dstCoord.x, dstCoord.y);
  }

  public static void main(String[] args) throws Exception{
    Geometry p = wktReader.read("POINT(100 38.99)");
    Coordinate coord4326 = new Coordinate(100, 40);
    Coordinate coord3857 = projectionTrans(coord4326);
    Point point = factory.createPoint(coord3857);
    Geometry buffer3857 = point.buffer(10000);
    Geometry buffer4326 = projectTrans(buffer3857, "EPSG:3857", "EPSG:4326");
    System.out.println(buffer4326.intersects(p));
    System.out.println(wktWriter.write(buffer3857));
    System.out.println(wktWriter.write(buffer4326));

//    Coordinate coordinate = new Coordinate(100, 29);
//    GeometryFactory factory = new GeometryFactory();
//    WKTReader wktReader = new WKTReader(factory);
//    File file = new File("D:\\lzugis19\\code\\lzugis\\src\\wkt.txt");
//    String encoding="GBK";
//    if(file.isFile() && file.exists()) { //判断文件是否存在
//      InputStreamReader read = new InputStreamReader(
//        new FileInputStream(file), encoding);//考虑到编码格式
//      BufferedReader reader = new BufferedReader(read);
//      Geometry geometry = wktReader.read(reader);
//      Point pt = factory.createPoint(coordinate);
//      System.out.println(pt.intersects(geometry));
//    }
  }
}
