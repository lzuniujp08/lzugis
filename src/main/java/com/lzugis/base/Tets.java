package com.lzugis.base;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Tets {

    public String readFile(String path) throws IOException {
        File file=new File(path);
        if(!file.exists()||file.isDirectory())
            throw new FileNotFoundException();
        BufferedReader br=new BufferedReader(new FileReader(file));
        String temp=null;
        StringBuffer sb=new StringBuffer();
        temp=br.readLine();
        while(temp!=null){
            sb.append(temp+" ");
            temp=br.readLine();
        }
        return sb.toString();
    }

    public static void main(String[] args){
        Tets test = new Tets();
        String filePath = "D:\\北京市.dat";
        try {
            String content = test.readFile(filePath);
            System.out.println(content);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
