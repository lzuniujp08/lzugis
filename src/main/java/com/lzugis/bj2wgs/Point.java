package com.lzugis.bj2wgs;

public class Point
{
  private Double x = Double.valueOf(0.0D);
  private Double y = Double.valueOf(0.0D);

  public Point(Double x, Double y)
  {
    this.x = x;
    this.y = y;
  }

  public Point(int x, int y)
  {
    this.x = Double.valueOf(x * 1.0D);
    this.y = Double.valueOf(y * 1.0D);
  }

  public Double getX()
  {
    return this.x;
  }

  public void setX(Double x)
  {
    this.x = x;
  }

  public Double getY()
  {
    return this.y;
  }

  public void setY(Double y)
  {
    this.y = y;
  }
}
