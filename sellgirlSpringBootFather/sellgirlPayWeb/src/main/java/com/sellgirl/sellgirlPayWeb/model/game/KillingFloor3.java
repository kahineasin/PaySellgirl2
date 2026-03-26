package com.sellgirl.sellgirlPayWeb.model.game;

import org.springframework.stereotype.Component;

import com.sellgirl.sellgirlPayWeb.model.GamePadAction;
import com.sellgirl.sellgirlPayWeb.model.GamePadCustomAction;
import com.sellgirl.sellgirlPayWeb.model.HomeGame;
//import com.sellgirl.sellgirlPayWeb.model.IGamePadSetting;
import com.sellgirl.sellgirlPayWeb.model.ISwitchPadSetting;

@Component
public class KillingFloor3 implements ISwitchPadSetting{

	@Override
	public HomeGame getGame() {
		return null;
	}

	@Override
	public String getGameName() {
		return "KillingFloor3";
	}

	@Override
	public GamePadCustomAction[] getZL() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.射击)};
	}

	@Override
	public GamePadCustomAction[] getL() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.Jump)};
	}

	@Override
	public GamePadCustomAction[] getZR() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.短按切换成开镜)};
	}

	@Override
	public GamePadCustomAction[] getR() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.快捷菜单)};
	}

	@Override
	public GamePadCustomAction[] getX() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.ChangeWeapon)};
	}

	@Override
	public GamePadCustomAction[] getY() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.LATK)};
	}

	@Override
	public GamePadCustomAction[] getA() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.Dodge)};
	}

	@Override
	public GamePadCustomAction[] getB() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.补充弹药),new GamePadCustomAction(GamePadAction.B1)};
	}

	@Override
	public GamePadCustomAction[] getUP() {
		return new GamePadCustomAction[] {new GamePadCustomAction("手电筒")};
	}

	@Override
	public GamePadCustomAction[] getDOWN() {
		return new GamePadCustomAction[] {new GamePadCustomAction("切换弹药模式")};
	}

	@Override
	public GamePadCustomAction[] getLEFT() {
		return new GamePadCustomAction[] {};
	}

	@Override
	public GamePadCustomAction[] getRIGHT() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.放置标记) };
	}

	@Override
	public GamePadCustomAction[] getLS() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.Dash)};
	}

	@Override
	public GamePadCustomAction[] getRS() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.手榴弹)};
	}

}
