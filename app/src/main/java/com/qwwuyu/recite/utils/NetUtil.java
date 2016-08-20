package com.qwwuyu.recite.utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeoutException;

/**
 * 网络操作类.
 */
public class NetUtil {
    /** http链接 */
    private HttpURLConnection mConnection;
    /** 输出流 */
    private DataOutputStream dos;
    private BufferedReader reader;
    private static final int CONNECT_TIMEOUT = 50000;
    private static final int SO_TIMEOUT = 30000;

    /**
     * 像指定地址发送post请求提交数据
     *
     * @param path      数据提交路径.
     * @param attribute 发送请求参数,key为属性名,value为属性值.
     * @return 服务器的响应信息, 当发生错误时返回响应码.
     * @throws IOException      网络连接错误时抛出IOException.
     * @throws TimeoutException 网络连接超时时抛出TimeoutException.
     */
    public String sendPost(String path, HashMap<String, Object> attribute) throws IOException, TimeoutException {
        URL url = new URL(path);
        mConnection = (HttpURLConnection) url.openConnection();
        mConnection.setDoOutput(true); // 设置输出,post请求必须设置.
        mConnection.setDoInput(true); // 设置输入,post请求必须设置.
        mConnection.setUseCaches(false); // 设置是否启用缓存,post请求不能使用缓存.
        // 设置Content-Type属性.
        mConnection.setRequestProperty("Content-Type", "application/json");
        mConnection.setConnectTimeout(CONNECT_TIMEOUT);
        mConnection.setReadTimeout(SO_TIMEOUT);
        mConnection.setRequestMethod("POST");
        mConnection.connect(); // 打开网络链接.
        dos = new DataOutputStream(mConnection.getOutputStream());
        if (attribute != null) {
            String param = new JSONObject(attribute).toString();
            LogUtil.i(param);
            dos.writeBytes(param); // 将请求参数写入网络链接.
        }
        dos.flush();
        return readResponse();
    }

    /**
     * 像指定地址发送get请求.
     *
     * @param path 数据提交路径.
     * @return 服务器的响应信息, 当发生错误时返回响应码.
     * @throws IOException      网络连接错误时抛出IOException.
     * @throws TimeoutException 网络连接超时时抛出TimeoutException.
     */
    public String sendGet(String path) throws IOException, TimeoutException {
        try {
            URL url = new URL(path);
            mConnection = (HttpURLConnection) url.openConnection();
            mConnection.setUseCaches(false); // 设置是否启用缓存,post请求不能使用缓存.
            mConnection.setRequestProperty("Content-Type", "application/json");
            mConnection.setConnectTimeout(CONNECT_TIMEOUT);
            mConnection.setReadTimeout(SO_TIMEOUT);
            mConnection.setRequestMethod("GET");
            mConnection.connect(); // 打开网络链接.
            return readResponse();
        } catch (SocketTimeoutException e) {
            throw new TimeoutException(e.getMessage());
        }
    }

    /**
     * 读取服务器响应信息.
     *
     * @return 服务器的响应信息, 当发生错误时返回响应码.
     * @throws IOException 读取信息发生错误时抛出IOException.
     */
    private String readResponse() throws IOException {
        String result;
        int code = mConnection.getResponseCode();
        mConnection.getContentLength();
        if (code >= HttpURLConnection.HTTP_OK && code < 210) { // 若响应码以2开头则读取响应头总的返回信息
            result = inputStream2String(mConnection.getInputStream());
        } else { // 若响应码不以2开头则返回错误信息.
            result = "error";
        }
        closeConnection();
        return result;
    }

    private String inputStream2String(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        String line;
        reader = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }

    /**
     * 将发送请求的参数构造为指定格式.
     */
    private String getParams(HashMap<String, String> attribute) {
        Set<String> keys = attribute.keySet(); // 获取所有参数名
        Iterator<String> iterator = keys.iterator(); // 将所有参数名进行跌代
        StringBuffer params = new StringBuffer();
        // 取出所有参数进行构造
        while (iterator.hasNext()) {
            try {
                String key = iterator.next();
                String param = key + "=" + URLEncoder.encode(attribute.get(key)) + "&";
                params.append(param);
            } catch (Exception e) {
            }
        }
        // 返回构造结果
        return params.toString().substring(0, params.toString().length() - 1);
    }

    /**
     * 关闭链接与所有从链接中获得的流.
     *
     * @throws IOException
     */
    private void closeConnection() throws IOException {
        if (reader != null) {
            reader.close();
        }
        if (dos != null) {
            dos.close();
        }
        if (mConnection != null) {
            mConnection.disconnect();
        }
    }

    /**
     * 下载文件,下载文件存储至指定路径.
     *
     * @param path     下载路径.
     * @param savePath 存储路径.
     * @return 下载成功返回true, 若下载失败则返回false.
     * @throws MalformedURLException 建立连接发生错误抛出MalformedURLException.
     * @throws IOException           下载过程产生错误抛出IOException.
     */
    public boolean downloadFile(String path, String savePath) throws MalformedURLException, IOException {
        File file = null;
        InputStream input = null;
        OutputStream output = null;
        boolean success = false;
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                input = conn.getInputStream();
                file = new File(savePath);
                file.createNewFile(); // 创建文件
                output = new FileOutputStream(file);
                byte buffer[] = new byte[1024];
                int read = 0;
                while ((read = input.read(buffer)) != -1) { // 读取信息循环写入文件
                    output.write(buffer, 0, read);
                }
                output.flush();
                success = true;
            } else {
                success = false;
            }
        } catch (MalformedURLException e) {
            success = false;
            throw e;
        } catch (IOException e) {
            success = false;
            throw e;
        } finally {
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return success;
    }
}