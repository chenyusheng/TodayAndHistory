package ysheng.todayandhistory.Util;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by redlimit-web01 on 2016/9/18.
 */
public class Util_NetTool {
    private static OkHttpClient client;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    static String TAG = "tag";
    static Call currentCall;

    public static  Call getCurrentCall() {
        return currentCall;
    }

    public static OkHttpClient getClient() {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
//                    cache(new Cache cache).
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();
        }
        return client;
    }


    //用json参数发送post请求
    public static String JsonPost(final String path, final JSONObject json) {
        BufferedReader in = null;
        String result = "";
        OutputStream os = null;
        try {
            URL url = new URL(path);
// 然后我们使用httpPost的方式把lientKey封装成Json数据的形式传递给服务器
// 在这里呢我们要封装的时这样的数据
// 我们把JSON数据转换成String类型使用输出流向服务器写
            String content = String.valueOf(json);
// 现在呢我们已经封装好了数据,接着呢我们要把封装好的数据传递过去
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
// 设置允许输出
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
// 设置User-Agent: Fiddler
            conn.setRequestProperty("ser-Agent", "Fiddler");
// 设置contentType
            conn.setRequestProperty("Content-Type", "application/json");
            os = conn.getOutputStream();
            os.write(content.getBytes());
            os.flush();
// 定义BufferedReader输入流来读取URL的响应
// Log.i("-----send", "end");

            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            if (conn.getResponseCode() == 200) {
                while ((line = in.readLine()) != null) {
                    result += line;
                }
            }
        } catch (SocketTimeoutException e) {
// Log.i("错误", "连接时间超时");
            e.printStackTrace();
            return "错误";
        } catch (MalformedURLException e) {
// Log.i("错误", "jdkfa");
            e.printStackTrace();
            return "错误";
        } catch (ProtocolException e) {
// Log.i("错误", "jdkfa");
            e.printStackTrace();
            return "错误";
        } catch (IOException e) {
// Log.i("错误", "jdkfa");
            e.printStackTrace();
            return "错误";
        }// 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }


    public static void getNet(String url, Callback callback) throws Exception {
//        Log.e("开始连接\n"+url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        currentCall= getClient().newCall(request);
        currentCall.enqueue(callback);
    }

    //测试的function ，使用get方式，返回string
    String getData(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = getClient().newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    //测试的function 使用post，返回String
    String postData(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }


}
