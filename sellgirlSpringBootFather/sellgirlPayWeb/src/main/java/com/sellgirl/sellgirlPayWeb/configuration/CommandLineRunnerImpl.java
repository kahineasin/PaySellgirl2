package com.sellgirl.sellgirlPayWeb.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sellgirl.sellgirlPayWeb.controller.PrincessController;

//import com.perfect99.pfTransferTask.form.PfTransferTaskForm;
//import com.perfect99.pfTransferTask.projHelper.ITransferTimeTaskHelper;
//import com.perfect99.pfTransferTask.projHelper.SellGirlTransferTimeTaskHelper;
//import com.perfect99.pfTransferTask.projHelper.TransferTaskHelper;
//import com.perfect99.pfTransferTask.projHelper.TransferTimeTaskHelper;
//import com.perfect99.pfTransferTask.projHelper.config.ProjHelper;
//import com.perfect99.pfTransferTask.projHelper.tasks.*;
//import com.perfect99.pfTransferTask.service.YJQueryStaticService;

//import com.sellgirl.sgJavaHelper.DirectNode;
import com.sellgirl.sgJavaHelper.PFCmonth;
import com.sellgirl.sgJavaHelper.SGDate;
//import com.sellgirl.sgJavaHelper.PFEmailSend;
//import com.sellgirl.sgJavaHelper.PFSqlTransferItem;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
//import com.sellgirl.sgJavaHelper.model.PFEnvir;

/**
 * 自写的swt式管理任务ui createTime 2018-11-07 22:25
 **/
@Component
@Order(2) // 指定顺序
public class CommandLineRunnerImpl implements CommandLineRunner {
	public static Thread formThread = null;
	/**
	 * true: 在linux云端运行
	 * false: swt的UI管理方式
	 */
	private static boolean runInCloud = false;
	//private static boolean runInCloud = true;

	@Override
	public void run(String... args) throws Exception {

		if (runInCloud) {
			//RunCloudSpark();
		} else {
			RunFormAsync();
		}
	}

	public static Thread RunFormAsync() {
		formThread = new Thread() {// 线程操作//如果不放到线程里,会阻碍bean完成,导致SpringJUnit4ClassRunner等待bean完成但一直等不到,最终单元测试启动不了
			public void run() {
				RunForm();
			}
		};
		formThread.start();
		return formThread;
	}

	public static void RunForm() {
		// DriverManager.registerDriver(new net.sourceforge.jtds.jdbc.Driver());
		try {
	    	new com.sellgirl.sgJavaHelper.config.SGDataHelper(com.sellgirl.sgJavaSpringHelper.config.SGDataHelper.GetAppConfig().toNotSpring());
//			PFDataHelper.SysMinMonth = YJQueryStaticService.GetMinCmonth();
//			PFDataHelper.SysMaxMonth = YJQueryStaticService.GetMaxCmonth();
	    	PrincessController.InitCurrentImgTypeByLocalText();
		} catch (Exception e) {
			SGDataHelper.SysMaxMonth = new PFCmonth() {
				{
					setCmonth(SGDate.Now().toCmonth());
				}
			};
			SGDataHelper.SysMaxMonth = new PFCmonth() {
				{
					setCmonth(SGDate.Now().AddMonths(12).toCmonth());
				}
			};
		}

//		ITransferTimeTaskHelper helper=PFEnvir.sellgirl==PFDataHelper.CurrentEnvironmental
//				? new SellGirlTransferTimeTaskHelper()
//						:new TransferTimeTaskHelper();
//		helper.GenerateTask();
//		if (!PFDataHelper.IsDebug()) {
//			//helper.RunTask();
//			TransferTimeTaskHelper.RunTask();
//		}
//		if (runInCloud) {// 在云上不需要win管理,也不用邮件吧
//
//		} else {
//			//helper.ListenEmail();// 内存超，暂不使用试试 D:\wxj\workRecord\20210223_javaTask内存问题\20210223.txt
//			TransferTimeTaskHelper.ListenEmail(helper);// 内存超，暂不使用试试 D:\wxj\workRecord\20210223_javaTask内存问题\20210223.txt
//			ProjHelper._mainForm = new PfTransferTaskForm();
//		}
	}

//	private static void RunCloudSpark() {
//		long[] taskBeginTime = new long[] { PFDate.Now().ToCalendar().getTimeInMillis() };
//
//		DirectNode n0 = new DirectNode();
//		PFDate settleMonth = PFDate.Now().AddMonths(-1);
//		PFSqlTransferItem n1 = TransferTaskHelper.GetDirectTask(settleMonth, new tag_monthly_user());
//		PFSqlTransferItem n2 = TransferTaskHelper.GetDirectTask(settleMonth, new tag_monthly_user_order());
//
//		DirectNode n99 = new DirectNode();
//		n0.addNext( n1,n2);
//		n99.addPrev( n1,n2);
//		Boolean b = TransferTaskHelper.TransferDirect(n0, n99, a -> {
//			// 这里可以监听有向图任务的进度
//		});
//
//		PFEmailSend.SendMail(new String[] { "wxj@perfect99.com" }, "RunCloudSpark_全部完成",
//				"总时间" + PFDataHelper.GetTimeSpan(PFDate.Now().ToCalendar().getTimeInMillis() - taskBeginTime[0], null));
//
//	}
}