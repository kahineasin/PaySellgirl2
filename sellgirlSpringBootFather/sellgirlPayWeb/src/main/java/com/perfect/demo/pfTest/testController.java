package com.perfect.demo.pfTest;

//import com.aliyun.openservices.shade.com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

public class testController {
    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) throws Exception{
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = null;
//            URIBuilder uriBuilder = new URIBuilder("http://192.168.0.69:8089/procedure/getInvStock");
//            String url="http://127.0.0.1:8089/procedure/seekinvallkc2wmpsjk";
            String url="http://localhost:38201/crm/gethyzlchange";
            URIBuilder uriBuilder = new URIBuilder(url);

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("inv_no", "ACC"));
            uriBuilder.setParameters(nvps);
            // 根据带参数的URI对象构建GET请求对象
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.setHeader("Authorization", "appid=408d8304d4c36da99a5c01486fd913ad,appkey=AppDDOOECDFllsdfwfeooweDdsfew,token=e01771d4a0664ce3a0d1e21074072be5");
            // 执行请求
            response = httpClient.execute(httpGet);
            // 获得响应的实体对象
            HttpEntity entity = response.getEntity();
/*            // 使用Apache提供的工具类进行转换成字符串
            String entityStr = EntityUtils.toString(entity, "UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(entityStr);*/
            System.out.println(JSON.toJSONString(entity));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
