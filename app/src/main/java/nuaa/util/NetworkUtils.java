package nuaa.util;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/21.
 */

public class NetworkUtils {
    public static final String URL_BASE = "http://192.168.103.2:8080/web1/";
    public static final String QUERY_STU_URL = URL_BASE + "QueryAllStuServlet";
    public static final String INSERT_STU_URL = URL_BASE  + "AddStuServlet";

    public static String getDataByURL(String urlStr) throws Exception{
        //建立连接
        URL url = new URL(urlStr);
        //发起请求
        URLConnection conn = url.openConnection();
        //获取服务端返回的流
        InputStream is = conn.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));//BufferedReader类就是一个包装类，它可以包装字符流，将字符流放入缓存里，先把字符读到缓存里，到缓存满了或者你flush的时候，再读入内存，就是为了提供读的效率而设计的。
        //用来连接读取的每一行数据
        StringBuffer sb = new StringBuffer();
        //用来读取每一行数据
        String line = null;
        while((line=br.readLine())!=null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    public static String postDataByURL(String urlStr, Map<String, String> map) throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(urlStr);

        List<NameValuePair> list = new ArrayList<>();
        Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();//迭代器，我们常常使用JDK提供的迭代接口进行java collection的遍历
        while(iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
            list.add(pair);
        }

        post.setEntity(new UrlEncodedFormEntity(list));
        HttpResponse response = client.execute(post);

        return EntityUtils.toString(response.getEntity());
    }

    public static JSONObject getJSONByURL(String urlStr) throws  Exception{
        return new JSONObject(getDataByURL(urlStr));
    }

    public static JSONArray getJSONArrayByURL(String urlStr) throws  Exception{
        return new JSONArray(getDataByURL(urlStr));
    }
}
