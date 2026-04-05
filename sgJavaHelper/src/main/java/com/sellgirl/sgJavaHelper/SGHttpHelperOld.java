//package com.sellgirl.sgJavaHelper;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.lang.reflect.Type;
//import java.net.URISyntaxException;
//import java.nio.charset.Charset;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.function.Consumer;
//import java.util.function.Function;
//
//import com.alibaba.fastjson.JSON;
//import com.sellgirl.sgJavaHelper.config.SGDataHelper;
//import com.sellgirl.sgJavaHelper.model.HttpPostOption;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.ParseException;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
//import org.apache.http.client.methods.HttpPut;
//import org.apache.http.client.methods.HttpDelete;
//import org.apache.http.client.methods.HttpRequestBase;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.utils.URIBuilder;
//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.entity.mime.content.StringBody;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
////import com.sellgirl.sgJavaHelper.config.SGDataHelper;
//
///**
// * 
// * 使用SGHttpHelper类一定要有apache引用:(因为这些类库容易冲突,设置为provided了) 
//        api "org.apache.httpcomponents:httpclient:4.5.13"
//        api "org.apache.httpcomponents:httpmime:4.5.13"
//        
// * @deprecated HttpRequestBase已经过期,下次使用SGHttpHelper顺别把方式改了.注意proguard压缩后.运行时只要类中接触此类都会报错HttpRequestBase不存在,因为当前时provided的
// */
//@Deprecated
//public class SGHttpHelper {
//		public static <RequestType extends HttpRequestBase> SGRequestResult HttpRequest(RequestType httpRequest, String url,
//				String body, Map<String, String> headers) {
//			CloseableHttpClient httpClient = HttpClients.createDefault();
//			CloseableHttpResponse response = null;
//
//			URIBuilder uriBuilder;
//			try {
//				uriBuilder = new URIBuilder(url);
//				httpRequest.setURI(uriBuilder.build());
//			} catch (URISyntaxException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//
//
//			if (headers != null) {
//				Iterator<Entry<String, String>> headerIterator = headers.entrySet().iterator(); // 循环增加header
//				while (headerIterator.hasNext()) {
//					Entry<String, String> elem = headerIterator.next();
//					httpRequest.addHeader(elem.getKey(), elem.getValue());
//				}
//			}
//
//			// benjamin todo
//
//			if (httpRequest instanceof HttpEntityEnclosingRequestBase && (!com.sellgirl.sgJavaHelper.config.SGDataHelper.StringIsNullOrWhiteSpace(body))) {
//				HttpEntityEnclosingRequestBase httpPost = ((HttpEntityEnclosingRequestBase) httpRequest);
//				httpPost.setHeader("Content-Type", "application/json;charset=" + com.sellgirl.sgJavaHelper.config.SGDataHelper.encoding);
//				httpPost.setEntity(new StringEntity(body, com.sellgirl.sgJavaHelper.config.SGDataHelper.encoding));
//
//			}
//
//			SGRequestResult result = new SGRequestResult();
//			// 执行请求
//			try {
//				response = httpClient.execute(httpRequest);
//
//				result.statusCode = response.getStatusLine().getStatusCode();
//				if (com.sellgirl.sgJavaHelper.config.SGDataHelper.HTTP_STATUS_OK == result.statusCode) {
//					// 获取服务器请求的返回结果，注意此处为了保险要加上编码格式
//					try {
//						result.content = EntityUtils.toString(response.getEntity(),com.sellgirl.sgJavaHelper.config.SGDataHelper.encoding);
//					} catch (ParseException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				} else {
//					result.error = "Invalide response from API" + response.toString();
//				}
//			} catch (ClientProtocolException e) {
//				result.setError(e.getMessage());
//				result.refuse = true;
//			} catch (IOException e) {
//				result.setError(e.getMessage());
//				result.refuse = true;
//			}
//			return result;
//		}
//
//		public static SGRequestResult HttpDelete(String url, String body, Map<String, String> headers) {
//			return HttpRequest(new HttpDelete(), url, body, headers);
//		}
//
//		public static SGRequestResult HttpPut(String url, String body, Map<String, String> headers) {
//			return HttpRequest(new HttpPut(), url, body, headers);
//		}
//
//		public static SGRequestResult HttpGet(String url, String body, Map<String, String> headers) {
//			return HttpRequest(new HttpGet(), url, body, headers);
//		}
//
//		public static SGRequestResult HttpPost(String url, String body, Map<String, String> headers) {
//			return HttpRequest(new HttpPost(), url, body, headers);
//		}
//
//		public static SGRequestResult HttpDelete(String url, String body) {
//			return HttpRequest(new HttpDelete(), url, body, null);
//		}
//
//		public static SGRequestResult HttpPut(String url, String body) {
//			return HttpRequest(new HttpPut(), url, body, null);
//		}
//
//		public static SGRequestResult HttpGet(String url, String body) {
//			return HttpRequest(new HttpGet(), url, body, null);
//		}
//
//		public static SGRequestResult HttpPost(String url, String body) {
//			return HttpRequest(new HttpPost(), url, body, null);
//		}
//		/**
//		 * 文件上传支持中文 注意postBody里的每个参数，在controller里似乎只能用String或String[]等简单类型接收，原因不明
//		 *
//		 * @param reportUrl
//		 * @throws IOException
//		 */
//		public static void HttpPostFile(String reportUrl, File file, // String filepath,
//				Map<String, Object> postBody) {
//			CloseableHttpClient closeableHttpClient = null;
//			CloseableHttpResponse response = null;
//			try {
//				closeableHttpClient = HttpClients.createDefault();
//				HttpPost httpPost = new HttpPost(reportUrl);
//
//				RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(200000).setSocketTimeout(200000)
//						.build();
//				httpPost.setConfig(requestConfig);
//
//				MultipartEntityBuilder httpBuilder = MultipartEntityBuilder.create().setCharset(Charset.forName("UTF-8"))
//						.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//
//				if (file != null) {
//					String filename = file.getName();
//					FileBody fileBody = new FileBody(file, ContentType.create("multipart/form-data", "UTF-8"), filename);
//					httpBuilder.addPart("file", fileBody);
//				}
//
//				Iterator<Entry<String, Object>> postBodyIter = postBody.entrySet().iterator();
//				while (postBodyIter.hasNext()) {
//					Entry<String, Object> key = postBodyIter.next();
//					Object value = key.getValue();
//					if (value instanceof String) {
//						httpBuilder.addPart(key.getKey(),
//								new StringBody(com.sellgirl.sgJavaHelper.config.SGDataHelper.ObjectToString(value), ContentType.create("multipart/form-data", "UTF-8")));
//					} else {
//						httpBuilder.addPart(key.getKey(),
//								new StringBody(JSON.toJSONString(value), ContentType.APPLICATION_JSON));
//
//					}
//				}
//
//				HttpEntity httpEntity = httpBuilder.build();
//
//				httpPost.setEntity(httpEntity);
//				response = closeableHttpClient.execute(httpPost);
//				HttpEntity responseEntity = response.getEntity();
//				int statusCode = response.getStatusLine().getStatusCode();
//				if (statusCode == 200) {
//					System.out.println("上传成功");
//				} else {
//					BufferedReader reader = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
//					StringBuffer buffer = new StringBuffer();
//					String str = "";
//
//					while ((str = reader.readLine()) != null && str.trim().length() > 0) {
//						buffer.append(str);
//					}
//					System.out.println(buffer.toString());
//					System.out.println("上传失败：" + statusCode);
//				}
//				closeableHttpClient.close();
//				if (response != null) {
//					response.close();
//				}
//
//			} catch (Exception ex) {
//				System.out.println("uploadReport发生异常：" + ex);
//			} finally {
//				try {
//					if (closeableHttpClient != null) {
//						closeableHttpClient.close();
//					}
//					if (response != null) {
//						response.close();
//					}
//				} catch (Exception ex) {
//					ex.printStackTrace();
//				}
//			}
//		}
//
//		public static <TResponse, TData> Boolean HttpGetPageByPage(Type responseCls, int firstPage,
//				Function<Integer, String> urlAction, Function<TResponse, List<TData>> dataAction,
//				Consumer<List<TData>> dataDoAction, Consumer<TResponse> endAction, Function<TResponse, Boolean> errorAction,
//				String apiName,
//				Consumer<HttpPostOption> postOptionAction) throws Exception
//		{
//			HttpPostOption postOption = new HttpPostOption();
//			if (null != postOptionAction) {
//				postOptionAction.accept(postOption);
//			}
//			String r = null;
//
//			List<TData> data = null;
//
//			Boolean isEnd = false;
//			int pageNum = firstPage;
//
//			int logMaxLength = 100;
//			Function<String, String> shortLog = a -> {
//				return a == null ? "" : (a.length() > logMaxLength ? a.substring(0, logMaxLength) : a);
//			};
//
//			while (!isEnd) {
//				String url = urlAction.apply(pageNum);
//				SGRequestResult r2 = SGHttpHelper.HttpGet(url, "");
//				if ((!r2.success) || null == r2.content) {
//					throw new Exception(SGDataHelper.FormatString("{2}接口异常或没有读到数据\r\n", "url:{0}\r\n", "返回信息:{1}\r\n", url,
//							shortLog.apply(r2.content), apiName));
//				}
//				r = r2.content;
//
//				TResponse res = JSON.<TResponse>parseObject(r, responseCls);
//
//				data = dataAction.apply(res);
//				if (data != null && data.size() > 0) {
//					dataDoAction.accept(data);
//				} else if (pageNum == 1 && postOption.NoDataError) {// 第一页都没数据的话,认为是有异常,进行重试
//					throw new Exception(SGDataHelper.FormatString("{2}接口的第1页没有读到数据\r\n", "url:{0}\r\n", "返回信息:{1}", url,
//							shortLog.apply(r), apiName));
//				} else if (errorAction.apply(res)) {
//					throw new Exception(SGDataHelper.FormatString("{2}接口异常\r\n", "url:{0}\r\n", "返回信息:{1}", url,
//							shortLog.apply(r), apiName));
//				} else {
//					isEnd = true;
//					SGDataHelper.WriteLog(SGDataHelper.FormatString("{2}接口读取数据完成\r\n", "url:{0}\r\n",
//							"最后一次的返回信息(取前{3}字符):{1}", url, shortLog.apply(r), apiName, logMaxLength));
//					endAction.accept(res);
//				}
//
//				pageNum++;
//			}
//			return true;
//		}
//}


