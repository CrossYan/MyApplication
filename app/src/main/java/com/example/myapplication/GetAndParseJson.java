package com.example.myapplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetAndParseJson {
    //你要訪問的地址
    private String url = "http://t.weather.sojson.com/api/weather/city/101190401";
    public static final int PARSESUCCWSS = 0x2016;
    private Handler handler;

    public GetAndParseJson(Handler handler) {
        this.handler = handler;
    }

    /**
     * 获取网络上的json
     */
    public void getJsonFromInternet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                    conn.setConnectTimeout(3000);
                    conn.setRequestMethod("GET");
                    if (conn.getResponseCode() == 200) {
                        InputStream inputStream = conn.getInputStream();
                        List<News> listNews = parseJson(inputStream);
                        if (listNews.size() > 0) {
                            Message msg = new Message();
                            msg.what = PARSESUCCWSS;//通知UI线程Json解析完成
                            msg.obj = listNews;//将解析出的数据传递给UI线程
                            handler.sendMessage(msg);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * 解析json格式的输入流转换成List
     */
    protected List<News> parseJson(InputStream inputStream) throws Exception {
        List<News> listNews = new ArrayList<News>();
        byte[] jsonBytes = convertIsToByteArray(inputStream);
        String json = new String(jsonBytes);
        try {
//            JSONArray jsonArray=new JSONArray(json);
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jObject=jsonArray.getJSONObject(i);
//                int id=jObject.getInt("id");
            JSONObject jobject = new JSONObject(json);
            JSONObject jsoncityinfo = jobject.getJSONObject("cityInfo");
            JSONObject jsondata = jobject.getJSONObject("data");
            JSONArray jsonarray = jsondata.getJSONArray("forecast");

            String city = "当期城市：" + jsoncityinfo.getString("city");
            String wendu = "气温：" + jsondata.getString("wendu");
            String shidu = "湿度：" + jsondata.getString("shidu");
            String quality = "空气质量：" + jsondata.getString("quality");
            String notice = "温馨提示：" + jsonarray.getJSONObject(4).getString("notice");

            News news = new News(city, wendu, shidu, quality, notice);
            listNews.add(news);
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listNews;
    }

    /**
     * 将输入流转化成ByteArray
     */
    private byte[] convertIsToByteArray(InputStream inputStream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte buffer[] = new byte[1024];
        int length = 0;
        try {
            while ((length = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            inputStream.close();
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
}
