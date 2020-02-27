//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package wContour.Global;

public class PointD {
    public double X;
    public double Y;

    public PointD() {
        this.X = 0.0D;
        this.Y = 0.0D;
    }

    public PointD(double x, double y) {
        this.X = x;
        this.Y = y;
    }

    public Object clone() {
        return new PointD(this.X, this.Y);
    }
}
