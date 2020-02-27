package com.lzugis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class CommonMethod {
	/**
	 * 获取文件内容
	 * @param filePath
	 * @return
	 */
	public String getFileContent(String filePath){
		StringBuffer sb = new StringBuffer();
		try {
            String encoding="GBK";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    sb.append(lineTxt);
                }
                read.close();
		    }
            else{
		        System.out.println("找不到指定的文件");
		    }
	    } 
		catch (Exception e) {
	        System.out.println("读取文件内容出错");
	        e.printStackTrace();
	    }
		return sb.toString();
	}
	public void append2File(String file, String content) {
        FileWriter fw = null;

        try {
        	//如果文件存在，则追加内容；如果文件不存在，则创建文件
            File f = new File(file);
            fw = new FileWriter(f, true);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }

        PrintWriter pw = new PrintWriter(fw);
        pw.println(content);
        pw.flush();

        try {
            fw.flush();
            pw.close();
            fw.close();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
	public JSONObject getUrl2JSON(String url) throws IOException, JSONException{
		JSONObject json = null;
		InputStream is = null;
		try {
			is = new URL(url).openStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		    StringBuilder sb = new StringBuilder();
		    int cp;
		    while ((cp = rd.read()) != -1) {
		        sb.append((char) cp);
		    }
		    json = new JSONObject(sb.toString());
		    is.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public JSONObject getUrlContent(String url) throws IOException, JSONException{
		JSONObject json = null;
		InputStream is = null;
		try {
			is = new URL(url).openStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		    StringBuilder sb = new StringBuilder();
		    int cp;
		    while ((cp = rd.read()) != -1) {
		        sb.append((char) cp);
		    }
//			String strJson = sb.toString().substring(7,sb.toString().length()-2);
            String strJson = sb.toString();
            json = new JSONObject(strJson);
		    is.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}
	public String[] getLonLatByName(String name){
		String[] lonlat = new String[]{"99","99"};
		StringBuffer url = new StringBuffer();
		url.append("http://api.tianditu.com/apiserver/ajaxproxy?proxyReqUrl=")
				.append("http://map.tianditu.com/query.shtml?postStr={'keyWord':'"+name+"',")
				.append("'level':'9','mapBound':'114.6089,39.5392,118.7040,40.9562','queryType':'7','start':'0','count':'1'}&type=query");
		System.out.println(url);
		String bdUrl = "http://api.map.baidu.com/?qt=gc&wd=%E6%96%B0%E7%AB%99%E5%8C%BA%E8%83%9C%E5%88%A9%E8%B7%AF89&cn=%E4%B8%AD%E5%9B%BD&fromproduct=jsapi&res=api&callback=lzugis&ak=DD279b2a90afdf0ae7a3796787a0742e";
		InputStream is = null;
		try {
			is = new URL(url.toString()).openStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		    StringBuilder sb = new StringBuilder();
		    int cp;
		    while ((cp = rd.read()) != -1) {
			    sb.append((char) cp);
			}
			String strJson = sb.toString().substring(19,sb.toString().length()-1);
		    JSONObject json = new JSONObject(strJson);
		    JSONArray arr = new JSONArray();
		    if(!json.isNull("pois")){
		    	arr = json.getJSONArray("pois");
		    	JSONObject poiinfo = (JSONObject) arr.get(0);
			    lonlat = poiinfo.get("lonlat").toString().split(" ");
			    is.close();
		    }
		}
		catch (IOException | JSONException e) {
	    	e.printStackTrace();
	    }
	    return lonlat;
	}

	public double getAngleBy2Point(double x1,double y1,double x2,double y2){
		double cosfi = 0, fi = 0, norm = 0;  
		
		double x3 = 180,y3 = y1<y2?y1:y2;
	    double dsx = x1 - x2;  
	    double dsy = y1 - y2;  
	    double dex = x2 - x3;  
	    double dey = y2 - y3;  
	  
	    cosfi = dsx * dex + dsy * dey;  
	    norm = (dsx * dsx + dsy * dsy) * (dex * dex + dey * dey);  
	    cosfi /= Math.sqrt(norm);  
	  
	    if (cosfi >= 1.0) return 0;  
	    if (cosfi <= -1.0) return Math.PI;  
	    fi = Math.acos(cosfi);
	    
	    double angle = 0;
	    if (180 * fi / Math.PI < 180){  
	        angle = 180 * fi / Math.PI ;  
	    }  
	    else{  
	    	angle = 360 - 180 * fi / Math.PI;  
	    } 
	    if(y2<y1){
	    	angle = 0-angle;
	    }
	    return angle;
	}

	/**
	 * 将汉字转换为全拼
	 *
	 * @param src
	 * @return
	 */
	public String getPingYin(String src) {
		char[] t1 = null;
		t1 = src.toCharArray();
		String[] t2 = new String[t1.length];
		HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();

		t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		t3.setVCharType(HanyuPinyinVCharType.WITH_V);
		String t4 = "";
		int t0 = t1.length;
		try {
			for (int i = 0; i < t0; i++) {
				// 判断是否为汉字字符
				if (java.lang.Character.toString(t1[i]).matches(
						"[\\u4E00-\\u9FA5]+")) {
					t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
					t4 += t2[0];
				} else
					t4 += java.lang.Character.toString(t1[i]);
			}
			// System.out.println(t4);
			return t4;
		} catch (BadHanyuPinyinOutputFormatCombination e1) {
			e1.printStackTrace();
		}
		return t4;
	}

	/**
	 * 返回中文的首字母
	 *
	 * @param str
	 * @return
	 */
	public String getPinYinHeadChar(String str) {
		String convert = "";
		for (int j = 0; j < str.length(); j++) {
			char word = str.charAt(j);
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert += pinyinArray[0].charAt(0);
			} else {
				convert += word;
			}
		}
		return convert;
	}

	/**
	 * 将字符串转移为ASCII码
	 *
	 * @param cnStr
	 * @return
	 */
	public String getCnASCII(String cnStr) {
		StringBuffer strBuf = new StringBuffer();
		byte[] bGBK = cnStr.getBytes();
		for (int i = 0; i < bGBK.length; i++) {
			strBuf.append(Integer.toHexString(bGBK[i] & 0xff));
		}
		return strBuf.toString();
	}

	public static void main(String[] args) {
		CommonMethod cm = new CommonMethod();
        String name = "北京大学";
        try {
            String[] lonlat = cm.getLonLatByName(name);
            System.out.println(lonlat[0]+", "+lonlat[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
