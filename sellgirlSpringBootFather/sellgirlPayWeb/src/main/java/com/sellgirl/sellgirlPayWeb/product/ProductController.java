package com.sellgirl.sellgirlPayWeb.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
//import com.perfect99.right.amsweb.ActionReturnInfo;
//import com.perfect99.right.amsweb.BaseReturnInfo;

import com.sellgirl.sgJavaHelper.AbstractApiResult;
import com.sellgirl.sgJavaHelper.SGAllowAnonymous;
import com.sellgirl.sgJavaSpringHelper.PFCaching;
import com.sellgirl.sgJavaHelper.PFDataRow;
import com.sellgirl.sgJavaHelper.SGDataTable;
//import com.sellgirl.sgJavaSpringHelper.PFJsonData;
//import com.sellgirl.sgJavaSpringHelper.PFJsonDataT;
import com.sellgirl.sgJavaHelper.PFJsonData;
import com.sellgirl.sgJavaHelper.PFJsonDataT;
import com.sellgirl.sgJavaSpringHelper.PFObject;
//import com.sellgirl.sgJavaSpringHelper.PFObject;
import com.sellgirl.sgJavaHelper.SGRef;
import com.sellgirl.sgJavaSpringHelper.config.SGDataHelper;
import com.sellgirl.sgJavaHelper.model.SystemUser;
import com.sellgirl.sgJavaHelper.model.UserOrg;
import com.sellgirl.sgJavaHelper.model.UserTypeClass;
import com.sellgirl.sgJavaMvcHelper.PFBaseWebController;
//import pf.springBoot.springBootSSO.controller.shares.YJQueryController;
import com.sellgirl.sellgirlPayWeb.oAuth.FormsAuth;
import com.sellgirl.sellgirlPayWeb.oAuth.LoginerBase;
//import com.sellgirl.sellgirlPayWeb.oAuth.MetabaseDataHelper;
//import pf.springBoot.springBootSSO.oAuth.MetabaseFormsAuth;
//import com.sellgirl.sellgirlPayWeb.oAuth.MetabaseUser;
import com.sellgirl.sellgirlPayWeb.oAuth.model.*;
import com.sellgirl.sellgirlPayWeb.product.model.book;
import com.sellgirl.sellgirlPayWeb.product.model.bookChap;
import com.sellgirl.sellgirlPayWeb.product.model.bookChapQuery;
import com.sellgirl.sellgirlPayWeb.product.model.bookQuery;
import com.sellgirl.sellgirlPayWeb.product.service.BookService;
import com.sellgirl.sellgirlPayWeb.projHelper.DES_IV;
//import com.sellgirl.sellgirlPayWeb.service.BalanceService;
import com.sellgirl.sellgirlPayWeb.user.YJQueryController;

//@RestController
@Controller
//@RequestMapping("/User")
public class ProductController 
//extends  YJQueryController
extends PFBaseWebController
{
	@Autowired private BookService bookService;

/*
 * 所有系统的单点登陆控制，如果用户已在本系统登陆，直接返回token
 */
//    @PFAllowAnonymous
//  	@GetMapping(value = "/")
	@GetMapping(value = { "/","/index.html" })
//	@GetMapping(value = { "/Product/index" })
    public ModelAndView Home()
    {
		bookQuery q=new bookQuery();
		q.setLetter("A");
		List<book> book=bookService.GetbookList(q);
		ViewData.put("book", book);
  	  return View(new LoginerBase(),"Product/index");
    }

	@GetMapping(value = { "/author.html" })
//	@GetMapping(value = { "/Product/index" })
    public ModelAndView Author(String name)
    {
		bookQuery q=new bookQuery();
		q.setBook_author(name);
		List<book> book=bookService.GetbookList(q);
		ViewData.put("book", book);
  	  return View(new LoginerBase(),"Product/author");
    }
//	@GetMapping(value = { "/Product/detail?id={id}" })
//	@GetMapping(value = { "/Product/detail" })
//    public ModelAndView Detail(@PathVariable(name = "id")String id)
//    {
//  	  return View(new LoginerBase(),"Product/detail");
//    }
//	@GetMapping(value = { "/detail.html" })
//    public ModelAndView Detail(int id)
//    {
//		book book=bookService.GetOneBook(id);
//  	  return View(book,"Product/detail");
//    }
	@GetMapping(value = { "/search.html" })
    public ModelAndView Search(String q)
    {
  	  return View(new LoginerBase(),"Product/search");
    }
	@GetMapping(value = { "/board.html" })
    public ModelAndView Board(String board)
    {
  	  return View(new LoginerBase(),"Product/board");
    }

	@GetMapping(value = { "/ebook-search.html" })
    public ModelAndView BookSearch(String q)
    {
		List<book> book=bookService.GetbookListByName(q);
  	  return View(book,"Product/ebook-search");
    }
	@GetMapping(value = { "/book.html" })
    public ModelAndView Book(int id)
    {
		book book=bookService.GetOneBook(id);
		bookChapQuery q=new bookChapQuery();
		q.setBook_id(id);
		List<bookChap> chap=bookService.GetbookChapList(q);
		ViewData.put("chap", chap);		
  	  return View(book,"Product/book");
    }

	@GetMapping(value = { "/chapter.html" })
    public ModelAndView Chapter(int book,int chapter)
    {
		book bookO=bookService.GetOneBook(book);
		bookChap chap=bookService.GetOnebookChap(chapter);
		ViewData.put("chap", chap);
		ViewData.put("prevChapter", bookService.GetbookChapName(book, chapter-1));
		ViewData.put("nextChapter", bookService.GetbookChapName(book, chapter+1));
  	  return View(bookO,"Product/chapter");
    }
}