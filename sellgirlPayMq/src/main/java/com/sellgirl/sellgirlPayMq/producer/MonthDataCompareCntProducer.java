package com.sellgirl.sellgirlPayMq.producer;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.sellgirl.sellgirlPayMq.PFMqHelper;
import com.sellgirl.sellgirlPayMq.model.CompareCnt;

import com.sellgirl.sgJavaHelper.PFEmail;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.PagingParameters;
import com.sellgirl.sellgirlPayMq.PFMqConfig;

public class MonthDataCompareCntProducer implements PFMqHelper.PFProdutResponseTask
{
    public static List<CompareCnt> Product(String backupDatabase) {
                    
        String resMsg = "";

        PagingParameters p = new PagingParameters();
        //p["backupDatabase"] = "bonus";
        p.Set("backupDatabase", "bonus");

        SGRef<PFEmail> pfEmail=new SGRef<PFEmail>(); 
        //PFDate st = PFDate.Now();
        Thread t = PFMqHelper.BuildProducer(JSON.toJSONString(p),
                new MonthDataCompareCntProducer(),pfEmail);
        try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//.Wait();

        //resMsg = t.Result.Body;
        resMsg = pfEmail.GetValue().getBody();
        List<CompareCnt> result = JSONObject.parseObject(resMsg, new TypeReference<java.util.List<CompareCnt>>(){}) ;
       return result;
    }
    //public static async Task<List<CompareCnt>> ProductAsync(string backupDatabase)
    //{
    //    var resMsg = "";

    //    var p = new PagingParameters();
    //    p["backupDatabase"] = "bonus";

    //    var st = DateTime.Now;
    //    var t = await PFMqHelper.BuildProducer(JsonConvert.SerializeObject(p),
    //            new MonthDataCompareCntProducer());
    //    //t.Wait();
    //    //var et = DateTime.Now;
    //    resMsg = t.Body;
    //    var result = JsonConvert.DeserializeObject<List<CompareCnt>>(resMsg);
    //    return result;
    //}
    public PFMqConfig GetMqConfig(PFMqConfig mqConfig)
    {
        //		//参数来自 wisesystem-bns
        //		//mqConfig=new PFMqConfig();
        //		mqConfig.setGroupId("CID_PERFECT_PRS_INTERFACE_UAT");
        //		mqConfig.setAccessKey("JVczMfdmcZ3MCVX4");
        //		mqConfig.setSecretKey("gpgYF9JEaf4xlcKQtLKFiIhwBI5Vm8");
        //		//mqConfig.setNameSrvAddr("http://mq.server.icloud.topone.local:8080/rocketmq/nsaddr4client-internal");
        //		mqConfig.setNameSrvAddr(null);
        ////		mqConfig.setOnsAddr("http://mq.server.icloud.topone.local:8080/rocketmq/nsaddr4client-internal");
        //		//mqConfig.setOnsAddr("http://mq.server.icloud.topone.local/rocketmq/nsaddr4client-internet");
        mqConfig.setMqType(PFMqHelper.PFMqType.PFEmailMq);
        //mqConfig.setTopic(MqTopic.TcTask_MonthDataCompareCnt.ToString());
        mqConfig.setTopic("TcTask_MonthDataCompareCnt");
        //mqConfig.setTag("OPEN_CARD/EDIT_CARD");
        //mqConfig.setTag("DOMSORDER");
        return mqConfig;
    }
}