package org.sellgirl.sellgirlPayWeb.controller;

import junit.framework.TestCase;
import org.sellgirl.sellgirlPayWeb.controller.model.JdbcHelperTest;
import org.sellgirl.sellgirlPayWeb.controller.model.PFConfigTestMapper;

import com.sellgirl.sellgirlPayWeb.configuration.jdbc.JdbcHelper;
import com.sellgirl.sellgirlPayWeb.pay.model.vipOrder;
import com.sellgirl.sellgirlPayWeb.pay.service.OrderService;
import com.sellgirl.sellgirlPayWeb.pay.service.ZPayNativeService;
import com.sellgirl.sellgirlPayWeb.user.service.UserService;
import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.config.PFAppConfig;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.sql.ISGJdbc;
import com.sellgirl.sgJavaHelper.sql.ISqlExecute;
import com.sellgirl.sgJavaHelper.sql.MycatMulitJdbcVersionTest;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecute;

import java.math.BigDecimal;
import java.sql.DriverManager;

public class UncheckVipOrder  extends TestCase {
    public static void initPFHelper() {
        SGDataHelper.SetConfigMapper(new PFConfigTestMapper());
        new SGDataHelper(new PFAppConfig());
    }
    public void testUpdateOrderStatus() {
    	JdbcHelper.setShop(JdbcHelperTest.GetSgShopJdbc());
    	OrderService orderService = new OrderService();
    	orderService.updateOrderPaid(2,"123");
    	System.out.println(orderService.GetvipOrderStatusById(2));
    }

    public void testUpdateUserVip() {
    	JdbcHelper.setShop(JdbcHelperTest.GetSgShopJdbc());
    	UserService service=new UserService();
    	SGDate now=SGDate.Now();
    	service.updateUserVip(1, true, now, true, now);
    }

    /**
     * 退款
     */
    public void testRefund() {
    	JdbcHelper.setShop(JdbcHelperTest.GetSgShopJdbc());
    	OrderService orderService = new OrderService();
    	vipOrder order=orderService.GetOnevipOrder(25);
    	ZPayNativeService zPayService=new ZPayNativeService();
    	zPayService.refund(order.getVip_order_id(), order.getOrder_no(), order.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
//    	UserService service=new UserService();
//    	SGDate now=SGDate.Now();
//    	service.updateUserVip(1, true, now, true, now);
    }

    public void testGetOrder() {
    	JdbcHelper.setShop(JdbcHelperTest.GetSgShopJdbc());
    	OrderService orderService = new OrderService();
    	vipOrder order=orderService.GetOnevipOrder(25);
    	ZPayNativeService zPayService=new ZPayNativeService();
    	zPayService.getOrder(order.getVip_order_id());
//    	UserService service=new UserService();
//    	SGDate now=SGDate.Now();
//    	service.updateUserVip(1, true, now, true, now);
    }
}
