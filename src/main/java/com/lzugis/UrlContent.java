package com.lzugis;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.UUID;

public class UrlContent {
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

	public Object[] getCoordinate(String addr) throws IOException {
		String lng = null;//经度
		String lat = null;//纬度
		String address = null;
		try {
			address = java.net.URLEncoder.encode(addr, "UTF-8");
		}catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String key = "f247cdb592eb43ebac6ccd27f796e2d2";
		String url = String .format("http://api.map.baidu.com/geocoder?address=%s&output=json&key=%s", address, key);
		URL myURL = null;
		URLConnection httpsConn = null;
		try {
			myURL = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		InputStreamReader insr = null;
		BufferedReader br = null;
		try {
			httpsConn = (URLConnection) myURL.openConnection();// 不使用代理
			if (httpsConn != null) {
				insr = new InputStreamReader( httpsConn.getInputStream(), "UTF-8");
				br = new BufferedReader(insr);
				String data = null;
				int count = 1;
				while((data= br.readLine())!=null){
					if(count==5){
						lng = (String)data.subSequence(data.indexOf(":")+1, data.indexOf(","));//经度
						count++;
					}else if(count==6){
						lat = data.substring(data.indexOf(":")+1);//纬度
						count++;
					}else{
						count++;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(insr!=null){
				insr.close();
			}
			if(br!=null){
				br.close();
			}
		}
		return new Object[]{lng,lat};
	}

	public String getUrlContent(String url) throws IOException{
		InputStream is = null;
		String content = "";
		try {
			is = new URL(url).openStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		    StringBuilder sb = new StringBuilder();
		    int cp;
		    while ((cp = rd.read()) != -1) {
		        sb.append((char) cp);
		    }
			content = sb.toString();
		    is.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	public static void main(String[] args) throws Exception {
		UrlContent uc = new UrlContent();
		long start = System.currentTimeMillis();

		/*FileWriter fw = null;
		String output = "d:/gangkou.txt";
		File f = new File(output);
		fw = new FileWriter(f, true);
		PrintWriter pw = new PrintWriter(fw);

		try {
			int pageNum = 118;
			for(int i=1;i<=pageNum;i++){
				String url = "https://www.gangkoudaima.com/Search/"+i+"?keywords=";
				String content = uc.getUrlContent(url);
				Document doc = Jsoup.parse(content);
				Elements tabContent = doc.getElementsByClass("result-grid");
				for(int j=0;j<tabContent.size();j++){
					Element table = tabContent.get(j);
					pw.println(table.toString());
				}
				System.out.println("第"+i+"页写入完成");
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			pw.flush();
			pw.close();
		}*/

//		Object[] lonLat = uc.getCoordinate("纳尼斯维克");
//		System.out.println(lonLat[0]+","+lonLat[1]);
		String uuid = UUID.randomUUID().toString();
		System.out.println(uuid);
		System.out.println("共耗时"+(System.currentTimeMillis() - start)+"ms");
    }
}
