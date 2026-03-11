package com.sellgirl.sellgirlPayWeb.apiController;


import com.alibaba.druid.support.json.JSONUtils;
//import com.perfect.demo.extend.collection.FastView;
import com.perfect.demo.model.ResultModel;
import com.sellgirl.sellgirlPayDomain.HyzlChange;
import com.sellgirl.sellgirlPayDomain.SalesDay;
//import com.sellgirl.sellgirlPayDomain.Test;
import com.sellgirl.sellgirlPayDomain.UsersModify;
import com.sellgirl.sellgirlPayWeb.service.DayService;
//import com.sellgirl.sellgirlPayWeb.service.Demo2Service;
import com.sellgirl.sellgirlPayWeb.service.DemoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/crm")
public class CrmController {

    @Autowired
    private DemoService demoService;
//    @Autowired
//    private Demo2Service demo2Service;
    @Autowired
    private DayService _dayService;

    @RequestMapping("/test1")
    public Object test(){
        return "success";

    }

    /*
    @RequestMapping("/test1")
    public Object test(){
        return new ModelAndView("index").addObject("userName", "zhangSan");

    }


    @RequestMapping(value = "/test2")
    public Object test2(){
        Test test = new Test();
        vo.setId(1L);
        return new FastView("index").add("userName", demoService.testDemo1(test).get(0).getV()).done();
    }

*/
    
//    @GetMapping(value = "/test3")
//    public Object test3(){
//        Test test = new Test();
//        //vo.setId(1L);
//        return new FastView("index").add("userName",demo2Service.testDemo2(test).get(0).getV()).done();
//    }

    @GetMapping(value = "/getusersmodify")
    public List<UsersModify> GetUsersModify(String userid){
    	UsersModify vo = new UsersModify();
        vo.setUserid(userid);
        List<UsersModify> list=demoService.getUsersModify(vo);
        if(list==null||list.isEmpty()) {
        	return null;
        }
        return list;
        /*HyzlChange test = new HyzlChange();
        vo.setid(3);
        return new FastView("index").add("userName",demoService.getHyzlChange(test).get(0).gethybh()).done();*/
    }
    
    @GetMapping(value = "/getsalesday")
    public List<SalesDay> GetSalesDay(String sfNo){
    	SalesDay vo = new SalesDay();
        vo.setSfNo(sfNo);
        Calendar c = Calendar.getInstance();    
 	   	c.add(Calendar.MONTH, -1);
        vo.setCDay(c.getTime());
        List<SalesDay> list=_dayService.getSalesDay(vo);
        if(list==null||list.isEmpty()) {
        	return null;
        }
        return list;
        /*HyzlChange test = new HyzlChange();
        vo.setid(3);
        return new FastView("index").add("userName",demoService.getHyzlChange(test).get(0).gethybh()).done();*/
    }

    @PostMapping(value = "/addhyzlchange")
    public ResultModel AddHyzlChange(@Valid @RequestBody HyzlChange hyzlChange, BindingResult bindingResult){
        ResultModel r=new ResultModel();
        if (bindingResult.hasErrors()) {
            r.success=false;
            r.message= JSONUtils.toJSONString(bindingResult.getAllErrors());
        }
        try{
            int c=demoService.addHyzlChange(hyzlChange);
            r.success=true;
            r.message="成功新增"+c+"条数据";
        }catch (Exception e){
            r.success=false;
            r.message=e.getMessage();
        }
        return r;
    }

}
