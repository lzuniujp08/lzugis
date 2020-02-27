//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package wContour;

import java.util.ArrayList;
import java.util.List;

import wContour.Global.LPolygon;
import wContour.Global.LegendPara;
import wContour.Global.PointD;

public class Legend {
    public Legend() {
    }

    public static List<LPolygon> CreateLegend(LegendPara aLegendPara) {
        ArrayList var1 = new ArrayList();
        new ArrayList();
        ArrayList var2 = null;
        int var6 = aLegendPara.contourValues.length + 1;
        double var7 = aLegendPara.length / (double) var6;
        LPolygon var3;
        boolean var4;
        int var5;
        PointD var9;
        if (aLegendPara.isVertical) {
            for (var5 = 0; var5 < var6; ++var5) {
                var2 = new ArrayList();
                var4 = true;
                var3 = new LPolygon();
                if (var5 == 0) {
                    var3.value = aLegendPara.contourValues[0];
                    var3.isFirst = true;
                    if (aLegendPara.isTriangle) {
                        (var9 = new PointD()).X = aLegendPara.startPoint.X + aLegendPara.width / 2.0D;
                        var9.Y = aLegendPara.startPoint.Y;
                        var2.add(var9);
                        (var9 = new PointD()).X = aLegendPara.startPoint.X + aLegendPara.width;
                        var9.Y = aLegendPara.startPoint.Y + var7;
                        var2.add(var9);
                        (var9 = new PointD()).X = aLegendPara.startPoint.X;
                        var9.Y = aLegendPara.startPoint.Y + var7;
                        var2.add(var9);
                        var4 = false;
                    }
                } else {
                    var3.value = aLegendPara.contourValues[var5 - 1];
                    var3.isFirst = false;
                    if (var5 == var6 - 1 && aLegendPara.isTriangle) {
                        (var9 = new PointD()).X = aLegendPara.startPoint.X;
                        var9.Y = aLegendPara.startPoint.Y + (double) var5 * var7;
                        var2.add(var9);
                        (var9 = new PointD()).X = aLegendPara.startPoint.X + aLegendPara.width;
                        var9.Y = aLegendPara.startPoint.Y + (double) var5 * var7;
                        var2.add(var9);
                        (var9 = new PointD()).X = aLegendPara.startPoint.X + aLegendPara.width / 2.0D;
                        var9.Y = aLegendPara.startPoint.Y + (double) (var5 + 1) * var7;
                        var2.add(var9);
                        var4 = false;
                    }
                }

                if (var4) {
                    (var9 = new PointD()).X = aLegendPara.startPoint.X;
                    var9.Y = aLegendPara.startPoint.Y + (double) var5 * var7;
                    var2.add(var9);
                    (var9 = new PointD()).X = aLegendPara.startPoint.X + aLegendPara.width;
                    var9.Y = aLegendPara.startPoint.Y + (double) var5 * var7;
                    var2.add(var9);
                    (var9 = new PointD()).X = aLegendPara.startPoint.X + aLegendPara.width;
                    var9.Y = aLegendPara.startPoint.Y + (double) (var5 + 1) * var7;
                    var2.add(var9);
                    (var9 = new PointD()).X = aLegendPara.startPoint.X;
                    var9.Y = aLegendPara.startPoint.Y + (double) (var5 + 1) * var7;
                    var2.add(var9);
                }

                var2.add(var2.get(0));
                var3.pointList = var2;
                var1.add(var3);
            }
        } else {
            for (var5 = 0; var5 < var6; ++var5) {
                var2 = new ArrayList();
                var4 = true;
                var3 = new LPolygon();
                if (var5 == 0) {
                    var3.value = aLegendPara.contourValues[0];
                    var3.isFirst = true;
                    if (aLegendPara.isTriangle) {
                        (var9 = new PointD()).X = aLegendPara.startPoint.X;
                        var9.Y = aLegendPara.startPoint.Y + aLegendPara.width / 2.0D;
                        var2.add(var9);
                        (var9 = new PointD()).X = aLegendPara.startPoint.X + var7;
                        var9.Y = aLegendPara.startPoint.Y;
                        var2.add(var9);
                        (var9 = new PointD()).X = aLegendPara.startPoint.X + var7;
                        var9.Y = aLegendPara.startPoint.Y + aLegendPara.width;
                        var2.add(var9);
                        var4 = false;
                    }
                } else {
                    var3.value = aLegendPara.contourValues[var5 - 1];
                    var3.isFirst = false;
                    if (var5 == var6 - 1 && aLegendPara.isTriangle) {
                        (var9 = new PointD()).X = aLegendPara.startPoint.X + (double) var5 * var7;
                        var9.Y = aLegendPara.startPoint.Y;
                        var2.add(var9);
                        (var9 = new PointD()).X = aLegendPara.startPoint.X + (double) (var5 + 1) * var7;
                        var9.Y = aLegendPara.startPoint.Y + aLegendPara.width / 2.0D;
                        var2.add(var9);
                        (var9 = new PointD()).X = aLegendPara.startPoint.X + (double) var5 * var7;
                        var9.Y = aLegendPara.startPoint.Y + aLegendPara.width;
                        var2.add(var9);
                        var4 = false;
                    }
                }

                if (var4) {
                    (var9 = new PointD()).X = aLegendPara.startPoint.X + (double) var5 * var7;
                    var9.Y = aLegendPara.startPoint.Y;
                    var2.add(var9);
                    (var9 = new PointD()).X = aLegendPara.startPoint.X + (double) (var5 + 1) * var7;
                    var9.Y = aLegendPara.startPoint.Y;
                    var2.add(var9);
                    (var9 = new PointD()).X = aLegendPara.startPoint.X + (double) (var5 + 1) * var7;
                    var9.Y = aLegendPara.startPoint.Y + aLegendPara.width;
                    var2.add(var9);
                    (var9 = new PointD()).X = aLegendPara.startPoint.X + (double) var5 * var7;
                    var9.Y = aLegendPara.startPoint.Y + aLegendPara.width;
                    var2.add(var9);
                }

                var2.add(var2.get(0));
                var3.pointList = var2;
                var1.add(var3);
            }
        }

        return var1;
    }
}
