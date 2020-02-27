package com.lzugis.image;

import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * Created by admin on 2017/9/18.
 */
public class Base642Image {

    private static String rootPath = System.getProperty("user.dir");

    public static void main(String[] args) {
        String imageDataurl = "";
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(imageDataurl);//转码得到图片数据

            ByteArrayInputStream bais = new ByteArrayInputStream(b);
            BufferedImage bi1 = ImageIO.read(bais);

            String outImg = rootPath + "/out/map.png";
            File w2 = new File(outImg);

            ImageIO.write(bi1, "png", w2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
