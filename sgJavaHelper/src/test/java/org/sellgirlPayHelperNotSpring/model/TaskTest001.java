package org.sellgirlPayHelperNotSpring.model;

import com.sellgirl.sgJavaHelper.PFSqlCommandTimeoutSecond;
import com.sellgirl.sgJavaHelper.SqlExpressionOperator;
import com.sellgirl.sgJavaHelper.TransferTaskBase;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.sql.ISqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGMySqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlTransferItem;
import com.sellgirl.sgJavaHelper.task.PFTaskBase;

/**
 * 测试一下 PFTaskBase.running在多线程下能不能防止StartThread重复执行
 * 测试结果好像不会有多线程问题
 * 好像running是有线程安全问题
 * @author Administrator
 *
 */
@SuppressWarnings("unused")
public class TaskTest001 extends PFTaskBase{

//	private int number=0;
	@Override
	protected void StartThread() {
		//number++;
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		//number--;
		
	}

}


