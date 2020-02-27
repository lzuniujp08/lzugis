package com.lzugis.conndb;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;

import org.bson.Document;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geojson.feature.FeatureJSON;
import org.opengis.feature.simple.SimpleFeature;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ConnMongo {
    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();

        final String IP_ADDRESS = "127.0.0.1"; // 本机地址
        final String DB_NAME = "lzugis"; // 数据库名称
        final String COLLECTION_NAME = "capital"; // Collection名称
        final String SHAPE_FILE = "D:/data/wgs84/capital.shp"; // ShapeFile全路径

        // 初始化mongodb
        MongoClient client = new MongoClient(IP_ADDRESS);
        MongoDatabase db = client.getDatabase(DB_NAME);
        db.createCollection(COLLECTION_NAME);
        MongoCollection<Document> coll = db.getCollection(COLLECTION_NAME);

        // 使用GeoTools读取ShapeFile文件
        File shapeFile = new File(SHAPE_FILE);
        ShapefileDataStore store = new ShapefileDataStore(shapeFile.toURI().toURL());

        //设置编码
        Charset charset = Charset.forName("GBK");
        store.setCharset(charset);

        SimpleFeatureSource sfSource = store.getFeatureSource();
        SimpleFeatureIterator sfIter = sfSource.getFeatures().features();
        // 从ShapeFile文件中遍历每一个Feature，然后将Feature转为GeoJSON字符串，最后将字符串插入到mongodb的Collection中
        while (sfIter.hasNext()) {
            SimpleFeature feature = (SimpleFeature) sfIter.next();
            // Feature转GeoJSON
            FeatureJSON fjson = new FeatureJSON();
            StringWriter writer = new StringWriter();
            fjson.writeFeature(feature, writer);
            String sjson = writer.toString();
            // 插入到Collection中
            Document doc = Document.parse(sjson);
            coll.insertOne(doc);
        }
        client.close(); // 关闭数据库连接
        System.out.println("数据导入完成，共耗时"+(System.currentTimeMillis() - start)+"ms");
    }
}
