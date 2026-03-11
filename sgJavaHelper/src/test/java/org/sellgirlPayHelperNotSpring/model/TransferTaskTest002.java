package org.sellgirlPayHelperNotSpring.model;

import com.sellgirl.sgJavaHelper.PFCmonth;
import com.sellgirl.sgJavaHelper.PFSqlCommandTimeoutSecond;
import com.sellgirl.sgJavaHelper.SqlExpressionOperator;
import com.sellgirl.sgJavaHelper.TransferTaskBase;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.sql.*;

@SuppressWarnings("unused")
public class TransferTaskTest002 extends TransferTaskBase{

	@Override
	public SGSqlTransferItem get() {
		 SGSqlTransferItem r=super.get();
	     //r.SrcJdbc=JdbcHelper.GetYJQuery();
		 r.SrcJdbc=JdbcHelperTest.GetBalanceJdbc();
		 r.DstJdbc=JdbcHelperTest.GetTidbSale();

//			r.setCmonth("2021.11");
			PFCmonth pfCmonth = new PFCmonth();
			pfCmonth.setCmonth("2021.11");
			r.setSGDate(pfCmonth.ToSGDate());
       r.DstTableName="monthly_hyzl_2021";
       //r.DstTableName="monthly_province_statistics2";//测试表--benjamin todo

      	//r.DstTablePrimaryKeys=new String[] {"create_date", "province_name", "hpos", "qpos", "is_new_hy"};
		 //r.DstTableClusteredIndex=new String[] {"cmonth"};

		 //r.SrcSql=getSql();
		 r.SrcSql=getSql2();

//       r.TransferByMonth=a->{
//       	//a.DstTableName=a.DstTableName.replace("{year}",String.valueOf(a.getYear())); 
//    	   //a.SrcJdbc=JdbcHelper.GetYJQueryMonth(a.getPFCmonth().getYm());yjquerymonth没有当月数据
//       };
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
return "  select *\r\n" + 
		"  from (\r\n" + 
		"  select Cast('2021.11.01' as datetime) as create_date,hyzl.hybh,\r\n" + 
		"hyzl.accmon,'' as qxmonth,\r\n" + 
		"\r\n" + 
		"	(\r\n" + 
		"		case\r\n" + 
		"        when G2.hybh is not null\r\n" + 
		"            then 10\r\n" + 
		"        when CONVERT(int,isnull(p1.hpos,'0'))<5\r\n" + 
		"		    then 0\r\n" + 
		"		else\r\n" + 
		"		CONVERT(int,isnull(p1.hpos,'0'))\r\n" + 
		"		end) as hpos,\r\n" + 
		"	(case when G2.hybh is null then (\r\n" + 
		"		case when CONVERT(int,isnull(p1.hpos,'0'))<5\r\n" + 
		"		then\r\n" + 
		"			0\r\n" + 
		"		when CONVERT(int,isnull(p1.hpos,'0'))>=5 and p1.qpos<5\r\n" + 
		"		then \r\n" + 
		"			4\r\n" + 
		"		else\r\n" + 
		"		CONVERT(int,isnull(p1.qpos,'0'))\r\n" + 
		"		end\r\n" + 
		"	) else 10 end) as qpos,\r\n" + 
		"(case when hyzl.accmon='2021.11' then CONVERT(bit,1) else CONVERT(bit,0) end) as is_new_hy,\r\n" + 
		"isnull(s1.sales,0) as sales,isnull(s1.pv,0) as pv,\r\n" + 
		"-- cus.province as province_name,\r\n" + 
		"-- '' as province_name,\r\n" + 
		"lt_t_hyzl.province as province_name,\r\n" + 
		"(case when s1.pv>=200 then 1 else 0 end ) as active_months,\r\n" + 
		"(case when s1.pv>=200 then 0 else 1 end ) as unactive_months,\r\n" + 
		"(case when s1.pv>=1 then 1 else 0 end ) as buy_months,\r\n" + 
		"CONVERT(bit,0) as has_updated \r\n" + 
		"from hyzl \r\n" + 
		"left join p1 on p1.hybh=hyzl.hybh\r\n" + 
		"left join s1 on s1.hybh=hyzl.hybh\r\n" + 
		"--left join cus on cus.agentno=hyzl.dqbm \r\n" + 
		"left join [LT_SERVER].[balance].[dbo].[t_hyzl] as lt_t_hyzl on lt_t_hyzl.hybh=hyzl.hybh \r\n" + 
		"left join G2 on G2.hybh=hyzl.hybh \r\n" + 
		"-- left join (select hybh from yjquery.dbo.G2 group by hybh) hG2 on hG2.hybh=hyzl.hybh\r\n" + 
		"where hyzl.qx='0'\r\n" + 
		"union\r\n" + 
		"  select Cast('2021.11.01' as datetime) as create_date,hyzl.hybh,\r\n" + 
		"hyzl.accmon,hyzl.qxmonth,\r\n" + 
		"	(\r\n" + 
		"		case when CONVERT(int,isnull(p1.hpos,'0'))<5\r\n" + 
		"		then\r\n" + 
		"			0\r\n" + 
		"		else\r\n" + 
		"		CONVERT(int,isnull(p1.hpos,'0'))\r\n" + 
		"		end) as hpos,\r\n" + 
		"	(case when G2.hybh is null then (\r\n" + 
		"		case when CONVERT(int,isnull(p1.hpos,'0'))<5\r\n" + 
		"		then\r\n" + 
		"			0\r\n" + 
		"		when CONVERT(int,isnull(p1.hpos,'0'))>=5 and p1.qpos<5\r\n" + 
		"		then \r\n" + 
		"			4\r\n" + 
		"		else\r\n" + 
		"		CONVERT(int,isnull(p1.qpos,'0'))\r\n" + 
		"		end\r\n" + 
		"	) else 10 end) as qpos,\r\n" + 
		"(case when hyzl.accmon='2021.11' then CONVERT(bit,1) else CONVERT(bit,0) end) as is_new_hy,\r\n" + 
		"isnull(s1.sales,0) as sales,isnull(s1.pv,0) as pv,\r\n" + 
		"-- cus.province as province_name ,\r\n" + 
		"-- '' as province_name,\r\n" + 
		"lt_t_hyzl.province as province_name,\r\n" + 
		"0 as active_months,\r\n" + 
		"0 as unactive_months,\r\n" + 
		"0 as buy_months,\r\n" + 
		"CONVERT(bit,0) as has_updated \r\n" + 
		"from hyzl \r\n" + 
		"left join p1 on p1.hybh=hyzl.hybh\r\n" + 
		"left join s1 on s1.hybh=hyzl.hybh\r\n" + 
		"-- left join cus on cus.agentno=hyzl.dqbm \r\n" + 
		"left join [LT_SERVER].[balance].[dbo].[t_hyzl] as lt_t_hyzl on lt_t_hyzl.hybh=hyzl.hybh \r\n" + 
		"left join G2 on G2.hybh=hyzl.hybh\r\n" + 
		"where hyzl.qx='1' and hyzl.qxmonth='2021.10'\r\n" + 
		"  ) ba where ba.hybh='00000018'";
	}

	public static String getSql2() {
return "  " + 
		"  " + 
		"  select Cast('2021.11.01' as datetime) as create_date,hyzl.hybh,\r\n" + 
		"hyzl.accmon,'' as qxmonth,\r\n" + 
		"\r\n" + 
		"	(\r\n" + 
		"		case\r\n" + 
		"        when G2.hybh is not null\r\n" + 
		"            then 10\r\n" + 
		"        when CONVERT(int,isnull(p1.hpos,'0'))<5\r\n" + 
		"		    then 0\r\n" + 
		"		else\r\n" + 
		"		CONVERT(int,isnull(p1.hpos,'0'))\r\n" + 
		"		end) as hpos,\r\n" + 
		"	(case when G2.hybh is null then (\r\n" + 
		"		case when CONVERT(int,isnull(p1.hpos,'0'))<5\r\n" + 
		"		then\r\n" + 
		"			0\r\n" + 
		"		when CONVERT(int,isnull(p1.hpos,'0'))>=5 and p1.qpos<5\r\n" + 
		"		then \r\n" + 
		"			4\r\n" + 
		"		else\r\n" + 
		"		CONVERT(int,isnull(p1.qpos,'0'))\r\n" + 
		"		end\r\n" + 
		"	) else 10 end) as qpos,\r\n" + 
		"(case when hyzl.accmon='2021.11' then CONVERT(bit,1) else CONVERT(bit,0) end) as is_new_hy,\r\n" + 
		"isnull(s1.sales,0) as sales,isnull(s1.pv,0) as pv,\r\n" + 
		"-- cus.province as province_name,\r\n" + 
		"-- '' as province_name,\r\n" + 
		"lt_t_hyzl.province as province_name,\r\n" + 
		"(case when s1.pv>=200 then 1 else 0 end ) as active_months,\r\n" + 
		"(case when s1.pv>=200 then 0 else 1 end ) as unactive_months,\r\n" + 
		"(case when s1.pv>=1 then 1 else 0 end ) as buy_months,\r\n" + 
		"CONVERT(bit,0) as has_updated \r\n" + 
		"from hyzl \r\n" + 
		"left join p1 on p1.hybh=hyzl.hybh\r\n" + 
		"left join s1 on s1.hybh=hyzl.hybh\r\n" + 
		"--left join cus on cus.agentno=hyzl.dqbm \r\n" + 
		"left join [LT_SERVER].[balance].[dbo].[t_hyzl] as lt_t_hyzl on lt_t_hyzl.hybh=hyzl.hybh \r\n" + 
		"left join G2 on G2.hybh=hyzl.hybh \r\n" + 
		"-- left join (select hybh from yjquery.dbo.G2 group by hybh) hG2 on hG2.hybh=hyzl.hybh\r\n" + 
		"where hyzl.qx='0'\r\n" + 
		"union\r\n" + 
		"  select Cast('2021.11.01' as datetime) as create_date,hyzl.hybh,\r\n" + 
		"hyzl.accmon,hyzl.qxmonth,\r\n" + 
		"	(\r\n" + 
		"		case when CONVERT(int,isnull(p1.hpos,'0'))<5\r\n" + 
		"		then\r\n" + 
		"			0\r\n" + 
		"		else\r\n" + 
		"		CONVERT(int,isnull(p1.hpos,'0'))\r\n" + 
		"		end) as hpos,\r\n" + 
		"	(case when G2.hybh is null then (\r\n" + 
		"		case when CONVERT(int,isnull(p1.hpos,'0'))<5\r\n" + 
		"		then\r\n" + 
		"			0\r\n" + 
		"		when CONVERT(int,isnull(p1.hpos,'0'))>=5 and p1.qpos<5\r\n" + 
		"		then \r\n" + 
		"			4\r\n" + 
		"		else\r\n" + 
		"		CONVERT(int,isnull(p1.qpos,'0'))\r\n" + 
		"		end\r\n" + 
		"	) else 10 end) as qpos,\r\n" + 
		"(case when hyzl.accmon='2021.11' then CONVERT(bit,1) else CONVERT(bit,0) end) as is_new_hy,\r\n" + 
		"isnull(s1.sales,0) as sales,isnull(s1.pv,0) as pv,\r\n" + 
		"-- cus.province as province_name ,\r\n" + 
		"-- '' as province_name,\r\n" + 
		"lt_t_hyzl.province as province_name,\r\n" + 
		"0 as active_months,\r\n" + 
		"0 as unactive_months,\r\n" + 
		"0 as buy_months,\r\n" + 
		"CONVERT(bit,0) as has_updated \r\n" + 
		"from hyzl \r\n" + 
		"left join p1 on p1.hybh=hyzl.hybh\r\n" + 
		"left join s1 on s1.hybh=hyzl.hybh\r\n" + 
		"-- left join cus on cus.agentno=hyzl.dqbm \r\n" + 
		"left join [LT_SERVER].[balance].[dbo].[t_hyzl] as lt_t_hyzl on lt_t_hyzl.hybh=hyzl.hybh \r\n" + 
		"left join G2 on G2.hybh=hyzl.hybh\r\n" + 
		"where hyzl.qx='1' and hyzl.qxmonth='2021.10'\r\n" + 
		"";
	}
}


