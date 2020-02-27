package com.lzugis.url;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONObject;
import com.lzugis.CommonMethod;

public class GetChargingPile {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CommonMethod cm = new CommonMethod();
		String file = "d://chargingpile.txt";
		int pageSize = 20, total = 894;
		int pageNum = total%50==0?(int)total/pageSize:(int)total/pageSize+1;
		try{
			cm.append2File(file, "districtname,districtcode,latitude,longitude,name,address,picinfo");
			System.out.println("totalnum="+pageNum);
			for(int i=0;i<pageNum;i++){
				String url = "http://ditu.amap.com/service/poiInfo?query_type=TQUERY&pagesize="+pageSize+"&pagenum="+(i+1)
					+"&zoom=8&city=110000&keywords=%E5%85%85%E7%94%B5%E6%A1%A9";
				System.out.println("pagenum="+(i+1));
				JSONObject json = cm.getUrl2JSON(url);
				JSONArray arry = (JSONArray)((JSONObject)json.get("data")).get("poi_list");
				for(int j=0;j<arry.length();j++){
					JSONObject _chargPile = (JSONObject)arry.get(j);
					StringBuffer sb = new StringBuffer();
					//"districtname,latitude,longitude,name,address,pic_info"
					sb.append(_chargPile.get("districtname").toString()).append(",")
						.append(_chargPile.get("districtcode").toString()).append(",")
						.append(_chargPile.get("latitude").toString()).append(",")
						.append(_chargPile.get("longitude").toString()).append(",")
						.append(_chargPile.get("name").toString()).append(",")
						.append(_chargPile.get("address").toString()).append(",")
						.append(_chargPile.get("pic_info").toString());
					cm.append2File(file, sb.toString());
				}
			}
			System.out.println(file);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
