package com.sellgirl.sellgirlPayWeb.configuration.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sellgirl.sgJavaHelper.sql.ISGJdbc;
import com.sellgirl.sgJavaHelper.sql.SGJdbc;
import com.sellgirl.sgJavaHelper.sql.PFJdbcBase;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

@Component
public class JdbcHelper {

//	  protected static YJQueryJdbc _yjQueryJdbc;
//	  protected static YJQueryMonthJdbc _yjQueryMonthJdbc;
//	  protected static BalanceJdbc _balanceJdbc;
//	  protected static DayJdbc _dayJdbc;
//	  protected static ChangeUpdateJdbc _changeUpdateJdbc;
//	  protected static BonusJdbc _bonusJdbc;
//	  protected static Bonus33Jdbc _bonus33Jdbc;
//	  protected static ClickHouseSaleJdbc _clickHouseSaleJdbc;
//	  protected static ClickHouseShopDataJdbc _clickHouseShopDataJdbc;
//	  protected static TidbSaleJdbc _tidbSaleJdbc;
//	  protected static YunXiShopJdbc _yunXiShopJdbc;
//	  protected static LiGeShopJdbc _liGeShopJdbc;
//	  protected static LiGeShop2Jdbc _liGeShop2Jdbc;
//	  protected static LiGeOrderJdbc _liGeOrderJdbc;
//	  protected static LiGeMemberJdbc _liGeMemberJdbc;
//	  protected static LiGeProdJdbc _liGeProdJdbc;
//	  protected static LiGeProdSettlementJdbc _liGeProdSettlementJdbc;
//	  protected static LiGeSaleJdbc _liGeSaleJdbc;
//	  protected static LiGeSale2Jdbc _liGeSale2Jdbc;
//	  protected static BonusTestJdbc _bonusTestJdbc;
//	  protected static UserProfileJdbc _userProfileJdbc;
//	  protected static SellGirlJdbc _sellGirlJdbc;
//	  protected static SellGirlJdbc2 _sellGirlJdbc2;
	  protected static PFJdbcBase _shopJdbc;
	  @Autowired
	  public JdbcHelper(
//			  YJQueryJdbc yjQueryJdbc,YJQueryMonthJdbc yjQueryMonthJdbc,
//			  BalanceJdbc balanceJdbc,DayJdbc dayJdbc,
//			   ChangeUpdateJdbc changeUpdateJdbc,
//			  BonusJdbc bonusJdbc,
//			  Bonus33Jdbc bonus33Jdbc,
//			  ClickHouseSaleJdbc clickHouseSaleJdbc,
//			  ClickHouseShopDataJdbc clickHouseShopDataJdbc
//			  ,TidbSaleJdbc tidbSaleJdbc,YunXiShopJdbc yunXiShopJdbc
//			  ,LiGeShopJdbc liGeShopJdbc
//			  ,LiGeShop2Jdbc liGeShopJdbc2,
//			  LiGeOrderJdbc liGeOrderJdbc,LiGeMemberJdbc liGeMemberJdbc
//			  ,LiGeProdJdbc liGeProdJdbc
//			  ,LiGeProdSettlementJdbc liGeProdSettlementJdbc,
//			  LiGeSaleJdbc liGeSaleJdbc,
//			  LiGeSale2Jdbc liGeSaleJdbc2,
//			  BonusTestJdbc bonusTestJdbc,UserProfileJdbc userProfileJdbc,
//			  SellGirlJdbc sellGirlJdbc,SellGirlJdbc2 sellGirlJdbc2 
			  //,TestJdbcInject test1
			  ShopJdbc shopJdbc
			  ) {
//		  JdbcHelper._yjQueryJdbc = yjQueryJdbc;
//		  JdbcHelper._yjQueryMonthJdbc = yjQueryMonthJdbc;
//		  JdbcHelper._balanceJdbc = balanceJdbc;
//		  JdbcHelper._dayJdbc = dayJdbc;
//		  JdbcHelper._changeUpdateJdbc = changeUpdateJdbc;
//		  JdbcHelper._bonusJdbc = bonusJdbc;
//		  JdbcHelper._bonus33Jdbc = bonus33Jdbc;
//		  JdbcHelper._clickHouseSaleJdbc = clickHouseSaleJdbc;
//		  JdbcHelper._clickHouseShopDataJdbc = clickHouseShopDataJdbc;
//		  JdbcHelper._tidbSaleJdbc = tidbSaleJdbc;
//		  JdbcHelper._yunXiShopJdbc = yunXiShopJdbc;
//		  JdbcHelper._liGeShopJdbc = liGeShopJdbc;
//		  JdbcHelper._liGeShop2Jdbc = liGeShopJdbc2;
//		  JdbcHelper._liGeOrderJdbc=liGeOrderJdbc;
//		  JdbcHelper._liGeMemberJdbc = liGeMemberJdbc;
//		  JdbcHelper._liGeProdJdbc = liGeProdJdbc;
//		  JdbcHelper._liGeProdSettlementJdbc = liGeProdSettlementJdbc;
//		  JdbcHelper._liGeSaleJdbc = liGeSaleJdbc;
//		  JdbcHelper._liGeSale2Jdbc = liGeSaleJdbc2;
//		  JdbcHelper._bonusTestJdbc = bonusTestJdbc;
//		  JdbcHelper._userProfileJdbc=userProfileJdbc;
//		  JdbcHelper._sellGirlJdbc=sellGirlJdbc;
//		  JdbcHelper._sellGirlJdbc2=sellGirlJdbc2;
		  _shopJdbc=shopJdbc;
	  }
//	    public static ISGJdbc GetYJQuery()
//	    {
//	    	return new YJQueryJdbc().Apply(_yjQueryJdbc);//返回新实例,所以改变引用也没问题
//	    	//return _yjQueryJdbc;
//	    }
//	    public static ISGJdbc GetYJQueryMonth(String ym)
//	    {
//	    	ISGJdbc r=new YJQueryMonthJdbc().Apply(_yjQueryMonthJdbc);
////	    	r.setUrl(r.getUrl()+ym);
////	    	r.setDbName(r.getDbName()+ym);
//	    	r.setUrl(r.getUrl().replace("{ym}", ym));
//	    	r.setDbName(r.getDbName().replace("{ym}", ym));
//	    	return r;
//	    }
//	    /**
//	     * 172.18.2.132:1433/balance
//	     * @return
//	     */
//	    public static ISGJdbc GetBalance()
//	    {
//	    	return new BalanceJdbc().Apply(_balanceJdbc);//返回新实例,所以改变引用也没问题
//	    }
//	    /**
//	     * 33.balance
//	     * @return
//	     */
//	    public static ISGJdbc GetDay()
//	    {
//	    	return new DayJdbc().Apply(_dayJdbc);//返回新实例,所以改变引用也没问题
//	    }
//	    public static ISGJdbc GetChangeUpdate()
//	    {
//	    	return new DayJdbc().Apply(_changeUpdateJdbc);//返回新实例,所以改变引用也没问题
//	    }
//	    public static ISGJdbc GetClickHouseSale()
//	    {
//	    	return new ClickHouseSaleJdbc().Apply(_clickHouseSaleJdbc);//返回新实例,所以改变引用也没问题
//	    }
//	    public static ISGJdbc GetClickHouseShopData()
//	    {
//	    	return new ClickHouseShopDataJdbc().Apply(_clickHouseShopDataJdbc);//返回新实例,所以改变引用也没问题
//	    }
//	    public static ISGJdbc GetTidbSale()
//	    {
//	    	return new TidbSaleJdbc().Apply(_tidbSaleJdbc);//返回新实例,所以改变引用也没问题
//	    	//return _tidbSaleJdbc;
//	    }
//	    public static ISGJdbc GetYunXiShop()
//	    {
//	    	return new YunXiShopJdbc().Apply(_yunXiShopJdbc);//返回新实例,所以改变引用也没问题
//	    	//return _liGeShopJdbc;
//	    }
//	    /**
//	     * cloud.perfect99.com:10177/mall_center_store
//	     * @return
//	     */
//	    public static ISGJdbc GetLiGeShop()
//	    {
//	    	return new LiGeShopJdbc().Apply(_liGeShopJdbc);//返回新实例,所以改变引用也没问题
//	    	//return _liGeShopJdbc;
//	    }
//	    /**
//	     * cloud.perfect99.com:10177/mall_center_store
//	     * @return
//	     */
//	    public static ISGJdbc GetLiGeShop2()
//	    {
//	    	return new LiGeShop2Jdbc().Apply(_liGeShop2Jdbc);//返回新实例,所以改变引用也没问题
//	    }
//	    /**
//	     * cloud.perfect99.com:10077/mall_center_store
//	     * @return
//	     */
//	    public static ISGJdbc GetLiGeOrder()
//	    {
//	    	return new LiGeOrderJdbc().Apply(_liGeOrderJdbc);//返回新实例,所以改变引用也没问题
//	    	//return _liGeShopJdbc;
//	    }
//	    public static ISGJdbc GetLiGeMember()
//	    {
//	    	return new LiGeMemberJdbc().Apply(_liGeMemberJdbc);//返回新实例,所以改变引用也没问题
//	    	//return _liGeShopJdbc;
//	    }
//	    public static ISGJdbc GetLiGeProd()
//	    {
//	    	return new LiGeProdJdbc().Apply(_liGeProdJdbc);//返回新实例,所以改变引用也没问题
//	    	//return _liGeShopJdbc;
//	    }
//	    public static ISGJdbc GetLiGeProdSettlement()
//	    {
//	    	return new LiGeProdSettlementJdbc().Apply(_liGeProdSettlementJdbc);//返回新实例,所以改变引用也没问题
//	    	//return _liGeShopJdbc;
//	    }
//	    /**
//	     * cloud.perfect99.com:10129
//	     * @return
//	     */
//	    public static ISGJdbc GetLiGeSale()
//	    {
//	    	return new LiGeSaleJdbc().Apply(_liGeSaleJdbc);//返回新实例,所以改变引用也没问题
//	    	//return _liGeShopJdbc;
//	    }
//	    /**
//	     * cloud.perfect99.com:10177
//	     * @return
//	     */
//	    public static ISGJdbc GetLiGeSale2()
//	    {
//	    	return new LiGeSale2Jdbc().Apply(_liGeSale2Jdbc);//返回新实例,所以改变引用也没问题
//	    	//return _liGeShopJdbc;
//	    }
//	    /**
//	     * 192.168.0.30:1433/bonus
//	     * @return
//	     */
//	    public static ISGJdbc GetBonus()
//	    {
//	    	if(SGDataHelper.IsDebug()) {
//		    	return new BonusTestJdbc().Apply(_bonusTestJdbc);//返回新实例,所以改变引用也没问题
//
//	    	}else {
//		    	return new BonusJdbc().Apply(_bonusJdbc);//返回新实例,所以改变引用也没问题
//	    	}
//	    }
//	    /**
//	     * 192.168.0.33:1433/bonus{ym}
//	     * @return
//	     */
//	    public static ISGJdbc GetBonus33(String ym)
//	    {
//	    	ISGJdbc r=new Bonus33Jdbc().Apply(_bonus33Jdbc);
////	    	r.setUrl(r.getUrl()+ym);
////	    	r.setDbName(r.getDbName()+ym);
//	    	r.setUrl(r.getUrl().replace("{ym}", ym));
//	    	r.setDbName(r.getDbName().replace("{ym}", ym));
//	    	return r;
//	    }
//	    /**
//	     * 192.168.0.29:1433/bonus
//	     * @return
//	     */
//	    public static ISGJdbc GetBonusTest()
//	    {
//	    	return new BonusTestJdbc().Apply(_bonusTestJdbc);
//	    }
//	    public static ISGJdbc GetUserProfile()	
//	    {	
//	    	return new UserProfileJdbc().Apply(_userProfileJdbc);//返回新实例,所以改变引用也没问题	
//	    	//return _liGeShopJdbc;	
//	    }
//	    public static ISGJdbc GetSellGirlJdbc()
//	    {
//	    	return new SellGirlJdbc().Apply(_sellGirlJdbc);
//	    }
//	    public static ISGJdbc GetSellGirlJdbc2()
//	    {
//	    	return new SellGirlJdbc2().Apply(_sellGirlJdbc2);
//	    }
//	    /**
//	     * uat-cloud.perfect99.com:10129/test
//	     * @return
//	     */
//		public static ISGJdbc GetMySqlTestJdbc() {
//
//			ISGJdbc srcJdbc = new PFJdbc();
//			srcJdbc.setDriverClassName("com.mysql.jdbc.Driver");
//			srcJdbc.setPassword("perfect@EAS");
//			srcJdbc.setUsername("root");
//			srcJdbc.setUrl("jdbc:mysql://uat-cloud.perfect99.com:10129/test?rewriteBatchedStatements=true");
////			srcJdbc.setIp("192.168.0.29");
////			srcJdbc.setPort("1433");
////			srcJdbc.setDbName("balance");
//			return srcJdbc;
//		}

	    public static ISGJdbc GetShop()
	    {
	    	return new ShopJdbc().Apply(_shopJdbc);//返回新实例,所以改变引用也没问题
	    	//return _yjQueryJdbc;
	    }
	    public static void setShop(PFJdbcBase j)//便于单元测试
	    {
	    	_shopJdbc=j;//返回新实例,所以改变引用也没问题
	    	//return _yjQueryJdbc;
	    }
}
