package com.sellgirl.sellgirlPayWeb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.sellgirl.sellgirlPayWeb.model.GamePadSetting;
//import com.sellgirl.sellgirlPayWeb.model.HomeGame;
import com.sellgirl.sellgirlPayWeb.model.IGamePadSetting;
import com.sellgirl.sellgirlPayWeb.model.ISwitchPadSetting;
import com.sellgirl.sellgirlPayWeb.model.IXboxPadSetting;
import com.sellgirl.sgJavaMvcHelper.PFBaseWebController;

@Controller
public class GamePadController  extends PFBaseWebController{
    
//	@GetMapping(value = { "/GamePad/{gameName}" })
//	public ModelAndView Story(@PathVariable(name = "gameName")HomeGame gameName) {
//		//GamePadSetting m=GamePadSetting.GetGame(gameName);
//		IGamePadSetting m=GamePadSetting.GetIGame(gameName,ISwitchPadSetting.class);//aabb
//        return View(m,"GamePad/Index");
//	}
	@GetMapping(value = { "/ISwitchPadSetting/{gameName}" })
	public ModelAndView Story(@PathVariable(name = "gameName")String gameName) {
		//GamePadSetting m=GamePadSetting.GetGame(gameName);
		IGamePadSetting m=GamePadSetting.GetIGame(gameName,ISwitchPadSetting.class);//aabb
        return View(m,"GamePad/Index");
	}
	@GetMapping(value = { "/IXboxPadSetting/{gameName}" })
	public ModelAndView XboxPad(@PathVariable(name = "gameName")String gameName) {
		//GamePadSetting m=GamePadSetting.GetGame(gameName);
		IGamePadSetting m=GamePadSetting.GetIGame(gameName,IXboxPadSetting.class);//aabb
        return View(m,"GamePad/Xbox");
	}
}
