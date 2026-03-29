package com.sellgirl.sellgirlPayWeb.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
//import com.perfect99.right.amsweb.ActionReturnInfo;
//import com.perfect99.right.amsweb.BaseReturnInfo;

import com.sellgirl.sgJavaHelper.AbstractApiResult;
import com.sellgirl.sgJavaHelper.SGAllowAnonymous;
import com.sellgirl.sgJavaHelper.SGDate;
import com.sellgirl.sgJavaHelper.SGCaching;
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
import com.sellgirl.sgJavaMvcHelper.config.SGCookieUtils;
import com.sellgirl.sellgirlPayDao.DayDAO;
//import pf.springBoot.springBootSSO.controller.shares.YJQueryController;
import com.sellgirl.sellgirlPayWeb.oAuth.FormsAuth;
import com.sellgirl.sellgirlPayWeb.oAuth.LoginerBase;
//import com.sellgirl.sellgirlPayWeb.oAuth.MetabaseDataHelper;
//import pf.springBoot.springBootSSO.oAuth.MetabaseFormsAuth;
//import com.sellgirl.sellgirlPayWeb.oAuth.MetabaseUser;
import com.sellgirl.sellgirlPayWeb.oAuth.model.*;
import com.sellgirl.sellgirlPayWeb.product.model.book;
import com.sellgirl.sellgirlPayWeb.product.model.bookQuery;
import com.sellgirl.sellgirlPayWeb.product.service.BookService;
import com.sellgirl.sellgirlPayWeb.projHelper.DES_IV;
import com.sellgirl.sellgirlPayWeb.user.YJQueryController;
import com.sellgirl.sellgirlPayWeb.user.model.UserCreate;
//import com.sellgirl.sellgirlPayWeb.service.BalanceService;
import com.sellgirl.sellgirlPayWeb.user.service.UserService;

//@RestController
@RestController
//@RequestMapping("/User")
public class ProductApiController extends  YJQueryController
{
	@Autowired private BookService bookService;
	@PostMapping(value = { "/PostBook" })
    public AbstractApiResult<?> PostBook(String letter)
    {
		bookQuery q=new bookQuery();
		q.setLetter(letter);
		List<book> book=bookService.GetbookList(q);
		return AbstractApiResult.success(book);
    }


}