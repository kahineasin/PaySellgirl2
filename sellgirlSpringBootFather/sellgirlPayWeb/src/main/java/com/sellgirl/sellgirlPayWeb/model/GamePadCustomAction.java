package com.sellgirl.sellgirlPayWeb.model;

import com.sellgirl.sellgirlPayWeb.model.GamePadAction.PressType;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public class GamePadCustomAction {
	GamePadAction standardAction;
	String customAction;
	boolean standard=true;
	private PressType pressType;//暂未使用
	/**
	 * 改用Init方法吧
	 * @param standardAction
	 */
	public GamePadCustomAction(GamePadAction standardAction){
		this.standardAction=standardAction;
		standard=true;
		pressType=standardAction.getPressType();
	}
	public GamePadCustomAction(String action){
//		this.customAction=action;
//		standard=false;
//		pressType=PressType.短按;
		this(action,PressType.短按);
	}
	public GamePadCustomAction(String action,PressType pressType){
		this.customAction=action;
		standard=false;
		this.pressType=pressType;
	}
	public static GamePadCustomAction[] Init(GamePadAction... standardAction) {
		return SGDataHelper.ArraySelect(GamePadCustomAction.class, standardAction, a->new GamePadCustomAction(a));
	}
	@Override
	public String toString() {
		return standard?standardAction.toString():customAction;
	}
}
