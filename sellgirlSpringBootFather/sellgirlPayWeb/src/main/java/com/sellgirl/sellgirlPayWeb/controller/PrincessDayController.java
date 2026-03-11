package com.sellgirl.sellgirlPayWeb.controller;

//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfect.demo.interceptor.PFDayUrl;

//import pf.java.pfHelper.PFDate;

//@RestController//("2015")
//@Controller("/2015")
//@RestController("2015")
//@Component("/2015")
//@RequestMapping("/2015")
@RestController
@RequestMapping("/{asyncDay:[0-9]{8}}")
@PFDayUrl("^/([0-9]{8})/")
//@RequestMapping("/"+PrincessDayController.GetDay())
public class PrincessDayController extends PrincessController {

//	public static String GetDay() {
//		PFDate now=PFDate.Now();
//		System.out.println(now);
//		return now.toString();
//	}
}
