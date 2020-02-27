package com.lzugis.netcdf;

import com.lzugis.netcdf.util.NetcdfUtil;

import java.io.IOException;
import java.text.NumberFormat;

public class GetInfoByLonlat {

    public static void main(String[] args){
        String file = "D:\\nctest\\bjdw_yb_ec7d0.25o-efi_201807060800_201807060800_201807130800.nc",
                varStr = "fgi10";
        float lon = 115.74743273316865f,
                lat = 40.10533215172424f;
        double start = System.currentTimeMillis();
        try {
            NetcdfUtil netcdfUtil = new NetcdfUtil(file);
            float val = netcdfUtil.getInfoByLonlat(varStr, lon, lat);
            netcdfUtil.closeNcFile();

            NumberFormat nf = NumberFormat.getInstance();
            nf.setGroupingUsed(false);
            nf.setMaximumFractionDigits(20);

            double end = System.currentTimeMillis();

            System.out.println("共耗时"+(end-start)+"ms, 对应的值为："+nf.format(val));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
