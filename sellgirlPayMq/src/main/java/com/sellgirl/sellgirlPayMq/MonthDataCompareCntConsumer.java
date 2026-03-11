package com.sellgirl.sellgirlPayMq;

import java.util.ArrayList;
import java.util.List;

import com.sellgirl.sellgirlPayMq.model.CompareCnt;

/**
 * 测试通过 
 * public void testSendPFEmailMq() throws Exception
 * @author Administrator
 *
 */
public class MonthDataCompareCntConsumer implements PFMqHelper.PFConsumerResponseTask
{
    public Object handle(String consumerTag, PFMqMessage message)
    {
//        var lastCMonth = ProjDataHelper.GetLastCMonthByDate(DateTime.Now);
//
//        //return DbReportTask.GetZxbmonthEMailBody(backupDatabase, lastCMonth);
//
//        var p = JsonConvert.DeserializeObject<PagingParameters>(message.ToString());
//        var backupDatabase = PFDataHelper.ObjectToString(p["backupDatabase"]) ?? "";
//        var service = new DbReportService(lastCMonth);
//        var list = service.GetCompareCntList(backupDatabase, false);

    	List<CompareCnt> list = new ArrayList<CompareCnt>();
    	CompareCnt item=new CompareCnt();
    	item.SrcTbName="aaa";
    	item.Total=555;
    	list.add(item);
        return list;
        
    }

    public PFMqConfig GetMqConfig(PFMqConfig mqConfig)
    {
        mqConfig.setMqType(PFMqHelper.PFMqType.PFEmailMq);
        //mqConfig.setTopic(MqTopic.TcTask_MonthDataCompareCnt.ToString());
        mqConfig.setTopic("TcTask_MonthDataCompareCnt");
        return mqConfig;
    }

}
