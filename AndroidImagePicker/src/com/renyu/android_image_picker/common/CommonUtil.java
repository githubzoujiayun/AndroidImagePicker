package com.renyu.android_image_picker.common;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;

public class CommonUtil {


    /**
     * 执行异步任务
     * <p>
     * android 2.3 及一下使用execute()方法
     * <p>
     * android 3.0 及以上使用executeOnExecutor方法
     * 
     * @param task
     * @param params
     */
    @SuppressLint("NewApi")
    public static <Params, Progress, Result> void execute(AsyncTask<Params, Progress, Result> task,
            Params... params) {
        if (Build.VERSION.SDK_INT >= 11) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }

    /**
     * 是否有SD卡
     * 
     * @return
     */
    public static boolean hasExternalStorage() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 提交数据到服务器
     * @param url     上传路径
     * @param params  请求参数 key为参数名,value为参数值
     * @param fileMap 文件及其参数键值对
     * @return 返回请求response的body内容
     */
    public static String post(String url, Map<String, String> params, Map<String, File> fileMap) throws IOException {
        String BOUNDARY = "---------------------------" + System.currentTimeMillis();//分割符
        String PREFIX = "--"; //前缀
        String LINEND = "\r\n";    //换行符
        String MULTIPART_FROM_DATA = "multipart/form-data";//数据类型
        String CHARSET = "UTF-8";//字符编码

        URL uri = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(5 * 1000); // 缓存的最长时间
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false); // 不允许使用缓存
        //设置头信息
        conn.setRequestMethod("POST");
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

        // 首先组拼文本类型的参数
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);
            sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
            sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
            sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
            sb.append(LINEND);
            sb.append(entry.getValue());
            sb.append(LINEND);
        }

        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
        outStream.write(sb.toString().getBytes());
        InputStream in = null;
        // 发送文件数据
        if (fileMap != null) {
            for (Map.Entry<String, File> file : fileMap.entrySet()) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINEND);
                // name是post中传参的键 filename是文件的名称
                sb1.append("Content-Disposition: form-data; name=\"" + file.getKey() + "\"; filename=\"" + file.getValue() + "\"" + LINEND);
                sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
                sb1.append(LINEND);
                outStream.write(sb1.toString().getBytes());
                InputStream is = new FileInputStream(file.getValue());

                int bytesAvailable;
                while ((bytesAvailable = is.available()) > 0) {
                    int bufferSize = Math.min(bytesAvailable, 4096);
                    byte[] buffer = new byte[bufferSize];
                    int bytesRead = is.read(buffer, 0, bufferSize);
                    outStream.write(buffer, 0, bytesRead);
                }

                is.close();
                outStream.write(LINEND.getBytes());
            }

            // 请求结束标志

        }
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
        outStream.write(end_data);
        outStream.flush();
        outStream.close();
        // 得到响应码
        StringBuilder sb2 = null;
        int res = conn.getResponseCode();
        if (res == 200) {
            in = conn.getInputStream();
            int ch;
            sb2 = new StringBuilder();
            while ((ch = in.read()) != -1) {
                sb2.append((char) ch);
            }
        } else {
            return null;
        }
        conn.disconnect();
        return sb2.toString();
    }
    
    public static void upload(String path, String token) {
    	try {
            String url = ParamsManager.URL+"?token="+token;

            //文本参数
            Map<String, String> params = new HashMap<String, String>();

            //文件数据
            File imageFile = new File(path);
            Map<String, File> files = new HashMap<String, File>();
            files.put("avatar", imageFile);

            String response = post(url, params, files);
            if (response != null) {

                JSONObject jsonObject = new JSONObject(response);
                int feedback = (Integer) jsonObject.get("feedback");

                /**
                 * feedback
                 * 100:成功
                 *-401:token错误
                 *-201:文件上传有误
                 */
                System.out.println("feedback=" + feedback);
            } else {
                //ResponseCode!=200
            }

        } catch (Exception e) {
            System.out.println("upload error");
            e.printStackTrace();
        }
    }
    
}
