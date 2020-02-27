//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package wContour.Global;

import java.util.ArrayList;
import java.util.List;

public class Border {
    public List<BorderLine> LineList = new ArrayList();

    public Border() {
    }

    public int getLineNum() {
        return this.LineList.size();
    }
}
