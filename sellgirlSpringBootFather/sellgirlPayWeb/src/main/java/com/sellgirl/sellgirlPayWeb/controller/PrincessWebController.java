package com.sellgirl.sellgirlPayWeb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.sellgirl.sellgirlPayWeb.model.GamePadAction;
import com.sellgirl.sellgirlPayWeb.model.GamePadCustomAction;
import com.sellgirl.sellgirlPayWeb.model.GamePadSetting;
import com.sellgirl.sellgirlPayWeb.model.IGamePadSetting;
import com.sellgirl.sellgirlPayWeb.model.ISwitchPadSetting;
import com.sellgirl.sellgirlPayWeb.model.IXboxPadSetting;
import com.sellgirl.sellgirlPayWeb.model.game.StreetFighter6Classic;
import com.sellgirl.sgJavaMvcHelper.PFBaseWebController;
import com.sellgirl.sgJavaSpringHelper.config.SGDataHelper;
import com.sellgirl.sgJavaSpringHelper.config.SGDataHelper.LocalDataType;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
@Controller
public class PrincessWebController  extends PFBaseWebController{
    /*
     * 参考:D:\eclipse_workspace_springBoot\springBootYjquery
     */
    @GetMapping("/pcdeskimgmanager")
    public ModelAndView PcDeskImgManager()
    {
		Map<String, Object> requiredMap = new LinkedHashMap<String, Object>();
		//String[] texts=PcDeskImgType.PrincessSasha.GetAllTexts();
		String[] texts=PrincessController.GetPcDeskImageTypeOrderedList();
		for( int i = 0 ; i < texts.length ; i++) {//内部不锁定，效率最高，但在多线程要考虑并发操作的问题。
			requiredMap.put(texts[i],texts[i]);
		}
//		requiredMap.put(PcDeskImgType.PrincessSasha.toString(),PcDeskImgType.PrincessSasha);
//		requiredMap.put(PcDeskImgType.Nilisz.toString(),PcDeskImgType.Nilisz);
//		requiredMap.put(PcDeskImgType.Normal.toString(),PcDeskImgType.Nilisz);
		
 	   ViewData.put("imgList",requiredMap);
        return View(PrincessController.currentPcDeskImg,"Sasha/PcDeskImgManager");
    }
    private  <T extends IGamePadSetting>  String getGameS(String s,Class<T> gameCls) {

		//旧版本,前端要这样写 {ZL_防御}
		String[] btnNames=GamePadSetting.AllBtnKey();
		for(GamePadAction i :GamePadAction.values()) {//如:防御
			for(String j:btnNames) {//如:ZL
				//s=s.replace("{"+j+"_"+i.toString()+"}",i.toString()+"^"+String.valueOf( GamePadSetting.GetActionOnBtnTime(i,(a,b,c)->GamePadSetting.GetBtnByText(a, j))));
//				List<String> games=GamePadSetting.GetActionOnBtnOfAllGame(i,(a,b,c)->GamePadSetting.GetBtnByText(a, j));
//				s=s.replace("{"+j+"_"+i.toString()+"}",PFDataHelper.FormatString("<a href=\"#\" onclick=\"alert('{2}')\" >{0}^{1}</a>", i,games.size(),String.join(",",games)) );
				List<T> games=GamePadSetting.GetActionOnBtnOfAllGame(gameCls,i,(a,b,c)->GamePadSetting.GetBtnByText(a, j));
				s=s.replace("{"+j+"_"+i.toString()+"}",SGDataHelper.FormatString("<a href=\"#\" onclick=\"alert('{2}')\" >{0}^{1}</a>", i,games.size(),String.join(",",SGDataHelper.ListSelect(games, a->a.getGameName()) )) );
			}
//			s=s.replace("{ZL_"+i.toString()+"}",i.toString()+"^"+String.valueOf( GamePadSetting.GetActionOnBtnTime(i,(a,b,c)->a.getZL())));
//			s=s.replace("{L_"+i.toString()+"}",i.toString()+"^"+String.valueOf( GamePadSetting.GetActionOnBtnTime(i,(a,b,c)->a.getL())));
//			s=s.replace("{B_"+i.toString()+"}",i.toString()+"^"+String.valueOf( GamePadSetting.GetActionOnBtnTime(i,(a,b,c)->a.getB())));
//			s=s.replace("{R_"+i.toString()+"}",i.toString()+"^"+String.valueOf( GamePadSetting.GetActionOnBtnTime(i,(a,b,c)->a.getR())));
		}
		
		//新版本,自动统计所有按钮
		int showTop=5;
		for(String j:btnNames) {//如:ZL
			List<Pair<GamePadAction, List<T>>> list=new ArrayList<Pair<GamePadAction, List<T>>>();
			for(GamePadAction i :GamePadAction.values()) {//如:防御
				List<T> games=GamePadSetting.GetActionOnBtnOfAllGame(gameCls,i,(a,b,c)->GamePadSetting.GetBtnByText(a, j));
				if(!games.isEmpty()) {
					Pair<GamePadAction, List<T>> keyValue = ImmutablePair.of(i, games);
					   list.add(keyValue);
				}   
				//s=s.replace("{"+j+"_"+i.toString()+"}",PFDataHelper.FormatString("<a href=\"#\" onclick=\"alert('{2}')\" >{0}^{1}</a>", i,games.size(),String.join(",",PFDataHelper.ListSelect(games, a->a.getGameName()) )) );
			}
			if(!list.isEmpty()) {
				sortByComparator2(list);
				Pair<GamePadAction, List<T>> top=list.get(0);
				int topIdx=0;
				while(GamePadAction.OK==top.getKey()||GamePadAction.Cancel==top.getKey()) {
					topIdx++;
					top=list.get(topIdx);
				}
				String tmpS=SGDataHelper.FormatString("{0}<h3 style=\"white-space:nowrap\">({1})</h3>"
						, j
						,SGDataHelper.FormatString("<a href=\"#\" onclick=\"alert('{2}')\" >{0}^{1}</a>", top.getKey(),top.getValue().size(),String.join(",",SGDataHelper.ListSelect(top.getValue(), a->a.getGameName()) ))
						);
				int cnt=list.size();
				if(cnt>1) {
//					List<Pair<GamePadAction, List<IGamePadSetting>>> other=list.subList(1,list.size()<=showTop?list.size():showTop);

					List<Pair<GamePadAction, List<T>>> other=list.stream().collect(Collectors.toList());
					other.remove(topIdx);
					if(cnt>showTop) {
						 other=other.subList(0,showTop-1);
					}
					List<String> sList=SGDataHelper.ListSelect(other, a->SGDataHelper.FormatString("(<a href=\"#\" onclick=\"alert('{2}')\" >{0}^{1}</a>)", a.getKey(),a.getValue().size(),String.join(",",SGDataHelper.ListSelect(a.getValue(), b->b.getGameName()) )));
					//Object sList=PFDataHelper.FormatString("<a href=\"#\" onclick=\"alert('{2}')\" >{0}^{1}</a>", i,games.size(),String.join(",",PFDataHelper.ListSelect(games, a->a.getGameName()) )) ;
					tmpS+=String.join("", sList);
				}
				s=s.replace("{"+j+"}", tmpS);					
			}
//			s=s.replace("{ZL_"+i.toString()+"}",i.toString()+"^"+String.valueOf( GamePadSetting.GetActionOnBtnTime(i,(a,b,c)->a.getZL())));
//			s=s.replace("{L_"+i.toString()+"}",i.toString()+"^"+String.valueOf( GamePadSetting.GetActionOnBtnTime(i,(a,b,c)->a.getL())));
//			s=s.replace("{B_"+i.toString()+"}",i.toString()+"^"+String.valueOf( GamePadSetting.GetActionOnBtnTime(i,(a,b,c)->a.getB())));
//			s=s.replace("{R_"+i.toString()+"}",i.toString()+"^"+String.valueOf( GamePadSetting.GetActionOnBtnTime(i,(a,b,c)->a.getR())));
		}
		
		//游戏列表
		List<String> games=SGDataHelper.MapSelect(GamePadSetting.AllIGame(gameCls), (k,v)->SGDataHelper.FormatString("<a href=\"/{2}/{0}\" target=\"_blank\">{1}</a>",v.getClass().getSimpleName(),v.getGameName(),gameCls.getSimpleName()));
		Collections.sort(games);
		s=s.replace("{gamePageHrefList}",String.join(",&nbsp;", games) );
		return s;
    }

    /**
     * 单页面方式，不跳转，便于使用git pages服务
     * @param <T>
     * @param s
     * @param gameCls
     * @return
     */
    private  <T extends IGamePadSetting>  String getGameS2(String s,Class<T> gameCls) {

		//旧版本,前端要这样写 {ZL_防御}
		String[] btnNames=GamePadSetting.AllBtnKey();
		for(GamePadAction i :GamePadAction.values()) {//如:防御
			for(String j:btnNames) {//如:ZL
				List<T> games=GamePadSetting.GetActionOnBtnOfAllGame(gameCls,i,(a,b,c)->GamePadSetting.GetBtnByText(a, j));
				s=s.replace("{"+j+"_"+i.toString()+"}",SGDataHelper.FormatString("<a href=\"#\" onclick=\"alert('{2}')\" >{0}^{1}</a>", i,games.size(),String.join(",",SGDataHelper.ListSelect(games, a->a.getGameName()) )) );
			}
//			s=s.replace("{ZL_"+i.toString()+"}",i.toString()+"^"+String.valueOf( GamePadSetting.GetActionOnBtnTime(i,(a,b,c)->a.getZL())));
//			s=s.replace("{L_"+i.toString()+"}",i.toString()+"^"+String.valueOf( GamePadSetting.GetActionOnBtnTime(i,(a,b,c)->a.getL())));
//			s=s.replace("{B_"+i.toString()+"}",i.toString()+"^"+String.valueOf( GamePadSetting.GetActionOnBtnTime(i,(a,b,c)->a.getB())));
//			s=s.replace("{R_"+i.toString()+"}",i.toString()+"^"+String.valueOf( GamePadSetting.GetActionOnBtnTime(i,(a,b,c)->a.getR())));
		}
		
		//新版本,自动统计所有按钮,前端要这样写 {ZL}
		int showTop=5;
		for(String j:btnNames) {//如:ZL
			List<Pair<GamePadAction, List<T>>> list=new ArrayList<Pair<GamePadAction, List<T>>>();
			for(GamePadAction i :GamePadAction.values()) {//如:防御
				List<T> games=GamePadSetting.GetActionOnBtnOfAllGame(gameCls,i,(a,b,c)->GamePadSetting.GetBtnByText(a, j));
				if(!games.isEmpty()) {
					Pair<GamePadAction, List<T>> keyValue = ImmutablePair.of(i, games);
					   list.add(keyValue);
				}   
				//s=s.replace("{"+j+"_"+i.toString()+"}",PFDataHelper.FormatString("<a href=\"#\" onclick=\"alert('{2}')\" >{0}^{1}</a>", i,games.size(),String.join(",",PFDataHelper.ListSelect(games, a->a.getGameName()) )) );
			}
			if(!list.isEmpty()) {
				sortByComparator2(list);
				Pair<GamePadAction, List<T>> top=list.get(0);
				int topIdx=0;
				while(GamePadAction.OK==top.getKey()||GamePadAction.Cancel==top.getKey()) {
					topIdx++;
					top=list.get(topIdx);
				}
				String tmpS=SGDataHelper.FormatString("{0}<h3 style=\"white-space:nowrap\">({1})</h3>"
						, j
						,SGDataHelper.FormatString("<a href=\"javascript:;\" onclick=\"alert('{2}')\" >{0}^{1}</a>", top.getKey(),top.getValue().size(),String.join(",",SGDataHelper.ListSelect(top.getValue(), a->a.getGameName()) ))
						);
				int cnt=list.size();
				if(cnt>1) {
//					List<Pair<GamePadAction, List<IGamePadSetting>>> other=list.subList(1,list.size()<=showTop?list.size():showTop);

					List<Pair<GamePadAction, List<T>>> other=list.stream().collect(Collectors.toList());
					other.remove(topIdx);
					if(cnt>showTop) {
						 other=other.subList(0,showTop-1);
					}
					List<String> sList=SGDataHelper.ListSelect(other, a->SGDataHelper.FormatString("(<a href=\"javascript:;\" onclick=\"alert('{2}')\" >{0}^{1}</a>)", a.getKey(),a.getValue().size(),String.join(",",SGDataHelper.ListSelect(a.getValue(), b->b.getGameName()) )));
					//Object sList=PFDataHelper.FormatString("<a href=\"#\" onclick=\"alert('{2}')\" >{0}^{1}</a>", i,games.size(),String.join(",",PFDataHelper.ListSelect(games, a->a.getGameName()) )) ;
					tmpS+=String.join("", sList);
				}
				s=s.replace("{"+j+"}", tmpS);					
			}

		}
		
		//游戏列表
		//1.跳转方式
//		List<String> games=SGDataHelper.MapSelect(GamePadSetting.AllIGame(gameCls), 
//				(k,v)->
//		SGDataHelper.FormatString("<a href=\"/{2}/{0}\" target=\"_blank\">{1}</a>",v.getClass().getSimpleName(),v.getGameName(),gameCls.getSimpleName()));
		//2.弹窗方式
		List<String> games=SGDataHelper.MapSelect(GamePadSetting.AllIGame(gameCls), 
				(k,v)->{
					
		 return SGDataHelper.FormatString(
				 "(<a href=\"javascript:;\" onclick=\"alert('{0}\\r\\n L2:{1}  R2:{2}\\r\\n L1:{3}  R1:{4}\\r\\n"
		 +" Y:{5}  X:{6}\\r\\n B:{7}  A:{8}\\r\\n L3:{9}  R3:{10}\\r\\n"
						 +" LEFT:{11}  RIGHT:{12}\\r\\n UP:{13}  DOWN:{14}')\" >{0}</a>)",
				//v.getClass().getSimpleName(),
				v.getGameName()//,gameCls.getSimpleName()
				,getButtonActionName(v.getZL()),getButtonActionName(v.getZR())
				,getButtonActionName(v.getL()),getButtonActionName(v.getR())
				,getButtonActionName(v.getY()),getButtonActionName(v.getX())
				,getButtonActionName(v.getB()),getButtonActionName(v.getA())
				,getButtonActionName(v.getLS()),getButtonActionName(v.getRS())
				,getButtonActionName(v.getLEFT()),getButtonActionName(v.getRIGHT())
				,getButtonActionName(v.getUP()),getButtonActionName(v.getDOWN())
				);
				}
				);
		
		Collections.sort(games);
		s=s.replace("{gamePageHrefList}",String.join(",&nbsp;", games) );
		

		//格斗例子,前端要这样写 {kof_ZL}
		StreetFighter6Classic kof=new StreetFighter6Classic();
		for(String j:btnNames) {//如:ZL
			s=s.replace("{kof_"+j+"}",GamePadSetting.GetBtnText(GamePadSetting.GetBtnByText(kof, j) ));
		}
		return s;
    }
    private String getButtonActionName(GamePadCustomAction[] action) {
    	if(null==action) {return "";}
    	return String.join(",",SGDataHelper.ArraySelect(String.class, action, (a)->a.toString()));
    }
	@GetMapping(value = { "/story/{storyName}" })
	public ModelAndView Story(@PathVariable(name = "storyName")String storyName) throws IOException {
		String s=SGDataHelper.ReadLocalTxt(storyName+".html",LocalDataType.System);
		ViewData.put("storyName",storyName);
		if("gamePad".equals(storyName)||"gamePadOld".equals(storyName)) {

			s=getGameS(s,ISwitchPadSetting.class);
		}else if("xboxPad".equals(storyName)) {
			s=getGameS(s,IXboxPadSetting.class);
		}else if("gamePad2".equals(storyName)) {
			//单页面，不跳转
			s=getGameS2(s,ISwitchPadSetting.class);
		}
        return View(s,"Story/Index");
	}

	private static <T extends IGamePadSetting> void sortByComparator2(List<Pair<GamePadAction, List<T>>> list) {
        // sort list based on comparator
        Collections.sort(list, new Comparator<Pair<GamePadAction, List<T>>>() {
            public int compare(Pair<GamePadAction, List<T>> o1, Pair<GamePadAction, List<T>> o2) {
                //return  o1.getValue().size().compareTo(o2.getValue().size());
                return  o2.getValue().size()-o1.getValue().size();
            }
        });

    }
}
