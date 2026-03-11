package com.sellgirl.sellgirlPayWeb.model.game;

import org.springframework.stereotype.Component;

import com.sellgirl.sellgirlPayWeb.model.GamePadAction;
import com.sellgirl.sellgirlPayWeb.model.GamePadCustomAction;
import com.sellgirl.sellgirlPayWeb.model.HomeGame;
import com.sellgirl.sellgirlPayWeb.model.ISwitchPadSetting;

@Component
public class YsIX implements ISwitchPadSetting{

	@Override
	public HomeGame getGame() {
		return HomeGame.YsIX;
	}

	@Override
	public String getGameName() {
		return "伊苏9";
	}

	@Override
	public GamePadCustomAction[] getZL() {
		return GamePadCustomAction.Init(GamePadAction.Defend);
	}

	@Override
	public GamePadCustomAction[] getL() {
		return GamePadCustomAction.Init();
	}

	@Override
	public GamePadCustomAction[] getZR() {
		return GamePadCustomAction.Init(GamePadAction.变更领队);
	}

	@Override
	public GamePadCustomAction[] getR() {
		return GamePadCustomAction.Init(GamePadAction.技能快捷菜单);
	}

	@Override
	public GamePadCustomAction[] getX() {
		return GamePadCustomAction.Init(GamePadAction.锁定目标);
	}

	@Override
	public GamePadCustomAction[] getY() {
		return GamePadCustomAction.Init(GamePadAction.LATK);
	}

	@Override
	public GamePadCustomAction[] getA() {
		return GamePadCustomAction.Init(GamePadAction.Dodge);
	}

	@Override
	public GamePadCustomAction[] getB() {
		return GamePadCustomAction.Init(GamePadAction.Jump);
	}

	@Override
	public GamePadCustomAction[] getUP() {
		return new GamePadCustomAction[] {};
	}

	@Override
	public GamePadCustomAction[] getDOWN() {
		return GamePadCustomAction.Init();
	}

	@Override
	public GamePadCustomAction[] getLEFT() {
		return new GamePadCustomAction[] {new GamePadCustomAction("地图")};
	}

	@Override
	public GamePadCustomAction[] getRIGHT() {
		return null;
	}

	@Override
	public GamePadCustomAction[] getLS() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.Dash)};
	}

	@Override
	public GamePadCustomAction[] getRS() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.重置视角)};
	}

}
