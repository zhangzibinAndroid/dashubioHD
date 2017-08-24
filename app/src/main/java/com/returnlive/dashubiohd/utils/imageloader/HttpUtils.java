package com.returnlive.dashubiohd.utils.imageloader;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 作者： 张梓彬
 * 日期： 2017/6/22 0022
 * 时间： 上午 10:21
 * 描述： 网络加载工具类
 */

public class HttpUtils {
    private static HttpUtils instance;

    public HttpUtils() {
    }

    //单例模式
    public static HttpUtils getInstance() {
        if (instance == null) {
            synchronized (HttpUtils.class) {
                if (instance == null) {
                    instance = new HttpUtils();
                }
            }
        }
        return instance;
    }


    //通过网络访问，返回byte;
    public byte[] getByteArrayFromWeb(String path) {
        byte[] b = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setConnectTimeout(5000);
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                baos = new ByteArrayOutputStream();
                is = connection.getInputStream();
                byte[] tmp = new byte[1024];
                int length = 0;
                while ((length = is.read(tmp)) != -1) {
                    baos.write(tmp, 0, length);
                }
            }


            b = baos.toByteArray();
            Log.e("ZZZ", "getByteArrayFromWeb: "+b );

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }

                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return b;
    }
}
