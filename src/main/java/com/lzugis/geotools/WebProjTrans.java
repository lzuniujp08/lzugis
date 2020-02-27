package com.lzugis.geotools;

import com.lzugis.geotools.utils.ProjTransform;
import com.vividsolutions.jts.geom.*;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 实现wgs84，bd09，gcj02三者坐标间的转换
 */
public class WebProjTrans {
    private ProjTransform proj = new ProjTransform();

    public void transformShp(String inputShp, String outputShp, String inCrs, String outCrs){
        try {
            ShapefileDataStore shapeDS = (ShapefileDataStore) new ShapefileDataStoreFactory().createDataStore(new File(inputShp).toURI().toURL());
            SimpleFeatureSource fs = shapeDS.getFeatureSource(shapeDS.getTypeNames()[0]);
            SimpleFeatureIterator it = fs.getFeatures().features();

            FileDataStoreFactorySpi factory = new ShapefileDataStoreFactory();

            Map<String, Serializable> params = new HashMap<String, Serializable>();
            params.put(ShapefileDataStoreFactory.URLP.key, new File(outputShp).toURI().toURL());
            ShapefileDataStore ds = (ShapefileDataStore) factory.createNewDataStore(params);

            CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
            ds.createSchema(SimpleFeatureTypeBuilder.retype(fs.getSchema(), crs));

            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);

            while (it.hasNext()){
                SimpleFeature f = it.next();
                Geometry inGeom = (Geometry)f.getAttribute("the_geom");
                SimpleFeature fNew = writer.next();
                fNew.setAttributes(f.getAttributes());
                Geometry outGeom = projGeometry(inGeom, inCrs, outCrs);
                fNew.setAttribute("the_geom", outGeom);
            }
            writer.write();
            writer.close();
            ds.dispose();
            shapeDS.dispose();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 点坐标转换
     * @param inCoord
     * @param inCrs
     * @param outCrs
     * @return
     */
    public double[] projPoint(Coordinate inCoord, String inCrs, String outCrs){
        double[] lonlat = new double[2];
        String projStr = inCrs+","+outCrs;
        switch (projStr){
            case "wgs84,bd09":{
                lonlat = proj.wgs84tobd09(inCoord.x, inCoord.y);
                break;
            }
            case "wgs84,gcj02":{
                lonlat = proj.wgs84togcj02(inCoord.x, inCoord.y);
                break;
            }
            case "bd09,wgs84":{
                lonlat = proj.bd09towgs84(inCoord.x, inCoord.y);
                break;
            }
            case "gcj02,wgs84":{
                lonlat = proj.gcj02towgs84(inCoord.x, inCoord.y);
                break;
            }
            case "gcj02,bd09":{
                lonlat = proj.gcj02tobd09(inCoord.x, inCoord.y);
                break;
            }
            case "bd09,gcj02":{
                lonlat = proj.bd09togcj02(inCoord.x, inCoord.y);
                break;
            }
        }
        return lonlat;
    }

    /**
     * 获取转换后的点集合
     * @param geom
     * @param inCrs
     * @param outCrs
     * @return
     */
    public Coordinate[] getProjCoords(Geometry geom, String inCrs, String outCrs){
        Coordinate[] inCoords = geom.getCoordinates();
        Coordinate[] outCoords = new Coordinate[inCoords.length];
        for(int i=0;i<inCoords.length;i++){
            Coordinate inCoord = inCoords[i];
            double[] lonlat = projPoint(inCoord, inCrs, outCrs);
            Coordinate outCoord = new Coordinate(lonlat[0], lonlat[1]);
            String content = lonlat[0]+","+lonlat[1]+";"+inCoord.x+","+inCoord.y;
            outCoords[i] = outCoord;
        }
        return  outCoords;
    }

    /**
     * 单个Geometry做坐标转换
     * @param inGeom
     * @param inCrs
     * @param outCrs
     * @return
     */
    public Geometry projGeometry(Geometry inGeom, String inCrs, String outCrs){
        int geomNum = inGeom.getNumGeometries();
        Geometry[] geoms = new Geometry[geomNum];
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( null );
        for(int i=0;i<geomNum;i++){
            Geometry _inGeom = inGeom.getGeometryN(i),
                    _outGeom = null;
            Coordinate[] _outCoords = getProjCoords(_inGeom, inCrs, outCrs);
            String _geomType = _inGeom.getGeometryType().toLowerCase();
            switch (_geomType){
                case "point":{
                    _outGeom = geometryFactory.createPoint(_outCoords[0]);
                    break;
                }
                case "linestring":{
                    _outGeom = geometryFactory.createLineString(_outCoords);
                    break;
                }
                case "polygon":{
                    _outGeom = geometryFactory.createPolygon(_outCoords);
                    break;
                }
            }
            geoms[i] = _outGeom;
        }
        String geomType = inGeom.getGeometryType().toLowerCase();
        if(geomType.indexOf("multi")>0){//复杂图形
            Geometry outGeom = null;
            if(geomType.indexOf("linestring")>0){//复杂线
                outGeom = geometryFactory.createMultiLineString((LineString[]) geoms);
            }else if(geomType.indexOf("polygon")>0){//复杂面
                outGeom = geometryFactory.createMultiPolygon((Polygon[]) geoms);
            }else{//复杂点
                outGeom = geometryFactory.createMultiPoint((Point[]) geoms);
            }
            return outGeom;
        }else{
            return geoms[0];
        }
    }

    public static void main(String[] args){
        WebProjTrans webProj = new WebProjTrans();
        long start = System.currentTimeMillis();
        Coordinate inCoord = new Coordinate(124.97440000000003, 37.19788846500339);
        double[] lonlat = webProj.projPoint(inCoord, "gcj02", "bd09");
        System.out.println(lonlat[0]);
        System.out.println(lonlat[1]);
        System.out.println("共耗时"+(System.currentTimeMillis() - start)+"ms");
    }
}
