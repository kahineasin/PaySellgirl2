package com.sellgirl.sellgirlPayWeb.model.game;

import org.springframework.stereotype.Component;

import com.sellgirl.sellgirlPayWeb.model.GamePadAction;
import com.sellgirl.sellgirlPayWeb.model.GamePadCustomAction;
import com.sellgirl.sellgirlPayWeb.model.HomeGame;
//import com.sellgirl.sellgirlPayWeb.model.IGamePadSetting;
import com.sellgirl.sellgirlPayWeb.model.ISwitchPadSetting;

@Component
public class Kirby  implements ISwitchPadSetting{

	@Override
	public HomeGame getGame() {
		return HomeGame.Kirby;
	}

	@Override
	public String getGameName() {
		return "星之卡比";
	}

	@Override
	public GamePadCustomAction[] getZL() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.Defend)};
	}

	@Override
	public GamePadCustomAction[] getL() {
		return null;
	}

	@Override
	public GamePadCustomAction[] getZR() {
		return new GamePadCustomAction[] {};
	}

	@Override
	public GamePadCustomAction[] getR() {
		return null;
	}

	@Override
	public GamePadCustomAction[] getX() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.使用道具)};
	}

	@Override
	public GamePadCustomAction[] getY() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.LATK)};
	}

	@Override
	public GamePadCustomAction[] getA() {
		return new GamePadCustomAction[] {new GamePadCustomAction("舍弃能力")};
	}

	@Override
	public GamePadCustomAction[] getB() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.Jump)};
	}

	@Override
	public GamePadCustomAction[] getUP() {
		return null;
	}

	@Override
	public GamePadCustomAction[] getDOWN() {
		return null;
	}

	@Override
	public GamePadCustomAction[] getLEFT() {
		return null;
	}

	@Override
	public GamePadCustomAction[] getRIGHT() {
		return null;
	}

	@Override
	public GamePadCustomAction[] getLS() {
		return null;
	}

	@Override
	public GamePadCustomAction[] getRS() {
		return null;
	}

}
