package com.sellgirl.sgJavaHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
//import com.sellgirl.sgJavaHelper.config.SGDataHelper;

/**
 * 
 * 使用SGHttpHelper类一定要有apache引用:(因为这些类库容易冲突,设置为provided了) 
        api "org.apache.httpcomponents:httpclient:4.5.13"
        api "org.apache.httpcomponents:httpmime:4.5.13"
 */
public class SGHttpHelper {
	// #endregion String
		public static <RequestType extends HttpRequestBase> SGRequestResult HttpRequest(RequestType httpRequest, String url,
				String body, Map<String, String> headers) {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = null;
//	        URIBuilder uriBuilder = new URIBuilder("http://192.168.0.69:8089/procedure/getInvStock");
//	        String url="http://127.0.0.1:8089/procedure/seekinvallkc2wmpsjk";
			// String url="http://localhost:38201/crm/gethyzlchange";
			URIBuilder uriBuilder;
			try {
				uriBuilder = new URIBuilder(url);
				httpRequest.setURI(uriBuilder.build());
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			// nvps.add(new BasicNameValuePair("inv_no", "ACC"));
			// uriBuilder.setParameters(nvps);

			// 根据带参数的URI对象构建GET请求对象
			// HttpPost httpGet = new HttpPost(uriBuilder.build());

			if (headers != null) {
				// for(int i=0;i<headers.length;i++) {
				// httpRequest.addHeader(headers[i]);
				// }

				Iterator<Entry<String, String>> headerIterator = headers.entrySet().iterator(); // 循环增加header
				while (headerIterator.hasNext()) {
					Entry<String, String> elem = headerIterator.next();
					httpRequest.addHeader(elem.getKey(), elem.getValue());
				}
			}

			// benjamin todo

			if (httpRequest instanceof HttpEntityEnclosingRequestBase && (!com.sellgirl.sgJavaHelper.config.SGDataHelper.StringIsNullOrWhiteSpace(body))) {
				HttpEntityEnclosingRequestBase httpPost = ((HttpEntityEnclosingRequestBase) httpRequest);
				httpPost.setHeader("Content-Type", "application/json;charset=" + com.sellgirl.sgJavaHelper.config.SGDataHelper.encoding);
				httpPost.setEntity(new StringEntity(body, com.sellgirl.sgJavaHelper.config.SGDataHelper.encoding));

				/*
				 * try { List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				 * JSONObject js = JSON.parseObject(body); for (Map.Entry<String, Object> entry
				 * : js.entrySet()) { parameters.add(new
				 * BasicNameValuePair(entry.getKey(),JSON.toJSONString(entry.getValue())));
				 * //System.out.println(entry.getKey() + ":" + entry.getValue()); }
				 * UrlEncodedFormEntity encodedFormEntity; encodedFormEntity = new
				 * UrlEncodedFormEntity( parameters, encoding);
				 * httpPost.setEntity(encodedFormEntity);//这种方法加的参数格式为: p1=xx&p2=xx&... } catch
				 * (UnsupportedEncodingException e) { e.printStackTrace(); }
				 */
			}

//	       if(httpRequest instanceof HttpPost) {    	   
//	    	   HttpPost HttpPost=(HttpPost)httpRequest;
//	    	   HttpPost.fi
//	       }
//	       
			SGRequestResult result = new SGRequestResult();
			// 执行请求
			try {
				response = httpClient.execute(httpRequest);

				result.statusCode = response.getStatusLine().getStatusCode();
				if (com.sellgirl.sgJavaHelper.config.SGDataHelper.HTTP_STATUS_OK == result.statusCode) {
					// 获取服务器请求的返回结果，注意此处为了保险要加上编码格式
					try {
						result.content = EntityUtils.toString(response.getEntity(),com.sellgirl.sgJavaHelper.config.SGDataHelper.encoding);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					result.error = "Invalide response from API" + response.toString();
				}
			} catch (ClientProtocolException e) {
				result.setError(e.getMessage());
				result.refuse = true;
				// e.printStackTrace();
			} catch (IOException e) {
				result.setError(e.getMessage());
				result.refuse = true;
				// e.printStackTrace();
			}
			//// 获得响应的实体对象
			// HttpEntity entity = response.getEntity();
			// String rStr = EntityUtils.toString(entity,encoding);
			/// *
			// * // 使用Apache提供的工具类进行转换成字符串 String entityStr = EntityUtils.toString(entity,
			// * "UTF-8"); JSONObject jsonObject = JSONObject.parseObject(entityStr);
			// */
			//// System.out.println(JSON.toJSONString(entity));
			//// String r = JSON.toJSONString(entity);
			return result;
		}

		public static SGRequestResult HttpDelete(String url, String body, Map<String, String> headers) {
			return HttpRequest(new HttpDelete(), url, body, headers);
		}

		public static SGRequestResult HttpPut(String url, String body, Map<String, String> headers) {
			return HttpRequest(new HttpPut(), url, body, headers);
		}

		public static SGRequestResult HttpGet(String url, String body, Map<String, String> headers) {
			return HttpRequest(new HttpGet(), url, body, headers);
		}

		public static SGRequestResult HttpPost(String url, String body, Map<String, String> headers) {
			return HttpRequest(new HttpPost(), url, body, headers);
		}

		public static SGRequestResult HttpDelete(String url, String body) {
			return HttpRequest(new HttpDelete(), url, body, null);
		}

		public static SGRequestResult HttpPut(String url, String body) {
			return HttpRequest(new HttpPut(), url, body, null);
		}

		public static SGRequestResult HttpGet(String url, String body) {
			return HttpRequest(new HttpGet(), url, body, null);
		}

		public static SGRequestResult HttpPost(String url, String body) {
			return HttpRequest(new HttpPost(), url, body, null);
		}
		/**
		 * 文件上传支持中文 注意postBody里的每个参数，在controller里似乎只能用String或String[]等简单类型接收，原因不明
		 *
		 * @param reportUrl
		 * @throws IOException
		 */
		public static void HttpPostFile(String reportUrl, File file, // String filepath,
				Map<String, Object> postBody) {
			CloseableHttpClient closeableHttpClient = null;
			CloseableHttpResponse response = null;
			try {
				closeableHttpClient = HttpClients.createDefault();
				HttpPost httpPost = new HttpPost(reportUrl);

				RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(200000).setSocketTimeout(200000)
						.build();
				httpPost.setConfig(requestConfig);

				// String filename = filepath.split("/")[filepath.split("/").length - 1];
//	            File file = new File(filepath);

//	            String filename = file.getName(); 
//	            FileBody fileBody = new FileBody(file, ContentType.create("multipart/form-data", "UTF-8"), filename);

//	            StringBody userIdStringBody = new StringBody(userId, ContentType.create("multipart/form-data", "UTF-8"));
//	            StringBody systemNameStringBody = new StringBody(systemName, ContentType.create("multipart/form-data", "UTF-8"));
//	            StringBody reportNameStringBody = new StringBody(filename, ContentType.create("multipart/form-data", "UTF-8"));

//	            HttpEntity httpEntity = MultipartEntityBuilder
//	                    .create()
//	                    .setCharset(Charset.forName("UTF-8"))
//	                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
//	                    .addPart("file", fileBody)
//	                    .addPart("userId", userIdStringBody)
//	                    .addPart("systemName", systemNameStringBody)
//	                    .addPart("reportName", reportNameStringBody)
//	                    .build();
//	            MultipartEntityBuilder httpBuilder=MultipartEntityBuilder
//	                    .create()
//	                    .setCharset(Charset.forName("UTF-8"))
//	                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
//	                    .addPart("file", fileBody);

				MultipartEntityBuilder httpBuilder = MultipartEntityBuilder.create().setCharset(Charset.forName("UTF-8"))
						.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

				if (file != null) {
					String filename = file.getName();
					FileBody fileBody = new FileBody(file, ContentType.create("multipart/form-data", "UTF-8"), filename);
					httpBuilder.addPart("file", fileBody);
				}

				Iterator<Entry<String, Object>> postBodyIter = postBody.entrySet().iterator();
				while (postBodyIter.hasNext()) {
					Entry<String, Object> key = postBodyIter.next();
					Object value = key.getValue();
					if (value instanceof String) {
						httpBuilder.addPart(key.getKey(),
								new StringBody(com.sellgirl.sgJavaHelper.config.SGDataHelper.ObjectToString(value), ContentType.create("multipart/form-data", "UTF-8")));
					} else {
						httpBuilder.addPart(key.getKey(),
								new StringBody(JSON.toJSONString(value), ContentType.APPLICATION_JSON));
						// httpBuilder.addTextBody(key.getKey(),JSON.toJSONString(value),
						// ContentType.APPLICATION_JSON);
						// httpBuilder.addBinaryBody(key.getKey(), new
						// StringBody(JSON.toJSONString(value), ContentType.APPLICATION_JSON));
					}
				}

				HttpEntity httpEntity = httpBuilder.build();

				httpPost.setEntity(httpEntity);
				response = closeableHttpClient.execute(httpPost);
				HttpEntity responseEntity = response.getEntity();
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					System.out.println("上传成功");
				} else {
					BufferedReader reader = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
					StringBuffer buffer = new StringBuffer();
					String str = "";
//	                while ((str = reader.readLine()) != null && (str = reader.readLine()).trim().length() > 0) {
//	                    buffer.append(str);
//	                }
					while ((str = reader.readLine()) != null && str.trim().length() > 0) {
						buffer.append(str);
					}
					System.out.println(buffer.toString());
					System.out.println("上传失败：" + statusCode);
				}
				closeableHttpClient.close();
				if (response != null) {
					response.close();
				}

			} catch (Exception ex) {
				System.out.println("uploadReport发生异常：" + ex);
			} finally {
				try {
					if (closeableHttpClient != null) {
						closeableHttpClient.close();
					}
					if (response != null) {
						response.close();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

}
