package org.sellgirlPayHelperNotSpring.model;

import com.sellgirl.sgJavaHelper.PFSqlCommandTimeoutSecond;
import com.sellgirl.sgJavaHelper.SqlExpressionOperator;
import com.sellgirl.sgJavaHelper.TransferTaskBase;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.sql.*;

@SuppressWarnings("unused")
public class TransferTaskTest001 extends TransferTaskBase{

	@Override
	public SGSqlTransferItem get() {
		 SGSqlTransferItem r=super.get();
	     //r.SrcJdbc=JdbcHelper.GetYJQuery();
		 r.SrcJdbc=JdbcHelperTest.GetTidbSale();
		 r.DstJdbc=JdbcHelperTest.GetTidbSale();
		 
       r.DstTableName="monthly_province_statistics";
       //r.DstTableName="monthly_province_statistics2";//测试表--benjamin todo

      	r.DstTablePrimaryKeys=new String[] {"create_date", "province_name", "hpos", "qpos", "is_new_hy"};
		 //r.DstTableClusteredIndex=new String[] {"cmonth"};

		 r.SrcSql=getSql();

       r.TransferByMonth=a->{
       	//a.DstTableName=a.DstTableName.replace("{year}",String.valueOf(a.getYear())); 
    	   //a.SrcJdbc=JdbcHelper.GetYJQueryMonth(a.getPFCmonth().getYm());yjquerymonth没有当月数据
       };
       r.BeforeTransferAction=(transfer)->{
      	 ISqlExecute sqlExec =  SGSqlExecute.Init(transfer.DstJdbc);
      	 sqlExec.SetCommandTimeOut(transfer.getSqlCommandTimeout());

       	sqlExec.HugeDelete(transfer.DstTableName, where->{
       		where.Add("create_date",transfer.getPFCmonth().ToDateTime() , SqlExpressionOperator.GreaterOrEqual);
       		where.Add("create_date", transfer.getNextPFCmonth().ToDateTime(), SqlExpressionOperator.Less);//这句好像会清了下一个月，需要测试--benjamin todo
       	});
       };
//       r.AfterTransferAction=(transfer)->{
//    	   PFMySqlExecute sqlExec =  (PFMySqlExecute)PFSqlExecute.Init(transfer.DstJdbc);
//        	 sqlExec.SetCommandTimeOut(transfer.getSqlCommandTimeout());
//
//             sqlExec.UpdateYearOnYearField(
//                 transfer.DstTableName,
//                 transfer.getCmonth(),
//                 transfer.DstTablePrimaryKeys,
//                 new String[] { "new_hy_qty", "new_buyer_qty", "valid_hy_qty", "buyer_qty", "valid_hy_hpos_above_5_qty", "valid_hy_hpos_0_qty" }
//                 );
//         };

		 
		 return r;
		 
		//return TransferTaskHelper.tidb_sales();
	}
	public static String getSql() {
return "select STR_TO_DATE('2021.10.01 00:00:00','%Y.%m.%d %H:%i:%s') as create_date \r\n" + 
		"  ,a.province_name,a.hpos,a.qpos,a.is_new_hy,\r\n" + 
		"  date_add(STR_TO_DATE('2021.10.01 00:00:00','%Y.%m.%d %H:%i:%s'), INTERVAL -1 year) as ly_create_date,\r\n" + 
		"  date_add(STR_TO_DATE('2021.10.01 00:00:00','%Y.%m.%d %H:%i:%s'), INTERVAL -1 month) as lm_create_date\r\n" + 		
		"   , b.new_hy_qty"+
		
		",b.new_hy_sales"+  //这行报错,应该是sum(decimal)的字段没插入成功
		",b.new_buyer_qty\r\n" + 
		"   ,xxx.valid_hy_qty ,t.valid_hy_hpos_0_qty,t.valid_hy_hpos_5_qty,t.valid_hy_hpos_6_qty,t.valid_hy_hpos_7_qty,t.valid_hy_hpos_8_qty,t.valid_hy_hpos_9_qty,t.valid_hy_hpos_10_qty,t.valid_hy_hpos_above_5_qty,\r\n" + 
		"   d.cancel_hy_qty,e.cancel_hy_hpos_0_qty,e.cancel_hy_hpos_5_qty,e.cancel_hy_hpos_6_qty,e.cancel_hy_hpos_7_qty,e.cancel_hy_hpos_8_qty,e.cancel_hy_hpos_9_qty,e.cancel_hy_hpos_10_qty,\r\n" + 
		"   k.buyer_qty,l.buyer_hpos_0_qty,l.buyer_hpos_5_qty,l.buyer_hpos_6_qty,l.buyer_hpos_7_qty,l.buyer_hpos_8_qty,l.buyer_hpos_9_qty,l.buyer_hpos_10_qty,\r\n" + 
		"   k.buyer_sales,l.buyer_hpos_0_sales,l.buyer_hpos_5_sales,l.buyer_hpos_6_sales,l.buyer_hpos_7_sales,l.buyer_hpos_8_sales,l.buyer_hpos_9_sales,l.buyer_hpos_10_sales,\r\n" + 
		"   r.active_hy_qty,r.active_hy_hpos_0_qty\r\n" + 
		"   ,z.active_3_hy_qty,z.active_4_hy_qty,z.active_5_hy_qty,z.active_6_hy_qty,z.active_7_hy_qty,z.active_8_hy_qty,z.active_9_hy_qty,z.active_10_hy_qty,z.active_11_hy_qty,z.active_12_hy_qty,\r\n" + 
		"   aj.unactive_3_hy_qty,aj.unactive_4_hy_qty,aj.unactive_5_hy_qty,aj.unactive_6_hy_qty,aj.unactive_7_hy_qty,aj.unactive_8_hy_qty,aj.unactive_9_hy_qty,aj.unactive_10_hy_qty,aj.unactive_11_hy_qty,aj.unactive_12_hy_qty,\r\n" + 
		"   av.buy_above_2_hy_qty,\r\n" + 
		"   aw.buy_2_new_hy_qty\r\n" + 		
		"   ,0 as ly_new_hy_qty,0 as ly_new_buyer_qty,\r\n" + 
		"   0 as ly_valid_hy_qty,0 as ly_buyer_qty,0 as ly_valid_hy_hpos_above_5_qty,0 as ly_valid_hy_hpos_0_qty,\r\n" + 
		"   0 as lm_new_hy_qty,0 as lm_new_buyer_qty,\r\n" + 
		"   0 as lm_valid_hy_qty,0 as lm_buyer_qty,0 as lm_valid_hy_hpos_above_5_qty,0 as lm_valid_hy_hpos_0_qty\r\n" +
		
		"   from\r\n" + 
		"  ( \r\n" + 
		"    select abcd.province_name,abcd.hpos,abcd.qpos,CAST(abcd.is_new_hy AS char(1)) as is_new_hy\r\n" + 
		"    from(\r\n" + 
		"        select province_name,hpos,qpos,is_new_hy from monthly_hyzl_2021\r\n" + 
		"     where create_date=STR_TO_DATE('2021.10.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		"     union \r\n" + 
		"     select province_name,hpos,qpos,is_new_hy from monthly_hyzl_2021\r\n" + 
		"     where create_date=STR_TO_DATE('2020.10.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		"     union \r\n" + 
		"     select province_name,hpos,qpos,is_new_hy from monthly_hyzl_2021\r\n" + 
		"     where create_date=STR_TO_DATE('2021.09.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		"    ) abcd\r\n" + 
		"    group by province_name,hpos,qpos,CAST(abcd.is_new_hy AS char(1))\r\n" + 
		"  ) a \r\n" + 
		"  left join \r\n" + 
		"  (\r\n" + 
		" select count(hybh) as valid_hy_qty,province_name,hpos,qpos,is_new_hy from monthly_hyzl_2021 \r\n" + 
		" where create_date=STR_TO_DATE('2021.10.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		"    and qxmonth='' \r\n" + 
		"    group by province_name,hpos,qpos,is_new_hy\r\n" + 
		"  ) xxx on xxx.province_name=a.province_name and xxx.hpos=a.hpos and xxx.qpos=a.qpos and xxx.is_new_hy=a.is_new_hy\r\n" + 
		"  left join \r\n" + 
		"  (\r\n" + 
		"    select count(hybh) as new_hy_qty,sum(sales) as new_hy_sales,\r\n" + 
		"      SUM(case when sales>0 then 1 else 0 end) as new_buyer_qty,\r\n" + 
		"      province_name,hpos,qpos,is_new_hy \r\n" + 
		"    from monthly_hyzl_2021 \r\n" + 
		"    where create_date=STR_TO_DATE('2021.10.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		"    and accmon='2021.10'    \r\n" + 
		"    group by province_name,hpos,qpos,is_new_hy\r\n" + 
		"  ) b on b.province_name=a.province_name and b.hpos=a.hpos and b.qpos=a.qpos and b.is_new_hy=a.is_new_hy\r\n" + 
		"  left join\r\n" + 
		"  (\r\n" + 
		"   select count(hybh) as cancel_hy_qty,province_name,hpos,qpos,is_new_hy from monthly_hyzl_2021 \r\n" + 
		"    where create_date=STR_TO_DATE('2021.11.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		" and qxmonth='2021.10'    \r\n" + 
		"    group by province_name,hpos,qpos,is_new_hy\r\n" + 
		"  ) d on d.province_name=a.province_name and d.hpos=a.hpos and d.qpos=a.qpos and d.is_new_hy=a.is_new_hy\r\n" + 
		"  left join\r\n" + 
		"  (\r\n" + 
		"  select zzzz.*,\r\n" + 
		"  (case when hpos=0 then qty else 0 end) cancel_hy_hpos_0_qty,\r\n" + 
		"  (case when hpos=5 then qty else 0 end) cancel_hy_hpos_5_qty,\r\n" + 
		"  (case when hpos=6 then qty else 0 end) cancel_hy_hpos_6_qty,\r\n" + 
		"  (case when hpos=7 then qty else 0 end) cancel_hy_hpos_7_qty,\r\n" + 
		"  (case when hpos=8 then qty else 0 end) cancel_hy_hpos_8_qty,\r\n" + 
		"  (case when hpos=9 then qty else 0 end) cancel_hy_hpos_9_qty,\r\n" + 
		"  (case when hpos=10 then qty else 0 end) cancel_hy_hpos_10_qty\r\n" + 
		"  from(\r\n" + 
		"   select count(hybh) as qty,sum(sales) as sales,province_name,hpos,qpos,is_new_hy from monthly_hyzl_2021 \r\n" + 
		"   where create_date=STR_TO_DATE('2021.11.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		"   and qxmonth='2021.10'\r\n" + 
		"   group by province_name,hpos,qpos,is_new_hy  \r\n" + 
		"  ) zzzz  -- 6s,147条\r\n" + 
		"  ) e on e.province_name=a.province_name and e.hpos=a.hpos and e.qpos=a.qpos and e.is_new_hy=a.is_new_hy\r\n" + 
		"  left join\r\n" + 
		"  (\r\n" + 
		"   select count(hybh) as buyer_qty,sum(sales) as buyer_sales,province_name,hpos,qpos,is_new_hy from monthly_hyzl_2021 \r\n" + 
		"    where create_date=STR_TO_DATE('2021.10.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		"    and sales>0\r\n" + 
		"    group by province_name,hpos,qpos,is_new_hy\r\n" + 
		"  ) k on k.province_name=a.province_name and k.hpos=a.hpos and k.qpos=a.qpos and k.is_new_hy=a.is_new_hy\r\n" + 
		"  left join\r\n" + 
		"  (\r\n" + 
		"  select zzzz.*,\r\n" + 
		"  (case when hpos=0 then qty else 0 end) buyer_hpos_0_qty,\r\n" + 
		"  (case when hpos=5 then qty else 0 end) buyer_hpos_5_qty,\r\n" + 
		"  (case when hpos=6 then qty else 0 end) buyer_hpos_6_qty,\r\n" + 
		"  (case when hpos=7 then qty else 0 end) buyer_hpos_7_qty,\r\n" + 
		"  (case when hpos=8 then qty else 0 end) buyer_hpos_8_qty,\r\n" + 
		"  (case when hpos=9 then qty else 0 end) buyer_hpos_9_qty,\r\n" + 
		"  (case when hpos=10 then qty else 0 end) buyer_hpos_10_qty,\r\n" + 
		"  (case when hpos=0 then sales else 0 end) buyer_hpos_0_sales,\r\n" + 
		"  (case when hpos=5 then sales else 0 end) buyer_hpos_5_sales,\r\n" + 
		"  (case when hpos=6 then sales else 0 end) buyer_hpos_6_sales,\r\n" + 
		"  (case when hpos=7 then sales else 0 end) buyer_hpos_7_sales,\r\n" + 
		"  (case when hpos=8 then sales else 0 end) buyer_hpos_8_sales,\r\n" + 
		"  (case when hpos=9 then sales else 0 end) buyer_hpos_9_sales,\r\n" + 
		"  (case when hpos=10 then sales else 0 end) buyer_hpos_10_sales\r\n" + 
		"  from(\r\n" + 
		"   select count(hybh) as qty,sum(sales) as sales,province_name,hpos,qpos,is_new_hy from monthly_hyzl_2021\r\n" + 
		"   where create_date=STR_TO_DATE('2021.10.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		"   and sales>0\r\n" + 
		"   group by province_name,hpos,qpos,is_new_hy  \r\n" + 
		"  ) zzzz  -- 8s,760条\r\n" + 
		"  ) l on l.province_name=a.province_name and l.hpos=a.hpos and l.qpos=a.qpos and l.is_new_hy=a.is_new_hy\r\n" + 
		"  left join\r\n" + 
		"  (\r\n" + 
		"     select zzzz.*,\r\n" + 
		"  qty as active_hy_qty,\r\n" + 
		"  (case when hpos=0 then qty else 0 end) active_hy_hpos_0_qty\r\n" + 
		"  from(\r\n" + 
		"   select count(hybh) as qty,province_name,hpos,qpos,is_new_hy from monthly_hyzl_2021\r\n" + 
		"   where create_date=STR_TO_DATE('2021.10.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		"   and pv>=200\r\n" + 
		"   group by province_name,hpos,qpos,is_new_hy  \r\n" + 
		"  ) zzzz \r\n" + 
		"  ) r on r.province_name=a.province_name and r.hpos=a.hpos and r.qpos=a.qpos and r.is_new_hy=a.is_new_hy  \r\n" + 
		"  left join\r\n" + 
		"  (\r\n" + 
		"  select zzzz.*,\r\n" + 
		"  (case when hpos=0 then qty else 0 end) valid_hy_hpos_0_qty,\r\n" + 
		"  (case when hpos=5 then qty else 0 end) valid_hy_hpos_5_qty,\r\n" + 
		"  (case when hpos=6 then qty else 0 end) valid_hy_hpos_6_qty,\r\n" + 
		"  (case when hpos=7 then qty else 0 end) valid_hy_hpos_7_qty,\r\n" + 
		"  (case when hpos=8 then qty else 0 end) valid_hy_hpos_8_qty,\r\n" + 
		"  (case when hpos=9 then qty else 0 end) valid_hy_hpos_9_qty,\r\n" + 
		"  (case when hpos=10 then qty else 0 end) valid_hy_hpos_10_qty,\r\n" + 
		"  (case when hpos>=5 then qty else 0 end) valid_hy_hpos_above_5_qty\r\n" + 
		"  from(\r\n" + 
		"   select count(hybh) as qty,sum(sales) as sales,province_name,hpos,qpos,is_new_hy from monthly_hyzl_2021\r\n" + 
		"   where create_date=STR_TO_DATE('2021.10.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		"   and qxmonth=''\r\n" + 
		"   group by province_name,hpos,qpos,is_new_hy  \r\n" + 
		"  ) zzzz  -- 8s,760条\r\n" + 
		"  ) t on t.province_name=a.province_name and t.hpos=a.hpos and t.qpos=a.qpos and t.is_new_hy=a.is_new_hy\r\n" + 
		"  left join\r\n" + 
		"  (\r\n" + 
		"     select zzzza.province_name,zzzza.hpos,zzzza.qpos,zzzza.is_new_hy,\r\n" + 
		"  sum(active_3_hy_qty) as active_3_hy_qty,\r\n" + 
		"  sum(active_4_hy_qty) as active_4_hy_qty,\r\n" + 
		"  sum(active_5_hy_qty) as active_5_hy_qty,\r\n" + 
		"  sum(active_6_hy_qty) as active_6_hy_qty,\r\n" + 
		"  sum(active_7_hy_qty) as active_7_hy_qty,\r\n" + 
		"  sum(active_8_hy_qty) as active_8_hy_qty,\r\n" + 
		"  sum(active_9_hy_qty) as active_9_hy_qty,\r\n" + 
		"  sum(active_10_hy_qty) as active_10_hy_qty,\r\n" + 
		"  sum(active_11_hy_qty) as active_11_hy_qty,\r\n" + 
		"  sum(active_12_hy_qty) as active_12_hy_qty\r\n" + 
		"  from(\r\n" + 
		"    select zzzz.*,\r\n" + 
		"   (case when active_months=3 then qty else 0 end) active_3_hy_qty,\r\n" + 
		"   (case when active_months=4 then qty else 0 end) active_4_hy_qty,\r\n" + 
		"   (case when active_months=5 then qty else 0 end) active_5_hy_qty,\r\n" + 
		"   (case when active_months=6 then qty else 0 end) active_6_hy_qty,\r\n" + 
		"   (case when active_months=7 then qty else 0 end) active_7_hy_qty,\r\n" + 
		"   (case when active_months=8 then qty else 0 end) active_8_hy_qty,\r\n" + 
		"   (case when active_months=9 then qty else 0 end) active_9_hy_qty,\r\n" + 
		"   (case when active_months=10 then qty else 0 end) active_10_hy_qty,\r\n" + 
		"   (case when active_months=11 then qty else 0 end) active_11_hy_qty,\r\n" + 
		"   (case when active_months=12 then qty else 0 end) active_12_hy_qty\r\n" + 
		"   from(\r\n" + 
		"    select count(hybh) as qty,sum(sales) as sales,province_name,hpos,qpos,is_new_hy,active_months from monthly_hyzl_2021\r\n" + 
		"    where create_date=STR_TO_DATE('2021.10.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		"    and qxmonth=''\r\n" + 
		"    group by province_name,hpos,qpos,is_new_hy,active_months  \r\n" + 
		"   ) zzzz  \r\n" + 
		"  )zzzza\r\n" + 
		"  group by zzzza.province_name,zzzza.hpos,zzzza.qpos,zzzza.is_new_hy -- 6s 726条(耗时只有1/4以下)\r\n" + 
		"  ) z on z.province_name=a.province_name and z.hpos=a.hpos and z.qpos=a.qpos and z.is_new_hy=a.is_new_hy  \r\n" + 
		"  left join\r\n" + 
		"  (\r\n" + 
		"     select zzzza.province_name,zzzza.hpos,zzzza.qpos,zzzza.is_new_hy,\r\n" + 
		"  sum(unactive_3_hy_qty) as unactive_3_hy_qty,\r\n" + 
		"  sum(unactive_4_hy_qty) as unactive_4_hy_qty,\r\n" + 
		"  sum(unactive_5_hy_qty) as unactive_5_hy_qty,\r\n" + 
		"  sum(unactive_6_hy_qty) as unactive_6_hy_qty,\r\n" + 
		"  sum(unactive_7_hy_qty) as unactive_7_hy_qty,\r\n" + 
		"  sum(unactive_8_hy_qty) as unactive_8_hy_qty,\r\n" + 
		"  sum(unactive_9_hy_qty) as unactive_9_hy_qty,\r\n" + 
		"  sum(unactive_10_hy_qty) as unactive_10_hy_qty,\r\n" + 
		"  sum(unactive_11_hy_qty) as unactive_11_hy_qty,\r\n" + 
		"  sum(unactive_12_hy_qty) as unactive_12_hy_qty\r\n" + 
		"  from(\r\n" + 
		"   select zzzz.*,\r\n" + 
		"   (case when unactive_months=3 then qty else 0 end) unactive_3_hy_qty,\r\n" + 
		"   (case when unactive_months=4 then qty else 0 end) unactive_4_hy_qty,\r\n" + 
		"   (case when unactive_months=5 then qty else 0 end) unactive_5_hy_qty,\r\n" + 
		"   (case when unactive_months=6 then qty else 0 end) unactive_6_hy_qty,\r\n" + 
		"   (case when unactive_months=7 then qty else 0 end) unactive_7_hy_qty,\r\n" + 
		"   (case when unactive_months=8 then qty else 0 end) unactive_8_hy_qty,\r\n" + 
		"   (case when unactive_months=9 then qty else 0 end) unactive_9_hy_qty,\r\n" + 
		"   (case when unactive_months=10 then qty else 0 end) unactive_10_hy_qty,\r\n" + 
		"   (case when unactive_months=11 then qty else 0 end) unactive_11_hy_qty,\r\n" + 
		"   (case when unactive_months=12 then qty else 0 end) unactive_12_hy_qty\r\n" + 
		"   from(\r\n" + 
		"    select count(hybh) as qty,sum(sales) as sales,province_name,hpos,qpos,is_new_hy,unactive_months from monthly_hyzl_2021\r\n" + 
		"    where create_date=STR_TO_DATE('2021.10.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		"    and qxmonth=''\r\n" + 
		"    group by province_name,hpos,qpos,is_new_hy,unactive_months  \r\n" + 
		"   ) zzzz  \r\n" + 
		"  )zzzza\r\n" + 
		"  group by zzzza.province_name,zzzza.hpos,zzzza.qpos,zzzza.is_new_hy -- 6s 726条(耗时只有1/4以下)\r\n" + 
		"  ) aj on aj.province_name=a.province_name and aj.hpos=a.hpos and aj.qpos=a.qpos and aj.is_new_hy=a.is_new_hy  \r\n" + 
		"  left join\r\n" + 
		"  (\r\n" + 
		"   select count(hybh) as buy_above_2_hy_qty,province_name,hpos,qpos,is_new_hy from monthly_hyzl_2021 \r\n" + 
		"    where create_date=STR_TO_DATE('2021.10.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		"    and qxmonth='' and buy_months>1\r\n" + 
		"    group by province_name,hpos,qpos,is_new_hy\r\n" + 
		"  ) av on av.province_name=a.province_name and av.hpos=a.hpos and av.qpos=a.qpos and av.is_new_hy=a.is_new_hy\r\n" + 
		"  left join\r\n" + 
		"  (\r\n" + 
		"   select count(hybh) as buy_2_new_hy_qty,province_name,hpos,qpos,is_new_hy from monthly_hyzl_2021 \r\n" + 
		"    where create_date=STR_TO_DATE('2021.10.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		"    and accmon='2021.09'   \r\n" + 
		"    and qxmonth='' and buy_months=2\r\n" + 
		"    group by province_name,hpos,qpos,is_new_hy\r\n" + 
		"  ) aw on aw.province_name=a.province_name and aw.hpos=a.hpos and aw.qpos=a.qpos and aw.is_new_hy=a.is_new_hy	\r\n" + 
		"";
	}

	public static String getSql2() {
return "select * from "+
	"("+
		"select "+
	   " STR_TO_DATE('2021.10.01 00:00:00','%Y.%m.%d %H:%i:%s') as create_date \r\n" + 
		"  ,a.province_name,a.hpos,a.qpos,a.is_new_hy,\r\n" + 
		"  date_add(STR_TO_DATE('2021.10.01 00:00:00','%Y.%m.%d %H:%i:%s'), INTERVAL -1 year) as ly_create_date,\r\n" + 
		"  date_add(STR_TO_DATE('2021.10.01 00:00:00','%Y.%m.%d %H:%i:%s'), INTERVAL -1 month) as lm_create_date\r\n" + 		
		"   , b.new_hy_qty"+
		
		",b.new_hy_sales"+  //这行报错,应该是sum(decimal)的字段没插入成功
		",b.new_buyer_qty\r\n" + 
		"   ,xxx.valid_hy_qty ,t.valid_hy_hpos_0_qty,t.valid_hy_hpos_5_qty,t.valid_hy_hpos_6_qty,t.valid_hy_hpos_7_qty,t.valid_hy_hpos_8_qty,t.valid_hy_hpos_9_qty,t.valid_hy_hpos_10_qty,t.valid_hy_hpos_above_5_qty,\r\n" + 
		"   d.cancel_hy_qty,e.cancel_hy_hpos_0_qty,e.cancel_hy_hpos_5_qty,e.cancel_hy_hpos_6_qty,e.cancel_hy_hpos_7_qty,e.cancel_hy_hpos_8_qty,e.cancel_hy_hpos_9_qty,e.cancel_hy_hpos_10_qty,\r\n" + 
		"   k.buyer_qty,l.buyer_hpos_0_qty,l.buyer_hpos_5_qty,l.buyer_hpos_6_qty,l.buyer_hpos_7_qty,l.buyer_hpos_8_qty,l.buyer_hpos_9_qty,l.buyer_hpos_10_qty,\r\n" + 
		"   k.buyer_sales,l.buyer_hpos_0_sales,l.buyer_hpos_5_sales,l.buyer_hpos_6_sales,l.buyer_hpos_7_sales,l.buyer_hpos_8_sales,l.buyer_hpos_9_sales,l.buyer_hpos_10_sales,\r\n" + 
		"   r.active_hy_qty,r.active_hy_hpos_0_qty\r\n" + 
		"   ,z.active_3_hy_qty,z.active_4_hy_qty,z.active_5_hy_qty,z.active_6_hy_qty,z.active_7_hy_qty,z.active_8_hy_qty,z.active_9_hy_qty,z.active_10_hy_qty,z.active_11_hy_qty,z.active_12_hy_qty,\r\n" + 
		"   aj.unactive_3_hy_qty,aj.unactive_4_hy_qty,aj.unactive_5_hy_qty,aj.unactive_6_hy_qty,aj.unactive_7_hy_qty,aj.unactive_8_hy_qty,aj.unactive_9_hy_qty,aj.unactive_10_hy_qty,aj.unactive_11_hy_qty,aj.unactive_12_hy_qty,\r\n" + 
		"   av.buy_above_2_hy_qty,\r\n" + 
		"   aw.buy_2_new_hy_qty\r\n" + 		
		"   ,0 as ly_new_hy_qty,0 as ly_new_buyer_qty,\r\n" + 
		"   0 as ly_valid_hy_qty,0 as ly_buyer_qty,0 as ly_valid_hy_hpos_above_5_qty,0 as ly_valid_hy_hpos_0_qty,\r\n" + 
		"   0 as lm_new_hy_qty,0 as lm_new_buyer_qty,\r\n" + 
		"   0 as lm_valid_hy_qty,0 as lm_buyer_qty,0 as lm_valid_hy_hpos_above_5_qty,0 as lm_valid_hy_hpos_0_qty\r\n" +
		
		"   from\r\n" + 
		"  ( \r\n" + 
		"    select abcd.province_name,abcd.hpos,abcd.qpos,CAST(abcd.is_new_hy AS char(1)) as is_new_hy\r\n" + 
		"    from(\r\n" + 
		"        select province_name,hpos,qpos,is_new_hy from monthly_hyzl_2021\r\n" + 
		"     where create_date=STR_TO_DATE('2021.10.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		"     union \r\n" + 
		"     select province_name,hpos,qpos,is_new_hy from monthly_hyzl_2021\r\n" + 
		"     where create_date=STR_TO_DATE('2020.10.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		"     union \r\n" + 
		"     select province_name,hpos,qpos,is_new_hy from monthly_hyzl_2021\r\n" + 
		"     where create_date=STR_TO_DATE('2021.09.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		"    ) abcd\r\n" + 
		"    group by province_name,hpos,qpos,CAST(abcd.is_new_hy AS char(1))\r\n" + 
		"  ) a \r\n" + 
		"  left join \r\n" + 
		"  (\r\n" + 
		" select count(hybh) as valid_hy_qty,province_name,hpos,qpos,is_new_hy from monthly_hyzl_2021 \r\n" + 
		" where create_date=STR_TO_DATE('2021.10.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		"    and qxmonth='' \r\n" + 
		"    group by province_name,hpos,qpos,is_new_hy\r\n" + 
		"  ) xxx on xxx.province_name=a.province_name and xxx.hpos=a.hpos and xxx.qpos=a.qpos and xxx.is_new_hy=a.is_new_hy\r\n" + 
		"  left join \r\n" + 
		"  (\r\n" + 
		"    select count(hybh) as new_hy_qty,sum(sales) as new_hy_sales,\r\n" + 
		"      SUM(case when sales>0 then 1 else 0 end) as new_buyer_qty,\r\n" + 
		"      province_name,hpos,qpos,is_new_hy \r\n" + 
		"    from monthly_hyzl_2021 \r\n" + 
		"    where create_date=STR_TO_DATE('2021.10.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		"    and accmon='2021.10'    \r\n" + 
		"    group by province_name,hpos,qpos,is_new_hy\r\n" + 
		"  ) b on b.province_name=a.province_name and b.hpos=a.hpos and b.qpos=a.qpos and b.is_new_hy=a.is_new_hy\r\n" + 
		"  left join\r\n" + 
		"  (\r\n" + 
		"   select count(hybh) as cancel_hy_qty,province_name,hpos,qpos,is_new_hy from monthly_hyzl_2021 \r\n" + 
		"    where create_date=STR_TO_DATE('2021.11.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		" and qxmonth='2021.10'    \r\n" + 
		"    group by province_name,hpos,qpos,is_new_hy\r\n" + 
		"  ) d on d.province_name=a.province_name and d.hpos=a.hpos and d.qpos=a.qpos and d.is_new_hy=a.is_new_hy\r\n" + 
		"  left join\r\n" + 
		"  (\r\n" + 
		"  select zzzz.*,\r\n" + 
		"  (case when hpos=0 then qty else 0 end) cancel_hy_hpos_0_qty,\r\n" + 
		"  (case when hpos=5 then qty else 0 end) cancel_hy_hpos_5_qty,\r\n" + 
		"  (case when hpos=6 then qty else 0 end) cancel_hy_hpos_6_qty,\r\n" + 
		"  (case when hpos=7 then qty else 0 end) cancel_hy_hpos_7_qty,\r\n" + 
		"  (case when hpos=8 then qty else 0 end) cancel_hy_hpos_8_qty,\r\n" + 
		"  (case when hpos=9 then qty else 0 end) cancel_hy_hpos_9_qty,\r\n" + 
		"  (case when hpos=10 then qty else 0 end) cancel_hy_hpos_10_qty\r\n" + 
		"  from(\r\n" + 
		"   select count(hybh) as qty,sum(sales) as sales,province_name,hpos,qpos,is_new_hy from monthly_hyzl_2021 \r\n" + 
		"   where create_date=STR_TO_DATE('2021.11.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		"   and qxmonth='2021.10'\r\n" + 
		"   group by province_name,hpos,qpos,is_new_hy  \r\n" + 
		"  ) zzzz  -- 6s,147条\r\n" + 
		"  ) e on e.province_name=a.province_name and e.hpos=a.hpos and e.qpos=a.qpos and e.is_new_hy=a.is_new_hy\r\n" + 
		"  left join\r\n" + 
		"  (\r\n" + 
		"   select count(hybh) as buyer_qty,sum(sales) as buyer_sales,province_name,hpos,qpos,is_new_hy from monthly_hyzl_2021 \r\n" + 
		"    where create_date=STR_TO_DATE('2021.10.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		"    and sales>0\r\n" + 
		"    group by province_name,hpos,qpos,is_new_hy\r\n" + 
		"  ) k on k.province_name=a.province_name and k.hpos=a.hpos and k.qpos=a.qpos and k.is_new_hy=a.is_new_hy\r\n" + 
		"  left join\r\n" + 
		"  (\r\n" + 
		"  select zzzz.*,\r\n" + 
		"  (case when hpos=0 then qty else 0 end) buyer_hpos_0_qty,\r\n" + 
		"  (case when hpos=5 then qty else 0 end) buyer_hpos_5_qty,\r\n" + 
		"  (case when hpos=6 then qty else 0 end) buyer_hpos_6_qty,\r\n" + 
		"  (case when hpos=7 then qty else 0 end) buyer_hpos_7_qty,\r\n" + 
		"  (case when hpos=8 then qty else 0 end) buyer_hpos_8_qty,\r\n" + 
		"  (case when hpos=9 then qty else 0 end) buyer_hpos_9_qty,\r\n" + 
		"  (case when hpos=10 then qty else 0 end) buyer_hpos_10_qty,\r\n" + 
		"  (case when hpos=0 then sales else 0 end) buyer_hpos_0_sales,\r\n" + 
		"  (case when hpos=5 then sales else 0 end) buyer_hpos_5_sales,\r\n" + 
		"  (case when hpos=6 then sales else 0 end) buyer_hpos_6_sales,\r\n" + 
		"  (case when hpos=7 then sales else 0 end) buyer_hpos_7_sales,\r\n" + 
		"  (case when hpos=8 then sales else 0 end) buyer_hpos_8_sales,\r\n" + 
		"  (case when hpos=9 then sales else 0 end) buyer_hpos_9_sales,\r\n" + 
		"  (case when hpos=10 then sales else 0 end) buyer_hpos_10_sales\r\n" + 
		"  from(\r\n" + 
		"   select count(hybh) as qty,sum(sales) as sales,province_name,hpos,qpos,is_new_hy from monthly_hyzl_2021\r\n" + 
		"   where create_date=STR_TO_DATE('2021.10.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		"   and sales>0\r\n" + 
		"   group by province_name,hpos,qpos,is_new_hy  \r\n" + 
		"  ) zzzz  -- 8s,760条\r\n" + 
		"  ) l on l.province_name=a.province_name and l.hpos=a.hpos and l.qpos=a.qpos and l.is_new_hy=a.is_new_hy\r\n" + 
		"  left join\r\n" + 
		"  (\r\n" + 
		"     select zzzz.*,\r\n" + 
		"  qty as active_hy_qty,\r\n" + 
		"  (case when hpos=0 then qty else 0 end) active_hy_hpos_0_qty\r\n" + 
		"  from(\r\n" + 
		"   select count(hybh) as qty,province_name,hpos,qpos,is_new_hy from monthly_hyzl_2021\r\n" + 
		"   where create_date=STR_TO_DATE('2021.10.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		"   and pv>=200\r\n" + 
		"   group by province_name,hpos,qpos,is_new_hy  \r\n" + 
		"  ) zzzz \r\n" + 
		"  ) r on r.province_name=a.province_name and r.hpos=a.hpos and r.qpos=a.qpos and r.is_new_hy=a.is_new_hy  \r\n" + 
		"  left join\r\n" + 
		"  (\r\n" + 
		"  select zzzz.*,\r\n" + 
		"  (case when hpos=0 then qty else 0 end) valid_hy_hpos_0_qty,\r\n" + 
		"  (case when hpos=5 then qty else 0 end) valid_hy_hpos_5_qty,\r\n" + 
		"  (case when hpos=6 then qty else 0 end) valid_hy_hpos_6_qty,\r\n" + 
		"  (case when hpos=7 then qty else 0 end) valid_hy_hpos_7_qty,\r\n" + 
		"  (case when hpos=8 then qty else 0 end) valid_hy_hpos_8_qty,\r\n" + 
		"  (case when hpos=9 then qty else 0 end) valid_hy_hpos_9_qty,\r\n" + 
		"  (case when hpos=10 then qty else 0 end) valid_hy_hpos_10_qty,\r\n" + 
		"  (case when hpos>=5 then qty else 0 end) valid_hy_hpos_above_5_qty\r\n" + 
		"  from(\r\n" + 
		"   select count(hybh) as qty,sum(sales) as sales,province_name,hpos,qpos,is_new_hy from monthly_hyzl_2021\r\n" + 
		"   where create_date=STR_TO_DATE('2021.10.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		"   and qxmonth=''\r\n" + 
		"   group by province_name,hpos,qpos,is_new_hy  \r\n" + 
		"  ) zzzz  -- 8s,760条\r\n" + 
		"  ) t on t.province_name=a.province_name and t.hpos=a.hpos and t.qpos=a.qpos and t.is_new_hy=a.is_new_hy\r\n" + 
		"  left join\r\n" + 
		"  (\r\n" + 
		"     select zzzza.province_name,zzzza.hpos,zzzza.qpos,zzzza.is_new_hy,\r\n" + 
		"  sum(active_3_hy_qty) as active_3_hy_qty,\r\n" + 
		"  sum(active_4_hy_qty) as active_4_hy_qty,\r\n" + 
		"  sum(active_5_hy_qty) as active_5_hy_qty,\r\n" + 
		"  sum(active_6_hy_qty) as active_6_hy_qty,\r\n" + 
		"  sum(active_7_hy_qty) as active_7_hy_qty,\r\n" + 
		"  sum(active_8_hy_qty) as active_8_hy_qty,\r\n" + 
		"  sum(active_9_hy_qty) as active_9_hy_qty,\r\n" + 
		"  sum(active_10_hy_qty) as active_10_hy_qty,\r\n" + 
		"  sum(active_11_hy_qty) as active_11_hy_qty,\r\n" + 
		"  sum(active_12_hy_qty) as active_12_hy_qty\r\n" + 
		"  from(\r\n" + 
		"    select zzzz.*,\r\n" + 
		"   (case when active_months=3 then qty else 0 end) active_3_hy_qty,\r\n" + 
		"   (case when active_months=4 then qty else 0 end) active_4_hy_qty,\r\n" + 
		"   (case when active_months=5 then qty else 0 end) active_5_hy_qty,\r\n" + 
		"   (case when active_months=6 then qty else 0 end) active_6_hy_qty,\r\n" + 
		"   (case when active_months=7 then qty else 0 end) active_7_hy_qty,\r\n" + 
		"   (case when active_months=8 then qty else 0 end) active_8_hy_qty,\r\n" + 
		"   (case when active_months=9 then qty else 0 end) active_9_hy_qty,\r\n" + 
		"   (case when active_months=10 then qty else 0 end) active_10_hy_qty,\r\n" + 
		"   (case when active_months=11 then qty else 0 end) active_11_hy_qty,\r\n" + 
		"   (case when active_months=12 then qty else 0 end) active_12_hy_qty\r\n" + 
		"   from(\r\n" + 
		"    select count(hybh) as qty,sum(sales) as sales,province_name,hpos,qpos,is_new_hy,active_months from monthly_hyzl_2021\r\n" + 
		"    where create_date=STR_TO_DATE('2021.10.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		"    and qxmonth=''\r\n" + 
		"    group by province_name,hpos,qpos,is_new_hy,active_months  \r\n" + 
		"   ) zzzz  \r\n" + 
		"  )zzzza\r\n" + 
		"  group by zzzza.province_name,zzzza.hpos,zzzza.qpos,zzzza.is_new_hy -- 6s 726条(耗时只有1/4以下)\r\n" + 
		"  ) z on z.province_name=a.province_name and z.hpos=a.hpos and z.qpos=a.qpos and z.is_new_hy=a.is_new_hy  \r\n" + 
		"  left join\r\n" + 
		"  (\r\n" + 
		"     select zzzza.province_name,zzzza.hpos,zzzza.qpos,zzzza.is_new_hy,\r\n" + 
		"  sum(unactive_3_hy_qty) as unactive_3_hy_qty,\r\n" + 
		"  sum(unactive_4_hy_qty) as unactive_4_hy_qty,\r\n" + 
		"  sum(unactive_5_hy_qty) as unactive_5_hy_qty,\r\n" + 
		"  sum(unactive_6_hy_qty) as unactive_6_hy_qty,\r\n" + 
		"  sum(unactive_7_hy_qty) as unactive_7_hy_qty,\r\n" + 
		"  sum(unactive_8_hy_qty) as unactive_8_hy_qty,\r\n" + 
		"  sum(unactive_9_hy_qty) as unactive_9_hy_qty,\r\n" + 
		"  sum(unactive_10_hy_qty) as unactive_10_hy_qty,\r\n" + 
		"  sum(unactive_11_hy_qty) as unactive_11_hy_qty,\r\n" + 
		"  sum(unactive_12_hy_qty) as unactive_12_hy_qty\r\n" + 
		"  from(\r\n" + 
		"   select zzzz.*,\r\n" + 
		"   (case when unactive_months=3 then qty else 0 end) unactive_3_hy_qty,\r\n" + 
		"   (case when unactive_months=4 then qty else 0 end) unactive_4_hy_qty,\r\n" + 
		"   (case when unactive_months=5 then qty else 0 end) unactive_5_hy_qty,\r\n" + 
		"   (case when unactive_months=6 then qty else 0 end) unactive_6_hy_qty,\r\n" + 
		"   (case when unactive_months=7 then qty else 0 end) unactive_7_hy_qty,\r\n" + 
		"   (case when unactive_months=8 then qty else 0 end) unactive_8_hy_qty,\r\n" + 
		"   (case when unactive_months=9 then qty else 0 end) unactive_9_hy_qty,\r\n" + 
		"   (case when unactive_months=10 then qty else 0 end) unactive_10_hy_qty,\r\n" + 
		"   (case when unactive_months=11 then qty else 0 end) unactive_11_hy_qty,\r\n" + 
		"   (case when unactive_months=12 then qty else 0 end) unactive_12_hy_qty\r\n" + 
		"   from(\r\n" + 
		"    select count(hybh) as qty,sum(sales) as sales,province_name,hpos,qpos,is_new_hy,unactive_months from monthly_hyzl_2021\r\n" + 
		"    where create_date=STR_TO_DATE('2021.10.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		"    and qxmonth=''\r\n" + 
		"    group by province_name,hpos,qpos,is_new_hy,unactive_months  \r\n" + 
		"   ) zzzz  \r\n" + 
		"  )zzzza\r\n" + 
		"  group by zzzza.province_name,zzzza.hpos,zzzza.qpos,zzzza.is_new_hy -- 6s 726条(耗时只有1/4以下)\r\n" + 
		"  ) aj on aj.province_name=a.province_name and aj.hpos=a.hpos and aj.qpos=a.qpos and aj.is_new_hy=a.is_new_hy  \r\n" + 
		"  left join\r\n" + 
		"  (\r\n" + 
		"   select count(hybh) as buy_above_2_hy_qty,province_name,hpos,qpos,is_new_hy from monthly_hyzl_2021 \r\n" + 
		"    where create_date=STR_TO_DATE('2021.10.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		"    and qxmonth='' and buy_months>1\r\n" + 
		"    group by province_name,hpos,qpos,is_new_hy\r\n" + 
		"  ) av on av.province_name=a.province_name and av.hpos=a.hpos and av.qpos=a.qpos and av.is_new_hy=a.is_new_hy\r\n" + 
		"  left join\r\n" + 
		"  (\r\n" + 
		"   select count(hybh) as buy_2_new_hy_qty,province_name,hpos,qpos,is_new_hy from monthly_hyzl_2021 \r\n" + 
		"    where create_date=STR_TO_DATE('2021.10.01 00:00:00','%Y.%m.%d %H:%i:%s') \r\n" + 
		"    and accmon='2021.09'   \r\n" + 
		"    and qxmonth='' and buy_months=2\r\n" + 
		"    group by province_name,hpos,qpos,is_new_hy\r\n" + 
		"  ) aw on aw.province_name=a.province_name and aw.hpos=a.hpos and aw.qpos=a.qpos and aw.is_new_hy=a.is_new_hy	\r\n" + 
		") ba limit 1";
	}
}


