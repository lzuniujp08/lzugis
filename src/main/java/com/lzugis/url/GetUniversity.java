package com.lzugis.url;

import java.io.IOException;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.lzugis.CommonMethod;

public class GetUniversity {
	static String filePath = "d:/universities.txt";
	static CommonMethod cm = new CommonMethod();
	
	public static void main(String[] args){
		int pageSize = 50, total = 20;
		int pageNum = total%50==0?(int)total/pageSize:(int)total/pageSize+1;
		try {
			int flag = 1;
			for(int j=0;j<pageNum;j++){
				int currNum = j+1;
				String url = "http://data.api.gkcx.eol.cn/soudaxue/queryschool.html?messtype=jsonp&callback=lzugis&page="+currNum+"&size="+pageSize;
				JSONObject json = cm.getUrlContent(url);
				JSONArray jsonarr = (JSONArray) json.get("school");
				System.out.println(currNum);
			    for(int i=0, len=jsonarr.length();i<len;i++){
			    	JSONObject _json = (JSONObject) jsonarr.get(i);
			    	StringBuffer _sb = new StringBuffer();
			    	String[] lonlat = cm.getLonLatByName(_json.get("schoolname").toString());
			    	_sb.append(flag+",").append(_json.get("schoolname")).append(",")
			    	  	.append(_json.get("province")).append(",")
			    	  	.append(lonlat[0]).append(",")
			    	  	.append(lonlat[1]).append(",")
			    	  	.append(_json.get("schoolproperty")).append(",")
			    	  	.append(_json.get("rankingCollegetype")).append(",")
			    	  	.append(_json.get("level")).append(",")
			    	  	.append(_json.get("jianjie")).append(",")
			    	  	.append(_json.get("guanwang")).append(",")
			    	  	.append(_json.get("membership")).append(",")
			    	  	.append(_json.get("f985")=="1"?"985":"非985");
			    	cm.append2File(filePath, _sb.toString()); 
			    	flag++;
			    }
			}
		} 
		catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
