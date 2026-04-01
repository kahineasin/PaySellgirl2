package com.sellgirl.sellgirlPayWeb.user.service;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.sellgirl.sellgirlPayDao.TestDAO;
import com.sellgirl.sellgirlPayWeb.configuration.jdbc.JdbcHelper;
import com.sellgirl.sellgirlPayWeb.user.model.User;
import com.sellgirl.sellgirlPayWeb.user.model.UserCreate;
import com.sellgirl.sellgirlPayWeb.user.model.UserQuery;
import com.sellgirl.sgJavaHelper.SGDataTable;
import com.sellgirl.sgJavaHelper.PFDataRow;
import com.sellgirl.sgJavaHelper.PFMySqlWhereCollection;
import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaHelper.SGSpeedCounter;
import com.sellgirl.sgJavaHelper.SGSqlCommandString;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.sql.ISGJdbc;
import com.sellgirl.sgJavaHelper.sql.ISqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlInsertCollection;
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
									"email",
									"invitation_code","create_date"}),
							"sg_user",
							(a, b2, c) -> a < insertCnt,
							(a) -> {
								Map<String,Object> map=new HashMap<>();
								map.put("user_name", model.getUserName());
								map.put("pwd", model.getPwd());
								map.put("email", model.getEmail());
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
	
	

    public boolean updateUserVip(long userId,
    		Boolean vip1,SGDate vip1_expire,
    		Boolean vip2,SGDate vip2_expire)
    {
        ISGJdbc jdbc=JdbcHelper.GetShop();
        try (ISqlExecute dstExec = SGSqlExecute.Init(jdbc)) {
            dstExec.AutoCloseConn(false);
            List<String> srcFieldNames = new ArrayList<String>();
            srcFieldNames.add("user_id");
            srcFieldNames.add("vip1");
            srcFieldNames.add("vip1_expire");
            srcFieldNames.add("vip2");
            srcFieldNames.add("vip2_expire");
            
            String tableName="sg_user";
            ResultSetMetaData dstMd = dstExec.GetMetaDataNotClose(tableName, srcFieldNames);

            PFSqlUpdateCollection update = dstExec.getUpdateCollection(dstMd);
            update.Set("user_id", userId);
            update.Set("vip1", vip1);
            update.Set("vip1_expire", vip1_expire);
            update.Set("vip2", vip2);
            update.Set("vip2_expire", vip2_expire);
            
//            String[] mArray = new String[] {"status"};
            String[] primaryKeys = new String[] {"user_id"};
//            update.UpdateFields(mArray);
            update.PrimaryKeyFields(true, primaryKeys);

//            update.Set("vip_order_id", id);
//            update.Set("status", com.sellgirl.sellgirlPayWeb.pay.model.OrderStatus.已支付.ordinal());
            
            
            SGSqlCommandString sql=new SGSqlCommandString(
                    SGDataHelper.FormatString(
                            " update {2} set  {0} {1} limit 1",
                            update.ToSetSql(),
                            update.ToWhereSql(),
                            tableName
                    ));
            dstExec.close();
            int r=dstExec.ExecuteSqlInt(sql, null, true);
            dstExec.close();
            if(0<r) {
                return true;
            }
            //System.out.println("id2:"+dstExec.GetLastInsertedId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addUserPoint(long userId,int point)
    {
    	if(0==point) {return false;}
        ISGJdbc jdbc=JdbcHelper.GetShop();
        try (ISqlExecute dstExec = SGSqlExecute.Init(jdbc)) {
            dstExec.AutoCloseConn(false);
            List<String> srcFieldNames = new ArrayList<String>();
            srcFieldNames.add("user_id");
//            srcFieldNames.add("vip1");
//            srcFieldNames.add("vip1_expire");
//            srcFieldNames.add("vip2");
//            srcFieldNames.add("vip2_expire");
            
            String tableName="sg_user";
            ResultSetMetaData dstMd = dstExec.GetMetaDataNotClose(tableName, srcFieldNames);

            PFSqlUpdateCollection update = dstExec.getUpdateCollection(dstMd);
            update.Set("user_id", userId);
//            update.Set("vip1", vip1);
//            update.Set("vip1_expire", vip1_expire);
//            update.Set("vip2", vip2);
//            update.Set("vip2_expire", vip2_expire);
            
//            String[] mArray = new String[] {"status"};
            String[] primaryKeys = new String[] {"user_id"};
//            update.UpdateFields(mArray);
            update.PrimaryKeyFields(true, primaryKeys);

//            update.Set("vip_order_id", id);
//            update.Set("status", com.sellgirl.sellgirlPayWeb.pay.model.OrderStatus.已支付.ordinal());
            
            
            SGSqlCommandString sql=new SGSqlCommandString(
                    SGDataHelper.FormatString(
                            " update {2} set  point=point{0} {1} limit 1",
                            0<point?("+"+point):point,
                            update.ToWhereSql(),
                            tableName
                    ));
            dstExec.close();
            int r=dstExec.ExecuteSqlInt(sql, null, true);
            dstExec.close();
            if(0<r) {
                return true;
            }
            //System.out.println("id2:"+dstExec.GetLastInsertedId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
	public User getUser(UserQuery q) {
		if(null==q.getUserId()&&null==q.getUserName()) {
			return null;
		}
		ISGJdbc dstJdbc=JdbcHelper.GetShop();
		try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
			SGSqlWhereCollection query =myResource.getWhereCollection();
			//query.setIgnoreNullValue(false);
            query.Add("user_id",q.getUserId());
            query.Add("user_name",q.getUserName());
            String SqlString = SGDataHelper.FormatString( 
            		"select * from sg_user " +
            		"{0} " 
            		, 
            		        query.ToSql()
            		    );
            SGDataTable dt= myResource.GetDataTable(SqlString,null);
            if(null!=dt&&!dt.IsEmpty()) {
            	List<User> list= dt.ToList(User.class, (obj,row,c)->{
//            		obj.setUserId(SGDataHelper.ObjectToLong0(row.getColumn("user_id")) );
//            		obj.setUserName(row.getStringColumn("user_name"));
//            		obj.setInvitationCode(row.getStringColumn("invitation_code"));
//            		obj.setSignDay(SGDataHelper.ObjectToInt(row.getColumn("sign_day")));
//            		obj.setLastSign(SGDataHelper.ObjectToSGDate(row.getColumn("last_sign")));

            		mapRowToUser(row,obj);
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

	private void mapRowToUser(PFDataRow row,User obj) {

		obj.setUserId(SGDataHelper.ObjectToLong0(row.getColumn("user_id")) );
		obj.setUserName(row.getStringColumn("user_name"));
		obj.setInvitationCode(row.getStringColumn("invitation_code"));
		obj.setSignDay(SGDataHelper.ObjectToInt(row.getColumn("sign_day")));
		obj.setLastSign(SGDataHelper.ObjectToSGDate(row.getColumn("last_sign")));
	}
	public ArrayList<LinkedHashMap<String,Object>> getPWD(String email) {
		ISGJdbc dstJdbc=JdbcHelper.GetShop();
		try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
			SGSqlWhereCollection query =myResource.getWhereCollection();
			query.setIgnoreNullValue(false);
            query.Add("email",email);
            String SqlString = SGDataHelper.FormatString( 
            		"select pwd,user_name from sg_user  " +
            		"{0} " 
            		, 
            		        query.ToSql()
            		    );
            SGDataTable dt= myResource.GetDataTable(SqlString,null);
            if(null!=dt&&!dt.IsEmpty()) {
            	return dt.ToDictList();
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
	 * @param signDay 小于0时原子加1, 否则直接update
	 * @return
	 */
	public boolean signDay(String userName
//			,SGRef<Integer> days
			,SGRef<User> user,
//			boolean rebegin,
			int signDay
			) {
//		User user=getUser(userName);
//		if(null==user) {return false;}
		ISGJdbc dstJdbc=JdbcHelper.GetShop();
		try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
			myResource.AutoCloseConn(false);
			SGSqlWhereCollection query =myResource.getWhereCollection();
//			PFSqlInsertCollection insert =myResource.getInsertCollection();
//			PFSqlUpdateCollection update =myResource.getUpdateCollection(myResource.GetMetaData(userName, null));
//			insert.setIgnoreNullValue(false);
			query.Add("user_name",userName);
            String SqlString =SGDataHelper.FormatString( 
            		"UPDATE sg_user SET sign_day = {2}, last_sign='{1}' " +
            		"{0} " 
            		, 
            		        query.ToSql(),
            		        SGDate.Now().toString(),
            		        -1<signDay?signDay:"sign_day + 1"
            		    );
            int r= myResource.ExecuteSqlInt(new SGSqlCommandString(SqlString), null, false);
            if(0<r) {            	
//            	days.SetValue(user.getSignDay()+1);
            	UserQuery q=new UserQuery();
            	q.setUserName(userName);
            	user.SetValue(getUser(q));
            	myResource.close();
            	return true;
            }else {
            	myResource.close();
                return false;
            }
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

    public boolean updateUserInvite(long userId,
    		String inviteCode)
    {
        ISGJdbc jdbc=JdbcHelper.GetShop();
        try (ISqlExecute dstExec = SGSqlExecute.Init(jdbc)) {
            dstExec.AutoCloseConn(false);
            List<String> srcFieldNames = new ArrayList<String>();
            srcFieldNames.add("user_id");
            srcFieldNames.add("invitation_code");
            
            String tableName="sg_user";
            ResultSetMetaData dstMd = dstExec.GetMetaDataNotClose(tableName, srcFieldNames);

            PFSqlUpdateCollection update = dstExec.getUpdateCollection(dstMd);
            update.Set("user_id", userId);
            update.Set("invitation_code", inviteCode);
            
            String[] primaryKeys = new String[] {"user_id"};
            update.PrimaryKeyFields(true, primaryKeys);

       
            
            SGSqlCommandString sql=new SGSqlCommandString(
                    SGDataHelper.FormatString(
                            " update {2} set  {0} {1} limit 1",
                            update.ToSetSql(),
                            update.ToWhereSql(),
                            tableName
                    ));
            dstExec.close();
            int r=dstExec.ExecuteSqlInt(sql, null, true);
            dstExec.close();
            if(0<r) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
