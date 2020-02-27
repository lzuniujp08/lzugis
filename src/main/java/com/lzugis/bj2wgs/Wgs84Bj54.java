package com.lzugis.bj2wgs;

public class Wgs84Bj54
{
  public int llon;
  public int llat;
  public double dx;
  public double dy;

  static
  {
    try
    {
      System.load("D:\\lzugis\\code\\lzugis\\lib\\libwgs_bj_lic.so");
    }
    catch (Exception localException)
    {
      System.out.println("load license failed!");
    }
  }

  public native long Wgs2Bj(int paramInt1, int paramInt2);

  public native long Bj2Wgs(double paramDouble1, double paramDouble2);

  public Wgs84Bj54()
  {
    this.llon = 0;
    this.llat = 0;
    this.dx = 0.0D;
    this.dy = 0.0D;
  }

  public Point Wgs84_Bj(Point p)
  {
    Wgs2Bj((int)(p.getX().doubleValue() * 100000.0D), (int)(p.getY().doubleValue() * 100000.0D));
    return new Point(new Double(this.dx), new Double(this.dy));
  }

  public Point WgsBj_84(Point p)
  {
    Bj2Wgs((int)p.getX().doubleValue(), (int)p.getY().doubleValue());
    return new Point(Double.valueOf(new Double(this.llon).doubleValue() / 100000.0D), Double.valueOf(new Double(this.llat).doubleValue() / 100000.0D));
  }

  public static void main(String[] argv)
  {
    Wgs84Bj54 converterObj = new Wgs84Bj54();
    Point bjPoint = new Point(537216.308288, 360539.308757);
    Point wgsPoint = converterObj.WgsBj_84(bjPoint);
    System.out.println(wgsPoint.getX());
    System.out.println(wgsPoint.getY());
  }
}