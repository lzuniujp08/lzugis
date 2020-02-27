package com.lzugis.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by admin on 2017/9/18.
 */
public class MergeImgMethod {

    private static String rootPath = System.getProperty("user.dir");

    /**
     * @param img1  源文件(图片)
     * @param img2  水印文件(图片)
     * @return BufferedImage
     * @throws IOException
     * @Title: 构造图片
     * @Description: 生成水印并返回java.awt.image.BufferedImage
     */
    public BufferedImage mergeImage(String img1, String img2) throws IOException {
        //获取第一个图片
        BufferedImage buffImg = ImageIO.read(new File(img1));
        // 创建Graphics2D对象，用在底图对象上绘图
        Graphics2D g2d = buffImg.createGraphics();
        int imgWidth = buffImg.getWidth();// 获取层图的宽度
        int imgHeight = buffImg.getHeight();// 获取层图的高度

        // 获取层图
        BufferedImage overImg = ImageIO.read(new File(img2));
        // 添加蒙版
        g2d.drawImage(overImg, 0, 0, imgWidth, imgHeight, null);

        //设置颜色
        g2d.setColor(Color.BLACK);

        //地图标题
        g2d.setFont(new Font("宋体", Font.BOLD, 25));
        g2d.drawString("2017年9月18日温度图", imgWidth / 2 - 100, 30);

        //数据来源说明
        g2d.setFont(new Font("隶书", Font.PLAIN, 16));
        g2d.drawString("数据来源：FY2号星PM25", imgWidth / 2 - 100, imgHeight - 50);
        g2d.drawString("制图单位：国家气象预报中心", imgWidth / 2 - 100, imgHeight - 25);

        g2d.dispose();// 释放图形上下文使用的系统资源
        return buffImg;
    }
    /**
     * 输出水印图片
     *
     * @param buffImg  图像加水印之后的BufferedImage对象
     * @param savePath 图像加水印之后的保存路径
     */
    public void generateMergeFile(BufferedImage buffImg, String savePath) {
        int temp = savePath.lastIndexOf(".") + 1;
        try {
            ImageIO.write(buffImg, savePath.substring(temp), new File(savePath));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MergeImgMethod mm = new MergeImgMethod();

        String img1 = rootPath + "/data/img/bj_wind.png";
        String img2 = rootPath + "/data/img/bj_modal.png";
        String imgOut = rootPath + "/out/bj_img.png";
        long start = System.currentTimeMillis();
        try {
            BufferedImage bi = mm.mergeImage(img1, img2);
            mm.generateMergeFile(bi, imgOut);
            System.out.println("图片生成完成，共耗时" + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
