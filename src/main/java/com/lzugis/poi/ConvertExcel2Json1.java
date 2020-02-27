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

public class ConvertExcel2Json1 {
    public static void main(String[] args) {
        String xlsfile = "D:\\project\\2017年\\中石化\\台风路径实况和预报样例数据\\typhoon_forcast.xls";
        String txtfile = "D:\\project\\2017年\\中石化\\台风路径实况和预报样例数据\\typhoon_forcast.txt";
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
                HSSFCell celltime = row.getCell(0);
                HSSFCell celllon = row.getCell(2);
                HSSFCell celllat = row.getCell(1);
                HSSFCell cellmax = row.getCell(4);
                HSSFCell celllow = row.getCell(3);
                String year = celltime.getStringCellValue();
                double lon = celllon.getNumericCellValue();
                double lat = celllat.getNumericCellValue();
                double max = cellmax.getNumericCellValue();
                double low = celllow.getNumericCellValue();
                //{'time':'2017-7-26 14:00','lon':123,'lat':18,'maxspeed':123,'lowpress':123,'radius7':10,'radius10':100,'direction':123,'speed':123},
                sb.append("{'time':'" + year + "','lon':" + lon + ",'lat':" + lat + ",'maxspeed':" + max
                        + ",'lowpress':" + low + "}");
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
