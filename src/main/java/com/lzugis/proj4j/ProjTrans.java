package com.lzugis.proj4j;

import org.osgeo.proj4j.*;

public class ProjTrans {
  public double[] projectionTrans(String srcProj, String dstProj, double[] coord) {
    ProjCoordinate srcCoord = new ProjCoordinate(coord[0], coord[1]);
    ProjCoordinate dstCoord = new ProjCoordinate();

    CRSFactory factory = new CRSFactory();
    CoordinateReferenceSystem srcCrs = factory.createFromName(srcProj);
    CoordinateReferenceSystem dstCrs = factory.createFromName(dstProj);
    BasicCoordinateTransform transform = new BasicCoordinateTransform(srcCrs, dstCrs);
    transform.transform(srcCoord, dstCoord);

    return new double[]{dstCoord.x, dstCoord.y};
  }


  public double[] projectionTransByWkt(String srcProj, String dstProj, double[] coord) {
    ProjCoordinate srcCoord = new ProjCoordinate(coord[0], coord[1]);
    ProjCoordinate dstCoord = new ProjCoordinate();

    CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
    CRSFactory csFactory = new CRSFactory();
    CoordinateReferenceSystem srcCRS = csFactory.createFromParameters("src-proj", srcProj);
    CoordinateReferenceSystem dstCRS = csFactory.createFromParameters("dst-proj", dstProj);
    CoordinateTransform transform = ctFactory.createTransform(srcCRS, dstCRS);
    transform.transform(srcCoord, dstCoord);
    return new double[]{dstCoord.x, dstCoord.y};
  }

  public static void main(String[] args){
    double[] srcCoord = new double[]{113.475, 37.475};
    try {
      long start = System.currentTimeMillis();
      ProjTrans proj = new ProjTrans();
      String srcProj = "EPSG:4326",
        dstProj = "EPSG:3857";
      double[] dstCoord1 = proj.projectionTrans(srcProj, dstProj, srcCoord);
      System.out.println(dstCoord1[0] + ", " + dstCoord1[1]);
      // +proj=longlat +datum=WGS84 +no_defs
      String srcWkt = "+proj=longlat +datum=WGS84 +no_defs ",
        dstWkt = "+proj=lcc +lat_1=38 +lat_2=42 +lat_0=40 +lon_0=116.5 +x_0=400000 +y_0=400000 +ellps=WGS84 +datum=WGS84 +units=m +no_defs";
      double[] dstCoord2 = proj.projectionTransByWkt(srcWkt, dstWkt, srcCoord);
      System.out.println(dstCoord2[0] + ", " + dstCoord2[1]);

      System.out.println("坐标转换完成，共耗时"+(System.currentTimeMillis() - start)+"ms");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

