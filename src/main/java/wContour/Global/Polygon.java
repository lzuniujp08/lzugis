//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package wContour.Global;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import wContour.Contour;

public class Polygon {
    public boolean IsBorder;
    public boolean IsInnerBorder = false;
    public double LowValue;
    public double HighValue;
    public boolean IsClockWise;
    public int StartPointIdx;
    public boolean IsHighCenter;
    public Extent Extent = new Extent();
    public double Area;
    public PolyLine OutLine = new PolyLine();
    public List<PolyLine> HoleLines = new ArrayList();
    public int HoleIndex;

    public Polygon() {
    }

    public Object Clone() {
        Polygon var1;
        (var1 = new Polygon()).IsBorder = this.IsBorder;
        var1.LowValue = this.LowValue;
        var1.HighValue = this.HighValue;
        var1.IsClockWise = this.IsClockWise;
        var1.StartPointIdx = this.StartPointIdx;
        var1.IsHighCenter = this.IsHighCenter;
        var1.Extent = this.Extent;
        var1.Area = this.Area;
        var1.OutLine = this.OutLine;
        var1.HoleLines = new ArrayList(this.HoleLines);
        var1.HoleIndex = this.HoleIndex;
        return var1;
    }

    public boolean HasHoles() {
        return this.HoleLines.size() > 0;
    }

    public void AddHole(Polygon aPolygon) {
        this.HoleLines.add(aPolygon.OutLine);
    }

    public void AddHole(List<PointD> pList) {
        if (Contour.isClockwise(pList)) {
            Collections.reverse(pList);
        }

        PolyLine var2;
        (var2 = new PolyLine()).PointList = pList;
        this.HoleLines.add(var2);
    }
}
