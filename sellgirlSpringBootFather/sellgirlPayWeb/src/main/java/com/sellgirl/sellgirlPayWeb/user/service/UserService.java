package com.sellgirl.sellgirlPayWeb.user.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.sellgirl.sellgirlPayDao.TestDAO;
import com.sellgirl.sellgirlPayWeb.configuration.jdbc.JdbcHelper;
import com.sellgirl.sellgirlPayWeb.user.model.User;
import com.sellgirl.sellgirlPayWeb.user.model.UserCreate;
import com.sellgirl.sgJavaHelper.SGDataTable;
import com.sellgirl.sgJavaHelper.PFMySqlWhereCollection;
import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.SGSpeedCounter;
import com.sellgirl.sgJavaHelper.SGSqlCommandString;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.sql.ISGJdbc;
import com.sellgirl.sgJavaHelper.sql.ISqlExecute;
import com.sellgirl.sgJavaHelper.sql.PFSqlInsertCollection;
import com.sellgirl.sgJavaHelper.sql.PFSqlUpdateCollection;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlWhereCollection;

@Service
public class UserService 
//implements IUserService 
{
//
//	public boolean addUser(UserCreate model) {
//		ISGJdbc dstJdbc=JdbcHelper.GetShop();
//		try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
//			myResource.GetConn().setAutoCommit(false);
//			myResource.SetInsertOption(a->a.setProcessBatch(50000));
//			int insertCnt=1;
//			int[] idx= new int[]{0};
//			boolean b =
//					myResource.doInsertList(
//							Arrays.asList(new String[]{"user_name","email","invitation_code","create_date"}),
//							"sg_user",
//							(a, b2, c) -> a < insertCnt,
//							(a) -> {
//								Map<String,Object> map=new HashMap<>();
//								map.put("user_name", "a1");
//								map.put("email", "a1@xx.com");
//								map.put("invitation_code", "aa01");
//								map.put("create_date", SGDate.Now());
//								idx[0]++;
//								return map;
//							},
//							null,
//							a -> {
////								// 测试速度
////								if(null==speed[0]){
////									speed[0]=new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
////									startCnt[0]=a;
////								}
////								System.out.println(speed[0].getSpeed(a-startCnt[0],com.sellgirl.sgJavaHelper.SGDate.Now()));
////								System.out.println("ProcessBatch:"+myResource.GetInsertOption().getProcessBatch());
//							},
//							null);
//			return b;
//		} catch (Throwable e) {
//			throw new RuntimeException(e);
//		}
//	}

	public boolean addUser(UserCreate model,SGRef<String> error) {
		ISGJdbc dstJdbc=JdbcHelper.GetShop();
		try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
			myResource.GetConn().setAutoCommit(false);
			myResource.SetInsertOption(a->a.setProcessBatch(50000));
			int insertCnt=1;
			int[] idx= new int[]{0};
			boolean b =
					myResource.doInsertList(
							Arrays.asList(new String[]{
									"user_name",
									"pwd",
									//"email",
									"invitation_code","create_date"}),
							"sg_user",
							(a, b2, c) -> a < insertCnt,
							(a) -> {
								Map<String,Object> map=new HashMap<>();
								map.put("user_name", model.getUserName());
								map.put("pwd", model.getPwd());
//								map.put("email", "");
								map.put("invitation_code", model.getInvitationCode());
								map.put("create_date", SGDate.Now());
								idx[0]++;
								return map;
							},
							null,
							a -> {
//								// 测试速度
//								if(null==speed[0]){
//									speed[0]=new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
//									startCnt[0]=a;
//								}
//								System.out.println(speed[0].getSpeed(a-startCnt[0],com.sellgirl.sgJavaHelper.SGDate.Now()));
//								System.out.println("ProcessBatch:"+myResource.GetInsertOption().getProcessBatch());
							},
							null);
			String aa=myResource.GetErrorFullMessage();
			error.SetValue(aa);
			return b;
		} catch (Throwable e) {
			error.SetValue(e.toString());
			return false;
			//throw new RuntimeException(e);
		}
	}

	public User getUser(String userName) {
		ISGJdbc dstJdbc=JdbcHelper.GetShop();
		try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
			SGSqlWhereCollection query =myResource.getWhereCollection();
			query.setIgnoreNullValue(false);
            query.Add("user_name",userName);
            String SqlString = SGDataHelper.FormatString( 
            		"select * from sg_user " +
            		"{0} " 
            		, 
            		        query.ToSql()
            		    );
            SGDataTable dt= myResource.GetDataTable(SqlString,null);
            if(null!=dt&&!dt.IsEmpty()) {
            	List<User> list= dt.ToList(User.class, (obj,row,c)->{
            		obj.setUserName(row.getStringColumn("user_name"));
            		obj.setInvitationCode(row.getStringColumn("invitation_code"));
            		obj.setSignDay(SGDataHelper.ObjectToInt(row.getColumn("sign_day")));
            		obj.setLastSign(SGDataHelper.ObjectToSGDate(row.getColumn("last_sign")));
            	});
//            	User user=list.get(0);
            	return list.get(0);
            }else {
            	return null;
            }
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 每日签到
	 * @param userName
	 * @return
	 */
	public boolean signDay(String userName) {
		User user=getUser(userName);
		if(null==user) {return false;}
		ISGJdbc dstJdbc=JdbcHelper.GetShop();
		try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
			SGSqlWhereCollection query =myResource.getWhereCollection();
//			PFSqlInsertCollection insert =myResource.getInsertCollection();
//			PFSqlUpdateCollection update =myResource.getUpdateCollection(myResource.GetMetaData(userName, null));
//			insert.setIgnoreNullValue(false);
			query.Add("user_name",userName);
            String SqlString = SGDataHelper.FormatString( 
            		"UPDATE sg_user SET sign_day = sign_day + 1, last_sign='{1}' " +
            		"{0} " 
            		, 
            		        query.ToSql(),
            		        SGDate.Now().toString()
            		    );
            boolean r= myResource.ExecuteSql(new SGSqlCommandString(SqlString));
            return r;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
}
