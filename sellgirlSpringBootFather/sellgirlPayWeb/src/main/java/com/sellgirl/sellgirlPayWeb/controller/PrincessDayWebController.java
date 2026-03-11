package com.sellgirl.sellgirlPayWeb.controller;

import org.springframework.stereotype.Controller;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.perfect.demo.interceptor.PFDayUrl;


@Controller
@RequestMapping("/{asyncDay:[0-9]{8}}")
@PFDayUrl("^/([0-9]{8})/")
//@RequestMapping("/"+PrincessDayController.GetDay())
public class PrincessDayWebController extends PrincessWebController {

//	public static String GetDay() {
//		PFDate now=PFDate.Now();
//		System.out.println(now);
//		return now.toString();
//	}
}
