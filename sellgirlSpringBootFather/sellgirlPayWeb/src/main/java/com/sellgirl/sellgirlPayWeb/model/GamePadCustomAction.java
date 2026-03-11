package com.sellgirl.sellgirlPayWeb.model;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public class GamePadCustomAction {
	GamePadAction standardAction;
	String customAction;
	boolean standard=true;
	/**
	 * 改用Init方法吧
	 * @param standardAction
	 */
	public GamePadCustomAction(GamePadAction standardAction){
		this.standardAction=standardAction;
		standard=true;
	}
	public GamePadCustomAction(String action){
		this.customAction=action;
		standard=false;
	}
	public static GamePadCustomAction[] Init(GamePadAction... standardAction) {
		return SGDataHelper.ArraySelect(GamePadCustomAction.class, standardAction, a->new GamePadCustomAction(a));
	}
	@Override
	public String toString() {
		return standard?standardAction.toString():customAction;
	}
}
