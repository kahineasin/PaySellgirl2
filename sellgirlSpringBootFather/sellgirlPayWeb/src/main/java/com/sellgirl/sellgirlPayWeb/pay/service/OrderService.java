package com.sellgirl.sellgirlPayWeb.pay.service;


import org.springframework.stereotype.Service;

import com.sellgirl.sellgirlPayWeb.configuration.jdbc.JdbcHelper;
import com.sellgirl.sellgirlPayWeb.pay.model.OrderStatus;
import com.sellgirl.sellgirlPayWeb.pay.model.vipOrder;
import com.sellgirl.sellgirlPayWeb.pay.model.vipOrderCreate;
import com.sellgirl.sellgirlPayWeb.user.model.PayPlan;
import com.sellgirl.sgJavaHelper.SGDataTable;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.SGSqlCommandString;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.sql.ISGJdbc;
import com.sellgirl.sgJavaHelper.sql.ISqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlUpdateCollection;
import com.sellgirl.sgJavaHelper.sql.SGSqlInsertCollection;
//import com.wechat.pay.java.service.profitsharing.model.OrderStatus;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecute;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderService {

    private  String TAG="OrderService";
    // 模拟订单存储：key=商户订单号，value=支付状态（0-未支付，1-已支付）
    private final Map<String, Integer> orderMap = new ConcurrentHashMap<>();

    public String createOrder(String outTradeNo) {
        orderMap.put(outTradeNo, 0); // 初始状态未支付
        return outTradeNo;
    }

    public void markPaid(String outTradeNo) {
        orderMap.put(outTradeNo, 1);
    }

    public int getPayStatus(String outTradeNo) {
        return orderMap.getOrDefault(outTradeNo, -1);
    }

    public SGDataTable GetvipOrderById(long id)
    {
		ISGJdbc jdbc=JdbcHelper.GetShop();
		try (ISqlExecute sql = SGSqlExecute.Init(jdbc)) {
		    return sql.GetOneRow("sg_vip_order",a->a.Add("vip_order_id", id));
		} catch (Throwable e) {
		    SGDataHelper.getLog().writeException(e, TAG);
		    return null;
		}
    }

	public  long insertvipOrder(vipOrderCreate m) {		

		ISGJdbc jdbc=JdbcHelper.GetShop();
		try (ISqlExecute dstExec = SGSqlExecute.Init(jdbc)) {						
			SGSqlInsertCollection insert=dstExec.getInsertCollection();			
			insert.InitItemByModel(m);
			
			SGSqlCommandString sql=new SGSqlCommandString(
					SGDataHelper.FormatString(
							"insert into sg_vip_order ({0}) values ({1})",
							insert.ToKeysSql(),
							insert.ToValuesSql())
					);
			int r=dstExec.ExecuteSqlInt(sql, null, true);
			if(0<r) {
				return dstExec.GetLastInsertedId();
			}
			//System.out.println("id2:"+dstExec.GetLastInsertedId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public  long updateOrderPaid(long id,String order_no) {		

		ISGJdbc jdbc=JdbcHelper.GetShop();
		try (ISqlExecute dstExec = SGSqlExecute.Init(jdbc)) {
			dstExec.AutoCloseConn(false);
            List<String> srcFieldNames = new ArrayList<String>();
            srcFieldNames.add("vip_order_id");
            srcFieldNames.add("status");
            srcFieldNames.add("order_no");
            ResultSetMetaData dstMd = dstExec.GetMetaDataNotClose("sg_vip_order", srcFieldNames);

            SGSqlUpdateCollection update = dstExec.getUpdateCollection(dstMd);

            
            String[] mArray = new String[] {"status","order_no"};
            String[] primaryKeys = new String[] {"vip_order_id"};
            update.UpdateFields(mArray);
            update.PrimaryKeyFields(false, primaryKeys);

            update.Set("vip_order_id", id);
            update.Set("status", com.sellgirl.sellgirlPayWeb.pay.model.OrderStatus.已支付.ordinal());
            update.Set("order_no", order_no);
            
			
			SGSqlCommandString sql=new SGSqlCommandString(
					SGDataHelper.FormatString(
							" update sg_vip_order set  {0} {1} limit 1",
							update.ToSetSql(),
			                update.ToWhereSql()
					));
			dstExec.close();
			int r=dstExec.ExecuteSqlInt(sql, null, true);
			dstExec.close();
			if(0<r) {
				return dstExec.GetLastInsertedId();
			}
			//System.out.println("id2:"+dstExec.GetLastInsertedId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
    public vipOrder GetOnevipOrder(long id)
    {
        vipOrder model = null;
        SGDataTable dt = GetvipOrderById(id);
        if (dt != null && !dt.IsEmpty())
        {
            model = dt.ToList(vipOrder.class,null).get(0);
        }
        return model;
    }

    public OrderStatus GetvipOrderStatusById(long id)
    {
		ISGJdbc jdbc=JdbcHelper.GetShop();
		try (ISqlExecute sql = SGSqlExecute.Init(jdbc)) {
//		    return SGDataHelper.ObjectToEnum(OrderStatus.class,sql.QuerySingleValue("select status from sg_vip_order where vip_order_id="+id));
		    return SGDataHelper.ObjectToEnum(OrderStatus.class,sql.QuerySingleValue("sg_vip_order","status",a->a.Add("vip_order_id", id)));
		} catch (Throwable e) {
		    SGDataHelper.getLog().writeException(e, TAG);
		    return null;
		}
    }

    public PayPlan GetvipOrderVipTypeById(long id)
    {
		ISGJdbc jdbc=JdbcHelper.GetShop();
		try (ISqlExecute sql = SGSqlExecute.Init(jdbc)) {
//		    return SGDataHelper.ObjectToEnum(OrderStatus.class,sql.QuerySingleValue("select status from sg_vip_order where vip_order_id="+id));
		    return SGDataHelper.ObjectToEnum(PayPlan.class,sql.QuerySingleValue("sg_vip_order","vip_type",a->a.Add("vip_order_id", id)));
		} catch (Throwable e) {
		    SGDataHelper.getLog().writeException(e, TAG);
		    return null;
		}
    }
    
}