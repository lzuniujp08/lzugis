package com.lzugis.netcdf.util;

import com.lzugis.CommonMethod;
import com.lzugis.netcdf.model.GridPoint;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.apache.poi.util.SystemOutLogger;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import scala.Int;
import ucar.ma2.ArrayDouble;
import ucar.ma2.ArrayFloat;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

import java.io.IOException;
import java.util.*;

public class NetcdfUtil {
    private NetcdfFile ncFile;
    private Map<String, GridPoint> pointsData = new HashMap();

    public NetcdfUtil(String ncPath) throws IOException{
        openNcFile(ncPath);
    }

    /**
     * 打开NC文件
     * @param ncPath
     * @throws IOException
     */
    public void openNcFile(String ncPath) throws IOException{
        ncFile = NetcdfFile.open(ncPath);
        getGeoData();
    }

    /**
     * 获取NC的变量
     * @param type
     * @return
     */
    public List getVariables(String type){
        if("string".equals(type)){
            List<Variable> list = getVariables("");
            List result = new ArrayList();
            for(int i=0;i<list.size();i++){
                result.add(list.get(i).getName());
            }
            return result;
        }else{
            return ncFile.getVariables();
        }
    }

    /**
     * 获取NC经纬度的维度
     * @return
     */
    public int getGeoDimension(){
        Variable geoV = ncFile.findVariable("lat");
        if (geoV == null)
            geoV = ncFile.findVariable("latitude");//longitude
        return geoV.getDimensions().size();
    }

    /**
     * 获取NC文件的行列数
     * @return lat， lon
     */
    public int[] getNcSize(){
        Variable latV = ncFile.findVariable("lat");
        if (latV == null)
            latV = ncFile.findVariable("latitude");//longitude
        int geoD = getGeoDimension();
        switch (geoD){
            case 2:{
                int lat = latV.getDimension(0).getLength(),
                        lon = latV.getDimension(1).getLength();
                return new int[]{lat, lon};
            }
            default:{
                Variable lonV = ncFile.findVariable("lon");
                if (lonV == null)
                    lonV = ncFile.findVariable("longitude");//longitude
                int lat = latV.getDimension(0).getLength(),
                        lon = lonV.getDimension(0).getLength();
                return new int[]{lat, lon};
            }
        }
    }
    /**
     * 获取经纬度信息
     * @return
     * @throws IOException
     */
    private void getGeoData() throws IOException{
        int geoDim = getGeoDimension();
        int[] ncSize = getNcSize();

        Variable latV = ncFile.findVariable("lat");
        if (latV == null)
            latV = ncFile.findVariable("latitude");//longitude
        Variable lonV = ncFile.findVariable("lon");
        if (lonV == null)
            lonV = ncFile.findVariable("longitude");//longitude
        switch (geoDim){
            case 2:{
                ArrayFloat.D2 arrayLat = (ArrayFloat.D2) latV.read();
                ArrayFloat.D2 arrayLon = (ArrayFloat.D2) lonV.read();
                for (int i = 0; i < ncSize[0]; i++) {//lat
                    for (int j = 0; j < ncSize[1]; j++) {//lon
                        float fLat = arrayLat.get(i, j);
                        float fLon = arrayLon.get(i, j);
                        GridPoint point = new GridPoint(j, i, fLat, fLon);
                        pointsData.put(i + "," + j, point);
                    }
                }
                break;
            }
            case 1:{
                ArrayDouble.D1 arrayLat = (ArrayDouble.D1) latV.read();
                ArrayDouble.D1 arrayLon = (ArrayDouble.D1) lonV.read();
                for (int i = 0; i < ncSize[0]; i++) {//lat
                    double fLat = arrayLat.get(i);
                    for (int j = 0; j < ncSize[1]; j++) {//lon
                        double fLon = arrayLon.get(j);
                        GridPoint point = new GridPoint(j, i, fLat, fLon);
                        pointsData.put(i + "," + j, point);
                    }
                    System.out.println(i);
                }
                break;
            }
        }
    }

    /**
     * 根据经纬度获取下标
     * @param x
     * @param y
     * @return
     */
    public double[] getLatlonByIndex(int x, int y) {
        GridPoint pointData = (GridPoint)pointsData.get(x + "," + y);
        return new double[]{pointData.getLat(), pointData.getLon()};
    }

    /**
     * 根据下标获取经纬度
     * @param lat
     * @param lon
     * @return
     */
    public int[] getIndexByLatlon(float lat, float lon){
        int geoDim = getGeoDimension();
        switch (geoDim){
            case 2:{
                List<GridPoint> points = getNearPoints2(lat, lon);
                return new int[]{points.get(0).getNy(), points.get(0).getNx()};
            }
            default:{
                return getNearPoints1(lat, lon);
            }
        }
    }

    public double[] getNcBound(String outSR){
        CommonMethod cm = new CommonMethod();
        int[] size = getNcSize();
        double[] min = getLatlonByIndex(0, 0),
                max = getLatlonByIndex(size[0]-1, size[1]-1);
        Geometry geomMin = new GeometryFactory().createPoint(new Coordinate(min[1], min[0])),
                geomMax = new GeometryFactory().createPoint(new Coordinate(max[1], max[0]));

        Geometry geomTransMin = projTransform(geomMin, outSR),
                geomTransMax = projTransform(geomMax, outSR);

        return new double[]{
                geomTransMin.getCoordinate().x,
                geomTransMin.getCoordinate().y,
                geomTransMax.getCoordinate().x,
                geomTransMax.getCoordinate().y
        };
    }

    public Geometry projTransform(Geometry geom, String proj){
        try{
            CoordinateReferenceSystem crsTarget = CRS.decode(proj);
            // 投影转换
            MathTransform transform = CRS.findMathTransform(DefaultGeographicCRS.WGS84, crsTarget);
            return JTS.transform(geom, transform);
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 关闭NC文件
     * @throws IOException
     */
    public void closeNcFile() throws IOException{
        ncFile.close();
    }

    /**
     * 获取二维最近的点
     * @param lat
     * @param lon
     * @return
     */
    private List<GridPoint> getNearPoints2(float lat, float lon) {
        float latNear1 = lat - 0.2f, latNear2 = lat + 0.2f;
        float lonNear1 = lon - 0.2f, lonNear2 = lon + 0.2f;
        // 找出最近的 N 多个点，并计算出各自的距离 dis
        List<GridPoint> tmpList = new ArrayList<>();
        for (GridPoint point : pointsData.values()) {
            if (latNear1 <= point.getLat() && point.getLat() <= latNear2 && lonNear1 <= point.getLon()
                    && point.getLon() <= lonNear2) {
                point.setDis((float) (Math.sqrt(Math.pow(point.getLat() - lat, 2) + Math.pow(point.getLon() - lon, 2))));
                tmpList.add(point);
            }
        }
        if (tmpList.size() < 4)
            return null;
        tmpList.sort(Comparator.comparing(GridPoint::getDis));
        List<GridPoint> resList = tmpList.subList(0, 4);
        int num = 1;
        for (GridPoint p : resList) {
            p.setNum(num++);
        }
        return resList;
    }

    /**
     * 获取1维点
     * @param lat
     * @param lon
     * @return
     */
    private int[] getNearPoints1(float lat, float lon) {
        GridPoint point0 = pointsData.get("0,0"),
                point1 = pointsData.get("1,1");
        double latMin = point0.getLat(),
                lonMin = point0.getLon(),
                latSec = point1.getLat(),
                lonSec = point1.getLon();
        double lonV = lonSec - lonMin,
                latV = latSec - latMin;
        int lonI = (int) Math.round((lon - lonMin)/lonV),
                latI = (int) Math.round((lat - latMin)/latV);
        return new int[]{latI, lonI};
    }

    /**
     * 根据经纬度获取值
     * @param varStr
     * @param lon
     * @param lat
     * @return
     * @throws IOException
     */
    public float getInfoByLonlat(String varStr, float lon, float lat) throws IOException{
        Variable valV = ncFile.findVariable(varStr);
        int[] index = getIndexByLatlon(lat, lon);
        ArrayFloat.D3 arrayVal = (ArrayFloat.D3) valV.read();
        return arrayVal.get(0, index[0], index[1]);
    }

}
