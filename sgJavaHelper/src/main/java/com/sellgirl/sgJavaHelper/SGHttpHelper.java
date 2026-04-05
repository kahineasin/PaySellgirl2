package com.sellgirl.sgJavaHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;

import com.alibaba.fastjson.JSON;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.model.HttpPostOption;

// HttpClient 5.x 导入
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.entity.mime.HttpMultipartMode;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;

/**
 * HTTP 请求工具类（基于 Apache HttpClient 5.x）
 * 
 * deepseek直接升级了我的SGHttpHelper, 把原本的依赖从org.apache.http升级到了org.apache.hc
 */
public class SGHttpHelper {

    /**
     * 通用 HTTP 请求方法（支持 GET/POST/PUT/DELETE）
     * @param httpRequest 具体的请求对象（如 HttpGet, HttpPost 等）
     * @param url 请求地址
     * @param body 请求体（JSON 字符串，仅对 POST/PUT 有效）
     * @param headers 请求头
     * @return 封装的结果对象
     */
    public static <T extends HttpUriRequestBase> SGRequestResult httpRequest(T httpRequest, String url,
                                                                              String body, Map<String, String> headers) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            httpRequest.setUri(uriBuilder.build());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            SGRequestResult result = new SGRequestResult();
            result.setError("Invalid URL: " + e.getMessage());
            result.refuse = true;
            return result;
        }

        // 设置请求头
        if (headers != null) {
            for (Entry<String, String> header : headers.entrySet()) {
                httpRequest.setHeader(header.getKey(), header.getValue());
            }
        }

        // 设置请求体（仅对有实体的请求，如 POST/PUT）
        if (httpRequest instanceof HttpPost || httpRequest instanceof HttpPut) {
            if (!SGDataHelper.StringIsNullOrWhiteSpace(body)) {
                httpRequest.setHeader("Content-Type", "application/json;charset=" + SGDataHelper.encoding);
                httpRequest.setEntity(new StringEntity(body, Charset.forName(SGDataHelper.encoding)));
            }
        }

        SGRequestResult result = new SGRequestResult();

        try {
            response = httpClient.execute(httpRequest);
            result.statusCode = response.getCode();  // 5.x 中直接 getCode()
            if (SGDataHelper.HTTP_STATUS_OK == result.statusCode) {
                result.content = EntityUtils.toString(response.getEntity(), Charset.forName(SGDataHelper.encoding));
            } else {
                result.error = "Invalid response from API, status code: " + result.statusCode;
            }
        } catch (IOException | ParseException e) {
            result.setError(e.getMessage());
            result.refuse = true;
        } finally {
            try {
                if (response != null) response.close();
                if (httpClient != null) httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    // 以下便捷方法保持签名不变，内部调用 httpRequest

    public static SGRequestResult HttpDelete(String url, String body, Map<String, String> headers) {
        return httpRequest(new HttpDelete(url), url, body, headers);
    }

    public static SGRequestResult HttpPut(String url, String body, Map<String, String> headers) {
        return httpRequest(new HttpPut(url), url, body, headers);
    }

    public static SGRequestResult HttpGet(String url, String body, Map<String, String> headers) {
        return httpRequest(new HttpGet(url), url, body, headers);
    }

    public static SGRequestResult HttpPost(String url, String body, Map<String, String> headers) {
        return httpRequest(new HttpPost(url), url, body, headers);
    }

    public static SGRequestResult HttpDelete(String url, String body) {
        return HttpDelete(url, body, null);
    }

    public static SGRequestResult HttpPut(String url, String body) {
        return HttpPut(url, body, null);
    }

    public static SGRequestResult HttpGet(String url, String body) {
        return HttpGet(url, body, null);
    }

    public static SGRequestResult HttpPost(String url, String body) {
        return HttpPost(url, body, null);
    }

    /**
     * 文件上传（支持中文）
     */
    public static void HttpPostFile(String reportUrl, File file, Map<String, Object> postBody) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(reportUrl);

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(200000,java.util.concurrent.TimeUnit.MILLISECONDS)
                    .setResponseTimeout(200000, java.util.concurrent.TimeUnit.MILLISECONDS) // 5.x 使用 setResponseTimeout
                    .build();
            httpPost.setConfig(requestConfig);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                    .setCharset(Charset.forName("UTF-8"))
                    .setMode(HttpMultipartMode.LEGACY); // 对应旧版的 BROWSER_COMPATIBLE

            if (file != null) {
                builder.addBinaryBody("file", file, ContentType.create("multipart/form-data", Charset.forName("UTF-8")), file.getName());
            }

            for (Entry<String, Object> entry : postBody.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof String) {
                    builder.addTextBody(key, SGDataHelper.ObjectToString(value),
                            ContentType.create("multipart/form-data", Charset.forName("UTF-8")));
                } else {
                    builder.addTextBody(key, JSON.toJSONString(value), ContentType.APPLICATION_JSON);
                }
            }

            httpPost.setEntity(builder.build());
            response = httpClient.execute(httpPost);
            int statusCode = response.getCode();
            if (statusCode == 200) {
                System.out.println("上传成功");
            } else {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
                    StringBuilder buffer = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null && line.trim().length() > 0) {
                        buffer.append(line);
                    }
                    System.out.println(buffer.toString());
                }
                System.out.println("上传失败：" + statusCode);
            }
        } catch (Exception ex) {
            System.out.println("uploadReport发生异常：" + ex);
        } finally {
            try {
                if (response != null) response.close();
                if (httpClient != null) httpClient.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // 分页请求方法（签名保持不变，内部无需修改）
    public static <TResponse, TData> Boolean HttpGetPageByPage(Type responseCls, int firstPage,
                                                               Function<Integer, String> urlAction,
                                                               Function<TResponse, List<TData>> dataAction,
                                                               Consumer<List<TData>> dataDoAction,
                                                               Consumer<TResponse> endAction,
                                                               Function<TResponse, Boolean> errorAction,
                                                               String apiName,
                                                               Consumer<HttpPostOption> postOptionAction) throws Exception {
        HttpPostOption postOption = new HttpPostOption();
        if (null != postOptionAction) {
            postOptionAction.accept(postOption);
        }
        String r;
        List<TData> data;
        Boolean isEnd = false;
        int pageNum = firstPage;

        int logMaxLength = 100;
        java.util.function.Function<String, String> shortLog = a -> a == null ? "" : (a.length() > logMaxLength ? a.substring(0, logMaxLength) : a);

        while (!isEnd) {
            String url = urlAction.apply(pageNum);
            SGRequestResult r2 = SGHttpHelper.HttpGet(url, "");
            if (!r2.success || null == r2.content) {
                throw new Exception(SGDataHelper.FormatString("{2}接口异常或没有读到数据\r\nurl:{0}\r\n返回信息:{1}\r\n", url,
                        shortLog.apply(r2.content), apiName));
            }
            r = r2.content;

            TResponse res = JSON.parseObject(r, responseCls);

            data = dataAction.apply(res);
            if (data != null && data.size() > 0) {
                dataDoAction.accept(data);
            } else if (pageNum == 1 && postOption.NoDataError) {
                throw new Exception(SGDataHelper.FormatString("{2}接口的第1页没有读到数据\r\nurl:{0}\r\n返回信息:{1}", url,
                        shortLog.apply(r), apiName));
            } else if (errorAction.apply(res)) {
                throw new Exception(SGDataHelper.FormatString("{2}接口异常\r\nurl:{0}\r\n返回信息:{1}", url,
                        shortLog.apply(r), apiName));
            } else {
                isEnd = true;
                SGDataHelper.WriteLog(SGDataHelper.FormatString("{2}接口读取数据完成\r\nurl:{0}\r\n最后一次的返回信息(取前{3}字符):{1}",
                        url, shortLog.apply(r), apiName, logMaxLength));
                endAction.accept(res);
            }
            pageNum++;
        }
        return true;
    }
}