package com.topunion.chili.paylib.net.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.topunion.chili.paylib.Constants;
import com.topunion.chili.paylib.net.interf.Callback;
import com.topunion.chili.paylib.net.interf.ProgressCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LinYi.
 */
public class ApiHttpClient {

    private final static String TAG = "ApiHttpClient";
    public static final int TIMEOUT_MS = 150000;//超时时间

    /**
     * 本类实例
     */
    private static volatile ApiHttpClient mInstance;
    /**
     * 维护一个请求队列
     */
    private static RequestQueue mRequestQueue;

    /**
     * 用于上传下载
     */
    private OkHttpClient mOkHttpClient;

    private ApiHttpClient(Context context) {
        mRequestQueue = Volley.newRequestQueue(context, new OkHttpStack(new OkHttpClient()));
        mOkHttpClient = new OkHttpClient();
    }

    /**
     * 获取本类实例
     *
     * @param context
     * @return
     */
    public static ApiHttpClient getInstance(Context context) {
        if (mInstance == null) {
            synchronized (ApiHttpClient.class) {
                if (mInstance == null) {
                    mInstance = new ApiHttpClient(context);
                }
            }
        }
        return mInstance;
    }

    /***
     * post请求
     *
     * @param url
     * @param params
     * @param isJSONArray 返回结果是否是JSONArray
     * @param callback
     * @return
     */
    public Request doPost(String url, final Map<String, String> params, final boolean isJSONArray, final Callback callback) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                handleData(response, isJSONArray, callback);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse", error);
                callback.onCall(Constants.STATE_CODE_FAILED, null);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        requestConfig(request);

        mRequestQueue.add(request);
        return request;
    }

    /**
     * 请求参数为json格式的字符串post请求<br/>
     *
     * @param url
     * @param jsonParams
     * @param callback
     * @return 返回值为JsonObjectRequest
     */
    public JsonObjectRequest doPost(String url, String jsonParams, final Callback callback) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                handleData(response.toString(), false, callback);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onCall(Constants.STATE_CODE_FAILED, null);
            }
        });
        requestConfig(request);
        mRequestQueue.add(request);
        return request;
    }

    /**
     * 配置request
     *
     * @param request
     */
    private void requestConfig(Request request) {
        request.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MS,//默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    /**
     * 返回值默认为JSONObject的POST请求
     *
     * @param url
     * @param params
     * @param callback
     * @return
     */
    public Request doPost(String url, final Map<String, String> params, final Callback callback) {
        return this.doPost(url, params, false, callback);
    }

    /**
     * GET请求
     *
     * @param url
     * @param params
     * @param isJSONArray 返回结果是否是JSONArray
     * @param callback
     * @return
     */
    public Request doGet(final String url, final Map<String, String> params, final boolean isJSONArray, final Callback callback) {
        StringRequest request = new StringRequest(Request.Method.GET, fixUrl(Request.Method.GET, url, params), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                handleData(response, isJSONArray, callback);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse", error);
                callback.onCall(Constants.STATE_CODE_FAILED, null);
            }
        });
        requestConfig(request);
        mRequestQueue.add(request);
        return request;
    }

    /**
     * 请求参数为json格式的字符串get请求<br/>
     *
     * @param url
     * @param jsonParams
     * @param callback
     * @return 返回值为JsonObjectRequest
     */
    public JsonObjectRequest doGet(String url, String jsonParams, final Callback callback) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, jsonParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                handleData(response.toString(), false, callback);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onCall(Constants.STATE_CODE_FAILED, null);
            }
        });
        requestConfig(request);
        mRequestQueue.add(request);
        return request;
    }

    /**
     * 返回值默认为JSONObject的GET请求
     *
     * @param url
     * @param params
     * @param callback
     * @return
     */
    public Request doGet(final String url, final Map<String, String> params, final Callback callback) {
        return this.doGet(url, params, false, callback);
    }

    /**
     * 处理网络请求返回的数据
     *
     * @param response
     * @param isJSONArray
     * @param callback
     */
    private void handleData(String response, boolean isJSONArray, Callback callback) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int resultCode = jsonObject.getInt("state");//状态码
                Object data = null;//返回json数据（JSONObject/JSONArray）
                if (!jsonObject.isNull("data")) {
                    if (isJSONArray) {
                        data = jsonObject.getJSONArray("data");
                    } else {
                        data = jsonObject.getJSONObject("data");
                    }
                }
                callback.onCall(resultCode, data);
            } catch (JSONException e) {
                e.printStackTrace();
                callback.onCall(Constants.STATE_CODE_FAILED, null);
            }
        }
    }


    /**
     * 修正url，把params添加到url（主要用于get）
     *
     * @param method
     * @param url
     * @return
     */
    private String fixUrl(int method, String url, Map<String, String> params) {
        String fixedUrl = url;
        if (Request.Method.GET == method && null != params && 0 < params.size()) {
            if (fixedUrl.indexOf("?") > 0) {
                if (!fixedUrl.endsWith("&")) {
                    fixedUrl += "&";
                }
            } else {
                fixedUrl += "?";
            }
            for (String key : params.keySet()) {
                String value = params.get(key);
                fixedUrl += (key + "=" + value + "&");
            }
            fixedUrl = fixedUrl.substring(0, fixedUrl.length() - 1);
        }
        return fixedUrl;
    }


    /**
     * ----------------------------以下为上传下载-------------------------------------------
     */

    /**
     * 上传图片
     *
     * @param url
     * @param params
     * @param bitmapMap
     * @param callback
     * @throws IOException
     */
    public void uploadByBitmap(String url, Map<String, String> params, Map<String, List<Bitmap>> bitmapMap, final ProgressCallback callback) {

        try {
            Map<String, List<byte[]>> byteMap = new HashMap<>();
            for (String keyname : bitmapMap.keySet()) {

                List<Bitmap> bitmaps = bitmapMap.get(keyname);
                List<byte[]> bytes = new ArrayList<>();

                for (Bitmap bitmap : bitmaps) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    bytes.add(out.toByteArray());
                    out.close();
                }

                if (!bytes.isEmpty()) {
                    byteMap.put(keyname, bytes);
                }
            }

            uploadByByteArray(url, params, byteMap, callback);

        } catch (IOException e) {
            e.printStackTrace();
            //异常了，回调错误
            callback.onCall(Constants.STATE_CODE_FAILED, null);
            return;
        }

    }

    /**
     * 注意：此处callback的处理位于子线程，当需要在UI线程上更新进度时，必须手动post到主线程
     * <br/>该方法用于一个byte[]对应一个key
     * <p/>
     * 传输类型：application/octet-stream
     * 文件名：使用 时间戳
     *
     * @param url            接口地址
     * @param params         接口参数
     * @param fileByteArrays Map<String, List<byte[]>>
     * @param callback       回调
     */
    public void uploadByByteArray(String url, Map<String, String> params, Map<String, List<byte[]>> fileByteArrays, final ProgressCallback callback) {
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        addParams(params, builder);
        if (fileByteArrays != null && !fileByteArrays.isEmpty()) {
            RequestBody fileBody;
            for (String fileKeyName : fileByteArrays.keySet()) {
                List<byte[]> bytelist = fileByteArrays.get(fileKeyName);//得到文件对象

                for (byte[] bytes : bytelist) {
                    //默认传输 application/octet-stream 类型
                    fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), bytes);

                    //文件名 使用时间戳
                    String fileName = String.valueOf(System.currentTimeMillis());

                    builder.addPart(Headers.of("Content-Disposition",
                                    "form-data; name=\"" + fileKeyName + "\"; filename=\"" + fileName + "\""),
                            fileBody);
                }
            }
        }
        CountingRequestBody countingRequestBody = wrapRequestBody(callback, builder);
        doUpload(url, callback, countingRequestBody);

    }

    /**
     * 注意：此处callback的处理位于子线程，当需要在UI线程上更新进度时，必须手动post到主线程
     */
    public void upload(String url, Map<String, File> files, final ProgressCallback callback) {
        this.upload(url, null, files, callback);
    }

    /**
     * 注意：此处callback的处理位于子线程，当需要在UI线程上更新进度时，必须手动post到主线程
     * <br/>该方法用于一个file对应一个key
     *
     * @param url      接口地址
     * @param params   接口参数
     * @param files    文件参数
     * @param callback 回调
     */
    public void upload(String url, Map<String, String> params, Map<String, File> files, final ProgressCallback callback) {
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        addParams(params, builder);
        if (files != null && !files.isEmpty()) {
            RequestBody fileBody;
            for (String fileKeyName : files.keySet()) {
                File file = files.get(fileKeyName);//得到文件对象
                String fileName = file.getName();//得到文件本身自带文件名
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                builder.addPart(Headers.of("Content-Disposition",
                                "form-data; name=\"" + fileKeyName + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }
        CountingRequestBody countingRequestBody = wrapRequestBody(callback, builder);
        doUpload(url, callback, countingRequestBody);

    }

    /**
     * 注意：此处callback的处理位于子线程，当需要在UI线程上更新进度时，必须手动post到主线程
     * <br/>该方法用于一个file的key对应多个文件
     *
     * @param url      接口地址
     * @param params   接口参数
     * @param files    文件参数
     * @param callback 回调
     */
    public void upload2(String url, Map<String, String> params, Map<String, List<File>> files, final ProgressCallback callback) {
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        addParams(params, builder);
        if (files != null && !files.isEmpty()) {
            RequestBody fileBody;
            for (String fileKeyName : files.keySet()) {
                List<File> fileList = files.get(fileKeyName);//得到文件列表
                for (File file : fileList) {
                    String fileName = file.getName();//得到文件本身自带文件名
                    fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                    builder.addPart(Headers.of("Content-Disposition",
                                    "form-data; name=\"" + fileKeyName + "\"; filename=\"" + fileName + "\""),
                            fileBody);
                }
            }
            CountingRequestBody countingRequestBody = wrapRequestBody(callback, builder);
            doUpload(url, callback, countingRequestBody);
        }

    }


    private void doUpload(String url, final ProgressCallback callback, CountingRequestBody countingRequestBody) {
        com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                .url(url)
                .post(countingRequestBody)
                .build();
        //执行
        mOkHttpClient.newCall(request).enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(com.squareup.okhttp.Request request, IOException e) {
                callback.onCall(Constants.STATE_CODE_FAILED, null);
                Log.e(TAG, "upload()发生错误：" + e);
            }

            @Override
            public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                String result = response.body().string();
                Log.e(TAG, "response " + result);
                handleData(result, false, callback);
            }
        });
    }

    @NonNull
    private CountingRequestBody wrapRequestBody(final ProgressCallback callback, MultipartBuilder builder) {
        return new CountingRequestBody(builder.build(), new CountingRequestBody.Listener() {
            @Override
            public void onRequestProgress(long bytesWritten, long contentLength) {
                callback.onProgress(bytesWritten * 1.0f / contentLength);
            }
        });
    }

    /**
     * 上传时包装请求参数
     *
     * @param params
     * @param builder
     */
    private void addParams(Map<String, String> params, MultipartBuilder builder) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, params.get(key)));
            }
        }
    }


    /***
     * 下载
     * 注意：此处callback的处理位于子线程，当需要在UI线程上更新进度时，必须手动post到主线程
     *
     * @param url
     * @param targetDir 保存路径
     * @param callback
     */
    public void download(final String url, final String targetDir, final ProgressCallback callback) {
        final com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                .url(url)
                .build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(com.squareup.okhttp.Request request, IOException e) {
                callback.onCall(Constants.STATE_CODE_FAILED, null);
                Log.e(TAG, "download()发生错误：" + e);
            }

            @Override
            public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                Log.e("response", "response " + response.body().toString());
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    final long total = response.body().contentLength();//文件总大小
                    long sum = 0;
                    File file = new File(targetDir, getFileName(url));
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        sum += len;
                        fos.write(buf, 0, len);
                        if (callback != null) {
                            final long finalSum = sum;
                            callback.onProgress(finalSum * 1.0f / total);
                        }
                    }
                    fos.flush();
                    callback.onCall(Constants.STATE_CODE_SUCCESS, file);//将路径file.getAbsolutePath();
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.onCall(Constants.STATE_CODE_FAILED, null);
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null) fos.close();
                    } catch (IOException e) {
                    }
                }

            }
        });
    }


    /**
     * 生成文件名
     *
     * @param path
     * @return
     */
    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    /**
     * 判断类型
     *
     * @param path
     * @return
     */
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


}
