package com.lzugis.conndb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.lzugis.CommonMethod;


public class ConnPostgres {
	static CommonMethod cm = new CommonMethod();
    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        try {
            String url = "jdbc:postgresql://localhost:5432/lzugis"; 
            String user = "postgres";
            String password = "root";
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("是否成功连接pg数据库"+connection);

//            String sql = "select st_astext(st_transform(geom, 3857)) as wkt from henans";
            String sql = "select st_astext(geom) as wkt from beijing";
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                System.out.println(resultSet.getString("wkt"));
                cm.append2File("d:\\3.txt", resultSet.getString("wkt"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
