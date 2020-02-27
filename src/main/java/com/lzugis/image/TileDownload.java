package com.lzugis.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by admin on 2017/9/18.
 */
public class TileDownload {
    private static String rootPath = System.getProperty("user.dir");

    public void saveImage(int z, int x, int y) {
        String imgUrl = "http://mt3.google.cn/vt/lyrs=t@131&hl=zh-CN&gl=cn&x=" + x + "&y=" + y + "&z=" + z + "&s=Ga";
        String offPath = rootPath + File.separator + "out" + File.separator +
                "google" + File.separator + z + File.separator + x + File.separator;
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

            //判断文件夹是否存在，否则创建
            File fileOffPath = new File(offPath);
            if (!fileOffPath.exists()) {
                fileOffPath.mkdirs();
            }

            String savePath = offPath + y + ".png";
            ImageIO.write(bi, "png", new File(savePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        TileDownload tile = new TileDownload();
        for (int z = 0; z < 5; z++) {
            System.out.println(z);
            int _x = (int) Math.pow(2, (double) z);
            int _y = _x;
            for (int x = 0; x < _x; x++) {
                System.out.println("|-----" + x);
                for (int y = 0; y < _y; y++) {
                    System.out.println("      |-----" + y + ".png");
                    tile.saveImage(z, x, y);
                }
            }
        }
        System.out.println("图片缓存完成，共耗时" + (System.currentTimeMillis() - start) + "ms");
    }
}
