package com.example.stamp.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.stamp.utils.MyLog;
import com.example.stamp.utils.ThreadManager;
import com.example.stamp.utils.UIUtils;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 网络请求的基类
 *
 * @author Administrator
 */
public class HttpUtils {

    /**
     * 判断网络连接是否已开 true 已打开 false 未打开
     */
    public static boolean isConn(Context context) {
        boolean bisConnFlag = false;
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = conManager.getActiveNetworkInfo();
        if (network != null) {
            bisConnFlag = conManager.getActiveNetworkInfo().isAvailable();
        }
        return bisConnFlag;
    }

    /**
     * Function : 发送Post请求到服务器 Param : params请求体内容，encode编码格式
     */
    public static String submitPostData(String strUrlPath, Map<String, String> params) {
        byte[] data = getRequestData(params, "UTF-8").toString().getBytes();// 获得请求体
        try {
            URL url = new URL(strUrlPath);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(5000); // 设置连接超时时间
            httpURLConnection.setDoInput(true); // 打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true); // 打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST"); // 设置以Post方式提交数据
            httpURLConnection.setUseCaches(false); // 使用Post方式不能使用缓存
            // 设置请求体的类型是文本类型
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 设置请求体的长度
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            // 获得输出流，向服务器写入数据
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);
            int response = httpURLConnection.getResponseCode(); // 获得服务器的响应码
            MyLog.e("response", "" + response);
            if (response == HttpURLConnection.HTTP_OK) {
                InputStream inptStream = httpURLConnection.getInputStream();
                return dealResponseResult(inptStream); // 处理服务器的响应结果
            }
        } catch (MalformedURLException e) {
            Log.e("网络请求", "url异常");
        } catch (IOException e) {
            Log.e("网络请求", "IO流异常");
            return "-2";
        }
        return "-1";
    }

    /**
     * Function : 封装请求体信息 Param : params请求体内容，encode编码格式
     */
    public static StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer stringBuffer = new StringBuffer(); // 存储封装好的请求体信息
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), encode)).append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1); // 删除最后的一个"&"
        } catch (Exception e) {
            // e.printStackTrace();
            Log.e("网络请求", "编码格式错误");
        }
        return stringBuffer;
    }

    /**
     * Function : 处理服务器的响应结果（将输入流转化成字符串） Param : inputStream服务器的响应输入流
     */
    public static String dealResponseResult(InputStream inputStream) {
        String resultData = null; // 存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            Log.e("网络请求", "IO流异常");
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }


    /**
     * 请求网络的方法
     *
     * @param strUrlPath 请求的地址
     * @param params 传入的集合
     */
    public static <T> void postData(final String strUrlPath, final Map<String, String> params, final Class<T> t, final NetListener mNetlistener) {
        // 用线程管理者管理线程
        ThreadManager.ThreadPoolProxy createShortPool = ThreadManager.getInstance().createShortPool();
        createShortPool.execute(new Runnable() {
            @Override
            public void run() {
                final String result = submitPostData(strUrlPath, params);
                UIUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result.equals("-1")) {
                            mNetlistener.onErrorResponse();
                        } else {
                            Gson gson = new Gson();
                            T obj = gson.fromJson(result, t);
                            mNetlistener.onResponse(obj);
                        }
                    }
                });
            }
        });
    }

    /**
     * 访问网络回调的成功与失败接口
     */
    public interface NetListener {
        <T> void onResponse(T t);

        void onErrorResponse();
    }
}
