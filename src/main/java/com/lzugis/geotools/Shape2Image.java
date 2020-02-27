package com.lzugis.geotools;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.SLD;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.swing.JMapFrame;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class Shape2Image {
	private static MapContent map = new MapContent();
	private static String rootPath = System.getProperty("user.dir");

	/**
	 * 添加shp文件
	 * @param shpPath
	 */
	public void addShapeLayer(String shpPath, String sldPath){
		try {
			File file = new File(shpPath);
			ShapefileDataStore shpDataStore = null;
			shpDataStore = new ShapefileDataStore(file.toURL());
			//设置编码
			Charset charset = Charset.forName("GBK");
			shpDataStore.setCharset(charset);
			String typeName = shpDataStore.getTypeNames()[0];
			SimpleFeatureSource featureSource = null;
			featureSource = shpDataStore.getFeatureSource(typeName);

			Style style = SLD.createSimpleStyle(featureSource.getSchema());
			if (sldPath != "") {
				//SLD的方式
				File sldFile = new File(sldPath);
				StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
				SLDParser stylereader = new SLDParser(styleFactory, sldFile.toURI().toURL());
				Style[] stylearray = stylereader.readXML();
				style = stylearray[0];
			} else {
				SLD.setPolyColour(style, Color.RED);
			}

		    Layer layer = new FeatureLayer(featureSource, style);
		    map.addLayer(layer);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 根据四至、长、宽获取地图内容，并生成图片
	 * @param paras
	 * @param imgPath
	 */
	public void getMapContent(Map paras, String imgPath){
		try{
			double[] bbox = (double[]) paras.get("bbox");
			double x1 = bbox[0], y1 = bbox[1], 
				   x2 = bbox[2], y2 = bbox[3];
		    int width = (int) paras.get("width"), 
		    	height=(int) paras.get("height");
		    
		    // 设置输出范围
//			CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
			CoordinateReferenceSystem crs = CRS.decode("EPSG:3857");
			ReferencedEnvelope mapArea = new ReferencedEnvelope(x1, x2, y1, y2, crs);
		    // 初始化渲染器
		    StreamingRenderer sr = new StreamingRenderer();
		    sr.setMapContent(map);
		    // 初始化输出图像
		    BufferedImage bi = new BufferedImage(width, height,
		            BufferedImage.TYPE_INT_ARGB);
		    Graphics g = bi.getGraphics();
		    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		            RenderingHints.VALUE_ANTIALIAS_ON);
		    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		    Rectangle rect = new Rectangle(0, 0, width, height);
		    // 绘制地图
		    sr.paint((Graphics2D) g, rect, mapArea);
		    //将BufferedImage变量写入文件中。 
	        ImageIO.write(bi,"png",new File(imgPath)); 
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 工具类测试方法
	 * @param args
	 */
	public static void main(String[] args){
		long start = System.currentTimeMillis();
		
		Shape2Image shp2img = new Shape2Image();
		String shpPath = rootPath + "/data/shp/globedata_temp.shp";
		String sldPath = rootPath + "/data/sld/world_temp.sld";

		String shpPath1 = rootPath + "/data/shp/res1_4m.shp";
		String sldPath1 = rootPath + "/data/sld/capital.sld";

		String imgPath = rootPath + "/out/world.png";
		Map paras = new HashMap();
		double[] bbox = new double[]{-193.0078125, -40.078125, 76.9921875, 75.9375};
		paras.put("bbox", bbox);
		paras.put("width", 768);
		paras.put("height", 330);

		shp2img.addShapeLayer(shpPath, sldPath);
		shp2img.addShapeLayer(shpPath1, sldPath1);

		JMapFrame.showMap(map);
		
		shp2img.getMapContent(paras, imgPath);
		System.out.println("图片生成完成，共耗时"+(System.currentTimeMillis() - start)+"ms");
	}
}
