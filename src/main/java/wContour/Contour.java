//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package wContour;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import wContour.Global.Border;
import wContour.Global.BorderLine;
import wContour.Global.BorderPoint;
import wContour.Global.EndPoint;
import wContour.Global.Extent;
import wContour.Global.IJPoint;
import wContour.Global.Line;
import wContour.Global.PointD;
import wContour.Global.PointF;
import wContour.Global.PolyLine;
import wContour.Global.Polygon;

public class Contour {
    private static List<EndPoint> a = new ArrayList();

    public Contour() {
    }

    public static List<PolyLine> tracingContourLines(double[][] S0, double[] X, double[] Y, int nc, double[] contour, double undefData, List<Border> borders, int[][] S1) {
        double var9 = X[1] - X[0];
        double var11 = Y[1] - Y[0];
        List var6 = borders;
        double var24 = undefData;
        int[][] undefData1 = S1;
        double var21 = var11;
        double var19 = var9;
        contour = contour;
        nc = nc;
        Y = Y;
        X = X;
        S0 = S0;
        ArrayList borders1 = new ArrayList();
        new ArrayList();
        List S11 = null;
        int var55 = S0.length;
        int var10 = S0[0].length;
        double var33;
        if ((var33 = contour[0] * 1.0E-5D) == 0.0D) {
            var33 = 1.0E-5D;
        }

        int S12;
        int var56;
        for (S12 = 0; S12 < var55; ++S12) {
            for (var56 = 0; var56 < var10; ++var56) {
                if (Math.abs(S0[S12][var56] / var24 - 1.0D) >= 0.01D) {
                    S0[S12][var56] += var33;
                }
            }
        }

        int[][][] var12 = new int[2][var55][var10 - 1];
        int[][][] var13 = new int[2][var55 - 1][var10];

        for (S12 = 0; S12 < var55; ++S12) {
            for (var56 = 0; var56 < var10; ++var56) {
                if (var56 < var10 - 1) {
                    var12[0][S12][var56] = -1;
                    var12[1][S12][var56] = -1;
                }

                if (S12 < var55 - 1) {
                    var13[0][S12][var56] = -1;
                    var13[1][S12][var56] = -1;
                }
            }
        }

        new ArrayList();
        List var15 = null;

        int var16;
        for (S12 = 0; S12 < var6.size(); ++S12) {
            Border var14 = (Border) var6.get(S12);

            for (var56 = 0; var56 < var14.getLineNum(); ++var56) {
                var15 = ((BorderLine) var14.LineList.get(var56)).ijPointList;

                for (var16 = 0; var16 < var15.size() - 1; ++var16) {
                    IJPoint var23 = (IJPoint) var15.get(var16);
                    IJPoint var60 = (IJPoint) var15.get(var16 + 1);
                    int var17;
                    int var18;
                    if (var23.I == var60.I) {
                        var17 = var23.I;
                        var18 = Math.min(var23.J, var60.J);
                        var12[0][var17][var18] = S12;
                        if (var60.J > var23.J) {
                            var12[1][var17][var18] = 1;
                        } else {
                            var12[1][var17][var18] = 0;
                        }
                    } else {
                        var18 = var23.J;
                        var17 = Math.min(var23.I, var60.I);
                        var13[0][var17][var18] = S12;
                        if (var60.I > var23.I) {
                            var13[1][var17][var18] = 0;
                        } else {
                            var13[1][var17][var18] = 1;
                        }
                    }
                }
            }
        }

        double[][] var57 = new double[var55][var10 - 1];
        double[][] var58 = new double[var55 - 1][var10];

        for (var16 = 0; var16 < nc; ++var16) {
            double var47 = contour[var16];

            for (S12 = 0; S12 < var55; ++S12) {
                for (var56 = 0; var56 < var10; ++var56) {
                    if (var56 < var10 - 1) {
                        if (undefData1[S12][var56] != 0 && undefData1[S12][var56 + 1] != 0) {
                            if ((S0[S12][var56] - var47) * (S0[S12][var56 + 1] - var47) < 0.0D) {
                                var57[S12][var56] = (var47 - S0[S12][var56]) / (S0[S12][var56 + 1] - S0[S12][var56]);
                            } else {
                                var57[S12][var56] = -2.0D;
                            }
                        } else {
                            var57[S12][var56] = -2.0D;
                        }
                    }

                    if (S12 < var55 - 1) {
                        if (undefData1[S12][var56] != 0 && undefData1[S12 + 1][var56] != 0) {
                            if ((S0[S12][var56] - var47) * (S0[S12 + 1][var56] - var47) < 0.0D) {
                                var58[S12][var56] = (var47 - S0[S12][var56]) / (S0[S12 + 1][var56] - S0[S12][var56]);
                            } else {
                                var58[S12][var56] = -2.0D;
                            }
                        } else {
                            var58[S12][var56] = -2.0D;
                        }
                    }
                }
            }

            S11 = a(S0, X, Y, var47, var19, var21, var57, var58, var12, var13, borders1.size());
            borders1.addAll(S11);
        }

        for (S12 = 0; S12 < var6.size(); ++S12) {
            BorderLine var59 = (BorderLine) ((Border) var6.get(S12)).LineList.get(0);

            for (var56 = 0; var56 < borders1.size(); ++var56) {
                PolyLine S01;
                if ((S01 = (PolyLine) borders1.get(var56)).Type.equals("Close")) {
                    PointD X1 = (PointD) S01.PointList.get(0);
                    if (pointInPolygon(var59.pointList, X1)) {
                        S01.BorderIdx = S12;
                    }
                }

                borders1.remove(var56);
                borders1.add(var56, S01);
            }
        }

        return borders1;
    }

    public static List<Border> tracingBorders(double[][] S0, double[] X, double[] Y, int[][] S1, double undefData) {
        ArrayList var6 = new ArrayList();
        int var7 = S0.length;
        int var8 = S0[0].length;

        int var9;
        int var10;
        for (var9 = 0; var9 < var7; ++var9) {
            for (var10 = 0; var10 < var8; ++var10) {
                if (Math.abs(S0[var9][var10] / undefData - 1.0D) < 0.01D) {
                    S1[var9][var10] = 0;
                } else {
                    S1[var9][var10] = 1;
                }
            }
        }

        int var5;
        int var11;
        int var12;
        int var13;
        int var14;
        int var15;
        int S01;
        int undefData1;
        for (var9 = 1; var9 < var7 - 1; ++var9) {
            for (var10 = 1; var10 < var8 - 1; ++var10) {
                if (S1[var9][var10] == 1) {
                    S01 = S1[var9][var10 - 1];
                    undefData1 = S1[var9][var10 + 1];
                    var5 = S1[var9 - 1][var10];
                    var11 = S1[var9 + 1][var10];
                    var12 = S1[var9 - 1][var10 - 1];
                    var13 = S1[var9 - 1][var10 + 1];
                    var14 = S1[var9 + 1][var10 - 1];
                    var15 = S1[var9 + 1][var10 + 1];
                    if (S01 > 0 && undefData1 > 0 && var5 > 0 && var11 > 0 && var12 > 0 && var13 > 0 && var14 > 0 && var15 > 0) {
                        S1[var9][var10] = 2;
                    }

                    if (S01 + undefData1 + var5 + var11 + var12 + var13 + var14 + var15 <= 2) {
                        S1[var9][var10] = 0;
                    }
                }
            }
        }

        boolean var16 = false;

        do {
            var16 = false;

            for (var9 = 1; var9 < var7 - 1; ++var9) {
                for (var10 = 1; var10 < var8 - 1; ++var10) {
                    if (S1[var9][var10] == 1) {
                        S01 = S1[var9][var10 - 1];
                        undefData1 = S1[var9][var10 + 1];
                        var5 = S1[var9 - 1][var10];
                        var11 = S1[var9 + 1][var10];
                        var12 = S1[var9 - 1][var10 - 1];
                        var13 = S1[var9 - 1][var10 + 1];
                        var14 = S1[var9 + 1][var10 - 1];
                        var15 = S1[var9 + 1][var10 + 1];
                        if (S01 == 0 && undefData1 == 0 || var5 == 0 && var11 == 0) {
                            S1[var9][var10] = 0;
                            var16 = true;
                        }

                        if (var14 == 0 && undefData1 == 0 && var5 == 0 || var15 == 0 && S01 == 0 && var5 == 0 || var12 == 0 && undefData1 == 0 && var11 == 0 || var13 == 0 && S01 == 0 && var11 == 0) {
                            S1[var9][var10] = 0;
                            var16 = true;
                        }
                    }
                }
            }
        } while (var16);

        for (var10 = 0; var10 < var8; ++var10) {
            if (S1[0][var10] == 1) {
                if (S1[1][var10] == 0) {
                    S1[0][var10] = 0;
                } else if (var10 == 0) {
                    if (S1[0][var10 + 1] == 0) {
                        S1[0][var10] = 0;
                    }
                } else if (var10 == var8 - 1) {
                    if (S1[0][var8 - 2] == 0) {
                        S1[0][var10] = 0;
                    }
                } else if (S1[0][var10 - 1] == 0 && S1[0][var10 + 1] == 0) {
                    S1[0][var10] = 0;
                }
            }

            if (S1[var7 - 1][var10] == 1) {
                if (S1[var7 - 2][var10] == 0) {
                    S1[var7 - 1][var10] = 0;
                } else if (var10 == 0) {
                    if (S1[var7 - 1][var10 + 1] == 0) {
                        S1[var7 - 1][var10] = 0;
                    }
                } else if (var10 == var8 - 1) {
                    if (S1[var7 - 1][var8 - 2] == 0) {
                        S1[var7 - 1][var10] = 0;
                    }
                } else if (S1[var7 - 1][var10 - 1] == 0 && S1[var7 - 1][var10 + 1] == 0) {
                    S1[var7 - 1][var10] = 0;
                }
            }
        }

        for (var9 = 0; var9 < var7; ++var9) {
            if (S1[var9][0] == 1) {
                if (S1[var9][1] == 0) {
                    S1[var9][0] = 0;
                } else if (var9 == 0) {
                    if (S1[var9 + 1][0] == 0) {
                        S1[var9][0] = 0;
                    }
                } else if (var9 == var7 - 1) {
                    if (S1[var7 - 2][0] == 0) {
                        S1[var9][0] = 0;
                    }
                } else if (S1[var9 - 1][0] == 0 && S1[var9 + 1][0] == 0) {
                    S1[var9][0] = 0;
                }
            }

            if (S1[var9][var8 - 1] == 1) {
                if (S1[var9][var8 - 2] == 0) {
                    S1[var9][var8 - 1] = 0;
                } else if (var9 == 0) {
                    if (S1[var9 + 1][var8 - 1] == 0) {
                        S1[var9][var8 - 1] = 0;
                    }
                } else if (var9 == var7 - 1) {
                    if (S1[var7 - 2][var8 - 1] == 0) {
                        S1[var9][var8 - 1] = 0;
                    }
                } else if (S1[var9 - 1][var8 - 1] == 0 && S1[var9 + 1][var8 - 1] == 0) {
                    S1[var9][var8 - 1] = 0;
                }
            }
        }

        int[][] var40 = new int[var7 + 2][var8 + 2];

        for (var9 = 0; var9 < var7 + 2; ++var9) {
            for (var10 = 0; var10 < var8 + 2; ++var10) {
                if (var9 != 0 && var9 != var7 + 1) {
                    if (var10 != 0 && var10 != var8 + 1) {
                        var40[var9][var10] = S1[var9 - 1][var10 - 1];
                    } else {
                        var40[var9][var10] = 0;
                    }
                } else {
                    var40[var9][var10] = 0;
                }
            }
        }

        S1 = new int[var7 + 2][var8 + 2];

        for (var9 = 0; var9 < var7 + 2; ++var9) {
            for (var10 = 0; var10 < var8 + 2; ++var10) {
                if (var40[var9][var10] != 1) {
                    S1[var9][var10] = 0;
                } else {
                    S01 = var40[var9][var10 - 1];
                    undefData1 = var40[var9][var10 + 1];
                    var5 = var40[var9 - 1][var10];
                    var11 = var40[var9 + 1][var10];
                    var12 = var40[var9 - 1][var10 - 1];
                    var13 = var40[var9 - 1][var10 + 1];
                    var14 = var40[var9 + 1][var10 - 1];
                    var15 = var40[var9 + 1][var10 + 1];
                    if (S01 != 1 || undefData1 != 1 || var5 != 1 || var11 != 1 || (var12 != 0 || var15 != 0) && (var13 != 0 || var14 != 0)) {
                        S1[var9][var10] = 1;
                    } else {
                        S1[var9][var10] = 2;
                    }
                }
            }
        }

        new BorderLine();
        PointD S02 = null;
        new ArrayList();
        ArrayList var37 = null;
        new ArrayList();
        ArrayList var39 = null;
        int var19 = 0;
        int var20 = 0;

        for (var9 = 1; var9 < var7 + 1; ++var9) {
            for (var10 = 1; var10 < var8 + 1; ++var10) {
                if (var40[var9][var10] == 1) {
                    var37 = new ArrayList();
                    var39 = new ArrayList();
                    (S02 = new PointD()).X = X[var10 - 1];
                    S02.Y = Y[var9 - 1];
                    IJPoint undefData2;
                    (undefData2 = new IJPoint()).I = var9 - 1;
                    undefData2.J = var10 - 1;
                    var37.add(S02);
                    var39.add(undefData2);
                    var12 = var9;
                    var13 = var10;
                    int var17 = var9;
                    int var18 = var10;
                    var14 = var9;
                    var15 = -1;

                    do {
                        int[] undefData3;
                        (undefData3 = new int[2])[0] = var19;
                        undefData3[1] = var20;
                        boolean var24 = true;
                        int var21;
                        int var25;
                        int var26;
                        if (var14 < var17) {
                            if (var40[var17][var18 - 1] == 1 && var40[var17][var18 + 1] == 1) {
                                var14 = var40[var17 - 1][var18 - 1];
                                var21 = var40[var17 + 1][var18];
                                var25 = var40[var17 + 1][var18 - 1];
                                if ((var14 == 0 || var21 != 0) && (var14 != 0 || var21 == 0 || var25 == 0)) {
                                    undefData3[0] = var17;
                                    undefData3[1] = var18 + 1;
                                } else {
                                    undefData3[0] = var17;
                                    undefData3[1] = var18 - 1;
                                }
                            } else if (var40[var17][var18 - 1] == 1 && var40[var17 + 1][var18] == 1) {
                                var14 = var40[var17 + 1][var18 - 1];
                                var21 = var40[var17 + 1][var18 + 1];
                                var25 = var40[var17][var18 - 1];
                                var26 = var40[var17][var18 + 1];
                                if (var14 != 0 && var21 != 0 && var25 != 0 && var26 != 0) {
                                    undefData3[0] = var17;
                                    undefData3[1] = var18 - 1;
                                } else if ((var14 != 0 || var26 != 0) && (var21 != 0 || var25 != 0)) {
                                    undefData3[0] = var17 + 1;
                                    undefData3[1] = var18;
                                } else {
                                    undefData3[0] = var17;
                                    undefData3[1] = var18 - 1;
                                }
                            } else if (var40[var17][var18 + 1] == 1 && var40[var17 + 1][var18] == 1) {
                                var14 = var40[var17 + 1][var18 - 1];
                                var21 = var40[var17 + 1][var18 + 1];
                                var25 = var40[var17][var18 - 1];
                                var26 = var40[var17][var18 + 1];
                                if (var14 != 0 && var21 != 0 && var25 != 0 && var26 != 0) {
                                    undefData3[0] = var17;
                                    undefData3[1] = var18 + 1;
                                } else if ((var14 != 0 || var26 != 0) && (var21 != 0 || var25 != 0)) {
                                    undefData3[0] = var17 + 1;
                                    undefData3[1] = var18;
                                } else {
                                    undefData3[0] = var17;
                                    undefData3[1] = var18 + 1;
                                }
                            } else if (var40[var17][var18 - 1] == 1) {
                                undefData3[0] = var17;
                                undefData3[1] = var18 - 1;
                            } else if (var40[var17][var18 + 1] == 1) {
                                undefData3[0] = var17;
                                undefData3[1] = var18 + 1;
                            } else if (var40[var17 + 1][var18] == 1) {
                                undefData3[0] = var17 + 1;
                                undefData3[1] = var18;
                            } else {
                                var24 = false;
                            }
                        } else if (var15 < var18) {
                            if (var40[var17 + 1][var18] == 1 && var40[var17 - 1][var18] == 1) {
                                var14 = var40[var17 + 1][var18 - 1];
                                var21 = var40[var17][var18 + 1];
                                var25 = var40[var17 + 1][var18 + 1];
                                if ((var14 == 0 || var21 != 0) && (var14 != 0 || var21 == 0 || var25 == 0)) {
                                    undefData3[0] = var17 - 1;
                                    undefData3[1] = var18;
                                } else {
                                    undefData3[0] = var17 + 1;
                                    undefData3[1] = var18;
                                }
                            } else if (var40[var17 + 1][var18] == 1 && var40[var17][var18 + 1] == 1) {
                                var25 = var40[var17 - 1][var18];
                                var26 = var40[var17 + 1][var18];
                                var14 = var40[var17 - 1][var18 + 1];
                                var21 = var40[var17 + 1][var18 + 1];
                                if (var14 != 0 && var21 != 0 && var25 != 0 && var26 != 0) {
                                    undefData3[0] = var17 + 1;
                                    undefData3[1] = var18;
                                } else if ((var14 != 0 || var26 != 0) && (var21 != 0 || var25 != 0)) {
                                    undefData3[0] = var17;
                                    undefData3[1] = var18 + 1;
                                } else {
                                    undefData3[0] = var17 + 1;
                                    undefData3[1] = var18;
                                }
                            } else if (var40[var17 - 1][var18] == 1 && var40[var17][var18 + 1] == 1) {
                                var25 = var40[var17 - 1][var18];
                                var26 = var40[var17 + 1][var18];
                                var14 = var40[var17 - 1][var18 + 1];
                                var21 = var40[var17 + 1][var18 + 1];
                                if (var14 != 0 && var21 != 0 && var25 != 0 && var26 != 0) {
                                    undefData3[0] = var17 - 1;
                                    undefData3[1] = var18;
                                } else if ((var14 != 0 || var26 != 0) && (var21 != 0 || var25 != 0)) {
                                    undefData3[0] = var17;
                                    undefData3[1] = var18 + 1;
                                } else {
                                    undefData3[0] = var17 - 1;
                                    undefData3[1] = var18;
                                }
                            } else if (var40[var17 + 1][var18] == 1) {
                                undefData3[0] = var17 + 1;
                                undefData3[1] = var18;
                            } else if (var40[var17 - 1][var18] == 1) {
                                undefData3[0] = var17 - 1;
                                undefData3[1] = var18;
                            } else if (var40[var17][var18 + 1] == 1) {
                                undefData3[0] = var17;
                                undefData3[1] = var18 + 1;
                            } else {
                                var24 = false;
                            }
                        } else if (var14 > var17) {
                            if (var40[var17][var18 - 1] == 1 && var40[var17][var18 + 1] == 1) {
                                var14 = var40[var17 + 1][var18 - 1];
                                var21 = var40[var17 - 1][var18];
                                var25 = var40[var17 - 1][var18 + 1];
                                if ((var14 == 0 || var21 != 0) && (var14 != 0 || var21 == 0 || var25 == 0)) {
                                    undefData3[0] = var17;
                                    undefData3[1] = var18 + 1;
                                } else {
                                    undefData3[0] = var17;
                                    undefData3[1] = var18 - 1;
                                }
                            } else if (var40[var17][var18 - 1] == 1 && var40[var17 - 1][var18] == 1) {
                                var14 = var40[var17 - 1][var18 - 1];
                                var21 = var40[var17 - 1][var18 + 1];
                                var25 = var40[var17][var18 - 1];
                                var26 = var40[var17][var18 + 1];
                                if (var14 != 0 && var21 != 0 && var25 != 0 && var26 != 0) {
                                    undefData3[0] = var17;
                                    undefData3[1] = var18 - 1;
                                } else if ((var14 != 0 || var26 != 0) && (var21 != 0 || var25 != 0)) {
                                    undefData3[0] = var17 - 1;
                                    undefData3[1] = var18;
                                } else {
                                    undefData3[0] = var17;
                                    undefData3[1] = var18 - 1;
                                }
                            } else if (var40[var17][var18 + 1] == 1 && var40[var17 - 1][var18] == 1) {
                                var14 = var40[var17 - 1][var18 - 1];
                                var21 = var40[var17 - 1][var18 + 1];
                                var25 = var40[var17][var18 - 1];
                                var26 = var40[var17][var18 + 1];
                                if (var14 != 0 && var21 != 0 && var25 != 0 && var26 != 0) {
                                    undefData3[0] = var17;
                                    undefData3[1] = var18 + 1;
                                } else if ((var14 != 0 || var26 != 0) && (var21 != 0 || var25 != 0)) {
                                    undefData3[0] = var17 - 1;
                                    undefData3[1] = var18;
                                } else {
                                    undefData3[0] = var17;
                                    undefData3[1] = var18 + 1;
                                }
                            } else if (var40[var17][var18 - 1] == 1) {
                                undefData3[0] = var17;
                                undefData3[1] = var18 - 1;
                            } else if (var40[var17][var18 + 1] == 1) {
                                undefData3[0] = var17;
                                undefData3[1] = var18 + 1;
                            } else if (var40[var17 - 1][var18] == 1) {
                                undefData3[0] = var17 - 1;
                                undefData3[1] = var18;
                            } else {
                                var24 = false;
                            }
                        } else if (var15 > var18) {
                            if (var40[var17 + 1][var18] == 1 && var40[var17 - 1][var18] == 1) {
                                var14 = var40[var17 + 1][var18 + 1];
                                var21 = var40[var17][var18 - 1];
                                var25 = var40[var17 - 1][var18 - 1];
                                if ((var14 == 0 || var21 != 0) && (var14 != 0 || var21 == 0 || var25 == 0)) {
                                    undefData3[0] = var17 - 1;
                                    undefData3[1] = var18;
                                } else {
                                    undefData3[0] = var17 + 1;
                                    undefData3[1] = var18;
                                }
                            } else if (var40[var17 + 1][var18] == 1 && var40[var17][var18 - 1] == 1) {
                                var25 = var40[var17 - 1][var18];
                                var26 = var40[var17 + 1][var18];
                                var14 = var40[var17 - 1][var18 - 1];
                                var21 = var40[var17 + 1][var18 - 1];
                                if (var14 != 0 && var21 != 0 && var25 != 0 && var26 != 0) {
                                    undefData3[0] = var17 + 1;
                                    undefData3[1] = var18;
                                } else if ((var14 != 0 || var26 != 0) && (var21 != 0 || var25 != 0)) {
                                    undefData3[0] = var17;
                                    undefData3[1] = var18 - 1;
                                } else {
                                    undefData3[0] = var17 + 1;
                                    undefData3[1] = var18;
                                }
                            } else if (var40[var17 - 1][var18] == 1 && var40[var17][var18 - 1] == 1) {
                                var25 = var40[var17 - 1][var18];
                                var26 = var40[var17 + 1][var18];
                                var14 = var40[var17 - 1][var18 - 1];
                                var21 = var40[var17 + 1][var18 - 1];
                                if (var14 != 0 && var21 != 0 && var25 != 0 && var26 != 0) {
                                    undefData3[0] = var17 - 1;
                                    undefData3[1] = var18;
                                } else if ((var14 != 0 || var26 != 0) && (var21 != 0 || var25 != 0)) {
                                    undefData3[0] = var17;
                                    undefData3[1] = var18 - 1;
                                } else {
                                    undefData3[0] = var17 - 1;
                                    undefData3[1] = var18;
                                }
                            } else if (var40[var17 + 1][var18] == 1) {
                                undefData3[0] = var17 + 1;
                                undefData3[1] = var18;
                            } else if (var40[var17 - 1][var18] == 1) {
                                undefData3[0] = var17 - 1;
                                undefData3[1] = var18;
                            } else if (var40[var17][var18 - 1] == 1) {
                                undefData3[0] = var17;
                                undefData3[1] = var18 - 1;
                            } else {
                                var24 = false;
                            }
                        }

                        if (!var24) {
                            break;
                        }

                        var19 = undefData3[0];
                        var20 = undefData3[1];
                        var14 = var17;
                        var15 = var18;
                        var17 = var19;
                        var18 = var20;
                        --S1[var19][var20];
                        if (S1[var19][var20] == 0) {
                            var40[var19][var20] = 3;
                        }

                        (S02 = new PointD()).X = X[var20 - 1];
                        S02.Y = Y[var19 - 1];
                        (undefData2 = new IJPoint()).I = var19 - 1;
                        undefData2.J = var20 - 1;
                        var37.add(S02);
                        var39.add(undefData2);
                    } while (var19 != var12 || var20 != var13);

                    --S1[var9][var10];
                    if (S1[var9][var10] == 0) {
                        var40[var9][var10] = 3;
                    }

                    if (var37.size() > 1) {
                        BorderLine S03;
                        (S03 = new BorderLine()).area = a((List) var37, (Extent) S03.extent);
                        S03.isOutLine = true;
                        S03.isClockwise = true;
                        S03.pointList = var37;
                        S03.ijPointList = var39;
                        var6.add(S03);
                    }
                }
            }
        }

        ArrayList undefData4 = new ArrayList();
        new Border();
        S02 = null;

        BorderLine X1;
        BorderLine Y1;
        for (var9 = 1; var9 < var6.size(); ++var9) {
            X1 = (BorderLine) var6.get(var9);

            for (var10 = 0; var10 < var9; ++var10) {
                Y1 = (BorderLine) var6.get(var10);
                if (X1.area > Y1.area) {
                    var6.remove(var9);
                    var6.add(var10, X1);
                    break;
                }
            }
        }

        Border S04;
        ArrayList S11;
        if (var6.size() == 1) {
            if (!isClockwise((X1 = (BorderLine) var6.get(0)).pointList)) {
                Collections.reverse(X1.pointList);
                Collections.reverse(X1.ijPointList);
            }

            X1.isClockwise = true;
            (S11 = new ArrayList()).add(X1);
            (S04 = new Border()).LineList = S11;
            undefData4.add(S04);
        } else {
            for (var9 = 0; var9 < var6.size() && var9 != var6.size(); ++var9) {
                if (!isClockwise((X1 = (BorderLine) var6.get(var9)).pointList)) {
                    Collections.reverse(X1.pointList);
                    Collections.reverse(X1.ijPointList);
                }

                X1.isClockwise = true;
                (S11 = new ArrayList()).add(X1);

                for (var10 = var9 + 1; var10 < var6.size() && var10 != var6.size(); ++var10) {
                    if ((Y1 = (BorderLine) var6.get(var10)).extent.xMin > X1.extent.xMin && Y1.extent.xMax < X1.extent.xMax && Y1.extent.yMin > X1.extent.yMin && Y1.extent.yMax < X1.extent.yMax) {
                        S02 = (PointD) Y1.pointList.get(0);
                        if (pointInPolygon(X1.pointList, S02)) {
                            Y1.isOutLine = false;
                            if (isClockwise(Y1.pointList)) {
                                Collections.reverse(Y1.pointList);
                                Collections.reverse(Y1.ijPointList);
                            }

                            Y1.isClockwise = false;
                            S11.add(Y1);
                            var6.remove(var10);
                            --var10;
                        }
                    }
                }

                (S04 = new Border()).LineList = S11;
                undefData4.add(S04);
            }
        }

        return undefData4;
    }

    public static List<PolyLine> smoothLines(List<PolyLine> aLineList) {
        ArrayList var1 = new ArrayList();
        new ArrayList();
        ArrayList var4 = null;

        for (int var2 = 0; var2 < aLineList.size(); ++var2) {
            PolyLine var3 = (PolyLine) aLineList.get(var2);
            if ((var4 = new ArrayList(var3.PointList)).size() > 1) {
                PointD var5;
                PointD var6;
                PointD var7;
                if (var4.size() == 2) {
                    var5 = new PointD();
                    var6 = (PointD) var4.get(0);
                    var7 = (PointD) var4.get(1);
                    var5.X = (var7.X - var6.X) / 4.0D + var6.X;
                    var5.Y = (var7.Y - var6.Y) / 4.0D + var6.Y;
                    var4.add(1, var5);
                    (var5 = new PointD()).X = (var7.X - var6.X) / 4.0D * 3.0D + var6.X;
                    var5.Y = (var7.Y - var6.Y) / 4.0D * 3.0D + var6.Y;
                    var4.add(2, var5);
                }

                if (var4.size() == 3) {
                    var5 = new PointD();
                    var6 = (PointD) var4.get(0);
                    var7 = (PointD) var4.get(1);
                    var5.X = (var7.X - var6.X) / 2.0D + var6.X;
                    var5.Y = (var7.Y - var6.Y) / 2.0D + var6.Y;
                    var4.add(1, var5);
                }

                List var8 = a(var4, var4.size());
                var3.PointList = var8;
                var1.add(var3);
            }
        }

        return var1;
    }

    public static List<PointD> smoothPoints(List<PointD> pointList) {
        return a(pointList, pointList.size());
    }

    public static List<Polygon> tracingPolygons(double[][] S0, List<PolyLine> cLineList, List<Border> borderList, double[] contour) {
        Object var4 = new ArrayList();
        ArrayList var5 = new ArrayList();
        new ArrayList();
        List var6 = null;
        ArrayList var7 = new ArrayList();
        new ArrayList();
        var6 = null;
        ArrayList var14 = new ArrayList();
        ArrayList var15 = new ArrayList();
        double var20 = 0.0D;

        Polygon var26;
        for (int var12 = 0; var12 < borderList.size(); ++var12) {
            var15.clear();
            var7.clear();
            var14.clear();
            ((List) var4).clear();
            Border var8;
            BorderLine var9;
            if (!isClockwise(var6 = (var9 = (BorderLine) (var8 = (Border) borderList.get(var12)).LineList.get(0)).pointList)) {
                Collections.reverse(var6);
            }

            PointD var10;
            BorderPoint var11;
            int var13;
            PolyLine var16;
            IJPoint var28;
            if (var8.getLineNum() == 1) {
                for (var13 = 0; var13 < var6.size(); ++var13) {
                    var10 = (PointD) var6.get(var13);
                    (var11 = new BorderPoint()).Id = -1;
                    var11.Point = var10;
                    var11.Value = S0[((IJPoint) var9.ijPointList.get(var13)).I][((IJPoint) var9.ijPointList.get(var13)).J];
                    var15.add(var11);
                }

                for (var13 = 0; var13 < cLineList.size(); ++var13) {
                    if ((var16 = (PolyLine) cLineList.get(var13)).BorderIdx == var12) {
                        var14.add(var16);
                        if (var16.Type.equals("Border")) {
                            var10 = (PointD) var16.PointList.get(0);
                            (var11 = new BorderPoint()).Id = var14.size() - 1;
                            var11.Point = var10;
                            var11.Value = var16.Value;
                            var7.add(var11);
                            var10 = (PointD) var16.PointList.get(var16.PointList.size() - 1);
                            (var11 = new BorderPoint()).Id = var14.size() - 1;
                            var11.Point = var10;
                            var11.Value = var16.Value;
                            var7.add(var11);
                        }
                    }
                }

                if (var14.isEmpty()) {
                    var28 = (IJPoint) var9.ijPointList.get(0);
                    var26 = new Polygon();
                    if (S0[var28.I][var28.J] < contour[0]) {
                        var20 = contour[0];
                        var26.IsHighCenter = false;
                    } else {
                        for (var13 = contour.length - 1; var13 >= 0; --var13) {
                            if (S0[var28.I][var28.J] > contour[var13]) {
                                var20 = contour[var13];
                                break;
                            }
                        }

                        var26.IsHighCenter = true;
                    }

                    if (var6.size() > 0) {
                        var26.IsBorder = true;
                        var26.HighValue = var20;
                        var26.LowValue = var20;
                        var26.Extent = new Extent();
                        var26.Area = a(var6, var26.Extent);
                        var26.StartPointIdx = 0;
                        var26.IsClockWise = true;
                        var26.OutLine.Type = "Border";
                        var26.OutLine.Value = var20;
                        var26.OutLine.BorderIdx = var12;
                        var26.OutLine.PointList = var6;
                        var26.HoleLines = new ArrayList();
                        ((List) var4).add(var26);
                    }
                } else {
                    var6 = c(var7, var15);
                    var4 = a((List) var14, (List) var6);
                }

                var4 = a((List) var4);
            } else {
                var9 = (BorderLine) var8.LineList.get(0);

                for (var13 = 0; var13 < cLineList.size(); ++var13) {
                    if ((var16 = (PolyLine) cLineList.get(var13)).BorderIdx == var12) {
                        var14.add(var16);
                        if (var16.Type.equals("Border")) {
                            var10 = (PointD) var16.PointList.get(0);
                            (var11 = new BorderPoint()).Id = var14.size() - 1;
                            var11.Point = var10;
                            var11.Value = var16.Value;
                            var7.add(var11);
                            var10 = (PointD) var16.PointList.get(var16.PointList.size() - 1);
                            (var11 = new BorderPoint()).Id = var14.size() - 1;
                            var11.Point = var10;
                            var11.Value = var16.Value;
                            var7.add(var11);
                        }
                    }
                }

                ArrayList var24;
                if (var14.isEmpty()) {
                    var28 = (IJPoint) var9.ijPointList.get(0);
                    var26 = new Polygon();
                    if (S0[var28.I][var28.J] < contour[0]) {
                        var20 = contour[0];
                        var26.IsHighCenter = false;
                    } else {
                        for (var13 = contour.length - 1; var13 >= 0; --var13) {
                            if (S0[var28.I][var28.J] > contour[var13]) {
                                var20 = contour[var13];
                                break;
                            }
                        }

                        var26.IsHighCenter = true;
                    }

                    if (var6.size() > 0) {
                        var26.IsBorder = true;
                        var26.HighValue = var20;
                        var26.LowValue = var20;
                        var26.Area = a(var6, var26.Extent);
                        var26.StartPointIdx = 0;
                        var26.IsClockWise = true;
                        var26.OutLine.Type = "Border";
                        var26.OutLine.Value = var20;
                        var26.OutLine.BorderIdx = var12;
                        var26.OutLine.PointList = var6;
                        var26.HoleLines = new ArrayList();
                        ((List) var4).add(var26);
                    }
                } else {
                    int[] var22 = new int[var8.getLineNum()];
                    var6 = a((double[][]) S0, var7, (Border) var8, (int[]) var22);
                    List var23 = a(var14, var6, var8, contour, var22);

                    for (var24 = new ArrayList(); var23.size() > 0; var23.remove(0)) {
                        boolean var27 = false;

                        for (var13 = 0; var13 < var24.size(); ++var13) {
                            if (((Polygon) var23.get(0)).Area > ((Polygon) var24.get(var13)).Area) {
                                var24.add(var23.get(0));
                                var27 = true;
                                break;
                            }
                        }

                        if (!var27) {
                            var24.add(var23.get(0));
                        }
                    }

                    var4 = var24;
                }

                var24 = new ArrayList();

                for (var13 = 0; var13 < var8.getLineNum(); ++var13) {
                    var24.add(((BorderLine) var8.LineList.get(var13)).pointList);
                }

                if (var24.size() > 0) {
                    b((List) var4, (List) var24);
                }

                var4 = b((List) var4);
            }

            var5.addAll((Collection) var4);
        }

        Iterator var25 = var5.iterator();

        while (var25.hasNext()) {
            if (!isClockwise((var26 = (Polygon) var25.next()).OutLine.PointList)) {
                Collections.reverse(var26.OutLine.PointList);
            }
        }

        return var5;
    }

    public static boolean pointInPolygon(List<PointD> poly, PointD aPoint) {
        boolean var19 = false;
        int var20;
        if ((var20 = poly.size()) < 3) {
            return false;
        } else {
            double var6 = ((PointD) poly.get(var20 - 1)).X;
            double var8 = ((PointD) poly.get(var20 - 1)).Y;

            for (int var18 = 0; var18 < var20; ++var18) {
                double var2 = ((PointD) poly.get(var18)).X;
                double var4 = ((PointD) poly.get(var18)).Y;
                double var10;
                double var12;
                double var14;
                double var16;
                if (var2 > var6) {
                    var10 = var6;
                    var14 = var2;
                    var12 = var8;
                    var16 = var4;
                } else {
                    var10 = var2;
                    var14 = var6;
                    var12 = var4;
                    var16 = var8;
                }

                if (var2 < aPoint.X == aPoint.X <= var6 && (aPoint.Y - var12) * (var14 - var10) < (var16 - var12) * (aPoint.X - var10)) {
                    var19 = !var19;
                }

                var6 = var2;
                var8 = var4;
            }

            return var19;
        }
    }

    public static boolean pointInPolygon(Polygon aPolygon, PointD aPoint) {
        if (!aPolygon.HasHoles()) {
            return pointInPolygon(aPolygon.OutLine.PointList, aPoint);
        } else {
            boolean var2;
            if (var2 = pointInPolygon(aPolygon.OutLine.PointList, aPoint)) {
                Iterator aPolygon1 = aPolygon.HoleLines.iterator();

                while (aPolygon1.hasNext()) {
                    if (pointInPolygon(((PolyLine) aPolygon1.next()).PointList, aPoint)) {
                        var2 = false;
                        break;
                    }
                }
            }

            return var2;
        }
    }

    public static List<PolyLine> clipPolylines(List<PolyLine> polylines, List<PointD> clipPList) {
        ArrayList var2 = new ArrayList();
        Iterator polylines1 = polylines.iterator();

        while (polylines1.hasNext()) {
            PolyLine var3 = (PolyLine) polylines1.next();
            var2.addAll(a(var3, clipPList));
        }

        return var2;
    }

    public static List<Polygon> clipPolygons(List<Polygon> polygons, List<PointD> clipPList) {
        ArrayList var2 = new ArrayList();

        for (int var3 = 0; var3 < polygons.size(); ++var3) {
            Polygon var4;
            if ((var4 = (Polygon) polygons.get(var3)).HasHoles()) {
                var2.addAll(a(var4, clipPList));
            } else {
                var2.addAll(b(var4, clipPList));
            }
        }

        ArrayList var8 = new ArrayList();
        boolean var9 = false;

        for (int polygons1 = 0; polygons1 < var2.size(); ++polygons1) {
            Polygon clipPList1 = (Polygon) var2.get(polygons1);
            var9 = false;

            for (int var5 = 0; var5 < var8.size(); ++var5) {
                if (clipPList1.Area > ((Polygon) var8.get(var5)).Area) {
                    var8.add(var5, clipPList1);
                    var9 = true;
                    break;
                }
            }

            if (!var9) {
                var8.add(clipPList1);
            }
        }

        return var8;
    }

    private static boolean a(int var0, int var1, double[][] var2, double[][] var3, int var4, int var5, double[] var6, double[] var7, double var8, double var10, double var12, int[] var14, double[] var15, boolean[] var16) {
        boolean var17 = true;
        double var18 = 0.0D;
        double var20 = 0.0D;
        int var22 = 0;
        int var23 = 0;
        boolean var24 = true;
        if (var0 < var1) {
            if (var2[var1][var5] != -2.0D && var2[var1][var5 + 1] != -2.0D) {
                if (var2[var1][var5] < var2[var1][var5 + 1]) {
                    var18 = var6[var5];
                    var20 = var7[var1] + var2[var1][var5] * var10;
                    var22 = var1;
                    var23 = var5;
                    var2[var1][var5] = -2.0D;
                    var24 = false;
                } else {
                    var18 = var6[var5 + 1];
                    var20 = var7[var1] + var2[var1][var5 + 1] * var10;
                    var22 = var1;
                    var23 = var5 + 1;
                    var2[var1][var23] = -2.0D;
                    var24 = false;
                }
            } else if (var2[var1][var5] != -2.0D && var2[var1][var5 + 1] == -2.0D) {
                var18 = var6[var5];
                var20 = var7[var1] + var2[var1][var5] * var10;
                var22 = var1;
                var23 = var5;
                var2[var1][var5] = -2.0D;
                var24 = false;
            } else if (var2[var1][var5] == -2.0D && var2[var1][var5 + 1] != -2.0D) {
                var18 = var6[var5 + 1];
                var20 = var7[var1] + var2[var1][var5 + 1] * var10;
                var22 = var1;
                var23 = var5 + 1;
                var2[var1][var23] = -2.0D;
                var24 = false;
            } else if (var3[var1 + 1][var5] != -2.0D) {
                var18 = var6[var5] + var3[var1 + 1][var5] * var8;
                var20 = var7[var1 + 1];
                var22 = var1 + 1;
                var23 = var5;
                var3[var22][var5] = -2.0D;
                var24 = true;
            } else {
                var17 = false;
            }
        } else if (var4 < var5) {
            if (var3[var1][var5] != -2.0D && var3[var1 + 1][var5] != -2.0D) {
                if (var3[var1][var5] < var3[var1 + 1][var5]) {
                    var18 = var6[var5] + var3[var1][var5] * var8;
                    var20 = var7[var1];
                    var22 = var1;
                    var23 = var5;
                    var3[var1][var5] = -2.0D;
                    var24 = true;
                } else {
                    var18 = var6[var5] + var3[var1 + 1][var5] * var8;
                    var20 = var7[var1 + 1];
                    var22 = var1 + 1;
                    var23 = var5;
                    var3[var22][var5] = -2.0D;
                    var24 = true;
                }
            } else if (var3[var1][var5] != -2.0D && var3[var1 + 1][var5] == -2.0D) {
                var18 = var6[var5] + var3[var1][var5] * var8;
                var20 = var7[var1];
                var22 = var1;
                var23 = var5;
                var3[var1][var5] = -2.0D;
                var24 = true;
            } else if (var3[var1][var5] == -2.0D && var3[var1 + 1][var5] != -2.0D) {
                var18 = var6[var5] + var3[var1 + 1][var5] * var8;
                var20 = var7[var1 + 1];
                var22 = var1 + 1;
                var23 = var5;
                var3[var22][var5] = -2.0D;
                var24 = true;
            } else if (var2[var1][var5 + 1] != -2.0D) {
                var18 = var6[var5 + 1];
                var20 = var7[var1] + var2[var1][var5 + 1] * var10;
                var22 = var1;
                var23 = var5 + 1;
                var2[var1][var23] = -2.0D;
                var24 = false;
            } else {
                var17 = false;
            }
        } else if (var6[var5] < var12) {
            if (var2[var1 - 1][var5] != -2.0D && var2[var1 - 1][var5 + 1] != -2.0D) {
                if (var2[var1 - 1][var5] > var2[var1 - 1][var5 + 1]) {
                    var18 = var6[var5];
                    var20 = var7[var1 - 1] + var2[var1 - 1][var5] * var10;
                    var22 = var1 - 1;
                    var23 = var5;
                    var2[var22][var5] = -2.0D;
                    var24 = false;
                } else {
                    var18 = var6[var5 + 1];
                    var20 = var7[var1 - 1] + var2[var1 - 1][var5 + 1] * var10;
                    var22 = var1 - 1;
                    var23 = var5 + 1;
                    var2[var22][var23] = -2.0D;
                    var24 = false;
                }
            } else if (var2[var1 - 1][var5] != -2.0D && var2[var1 - 1][var5 + 1] == -2.0D) {
                var18 = var6[var5];
                var20 = var7[var1 - 1] + var2[var1 - 1][var5] * var10;
                var22 = var1 - 1;
                var23 = var5;
                var2[var22][var5] = -2.0D;
                var24 = false;
            } else if (var2[var1 - 1][var5] == -2.0D && var2[var1 - 1][var5 + 1] != -2.0D) {
                var18 = var6[var5 + 1];
                var20 = var7[var1 - 1] + var2[var1 - 1][var5 + 1] * var10;
                var22 = var1 - 1;
                var23 = var5 + 1;
                var2[var22][var23] = -2.0D;
                var24 = false;
            } else if (var3[var1 - 1][var5] != -2.0D) {
                var18 = var6[var5] + var3[var1 - 1][var5] * var8;
                var20 = var7[var1 - 1];
                var22 = var1 - 1;
                var23 = var5;
                var3[var22][var5] = -2.0D;
                var24 = true;
            } else {
                var17 = false;
            }
        } else if (var3[var1 + 1][var5 - 1] != -2.0D && var3[var1][var5 - 1] != -2.0D) {
            if (var3[var1 + 1][var5 - 1] > var3[var1][var5 - 1]) {
                var18 = var6[var5 - 1] + var3[var1 + 1][var5 - 1] * var8;
                var20 = var7[var1 + 1];
                var22 = var1 + 1;
                var23 = var5 - 1;
                var3[var22][var23] = -2.0D;
                var24 = true;
            } else {
                var18 = var6[var5 - 1] + var3[var1][var5 - 1] * var8;
                var20 = var7[var1];
                var22 = var1;
                var23 = var5 - 1;
                var3[var1][var23] = -2.0D;
                var24 = true;
            }
        } else if (var3[var1 + 1][var5 - 1] != -2.0D && var3[var1][var5 - 1] == -2.0D) {
            var18 = var6[var5 - 1] + var3[var1 + 1][var5 - 1] * var8;
            var20 = var7[var1 + 1];
            var22 = var1 + 1;
            var23 = var5 - 1;
            var3[var22][var23] = -2.0D;
            var24 = true;
        } else if (var3[var1 + 1][var5 - 1] == -2.0D && var3[var1][var5 - 1] != -2.0D) {
            var18 = var6[var5 - 1] + var3[var1][var5 - 1] * var8;
            var20 = var7[var1];
            var22 = var1;
            var23 = var5 - 1;
            var3[var1][var23] = -2.0D;
            var24 = true;
        } else if (var2[var1][var5 - 1] != -2.0D) {
            var18 = var6[var5 - 1];
            var20 = var7[var1] + var2[var1][var5 - 1] * var10;
            var22 = var1;
            var23 = var5 - 1;
            var2[var1][var23] = -2.0D;
            var24 = false;
        } else {
            var17 = false;
        }

        var14[0] = var22;
        var14[1] = var23;
        var15[0] = var18;
        var15[1] = var20;
        var16[0] = var24;
        return var17;
    }

    private static List<PolyLine> a(double[][] var0, double[] var1, double[] var2, double var3, double var5, double var7, double[][] var9, double[][] var10, int[][][] var11, int[][][] var12, int var13) {
        ArrayList var14 = new ArrayList();
        int var15 = var0.length;
        int var39 = var0[0].length;
        int var22 = 0;
        int var23 = 0;
        double var29 = 0.0D;
        double var31 = 0.0D;
        boolean var34 = true;
        EndPoint var35 = new EndPoint();

        int var16;
        int var17;
        int var18;
        int var19;
        int var20;
        int var21;
        PointD var24;
        double var25;
        double var27;
        PolyLine var41;
        for (var16 = 0; var16 < var15; ++var16) {
            for (var17 = 0; var17 < var39; ++var17) {
                double[] var28;
                ArrayList var33;
                int[] var37;
                boolean[] var38;
                if (var17 < var39 - 1 && var11[0][var16][var17] >= 0 && var9[var16][var17] != -2.0D) {
                    var33 = new ArrayList();
                    var19 = var16;
                    var21 = var17;
                    var25 = var1[var17] + var9[var16][var17] * var5;
                    var27 = var2[var16];
                    if (var11[1][var16][var17] == 0) {
                        var18 = -1;
                        var35.sPoint.X = var1[var17 + 1];
                        var35.sPoint.Y = var2[var16];
                    } else {
                        var18 = var16;
                        var35.sPoint.X = var1[var17];
                        var35.sPoint.Y = var2[var16];
                    }

                    var20 = var17;
                    (var24 = new PointD()).X = var25;
                    var24.Y = var27;
                    var33.add(var24);
                    var35.Index = var13 + var14.size();
                    var35.Point = var24;
                    var35.BorderIdx = var11[0][var16][var17];
                    a.add(var35);
                    (var41 = new PolyLine()).Type = "Border";
                    var41.BorderIdx = var11[0][var16][var17];

                    while (true) {
                        var37 = new int[]{var22, var23};
                        var28 = new double[]{var29, var31};
                        var38 = new boolean[]{var34};
                        if (!a(var18, var19, var10, var9, var20, var21, var1, var2, var5, var7, var25, var37, var28, var38)) {
                            var41.Type = "Error";
                            break;
                        }

                        var22 = var37[0];
                        var23 = var37[1];
                        var29 = var28[0];
                        var31 = var28[1];
                        var34 = var38[0];
                        (var24 = new PointD()).X = var29;
                        var24.Y = var31;
                        var33.add(var24);
                        if (var34) {
                            if (var11[0][var22][var23] >= 0) {
                                if (var11[1][var22][var23] == 0) {
                                    var35.sPoint.X = var1[var23 + 1];
                                    var35.sPoint.Y = var2[var22];
                                } else {
                                    var35.sPoint.X = var1[var23];
                                    var35.sPoint.Y = var2[var22];
                                }
                                break;
                            }
                        } else if (var12[0][var22][var23] >= 0) {
                            if (var12[1][var22][var23] == 0) {
                                var35.sPoint.X = var1[var23];
                                var35.sPoint.Y = var2[var22];
                            } else {
                                var35.sPoint.X = var1[var23];
                                var35.sPoint.Y = var2[var22 + 1];
                            }
                            break;
                        }

                        var25 = var29;
                        var18 = var19;
                        var20 = var21;
                        var19 = var22;
                        var21 = var23;
                    }

                    var9[var16][var17] = -2.0D;
                    if (var33.size() > 1 && !var41.Type.equals("Error")) {
                        var35.Point = var24;
                        a.add(var35);
                        var41.Value = var3;
                        var41.PointList = var33;
                        var14.add(var41);
                    } else {
                        a.remove(a.size() - 1);
                    }
                }

                if (var16 < var15 - 1 && var12[0][var16][var17] >= 0 && var10[var16][var17] != -2.0D) {
                    var33 = new ArrayList();
                    var19 = var16;
                    var21 = var17;
                    var25 = var1[var17];
                    var27 = var2[var16] + var10[var16][var17] * var7;
                    var18 = var16;
                    if (var12[1][var16][var17] == 0) {
                        var20 = -1;
                        var35.sPoint.X = var1[var17];
                        var35.sPoint.Y = var2[var16];
                    } else {
                        var20 = var17;
                        var35.sPoint.X = var1[var17];
                        var35.sPoint.Y = var2[var16 + 1];
                    }

                    (var24 = new PointD()).X = var25;
                    var24.Y = var27;
                    var33.add(var24);
                    var35.Index = var13 + var14.size();
                    var35.Point = var24;
                    var35.BorderIdx = var12[0][var16][var17];
                    a.add(var35);
                    (var41 = new PolyLine()).Type = "Border";
                    var41.BorderIdx = var12[0][var16][var17];

                    while (true) {
                        var37 = new int[]{var22, var23};
                        var28 = new double[]{var29, var31};
                        var38 = new boolean[]{var34};
                        if (!a(var18, var19, var10, var9, var20, var21, var1, var2, var5, var7, var25, var37, var28, var38)) {
                            var41.Type = "Error";
                            break;
                        }

                        var22 = var37[0];
                        var23 = var37[1];
                        var29 = var28[0];
                        var31 = var28[1];
                        var34 = var38[0];
                        (var24 = new PointD()).X = var29;
                        var24.Y = var31;
                        var33.add(var24);
                        if (var34) {
                            if (var11[0][var22][var23] >= 0) {
                                if (var11[1][var22][var23] == 0) {
                                    var35.sPoint.X = var1[var23 + 1];
                                    var35.sPoint.Y = var2[var22];
                                } else {
                                    var35.sPoint.X = var1[var23];
                                    var35.sPoint.Y = var2[var22];
                                }
                                break;
                            }
                        } else if (var12[0][var22][var23] >= 0) {
                            if (var12[1][var22][var23] == 0) {
                                var35.sPoint.X = var1[var23];
                                var35.sPoint.Y = var2[var22];
                            } else {
                                var35.sPoint.X = var1[var23];
                                var35.sPoint.Y = var2[var22 + 1];
                            }
                            break;
                        }

                        var25 = var29;
                        var18 = var19;
                        var20 = var21;
                        var19 = var22;
                        var21 = var23;
                    }

                    var10[var16][var17] = -2.0D;
                    if (var33.size() > 1 && !var41.Type.equals("Error")) {
                        var35.Point = var24;
                        a.add(var35);
                        var41.Value = var3;
                        var41.PointList = var33;
                        var14.add(var41);
                    } else {
                        a.remove(a.size() - 1);
                    }
                }
            }
        }

        for (var17 = 0; var17 < var39 - 1; ++var17) {
            if (var9[0][var17] != -2.0D) {
                var9[0][var17] = -2.0D;
            }

            if (var9[var15 - 1][var17] != -2.0D) {
                var9[var15 - 1][var17] = -2.0D;
            }
        }

        for (var16 = 0; var16 < var15 - 1; ++var16) {
            if (var10[var16][0] != -2.0D) {
                var10[var16][0] = -2.0D;
            }

            if (var10[var16][var39 - 1] != -2.0D) {
                var10[var16][var39 - 1] = -2.0D;
            }
        }

        boolean[] var40;
        int[] var42;
        double var43;
        double var44;
        ArrayList var45;
        double[] var46;
        for (var16 = 1; var16 < var15 - 2; ++var16) {
            for (var17 = 1; var17 < var39 - 1; ++var17) {
                if (var10[var16][var17] != -2.0D) {
                    var45 = new ArrayList();
                    var19 = var16;
                    var21 = var17;
                    var25 = var1[var17];
                    var27 = var2[var16] + var10[var16][var17] * var7;
                    var20 = -1;
                    var18 = var16;
                    var43 = var25;
                    var44 = var27;
                    (var24 = new PointD()).X = var25;
                    var24.Y = var27;
                    var45.add(var24);
                    (var41 = new PolyLine()).Type = "Close";

                    while (true) {
                        var42 = new int[2];
                        var46 = new double[2];
                        var40 = new boolean[1];
                        if (!a(var18, var19, var10, var9, var20, var21, var1, var2, var5, var7, var25, var42, var46, var40)) {
                            var41.Type = "Error";
                            break;
                        }

                        var22 = var42[0];
                        var23 = var42[1];
                        var29 = var46[0];
                        var31 = var46[1];
                        (var24 = new PointD()).X = var29;
                        var24.Y = var31;
                        var45.add(var24);
                        if (Math.abs(var31 - var44) < 1.0E-6D && Math.abs(var29 - var43) < 1.0E-6D) {
                            break;
                        }

                        var25 = var29;
                        var18 = var19;
                        var20 = var21;
                        var19 = var22;
                        var21 = var23;
                    }

                    var10[var16][var17] = -2.0D;
                    if (var45.size() > 1 && !var41.Type.equals("Error")) {
                        var41.Value = var3;
                        var41.PointList = var45;
                        var14.add(var41);
                    }
                }
            }
        }

        for (var16 = 1; var16 < var15 - 1; ++var16) {
            for (var17 = 1; var17 < var39 - 2; ++var17) {
                if (var9[var16][var17] != -2.0D) {
                    var45 = new ArrayList();
                    var19 = var16;
                    var21 = var17;
                    var25 = var1[var17] + var9[var16][var17] * var5;
                    var27 = var2[var16];
                    var20 = var17;
                    var18 = -1;
                    var43 = var25;
                    var44 = var27;
                    (var24 = new PointD()).X = var25;
                    var24.Y = var27;
                    var45.add(var24);
                    (var41 = new PolyLine()).Type = "Close";

                    while (true) {
                        var42 = new int[2];
                        var46 = new double[2];
                        var40 = new boolean[1];
                        if (!a(var18, var19, var10, var9, var20, var21, var1, var2, var5, var7, var25, var42, var46, var40)) {
                            var41.Type = "Error";
                            break;
                        }

                        var22 = var42[0];
                        var23 = var42[1];
                        var29 = var46[0];
                        var31 = var46[1];
                        (var24 = new PointD()).X = var29;
                        var24.Y = var31;
                        var45.add(var24);
                        if (Math.abs(var31 - var44) < 1.0E-6D && Math.abs(var29 - var43) < 1.0E-6D) {
                            break;
                        }

                        var25 = var29;
                        var18 = var19;
                        var20 = var21;
                        var19 = var22;
                        var21 = var23;
                    }

                    var9[var16][var17] = -2.0D;
                    if (var45.size() > 1 && !var41.Type.equals("Error")) {
                        var41.Value = var3;
                        var41.PointList = var45;
                        var14.add(var41);
                    }
                }
            }
        }

        return var14;
    }

    private static List<Polygon> a(List<PolyLine> var0, List<BorderPoint> var1) {
        if (var0.isEmpty()) {
            return new ArrayList();
        } else {
            ArrayList var2 = new ArrayList();
            new ArrayList();
            ArrayList var23 = new ArrayList(var0);
            new ArrayList();
            ArrayList var4 = null;
            int[] var8 = new int[var1.size() - 1];

            int var6;
            for (var6 = 0; var6 < var8.length; ++var6) {
                var8[var6] = 0;
            }

            double var17 = 0.0D;
            double var19 = 0.0D;
            double var21 = 0.0D;
            ArrayList var12 = new ArrayList();
            int var10 = var1.size() - 1;

            PolyLine var3;
            ArrayList var7;
            int var25;
            Polygon var26;
            Extent var27;
            for (var6 = 0; var6 < var10; ++var6) {
                if (((BorderPoint) var1.get(var6)).Id != -1) {
                    var7 = new ArrayList();
                    var12.add(var1.get(var6));
                    BorderPoint var5;
                    int var9;
                    int var11;
                    PointD var24;
                    if (var8[var6] < 2) {
                        var7.add(((BorderPoint) var1.get(var6)).Point);
                        var9 = var6 + 1;
                        if (var9 == var10) {
                            var9 = 0;
                        }

                        var11 = 0;

                        while (true) {
                            if ((var5 = (BorderPoint) var1.get(var9)).Id == -1) {
                                if (var8[var9] == 1) {
                                    break;
                                }

                                var21 = var5.Value;
                                var7.add(var5.Point);
                                ++var8[var9];
                            } else {
                                if (var8[var9] == 2) {
                                    break;
                                }

                                label190:
                                {
                                    ++var8[var9];
                                    var3 = (PolyLine) var23.get(var5.Id);
                                    if (var11 == 0) {
                                        var17 = var3.Value;
                                        var19 = var3.Value;
                                    } else {
                                        if (var17 != var19) {
                                            break label190;
                                        }

                                        if (var3.Value > var17) {
                                            var19 = var3.Value;
                                        } else if (var3.Value < var17) {
                                            var17 = var3.Value;
                                        }
                                    }

                                    ++var11;
                                }

                                var24 = (PointD) (var4 = new ArrayList(var3.PointList)).get(0);
                                if (var5.Point.X != var24.X || var5.Point.Y != var24.Y) {
                                    Collections.reverse(var4);
                                }

                                var7.addAll(var4);

                                for (var25 = 0; var25 < var1.size() - 1; ++var25) {
                                    if (var25 != var9 && ((BorderPoint) var1.get(var25)).Id == var5.Id) {
                                        var9 = var25;
                                        ++var8[var25];
                                        break;
                                    }
                                }
                            }

                            if (var9 == var6) {
                                if (var7.size() > 0) {
                                    (var26 = new Polygon()).IsBorder = true;
                                    var26.LowValue = var17;
                                    var26.HighValue = var19;
                                    var27 = new Extent();
                                    var26.Area = a((List) var7, (Extent) var27);
                                    var26.IsClockWise = true;
                                    var26.StartPointIdx = var12.size() - 1;
                                    var26.Extent = var27;
                                    var26.OutLine.PointList = var7;
                                    var26.OutLine.Value = var17;
                                    var26.IsHighCenter = true;
                                    var26.HoleLines = new ArrayList();
                                    if (var17 == var19 && var21 < var17) {
                                        var26.IsHighCenter = false;
                                    }

                                    var26.OutLine.Type = "Border";
                                    var2.add(var26);
                                }
                                break;
                            }

                            ++var9;
                            if (var9 == var10) {
                                var9 = 0;
                            }
                        }
                    }

                    if (var8[var6] < 2) {
                        (var7 = new ArrayList()).add(((BorderPoint) var1.get(var6)).Point);
                        var9 = var6 - 1;
                        if (var9 == -1) {
                            var9 = var10 - 1;
                        }

                        var11 = 0;

                        while (true) {
                            if ((var5 = (BorderPoint) var1.get(var9)).Id == -1) {
                                if (var8[var9] == 1) {
                                    break;
                                }

                                var21 = var5.Value;
                                var7.add(var5.Point);
                                ++var8[var9];
                            } else {
                                if (var8[var9] == 2) {
                                    break;
                                }

                                label159:
                                {
                                    ++var8[var9];
                                    var3 = (PolyLine) var23.get(var5.Id);
                                    if (var11 == 0) {
                                        var17 = var3.Value;
                                        var19 = var3.Value;
                                    } else {
                                        if (var17 != var19) {
                                            break label159;
                                        }

                                        if (var3.Value > var17) {
                                            var19 = var3.Value;
                                        } else if (var3.Value < var17) {
                                            var17 = var3.Value;
                                        }
                                    }

                                    ++var11;
                                }

                                var24 = (PointD) (var4 = new ArrayList(var3.PointList)).get(0);
                                if (var5.Point.X != var24.X || var5.Point.Y != var24.Y) {
                                    Collections.reverse(var4);
                                }

                                var7.addAll(var4);

                                for (var25 = 0; var25 < var1.size() - 1; ++var25) {
                                    if (var25 != var9 && ((BorderPoint) var1.get(var25)).Id == var5.Id) {
                                        var9 = var25;
                                        ++var8[var25];
                                        break;
                                    }
                                }
                            }

                            if (var9 == var6) {
                                if (var7.size() > 0) {
                                    (var26 = new Polygon()).IsBorder = true;
                                    var26.LowValue = var17;
                                    var26.HighValue = var19;
                                    var27 = new Extent();
                                    var26.Area = a((List) var7, (Extent) var27);
                                    var26.IsClockWise = false;
                                    var26.StartPointIdx = var12.size() - 1;
                                    var26.Extent = var27;
                                    var26.OutLine.PointList = var7;
                                    var26.OutLine.Value = var17;
                                    var26.IsHighCenter = true;
                                    var26.HoleLines = new ArrayList();
                                    if (var17 == var19 && var21 < var17) {
                                        var26.IsHighCenter = false;
                                    }

                                    var26.OutLine.Type = "Border";
                                    var2.add(var26);
                                }
                                break;
                            }

                            --var9;
                            if (var9 == -1) {
                                var9 = var10 - 1;
                            }
                        }
                    }
                }
            }

            var7 = new ArrayList();

            for (var6 = 0; var6 < var23.size(); ++var6) {
                if ((var3 = (PolyLine) var23.get(var6)).Type.equals("Close") && var3.PointList.size() > 0) {
                    (var26 = new Polygon()).IsBorder = false;
                    var26.LowValue = var3.Value;
                    var26.HighValue = var3.Value;
                    var27 = new Extent();
                    var26.Area = a(var3.PointList, var27);
                    var26.IsClockWise = isClockwise(var3.PointList);
                    var26.Extent = var27;
                    var26.OutLine = var3;
                    var26.IsHighCenter = true;
                    var26.HoleLines = new ArrayList();
                    boolean var28 = false;

                    for (var25 = 0; var25 < var7.size(); ++var25) {
                        if (var26.Area > ((Polygon) var7.get(var25)).Area) {
                            var7.add(var25, var26);
                            var28 = true;
                            break;
                        }
                    }

                    if (!var28) {
                        var7.add(var26);
                    }
                }
            }

            return a((List) var2, var7, (List) var23, (List) var1);
        }
    }

    private static List<Polygon> a(Polygon var0, List<PolyLine> var1, List<BorderPoint> var2) {
        if (var1.isEmpty()) {
            return new ArrayList();
        } else {
            ArrayList var3 = new ArrayList();
            new ArrayList();
            ArrayList var14 = new ArrayList(var1);
            new ArrayList();
            ArrayList var5 = null;
            int[] var9 = new int[var2.size() - 1];

            int var6;
            for (var6 = 0; var6 < var9.length; ++var6) {
                var9[var6] = 0;
            }

            ArrayList var12 = new ArrayList();
            int var11 = var2.size() - 1;

            for (var6 = 0; var6 < var11; ++var6) {
                if (((BorderPoint) var2.get(var6)).Id != -1) {
                    var12.add(var2.get(var6));
                    var2.get(var6);
                    BorderPoint var8 = null;
                    PointD var13 = ((BorderPoint) var2.get(var6)).Point;
                    PointD var4;
                    ArrayList var7;
                    int var10;
                    int var15;
                    int var16;
                    PolyLine var17;
                    Extent var18;
                    Polygon var19;
                    if (var9[var6] <= 0) {
                        (var7 = new ArrayList()).add(((BorderPoint) var2.get(var6)).Point);
                        var10 = var6 + 1;
                        if (var10 == var11) {
                            var10 = 0;
                        }

                        var4 = (PointD) ((BorderPoint) var2.get(var10)).Point.clone();
                        if (((BorderPoint) var2.get(var10)).Id != -1) {
                            var4.X = (var4.X + var13.X) / 2.0D;
                            var4.Y = (var4.Y + var13.Y) / 2.0D;
                        } else {
                            var15 = var10 + 10;

                            for (var16 = 1; var16 <= 10; ++var16) {
                                if (((BorderPoint) var2.get(var10 + var16)).Id >= 0) {
                                    var15 = var10 + var16 - 1;
                                    break;
                                }
                            }

                            var4 = (PointD) ((BorderPoint) var2.get(var15)).Point.clone();
                        }

                        if (pointInPolygon(var0, var4)) {
                            while (true) {
                                if ((var8 = (BorderPoint) var2.get(var10)).Id == -1) {
                                    if (var9[var10] == 1) {
                                        break;
                                    }

                                    var7.add(var8.Point);
                                    ++var9[var10];
                                } else {
                                    if (var9[var10] == 1) {
                                        break;
                                    }

                                    ++var9[var10];
                                    var17 = (PolyLine) var14.get(var8.Id);
                                    var4 = (PointD) (var5 = new ArrayList(var17.PointList)).get(0);
                                    if (!a(var8.Point.X, var4.X) || !a(var8.Point.Y, var4.Y)) {
                                        Collections.reverse(var5);
                                    }

                                    var7.addAll(var5);

                                    for (var15 = 0; var15 < var2.size() - 1; ++var15) {
                                        if (var15 != var10 && ((BorderPoint) var2.get(var15)).Id == var8.Id) {
                                            var10 = var15;
                                            ++var9[var15];
                                            break;
                                        }
                                    }
                                }

                                if (var10 == var6) {
                                    if (var7.size() > 0) {
                                        (var19 = new Polygon()).IsBorder = true;
                                        var19.LowValue = var0.LowValue;
                                        var19.HighValue = var0.HighValue;
                                        var18 = new Extent();
                                        var19.Area = a((List) var7, (Extent) var18);
                                        var19.IsClockWise = true;
                                        var19.StartPointIdx = var12.size() - 1;
                                        var19.Extent = var18;
                                        var19.OutLine.PointList = var7;
                                        var19.OutLine.Value = var0.LowValue;
                                        var19.IsHighCenter = var0.IsHighCenter;
                                        var19.OutLine.Type = "Border";
                                        var19.HoleLines = new ArrayList();
                                        var3.add(var19);
                                    }
                                    break;
                                }

                                ++var10;
                                if (var10 == var11) {
                                    var10 = 0;
                                }
                            }
                        }
                    }

                    if (var9[var6] <= 0) {
                        (var7 = new ArrayList()).add(((BorderPoint) var2.get(var6)).Point);
                        var10 = var6 - 1;
                        if (var10 == -1) {
                            var10 = var11 - 1;
                        }

                        var4 = (PointD) ((BorderPoint) var2.get(var10)).Point.clone();
                        if (((BorderPoint) var2.get(var10)).Id == -1) {
                            var15 = var10 + 10;

                            for (var16 = 1; var16 <= 10; ++var16) {
                                if (((BorderPoint) var2.get(var10 + var16)).Id >= 0) {
                                    var15 = var10 + var16 - 1;
                                    break;
                                }
                            }

                            var4 = (PointD) ((BorderPoint) var2.get(var15)).Point.clone();
                        } else {
                            var4.X = (var4.X + var13.X) / 2.0D;
                            var4.Y = (var4.Y + var13.Y) / 2.0D;
                        }

                        if (pointInPolygon(var0, var4)) {
                            while (true) {
                                if ((var8 = (BorderPoint) var2.get(var10)).Id == -1) {
                                    if (var9[var10] == 1) {
                                        break;
                                    }

                                    var7.add(var8.Point);
                                    ++var9[var10];
                                } else {
                                    if (var9[var10] == 1) {
                                        break;
                                    }

                                    ++var9[var10];
                                    var17 = (PolyLine) var14.get(var8.Id);
                                    var4 = (PointD) (var5 = new ArrayList(var17.PointList)).get(0);
                                    if (!a(var8.Point.X, var4.X) || !a(var8.Point.Y, var4.Y)) {
                                        Collections.reverse(var5);
                                    }

                                    var7.addAll(var5);

                                    for (var15 = 0; var15 < var2.size() - 1; ++var15) {
                                        if (var15 != var10 && ((BorderPoint) var2.get(var15)).Id == var8.Id) {
                                            var10 = var15;
                                            ++var9[var15];
                                            break;
                                        }
                                    }
                                }

                                if (var10 == var6) {
                                    if (var7.size() > 0) {
                                        (var19 = new Polygon()).IsBorder = true;
                                        var19.LowValue = var0.LowValue;
                                        var19.HighValue = var0.HighValue;
                                        var18 = new Extent();
                                        var19.Area = a((List) var7, (Extent) var18);
                                        var19.IsClockWise = false;
                                        var19.StartPointIdx = var12.size() - 1;
                                        var19.Extent = var18;
                                        var19.OutLine.PointList = var7;
                                        var19.OutLine.Value = var0.LowValue;
                                        var19.IsHighCenter = var0.IsHighCenter;
                                        var19.OutLine.Type = "Border";
                                        var19.HoleLines = new ArrayList();
                                        var3.add(var19);
                                    }
                                    break;
                                }

                                --var10;
                                if (var10 == -1) {
                                    var10 = var11 - 1;
                                }
                            }
                        }
                    }
                }
            }

            return var3;
        }
    }

    private static List<Polygon> a(List<Polygon> var0, List<Polygon> var1, List<PolyLine> var2, List<BorderPoint> var3) {
        ArrayList var5 = new ArrayList();
        double var10 = 0.0D;
        double var12 = 0.0D;
        Polygon var4;
        if (var0.isEmpty()) {
            double var15 = ((PolyLine) var2.get(0)).Value;
            double var17 = ((PolyLine) var2.get(0)).Value;
            Iterator var6 = var2.iterator();

            while (var6.hasNext()) {
                PolyLine var7;
                if ((var7 = (PolyLine) var6.next()).Value > var15) {
                    var15 = var7.Value;
                }

                if (var7.Value < var17) {
                    var17 = var7.Value;
                }
            }

            var4 = new Polygon();
            PolyLine var20;
            (var20 = new PolyLine()).Type = "Border";
            var20.Value = var17;
            var4.IsHighCenter = false;
            if (var1.size() > 0 && ((BorderPoint) var3.get(0)).Value >= ((Polygon) var1.get(0)).LowValue) {
                var20.Value = var15;
                var4.IsHighCenter = true;
            }

            var5.clear();
            var6 = var3.iterator();

            while (var6.hasNext()) {
                BorderPoint var24 = (BorderPoint) var6.next();
                var5.add(var24.Point);
            }

            var20.PointList = new ArrayList(var5);
            if (var20.PointList.size() > 0) {
                var4.IsBorder = true;
                var4.LowValue = var20.Value;
                var4.HighValue = var20.Value;
                Extent var22 = new Extent();
                var4.Area = a(var20.PointList, var22);
                var4.IsClockWise = isClockwise(var20.PointList);
                var4.Extent = var22;
                var4.OutLine = var20;
                var4.HoleLines = new ArrayList();
                var0.add(var4);
            }
        }

        var0.addAll(var1);
        int var26 = var0.size();

        for (int var19 = 1; var19 < var26; ++var19) {
            if ((var4 = (Polygon) var0.get(var19)).OutLine.Type.equals("Close")) {
                Extent var25 = var4.Extent;
                var10 = var4.LowValue;
                PointD var23 = (PointD) var4.OutLine.PointList.get(0);

                for (int var21 = var19 - 1; var21 >= 0; --var21) {
                    Polygon var18;
                    Extent var16 = (var18 = (Polygon) var0.get(var21)).Extent;
                    var12 = var18.LowValue;
                    if (pointInPolygon((List) (new ArrayList(var18.OutLine.PointList)), var23) && var25.xMin > var16.xMin && var25.yMin > var16.yMin && var25.xMax < var16.xMax && var25.yMax < var16.yMax) {
                        if (var10 < var12) {
                            var4.IsHighCenter = false;
                        } else if (var10 == var12 && var18.IsHighCenter) {
                            var4.IsHighCenter = false;
                        }
                        break;
                    }
                }
            }
        }

        return var0;
    }

    private static List<Polygon> a(List<PolyLine> var0, List<BorderPoint> var1, Border var2, double[] var3, int[] var4) {
        ArrayList var5 = new ArrayList();
        new ArrayList();
        boolean var8 = false;
        ArrayList var28 = new ArrayList(var0);
        new ArrayList();
        ArrayList var11 = null;
        int[] var12 = new int[var1.size()];

        int var9;
        for (var9 = 0; var9 < var12.length; ++var9) {
            var12[var9] = 0;
        }

        boolean var13 = false;
        boolean var15 = false;
        double var21 = 0.0D;
        double var23 = 0.0D;
        double var25 = 0.0D;
        ArrayList var16 = new ArrayList();
        boolean var17 = false;
        boolean var18 = false;
        boolean var19 = false;
        int var14 = var1.size();

        PolyLine var6;
        PointD var29;
        int var33;
        Polygon var34;
        Extent var35;
        for (var9 = 0; var9 < var14; ++var9) {
            if (((BorderPoint) var1.get(var9)).Id != -1) {
                var16.add(var1.get(var9));
                boolean var20 = false;
                BorderPoint var7;
                ArrayList var10;
                ArrayList var27;
                BorderPoint var30;
                int var31;
                int var32;
                int var36;
                int var37;
                int var38;
                int var40;
                int var41;
                if (var12[var9] < 2) {
                    var41 = (var7 = (BorderPoint) var1.get(var9)).BInnerIdx;
                    var10 = new ArrayList();
                    var27 = new ArrayList();
                    var10.add(var7.Point);
                    var27.add(Integer.valueOf(var9));
                    var38 = var7.BorderIdx;
                    var40 = var7.BorderIdx;
                    var36 = var9 + 1;
                    ++var41;
                    if (var41 == var4[var38] - 1) {
                        var36 -= var4[var38] - 1;
                    }

                    var37 = 0;

                    while (true) {
                        if ((var7 = (BorderPoint) var1.get(var36)).Id == -1) {
                            if (var12[var36] == 1) {
                                break;
                            }

                            var25 = var7.Value;
                            var10.add(var7.Point);
                            ++var12[var36];
                            var27.add(Integer.valueOf(var36));
                        } else {
                            if (var12[var36] == 2) {
                                break;
                            }

                            label321:
                            {
                                ++var12[var36];
                                var27.add(Integer.valueOf(var36));
                                var6 = (PolyLine) var28.get(var7.Id);
                                if (var37 == 0) {
                                    var21 = var6.Value;
                                    var23 = var6.Value;
                                } else {
                                    if (var21 != var23) {
                                        break label321;
                                    }

                                    if (var6.Value > var21) {
                                        var23 = var6.Value;
                                    } else if (var6.Value < var21) {
                                        var21 = var6.Value;
                                    }
                                }

                                ++var37;
                            }

                            var29 = (PointD) (var11 = new ArrayList(var6.PointList)).get(0);
                            if (var7.Point.X != var29.X || var7.Point.Y != var29.Y) {
                                Collections.reverse(var11);
                            }

                            var10.addAll(var11);

                            for (var33 = 0; var33 < var1.size(); ++var33) {
                                if (var33 != var36 && (var30 = (BorderPoint) var1.get(var33)).Id == var7.Id) {
                                    var36 = var33;
                                    var41 = var30.BInnerIdx;
                                    ++var12[var33];
                                    var27.add(Integer.valueOf(var33));
                                    var40 = var30.BorderIdx;
                                    if (var7.BorderIdx > 0 && var7.BorderIdx == var30.BorderIdx) {
                                        var20 = true;
                                    }
                                    break;
                                }
                            }
                        }

                        if (var36 == var9) {
                            if (var10.size() > 0) {
                                if (var20) {
                                    var13 = false;
                                    var37 = 0;

                                    for (var31 = 0; var31 < var7.BorderIdx; ++var31) {
                                        var37 += var4[var31];
                                    }

                                    var38 = var37 + var4[var7.BorderIdx];
                                    var32 = var37;

                                    for (var31 = var37; var31 < var38; ++var31) {
                                        if (!var27.contains(Integer.valueOf(var31))) {
                                            var32 = var31;
                                            break;
                                        }
                                    }

                                    if (pointInPolygon((List) var10, ((BorderPoint) var1.get(var32)).Point)) {
                                        var13 = true;
                                    }

                                    if (var13) {
                                        break;
                                    }
                                }

                                (var34 = new Polygon()).IsBorder = true;
                                var34.IsInnerBorder = var20;
                                var34.LowValue = var21;
                                var34.HighValue = var23;
                                var35 = new Extent();
                                var34.Area = a((List) var10, (Extent) var35);
                                var34.IsClockWise = true;
                                var34.StartPointIdx = var16.size() - 1;
                                var34.Extent = var35;
                                var34.OutLine.PointList = var10;
                                var34.OutLine.Value = var21;
                                var34.IsHighCenter = true;
                                if (var21 == var23 && var25 < var21) {
                                    var34.IsHighCenter = false;
                                }

                                var34.OutLine.Type = "Border";
                                var34.HoleLines = new ArrayList();
                                var5.add(var34);
                            }
                            break;
                        }

                        ++var36;
                        ++var41;
                        if (var38 != var40) {
                            var38 = var40;
                        }

                        if (var41 == var4[var38] - 1) {
                            var36 -= var4[var38] - 1;
                            var41 = 0;
                        }
                    }
                }

                var20 = false;
                if (var12[var9] < 2) {
                    var10 = new ArrayList();
                    var27 = new ArrayList();
                    var41 = (var7 = (BorderPoint) var1.get(var9)).BInnerIdx;
                    var10.add(var7.Point);
                    var27.add(Integer.valueOf(var9));
                    var38 = var7.BorderIdx;
                    var40 = var7.BorderIdx;
                    var36 = var9 - 1;
                    --var41;
                    if (var41 == -1) {
                        var36 += var4[var38] - 1;
                    }

                    var37 = 0;

                    while (true) {
                        if ((var7 = (BorderPoint) var1.get(var36)).Id == -1) {
                            if (var12[var36] == 1) {
                                break;
                            }

                            var25 = var7.Value;
                            var10.add(var7.Point);
                            var27.add(Integer.valueOf(var36));
                            ++var12[var36];
                        } else {
                            if (var12[var36] == 2) {
                                break;
                            }

                            label270:
                            {
                                ++var12[var36];
                                var27.add(Integer.valueOf(var36));
                                var6 = (PolyLine) var28.get(var7.Id);
                                if (var37 == 0) {
                                    var21 = var6.Value;
                                    var23 = var6.Value;
                                } else {
                                    if (var21 != var23) {
                                        break label270;
                                    }

                                    if (var6.Value > var21) {
                                        var23 = var6.Value;
                                    } else if (var6.Value < var21) {
                                        var21 = var6.Value;
                                    }
                                }

                                ++var37;
                            }

                            var29 = (PointD) (var11 = new ArrayList(var6.PointList)).get(0);
                            if (var7.Point.X != var29.X || var7.Point.Y != var29.Y) {
                                Collections.reverse(var11);
                            }

                            var10.addAll(var11);

                            for (var33 = 0; var33 < var1.size(); ++var33) {
                                if (var33 != var36 && (var30 = (BorderPoint) var1.get(var33)).Id == var7.Id) {
                                    var36 = var33;
                                    var41 = var30.BInnerIdx;
                                    ++var12[var33];
                                    var27.add(Integer.valueOf(var33));
                                    var40 = var30.BorderIdx;
                                    if (var7.BorderIdx > 0 && var7.BorderIdx == var30.BorderIdx) {
                                        var20 = true;
                                    }
                                    break;
                                }
                            }
                        }

                        if (var36 == var9) {
                            if (var10.size() <= 0) {
                                break;
                            }

                            if (var20) {
                                var13 = false;
                                var37 = 0;

                                for (var31 = 0; var31 < var7.BorderIdx; ++var31) {
                                    var37 += var4[var31];
                                }

                                var38 = var37 + var4[var7.BorderIdx];
                                var32 = var37;

                                for (var31 = var37; var31 < var38; ++var31) {
                                    if (!var27.contains(Integer.valueOf(var31))) {
                                        var32 = var31;
                                        break;
                                    }
                                }

                                if (pointInPolygon((List) var10, ((BorderPoint) var1.get(var32)).Point)) {
                                    var13 = true;
                                }

                                if (var13) {
                                    break;
                                }
                            }

                            (var34 = new Polygon()).IsBorder = true;
                            var34.IsInnerBorder = var20;
                            var34.LowValue = var21;
                            var34.HighValue = var23;
                            var35 = new Extent();
                            var34.Area = a((List) var10, (Extent) var35);
                            var34.IsClockWise = false;
                            var34.StartPointIdx = var16.size() - 1;
                            var34.Extent = var35;
                            var34.OutLine.PointList = var10;
                            var34.OutLine.Value = var21;
                            var34.IsHighCenter = true;
                            if (var21 == var23 && var25 < var21) {
                                var34.IsHighCenter = false;
                            }

                            var34.OutLine.Type = "Border";
                            var34.HoleLines = new ArrayList();
                            var5.add(var34);
                            break;
                        }

                        --var36;
                        --var41;
                        if (var38 != var40) {
                            var38 = var40;
                        }

                        if (var41 == -1) {
                            var36 += var4[var38];
                            var41 = var4[var38] - 1;
                        }
                    }
                }
            }
        }

        ArrayList var44 = new ArrayList();
        boolean var45 = false;

        for (var9 = 0; var9 < var28.size(); ++var9) {
            if ((var6 = (PolyLine) var28.get(var9)).Type.equals("Close")) {
                (var34 = new Polygon()).IsBorder = false;
                var34.LowValue = var6.Value;
                var34.HighValue = var6.Value;
                var35 = new Extent();
                var34.Area = a(var6.PointList, var35);
                var34.IsClockWise = isClockwise(var6.PointList);
                var34.Extent = var35;
                var34.OutLine = var6;
                var34.IsHighCenter = true;
                var34.HoleLines = new ArrayList();
                var45 = false;

                for (var33 = 0; var33 < var44.size(); ++var33) {
                    if (var34.Area > ((Polygon) var44.get(var33)).Area) {
                        var44.add(var33, var34);
                        var45 = true;
                        break;
                    }
                }

                if (!var45) {
                    var44.add(var34);
                }
            }
        }

        if (var5.isEmpty()) {
            (var6 = new PolyLine()).Type = "Border";
            var6.Value = var3[0];
            var6.PointList = new ArrayList(((BorderLine) var2.LineList.get(0)).pointList);
            if (var6.PointList.size() > 0) {
                (var34 = new Polygon()).LowValue = var6.Value;
                var34.HighValue = var6.Value;
                var35 = new Extent();
                var34.Area = a(var6.PointList, var35);
                var34.IsClockWise = isClockwise(var6.PointList);
                var34.Extent = var35;
                var34.OutLine = var6;
                var34.IsHighCenter = false;
                var5.add(var34);
            }
        }

        var5.addAll(var44);

        for (var9 = var5.size() - 1; var9 >= 0; --var9) {
            if ((var34 = (Polygon) var5.get(var9)).OutLine.Type.equals("Close")) {
                Extent var42 = var34.Extent;
                var21 = var34.LowValue;
                var29 = (PointD) var34.OutLine.PointList.get(0);

                for (var33 = var9 - 1; var33 >= 0; --var33) {
                    Polygon var43;
                    Extent var39 = (var43 = (Polygon) var5.get(var33)).Extent;
                    var23 = var43.LowValue;
                    if (pointInPolygon((List) (new ArrayList(var43.OutLine.PointList)), var29) && var42.xMin > var39.xMin & var42.yMin > var39.yMin & var42.xMax < var39.xMax & var42.yMax < var39.yMax) {
                        if (var21 < var23) {
                            var34.IsHighCenter = false;
                        } else if (var21 == var23 && var43.IsHighCenter) {
                            var34.IsHighCenter = false;
                        }
                        break;
                    }
                }
            }
        }

        return var5;
    }

    private static List<Polygon> a(List<Polygon> var0) {
        ArrayList var1 = new ArrayList();

        int var2;
        for (var2 = 0; var2 < var0.size(); ++var2) {
            Polygon var4;
            if (!(var4 = (Polygon) var0.get(var2)).IsBorder) {
                var4.HoleIndex = 1;
                var1.add(var4);
            }
        }

        if (var1.isEmpty()) {
            return var0;
        } else {
            ArrayList var8 = new ArrayList();

            int var3;
            Polygon var6;
            for (var2 = 1; var2 < var1.size(); ++var2) {
                Polygon var5 = (Polygon) var1.get(var2);

                for (var3 = var2 - 1; var3 >= 0; --var3) {
                    if ((var6 = (Polygon) var1.get(var3)).Extent.Include(var5.Extent) && pointInPolygon(var6.OutLine.PointList, (PointD) var5.OutLine.PointList.get(0))) {
                        var5.HoleIndex = var6.HoleIndex + 1;
                        var6.AddHole(var5);
                        break;
                    }
                }
            }

            ArrayList var9 = new ArrayList();

            for (var2 = 0; var2 < var1.size(); ++var2) {
                if (((Polygon) var1.get(var2)).HoleIndex == 1) {
                    var9.add(var1.get(var2));
                }
            }

            for (var2 = 0; var2 < var0.size(); ++var2) {
                if ((var6 = (Polygon) var0.get(var2)).IsBorder) {
                    for (var3 = 0; var3 < var9.size(); ++var3) {
                        Polygon var7 = (Polygon) var9.get(var3);
                        if (var6.Extent.Include(var7.Extent) && pointInPolygon(var6.OutLine.PointList, (PointD) var7.OutLine.PointList.get(0))) {
                            var6.AddHole(var7);
                        }
                    }

                    var8.add(var6);
                }
            }

            var8.addAll(var1);
            return var8;
        }
    }

    private static List<Polygon> b(List<Polygon> var0) {
        ArrayList var1 = new ArrayList();

        int var2;
        for (var2 = 0; var2 < var0.size(); ++var2) {
            Polygon var4;
            if (!(var4 = (Polygon) var0.get(var2)).IsBorder || var4.IsInnerBorder) {
                var4.HoleIndex = 1;
                var1.add(var4);
            }
        }

        if (var1.isEmpty()) {
            return var0;
        } else {
            ArrayList var8 = new ArrayList();

            int var3;
            Polygon var6;
            for (var2 = 1; var2 < var1.size(); ++var2) {
                Polygon var5 = (Polygon) var1.get(var2);

                for (var3 = var2 - 1; var3 >= 0; --var3) {
                    if ((var6 = (Polygon) var1.get(var3)).Extent.Include(var5.Extent) && pointInPolygon(var6.OutLine.PointList, (PointD) var5.OutLine.PointList.get(0))) {
                        var5.HoleIndex = var6.HoleIndex + 1;
                        var6.AddHole(var5);
                        break;
                    }
                }
            }

            ArrayList var9 = new ArrayList();

            for (var2 = 0; var2 < var1.size(); ++var2) {
                if (((Polygon) var1.get(var2)).HoleIndex == 1) {
                    var9.add(var1.get(var2));
                }
            }

            for (var2 = 0; var2 < var0.size(); ++var2) {
                if ((var6 = (Polygon) var0.get(var2)).IsBorder && !var6.IsInnerBorder) {
                    for (var3 = 0; var3 < var9.size(); ++var3) {
                        Polygon var7 = (Polygon) var9.get(var3);
                        if (var6.Extent.Include(var7.Extent) && pointInPolygon(var6.OutLine.PointList, (PointD) var7.OutLine.PointList.get(0))) {
                            var6.AddHole(var7);
                        }
                    }

                    var8.add(var6);
                }
            }

            var8.addAll(var1);
            return var8;
        }
    }

    private static void b(List<Polygon> var0, List<List<PointD>> var1) {
        for (int var2 = 0; var2 < var1.size(); ++var2) {
            List var4;
            Extent var5 = c(var4 = (List) var1.get(var2));

            for (int var3 = var0.size() - 1; var3 >= 0; --var3) {
                Polygon var6;
                if ((var6 = (Polygon) var0.get(var3)).Extent.Include(var5)) {
                    boolean var7 = true;
                    Iterator var8 = var4.iterator();

                    while (var8.hasNext()) {
                        PointD var9 = (PointD) var8.next();
                        if (!pointInPolygon(var6.OutLine.PointList, var9)) {
                            var7 = false;
                            break;
                        }
                    }

                    if (var7) {
                        var6.AddHole(var4);
                        break;
                    }
                }
            }
        }

    }

    private static List<PolyLine> a(PolyLine var0, List<PointD> var1) {
        ArrayList var2 = new ArrayList();
        Object var3 = var0.PointList;
        Extent var4 = c(var0.PointList);
        Extent var5 = c(var1);
        if (!a(var4, var5)) {
            return var2;
        } else {
            if (!isClockwise(var1)) {
                Collections.reverse(var1);
            }

            int var13;
            boolean var14;
            if (pointInPolygon(var1, (PointD) ((List) var3).get(0))) {
                var14 = true;
                int var6 = 0;

                for (var13 = 0; var13 < ((List) var3).size(); ++var13) {
                    if (!pointInPolygon(var1, (PointD) ((List) var3).get(var13))) {
                        var6 = var13;
                        var14 = false;
                        break;
                    }
                }

                if (var14) {
                    var2.add(var0);
                    return var2;
                }

                if (var0.Type.equals("Close")) {
                    ArrayList var7 = new ArrayList();

                    for (var13 = var6; var13 < ((List) var3).size(); ++var13) {
                        var7.add(((List) var3).get(var13));
                    }

                    for (var13 = 1; var13 < var6; ++var13) {
                        var7.add(((List) var3).get(var13));
                    }

                    var7.add(var7.get(0));
                    var3 = new ArrayList(var7);
                } else {
                    Collections.reverse((List) var3);
                }
            }

            var14 = pointInPolygon(var1, (PointD) ((List) var3).get(0));
            ArrayList var12 = new ArrayList();
            PointD var16 = (PointD) ((List) var3).get(0);

            PolyLine var17;
            for (var13 = 1; var13 < ((List) var3).size(); ++var13) {
                PointD var8 = (PointD) ((List) var3).get(var13);
                PointD var9;
                Line var10;
                Line var11;
                int var15;
                PointD var18;
                if (pointInPolygon(var1, var8)) {
                    if (!var14) {
                        var9 = new PointD();
                        (var10 = new Line()).P1 = var16;
                        var10.P2 = var8;
                        var16 = (PointD) var1.get(var1.size() - 1);

                        for (var15 = 0; var15 < var1.size(); ++var15) {
                            var18 = (PointD) var1.get(var15);
                            (var11 = new Line()).P1 = var16;
                            var11.P2 = var18;
                            if (a(var10, var11)) {
                                var9 = b(var10, var11);
                                break;
                            }

                            var16 = var18;
                        }

                        var12.add(var9);
                    }

                    var12.add(((List) var3).get(var13));
                    var14 = true;
                } else if (var14) {
                    var9 = new PointD();
                    (var10 = new Line()).P1 = var16;
                    var10.P2 = var8;
                    var16 = (PointD) var1.get(var1.size() - 1);

                    for (var15 = 0; var15 < var1.size(); ++var15) {
                        var18 = (PointD) var1.get(var15);
                        (var11 = new Line()).P1 = var16;
                        var11.P2 = var18;
                        if (a(var10, var11)) {
                            var9 = b(var10, var11);
                            break;
                        }

                        var16 = var18;
                    }

                    var12.add(var9);
                    (var17 = new PolyLine()).Value = var0.Value;
                    var17.Type = var0.Type;
                    var17.PointList = var12;
                    var2.add(var17);
                    var14 = false;
                    var12 = new ArrayList();
                }

                var16 = var8;
            }

            if (var14 && var12.size() > 1) {
                (var17 = new PolyLine()).Value = var0.Value;
                var17.Type = var0.Type;
                var17.PointList = var12;
                var2.add(var17);
            }

            return var2;
        }
    }

    private static List<Polygon> a(Polygon var0, List<PointD> var1) {
        Object var2 = new ArrayList();
        ArrayList var3 = new ArrayList();
        List var4 = var0.OutLine.PointList;
        Extent var5 = c(var0.OutLine.PointList);
        Extent var6 = c(var1);
        if (!a(var5, var6)) {
            return (List) var2;
        } else {
            if (!isClockwise(var1)) {
                Collections.reverse(var1);
            }

            ArrayList var7 = new ArrayList();
            int var9;
            int var23;
            if (pointInPolygon(var1, (PointD) var4.get(0))) {
                boolean var8 = true;
                var9 = 0;

                for (var23 = 0; var23 < var4.size(); ++var23) {
                    if (!pointInPolygon(var1, (PointD) var4.get(var23))) {
                        var9 = var23;
                        var8 = false;
                        break;
                    }
                }

                if (var8) {
                    ((List) var2).add(var0);
                    return (List) var2;
                }

                ArrayList var10 = new ArrayList();

                for (var23 = var9; var23 < var4.size(); ++var23) {
                    var10.add(var4.get(var23));
                }

                for (var23 = 1; var23 < var9; ++var23) {
                    var10.add(var4.get(var23));
                }

                var10.add(var10.get(0));
                var7.add(var10);
            } else {
                var7.add(var4);
            }

            ArrayList var27 = new ArrayList();

            for (var9 = 0; var9 < var0.HoleLines.size(); ++var9) {
                List var28;
                if (a(c(var28 = ((PolyLine) var0.HoleLines.get(var9)).PointList), var6)) {
                    if (!pointInPolygon(var1, (PointD) var28.get(0))) {
                        var7.add(var28);
                    } else {
                        boolean var11 = true;
                        int var12 = 0;

                        for (var23 = 0; var23 < var28.size(); ++var23) {
                            if (!pointInPolygon(var1, (PointD) var28.get(var23))) {
                                var12 = var23;
                                var11 = false;
                                break;
                            }
                        }

                        if (var11) {
                            var27.add(var28);
                        } else {
                            ArrayList var13 = new ArrayList();

                            for (var23 = var12; var23 < var28.size(); ++var23) {
                                var13.add(var28.get(var23));
                            }

                            for (var23 = 1; var23 < var12; ++var23) {
                                var13.add(var28.get(var23));
                            }

                            var13.add(var13.get(0));
                            var7.add(var13);
                        }
                    }
                }
            }

            ArrayList var29 = new ArrayList();
            BorderPoint var30 = new BorderPoint();
            Iterator var31 = var1.iterator();

            PointD var33;
            while (var31.hasNext()) {
                var33 = (PointD) var31.next();
                (var30 = new BorderPoint()).Point = var33;
                var30.Id = -1;
                var29.add(var30);
            }

            for (int var32 = 0; var32 < var7.size(); ++var32) {
                var4 = (List) var7.get(var32);
                boolean var35 = false;
                ArrayList var18 = new ArrayList();
                PointD var24 = (PointD) var4.get(0);
                int var19 = -1;
                int var20 = -1;
                boolean var21 = true;
                int var22 = 0;

                for (var23 = 1; var23 < var4.size(); ++var23) {
                    PointD var14 = (PointD) var4.get(var23);
                    PointD var15;
                    Line var16;
                    Line var17;
                    int var25;
                    PointD var36;
                    if (pointInPolygon(var1, var14)) {
                        if (!var35) {
                            (var16 = new Line()).P1 = var24;
                            var16.P2 = var14;
                            var36 = ((BorderPoint) var29.get(var29.size() - 1)).Point;
                            var15 = new PointD();

                            for (var25 = 0; var25 < var29.size(); ++var25) {
                                var33 = ((BorderPoint) var29.get(var25)).Point;
                                (var17 = new Line()).P1 = var36;
                                var17.P2 = var33;
                                if (a(var16, var17)) {
                                    var15 = b(var16, var17);
                                    (var30 = new BorderPoint()).Id = var3.size();
                                    var30.Point = var15;
                                    var29.add(var25, var30);
                                    var19 = var25;
                                    break;
                                }

                                var36 = var33;
                            }

                            var18.add(var15);
                        }

                        var18.add(var4.get(var23));
                        var35 = true;
                    } else if (var35) {
                        (var16 = new Line()).P1 = var24;
                        var16.P2 = var14;
                        var36 = ((BorderPoint) var29.get(var29.size() - 1)).Point;
                        var15 = new PointD();

                        for (var25 = 0; var25 < var29.size(); ++var25) {
                            var33 = ((BorderPoint) var29.get(var25)).Point;
                            (var17 = new Line()).P1 = var36;
                            var17.P2 = var33;
                            if (a(var16, var17)) {
                                if (!var21) {
                                    if (var19 - var20 > 0 && var19 - var20 <= 10) {
                                        if (!a(var22, var20, var19, var25)) {
                                            var29.remove(var19);
                                            var29.add(var20, var30);
                                        }
                                    } else if (var19 - var20 < 0 && var19 - var20 >= -10) {
                                        if (!a(var22, var20, var19, var25)) {
                                            var29.remove(var19);
                                            var29.add(var20 + 1, var30);
                                        }
                                    } else if (var19 == var20 && !a(var22, var20, var19, var25)) {
                                        var29.remove(var19);
                                        var29.add(var19 + 1, var30);
                                    }
                                }

                                var15 = b(var16, var17);
                                (var30 = new BorderPoint()).Id = var3.size();
                                var30.Point = var15;
                                var29.add(var25, var30);
                                var20 = var25;
                                var22 = var19;
                                var21 = false;
                                break;
                            }

                            var36 = var33;
                        }

                        var18.add(var15);
                        PolyLine var26;
                        (var26 = new PolyLine()).Value = var0.OutLine.Value;
                        var26.Type = var0.OutLine.Type;
                        var26.PointList = var18;
                        var3.add(var26);
                        var35 = false;
                        var18 = new ArrayList();
                    }

                    var24 = var14;
                }
            }

            if (var3.size() > 0) {
                var2 = a(var0, var3, var29);
            } else if (pointInPolygon(var4, (PointD) var1.get(0))) {
                Extent var34 = new Extent();
                Polygon var37;
                (var37 = new Polygon()).IsBorder = true;
                var37.LowValue = var0.LowValue;
                var37.HighValue = var0.HighValue;
                var37.Area = a(var1, var34);
                var37.IsClockWise = true;
                var37.Extent = var34;
                var37.OutLine.PointList = var1;
                var37.OutLine.Value = var0.LowValue;
                var37.IsHighCenter = var0.IsHighCenter;
                var37.OutLine.Type = "Border";
                var37.HoleLines = new ArrayList();
                ((List) var2).add(var37);
            }

            if (var27.size() > 0) {
                b((List) var2, (List) var27);
            }

            return (List) var2;
        }
    }

    private static List<Polygon> b(Polygon var0, List<PointD> var1) {
        Object var2 = new ArrayList();
        ArrayList var3 = new ArrayList();
        Object var4 = var0.OutLine.PointList;
        Extent var5 = c(var0.OutLine.PointList);
        Extent var6 = c(var1);
        if (!a(var5, var6)) {
            return (List) var2;
        } else {
            if (!isClockwise(var1)) {
                Collections.reverse(var1);
            }

            int var22;
            if (pointInPolygon(var1, (PointD) ((List) var4).get(0))) {
                boolean var7 = true;
                int var8 = 0;

                for (var22 = 0; var22 < ((List) var4).size(); ++var22) {
                    if (!pointInPolygon(var1, (PointD) ((List) var4).get(var22))) {
                        var8 = var22;
                        var7 = false;
                        break;
                    }
                }

                if (var7) {
                    ((List) var2).add(var0);
                    return (List) var2;
                }

                ArrayList var23 = new ArrayList();

                for (var22 = var8; var22 < ((List) var4).size(); ++var22) {
                    var23.add(((List) var4).get(var22));
                }

                for (var22 = 1; var22 < var8; ++var22) {
                    var23.add(((List) var4).get(var22));
                }

                var23.add(var23.get(0));
                var4 = new ArrayList(var23);
            }

            ArrayList var27 = new ArrayList();
            BorderPoint var29 = new BorderPoint();
            Iterator var24 = var1.iterator();

            PointD var9;
            while (var24.hasNext()) {
                var9 = (PointD) var24.next();
                (var29 = new BorderPoint()).Point = var9;
                var29.Id = -1;
                var27.add(var29);
            }

            boolean var25 = false;
            ArrayList var15 = new ArrayList();
            var9 = (PointD) ((List) var4).get(0);
            int var16 = -1;
            int var17 = -1;
            int var18 = 0;
            boolean var19 = true;

            for (var22 = 1; var22 < ((List) var4).size(); ++var22) {
                PointD var11 = (PointD) ((List) var4).get(var22);
                PointD var10;
                PointD var12;
                Line var13;
                Line var14;
                int var26;
                if (pointInPolygon(var1, var11)) {
                    if (!var25) {
                        (var13 = new Line()).P1 = var9;
                        var13.P2 = var11;
                        var9 = ((BorderPoint) var27.get(var27.size() - 1)).Point;
                        var12 = new PointD();

                        for (var26 = 0; var26 < var27.size(); ++var26) {
                            var10 = ((BorderPoint) var27.get(var26)).Point;
                            (var14 = new Line()).P1 = var9;
                            var14.P2 = var10;
                            if (a(var13, var14)) {
                                var12 = b(var13, var14);
                                (var29 = new BorderPoint()).Id = var3.size();
                                var29.Point = var12;
                                var27.add(var26, var29);
                                var16 = var26;
                                break;
                            }

                            var9 = var10;
                        }

                        var15.add(var12);
                    }

                    var15.add(((List) var4).get(var22));
                    var25 = true;
                } else if (var25) {
                    (var13 = new Line()).P1 = var9;
                    var13.P2 = var11;
                    var9 = ((BorderPoint) var27.get(var27.size() - 1)).Point;
                    var12 = new PointD();

                    for (var26 = 0; var26 < var27.size(); ++var26) {
                        var10 = ((BorderPoint) var27.get(var26)).Point;
                        (var14 = new Line()).P1 = var9;
                        var14.P2 = var10;
                        if (a(var13, var14)) {
                            if (!var19) {
                                if (var16 - var17 > 0 && var16 - var17 <= 10) {
                                    if (!a(var18, var17, var16, var26)) {
                                        var27.remove(var16);
                                        var27.add(var17, var29);
                                    }
                                } else if (var16 - var17 < 0 && var16 - var17 >= -10) {
                                    if (!a(var18, var17, var16, var26)) {
                                        var27.remove(var16);
                                        var27.add(var17 + 1, var29);
                                    }
                                } else if (var16 == var17 && !a(var18, var17, var16, var26)) {
                                    var27.remove(var16);
                                    var27.add(var16 + 1, var29);
                                }
                            }

                            var12 = b(var13, var14);
                            (var29 = new BorderPoint()).Id = var3.size();
                            var29.Point = var12;
                            var27.add(var26, var29);
                            var17 = var26;
                            var18 = var16;
                            var19 = false;
                            break;
                        }

                        var9 = var10;
                    }

                    var15.add(var12);
                    PolyLine var28;
                    (var28 = new PolyLine()).Value = var0.OutLine.Value;
                    var28.Type = var0.OutLine.Type;
                    var28.PointList = var15;
                    var3.add(var28);
                    var25 = false;
                    var15 = new ArrayList();
                }

                var9 = var11;
            }

            if (var3.size() > 0) {
                var2 = a(var0, var3, var27);
            } else if (pointInPolygon((List) var4, (PointD) var1.get(0))) {
                Extent var20 = new Extent();
                Polygon var21;
                (var21 = new Polygon()).IsBorder = true;
                var21.LowValue = var0.LowValue;
                var21.HighValue = var0.HighValue;
                var21.Area = a(var1, var20);
                var21.IsClockWise = true;
                var21.Extent = var20;
                var21.OutLine.PointList = var1;
                var21.OutLine.Value = var0.LowValue;
                var21.IsHighCenter = var0.IsHighCenter;
                var21.OutLine.Type = "Border";
                var21.HoleLines = new ArrayList();
                ((List) var2).add(var21);
            }

            return (List) var2;
        }
    }

    private static boolean a(int var0, int var1, int var2, int var3) {
        if (var1 < var0) {
            ++var0;
        }

        if (var2 < var0) {
            ++var0;
        }

        if (var2 < var1) {
            ++var1;
        }

        if (var1 < var0) {
            int var4 = var0;
            var0 = var1;
            var1 = var4;
        }

        return var2 > var0 && var2 <= var1 ? var3 > var0 && var3 <= var1 : var3 <= var0 || var3 > var1;
    }

    private static List<PointD> a(List<PointD> var0, int var1) {
        double var4 = 0.0D;
        double var6 = 0.0D;
        ArrayList var9 = new ArrayList();
        if (var1 < 4) {
            return null;
        } else {
            boolean var10 = false;
            PointD var8 = (PointD) var0.get(0);
            PointD var11 = (PointD) var0.get(var1 - 1);
            if (var8.X == var11.X && var8.Y == var11.Y) {
                var0.remove(0);
                var0.add(var0.get(0));
                var0.add(var0.get(1));
                var0.add(var0.get(2));
                var0.add(var0.get(3));
                var0.add(var0.get(4));
                var0.add(var0.get(5));
                var0.add(var0.get(6));
                var10 = true;
            }

            var1 = var0.size();

            for (int var3 = 0; var3 < var1 - 3; ++var3) {
                for (float var2 = 0.0F; var2 <= 1.0F; var2 += 0.05F) {
                    double[] var12;
                    var4 = (var12 = a(var0, (double) var2, var3))[0];
                    var6 = var12[1];
                    if (var10) {
                        if (var3 > 3) {
                            (var8 = new PointD()).X = var4;
                            var8.Y = var6;
                            var9.add(var8);
                        }
                    } else {
                        (var8 = new PointD()).X = var4;
                        var8.Y = var6;
                        var9.add(var8);
                    }
                }
            }

            if (var10) {
                var9.add(var9.get(0));
            } else {
                var9.add(0, var0.get(0));
                var9.add(var0.get(var0.size() - 1));
            }

            return var9;
        }
    }

    private static double[] a(List<PointD> var0, double var1, int var3) {
        double[] var4 = new double[]{0.16666666666666666D * (-var1 + 1.0D) * (-var1 + 1.0D) * (-var1 + 1.0D), 0.16666666666666666D * (var1 * 3.0D * var1 * var1 - var1 * 6.0D * var1 + 4.0D), 0.16666666666666666D * (var1 * -3.0D * var1 * var1 + var1 * 3.0D * var1 + var1 * 3.0D + 1.0D), var1 * 0.16666666666666666D * var1 * var1};
        double var6 = 0.0D;
        double var8 = 0.0D;

        for (int var18 = 0; var18 < 4; ++var18) {
            PointD var2 = (PointD) var0.get(var3 + var18);
            var6 += var4[var18] * var2.X;
            var8 += var4[var18] * var2.Y;
        }

        double[] var17;
        (var17 = new double[2])[0] = var6;
        var17[1] = var8;
        return var17;
    }

    public static List<PolyLine> tracingStreamline(double[][] U, double[][] V, double[] X, double[] Y, double UNDEF, int density) {
        ArrayList var7 = new ArrayList();
        int var8 = U[1].length;
        int var9;
        double[][] var10 = new double[var9 = U.length][var8];
        double[][] var11 = new double[var9][var8];
        double var12 = X[1] - X[0];
        double var14 = Y[1] - Y[0];
        if (density == 0) {
            density = 1;
        }

        double var16 = var12 / (double) (density * density);

        int var18;
        int var19;
        for (var18 = 0; var18 < var9; ++var18) {
            for (var19 = 0; var19 < var8; ++var19) {
                if (Math.abs(U[var18][var19] / UNDEF - 1.0D) < 0.01D) {
                    var10[var18][var19] = 0.1D;
                    var11[var18][var19] = 0.1D;
                } else {
                    double var20;
                    if ((var20 = Math.sqrt(U[var18][var19] * U[var18][var19] + V[var18][var19] * V[var18][var19])) == 0.0D) {
                        var20 = 1.0D;
                    }

                    var10[var18][var19] = U[var18][var19] / var20 * var12 / (double) density;
                    var11[var18][var19] = V[var18][var19] / var20 * var14 / (double) density;
                }
            }
        }

        ArrayList[][] var31 = new ArrayList[var9 - 1][var8 - 1];
        int[][] var21 = new int[var9 - 1][var8 - 1];

        for (var18 = 0; var18 < var9 - 1; ++var18) {
            for (var19 = 0; var19 < var8 - 1; ++var19) {
                if (var18 % 2 == 0 && var19 % 2 == 0) {
                    var21[var18][var19] = 0;
                } else {
                    var21[var18][var19] = 1;
                }

                var31[var18][var19] = new ArrayList();
            }
        }

        for (var18 = 0; var18 < var9 - 1; ++var18) {
            for (var19 = 0; var19 < var8 - 1; ++var19) {
                if (var21[var18][var19] == 0) {
                    ArrayList V1 = new ArrayList();
                    PointD density1 = new PointD();
                    PolyLine var25 = new PolyLine();
                    density1.X = X[var19] + var12 / 2.0D;
                    density1.Y = Y[var18] + var14 / 2.0D;
                    V1.add((PointD) density1.clone());
                    var31[var18][var19].add((PointD) density1.clone());
                    var21[var18][var19] = 1;
                    int var22 = var18;
                    int var23 = var19;

                    int var24;
                    int[] var26;
                    boolean var27;
                    PointD var28;
                    boolean var32;
                    Iterator var33;
                    for (var24 = 0; var24 < 500; ++var24) {
                        (var26 = new int[2])[0] = var22;
                        var26[1] = var23;
                        var27 = a(density1, var10, var11, X, Y, var26, true);
                        var22 = var26[0];
                        var23 = var26[1];
                        if (!var27 || Math.abs(U[var22][var23] / UNDEF - 1.0D) < 0.01D || Math.abs(U[var22][var23 + 1] / UNDEF - 1.0D) < 0.01D || Math.abs(U[var22 + 1][var23] / UNDEF - 1.0D) < 0.01D || Math.abs(U[var22 + 1][var23 + 1] / UNDEF - 1.0D) < 0.01D) {
                            break;
                        }

                        var32 = false;
                        var33 = var31[var22][var23].iterator();

                        while (var33.hasNext()) {
                            var28 = (PointD) var33.next();
                            if (Math.sqrt((density1.X - var28.X) * (density1.X - var28.X) + (density1.Y - var28.Y) * (density1.Y - var28.Y)) < var16) {
                                var32 = true;
                                break;
                            }
                        }

                        if (var32) {
                            break;
                        }

                        V1.add((PointD) density1.clone());
                        var31[var22][var23].add((PointD) density1.clone());
                        var21[var22][var23] = 1;
                    }

                    density1.X = X[var19] + var12 / 2.0D;
                    density1.Y = Y[var18] + var14 / 2.0D;
                    var22 = var18;
                    var23 = var19;

                    for (var24 = 0; var24 < 500; ++var24) {
                        (var26 = new int[2])[0] = var22;
                        var26[1] = var23;
                        var27 = a(density1, var10, var11, X, Y, var26, false);
                        var22 = var26[0];
                        var23 = var26[1];
                        if (!var27 || Math.abs(U[var22][var23] / UNDEF - 1.0D) < 0.01D || Math.abs(U[var22][var23 + 1] / UNDEF - 1.0D) < 0.01D || Math.abs(U[var22 + 1][var23] / UNDEF - 1.0D) < 0.01D || Math.abs(U[var22 + 1][var23 + 1] / UNDEF - 1.0D) < 0.01D) {
                            break;
                        }

                        var32 = false;
                        var33 = var31[var22][var23].iterator();

                        while (var33.hasNext()) {
                            var28 = (PointD) var33.next();
                            if (Math.sqrt((density1.X - var28.X) * (density1.X - var28.X) + (density1.Y - var28.Y) * (density1.Y - var28.Y)) < var16) {
                                var32 = true;
                                break;
                            }
                        }

                        if (var32) {
                            break;
                        }

                        V1.add(0, (PointD) density1.clone());
                        var31[var22][var23].add((PointD) density1.clone());
                        var21[var22][var23] = 1;
                    }

                    if (V1.size() > 1) {
                        var25.PointList = V1;
                        var7.add(var25);
                    }
                }
            }
        }

        return var7;
    }

    private static boolean a(PointD var0, double[][] var1, double[][] var2, double[] var3, double[] var4, int[] var5, boolean var6) {
        int var23 = var3.length;
        int var24 = var4.length;
        double var25 = var3[1] - var3[0];
        double var27 = var4[1] - var4[0];
        int var29 = var5[0];
        int var30 = var5[1];
        double var7 = var1[var29][var30];
        double var9 = var1[var29][var30 + 1];
        double var11 = var1[var29 + 1][var30];
        double var13 = var1[var29 + 1][var30 + 1];
        double var15 = var7 + (var11 - var7) * ((var0.Y - var4[var29]) / var27);
        double var17 = var9 + (var13 - var9) * ((var0.Y - var4[var29]) / var27);
        double var19 = var15 + (var17 - var15) * ((var0.X - var3[var30]) / var25);
        var7 = var2[var29][var30];
        var9 = var2[var29][var30 + 1];
        var11 = var2[var29 + 1][var30];
        var13 = var2[var29 + 1][var30 + 1];
        var15 = var7 + (var11 - var7) * ((var0.Y - var4[var29]) / var27);
        var17 = var9 + (var13 - var9) * ((var0.Y - var4[var29]) / var27);
        double var21 = var15 + (var17 - var15) * ((var0.X - var3[var30]) / var25);
        if (var6) {
            var0.X += var19;
            var0.Y += var21;
        } else {
            var0.X -= var19;
            var0.Y -= var21;
        }

        if (var0.X < var3[var30] || var0.X > var3[var30 + 1] || var0.Y < var4[var29] || var0.Y > var4[var29 + 1]) {
            if (var0.X < var3[0] || var0.X > var3[var3.length - 1] || var0.Y < var4[0] || var0.Y > var4[var4.length - 1]) {
                return false;
            }

            label52:
            for (int var31 = var29 - 2; var31 < var29 + 3; ++var31) {
                if (var31 >= 0 && var31 < var24 && var0.Y >= var4[var31] && var0.Y <= var4[var31 + 1]) {
                    var29 = var31;
                    var31 = var30 - 2;

                    while (true) {
                        if (var31 >= var30 + 3) {
                            break label52;
                        }

                        if (var31 >= 0 && var31 < var23 && var0.X >= var3[var31] && var0.X <= var3[var31 + 1]) {
                            var30 = var31;
                            break label52;
                        }

                        ++var31;
                    }
                }
            }
        }

        var5[0] = var29;
        var5[1] = var30;
        return true;
    }

    private static Extent c(List<PointD> var0) {
        PointD var10;
        double var1 = (var10 = (PointD) var0.get(0)).X;
        double var5 = var10.X;
        double var3 = var10.Y;
        double var7 = var10.Y;

        for (int var9 = 1; var9 < var0.size(); ++var9) {
            if ((var10 = (PointD) var0.get(var9)).X < var1) {
                var1 = var10.X;
            }

            if (var10.X > var5) {
                var5 = var10.X;
            }

            if (var10.Y < var3) {
                var3 = var10.Y;
            }

            if (var10.Y > var7) {
                var7 = var10.Y;
            }
        }

        Extent var11;
        (var11 = new Extent()).xMin = var1;
        var11.yMin = var3;
        var11.xMax = var5;
        var11.yMax = var7;
        return var11;
    }

    private static double a(List<PointD> var0, Extent var1) {
        PointD var3;
        double var4 = (var3 = (PointD) var0.get(0)).X;
        double var8 = var3.X;
        double var6 = var3.Y;
        double var10 = var3.Y;

        for (int var2 = 1; var2 < var0.size(); ++var2) {
            if ((var3 = (PointD) var0.get(var2)).X < var4) {
                var4 = var3.X;
            }

            if (var3.X > var8) {
                var8 = var3.X;
            }

            if (var3.Y < var6) {
                var6 = var3.Y;
            }

            if (var3.Y > var10) {
                var10 = var3.Y;
            }
        }

        var1.xMin = var4;
        var1.yMin = var6;
        var1.xMax = var8;
        var1.yMax = var10;
        return (var8 - var4) * (var10 - var6);
    }

    public static boolean isClockwise(List<PointD> pointList) {
        double var3 = 0.0D;
        int var5 = 0;

        int var1;
        PointD var2;
        for (var1 = 0; var1 < pointList.size() - 1; ++var1) {
            var2 = (PointD) pointList.get(var1);
            if (var1 == 0) {
                var3 = var2.Y;
                var5 = 0;
            } else if (var3 < var2.Y) {
                var3 = var2.Y;
                var5 = var1;
            }
        }

        var1 = var5 - 1;
        int var8 = var5 + 1;
        if (var5 == 0) {
            var1 = pointList.size() - 2;
        }

        PointD var7 = (PointD) pointList.get(var1);
        var2 = (PointD) pointList.get(var5);
        PointD pointList1;
        if (((pointList1 = (PointD) pointList.get(var8)).X - var7.X) * (var2.Y - var7.Y) - (var2.X - var7.X) * (pointList1.Y - var7.Y) > 0.0D) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean a(Line var0, Line var1) {
        Extent var2 = new Extent();
        Extent var3 = new Extent();
        ArrayList var4 = new ArrayList();
        ArrayList var5 = new ArrayList();
        var4.add(var0.P1);
        var4.add(var0.P2);
        var5.add(var1.P1);
        var5.add(var1.P2);
        a((List) var4, (Extent) var2);
        a((List) var5, (Extent) var3);
        if (!a(var2, var3)) {
            return false;
        } else {
            double var6 = (var1.P1.X - var0.P1.X) * (var0.P2.Y - var0.P1.Y) - (var0.P2.X - var0.P1.X) * (var1.P1.Y - var0.P1.Y);
            double var8 = (var1.P2.X - var0.P1.X) * (var0.P2.Y - var0.P1.Y) - (var0.P2.X - var0.P1.X) * (var1.P2.Y - var0.P1.Y);
            return var6 * var8 <= 0.0D;
        }
    }

    private static boolean a(Extent var0, Extent var1) {
        return var0.xMin <= var1.xMax && var0.xMax >= var1.xMin && var0.yMin <= var1.yMax && var0.yMax >= var1.yMin;
    }

    public static PointF getCrossPoint(PointF aP1, PointF aP2, PointF bP1, PointF bP2) {
        PointF var4 = new PointF(0.0F, 0.0F);
        double var13 = (double) ((bP1.X - aP1.X) * (aP2.Y - aP1.Y) - (aP2.X - aP1.X) * (bP1.Y - aP1.Y));
        double var15 = (double) ((bP2.X - aP1.X) * (aP2.Y - aP1.Y) - (aP2.X - aP1.X) * (bP2.Y - aP1.Y));
        if (var13 == 0.0D) {
            var4 = bP1;
        } else if (var15 == 0.0D) {
            var4 = bP2;
        } else {
            double var9 = (double) ((bP2.X - bP1.X) * (aP1.Y - aP2.Y) - (aP2.X - aP1.X) * (bP1.Y - bP2.Y));
            double var11 = (double) ((aP1.Y - bP1.Y) * (aP2.X - aP1.X) * (bP2.X - bP1.X) + bP1.X * (bP2.Y - bP1.Y) * (aP2.X - aP1.X) - aP1.X * (aP2.Y - aP1.Y) * (bP2.X - bP1.X));
            var4.X = (float) (var11 / var9);
            var9 = (double) ((aP1.X - aP2.X) * (bP2.Y - bP1.Y) - (aP2.Y - aP1.Y) * (bP1.X - bP2.X));
            var11 = (double) (aP2.Y * (aP1.X - aP2.X) * (bP2.Y - bP1.Y) + (bP2.X - aP2.X) * (bP2.Y - bP1.Y) * (aP1.Y - aP2.Y) - bP2.Y * (bP1.X - bP2.X) * (aP2.Y - aP1.Y));
            var4.Y = (float) (var11 / var9);
        }

        return var4;
    }

    private static PointD b(Line var0, Line var1) {
        PointD var2 = new PointD();
        double var11 = (var1.P1.X - var0.P1.X) * (var0.P2.Y - var0.P1.Y) - (var0.P2.X - var0.P1.X) * (var1.P1.Y - var0.P1.Y);
        double var13 = (var1.P2.X - var0.P1.X) * (var0.P2.Y - var0.P1.Y) - (var0.P2.X - var0.P1.X) * (var1.P2.Y - var0.P1.Y);
        if (var11 == 0.0D) {
            var2 = var1.P1;
        } else if (var13 == 0.0D) {
            var2 = var1.P2;
        } else {
            PointD var3 = var0.P1;
            PointD var15 = var0.P2;
            PointD var4 = var1.P1;
            PointD var16;
            double var7 = ((var16 = var1.P2).X - var4.X) * (var3.Y - var15.Y) - (var15.X - var3.X) * (var4.Y - var16.Y);
            double var9 = (var3.Y - var4.Y) * (var15.X - var3.X) * (var16.X - var4.X) + var4.X * (var16.Y - var4.Y) * (var15.X - var3.X) - var3.X * (var15.Y - var3.Y) * (var16.X - var4.X);
            var2.X = var9 / var7;
            var7 = (var3.X - var15.X) * (var16.Y - var4.Y) - (var15.Y - var3.Y) * (var4.X - var16.X);
            var9 = var15.Y * (var3.X - var15.X) * (var16.Y - var4.Y) + (var16.X - var15.X) * (var16.Y - var4.Y) * (var3.Y - var15.Y) - var16.Y * (var4.X - var16.X) * (var15.Y - var3.Y);
            var2.Y = var9 / var7;
        }

        return var2;
    }

    private static List<BorderPoint> c(List<BorderPoint> var0, List<BorderPoint> var1) {
        ArrayList var7 = new ArrayList(var1);

        for (int var3 = 0; var3 < var0.size(); ++var3) {
            BorderPoint var2;
            PointD var6 = (var2 = (BorderPoint) var0.get(var3)).Point;
            PointD var5 = ((BorderPoint) var7.get(0)).Point;

            for (int var4 = 1; var4 < var7.size(); ++var4) {
                PointD var8 = ((BorderPoint) var7.get(var4)).Point;
                if ((var6.X - var5.X) * (var6.X - var8.X) <= 0.0D && (var6.Y - var5.Y) * (var6.Y - var8.Y) <= 0.0D && (var6.X - var5.X) * (var8.Y - var5.Y) - (var8.X - var5.X) * (var6.Y - var5.Y) == 0.0D) {
                    var7.add(var4, var2);
                    break;
                }

                var5 = var8;
            }
        }

        return var7;
    }

    private static List<BorderPoint> a(double[][] var0, List<BorderPoint> var1, Border var2, int[] var3) {
        ArrayList var11 = new ArrayList();
        ArrayList var12 = new ArrayList();
        ArrayList var13 = new ArrayList();

        for (int var7 = 0; var7 < var2.getLineNum(); ++var7) {
            BorderLine var6 = (BorderLine) var2.LineList.get(var7);
            var12.clear();

            BorderPoint var4;
            int var5;
            for (var5 = 0; var5 < var6.pointList.size(); ++var5) {
                (var4 = new BorderPoint()).Id = -1;
                var4.BorderIdx = var7;
                var4.Point = (PointD) var6.pointList.get(var5);
                var4.Value = var0[((IJPoint) var6.ijPointList.get(var5)).I][((IJPoint) var6.ijPointList.get(var5)).J];
                var12.add(var4);
            }

            for (var5 = 0; var5 < var1.size(); ++var5) {
                (var4 = (BorderPoint) ((BorderPoint) var1.get(var5)).clone()).BorderIdx = var7;
                PointD var10 = var4.Point;
                PointD var8 = (PointD) ((BorderPoint) var12.get(0)).Point.clone();

                for (int var14 = 1; var14 < var12.size(); ++var14) {
                    PointD var9 = (PointD) ((BorderPoint) var12.get(var14)).Point.clone();
                    if ((var10.X - var8.X) * (var10.X - var9.X) <= 0.0D && (var10.Y - var8.Y) * (var10.Y - var9.Y) <= 0.0D && (var10.X - var8.X) * (var9.Y - var8.Y) - (var9.X - var8.X) * (var10.Y - var8.Y) == 0.0D) {
                        var12.add(var14, var4);
                        break;
                    }

                    var8 = var9;
                }
            }

            var13.clear();

            for (var5 = 0; var5 < var12.size(); ++var5) {
                (var4 = (BorderPoint) var12.get(var5)).BInnerIdx = var5;
                var13.add(var4);
            }

            var3[var7] = var13.size();
            var11.addAll(var13);
        }

        return var11;
    }

    private static boolean a(double var0, double var2) {
        return Math.abs(var0 / var2 - 1.0D) < 1.0E-11D;
    }
}
