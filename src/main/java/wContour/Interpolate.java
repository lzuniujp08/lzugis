//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package wContour;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Interpolate {
    public Interpolate() {
    }

    public static void CreateGridXY_Delt(double Xlb, double Ylb, double Xrt, double Yrt, double XDelt, double YDelt, double[] var12, double[] var13) {
        int var5 = (int) ((Xrt - Xlb) / XDelt + 1.0D);
        int Yrt1 = (int) ((Yrt - Ylb) / YDelt + 1.0D);
        var12 = new double[var5];
        var13 = new double[Yrt1];

        int Xrt1;
        for (Xrt1 = 0; Xrt1 < var5; ++Xrt1) {
            var12[Xrt1] = Xlb + (double) Xrt1 * XDelt;
        }

        for (Xrt1 = 0; Xrt1 < Yrt1; ++Xrt1) {
            var13[Xrt1] = Ylb + (double) Xrt1 * YDelt;
        }

    }

    public static void CreateGridXY_Num(double Xlb, double Ylb, double Xrt, double Yrt, double[] X, double[] Y) {
        int var10 = X.length;
        int var15 = Y.length;
        double var11 = (Xrt - Xlb) / (double) var10;
        double var13 = (Yrt - Ylb) / (double) var15;

        int Xrt1;
        for (Xrt1 = 0; Xrt1 < var10; ++Xrt1) {
            X[Xrt1] = Xlb + (double) Xrt1 * var11;
        }

        for (Xrt1 = 0; Xrt1 < var15; ++Xrt1) {
            Y[Xrt1] = Ylb + (double) Xrt1 * var13;
        }

    }

    public static double[][] Interpolation_IDW_Neighbor(double[][] SCoords, double[] X, double[] Y, int NumberOfNearestNeighbors) {
        int var5 = X.length;
        int var4 = Y.length;
        int var6 = SCoords[0].length;
        double[][] var7 = new double[var4][var5];
        Object[][] var21 = new Object[1][NumberOfNearestNeighbors];

        int var8;
        int var9;
        for (var8 = 0; var8 < var4; ++var8) {
            for (var9 = 0; var9 < var5; ++var9) {
                var7[var8][var9] = -999.0D;
                double var15 = 0.0D;
                double var17 = 0.0D;

                int var10;
                double var13;
                for (var10 = 0; var10 < NumberOfNearestNeighbors; ++var10) {
                    if (Math.pow(X[var9] - SCoords[0][var10], 2.0D) + Math.pow(Y[var8] - SCoords[1][var10], 2.0D) == 0.0D) {
                        var7[var8][var9] = SCoords[2][var10];
                        break;
                    }

                    var13 = 1.0D / (Math.pow(X[var9] - SCoords[0][var10], 2.0D) + Math.pow(Y[var8] - SCoords[1][var10], 2.0D));
                    var21[0][var10] = Double.valueOf(var13);
                    var21[1][var10] = Integer.valueOf(var10);
                }

                if (var7[var8][var9] == -999.0D) {
                    for (var10 = NumberOfNearestNeighbors; var10 < var6; ++var10) {
                        if (Math.pow(X[var9] - SCoords[0][var10], 2.0D) + Math.pow(Y[var8] - SCoords[1][var10], 2.0D) == 0.0D) {
                            var7[var8][var9] = SCoords[2][var10];
                            break;
                        }

                        var13 = 1.0D / (Math.pow(X[var9] - SCoords[0][var10], 2.0D) + Math.pow(Y[var8] - SCoords[1][var10], 2.0D));
                        double var19 = Double.parseDouble(var21[0][0].toString());
                        int var12 = 0;

                        for (int var11 = 1; var11 < NumberOfNearestNeighbors; ++var11) {
                            if (Double.parseDouble(var21[0][var11].toString()) < var19) {
                                var19 = Double.parseDouble(var21[0][var11].toString());
                                var12 = var11;
                            }
                        }

                        if (var13 > var19) {
                            var21[0][var12] = Double.valueOf(var13);
                            var21[1][var12] = Integer.valueOf(var10);
                        }
                    }

                    if (var7[var8][var9] == -999.0D) {
                        for (var10 = 0; var10 < NumberOfNearestNeighbors; ++var10) {
                            var15 += Double.parseDouble(var21[0][var10].toString()) * SCoords[2][Integer.parseInt(var21[1][var10].toString())];
                            var17 += Double.parseDouble(var21[0][var10].toString());
                        }

                        var7[var8][var9] = var15 / var17;
                    }
                }
            }
        }

        for (var8 = 1; var8 < var4 - 1; ++var8) {
            for (var9 = 1; var9 < var5 - 1; ++var9) {
                var7[var8][var9] += 0.125D * (var7[var8 + 1][var9] + var7[var8 - 1][var9] + var7[var8][var9 + 1] + var7[var8][var9 - 1] - 4.0D * var7[var8][var9]);
            }
        }

        return var7;
    }

    public static double[][] Interpolation_IDW_Neighbor(double[][] SCoords, double[] X, double[] Y, int NumberOfNearestNeighbors, double unDefData) {
        int var7 = X.length;
        int var6 = Y.length;
        int var8 = SCoords[0].length;
        double[][] var9 = new double[var6][var7];
        double[] var23 = new double[var8];
        double[][] var24 = new double[2][NumberOfNearestNeighbors];

        int var10;
        int var11;
        for (var10 = 0; var10 < var6; ++var10) {
            for (var11 = 0; var11 < var7; ++var11) {
                var9[var10][var11] = unDefData;
                double var17 = 0.0D;
                double var19 = 0.0D;
                int var13 = 0;

                int var12;
                double var15;
                for (var12 = 0; var12 < var8; ++var12) {
                    if (SCoords[2][var12] == unDefData) {
                        var23[var12] = -1.0D;
                    } else {
                        if (Math.pow(X[var11] - SCoords[0][var12], 2.0D) + Math.pow(Y[var10] - SCoords[1][var12], 2.0D) == 0.0D) {
                            var9[var10][var11] = SCoords[2][var12];
                            break;
                        }

                        var15 = 1.0D / (Math.pow(X[var11] - SCoords[0][var12], 2.0D) + Math.pow(Y[var10] - SCoords[1][var12], 2.0D));
                        var23[var12] = var15;
                        if (var13 < NumberOfNearestNeighbors) {
                            var24[0][var13] = var15;
                            var24[1][var13] = (double) var12;
                        }

                        ++var13;
                    }
                }

                if (var9[var10][var11] == unDefData) {
                    for (var12 = 0; var12 < var8; ++var12) {
                        if ((var15 = var23[var12]) != -1.0D) {
                            double var21 = var24[0][0];
                            int var14 = 0;

                            for (var13 = 1; var13 < NumberOfNearestNeighbors; ++var13) {
                                if (var24[0][var13] < var21) {
                                    var21 = var24[0][var13];
                                    var14 = var13;
                                }
                            }

                            if (var15 > var21) {
                                var24[0][var14] = var15;
                                var24[1][var14] = (double) var12;
                            }
                        }
                    }

                    for (var12 = 0; var12 < NumberOfNearestNeighbors; ++var12) {
                        var17 += var24[0][var12] * SCoords[2][(int) var24[1][var12]];
                        var19 += var24[0][var12];
                    }

                    var9[var10][var11] = var17 / var19;
                }
            }
        }

        for (var10 = 1; var10 < var6 - 1; ++var10) {
            for (var11 = 1; var11 < var7 - 1; ++var11) {
                var9[var10][var11] += 0.125D * (var9[var10 + 1][var11] + var9[var10 - 1][var11] + var9[var10][var11 + 1] + var9[var10][var11 - 1] - 4.0D * var9[var10][var11]);
            }
        }

        return var9;
    }

    public static double[][] Interpolation_IDW_Radius(double[][] SCoords, double[] X, double[] Y, int NeededPointNum, double radius, double unDefData) {
        int var9 = X.length;
        int var8 = Y.length;
        int var10 = SCoords[0].length;
        double[][] var11 = new double[var8][var9];

        int var12;
        int var13;
        for (var12 = 0; var12 < var8; ++var12) {
            for (var13 = 0; var13 < var9; ++var13) {
                var11[var12][var13] = unDefData;
                boolean var22 = false;
                double var18 = 0.0D;
                double var20 = 0.0D;
                int var15 = 0;

                for (int var14 = 0; var14 < var10; ++var14) {
                    if (SCoords[2][var14] != unDefData && SCoords[0][var14] >= X[var13] - radius && SCoords[0][var14] <= X[var13] + radius && SCoords[1][var14] >= Y[var12] - radius && SCoords[1][var14] <= Y[var12] + radius) {
                        if (Math.pow(X[var13] - SCoords[0][var14], 2.0D) + Math.pow(Y[var12] - SCoords[1][var14], 2.0D) == 0.0D) {
                            var11[var12][var13] = SCoords[2][var14];
                            var22 = true;
                            break;
                        }

                        if (Math.sqrt(Math.pow(X[var13] - SCoords[0][var14], 2.0D) + Math.pow(Y[var12] - SCoords[1][var14], 2.0D)) <= radius) {
                            double var16 = 1.0D / (Math.pow(X[var13] - SCoords[0][var14], 2.0D) + Math.pow(Y[var12] - SCoords[1][var14], 2.0D));
                            var20 += var16;
                            var18 += SCoords[2][var14] * var16;
                            ++var15;
                        }
                    }
                }

                if (!var22 && var15 >= NeededPointNum) {
                    var11[var12][var13] = var18 / var20;
                }
            }
        }

        for (var12 = 1; var12 < var8 - 1; ++var12) {
            for (var13 = 1; var13 < var9 - 2; ++var13) {
                if (var11[var12][var13] != unDefData && var11[var12 + 1][var13] != unDefData && var11[var12 - 1][var13] != unDefData && var11[var12][var13 + 1] != unDefData && var11[var12][var13 - 1] != unDefData) {
                    var11[var12][var13] += 0.125D * (var11[var12 + 1][var13] + var11[var12 - 1][var13] + var11[var12][var13 + 1] + var11[var12][var13 - 1] - 4.0D * var11[var12][var13]);
                }
            }
        }

        return var11;
    }

    public static double[][] Interpolation_Grid(double[][] GridData, double[] X, double[] Y, double unDefData, double[] var5, double[] var6) {
        int var7 = (X.length << 1) - 1;
        int var8 = (Y.length << 1) - 1;
        var5 = new double[var7];
        var6 = new double[var8];
        double[][] var9 = new double[var8][var7];
        new ArrayList();
        ArrayList var11 = null;

        int var10;
        for (var10 = 0; var10 < var7; ++var10) {
            if (var10 % 2 == 0) {
                var5[var10] = X[var10 / 2];
            } else {
                var5[var10] = (X[(var10 - 1) / 2] + X[(var10 - 1) / 2 + 1]) / 2.0D;
            }
        }

        for (var10 = 0; var10 < var8; ++var10) {
            if (var10 % 2 == 0) {
                var6[var10] = Y[var10 / 2];
            } else {
                var6[var10] = (Y[(var10 - 1) / 2] + Y[(var10 - 1) / 2 + 1]) / 2.0D;
            }

            for (int X1 = 0; X1 < var7; ++X1) {
                if (var10 % 2 == 0 && X1 % 2 == 0) {
                    var9[var10][X1] = GridData[var10 / 2][X1 / 2];
                } else {
                    double var12;
                    double var14;
                    if (var10 % 2 == 0 && X1 % 2 != 0) {
                        var12 = GridData[var10 / 2][(X1 - 1) / 2];
                        var14 = GridData[var10 / 2][(X1 - 1) / 2 + 1];
                        var11 = new ArrayList();
                        if (var12 != unDefData) {
                            var11.add(Double.valueOf(var12));
                        }

                        if (var14 != unDefData) {
                            var11.add(Double.valueOf(var14));
                        }

                        if (var11.isEmpty()) {
                            var9[var10][X1] = unDefData;
                        } else if (var11.size() == 1) {
                            var9[var10][X1] = ((Double) var11.get(0)).doubleValue();
                        } else {
                            var9[var10][X1] = (var12 + var14) / 2.0D;
                        }
                    } else if (var10 % 2 != 0 && X1 % 2 == 0) {
                        var12 = GridData[(var10 - 1) / 2][X1 / 2];
                        var14 = GridData[(var10 - 1) / 2 + 1][X1 / 2];
                        var11 = new ArrayList();
                        if (var12 != unDefData) {
                            var11.add(Double.valueOf(var12));
                        }

                        if (var14 != unDefData) {
                            var11.add(Double.valueOf(var14));
                        }

                        if (var11.isEmpty()) {
                            var9[var10][X1] = unDefData;
                        } else if (var11.size() == 1) {
                            var9[var10][X1] = ((Double) var11.get(0)).doubleValue();
                        } else {
                            var9[var10][X1] = (var12 + var14) / 2.0D;
                        }
                    } else {
                        var12 = GridData[(var10 - 1) / 2][(X1 - 1) / 2];
                        var14 = GridData[(var10 - 1) / 2][(X1 - 1) / 2 + 1];
                        double var16 = GridData[(var10 - 1) / 2 + 1][(X1 - 1) / 2 + 1];
                        double var18 = GridData[(var10 - 1) / 2 + 1][(X1 - 1) / 2];
                        var11 = new ArrayList();
                        if (var12 != unDefData) {
                            var11.add(Double.valueOf(var12));
                        }

                        if (var14 != unDefData) {
                            var11.add(Double.valueOf(var14));
                        }

                        if (var16 != unDefData) {
                            var11.add(Double.valueOf(var16));
                        }

                        if (var18 != unDefData) {
                            var11.add(Double.valueOf(var18));
                        }

                        if (var11.isEmpty()) {
                            var9[var10][X1] = unDefData;
                        } else if (var11.size() == 1) {
                            var9[var10][X1] = ((Double) var11.get(0)).doubleValue();
                        } else {
                            double var21 = 0.0D;

                            double var24;
                            for (Iterator var27 = var11.iterator(); var27.hasNext(); var21 += var24) {
                                var24 = ((Double) var27.next()).doubleValue();
                            }

                            var9[var10][X1] = var21 / (double) var11.size();
                        }
                    }
                }
            }
        }

        return var9;
    }

    public static double[][] Cressman(double[][] stationData, double[] X, double[] Y, double unDefData) {
        ArrayList var5;
        (var5 = new ArrayList()).add(Double.valueOf(10.0D));
        var5.add(Double.valueOf(7.0D));
        var5.add(Double.valueOf(4.0D));
        var5.add(Double.valueOf(2.0D));
        var5.add(Double.valueOf(1.0D));
        return Cressman(stationData, X, Y, unDefData, var5);
    }

    public static double[][] Cressman(double[][] stData, double[] X, double[] Y, double unDefData, List<Double> radList) {
        int var6 = X.length;
        int var7 = Y.length;
        int var8 = stData[0].length;
        double[][] var9 = new double[var7][var6];
        int var10 = radList.size();
        double var13 = X[0];
        double var15 = 0.0D;
        double var17 = Y[0];
        double var19 = 0.0D;
        double var21 = X[1] - X[0];
        double var23 = Y[1] - Y[0];
        double var29 = 0.0D;
        boolean Y1 = false;
        double[][] var11 = new double[3][var8];

        double var25;
        double var27;
        int X1;
        for (X1 = 0; X1 < var8; ++X1) {
            var25 = stData[0][X1];
            var27 = stData[1][X1];
            var11[0][X1] = (var25 - var13) / var21;
            var11[1][X1] = (var27 - var17) / var23;
            var11[2][X1] = stData[2][X1];
        }

        double[][] var12 = new double[var7][var6];
        double[][] var73 = new double[var7][var6];

        int stData1;
        for (X1 = 0; X1 < var7; ++X1) {
            for (stData1 = 0; stData1 < var6; ++stData1) {
                var12[X1][stData1] = -9.999E20D;
                var73[X1][stData1] = 9.999E20D;
            }
        }

        double var35;
        if (radList.size() > 0) {
            var35 = ((Double) radList.get(0)).doubleValue();
        } else {
            var35 = 4.0D;
        }

        int var22;
        double var38;
        double var40;
        int Y2;
        for (X1 = 0; X1 < var7; ++X1) {
            var17 = (var27 = (double) X1) - var35;
            var19 = var27 + var35;

            for (stData1 = 0; stData1 < var6; ++stData1) {
                var13 = (var25 = (double) stData1) - var35;
                var15 = var25 + var35;
                Y2 = 0;
                var29 = 0.0D;

                for (var22 = 0; var22 < var8; ++var22) {
                    var38 = var11[2][var22];
                    var40 = var11[0][var22];
                    double var42 = var11[1][var22];
                    if (var40 >= 0.0D && var40 < (double) (var6 - 1) && var42 >= 0.0D && var42 < (double) (var7 - 1) && var38 != unDefData && var40 >= var13 && var40 <= var15 && var42 >= var17 && var42 <= var19 && Math.sqrt(Math.pow(var40 - var25, 2.0D) + Math.pow(var42 - var27, 2.0D)) <= var35) {
                        var29 += var38;
                        ++Y2;
                        if (var12[X1][stData1] < var38) {
                            var12[X1][stData1] = var38;
                        }

                        if (var73[X1][stData1] > var38) {
                            var73[X1][stData1] = var38;
                        }
                    }
                }

                if (Y2 == 0) {
                    var9[X1][stData1] = unDefData;
                } else {
                    var9[X1][stData1] = var29 / (double) Y2;
                }
            }
        }

        for (var22 = 0; var22 < var10; ++var22) {
            var35 = ((Double) radList.get(var22)).doubleValue();

            for (X1 = 0; X1 < var7; ++X1) {
                var17 = (var27 = (double) X1) - var35;
                var19 = var27 + var35;

                for (stData1 = 0; stData1 < var6; ++stData1) {
                    if (var9[X1][stData1] != unDefData) {
                        var13 = (var25 = (double) stData1) - var35;
                        var15 = var25 + var35;
                        var29 = 0.0D;
                        var38 = 0.0D;

                        for (int var76 = 0; var76 < var8; ++var76) {
                            double var41 = var11[2][var76];
                            double var43 = var11[0][var76];
                            double var45 = var11[1][var76];
                            double var47;
                            if (var43 >= 0.0D && var43 < (double) (var6 - 1) && var45 >= 0.0D && var45 < (double) (var7 - 1) && var41 != unDefData && var43 >= var13 && var43 <= var15 && var45 >= var17 && var45 <= var19 && (var47 = Math.sqrt(Math.pow(var43 - var25, 2.0D) + Math.pow(var45 - var27, 2.0D))) <= var35) {
                                Y2 = (int) var45;
                                int var74 = (int) var43;
                                int var24 = Y2 + 1;
                                int var31 = var74 + 1;
                                double var53 = var9[Y2][var74];
                                double var55 = var9[Y2][var31];
                                double var57 = var9[var24][var74];
                                double var59 = var9[var24][var31];
                                ArrayList var75 = new ArrayList();
                                if (var53 != unDefData) {
                                    var75.add(Double.valueOf(var53));
                                }

                                if (var55 != unDefData) {
                                    var75.add(Double.valueOf(var55));
                                }

                                if (var57 != unDefData) {
                                    var75.add(Double.valueOf(var57));
                                }

                                if (var59 != unDefData) {
                                    var75.add(Double.valueOf(var59));
                                }

                                if (!var75.isEmpty()) {
                                    double var62;
                                    double var64;
                                    double var77;
                                    if (var75.size() == 1) {
                                        var62 = ((Double) var75.get(0)).doubleValue();
                                    } else if (var75.size() <= 3) {
                                        var64 = 0.0D;

                                        double var67;
                                        for (Iterator var66 = var75.iterator(); var66.hasNext(); var64 += var67) {
                                            var67 = ((Double) var66.next()).doubleValue();
                                        }

                                        var62 = var64 / (double) var75.size();
                                    } else {
                                        var64 = var53 + (var57 - var53) * (var45 - (double) Y2);
                                        var77 = var55 + (var59 - var55) * (var45 - (double) Y2);
                                        var62 = var64 + (var77 - var64) * (var43 - (double) var74);
                                    }

                                    var64 = var41 - var62;
                                    var77 = (var35 * var35 - var47 * var47) / (var35 * var35 + var47 * var47);
                                    var29 += var64 * var77;
                                    var38 += var77;
                                }
                            }
                        }

                        if (var38 < 1.0E-6D) {
                            var9[X1][stData1] = unDefData;
                        } else {
                            var40 = var9[X1][stData1] + var29 / var38;
                            var9[X1][stData1] = Math.max(var73[X1][stData1], Math.min(var12[X1][stData1], var40));
                        }
                    }
                }
            }
        }

        return var9;
    }

    public static double[][] AssignPointToGrid(double[][] SCoords, double[] X, double[] Y, double unDefData) {
        int var6 = X.length;
        int var5 = Y.length;
        int var7 = SCoords[0].length;
        double[][] var8 = new double[var5][var6];
        double var9 = X[1] - X[0];
        double var11 = Y[1] - Y[0];
        int[][] var13 = new int[var5][var6];

        int var14;
        int var15;
        for (var14 = 0; var14 < var5; ++var14) {
            for (var15 = 0; var15 < var6; ++var15) {
                var13[var14][var15] = 0;
                var8[var14][var15] = 0.0D;
            }
        }

        for (var14 = 0; var14 < var7; ++var14) {
            if (Math.abs(SCoords[2][var14] / unDefData - 1.0D) >= 1.0E-11D) {
                double var25 = SCoords[0][var14];
                double var17 = SCoords[1][var14];
                if (var25 >= X[0] && var25 <= X[var6 - 1] && var17 >= Y[0] && var17 <= Y[var5 - 1]) {
                    var15 = (int) ((var25 - X[0]) / var9);
                    int var16 = (int) ((var17 - Y[0]) / var11);
                    ++var13[var16][var15];
                    var8[var16][var15] += SCoords[2][var14];
                }
            }
        }

        for (var14 = 0; var14 < var5; ++var14) {
            for (var15 = 0; var15 < var6; ++var15) {
                if (var13[var14][var15] == 0) {
                    var8[var14][var15] = unDefData;
                } else {
                    var8[var14][var15] /= (double) var13[var14][var15];
                }
            }
        }

        return var8;
    }
}
