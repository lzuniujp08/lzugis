//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package wContour.Global;

public class Extent {
    public double xMin;
    public double yMin;
    public double xMax;
    public double yMax;

    public Extent() {
    }

    public Extent(double minX, double maxX, double minY, double maxY) {
        this.xMin = minX;
        this.xMax = maxX;
        this.yMin = minY;
        this.yMax = maxY;
    }

    public boolean Include(Extent bExtent) {
        return this.xMin <= bExtent.xMin && this.xMax >= bExtent.xMax && this.yMin <= bExtent.yMin && this.yMax >= bExtent.yMax;
    }
}
