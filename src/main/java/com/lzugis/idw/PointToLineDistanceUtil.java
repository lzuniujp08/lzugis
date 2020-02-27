package com.lzugis.idw;

/**
 * @Title:
 * @Description: 点到直线的距离
 * @Copyright:Copyright (c) 2011
 * @Company: micromulti
 * @File name: PointToLine.java
 * @Author: 赵敬和
 * @Create DateTime: Nov 10, 2011 5:38:47 PM
 * @Version: 1.0.0
 * @Others:
 */
public class PointToLineDistanceUtil {
    /**
     * @param (x1,y1),(x2,y2)为所求直线，
     * @param 定点(x0,y0)
     * @return 返回点(x0, y0)到直线(x1, y1), (x2, y2)的距离
     */
    public double pointToLineDistance(double x1, double y1, double x2, double y2, double x0, double y0) {

        double distance = 0;

        double a, b, c;

        a = lineDistance(x1, y1, x2, y2);// 线段的长度

        b = lineDistance(x1, y1, x0, y0);// (x1,y1)到点的距离

        c = lineDistance(x2, y2, x0, y0);// (x2,y2)到点的距离

        if (c + b == a) {// 点在线段上

            distance = 0;

            return distance;

        }

        if (a <= 0.000001) {// 不是线段，是一个点

            distance = b;

            return distance;

        }

        if (c * c >= a * a + b * b) { // 组成直角三角形或钝角三角形，(x1,y1)为直角或钝角

            distance = b;

            return distance;

        }

        if (b * b >= a * a + c * c) {// 组成直角三角形或钝角三角形，(x2,y2)为直角或钝角

            distance = c;

            return distance;

        }
        // 组成锐角三角形，则求三角形的高
        if ((a + b > c) && (a + c > b) && (b + c > a)) {
            double p = (a + b + c) / 2;// 半周长

            double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// 海伦公式求面积

            distance = 2 * s / a;// 返回点到线的距离（利用三角形面积公式求高）


        }

        return distance;

    }

    // 计算两点之间的距离

    private double lineDistance(double x1, double y1, double x2, double y2) {

        double lineLength = 0;

        lineLength = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2)

                * (y1 - y2));

        return lineLength;

    }

}
