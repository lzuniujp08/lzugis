package com.lzugis.image;

import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by admin on 2017/10/14.
 */
public class Image2Base64 {
    private static String rootPath = System.getProperty("user.dir");
    private static BASE64Encoder encoder = new sun.misc.BASE64Encoder();

    public String getImageBase64Code(String imgpath) {
        File f = new File(imgpath);
        try {
            BufferedImage bi = ImageIO.read(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", baos);
            byte[] bytes = baos.toByteArray();
            return encoder.encodeBuffer(bytes).trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Image2Base64 img2base64 = new Image2Base64();
        String imgPath = rootPath + "\\data\\img\\Marker.png";
        System.out.println(imgPath);
        String base64 = img2base64.getImageBase64Code(imgPath);
        System.out.println(base64);
    }
}
