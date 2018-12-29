package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {


    private StringBuffer stringBuffer;
    private BufferedReader bufferedReader;

    private List<News> listNews;
    private ListView list;


    //用來接收GetAndParseJson.PARSESUCCWSS
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case GetAndParseJson.PARSESUCCWSS:
                    listNews = (List<News>) msg.obj;
                    initData();
                    break;
            }


            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        list = findViewById(R.id.listview2);
        GetAndParseJson getAndParseJson = new GetAndParseJson(mHandler);
        getAndParseJson.getJsonFromInternet();//執行它返回一個listNews集合

    }

    /**
     * 将解析后的json填充到ListView
     */
    protected void initData() {
        List<Map<String, Object>> jsonlist = new ArrayList<Map<String, Object>>();
        for (News news : listNews) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("city", news.getCity());
            map.put("wendu", news.getWendu());
            map.put("shidu", news.getShidu());
            map.put("quality", news.getQuality());
            map.put("notice", news.getNotice());

            jsonlist.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, jsonlist, R.layout.activity_listview, new String[]
                {"city", "wendu", "shidu", "quality", "notice"}, new int[]{R.id.textView5, R.id.textView6, R.id.textView7, R.id.textView8, R.id.textView9});
        list.setAdapter(adapter);
    }
//    private String getData() {
//        try {
//            URL url = new URL("http://t.weather.sojson.com/api/weather/city/101190401");//json地址
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");//使用get方法接收
//            InputStream inputStream = connection.getInputStream();//得到一个输入流
//            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTf-8"));
//            String sread = null;
//            while ((sread = bufferedReader.readLine()) != null) {
//                stringBuffer.append(sread);
////                stringBuffer.append("\r\n");
//            }
//
//            inputStream.close();
//            bufferedReader.close();
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return stringBuffer.toString();
//    }

}
