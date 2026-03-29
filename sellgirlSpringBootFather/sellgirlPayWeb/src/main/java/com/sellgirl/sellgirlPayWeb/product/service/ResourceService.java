package com.sellgirl.sellgirlPayWeb.product.service;

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
import com.sellgirl.sellgirlPayWeb.product.model.ProductType;
import com.sellgirl.sellgirlPayWeb.product.model.ResourceType;
import com.sellgirl.sellgirlPayWeb.product.model.resource;
//import com.sellgirl.sellgirlPayWeb.product.model.resourceChap;
//import com.sellgirl.sellgirlPayWeb.product.model.resourceChapQuery;
import com.sellgirl.sellgirlPayWeb.product.model.resourceQuery;
import com.sellgirl.sellgirlPayWeb.product.model.userBuyCreate;
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
import com.sellgirl.sgJavaHelper.sql.SGSqlInsertCollection;
import com.sellgirl.sgJavaHelper.sql.PFSqlUpdateCollection;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlWhereCollection;
import com.sellgirl.sgJavaMvcHelper.MvcPagingParameters;

@Service
public class ResourceService
{
	private final String TAG="ResourceService";
	private String SqlString;
	private String tableName="sg_resource";
	public void setResourceType(ResourceType resourceType) {
		switch(resourceType) {
		case movie:
			tableName="sg_resource";
			return;
		case image:
			tableName="sg_img";
			return;
		case comic:
			tableName="sg_comic";
			return;
		default:
			tableName="sg_resource";
			return;
		}
	}
	public void setResourceType2(ProductType resourceType) {
		switch(resourceType) {
		case movie:
			tableName="sg_resource";
			return;
		case image:
			tableName="sg_img";
			return;
		case comic:
			tableName="sg_comic";
			return;
		default:
			tableName="sg_resource";
			return;
		}
	}
	public String getTableName() {
		return tableName;
	}
    /**
    * 查询书
    */
    public SGDataTable Getresource(resourceQuery q,MvcPagingParameters p)
    {

		ISGJdbc jdbc=JdbcHelper.GetShop();
		try (ISqlExecute sql = SGSqlExecute.Init(jdbc)) {
			SGSqlWhereCollection query =sql.getWhereCollection();   
	        
//            query.Add("letter",q.getLetter() );
        
//if("X".equals(q.getLetter())) {
//	String aa="aa";
//}

            query.Add("resource_name",q.getResource_name() );
            query.Add("resource_author",q.getResource_author());         
            if(null!=p.getPageSize()) {
SqlString = SGDataHelper.FormatString( 
"select * from {1} t " +
"INNER JOIN("+
"select resource_id from {1} {0} order by {2} limit {3},{4}"+
") tmp ON t.resource_id=tmp.resource_id"
, 
query.ToSql(),
tableName,
p.getSort(),p.getPageStart(),p.getPageSize()
);
            }else {
SqlString = SGDataHelper.FormatString( 
"select * from {1} " +
"{0} " 
, 
query.ToSql(),
tableName
);
            }
		    return sql.GetDataTable(SqlString,null);
		} catch (Throwable e) {
		    SGDataHelper.getLog().writeException(e, TAG);
		    return null;
		}
    }

    public SGDataTable GetResourceById(long id)
    {
		ISGJdbc jdbc=JdbcHelper.GetShop();
		try (ISqlExecute sql = SGSqlExecute.Init(jdbc)) {
		    return sql.GetOneRow(tableName,a->a.Add("resource_id", id));
		} catch (Throwable e) {
		    SGDataHelper.getLog().writeException(e, TAG);
		    return null;
		}
    }

    public SGDataTable GetresourceByName(String name,MvcPagingParameters p)
    {

		ISGJdbc jdbc=JdbcHelper.GetShop();
		try (ISqlExecute sql = SGSqlExecute.Init(jdbc)) {        
//if("X".equals(q.getLetter())) {
//	String aa="aa";
//}

            if(null!=p.getPageSize()) {

SqlString = SGDataHelper.FormatString( 
"select * from {1} t " +
"INNER JOIN("+
"select resource_id from {1} where resource_name like '%{0}%' or resource_author like '%{0}%' order by {2} limit {3},{4}"+
") tmp ON t.resource_id=tmp.resource_id"
, 
name,
tableName,
p.getSort(),p.getPageStart(),p.getPageSize()
);
            }else {
SqlString = SGDataHelper.FormatString( 
"select * from {1} "+
"where resource_name like '%{0}%' or resource_author like '%{0}%' limit 20"
, name,tableName
);
            }
		    return sql.GetDataTable(SqlString,null);
		} catch (Throwable e) {
		    SGDataHelper.getLog().writeException(e, TAG);
		    return null;
		}
    }
    
    public boolean unlockResource(userBuyCreate m)
    {

		ISGJdbc jdbc=JdbcHelper.GetShop();
		try (ISqlExecute dstExec = SGSqlExecute.Init(jdbc)) { 
			SGSqlInsertCollection insert=dstExec.getInsertCollection();			
        insert.InitItemByModel(m);
        SGSqlCommandString sql=new SGSqlCommandString(
                SGDataHelper.FormatString(
"insert into sg_user_buy ({0}) values ({1})",insert.ToKeysSql(),insert.ToValuesSql())
                );
        int r=dstExec.ExecuteSqlInt(sql, null, true);
        	return -1<r;
		} catch (Throwable e) {
		    SGDataHelper.getLog().writeException(e, TAG);
//		    return null;
		}
		return false;
    }
    
    public boolean isResourceUnlocked(long userId,long resourceId,ProductType resourceType)
    {

		ISGJdbc jdbc=JdbcHelper.GetShop();
		try (ISqlExecute dstExec = SGSqlExecute.Init(jdbc)) {
			SGSqlWhereCollection query =dstExec.getWhereCollection();   
			query.setIgnoreNullValue(false);
          query.Add("user_id",userId);
          query.Add("source_id",resourceId); 
          query.Add("source_type",resourceType); 

SqlString = SGDataHelper.FormatString( 
"select user_id from sg_user_buy " +
"{0} " 
, 
query.ToSql()
);
        	return null!=dstExec.QuerySingleValue(SqlString);
		} catch (Throwable e) {
		    SGDataHelper.getLog().writeException(e, TAG);
//		    return null;
		}
		return false;
    }

    /**
     * 查询书
     */
          public List<resource> GetresourceList(resourceQuery q,MvcPagingParameters p)
          {
              List<resource> list = new ArrayList<resource>();
              SGDataTable result =Getresource(q,p);
              if (result != null && !result.IsEmpty())
              {
                  list = resourceTableToList(result);
              }
              return list;
          }

          /**
           * 查询一条书数据
           */
          public resource GetOneResource(long id)
          {
              resource model = null;
              SGDataTable result =GetResourceById(id);
              if (result != null && !result.IsEmpty())
              {
                  model = resourceTableToList(result).get(0);
              }
              return model;
          }
          private List<resource> resourceTableToList(SGDataTable result ){
              List<resource> list = new ArrayList<resource>();
              if (result != null && !result.IsEmpty())
              {
                  list = result.ToList(resource.class,(a,b,c)->{
                	  if(null!=a.getCover()&&"id".equals(a.getCover())) {
                		  a.setCover("resourceImg/cover/"+a.resource_id+".jpg");
                	  }
                  });
              }
              return list;
          }
          public List<resource> GetresourceListByName(String name,MvcPagingParameters p)
          {
//              List<resource> list = new ArrayList<resource>();
              SGDataTable result =GetresourceByName(name,p);
//              if (result != null && !result.IsEmpty())
//              {
//                  list = result.ToList(resource.class,(a,b,c)->{
//                	  if(null!=a.getCover()&&"id".equals(a.getCover())) {
//                		  a.setCover("resourceImg/cover/"+a.resource_id+".jpg");
//                	  }
//                  });
//              }
              return resourceTableToList(result);
          }
          //----------------------------章节---------------------
          /**
          * 查询章节
          */
//          public SGDataTable GetresourceChap(resourceChapQuery q,boolean content)
//          {
//              
//  		    ISGJdbc jdbc=JdbcHelper.GetShop();
//  		    try (ISqlExecute sql = SGSqlExecute.Init(jdbc)) {
//      
//  			SGSqlWhereCollection query =sql.getWhereCollection();  
//  			query.Add("resource_id", q.getResource_id());
//
//              SqlString = SGDataHelper.FormatString( 
//  "select {1} from sg_resource_chap " +
//  "{0} " 
//  , 
//              query.ToSql(),
//              content?"*":"resource_chap_id,resource_chap_name, resource_id"
//          );
//  		        return sql.GetDataTable(SqlString,null);
//  		    } catch (Throwable e) {
//  		        SGDataHelper.getLog().writeException(e, TAG);
//  		        return null;
//  		    }
//          }


}
