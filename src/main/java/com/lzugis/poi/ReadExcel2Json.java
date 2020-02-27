package com.lzugis.poi;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.amazonaws.util.json.JSONArray;
import com.lzugis.CommonMethod;

public class ReadExcel2Json {
	public static void main(String[] args){
		String xlsfile = "D:\\project\\2017年\\玖天气象\\legend.xls";
		String txtfile = "D:\\project\\2017年\\玖天气象\\legend.txt";
		CommonMethod cm = new CommonMethod();
		POIFSFileSystem fs;
	    HSSFWorkbook wb;
	    HSSFSheet sheet;
	    HSSFRow row;
		try{
			InputStream is = new FileInputStream(xlsfile);
			fs = new POIFSFileSystem(is);
            wb = new HSSFWorkbook(fs);
            sheet = wb.getSheetAt(0);
            // 得到总行数
            int rowNum = sheet.getLastRowNum();
            StringBuffer sb = new StringBuffer();
            sb.append("[");
            for(int i=0;i<=rowNum;i++){
            	row = sheet.getRow(i);
            	HSSFCell cell0 = row.getCell(0);
            	HSSFCell cell1 = row.getCell(1);
            	String lable = cell0.getStringCellValue();
            	String color = cell1.getStringCellValue();
            	//{'label':'≥50','value':'≥50','color':'#660000'},
            	sb.append("{'label':'"+lable+"','value':'"+lable+"','color':'#"+color+"'}");
            	if(i!=rowNum){
            		sb.append(",");
            	}
            }
            sb.append("]");
            is.close();
            
            JSONArray jsonarr = new JSONArray(sb.toString());
//            System.out.println(jsonarr.toString());
            
            //转置对象
            JSONArray jsonArray = new JSONArray();
            for(int i=0;i<jsonarr.length();i++){
            	jsonArray.put(jsonarr.length()-1-i, jsonarr.get(i));
            }
            System.out.println(jsonArray.toString());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
