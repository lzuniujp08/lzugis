package com.lzugis.image;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Svg2Png {
  public static void main(String[] args) {
		String strImg = "<svg xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.1\" class=\"highcharts-root\" style=\"font-family:&quot;Lucida Grande&quot;, &quot;Lucida Sans Unicode&quot;, Arial, Helvetica, sans-serif;font-size:12px;\" xmlns=\"http://www.w3.org/2000/svg\" width=\"600\" height=\"400\" viewBox=\"0 0 600 400\"><desc>Created with Highcharts 7.0.3</desc><defs><clipPath id=\"highcharts-4rjobgv-56\"><rect x=\"0\" y=\"0\" width=\"466\" height=\"312\" fill=\"none\"></rect></clipPath></defs><rect fill=\"#ffffff\" class=\"highcharts-background\" x=\"0\" y=\"0\" width=\"600\" height=\"400\" rx=\"0\" ry=\"0\"></rect><rect fill=\"none\" class=\"highcharts-plot-background\" x=\"67\" y=\"51\" width=\"466\" height=\"312\"></rect><g class=\"highcharts-grid highcharts-xaxis-grid \" data-z-index=\"1\"><path fill=\"none\" data-z-index=\"1\" class=\"highcharts-grid-line\" d=\"M 133.5 51 L 133.5 363\" opacity=\"1\"></path><path fill=\"none\" data-z-index=\"1\" class=\"highcharts-grid-line\" d=\"M 199.5 51 L 199.5 363\" opacity=\"1\"></path><path fill=\"none\" data-z-index=\"1\" class=\"highcharts-grid-line\" d=\"M 266.5 51 L 266.5 363\" opacity=\"1\"></path><path fill=\"none\" data-z-index=\"1\" class=\"highcharts-grid-line\" d=\"M 332.5 51 L 332.5 363\" opacity=\"1\"></path><path fill=\"none\" data-z-index=\"1\" class=\"highcharts-grid-line\" d=\"M 399.5 51 L 399.5 363\" opacity=\"1\"></path><path fill=\"none\" data-z-index=\"1\" class=\"highcharts-grid-line\" d=\"M 465.5 51 L 465.5 363\" opacity=\"1\"></path><path fill=\"none\" data-z-index=\"1\" class=\"highcharts-grid-line\" d=\"M 532.5 51 L 532.5 363\" opacity=\"1\"></path><path fill=\"none\" data-z-index=\"1\" class=\"highcharts-grid-line\" d=\"M 66.5 51 L 66.5 363\" opacity=\"1\"></path></g><g class=\"highcharts-grid highcharts-yaxis-grid \" data-z-index=\"1\"><path fill=\"none\" data-z-index=\"1\" class=\"highcharts-grid-line\" d=\"M 67 363.5 L 533 363.5\" opacity=\"1\"></path><path fill=\"none\" data-z-index=\"1\" class=\"highcharts-grid-line\" d=\"M 67 301.5 L 533 301.5\" opacity=\"1\"></path><path fill=\"none\" data-z-index=\"1\" class=\"highcharts-grid-line\" d=\"M 67 238.5 L 533 238.5\" opacity=\"1\"></path><path fill=\"none\" data-z-index=\"1\" class=\"highcharts-grid-line\" d=\"M 67 176.5 L 533 176.5\" opacity=\"1\"></path><path fill=\"none\" data-z-index=\"1\" class=\"highcharts-grid-line\" d=\"M 67 113.5 L 533 113.5\" opacity=\"1\"></path><path fill=\"none\" data-z-index=\"1\" class=\"highcharts-grid-line\" d=\"M 67 51.5 L 533 51.5\" opacity=\"1\"></path></g><g class=\"highcharts-grid highcharts-yaxis-grid \" data-z-index=\"1\"><path fill=\"none\" data-z-index=\"1\" class=\"highcharts-grid-line\" d=\"M 67 363.5 L 533 363.5\" opacity=\"1\"></path><path fill=\"none\" data-z-index=\"1\" class=\"highcharts-grid-line\" d=\"M 67 301.5 L 533 301.5\" opacity=\"1\"></path><path fill=\"none\" data-z-index=\"1\" class=\"highcharts-grid-line\" d=\"M 67 238.5 L 533 238.5\" opacity=\"1\"></path><path fill=\"none\" data-z-index=\"1\" class=\"highcharts-grid-line\" d=\"M 67 176.5 L 533 176.5\" opacity=\"1\"></path><path fill=\"none\" data-z-index=\"1\" class=\"highcharts-grid-line\" d=\"M 67 113.5 L 533 113.5\" opacity=\"1\"></path><path fill=\"none\" data-z-index=\"1\" class=\"highcharts-grid-line\" d=\"M 67 51.5 L 533 51.5\" opacity=\"1\"></path></g><rect fill=\"none\" class=\"highcharts-plot-border\" data-z-index=\"1\" x=\"67\" y=\"51\" width=\"466\" height=\"312\"></rect><g class=\"highcharts-axis highcharts-xaxis \" data-z-index=\"2\"><path fill=\"none\" class=\"highcharts-tick\" stroke=\"#ccd6eb\" stroke-width=\"1\" d=\"M 133.5 363 L 133.5 373\" opacity=\"1\"></path><path fill=\"none\" class=\"highcharts-tick\" stroke=\"#ccd6eb\" stroke-width=\"1\" d=\"M 199.5 363 L 199.5 373\" opacity=\"1\"></path><path fill=\"none\" class=\"highcharts-tick\" stroke=\"#ccd6eb\" stroke-width=\"1\" d=\"M 266.5 363 L 266.5 373\" opacity=\"1\"></path><path fill=\"none\" class=\"highcharts-tick\" stroke=\"#ccd6eb\" stroke-width=\"1\" d=\"M 332.5 363 L 332.5 373\" opacity=\"1\"></path><path fill=\"none\" class=\"highcharts-tick\" stroke=\"#ccd6eb\" stroke-width=\"1\" d=\"M 399.5 363 L 399.5 373\" opacity=\"1\"></path><path fill=\"none\" class=\"highcharts-tick\" stroke=\"#ccd6eb\" stroke-width=\"1\" d=\"M 465.5 363 L 465.5 373\" opacity=\"1\"></path><path fill=\"none\" class=\"highcharts-tick\" stroke=\"#ccd6eb\" stroke-width=\"1\" d=\"M 533.5 363 L 533.5 373\" opacity=\"1\"></path><path fill=\"none\" class=\"highcharts-tick\" stroke=\"#ccd6eb\" stroke-width=\"1\" d=\"M 66.5 363 L 66.5 373\" opacity=\"1\"></path><path fill=\"none\" class=\"highcharts-axis-line\" stroke=\"#ccd6eb\" stroke-width=\"1\" data-z-index=\"7\" d=\"M 67 363.5 L 533 363.5\"></path></g><g class=\"highcharts-axis highcharts-yaxis \" data-z-index=\"2\"><text x=\"26.09375\" data-z-index=\"7\" text-anchor=\"middle\" transform=\"translate(0,0) rotate(270 26.09375 207)\" class=\"highcharts-axis-title\" style=\"color:#666666;fill:#666666;\" y=\"207\"><tspan>累计降水(mm)</tspan></text><path fill=\"none\" class=\"highcharts-axis-line\" data-z-index=\"7\" d=\"M 67 51 L 67 363\"></path></g><g class=\"highcharts-axis highcharts-yaxis \" data-z-index=\"2\"><text x=\"573.90625\" data-z-index=\"7\" text-anchor=\"middle\" transform=\"translate(0,0) rotate(90 573.90625 207)\" class=\"highcharts-axis-title\" style=\"color:#666666;fill:#666666;\" y=\"207\"><tspan>离散度(mm)</tspan></text><path fill=\"none\" class=\"highcharts-axis-line\" data-z-index=\"7\" d=\"M 533 51 L 533 363\"></path></g><g class=\"highcharts-series-group\" data-z-index=\"3\"><g data-z-index=\"0.1\" class=\"highcharts-series highcharts-series-2 highcharts-column-series \" transform=\"translate(67,51) scale(1 1)\" clip-path=\"url(#highcharts-4rjobgv-56)\"><rect x=\"17.5\" y=\"295.5\" width=\"30\" height=\"17\" fill=\"#2884ff\" rx=\"2\" ry=\"2\" stroke=\"#ffffff\" stroke-width=\"1\" class=\"highcharts-point\"></rect><rect x=\"84.5\" y=\"280.5\" width=\"30\" height=\"32\" fill=\"#2884ff\" rx=\"2\" ry=\"2\" stroke=\"#ffffff\" stroke-width=\"1\" class=\"highcharts-point\"></rect><rect x=\"150.5\" y=\"252.5\" width=\"30\" height=\"60\" fill=\"#2884ff\" rx=\"2\" ry=\"2\" stroke=\"#ffffff\" stroke-width=\"1\" class=\"highcharts-point\"></rect><rect x=\"217.5\" y=\"250.5\" width=\"30\" height=\"62\" fill=\"#2884ff\" rx=\"2\" ry=\"2\" stroke=\"#ffffff\" stroke-width=\"1\" class=\"highcharts-point\"></rect><rect x=\"284.5\" y=\"60.5\" width=\"30\" height=\"252\" fill=\"#2884ff\" rx=\"2\" ry=\"2\" stroke=\"#ffffff\" stroke-width=\"1\" class=\"highcharts-point\"></rect><rect x=\"350.5\" y=\"231.5\" width=\"30\" height=\"81\" fill=\"#2884ff\" rx=\"2\" ry=\"2\" stroke=\"#ffffff\" stroke-width=\"1\" class=\"highcharts-point\"></rect><rect x=\"417.5\" y=\"274.5\" width=\"30\" height=\"38\" fill=\"#2884ff\" rx=\"2\" ry=\"2\" stroke=\"#ffffff\" stroke-width=\"1\" class=\"highcharts-point\"></rect></g><g data-z-index=\"0.1\" class=\"highcharts-markers highcharts-series-2 highcharts-column-series \" transform=\"translate(67,51) scale(1 1)\" clip-path=\"none\"></g><g data-z-index=\"2\" class=\"highcharts-series highcharts-series-0 highcharts-line-series \" transform=\"translate(67,51) scale(1 1)\" clip-path=\"url(#highcharts-4rjobgv-56)\"><path fill=\"none\" d=\"M 33.285714285714 255.82596 L 99.857142857143 186.80532 L 166.42857142857 239.87652 L 233 244.21488 L 299.57142857143 35.59449919999997 L 366.14285714286 238.89371584 L 432.71428571429 256.75260000000003\" class=\"highcharts-graph\" data-z-index=\"1\" stroke=\"#08a98b\" stroke-width=\"2\" stroke-dasharray=\"2,2\"></path></g><g data-z-index=\"2\" class=\"highcharts-markers highcharts-series-0 highcharts-line-series \" transform=\"translate(67,51) scale(1 1)\" clip-path=\"none\"><path fill=\"#08a98b\" d=\"M 37 256 A 4 4 0 1 1 36.99999800000017 255.99600000066667 Z\" class=\"highcharts-point\"></path><path fill=\"#08a98b\" d=\"M 103 187 A 4 4 0 1 1 102.99999800000016 186.99600000066667 Z\" class=\"highcharts-point\"></path><path fill=\"#08a98b\" d=\"M 170 240 A 4 4 0 1 1 169.99999800000018 239.99600000066667 Z\" class=\"highcharts-point\"></path><path fill=\"#08a98b\" d=\"M 237 244 A 4 4 0 1 1 236.99999800000018 243.99600000066667 Z\" class=\"highcharts-point\"></path><path fill=\"#08a98b\" d=\"M 303 36 A 4 4 0 1 1 302.9999980000002 35.99600000066666 Z\" class=\"highcharts-point\"></path><path fill=\"#08a98b\" d=\"M 370 239 A 4 4 0 1 1 369.9999980000002 238.99600000066667 Z\" class=\"highcharts-point\"></path><path fill=\"#08a98b\" d=\"M 436 257 A 4 4 0 1 1 435.9999980000002 256.99600000066664 Z\" class=\"highcharts-point\"></path></g><g data-z-index=\"2\" class=\"highcharts-series highcharts-series-1 highcharts-line-series \" transform=\"translate(67,51) scale(1 1)\" clip-path=\"url(#highcharts-4rjobgv-56)\"><path fill=\"none\" d=\"M 33.285714285714 256.96319688 L 99.857142857143 206.64384 L 166.42857142857 286.39104 L 233 299.98175896 L 299.57142857143 253.14432312 L 366.14285714286 305.37312 L 432.71428571429 299.30784\" class=\"highcharts-graph\" data-z-index=\"1\" stroke=\"#F89608\" stroke-width=\"2\" stroke-linejoin=\"round\" stroke-linecap=\"round\"></path></g><g data-z-index=\"2\" class=\"highcharts-markers highcharts-series-1 highcharts-line-series \" transform=\"translate(67,51) scale(1 1)\" clip-path=\"none\"><path fill=\"#F89608\" d=\"M 33 253 L 37 257 33 261 29 257 Z\" class=\"highcharts-point\"></path><path fill=\"#F89608\" d=\"M 99 203 L 103 207 99 211 95 207 Z\" class=\"highcharts-point\"></path><path fill=\"#F89608\" d=\"M 166 282 L 170 286 166 290 162 286 Z\" class=\"highcharts-point\"></path><path fill=\"#F89608\" d=\"M 233 296 L 237 300 233 304 229 300 Z\" class=\"highcharts-point\"></path><path fill=\"#F89608\" d=\"M 299 249 L 303 253 299 257 295 253 Z\" class=\"highcharts-point\"></path><path fill=\"#F89608\" d=\"M 366 301 L 370 305 366 309 362 305 Z\" class=\"highcharts-point\"></path><path fill=\"#F89608\" d=\"M 432 295 L 436 299 432 303 428 299 Z\" class=\"highcharts-point\"></path></g></g><text x=\"300\" text-anchor=\"middle\" class=\"highcharts-title\" data-z-index=\"4\" style=\"color:#333333;font-size:18px;fill:#333333;\" y=\"24\"></text><text x=\"300\" text-anchor=\"middle\" class=\"highcharts-subtitle\" data-z-index=\"4\" style=\"color:#666666;fill:#666666;\" y=\"24\"></text><g class=\"highcharts-legend\" data-z-index=\"7\" transform=\"translate(334,10)\"><rect fill=\"none\" class=\"highcharts-legend-box\" rx=\"0\" ry=\"0\" x=\"0\" y=\"0\" width=\"256\" height=\"29\" visibility=\"visible\"></rect><g data-z-index=\"1\"><g><g class=\"highcharts-legend-item highcharts-line-series highcharts-color-undefined highcharts-series-0\" data-z-index=\"1\" transform=\"translate(8,3)\"><path fill=\"none\" d=\"M 0 11 L 16 11\" class=\"highcharts-graph\" stroke-dasharray=\"2,2\" stroke=\"#08a98b\" stroke-width=\"2\"></path><path fill=\"#08a98b\" d=\"M 12 11 A 4 4 0 1 1 11.999998000000167 10.996000000666664 Z\" class=\"highcharts-point\"></path><text x=\"21\" style=\"color:#333333;cursor:pointer;font-size:12px;font-weight:bold;fill:#333333;\" text-anchor=\"start\" data-z-index=\"2\" y=\"15\"><tspan>集合平均</tspan></text></g><g class=\"highcharts-legend-item highcharts-line-series highcharts-color-undefined highcharts-series-1\" data-z-index=\"1\" transform=\"translate(98,3)\"><path fill=\"none\" d=\"M 0 11 L 16 11\" class=\"highcharts-graph\" stroke=\"#F89608\" stroke-width=\"2\"></path><path fill=\"#F89608\" d=\"M 8 7 L 12 11 8 15 4 11 Z\" class=\"highcharts-point\"></path><text x=\"21\" y=\"15\" style=\"color:#333333;cursor:pointer;font-size:12px;font-weight:bold;fill:#333333;\" text-anchor=\"start\" data-z-index=\"2\"><tspan>集合中值</tspan></text></g><g class=\"highcharts-legend-item highcharts-column-series highcharts-color-undefined highcharts-series-2\" data-z-index=\"1\" transform=\"translate(189,3)\"><text x=\"21\" y=\"15\" style=\"color:#333333;cursor:pointer;font-size:12px;font-weight:bold;fill:#333333;\" text-anchor=\"start\" data-z-index=\"2\"><tspan>离散度</tspan></text><rect x=\"2\" y=\"4\" width=\"12\" height=\"12\" fill=\"#2884ff\" rx=\"6\" ry=\"6\" class=\"highcharts-point\" data-z-index=\"3\"></rect></g></g></g></g><g class=\"highcharts-axis-labels highcharts-xaxis-labels \" data-z-index=\"7\"><text x=\"100.28571428571573\" style=\"color:#666666;cursor:default;font-size:11px;fill:#666666;\" text-anchor=\"middle\" transform=\"translate(0,0)\" y=\"382\" opacity=\"1\">4月2候</text><text x=\"166.85714285714573\" style=\"color:#666666;cursor:default;font-size:11px;fill:#666666;\" text-anchor=\"middle\" transform=\"translate(0,0)\" y=\"382\" opacity=\"1\">4月3候</text><text x=\"233.4285714285757\" style=\"color:#666666;cursor:default;font-size:11px;fill:#666666;\" text-anchor=\"middle\" transform=\"translate(0,0)\" y=\"382\" opacity=\"1\">4月4候</text><text x=\"299.99999999999574\" style=\"color:#666666;cursor:default;font-size:11px;fill:#666666;\" text-anchor=\"middle\" transform=\"translate(0,0)\" y=\"382\" opacity=\"1\">4月5候</text><text x=\"366.5714285714257\" style=\"color:#666666;cursor:default;font-size:11px;fill:#666666;\" text-anchor=\"middle\" transform=\"translate(0,0)\" y=\"382\" opacity=\"1\">4月6候</text><text x=\"433.14285714285575\" style=\"color:#666666;cursor:default;font-size:11px;fill:#666666;\" text-anchor=\"middle\" transform=\"translate(0,0)\" y=\"382\" opacity=\"1\">5月1候</text><text x=\"499.7142857142857\" style=\"color:#666666;cursor:default;font-size:11px;fill:#666666;\" text-anchor=\"middle\" transform=\"translate(0,0)\" y=\"382\" opacity=\"1\">5月2候</text></g><g class=\"highcharts-axis-labels highcharts-yaxis-labels \" data-z-index=\"7\"><text x=\"52\" style=\"color:#666666;cursor:default;font-size:11px;fill:#666666;\" text-anchor=\"end\" transform=\"translate(0,0)\" y=\"367\" opacity=\"1\">0</text><text x=\"52\" style=\"color:#666666;cursor:default;font-size:11px;fill:#666666;\" text-anchor=\"end\" transform=\"translate(0,0)\" y=\"305\" opacity=\"1\">6</text><text x=\"52\" style=\"color:#666666;cursor:default;font-size:11px;fill:#666666;\" text-anchor=\"end\" transform=\"translate(0,0)\" y=\"242\" opacity=\"1\">12</text><text x=\"52\" style=\"color:#666666;cursor:default;font-size:11px;fill:#666666;\" text-anchor=\"end\" transform=\"translate(0,0)\" y=\"180\" opacity=\"1\">18</text><text x=\"52\" style=\"color:#666666;cursor:default;font-size:11px;fill:#666666;\" text-anchor=\"end\" transform=\"translate(0,0)\" y=\"117\" opacity=\"1\">24</text><text x=\"52\" style=\"color:#666666;cursor:default;font-size:11px;fill:#666666;\" text-anchor=\"end\" transform=\"translate(0,0)\" y=\"55\" opacity=\"1\">30</text></g><g class=\"highcharts-axis-labels highcharts-yaxis-labels \" data-z-index=\"7\"><text x=\"548\" style=\"color:#666666;cursor:default;font-size:11px;fill:#666666;\" text-anchor=\"start\" transform=\"translate(0,0)\" y=\"367\" opacity=\"1\">0</text><text x=\"548\" style=\"color:#666666;cursor:default;font-size:11px;fill:#666666;\" text-anchor=\"start\" transform=\"translate(0,0)\" y=\"305\" opacity=\"1\">15</text><text x=\"548\" style=\"color:#666666;cursor:default;font-size:11px;fill:#666666;\" text-anchor=\"start\" transform=\"translate(0,0)\" y=\"242\" opacity=\"1\">30</text><text x=\"548\" style=\"color:#666666;cursor:default;font-size:11px;fill:#666666;\" text-anchor=\"start\" transform=\"translate(0,0)\" y=\"180\" opacity=\"1\">45</text><text x=\"548\" style=\"color:#666666;cursor:default;font-size:11px;fill:#666666;\" text-anchor=\"start\" transform=\"translate(0,0)\" y=\"117\" opacity=\"1\">60</text><text x=\"548\" style=\"color:#666666;cursor:default;font-size:11px;fill:#666666;\" text-anchor=\"start\" transform=\"translate(0,0)\" y=\"55\" opacity=\"1\">75</text></g></svg>";
//		GenerateImage(strImg);

//		base64ToImage(strImg, "c:/test5.png");

    try {
      convertToPng(strImg, "c:/test6.png");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (TranscoderException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  // 图片转化成base64字符串
  public static String GetImageStr() {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
    String imgFile = "C:\\Users\\admin\\Desktop\\aaa.svg";// 待处理的图片
    InputStream in = null;
    byte[] data = null;
    // 读取图片字节数组
    try {
      in = new FileInputStream(imgFile);
      data = new byte[in.available()];
      in.read(data);
      in.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    // 对字节数组Base64编码
    BASE64Encoder encoder = new BASE64Encoder();
    return encoder.encode(data);// 返回Base64编码过的字节数组字符串
  }

  // base64字符串转化成图片
  public static boolean GenerateImage(String imgStr) { // 对字节数组字符串进行Base64解码并生成图片
    if (imgStr == null) // 图像数据为空
      return false;
    BASE64Decoder decoder = new BASE64Decoder();
    try {
      // Base64解码
      byte[] b = decoder.decodeBuffer(imgStr);
      for (int i = 0; i < b.length; ++i) {
        if (b[i] < 0) {// 调整异常数据
          b[i] += 256;
        }
      }
      // 生成jpeg图片
      String imgFilePath = "C:/test22.svg";// 新生成的图片
      OutputStream out = new FileOutputStream(imgFilePath);
      out.write(b);
      out.flush();
      out.close();
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * @Descriptionmap 对字节数组字符串进行Base64解码并生成图片
   * @author temdy
   * @Date 2015-01-26
   * @param base64 图片Base64数据
   * @param path 图片路径
   * @return
   */
  public static boolean base64ToImage(String base64, String path) {// 对字节数组字符串进行Base64解码并生成图片
    if (base64 == null){ // 图像数据为空
      return false;
    }
    BASE64Decoder decoder = new BASE64Decoder();
    try {
      // Base64解码
      byte[] bytes = decoder.decodeBuffer(base64);
      for (int i = 0; i < bytes.length; ++i) {
        if (bytes[i] < 0) {// 调整异常数据
          bytes[i] += 256;
        }
      }
      // 生成jpeg图片
      OutputStream out = new FileOutputStream(path);
      out.write(bytes);
      out.flush();
      out.close();
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 将svg字符串转换为png
   *
   * @param svgCode svg代码
   * @param pngFilePath 保存的路径
   * @throws TranscoderException svg代码异常
   * @throws IOException io错误
   */
  public static void convertToPng(String svgCode, String pngFilePath) throws IOException,
    TranscoderException {

    File file = new File(pngFilePath);

    FileOutputStream outputStream = null;
    try {
      file.createNewFile();
      outputStream = new FileOutputStream(file);
      convertToPng(svgCode, outputStream);
    } finally {
      if (outputStream != null) {
        try {
          outputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * 将svgCode转换成png文件，直接输出到流中
   *
   * @param svgCode svg代码
   * @param outputStream 输出流
   * @throws TranscoderException 异常
   * @throws IOException io异常
   */
  public static void convertToPng(String svgCode, OutputStream outputStream)
    throws TranscoderException, IOException {
    try {
      // utf-8 解码
//            byte[] bytes = svgCode.getBytes("utf-8");

      // Base64解码
      BASE64Decoder decoder = new BASE64Decoder();
      byte[] bytes = decoder.decodeBuffer(svgCode);
      for (int i = 0; i < bytes.length; ++i) {
        if (bytes[i] < 0) {// 调整异常数据
          bytes[i] += 256;
        }
      }

      // 根据上面byte[]数组 生成 png 图片
      PNGTranscoder t = new PNGTranscoder();
      TranscoderInput input = new TranscoderInput(new ByteArrayInputStream(bytes));
      TranscoderOutput output = new TranscoderOutput(outputStream);
      t.transcode(input, output);
      outputStream.flush();
    } finally {
      if (outputStream != null) {
        try {
          outputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
