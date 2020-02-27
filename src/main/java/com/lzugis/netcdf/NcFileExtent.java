package com.lzugis.netcdf;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import ucar.ma2.Array;
import ucar.ma2.IndexIterator;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

import java.io.File;
import java.text.NumberFormat;

/**
 * @Author pnunu@git
 * @Date 2018/3/12 15:47
 */
public class NcFileExtent {

    public static double[] getExtent(String file, String outSR) throws Exception {
        float[] lat = getMaxMin(file, "lat");
        float[] lon = getMaxMin(file, "lon");
        if (lat == null)
            lat = getMaxMin(file, "latitude");
        if (lon == null)
            lon = getMaxMin(file, "longitude");
        Geometry geomMin = new GeometryFactory().createPoint(new Coordinate(lon[0], lat[0])),
                geomMax = new GeometryFactory().createPoint(new Coordinate(lon[1], lat[1]));

        Geometry geomTransMin = projTransform(geomMin, outSR),
                geomTransMax = projTransform(geomMax, outSR);

        if(outSR.equals("EPSG:4326")){
            return new double[]{
                    geomTransMin.getCoordinate().y,
                    geomTransMin.getCoordinate().x,
                    geomTransMax.getCoordinate().y,
                    geomTransMax.getCoordinate().x};
        }else {
            return new double[]{
                    geomTransMin.getCoordinate().x,
                    geomTransMin.getCoordinate().y,
                    geomTransMax.getCoordinate().x,
                    geomTransMax.getCoordinate().y};
        }
    }

    /** 投影转换 */
    private static Geometry projTransform(Geometry geom, String proj) throws Exception{
        CoordinateReferenceSystem crsTarget = CRS.decode(proj);
        MathTransform transform = CRS.findMathTransform(DefaultGeographicCRS.WGS84, crsTarget);
        return JTS.transform(geom, transform);
    }

    private static float[] getMaxMin(String file, String varStr) throws Exception {
        NetcdfFile netcdfFile = NetcdfFile.open(file);
        Variable var = netcdfFile.findVariable(varStr);
        if (var == null)
            return null;
        Array data = var.read().reduce();
        IndexIterator ii = data.getIndexIterator();
        float max = -99999;
        float min = 99999;
        while (ii.hasNext()) {
            float value = ii.getFloatNext();
            if (min > value)
                min = value;
            if (max < value)
                max = value;
        }
        netcdfFile.close();
        return new float[]{min, max};
    }

    public static void main(String[] args) {
        try {
            long start = System.currentTimeMillis();
//            String path = "F:\\实况";
//            File[] files = new File(path).listFiles();
//            for (int i = 0; i < files.length; i++) {
//                String file = files[i].getAbsolutePath();
//                NumberFormat nf = NumberFormat.getInstance();
//                nf.setGroupingUsed(false);
//                nf.setMaximumFractionDigits(20);
//
//                double[] extent = getExtent(file, "EPSG:3857");
//                System.out.print(file + "; ");
//                for (double n : extent)
//                    System.out.print(nf.format(n) + ", ");
//                System.out.println();
//            }
            String file = "C:\\Users\\lzuni\\Desktop\\bjdw_yb_znwg3h5km_utc8_201908082000_201908182000.nc";
            NumberFormat nf = NumberFormat.getInstance();
            nf.setGroupingUsed(false);
            nf.setMaximumFractionDigits(20);

            double[] extent = getExtent(file, "EPSG:3857");
            for (double n : extent)
                System.out.print(nf.format(n) + ", ");
            System.out.println();
            System.out.println("MS: " + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
