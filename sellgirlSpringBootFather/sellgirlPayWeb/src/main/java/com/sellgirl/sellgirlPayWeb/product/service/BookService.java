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
import com.sellgirl.sellgirlPayWeb.product.model.book;
import com.sellgirl.sellgirlPayWeb.product.model.bookChap;
import com.sellgirl.sellgirlPayWeb.product.model.bookChapQuery;
import com.sellgirl.sellgirlPayWeb.product.model.bookQuery;
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
import com.sellgirl.sgJavaHelper.sql.SGSqlUpdateCollection;
import com.sellgirl.sgJavaHelper.sql.SGSqlExecute;
import com.sellgirl.sgJavaHelper.sql.SGSqlWhereCollection;

@Service
public class BookService
{
	private final String TAG="BookService";
//	/**
//	 * @deprecated service内部不能有任何"可变的成员变量(如字段)
//	 */
//	@Deprecated
//	private String SqlString;
    /**
    * 查询书
    */
    public SGDataTable Getbook(bookQuery q)
    {

		ISGJdbc jdbc=JdbcHelper.GetShop();
		try (ISqlExecute sql = SGSqlExecute.Init(jdbc)) {
			SGSqlWhereCollection query =sql.getWhereCollection();   
	        
            query.Add("book_name",q.getBook_name() );
            query.Add("book_author",q.getBook_author());
            query.Add("letter",q.getLetter() );
        
//if("X".equals(q.getLetter())) {
//	String aa="aa";
//}

            String SqlString = SGDataHelper.FormatString( 
"select * from sg_book " +
"{0} " 
, 
query.ToSql()
);
		    return sql.GetDataTable(SqlString,null);
		} catch (Throwable e) {
		    SGDataHelper.getLog().writeException(e, TAG);
		    return null;
		}
    }

    public SGDataTable GetBookById(int id)
    {
		ISGJdbc jdbc=JdbcHelper.GetShop();
		try (ISqlExecute sql = SGSqlExecute.Init(jdbc)) {
		    return sql.GetOneRow("sg_book",a->a.Add("book_id", id));
		} catch (Throwable e) {
		    SGDataHelper.getLog().writeException(e, TAG);
		    return null;
		}
    }

    public SGDataTable GetbookByName(String name)
    {

		ISGJdbc jdbc=JdbcHelper.GetShop();
		try (ISqlExecute sql = SGSqlExecute.Init(jdbc)) {        
//if("X".equals(q.getLetter())) {
//	String aa="aa";
//}

			String SqlString = SGDataHelper.FormatString( 
"select * from sg_book "+
"where book_name like '%{0}%' or book_author like '%{0}%' limit 20"
, name
);
		    return sql.GetDataTable(SqlString,null);
		} catch (Throwable e) {
		    SGDataHelper.getLog().writeException(e, TAG);
		    return null;
		}
    }
  //-------------------------------------------Service bookService
//  	    private IbookDAL dal=new bookDAL();
//          /// <summary>
//          /// 查询书
//          /// </summary>
//          public PFDataTable Getbook(bookQuery q)
//          {
//              return dal.Getbook(q);
//          }
    /**
     * 查询书
     */
          public List<book> GetbookList(bookQuery q)
          {
              List<book> list = new ArrayList<book>();
              SGDataTable result =Getbook(q);
              if (result != null && !result.IsEmpty())
              {
                  list = bookTableToList(result);
              }
              return list;
          }

          /**
           * 查询一条书数据
           */
          public book GetOneBook(int id)
          {
              book model = null;
              SGDataTable result =GetBookById(id);
              if (result != null && !result.IsEmpty())
              {
                  model = bookTableToList(result).get(0);
              }
              return model;
          }
          private List<book> bookTableToList(SGDataTable result ){
              List<book> list = new ArrayList<book>();
              if (result != null && !result.IsEmpty())
              {
                  list = result.ToList(book.class,(a,b,c)->{
                	  if(null!=a.getCover()&&"id".equals(a.getCover())) {
                		  a.setCover("bookImg/cover/"+a.book_id+".jpg");
                	  }
                  });
              }
              return list;
          }
          public List<book> GetbookListByName(String name)
          {
//              List<book> list = new ArrayList<book>();
              SGDataTable result =GetbookByName(name);
//              if (result != null && !result.IsEmpty())
//              {
//                  list = result.ToList(book.class,(a,b,c)->{
//                	  if(null!=a.getCover()&&"id".equals(a.getCover())) {
//                		  a.setCover("bookImg/cover/"+a.book_id+".jpg");
//                	  }
//                  });
//              }
              return bookTableToList(result);
          }
          //----------------------------章节---------------------
          /**
          * 查询章节
          */
          public SGDataTable GetbookChap(bookChapQuery q,boolean content)
          {
              
  		    ISGJdbc jdbc=JdbcHelper.GetShop();
  		    try (ISqlExecute sql = SGSqlExecute.Init(jdbc)) {
      
  			SGSqlWhereCollection query =sql.getWhereCollection();  
  			query.Add("book_id", q.getBook_id());

  			String SqlString = SGDataHelper.FormatString( 
  "select {1} from sg_book_chap " +
  "{0} " 
  , 
              query.ToSql(),
              content?"*":"book_chap_id,book_chap_name, book_id"
          );
  		        return sql.GetDataTable(SqlString,null);
  		    } catch (Throwable e) {
  		        SGDataHelper.getLog().writeException(e, TAG);
  		        return null;
  		    }
          }

      public SGDataTable GetbookChapById(int id)
      {
  		ISGJdbc jdbc=JdbcHelper.GetShop();
  		try (ISqlExecute sql = SGSqlExecute.Init(jdbc)) {
		    return sql.GetOneRow("sg_book_chap",a->a.Add("book_chap_id", id));
  		} catch (Throwable e) {
  		    SGDataHelper.getLog().writeException(e, TAG);
  		    return null;
  		}
      }  

      /**
       * 获得上一章或下一章的名称
       * @param q
       * @return
       */
      public LinkedHashMap<String,Object> GetbookChapName(int book,int chapter)
      {
          
		    ISGJdbc jdbc=JdbcHelper.GetShop();
		    try (ISqlExecute sql = SGSqlExecute.Init(jdbc)) {
  
			SGSqlWhereCollection query =sql.getWhereCollection();  
			query.Add("book_id", book);  
			query.Add("book_chap_id", chapter);

			String SqlString = SGDataHelper.FormatString( 
"select book_chap_id,book_chap_name from sg_book_chap " +
"{0} " 
, 
          query.ToSql()
      );
          SGDataTable dt=sql.GetDataTable(SqlString,null);
          if(null!=dt&&!dt.IsEmpty()) {
        	  return dt.ToDictList().get(0);
          }else {
        	  return null;
          }
		    } catch (Throwable e) {
		        SGDataHelper.getLog().writeException(e, TAG);
		        return null;
		    }
      }
      
      /**
       * 查询章节
       */
      public List<bookChap> GetbookChapList(bookChapQuery q,boolean content)
      {
          List<bookChap> list = new ArrayList<bookChap>();
          SGDataTable result = GetbookChap(q, content);
          if (result != null && !result.IsEmpty())
          {
              list = result.ToList(bookChap.class,null);
          }
          return list;
      }

      /**
       * 查询一条章节数据
       */
      public bookChap GetOnebookChap(int id)
      {
          bookChap model = null;
          SGDataTable dt = GetbookChapById(id);
          if (dt != null && !dt.IsEmpty())
          {
              model = dt.ToList(bookChap.class,null).get(0);
          }
          return model;
      }
}
