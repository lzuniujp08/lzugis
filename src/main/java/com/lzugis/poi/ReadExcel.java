package com.lzugis.poi;

import com.lzugis.CommonMethod;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.FileInputStream;
import java.io.InputStream;

public class ReadExcel {
    private static String rootPath = System.getProperty("user.dir");
    private CommonMethod cm = new CommonMethod();

    public void getCellValue(HSSFCell cell) {
        if (cell == null) {
            return;
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
            System.out.println("cellType为：CELL_TYPE_BLANK");
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
            System.out.println("cellType为：CELL_TYPE_STRING  值为：" + cell.getRichStringCellValue().getString());
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
            System.out.println("cellType为：CELL_TYPE_NUMERIC  值为：" + cell.getNumericCellValue());
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
            System.out.println("cellType为：CELL_TYPE_BOOLEAN  值为：" + cell.getBooleanCellValue());
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
            System.out.println("cellType为：CELL_TYPE_FORMULA  值为：" + cell.getNumericCellValue() + " 公式为：" + cell.getCellFormula());
        }
        return;
    }

    public void getExcelData(String xlsfile) {
        POIFSFileSystem fs;
        HSSFWorkbook wb;
        HSSFSheet sheet;
        HSSFRow row;

        try {
            InputStream is = new FileInputStream(xlsfile);
            fs = new POIFSFileSystem(is);
            wb = new HSSFWorkbook(fs);
            sheet = wb.getSheetAt(0);

            //获取总列数
            int colNum = sheet.getRow(0).getPhysicalNumberOfCells();
            //读取表头
            row = sheet.getRow(0);
            for (int i = 0; i < colNum; i++) {
                HSSFCell cell = row.getCell(i);
                String cellValue = cell.getRichStringCellValue().getString();
                String cellPinyin = cm.getPinYinHeadChar(cellValue);
                System.out.println(cellValue + "(" + cellPinyin + ")");
            }

            // 得到总行数
            int rowNum = sheet.getLastRowNum();
            for (int i = 1; i <= 1; i++) {
                row = sheet.getRow(i);
                for (int j = 0; j < colNum; j++) {
                    HSSFCell cell = row.getCell(j);
                    getCellValue(cell);
                }
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        ReadExcel readXls = new ReadExcel();
        String xlsfile = rootPath + "/data/xls/floater.xls";
        readXls.getExcelData(xlsfile);
        long end = System.currentTimeMillis();
        System.out.println("Read Done, " + (end - start) + "ms");
    }
}
