package org.sellgirlPayClickHouse.model;

import com.sellgirl.sgJavaClickHouseHelper.SGClickHouseSqlExecute;
import com.sellgirl.sgJavaHelper.ISGConfigMapper;
import com.sellgirl.sgJavaHelper.PFModelConfigMapper;
import com.sellgirl.sgJavaHelper.PFNetworkConfig;
import com.sellgirl.sgJavaHelper.SGPathConfig;
import com.sellgirl.sgJavaHelper.PFRedisConfig;
import com.sellgirl.sgJavaHelper.sql.ISGJdbc;
import com.sellgirl.sgJavaHelper.sql.ISqlExecute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PFConfigTestMapper implements ISGConfigMapper
{
    public List<PFModelConfigMapper> GetModelConfigMapper()
    {
        return new ArrayList<PFModelConfigMapper>(Arrays.asList(
        		//#region 对应XML节点
    	        new PFModelConfigMapper
                (
                    "yjquery",
                    "yjquery"
                ),
                new PFModelConfigMapper
                (
                    "newshop",
                    "newshop"
                ),
                new PFModelConfigMapper
                (
                    "inv",
                    "inv"
                ),
                new PFModelConfigMapper
                (
                    "order",
                    "order"
                ),
                new PFModelConfigMapper
                (
                    "hyzl",
                    "hyzl"
                ),
                new PFModelConfigMapper
                (
                    "tc",
                    "tc"
                ),
    //#endregion
                //#region 按Area
    	        new PFModelConfigMapper//奖金
                (
                    "bonus",
                    "bonus",
                    new ArrayList<>(Arrays.asList( "newshop","yjquery" ))
                ), 
    //#endregion
                    //#region 对应Model
    	            new PFModelConfigMapper(
                    "InvoiceDBInfo",
                    "InvoiceDBInfo",
                    new ArrayList<>(Arrays.asList("yjquery"))
                    //ExProperty=new List<PFModelPropertyConfigMapper>(
                    //    new PFModelPropertyConfigMapper("AgentNo","yjquery","hyxm")
                    //)
                ),
                new PFModelConfigMapper
                (
                    "PerformanceAnalysis",
                    "PerformanceAnalysis"
                ),
                new PFModelConfigMapper
                (
                    "Hyzl",
                    "yjquery",
                    new ArrayList<>(Arrays.asList(  "hyzl" , "newshop" ))
                ),
                new PFModelConfigMapper
                (
                    "Agent",
                    "yjquery"
                ),
                new PFModelConfigMapper
                (
                    "HyRecentShop",
                    "yjquery",
                    new ArrayList<>(Arrays.asList("hyzl" ) )
                ),
                new PFModelConfigMapper
                (
                    "HyLiBao",
                    "yjquery",
                    new ArrayList<>(Arrays.asList( "hyzl" ) )
                ),
                new PFModelConfigMapper
                (
                    "HyNoActivity",
                    "HyNoActivity",
                    new ArrayList<>(Arrays.asList( "yjquery" , "hyzl" ) )
                ),
                new PFModelConfigMapper
                (
                    "BFreeze",
                    "BFreeze"
                ),
                new PFModelConfigMapper
                (
                    "Hyzl.Product",
                    "Hyzl.Product",
                    new ArrayList<>(Arrays.asList("hyzl","inv","yjquery","order"  ))
                ) ,
                new PFModelConfigMapper
                (
                    "Hyzl.Home.AgentPopups",
                    "Hyzl.Home.AgentPopups",
                    new ArrayList<>(Arrays.asList( "yjquery" ))
                ) ,
                new PFModelConfigMapper
                (
                    "YJProvinceNet",
                    "bonus",
                    new ArrayList<>(Arrays.asList( "yjquery" ))
                ) ,
                new PFModelConfigMapper
                (
                    "Meshwork",
                    "bonus",
                    new ArrayList<>(Arrays.asList(  "yjquery" ))
                ) ,
                new PFModelConfigMapper
                (
                    "UserVisitLog",
                    "UserVisitLog"
                ),
                new PFModelConfigMapper
                (
                    "Customer",
                    "Customer",
                    new ArrayList<>(Arrays.asList( "yjquery" ))
                ) ,
                new PFModelConfigMapper
                (
                    "MobileMessage",
                    "MobileMessage"
                ),
                new PFModelConfigMapper
                (
                    "DayOrders",
                    "DayOrders"
                ) 
   //#endregion 对应Model
        		));
    }

    public PFNetworkConfig GetNetworkConfig()
    {
        return new PFNetworkConfig();
    }

    public SGPathConfig GetPathConfig()
    {
        return new SGPathConfig();
    }
    /*
     * 便于有些项目不引用clickhouse的
     * @see pf.java.pfHelper.IPFConfigMapper#GetClickHouseSqlExecute(pf.java.pfHelper.IPFJdbc)
     */
    @Override
    public ISqlExecute GetClickHouseSqlExecute(ISGJdbc jdbc) throws Exception {
        return new SGClickHouseSqlExecute(jdbc);
    }
//    @Override
//    public ISqlExecute GetClickHouseSqlExecute(IPFJdbc jdbc,boolean sendConnErrorMessage) throws Exception
//    {
//        //return new PFClickHouseSqlExecute(jdbc);
//        return null;
//    }

	@Override
	public PFRedisConfig GetRedisConfig() {
//		PFRedisConfig r =new PFRedisConfig();
//		r.setIp("cloud.perfect99.com:10133");
//		r.setPassword("perfect@PRODUCE");
//		return r;
		return null;
	}

	@Override
	public boolean SendMobileMessage(String[] mobileNumber, String msg) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T getBeanInstance(Class<T> type) {
		// TODO Auto-generated method stub
		try {
			return type.getConstructor().newInstance();
		} catch (Exception e) {
			
			return null;
		}
	}
}