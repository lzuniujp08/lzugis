package com.lzugis.idw;

/**
 * @Title:
 * @Description:反距离加权插值法
 * @Copyright:Copyright (c) 2011
 * @Company: micromulti
 * @File name: IDWUtil.java
 * @Author: 赵敬和
 * @Create DateTime: Mar 7, 2012 1:28:07 PM
 * @Version: 1.0.0
 * @Others:
 */

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 其中x,y,z为已知坐标及其函数值,X,Y为要插值的坐标 x,y,z,X,Y最高为二维的，不可为三维 不考虑x，y中出现重复坐标的情况
 */
public class IDWUtil {
    private static String rootPath = System.getProperty("user.dir");

    public double[][] doIDW(double[] x, double[] y, double[] z, double[][] X,
                            double[][] Y) {

        int xSize = x.length;
        int XRows = X.length;
        int XCols = X[0].length;
        int zSize = z.length;
//        System.out.println("xSize * XRows * XCols " + xSize * XRows * XCols + "  xSize " + xSize + " XRows " + XRows + " XCols " + XCols);
        double[][] DisArray = new double[XRows][xSize * XCols];

        // 生成距离矩阵r(m0*m1*n1,n0)
        for (int i = 0; i < XRows; i++) {
            int m = 0;
            for (int j = 0; j < XCols; j++) {
                for (int k = 0; k < xSize; k++) {
                    DisArray[i][m] = Math.sqrt((X[i][j] - x[k])
                            * (X[i][j] - x[k]) + (Y[i][j] - y[k])
                            * (Y[i][j] - y[k]));
                    m = m + 1;
                }
            }
        }


        double[][] Z = new double[XRows][XCols];//定义插值完后数值
        // 定义插值函数
        for (int i = 0; i < XRows; i++) {
            int n = 0;
            int rr = 0;
            for (int j = 0; j < XCols; j++) {
                int index = 0;
                double sum = 0.0;
                double sum2 = 0.0;

                for (int k = 0; k < zSize; k++) {
                    sum = z[k] / DisArray[i][n] + sum;
                    sum2 = 1 / DisArray[i][n] + sum2;
                    n = n + 1;
                }
                Z[i][j] = sum / sum2;
                for (int k1 = 0; k1 < zSize; k1++) {
                    if (DisArray[i][rr] == 0) {
                        index = rr % zSize;
                        Z[i][j] = z[index];
                    }
                    rr = rr + 1;
                }
            }
        }
        return Z;
    }


    public static void main(String[] args) {
        IDWUtil idw = new IDWUtil();
        long start = System.currentTimeMillis();
        String csvpath = rootPath + "/data/xls/capital.txt";
        File csv = new File(csvpath);  // CSV文件路径
        BufferedReader br = null;
        try {
            List lx = new ArrayList(), ly = new ArrayList(), lz = new ArrayList();
            br = new BufferedReader(new FileReader(csv));
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] lineData = line.split(",");
                lx.add(lineData[2]);
                ly.add(lineData[3]);
                lz.add(lineData[5]);
            }

            double[] x = new double[lx.size()],
                    y = new double[lx.size()],
                    z = new double[lx.size()];
            for (int i = 0; i < lx.size(); i++) {
                x[i] = Double.parseDouble(lx.get(i).toString());
                y[i] = Double.parseDouble(ly.get(i).toString());
                z[i] = Double.parseDouble(lz.get(i).toString());
            }
            double[][] X = new double[][]{{73.4510046356223, 134.976797646506}},
                    Y = new double[][]{{18.1632471876417, 53.5319431522236}};
            double[][] Z = idw.doIDW(x, y, z, X, Y);
            System.out.println(Z);
            System.out.println("共耗时" + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
