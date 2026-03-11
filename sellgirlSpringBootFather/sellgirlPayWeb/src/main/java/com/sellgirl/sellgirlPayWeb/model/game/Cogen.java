package com.sellgirl.sellgirlPayWeb.model.game;

import org.springframework.stereotype.Component;

import com.sellgirl.sellgirlPayWeb.model.GamePadAction;
import com.sellgirl.sellgirlPayWeb.model.GamePadCustomAction;
import com.sellgirl.sellgirlPayWeb.model.HomeGame;
//import com.sellgirl.sellgirlPayWeb.model.IGamePadSetting;
import com.sellgirl.sellgirlPayWeb.model.ISwitchPadSetting;

@Component
public class Cogen implements ISwitchPadSetting{

	@Override
	public HomeGame getGame() {
		return HomeGame.Cogen;
	}

	@Override
	public String getGameName() {
		return "大凤羽空与刻之剑";
	}

	@Override
	public GamePadCustomAction[] getZL() {
		return new GamePadCustomAction[] {};
	}

	@Override
	public GamePadCustomAction[] getL() {
		return new GamePadCustomAction[] {};
	}

	@Override
	public GamePadCustomAction[] getZR() {
		return null;
	}

	@Override
	public GamePadCustomAction[] getR() {
		return GamePadCustomAction.Init(GamePadAction.Dash);
	}

	@Override
	public GamePadCustomAction[] getX() {
		return new GamePadCustomAction[] {new GamePadCustomAction("时光倒流")};
	}

	@Override
	public GamePadCustomAction[] getY() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.LATK)};
	}

	@Override
	public GamePadCustomAction[] getA() {
		return GamePadCustomAction.Init(GamePadAction.Dodge);
	}

	@Override
	public GamePadCustomAction[] getB() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.Jump)};
	}

	@Override
	public GamePadCustomAction[] getUP() {
		return new GamePadCustomAction[] {};
	}

	@Override
	public GamePadCustomAction[] getDOWN() {
		return new GamePadCustomAction[] {};
	}

	@Override
	public GamePadCustomAction[] getLEFT() {
		return new GamePadCustomAction[] {};
	}

	@Override
	public GamePadCustomAction[] getRIGHT() {
		return null;
	}

	@Override
	public GamePadCustomAction[] getLS() {
		return new GamePadCustomAction[] {};
	}

	@Override
	public GamePadCustomAction[] getRS() {
		return new GamePadCustomAction[] {};
	}

}
