package com.sellgirl.sgGameHelper;

//import java.util.List;

import com.sellgirl.sgGameHelper.gamepad.ISGPS5Gamepad;
import com.sellgirl.sgGameHelper.gamepad.SGPS5GamepadSetting;
//import com.sellgirl.sgJavaHelper.sql.ISGJdbc;
//import com.sellgirl.sgJavaHelper.sql.ISqlExecute;

public interface ISGGameConfig
{
////	Boolean IsEmpty();
//    List<PFModelConfigMapper> GetModelConfigMapper();
//
//    PFPathConfig GetPathConfig();
//    PFNetworkConfig GetNetworkConfig();
//
//    /**
//     * 如果想使用clickhouse,引用pfHelperClickHouse,
//     * 然后实现此方法,返回 new PFClickHouseSqlExecute(jdbc)
//     * @param jdbc
//     * @return
//     * @throws Exception
//     */
//    ISqlExecute GetClickHouseSqlExecute(ISGJdbc jdbc) throws Exception;
//    PFRedisConfig GetRedisConfig();
//    /**
//     * 发送手机短信的方法
//     * @param msg
//     * @return
//     */
//    boolean SendMobileMessage(String[] mobileNumber,String msg);
//
//    /**
//     * 便于自定义注入框架的实例化方法,如:
//     * 1.spring的 (DailyDataMapper)SpringContextUtil.getBean("dailyDataMapper")
//     *   或
//     *   return PerfectApplication.ctx.getBean(type);//ctx=SpringApplication.run(PerfectApplication.class, args);
//     * 2.google inject的 injector.getInstance(XSchedulerWebServer.class);
//     * @param type
//     * @return
//     */
//    <T> T getBeanInstance(Class<T> type);
	int getGdxControllerVersion();
	ScreenSetting getScreenSetting();
	/**
	 * 读取用户的手柄配置，主要为摇杆的不相应区间（防漂移）
	 * @param pad
	 * @return
	 */
	SGPS5GamepadSetting getGamepadSetting(ISGPS5Gamepad pad);
}
