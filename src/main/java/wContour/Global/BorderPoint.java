//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package wContour.Global;

public class BorderPoint {
    public int Id;
    public int BorderIdx;
    public int BInnerIdx;
    public PointD Point = new PointD();
    public double Value;

    public BorderPoint() {
    }

    public Object clone() {
        BorderPoint var1;
        (var1 = new BorderPoint()).Id = this.Id;
        var1.BorderIdx = this.BorderIdx;
        var1.BInnerIdx = this.BInnerIdx;
        var1.Point = this.Point;
        var1.Value = this.Value;
        return var1;
    }
}
