package com.lzugis.poi;

import com.amazonaws.util.json.JSONArray;
import com.lzugis.CommonMethod;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.FileInputStream;
import java.io.InputStream;

public class ConvertExcel2Json {
    public static void main(String[] args) {
        String xlsfile = "D:\\project\\2017年\\中石化\\台风路径实况和预报样例数据\\typhoon_actural.xls";
        String txtfile = "D:\\project\\2017年\\中石化\\台风路径实况和预报样例数据\\typhoon_actural.txt";
        CommonMethod cm = new CommonMethod();
        POIFSFileSystem fs;
        HSSFWorkbook wb;
        HSSFSheet sheet;
        HSSFRow row;
        try {
            InputStream is = new FileInputStream(xlsfile);
            fs = new POIFSFileSystem(is);
            wb = new HSSFWorkbook(fs);
            sheet = wb.getSheetAt(0);
            // 得到总行数
            int rowNum = sheet.getLastRowNum();
            StringBuffer sb = new StringBuffer();
            sb.append("[");
            for (int i = 0; i <= rowNum; i++) {
                row = sheet.getRow(i);
                HSSFCell cellyear = row.getCell(0);
                HSSFCell cellmonth = row.getCell(1);
                HSSFCell cellday = row.getCell(2);
                HSSFCell cellhour = row.getCell(3);
                HSSFCell celllon = row.getCell(5);
                HSSFCell celllat = row.getCell(6);
                HSSFCell cellmax = row.getCell(7);
                HSSFCell celllow = row.getCell(8);
                HSSFCell cellr7 = row.getCell(9);
                HSSFCell cellr10 = row.getCell(10);
                HSSFCell celld = row.getCell(11);
                HSSFCell cells = row.getCell(12);
                double year = cellyear.getNumericCellValue();
                double month = cellmonth.getNumericCellValue();
                double day = cellday.getNumericCellValue();
                double hour = cellhour.getNumericCellValue();
                double lon = celllon.getNumericCellValue();
                double lat = celllat.getNumericCellValue();
                double max = cellmax.getNumericCellValue();
                double low = celllow.getNumericCellValue();
                double r7 = cellr7.getNumericCellValue();
                double r10 = cellr10.getNumericCellValue();
                double d = celld.getNumericCellValue();
                double s = cells.getNumericCellValue();
                String time = (int) year + "-" + (int) month + "-" + (int) day + " " + (int) hour;
                //{'time':'2017-7-26 14:00','lon':123,'lat':18,'maxspeed':123,'lowpress':123,'radius7':10,'radius10':100,'direction':123,'speed':123},
                sb.append("{'time':'" + time + "','lon':" + lon + ",'lat':" + lat + ",'maxspeed':" + max
                        + ",'lowpress':" + low + ",'radius7':" + r7 + ",'radius10':" + r10 + ",'direction':+" + d
                        + ",'speed':" + s + "}");
                if (i != rowNum) {
                    sb.append(",");
                }
            }
            sb.append("]");
            is.close();

            JSONArray jsonarr = new JSONArray(sb.toString());
            System.out.println(jsonarr.toString());
            cm.append2File(txtfile, jsonarr.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
