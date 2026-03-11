package org.sellgirl.sellgirlPayWeb.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.validator.internal.util.stereotypes.ThreadSafe;
import org.sellgirl.sellgirlPayWeb.controller.model.JdbcHelperTest;
import org.sellgirl.sellgirlPayWeb.controller.model.TestModel001;

import com.alibaba.fastjson.JSON;
import com.sellgirl.sellgirlPayWeb.configuration.PFConfigMapper;
import com.sellgirl.sgJavaMvcHelper.HtmlHelper;
import com.sellgirl.sgJavaMvcHelper.PFGrid;

import junit.framework.TestCase;

import com.sellgirl.sgJavaHelper.DirectNode;
import com.sellgirl.sgJavaHelper.PFCmonth;
import com.sellgirl.sgJavaHelper.PFDataRow;
import com.sellgirl.sgJavaHelper.SGDataTable;
import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.SGHttpHelper;
import com.sellgirl.sgJavaHelper.PFFunc3;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.SGRequestResult;
import com.sellgirl.sgJavaHelper.PFSqlCommandTimeoutSecond;
import com.sellgirl.sgJavaHelper.SGSqlFieldInfo;
import com.sellgirl.sgJavaHelper.config.PFAppConfig;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.config.SGDataHelper.LocalDataType;
import com.sellgirl.sgJavaHelper.sql.ISGJdbc;
import com.sellgirl.sgJavaHelper.sql.ISqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGMySqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlCreateTableCollection;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecuteBase;
import com.sellgirl.sgJavaHelper.sql.SqlCreateTableItem;

@SuppressWarnings("unused")
public class Uncheck001 extends TestCase {
	public static void initPFHelper() {
		//PFDataHelper.SetConfigMapper(new PFConfigTestMapper());		
		SGDataHelper.SetConfigMapper(new PFConfigMapper());		
		new SGDataHelper(new PFAppConfig());
	}
	public void testTreeListFilter() {
		initPFHelper();
		
		//List<TestModel001> data=new ArrayList<TestModel001>();
		
		TestModel001 n0=new TestModel001();
		
		TestModel001 n1=new TestModel001();
		n1.setF01("n1_f01");
		n1.setF02("n1_f02");
		
		TestModel001 c1=new TestModel001();
		c1.setF01("n1_c1_f01");
		c1.setF02("n1_c1_f02");
		n1.Children=new ArrayList<TestModel001>();
		n1.Children.add(c1);

		TestModel001 n2=new TestModel001();
		n2.setF01("n2_f01");
		n2.setF02("n2_f02");
		
		TestModel001 c2=new TestModel001();
		c2.setF01("n2_c2_f01");
		c2.setF02("n2_c2_f02");
		n2.Children=new ArrayList<TestModel001>();
		n2.Children.add(c2);

		n0.Children=new ArrayList<TestModel001>();
		n0.Children.add(n1);
		n0.Children.add(n2);
		
		//末级一致
//		n0.FilterByLeaf(a->a.getF02().equals("n1_c1_f02"));
		//n0.FilterByLeaf2(a->a.getF02().equals("n1_c1_f02"));

		
		//不是末级应该无数据
		//n0.FilterByLeafOld(a->a.getF02().equals("n2_f02"));
		//n0.FilterByLeaf(a->a.getF02().equals("n2_f02"));
		
		n0.FilterByLeaf(a->a.getF02().equals("n2_c2_f02"));
		
		PFGrid component = new PFGrid();
		//component.SetModel(data);
		component.SetModel(n0.Children);
		
		component.ColumnsForAll("yjquery");
      //if (action != null) { action(component); }
		HtmlHelper htmlHelper=new HtmlHelper(new HashMap<String,Object>()) ;
//		HtmlHelper htmlHelper=new HtmlHelper(new HashMap<String,Object>(){{put("hasPFGridCss",true);}}) ;
      String s= component.Html(htmlHelper);
      System.out.print(s);
      String aa="aa";
	}
	public void testTreeGrid() {
		initPFHelper();
		List<TestModel001> data=new ArrayList<TestModel001>();
		for(int i=0;i<10;i++) {
			TestModel001 item=new TestModel001();
			item.setF01("f01_"+String.valueOf(i));
			item.setF02("f02_"+String.valueOf(i));

			TestModel001 c=new TestModel001();
			c.setF01("f01_"+String.valueOf(i)+"_c");
			c.setF02("f02_"+String.valueOf(i)+"_c");
			item.Children=new ArrayList<TestModel001>();
			item.Children.add(c);
			data.add(item);	
		}
		PFGrid component = new PFGrid();
		component.SetModel(data);
		
		component.ColumnsForAll("yjquery");
      //if (action != null) { action(component); }
		HtmlHelper htmlHelper=new HtmlHelper(new HashMap<String,Object>()) ;
//		HtmlHelper htmlHelper=new HtmlHelper(new HashMap<String,Object>(){{put("hasPFGridCss",true);}}) ;
      String s= component.Html(htmlHelper);
      System.out.print(s);
      String aa="aa";
	}

	public void testDirectNode() {
		initPFHelper();
		DirectNode n0=new DirectNode();
		DirectNode n1=new DirectNode();
		DirectNode n2=new DirectNode();
		DirectNode n3=new DirectNode();
		DirectNode n4=new DirectNode();
		
		n0.setHashId("0");
		n1.setHashId("1");
		n2.setHashId("2");
		n3.setHashId("3");
		n4.setHashId("4");
		
		PFFunc3<DirectNode, Object, Object,Boolean> successAction=(a,b,c)->{
			a.setFinishedPercent(100);
			return true;
		};
		PFFunc3<DirectNode, Object, Object,Boolean> errorAction=(a,b,c)->{
			a.setFinishedPercent(50);
			return false;
		};
		n1.action=successAction;
		n2.action=successAction;
		n3.action=errorAction;
		
		/**
		 *   0 -> 1       ->4
		 *        2 -> 3  ->
		 */
		n0.addNext(n1,n2);
		n2.addNext(n3);
		n4.addPrev(n1,n3);
		
		n0.GoSync(null);
		
		String s=SGDataHelper.FormatString("{0}执行失败,各节点完成情况:\r\n{1}", 
				"testTask",
				String.join("\r\n", 
						n0.getAllNodeFinishedPercent().getProgressItem().stream()
						.map(a->SGDataHelper.FormatString("节点{0}\t{1}%\t{2}", a.getHashId(),a.getFinishedPercent(),a.isSuccess())
						).collect(Collectors.toList())
				)
				);
	      System.out.print(s);
	}
//	/**
//	 * 其实 有向图 根本不应该用 treegrid来展示
//	 */
//	public void testTreeGrid2() {
//		initPFHelper();
//		List<DirectNode> data=new ArrayList<DirectNode>();
//		DirectNode n0=new DirectNode();
//		DirectNode n1=new DirectNode();
//		DirectNode n2=new DirectNode();
//		DirectNode n3=new DirectNode();
//		
//		n0.setHashId("0");
//		n1.setHashId("1");
//		n2.setHashId("2");
//		n3.setHashId("3");
//		
//		n0.addNext(n1,n2);
//		n3.addPrev(n1,n2);
//
//		data.add(n0);
//		PFGrid component = new PFGrid();
//		component.SetModel(data);
//		
////		component.ColumnsForAll("yjquery");
//		component.ColumnsFor("yjquery",(a,b,c)->{
//			a.Add("hashId");
//		});
//      //if (action != null) { action(component); }
//		HtmlHelper htmlHelper=new HtmlHelper(new HashMap<String,Object>()) ;
////		HtmlHelper htmlHelper=new HtmlHelper(new HashMap<String,Object>(){{put("hasPFGridCss",true);}}) ;
//      String s= component.Html(htmlHelper);
//      System.out.print(s);
//      String aa="aa";
//	}

//	public void testTreeGrid2() {
//
//		List<TestModel001> data=new ArrayList<TestModel001>();
//		for(int i=0;i<10;i++) {
//			TestModel001 item=new TestModel001();
//			item.setF01("f01_"+String.valueOf(i));
//			item.setF02("f02_"+String.valueOf(i));
//
//			TestModel001 c=new TestModel001();
//			c.setF01("f01_"+String.valueOf(i)+"_c");
//			c.setF02("f02_"+String.valueOf(i)+"_c");
//			item.Children=new ArrayList<TestModel001>();
//			item.Children.add(c);
//			data.add(item);	
//		}
//		PFGrid component = new PFGrid();
//		component.SetModel(data);
//		
//		component.ColumnsForAll("yjquery");
//      //if (action != null) { action(component); }
//		HtmlHelper htmlHelper=new HtmlHelper(new HashMap<String,Object>());
//      String s= component.Html(htmlHelper);
//      System.out.print(s);
//      String aa="aa";
//	}

	public void testCreateClickHouseTable02() throws Exception {
		SGDataHelper.SetConfigMapper(new PFConfigMapper());

		//IPFJdbc srcJdbc = JdbcHelperTest.GetYJQueryJdbc();
		ISGJdbc dstJdbc = org.sellgirl.sellgirlPayWeb.controller.model.JdbcHelperTest.GetClickHouseTestJdbc();


		SGSqlCreateTableCollection models = SGSqlCreateTableCollection.Init(dstJdbc);
		models.DbName = "perfect_test";
		models.TableName = "test_tb_02";
		models.Order = new String[] { "group_name","json" };
		SqlCreateTableItem item1=new SqlCreateTableItem();
		item1.FieldName="group_name";
		item1.FieldType=String.class;
		models.add(item1);
		SqlCreateTableItem item2=new SqlCreateTableItem();
		item2.FieldName="json";
		item2.FieldType=String.class;
		models.add(item2);

		
		ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
		Connection conn = dstExec.GetConn();
//	     
		dstExec.ExecuteSql(models.ToSql());

	}
	

	private <T> void PrintObject(T o) {
		//System.out.println(JSON.toJSONString(o));
		System.out.println("--------------");
		String s=JSON.toJSONString(o);
		String[] sa=s.split("\\\\r\\\\n");
		for(String i : sa) {
			System.out.println(i.replaceAll("\\\\t", "  "));
		}
		System.out.println("--------------");
	}
//	/**
//	 * 比对数据
//	 * 订单差异总结,电子商务和里格的订单差异为:
//	 * 1.隔月退货单(电子商务多)
//2.调差单(报表系统多)
//2.非正常退货单(电子商务多)
//	 */
//	public void testFindTableRowDifferenceOld() {		
//		try {
//			
//			IPFJdbc srcJdbc =  JdbcHelperTest.GetLiGeShopJdbc();
//			IPFJdbc dstJdbc =  JdbcHelperTest.GetYJQueryJdbc();
//			String srcSql="select order_no as orderno,\r\n" + 
//					" case when o.is_promotion = 1 then o.order_amount-o.promotion_discount when ex.order_route = 5 then o.order_amount-o.promotion_discount else o.order_amount end as Totalmoney,\r\n" + 
//					"  o.order_amount-o.promotion_discount  as Totalmoney2 -- 叶理胜20220526\r\n" + 
//					"from mall_center_order.order_order o \r\n" + 
//					"left join mall_center_order.order_order_ext ex on o.id=ex.order_id \r\n" + 
//					"where order_status in (2,3,99) \r\n" + 
//					"-- and order_type in (1,2,4) \r\n" + 
//					"and order_month ='202204' and del = 0 "+
//					"and order_no in('SG908170220415000073','SG911095220415000172','SG932764220415000062')"
//					;
//			String dstSql="select orderno, Totalmoney,invalid from  yjquery202204.dbo.orders  "
//					+" where orderno in('SG908170220415000073','SG911095220415000172','SG932764220415000062','SG920096220415000146')"
//					//+"order by orderno asc"
//					;
//			ISqlExecute srcExec = PFSqlExecute.Init(srcJdbc);
//			String[] joinColumn=new String[] {"orderno"}; 
//			String[] compareColumn=new String[] {"Totalmoney"}; 
//			long[] beginTime = new long[] { 0 };
//			beginTime[0]=PFDate.Now().ToCalendar().getTimeInMillis();
//			PFDataTable compareTable=((PFSqlExecuteBase)srcExec).FindTableRowDifferenceOld(dstJdbc, srcSql, dstSql, joinColumn, compareColumn, (cnt)->{
//				this.PrintObject("进度");
//				PFDate now = PFDate.Now();
//				long m = now.ToCalendar().getTimeInMillis();
//				String speedText=PFDataHelper.FormatString("speed:{0}",
//						PFDataHelper.ScientificNotation(
//								new Double(cnt) * 1000 / (m - beginTime[0])) + "条/秒");
//				this.PrintObject(PFDataHelper.FormatString("进度:{0}\r\n速度:{1}",cnt, speedText));
//				//this.PrintObject(cnt);
//			}, (dt)->{
//				this.PrintObject(PFDataHelper.FormatString("result更新\r\n{0}",dt));
////				this.PrintObject(dt);
//			},null);
//			this.PrintObject(compareTable.ToDictList());
//			this.PrintObject(PFDataHelper.FormatString("总耗时:{0}", PFDataHelper.GetTimeSpan(
//					PFDate.Now().ToCalendar().getTimeInMillis() - beginTime[0],
//					null)));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//				
//	}
	/**
	 * 比对数据
	 * 订单差异总结,电子商务和里格的订单差异为:
	 * 1.隔月退货单(电子商务多)
2.调差单(报表系统多)
2.非正常退货单(电子商务多)
	 */
	public void testFindTableRowDifference() {		
		try {
			
			ISGJdbc srcJdbc =  JdbcHelperTest.GetLiGeShopJdbc();
			ISGJdbc dstJdbc =  JdbcHelperTest.GetYJQueryJdbc();
			String srcSql="select order_no as orderno,\r\n" + 
					" case when o.is_promotion = 1 then o.order_amount-o.promotion_discount when ex.order_route = 5 then o.order_amount-o.promotion_discount else o.order_amount end as Totalmoney,\r\n" + 
					"  o.order_amount-o.promotion_discount  as Totalmoney2 -- 叶理胜20220526\r\n" + 
					"from mall_center_order.order_order o \r\n" + 
					"left join mall_center_order.order_order_ext ex on o.id=ex.order_id \r\n" + 
					"where order_status in (2,3,99) \r\n" + 
					"-- and order_type in (1,2,4) \r\n" + 
					"and order_month ='202204' and del = 0 "
					//+"and order_no in('SG908170220415000073','SG911095220415000172','SG932764220415000062')"
					;
			String dstSql="select orderno, Totalmoney,invalid from  yjquery202204.dbo.orders  "
					//+" where orderno in('SG908170220415000073','SG911095220415000172','SG932764220415000062','SG920096220415000146')"
					//+"order by orderno asc"
					;
			ISqlExecute srcExec = SGSqlExecute.Init(srcJdbc);
			String[] joinColumn=new String[] {"orderno"}; 
			String[] compareColumn=new String[] {"Totalmoney"}; 
			long[] beginTime = new long[] { 0 };
			beginTime[0]=SGDate.Now().ToCalendar().getTimeInMillis();
			SGDataTable compareTable=srcExec.FindTableRowDifference(dstJdbc, srcSql, dstSql, joinColumn, compareColumn, (cnt,total,compareCnt)->{
				this.PrintObject("进度");
				SGDate now = SGDate.Now();
				long m = now.ToCalendar().getTimeInMillis();
				String speedText=SGDataHelper.FormatString("speed:{0}",
						SGDataHelper.ScientificNotation(
								new Double(cnt) * 1000 / (m - beginTime[0])) + "条/秒");
				//this.PrintObject(PFDataHelper.FormatString("进度:{0}\r\n速度:{1}",cnt, speedText));
				this.PrintObject(SGDataHelper.FormatString("进度:{0}\r\n速度:{1}\r\n差异{2}",cnt, speedText,compareCnt));
				//this.PrintObject(cnt);
			}, (dt)->{
				this.PrintObject(SGDataHelper.FormatString("result更新\r\n{0}",dt));
//				this.PrintObject(dt);
			},null);
			this.PrintObject(compareTable.ToDictList());
			this.PrintObject(SGDataHelper.FormatString("总耗时:{0}", SGDataHelper.GetTimeSpan(
					SGDate.Now().ToCalendar().getTimeInMillis() - beginTime[0],
					null)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	/**
	 * 对比2022.05月份的日结订单,大概 624910  行
	 * 速度: 612743.0 条/2分钟
	 */
	public void testFindTableRowDifference2() {		
		try {
			
//			IPFJdbc srcJdbc =  JdbcHelperTest.GetLiGeShopJdbc();
//			IPFJdbc dstJdbc =  JdbcHelperTest.GetYJQueryJdbc();
			ISGJdbc srcJdbc = JdbcHelperTest.GetLiGeOrderJdbc();
			ISGJdbc dstJdbc = JdbcHelperTest.GetDayJdbc();

//			String srcSql="select order_no as orderno,\r\n" + 
//					" case when o.is_promotion = 1 then o.order_amount-o.promotion_discount when ex.order_route = 5 then o.order_amount-o.promotion_discount else o.order_amount end as Totalmoney,\r\n" + 
//					"  o.order_amount-o.promotion_discount  as Totalmoney2 -- 叶理胜20220526\r\n" + 
//					"from mall_center_order.order_order o \r\n" + 
//					"left join mall_center_order.order_order_ext ex on o.id=ex.order_id \r\n" + 
//					"where order_status in (2,3,99) \r\n" + 
//					"-- and order_type in (1,2,4) \r\n" + 
//					"and order_month ='202204' and del = 0 "
//					//+"and order_no in('SG908170220415000073','SG911095220415000172','SG932764220415000062')"
//					;
			PFCmonth pfCmonth=new PFCmonth();
			pfCmonth.setCmonth("2022.04");
			String cmonth=pfCmonth.getCmonth();
			String ym=pfCmonth.getYm();
			String srcSql =SGDataHelper.ReadLocalTxt("test03.txt",  LocalDataType.Deletable).replace("{ym}", ym);
			String dstSql=SGDataHelper.ReadLocalTxt("test04.txt",  LocalDataType.Deletable).replace("{cmonth}", cmonth);
			ISqlExecute srcExec = SGSqlExecute.Init(srcJdbc);
			ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
			String[] joinColumn=new String[] {"Orderno"}; 
			//String[] compareColumn=new String[] {"Totalmoney"}; 
			String tableName="t_orders_recent";
			List<SGSqlFieldInfo> compareColumnInfo=dstExec.GetTableFields(tableName); 
			
			//String[] compareColumn=PFDataHelper.ListSelect(compareColumnInfo, a->a.getFieldName()).toArray(new String[compareColumnInfo.size()]);

			String[] compareColumn=compareColumnInfo.stream().filter(a->{
				return !SGDataHelper.ArrayAny(joinColumn, b->b.equals(a.getFieldName()));
				//return true;
			})
			.map(a->a.getFieldName()).toArray(String[]::new);
			;
			
			long[] beginTime = new long[] { 0 };
			beginTime[0]=SGDate.Now().ToCalendar().getTimeInMillis();
			
			SGDataTable compareTable=srcExec.FindTableRowDifference(dstJdbc, srcSql, dstSql, joinColumn, compareColumn, (cnt,total,compareCnt)->{
				this.PrintObject("进度");
				SGDate now = SGDate.Now();
				long m = now.ToCalendar().getTimeInMillis();
				String speedText=SGDataHelper.FormatString("speed:{0}",
						SGDataHelper.ScientificNotation(
								new Double(cnt) * 1000 / (m - beginTime[0])) + "条/秒");
				//this.PrintObject(PFDataHelper.FormatString("进度:{0}\r\n速度:{1}",cnt, speedText));
				this.PrintObject(SGDataHelper.FormatString("进度:{0}\r\n速度:{1}\r\n差异{2}",cnt, speedText,compareCnt));
				//this.PrintObject(cnt);
			}, (dt)->{
				//this.PrintObject(PFDataHelper.FormatString("result更新\r\n{0}",dt.Rows.size()));
//				this.PrintObject(dt);
			},null);
			this.PrintObject(compareTable.ToDictList());
			this.PrintObject(SGDataHelper.FormatString("总耗时:{0}", SGDataHelper.GetTimeSpan(
					SGDate.Now().ToCalendar().getTimeInMillis() - beginTime[0],
					null)));
			
//			//插入
//			((PFMySqlExecute)dstExec).HugeUpdateByComparedTable(dstJdbc, srcSql, dstSql, joinColumn, compareColumn, (cnt)->{
//				this.PrintObject("进度");
//				PFDate now = PFDate.Now();
//				long m = now.ToCalendar().getTimeInMillis();
//				String speedText=PFDataHelper.FormatString("speed:{0}",
//						PFDataHelper.ScientificNotation(
//								new Double(cnt) * 1000 / (m - beginTime[0])) + "条/秒");
//				this.PrintObject(PFDataHelper.FormatString("进度:{0}\r\n速度:{1}",cnt, speedText));
//				//this.PrintObject(cnt);
//			}, (dt)->{
//				this.PrintObject(PFDataHelper.FormatString("result更新\r\n{0}",dt));
////				this.PrintObject(dt);
//			},null,tableName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	/**
	 * 小表测试
	 */
	public void testFindTableRowDifference3() {		
		try {
			
//			IPFJdbc srcJdbc =  JdbcHelperTest.GetLiGeShopJdbc();
//			IPFJdbc dstJdbc =  JdbcHelperTest.GetYJQueryJdbc();
//			IPFJdbc srcJdbc = JdbcHelperTest.GetLiGeOrderJdbc();
//			IPFJdbc dstJdbc = JdbcHelperTest.GetDayJdbc();
			ISGJdbc srcJdbc = JdbcHelperTest.GetMySqlTest2Jdbc();
			ISGJdbc dstJdbc = JdbcHelperTest.GetMySqlTest2Jdbc();


			PFCmonth pfCmonth=new PFCmonth();
			pfCmonth.setCmonth("2022.04");
			String cmonth=pfCmonth.getCmonth();
			String ym=pfCmonth.getYm();
			String srcSql =SGDataHelper.ReadLocalTxt("test05.txt",  LocalDataType.Deletable).replace("{ym}", ym);
			String dstSql=SGDataHelper.ReadLocalTxt("test06.txt",  LocalDataType.Deletable).replace("{cmonth}", cmonth);
			ISqlExecute srcExec = SGSqlExecute.Init(srcJdbc);
			ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
			String[] joinColumn=new String[] {"id"}; 
			//String[] compareColumn=new String[] {"Totalmoney"}; 
			String dstTableName="test_tb_04";
			List<SGSqlFieldInfo> compareColumnInfo=dstExec.GetTableFields(dstTableName); //这里有问题,应该根据sql来获得字段
			
			//String[] compareColumn=PFDataHelper.ListSelect(compareColumnInfo, a->a.getFieldName()).toArray(new String[compareColumnInfo.size()]);

			String[] compareColumn=compareColumnInfo.stream().filter(a->{
				return !SGDataHelper.ArrayAny(joinColumn, b->b.equals(a.getFieldName()));
				//return true;
			})
			.map(a->a.getFieldName()).toArray(String[]::new);
			;
			
			long[] beginTime = new long[] { 0 };
			beginTime[0]=SGDate.Now().ToCalendar().getTimeInMillis();
			
			SGDataTable compareTable=srcExec.FindTableRowDifference(dstJdbc, srcSql, dstSql, joinColumn, compareColumn, (cnt,total,compareCnt)->{
				this.PrintObject("进度");
				SGDate now = SGDate.Now();
				long m = now.ToCalendar().getTimeInMillis();
				String speedText=SGDataHelper.FormatString("speed:{0}",
						SGDataHelper.ScientificNotation(
								new Double(cnt) * 1000 / (m - beginTime[0])) + "条/秒");
				//this.PrintObject(PFDataHelper.FormatString("进度:{0}\r\n速度:{1}",cnt, speedText));
				this.PrintObject(SGDataHelper.FormatString("进度:{0}\r\n速度:{1}\r\n差异{2}",cnt, speedText,compareCnt));
				//this.PrintObject(cnt);
			}, (dt)->{
				//this.PrintObject(PFDataHelper.FormatString("result更新\r\n{0}",dt.Rows.size()));
//				this.PrintObject(dt);
			},null);
			this.PrintObject(compareTable.ToDictList());
			this.PrintObject(SGDataHelper.FormatString("总耗时:{0}", SGDataHelper.GetTimeSpan(
					SGDate.Now().ToCalendar().getTimeInMillis() - beginTime[0],
					null)));
			
//			//插入
//			((PFMySqlExecute)dstExec).HugeUpdateByComparedTable(dstJdbc, srcSql, dstSql, joinColumn, compareColumn, (cnt)->{
//				this.PrintObject("进度");
//				PFDate now = PFDate.Now();
//				long m = now.ToCalendar().getTimeInMillis();
//				String speedText=PFDataHelper.FormatString("speed:{0}",
//						PFDataHelper.ScientificNotation(
//								new Double(cnt) * 1000 / (m - beginTime[0])) + "条/秒");
//				this.PrintObject(PFDataHelper.FormatString("进度:{0}\r\n速度:{1}",cnt, speedText));
//				//this.PrintObject(cnt);
//			}, (dt)->{
//				this.PrintObject(PFDataHelper.FormatString("result更新\r\n{0}",dt));
////				this.PrintObject(dt);
//			},null,tableName);
			
			//插入到比较表		

			int insertCnt = compareTable.Rows.size();
			if(insertCnt<1) {
				PrintObject(dstTableName+"无差异");
			}else {
				dstExec.doInsertList(null, "test_tb_11", (a, b, c) -> a < insertCnt, (a) -> compareTable.Rows.get(a), null, null, null);
				
				PrintObject(String.valueOf(insertCnt)+"行差异已经输入到表test_tb_11");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}

	public void testFindTableRowDifference4() {
		try {

//			IPFJdbc srcJdbc =  JdbcHelperTest.GetLiGeShopJdbc();
//			IPFJdbc dstJdbc =  JdbcHelperTest.GetYJQueryJdbc();
//			IPFJdbc srcJdbc = JdbcHelperTest.GetLiGeOrderJdbc();
//			IPFJdbc dstJdbc = JdbcHelperTest.GetDayJdbc();
			ISGJdbc srcJdbc = JdbcHelperTest.GetMySqlTest2Jdbc();
			ISGJdbc dstJdbc = JdbcHelperTest.GetMySqlTest2Jdbc();


			PFCmonth pfCmonth=new PFCmonth();
			pfCmonth.setCmonth("2022.04");
			String cmonth=pfCmonth.getCmonth();
			String ym=pfCmonth.getYm();
			String srcSql =SGDataHelper.ReadLocalTxt("test05.txt",  LocalDataType.Deletable).replace("{ym}", ym);
			String dstSql=SGDataHelper.ReadLocalTxt("test06.txt",  LocalDataType.Deletable).replace("{cmonth}", cmonth);
			ISqlExecute srcExec = SGSqlExecute.Init(srcJdbc);
			ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
			String[] joinColumn=new String[] {"id"};
			//String[] compareColumn=new String[] {"Totalmoney"};
			String dstTableName="test_tb_04";
			List<SGSqlFieldInfo> compareColumnInfo=dstExec.GetTableFields(dstTableName); //这里有问题,应该根据sql来获得字段

			//String[] compareColumn=PFDataHelper.ListSelect(compareColumnInfo, a->a.getFieldName()).toArray(new String[compareColumnInfo.size()]);

			String[] compareColumn=compareColumnInfo.stream().filter(a->{
						return !SGDataHelper.ArrayAny(joinColumn, b->b.equals(a.getFieldName()));
						//return true;
					})
					.map(a->a.getFieldName()).toArray(String[]::new);
			;

			long[] beginTime = new long[] { 0 };
			beginTime[0]=SGDate.Now().ToCalendar().getTimeInMillis();

			List<PFDataRow> lRow = new ArrayList<PFDataRow>();
			List<PFDataRow> rRow = new ArrayList<PFDataRow>();
			//List<PFDataRow> lrRow = new ArrayList<PFDataRow>();
			SGRef<List<PFDataRow>> lRowRef = new SGRef<List<PFDataRow>>(lRow);
			SGRef<List<PFDataRow>> rRowRef = new SGRef<List<PFDataRow>>(rRow);
			SGDataTable compareTable=srcExec.doFindTableRowDifference3(dstJdbc, srcSql, dstSql, joinColumn, compareColumn, (cnt,total,compareCnt)->{
				this.PrintObject("进度");
				SGDate now = SGDate.Now();
				long m = now.ToCalendar().getTimeInMillis();
				String speedText=SGDataHelper.FormatString("speed:{0}",
						SGDataHelper.ScientificNotation(
								new Double(cnt) * 1000 / (m - beginTime[0])) + "条/秒");
				//this.PrintObject(PFDataHelper.FormatString("进度:{0}\r\n速度:{1}",cnt, speedText));
				this.PrintObject(SGDataHelper.FormatString("进度:{0}\r\n速度:{1}\r\n差异{2}",cnt, speedText,compareCnt));
				//this.PrintObject(cnt);
			}, (dt)->{
				//this.PrintObject(PFDataHelper.FormatString("result更新\r\n{0}",dt.Rows.size()));
//				this.PrintObject(dt);
			},null, lRowRef, rRowRef);

			this.PrintObject(compareTable.ToDictList());
			this.PrintObject(SGDataHelper.FormatString("总耗时:{0}", SGDataHelper.GetTimeSpan(
					SGDate.Now().ToCalendar().getTimeInMillis() - beginTime[0],
					null)));

			//插入到比较表

			int insertCnt = compareTable.Rows.size();
			if(insertCnt<1) {
				PrintObject(dstTableName+"无差异");
			}else {
				dstExec.doInsertList(null, "test_tb_11", (a, b, c) -> a < insertCnt, (a) -> compareTable.Rows.get(a), null, null, null);

				PrintObject(String.valueOf(insertCnt)+"行差异已经输入到表test_tb_11");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void testFindTableRowDifference5() {
		try {

//			IPFJdbc srcJdbc =  JdbcHelperTest.GetLiGeShopJdbc();
//			IPFJdbc dstJdbc =  JdbcHelperTest.GetYJQueryJdbc();
//			IPFJdbc srcJdbc = JdbcHelperTest.GetLiGeOrderJdbc();
//			IPFJdbc dstJdbc = JdbcHelperTest.GetDayJdbc();
			ISGJdbc srcJdbc = JdbcHelperTest.GetMySqlTest2Jdbc();
			ISGJdbc dstJdbc = JdbcHelperTest.GetMySqlTest2Jdbc();


			PFCmonth pfCmonth=new PFCmonth();
			pfCmonth.setCmonth("2022.04");
			String cmonth=pfCmonth.getCmonth();
			String ym=pfCmonth.getYm();
			String srcSql =SGDataHelper.ReadLocalTxt("test05.txt",  LocalDataType.Deletable).replace("{ym}", ym);
			String dstSql=SGDataHelper.ReadLocalTxt("test06.txt",  LocalDataType.Deletable).replace("{cmonth}", cmonth);
			ISqlExecute srcExec = SGSqlExecute.Init(srcJdbc);
			ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
			String[] joinColumn=new String[] {"id"};
			//String[] compareColumn=new String[] {"Totalmoney"};
			String dstTableName="test_tb_04";
			List<SGSqlFieldInfo> compareColumnInfo=dstExec.GetTableFields(dstTableName); //这里有问题,应该根据sql来获得字段

			//String[] compareColumn=PFDataHelper.ListSelect(compareColumnInfo, a->a.getFieldName()).toArray(new String[compareColumnInfo.size()]);

			String[] compareColumn=compareColumnInfo.stream().filter(a->{
						return !SGDataHelper.ArrayAny(joinColumn, b->b.equals(a.getFieldName()));
						//return true;
					})
					.map(a->a.getFieldName()).toArray(String[]::new);
			;

			long[] beginTime = new long[] { 0 };
			beginTime[0]=SGDate.Now().ToCalendar().getTimeInMillis();

			List<PFDataRow> lRow = new ArrayList<PFDataRow>();
			List<PFDataRow> rRow = new ArrayList<PFDataRow>();
			//List<PFDataRow> lrRow = new ArrayList<PFDataRow>();
			SGRef<List<PFDataRow>> lRowRef = new SGRef<List<PFDataRow>>(lRow);
			SGRef<List<PFDataRow>> rRowRef = new SGRef<List<PFDataRow>>(rRow);
			SGDataTable compareTable=srcExec.doFindTableRowDifference4(dstJdbc, srcSql, dstSql, joinColumn, compareColumn, (cnt,total,compareCnt)->{
				this.PrintObject("进度");
				SGDate now = SGDate.Now();
				long m = now.ToCalendar().getTimeInMillis();
				String speedText=SGDataHelper.FormatString("speed:{0}",
						SGDataHelper.ScientificNotation(
								new Double(cnt) * 1000 / (m - beginTime[0])) + "条/秒");
				//this.PrintObject(PFDataHelper.FormatString("进度:{0}\r\n速度:{1}",cnt, speedText));
				this.PrintObject(SGDataHelper.FormatString("进度:{0}\r\n速度:{1}\r\n差异{2}",cnt, speedText,compareCnt));
				//this.PrintObject(cnt);
			}, (dt)->{
				//this.PrintObject(PFDataHelper.FormatString("result更新\r\n{0}",dt.Rows.size()));
//				this.PrintObject(dt);
			},null, lRowRef, rRowRef);

			this.PrintObject(compareTable.ToDictList());
			this.PrintObject(SGDataHelper.FormatString("总耗时:{0}", SGDataHelper.GetTimeSpan(
					SGDate.Now().ToCalendar().getTimeInMillis() - beginTime[0],
					null)));

			//插入到比较表

			int insertCnt = compareTable.Rows.size();
			if(insertCnt<1) {
				PrintObject(dstTableName+"无差异");
			}else {
				dstExec.doInsertList(null, "test_tb_11", (a, b, c) -> a < insertCnt, (a) -> compareTable.Rows.get(a), null, null, null);

				PrintObject(String.valueOf(insertCnt)+"行差异已经输入到表test_tb_11");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * 参考数据范围
	 * select sum(total_amount) from order_order where order_month ='202211' and pay_time <'2022-11-30 00:00:00'
	 * 
	 * 性能
	 * 70万订单时,本机3GB CPU 32GB内存,结果跑到60万时,CPU 100% 内存66%,卡住
	 *
	 * 
	 * 比对里格订单并插入到数据表
	 */
	public void testFindLiGeOrderDifference1() {		
		try {
			
//			IPFJdbc srcJdbc =  JdbcHelperTest.GetLiGeShopJdbc();
//			IPFJdbc dstJdbc =  JdbcHelperTest.GetYJQueryJdbc();
//			IPFJdbc srcJdbc = JdbcHelperTest.GetLiGeOrderJdbc();
//			IPFJdbc dstJdbc = JdbcHelperTest.GetDayJdbc();
			//IPFJdbc srcJdbc = JdbcHelperTest.GetMySqlTest2Jdbc();
			ISGJdbc srcJdbc = JdbcHelperTest.GetLiGeOrderProdJdbc();
			ISGJdbc dstJdbc = JdbcHelperTest.GetLiGeOrderJdbc2();
			ISGJdbc lrJdbc = JdbcHelperTest.GetMySqlTest2Jdbc();
			


			PFCmonth pfCmonth=new PFCmonth();
			pfCmonth.setCmonth("2022.04");
			String cmonth=pfCmonth.getCmonth();
			String ym=pfCmonth.getYm();
			String srcSql =SGDataHelper.ReadLocalTxt("test07.txt",  LocalDataType.Deletable).replace("{ym}", ym);
			String dstSql=SGDataHelper.ReadLocalTxt("test08.txt",  LocalDataType.Deletable).replace("{cmonth}", cmonth);
			ISqlExecute srcExec = SGSqlExecute.Init(srcJdbc);
			srcExec.SetCommandTimeOut(PFSqlCommandTimeoutSecond.Huge());
			ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
			dstExec.SetCommandTimeOut(PFSqlCommandTimeoutSecond.Huge());
			String[] joinColumn=new String[] {"order_no"}; 
			//String[] compareColumn=new String[] {"Totalmoney"}; 
			String dstTableName="order_order";
			List<SGSqlFieldInfo> compareColumnInfo=dstExec.GetTableFields(dstTableName); //这里有问题,应该根据sql来获得字段
			
			//String[] compareColumn=PFDataHelper.ListSelect(compareColumnInfo, a->a.getFieldName()).toArray(new String[compareColumnInfo.size()]);

			String[] compareColumn=compareColumnInfo.stream().filter(a->{
				return !SGDataHelper.ArrayAny(joinColumn, b->b.equals(a.getFieldName()));
				//return true;
			})
			.map(a->a.getFieldName()).toArray(String[]::new);
			;
			
			long[] beginTime = new long[] { 0 };
			beginTime[0]=SGDate.Now().ToCalendar().getTimeInMillis();
			
			SGDataTable compareTable=srcExec.FindTableRowDifference(dstJdbc, srcSql, dstSql, joinColumn, compareColumn, (cnt,total,compareCnt)->{
				this.PrintObject("进度");
				SGDate now = SGDate.Now();
				long m = now.ToCalendar().getTimeInMillis();
				String speedText=SGDataHelper.FormatString("speed:{0}",
						SGDataHelper.ScientificNotation(
								new Double(cnt) * 1000 / (m - beginTime[0])) + "条/秒");
				this.PrintObject(SGDataHelper.FormatString("进度:{0}\r\n速度:{1}\r\n差异{2}",cnt, speedText,compareCnt));
				//PFDataHelper.GCCollect(true);
				//this.PrintObject(cnt);
			}, (dt)->{
				//this.PrintObject(PFDataHelper.FormatString("result更新\r\n{0}",dt.Rows.size()));
//				this.PrintObject(dt);
			},null);
			this.PrintObject(compareTable.ToDictList());
			this.PrintObject(SGDataHelper.FormatString("总耗时:{0}", SGDataHelper.GetTimeSpan(
					SGDate.Now().ToCalendar().getTimeInMillis() - beginTime[0],
					null)));
			
//			//插入
//			((PFMySqlExecute)dstExec).HugeUpdateByComparedTable(dstJdbc, srcSql, dstSql, joinColumn, compareColumn, (cnt)->{
//				this.PrintObject("进度");
//				PFDate now = PFDate.Now();
//				long m = now.ToCalendar().getTimeInMillis();
//				String speedText=PFDataHelper.FormatString("speed:{0}",
//						PFDataHelper.ScientificNotation(
//								new Double(cnt) * 1000 / (m - beginTime[0])) + "条/秒");
//				this.PrintObject(PFDataHelper.FormatString("进度:{0}\r\n速度:{1}",cnt, speedText));
//				//this.PrintObject(cnt);
//			}, (dt)->{
//				this.PrintObject(PFDataHelper.FormatString("result更新\r\n{0}",dt));
////				this.PrintObject(dt);
//			},null,tableName);
			
			//插入到比较表
			int insertCnt = compareTable.Rows.size();
			if(insertCnt<1) {
				PrintObject(dstTableName+"无差异");
			}else {
				ISqlExecute lrExec = SGSqlExecute.Init(lrJdbc);
				lrExec.doInsertList(null, "order_order_lr", (a, b, c) -> a < insertCnt, (a) -> compareTable.Rows.get(a), null, null, null);

				PrintObject(String.valueOf(insertCnt)+"行差异已经输入到表order_order_lr");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}

	public void testFindLiGeOrderDifference2() {
		try {

//			IPFJdbc srcJdbc =  JdbcHelperTest.GetLiGeShopJdbc();
//			IPFJdbc dstJdbc =  JdbcHelperTest.GetYJQueryJdbc();
//			IPFJdbc srcJdbc = JdbcHelperTest.GetLiGeOrderJdbc();
//			IPFJdbc dstJdbc = JdbcHelperTest.GetDayJdbc();
			//IPFJdbc srcJdbc = JdbcHelperTest.GetMySqlTest2Jdbc();
			ISGJdbc srcJdbc = JdbcHelperTest.GetLiGeOrderProdJdbc();
			ISGJdbc dstJdbc = JdbcHelperTest.GetLiGeOrderJdbc2();
			ISGJdbc lrJdbc = JdbcHelperTest.GetMySqlTest2Jdbc();



			PFCmonth pfCmonth=new PFCmonth();
			pfCmonth.setCmonth("2022.04");
			String cmonth=pfCmonth.getCmonth();
			String ym=pfCmonth.getYm();
			String srcSql =SGDataHelper.ReadLocalTxt("test07.txt",  LocalDataType.Deletable).replace("{ym}", ym);
			String dstSql=SGDataHelper.ReadLocalTxt("test08.txt",  LocalDataType.Deletable).replace("{cmonth}", cmonth);
			ISqlExecute srcExec = SGSqlExecute.Init(srcJdbc);
			srcExec.SetCommandTimeOut(PFSqlCommandTimeoutSecond.Huge());
			ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
			dstExec.SetCommandTimeOut(PFSqlCommandTimeoutSecond.Huge());
			String[] joinColumn=new String[] {"order_no"};
			//String[] compareColumn=new String[] {"Totalmoney"};
			String dstTableName="order_order";
			List<SGSqlFieldInfo> compareColumnInfo=dstExec.GetTableFields(dstTableName); //这里有问题,应该根据sql来获得字段

			//String[] compareColumn=PFDataHelper.ListSelect(compareColumnInfo, a->a.getFieldName()).toArray(new String[compareColumnInfo.size()]);

			String[] compareColumn=compareColumnInfo.stream().filter(a->{
						return !SGDataHelper.ArrayAny(joinColumn, b->b.equals(a.getFieldName()));
						//return true;
					})
					.map(a->a.getFieldName()).toArray(String[]::new);
			;

			long[] beginTime = new long[] { 0 };
			beginTime[0]=SGDate.Now().ToCalendar().getTimeInMillis();

			List<PFDataRow> lRow = new ArrayList<PFDataRow>();
			List<PFDataRow> rRow = new ArrayList<PFDataRow>();
			//List<PFDataRow> lrRow = new ArrayList<PFDataRow>();
			SGRef<List<PFDataRow>> lRowRef = new SGRef<List<PFDataRow>>(lRow);
			SGRef<List<PFDataRow>> rRowRef = new SGRef<List<PFDataRow>>(rRow);
			SGDataTable compareTable=srcExec.doFindTableRowDifference3(dstJdbc, srcSql, dstSql, joinColumn, compareColumn, (cnt,total,compareCnt)->{
				this.PrintObject("进度");
				SGDate now = SGDate.Now();
				long m = now.ToCalendar().getTimeInMillis();
				String speedText=SGDataHelper.FormatString("speed:{0}",
						SGDataHelper.ScientificNotation(
								new Double(cnt) * 1000 / (m - beginTime[0])) + "条/秒");
				this.PrintObject(SGDataHelper.FormatString("进度:{0}\r\n速度:{1}\r\n差异{2}",cnt, speedText,compareCnt));
				//PFDataHelper.GCCollect(true);
				//this.PrintObject(cnt);
			}, (dt)->{
				//this.PrintObject(PFDataHelper.FormatString("result更新\r\n{0}",dt.Rows.size()));
//				this.PrintObject(dt);
			},null,lRowRef,rRowRef);
			this.PrintObject(compareTable.ToDictList());
			this.PrintObject(SGDataHelper.FormatString("总耗时:{0}", SGDataHelper.GetTimeSpan(
					SGDate.Now().ToCalendar().getTimeInMillis() - beginTime[0],
					null)));

//			//插入
//			((PFMySqlExecute)dstExec).HugeUpdateByComparedTable(dstJdbc, srcSql, dstSql, joinColumn, compareColumn, (cnt)->{
//				this.PrintObject("进度");
//				PFDate now = PFDate.Now();
//				long m = now.ToCalendar().getTimeInMillis();
//				String speedText=PFDataHelper.FormatString("speed:{0}",
//						PFDataHelper.ScientificNotation(
//								new Double(cnt) * 1000 / (m - beginTime[0])) + "条/秒");
//				this.PrintObject(PFDataHelper.FormatString("进度:{0}\r\n速度:{1}",cnt, speedText));
//				//this.PrintObject(cnt);
//			}, (dt)->{
//				this.PrintObject(PFDataHelper.FormatString("result更新\r\n{0}",dt));
////				this.PrintObject(dt);
//			},null,tableName);

			//插入到比较表
			int insertCnt = compareTable.Rows.size();
			if(insertCnt<1) {
				PrintObject(dstTableName+"无差异");
			}else {
				ISqlExecute lrExec = SGSqlExecute.Init(lrJdbc);
				lrExec.doInsertList(null, "order_order_lr", (a, b, c) -> a < insertCnt, (a) -> compareTable.Rows.get(a), null, null, null);

				PrintObject(String.valueOf(insertCnt)+"行差异已经输入到表order_order_lr");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void testFindLiGeOrderDifference3() {
		try {

//			IPFJdbc srcJdbc =  JdbcHelperTest.GetLiGeShopJdbc();
//			IPFJdbc dstJdbc =  JdbcHelperTest.GetYJQueryJdbc();
//			IPFJdbc srcJdbc = JdbcHelperTest.GetLiGeOrderJdbc();
//			IPFJdbc dstJdbc = JdbcHelperTest.GetDayJdbc();
			//IPFJdbc srcJdbc = JdbcHelperTest.GetMySqlTest2Jdbc();
			ISGJdbc srcJdbc = JdbcHelperTest.GetLiGeOrderProdJdbc();
			ISGJdbc dstJdbc = JdbcHelperTest.GetLiGeOrderJdbc2();
			ISGJdbc lrJdbc = JdbcHelperTest.GetMySqlTest2Jdbc();



			PFCmonth pfCmonth=new PFCmonth();
			pfCmonth.setCmonth("2022.04");
			String cmonth=pfCmonth.getCmonth();
			String ym=pfCmonth.getYm();
			String srcSql =SGDataHelper.ReadLocalTxt("test07.txt",  LocalDataType.Deletable).replace("{ym}", ym);
			String dstSql=SGDataHelper.ReadLocalTxt("test08.txt",  LocalDataType.Deletable).replace("{cmonth}", cmonth);
			ISqlExecute srcExec = SGSqlExecute.Init(srcJdbc);
			srcExec.SetCommandTimeOut(PFSqlCommandTimeoutSecond.Huge());
			ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
			dstExec.SetCommandTimeOut(PFSqlCommandTimeoutSecond.Huge());
			String[] joinColumn=new String[] {"order_no"};
			//String[] compareColumn=new String[] {"Totalmoney"};
			String dstTableName="order_order";
			List<SGSqlFieldInfo> compareColumnInfo=dstExec.GetTableFields(dstTableName); //这里有问题,应该根据sql来获得字段

			//String[] compareColumn=PFDataHelper.ListSelect(compareColumnInfo, a->a.getFieldName()).toArray(new String[compareColumnInfo.size()]);

			String[] compareColumn=compareColumnInfo.stream().filter(a->{
						return !SGDataHelper.ArrayAny(joinColumn, b->b.equals(a.getFieldName()));
						//return true;
					})
					.map(a->a.getFieldName()).toArray(String[]::new);
			;

			long[] beginTime = new long[] { 0 };
			beginTime[0]=SGDate.Now().ToCalendar().getTimeInMillis();

			List<PFDataRow> lRow = new ArrayList<PFDataRow>();
			List<PFDataRow> rRow = new ArrayList<PFDataRow>();
			//List<PFDataRow> lrRow = new ArrayList<PFDataRow>();
			SGRef<List<PFDataRow>> lRowRef = new SGRef<List<PFDataRow>>(lRow);
			SGRef<List<PFDataRow>> rRowRef = new SGRef<List<PFDataRow>>(rRow);
			SGDataTable compareTable=srcExec.doFindTableRowDifference4(dstJdbc, srcSql, dstSql, joinColumn, compareColumn, (cnt,total,compareCnt)->{
				this.PrintObject("进度");
				SGDate now = SGDate.Now();
				long m = now.ToCalendar().getTimeInMillis();
				String speedText=SGDataHelper.FormatString("speed:{0}",
						SGDataHelper.ScientificNotation(
								new Double(cnt) * 1000 / (m - beginTime[0])) + "条/秒");
				this.PrintObject(SGDataHelper.FormatString("进度:{0}\r\n速度:{1}\r\n差异{2}",cnt, speedText,compareCnt));
				//PFDataHelper.GCCollect(true);
				//this.PrintObject(cnt);
			}, (dt)->{
				//this.PrintObject(PFDataHelper.FormatString("result更新\r\n{0}",dt.Rows.size()));
//				this.PrintObject(dt);
			},null,lRowRef,rRowRef);
			this.PrintObject(compareTable.ToDictList());
			this.PrintObject(SGDataHelper.FormatString("总耗时:{0}", SGDataHelper.GetTimeSpan(
					SGDate.Now().ToCalendar().getTimeInMillis() - beginTime[0],
					null)));

//			//插入
//			((PFMySqlExecute)dstExec).HugeUpdateByComparedTable(dstJdbc, srcSql, dstSql, joinColumn, compareColumn, (cnt)->{
//				this.PrintObject("进度");
//				PFDate now = PFDate.Now();
//				long m = now.ToCalendar().getTimeInMillis();
//				String speedText=PFDataHelper.FormatString("speed:{0}",
//						PFDataHelper.ScientificNotation(
//								new Double(cnt) * 1000 / (m - beginTime[0])) + "条/秒");
//				this.PrintObject(PFDataHelper.FormatString("进度:{0}\r\n速度:{1}",cnt, speedText));
//				//this.PrintObject(cnt);
//			}, (dt)->{
//				this.PrintObject(PFDataHelper.FormatString("result更新\r\n{0}",dt));
////				this.PrintObject(dt);
//			},null,tableName);

			//插入到比较表
			int insertCnt = compareTable.Rows.size();
			if(insertCnt<1) {
				PrintObject(dstTableName+"无差异");
			}else {
				ISqlExecute lrExec = SGSqlExecute.Init(lrJdbc);
				lrExec.doInsertList(null, "order_order_lr", (a, b, c) -> a < insertCnt, (a) -> compareTable.Rows.get(a), null, null, null);

				PrintObject(String.valueOf(insertCnt)+"行差异已经输入到表order_order_lr");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * 比对不成功,插入了重复数据 -- benjamin todo
	 */
	public void testChangeUpdateTableByDifference() {		
		try {
			
//			IPFJdbc srcJdbc =  JdbcHelperTest.GetLiGeShopJdbc();
//			IPFJdbc dstJdbc =  JdbcHelperTest.GetYJQueryJdbc();
			ISGJdbc srcJdbc = JdbcHelperTest.GetLiGeOrderJdbc();
			ISGJdbc dstJdbc = JdbcHelperTest.GetLiGeSaleJdbc();

//			String srcSql="select order_no as orderno,\r\n" + 
//					" case when o.is_promotion = 1 then o.order_amount-o.promotion_discount when ex.order_route = 5 then o.order_amount-o.promotion_discount else o.order_amount end as Totalmoney,\r\n" + 
//					"  o.order_amount-o.promotion_discount  as Totalmoney2 -- 叶理胜20220526\r\n" + 
//					"from mall_center_order.order_order o \r\n" + 
//					"left join mall_center_order.order_order_ext ex on o.id=ex.order_id \r\n" + 
//					"where order_status in (2,3,99) \r\n" + 
//					"-- and order_type in (1,2,4) \r\n" + 
//					"and order_month ='202204' and del = 0 "
//					//+"and order_no in('SG908170220415000073','SG911095220415000172','SG932764220415000062')"
//					;
			PFCmonth pfCmonth=new PFCmonth();
			pfCmonth.setCmonth("2022.04");
			String cmonth=pfCmonth.getCmonth();
			String ym=pfCmonth.getYm();
			String srcSql =SGDataHelper.ReadLocalTxt("test01.txt",  LocalDataType.Deletable).replace("{ym}", ym);
			String dstSql=SGDataHelper.ReadLocalTxt("test02.txt",  LocalDataType.Deletable).replace("{cmonth}", cmonth);
					//+" where orderno in('SG908170220415000073','SG911095220415000172','SG932764220415000062','SG920096220415000146')"
					//+"order by orderno asc"
					;
			ISqlExecute srcExec = SGSqlExecute.Init(srcJdbc);
			ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
			String[] joinColumn=new String[] {"order_no"}; 
			//String[] compareColumn=new String[] {"Totalmoney"}; 
			String tableName="order_order_cur_month";
			List<SGSqlFieldInfo> compareColumnInfo=dstExec.GetTableFields(tableName); 
			
			//String[] compareColumn=PFDataHelper.ListSelect(compareColumnInfo, a->a.getFieldName()).toArray(new String[compareColumnInfo.size()]);

			String[] compareColumn=compareColumnInfo.stream().filter(a->{
				return !SGDataHelper.ArrayAny(joinColumn, b->b.equals(a.getFieldName()));
				//return true;
			})
			.map(a->a.getFieldName()).toArray(String[]::new);
			;
			
			//long[] beginTime = new long[] { 0 };
			Long[] compareBeginTime = new Long[] { null };
			Long[] beginTime = new Long[] { null };
			beginTime[0]=SGDate.Now().ToCalendar().getTimeInMillis();
			
//			PFDataTable compareTable=srcExec.FindTableRowDifference(dstJdbc, srcSql, dstSql, joinColumn, compareColumn, (cnt)->{
//				this.PrintObject("进度");
//				PFDate now = PFDate.Now();
//				long m = now.ToCalendar().getTimeInMillis();
//				String speedText=PFDataHelper.FormatString("speed:{0}",
//						PFDataHelper.ScientificNotation(
//								new Double(cnt) * 1000 / (m - beginTime[0])) + "条/秒");
//				this.PrintObject(PFDataHelper.FormatString("进度:{0}\r\n速度:{1}",cnt, speedText));
//				//this.PrintObject(cnt);
//			}, (dt)->{
//				this.PrintObject(PFDataHelper.FormatString("result更新\r\n{0}",dt));
////				this.PrintObject(dt);
//			},null);
//			this.PrintObject(compareTable.ToDictList());
//			this.PrintObject(PFDataHelper.FormatString("总耗时:{0}", PFDataHelper.GetTimeSpan(
//					PFDate.Now().ToCalendar().getTimeInMillis() - beginTime[0],
//					null)));
			
			//插入
			((SGMySqlExecute)dstExec).HugeUpdateByComparedTable(dstJdbc, srcSql, dstSql, joinColumn, compareColumn,
					(cnt,total,compareCnt)->{
						if(null==compareBeginTime){
							compareBeginTime[0]=Long.valueOf(0);
						}
						this.PrintObject("比对进度");
						SGDate now = SGDate.Now();
						long m = now.ToCalendar().getTimeInMillis();
						String speedText=SGDataHelper.FormatString("speed:{0}",
								SGDataHelper.ScientificNotation(
										new Double(cnt) * 1000 / (m - compareBeginTime[0])) + "条/秒");
						this.PrintObject(SGDataHelper.FormatString("进度:{0}\r\n速度:{1}",cnt, speedText));
						//this.PrintObject(cnt);
					},
					(cnt,total)->{
						if(null==beginTime){
							beginTime[0]=Long.valueOf(0);
						}
				this.PrintObject("进度");
				SGDate now = SGDate.Now();
				long m = now.ToCalendar().getTimeInMillis();
				String speedText=SGDataHelper.FormatString("speed:{0}",
						SGDataHelper.ScientificNotation(
								new Double(cnt) * 1000 / (m - beginTime[0])) + "条/秒");
				this.PrintObject(SGDataHelper.FormatString("进度:{0}\r\n速度:{1}",cnt, speedText));
				//this.PrintObject(cnt);
			},
//					(dt)->{
//				this.PrintObject(PFDataHelper.FormatString("result更新\r\n{0}",dt));
////				this.PrintObject(dt);
//			},
					null,tableName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}

	public void testChangeUpdateTableByDifference2() {		
		try {

//			IPFJdbc srcJdbc =  JdbcHelperTest.GetLiGeShopJdbc();
//			IPFJdbc dstJdbc =  JdbcHelperTest.GetYJQueryJdbc();
			ISGJdbc srcJdbc = JdbcHelperTest.GetLiGeOrderJdbc();
			ISGJdbc dstJdbc = JdbcHelperTest.GetDayJdbc();

//			String srcSql="select order_no as orderno,\r\n" + 
//					" case when o.is_promotion = 1 then o.order_amount-o.promotion_discount when ex.order_route = 5 then o.order_amount-o.promotion_discount else o.order_amount end as Totalmoney,\r\n" + 
//					"  o.order_amount-o.promotion_discount  as Totalmoney2 -- 叶理胜20220526\r\n" + 
//					"from mall_center_order.order_order o \r\n" + 
//					"left join mall_center_order.order_order_ext ex on o.id=ex.order_id \r\n" + 
//					"where order_status in (2,3,99) \r\n" + 
//					"-- and order_type in (1,2,4) \r\n" + 
//					"and order_month ='202204' and del = 0 "
//					//+"and order_no in('SG908170220415000073','SG911095220415000172','SG932764220415000062')"
//					;
			PFCmonth pfCmonth=new PFCmonth();
			pfCmonth.setCmonth("2022.04");
			String cmonth=pfCmonth.getCmonth();
			String ym=pfCmonth.getYm();
			String srcSql =SGDataHelper.ReadLocalTxt("test03.txt",  LocalDataType.Deletable).replace("{ym}", ym);
			String dstSql=SGDataHelper.ReadLocalTxt("test04.txt",  LocalDataType.Deletable).replace("{cmonth}", cmonth);
					//+" where orderno in('SG908170220415000073','SG911095220415000172','SG932764220415000062','SG920096220415000146')"
					//+"order by orderno asc"
					;
			ISqlExecute srcExec = SGSqlExecute.Init(srcJdbc);
			ISqlExecute dstExec = SGSqlExecute.Init(dstJdbc);
			String[] joinColumn=new String[] {"Orderno"}; 
			//String[] compareColumn=new String[] {"Totalmoney"}; 
			String tableName="t_orders_recent";
			List<SGSqlFieldInfo> compareColumnInfo=dstExec.GetTableFields(tableName); 
			
			//String[] compareColumn=PFDataHelper.ListSelect(compareColumnInfo, a->a.getFieldName()).toArray(new String[compareColumnInfo.size()]);

			String[] compareColumn=compareColumnInfo.stream().filter(a->{
				return !SGDataHelper.ArrayAny(joinColumn, b->b.equals(a.getFieldName()));
				//return true;
			})
			.map(a->a.getFieldName()).toArray(String[]::new);
			;
			
			//long[] beginTime = new long[] { 0 };
			Long[] compareBeginTime = new Long[] { null };
			Long[] beginTime = new Long[] { null };
			beginTime[0]=SGDate.Now().ToCalendar().getTimeInMillis();
			
//			PFDataTable compareTable=srcExec.FindTableRowDifference(dstJdbc, srcSql, dstSql, joinColumn, compareColumn, (cnt)->{
//				this.PrintObject("进度");
//				PFDate now = PFDate.Now();
//				long m = now.ToCalendar().getTimeInMillis();
//				String speedText=PFDataHelper.FormatString("speed:{0}",
//						PFDataHelper.ScientificNotation(
//								new Double(cnt) * 1000 / (m - beginTime[0])) + "条/秒");
//				this.PrintObject(PFDataHelper.FormatString("进度:{0}\r\n速度:{1}",cnt, speedText));
//				//this.PrintObject(cnt);
//			}, (dt)->{
//				this.PrintObject(PFDataHelper.FormatString("result更新\r\n{0}",dt));
////				this.PrintObject(dt);
//			},null);
//			this.PrintObject(compareTable.ToDictList());
//			this.PrintObject(PFDataHelper.FormatString("总耗时:{0}", PFDataHelper.GetTimeSpan(
//					PFDate.Now().ToCalendar().getTimeInMillis() - beginTime[0],
//					null)));
			
			//插入
			(srcExec).HugeUpdateByComparedTable(dstJdbc, srcSql, dstSql, joinColumn, compareColumn,
					(cnt,total,compareCnt)->{
						if(null==compareBeginTime){
							compareBeginTime[0]=Long.valueOf(0);
						}
						this.PrintObject("比对进度");
						SGDate now = SGDate.Now();
						long m = now.ToCalendar().getTimeInMillis();
						String speedText=SGDataHelper.FormatString("speed:{0}",
								SGDataHelper.ScientificNotation(
										new Double(cnt) * 1000 / (m - compareBeginTime[0])) + "条/秒");
						this.PrintObject(SGDataHelper.FormatString("进度:{0}\r\n速度:{1}",cnt, speedText));
						//this.PrintObject(cnt);
					},
					(cnt,total)->{
						if(null==beginTime){
							beginTime[0]=Long.valueOf(0);
						}
				this.PrintObject("进度");
				SGDate now = SGDate.Now();
				long m = now.ToCalendar().getTimeInMillis();
				String speedText=SGDataHelper.FormatString("speed:{0}",
						SGDataHelper.ScientificNotation(
								new Double(cnt) * 1000 / (m - beginTime[0])) + "条/秒");
				this.PrintObject(SGDataHelper.FormatString("进度:{0}\r\n速度:{1}",cnt, speedText));
				//this.PrintObject(cnt);
			},
//					(dt)->{
//				this.PrintObject(PFDataHelper.FormatString("result更新\r\n{0}",dt.Rows.size()));
////				this.PrintObject(dt);
//			},
					null,tableName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}

	//保存gamepad页到静态文件
	public void testSaveGamepadHtml() {
		initPFHelper();
		String url="http://localhost:28303/story/gamePad2#";
		SGRequestResult r=SGHttpHelper.HttpGet(url,null);
		while(!r.success) {
//			SGDataHelper.SaveStringToFile(r.content,"D:\\cache\\1\\aa.txt");
			System.out.println("wait 1 second");
			try {
				Thread.sleep(1000);
				r=SGHttpHelper.HttpGet(url,null);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		SGDataHelper.SaveStringToFile(r.content,"D:\\github\\htmlAll\\gamePad\\index.html");
		System.out.println("save success");
		
	}
}
