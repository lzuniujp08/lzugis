package com.lzugis.dbscan;

/**
 * Dbscan基于密度的聚类算法测试类
 *
 * @author lyq
 */
public class Client {
    public static void main(String[] args) {
        String rootPath = System.getProperty("user.dir");
        String filePath = rootPath + "\\data\\xls\\dbscan.txt";
        //簇扫描半径
        double eps = 5;
        //最小包含点数阈值
        int minPts = 2;
        DBSCANTool tool = new DBSCANTool(filePath, eps, minPts);
        tool.dbScanCluster();
    }
}
