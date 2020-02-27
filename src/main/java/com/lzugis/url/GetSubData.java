package com.lzugis.url;

import java.io.IOException;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.lzugis.CommonMethod;

public class GetSubData {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String subsfile = "d:\\subway.txt";
		CommonMethod cm = new CommonMethod();
		String statUrl = "http://map.amap.com/service/subway?_1497246967299&srhdata=1100_drw_beijing.json";
		String lineUrl = "http://map.amap.com/service/subway?_1497246967303&srhdata=1100_info_beijing.json";
		try {
			JSONObject stations = cm.getUrlContent(statUrl);
		} 
		catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
