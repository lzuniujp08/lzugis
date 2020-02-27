package com.lzugis.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by admin on 2017/9/18.
 */
public class GetWmsImage {
    private static String rootPath = System.getProperty("user.dir");

    public BufferedImage getNetImage(String imgUrl) {
        BufferedImage bi = null;
        try {
            //new一个URL对象
            URL url = new URL(imgUrl);
            //打开链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置请求方式为"GET"
            conn.setRequestMethod("GET");
            //超时响应时间为5秒
            conn.setConnectTimeout(5 * 1000);
            //通过输入流获取图片数据
            InputStream inStream = conn.getInputStream();
            //得到图片的二进制数据，以二进制封装得到数据，具有通用性
            byte[] data = readInputStream(inStream);
            bi = ImageIO.read(new ByteArrayInputStream(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bi;
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        MergeImgMethod mm = new MergeImgMethod();
        GetWmsImage wms = new GetWmsImage();
        String imgUrl = "http://10.16.57.77:8086/geoserver/bj_grid/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=bj_grid%3Abj_county%2Cbj_grid%3Abase_town%2Cbj_grid%3Abj_layer_boundry&SRS=EPSG%3A3857&STYLES=&WIDTH=1537&HEIGHT=677&BBOX=12461782.23255697%2C4698160.662501532%2C13401651.932351498%2C5112143.607694046";
        BufferedImage bi = wms.getNetImage(imgUrl);

        String imgOut = rootPath + "/out/bj_img1.png";
        mm.generateMergeFile(bi, imgOut);

        System.out.println("图片生成完成，共耗时" + (System.currentTimeMillis() - start) + "ms");
    }

}
