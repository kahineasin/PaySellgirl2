package com.sellgirl.sellgirlPayWeb.model.game;

import org.springframework.stereotype.Component;

import com.sellgirl.sellgirlPayWeb.model.GamePadAction;
import com.sellgirl.sellgirlPayWeb.model.GamePadCustomAction;
import com.sellgirl.sellgirlPayWeb.model.HomeGame;
import com.sellgirl.sellgirlPayWeb.model.ISwitchPadSetting;
//import com.sellgirl.sellgirlPayWeb.model.IGamePadSetting;
import com.sellgirl.sellgirlPayWeb.model.IXboxPadSetting;

@Component
public class HollowKnight implements ISwitchPadSetting{

	@Override
	public HomeGame getGame() {
		return null;
	}

	@Override
	public String getGameName() {
		return "空洞骑士";
	}

	@Override
	public GamePadCustomAction[] getZL() {
		return new GamePadCustomAction[] {new GamePadCustomAction("高速冲刺"  )};
	}

	@Override
	public GamePadCustomAction[] getL() {
		return new GamePadCustomAction[] {new GamePadCustomAction("地图"  )};
	}

	@Override
	public GamePadCustomAction[] getZR() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.ATK含长按  )};
	}

	@Override
	public GamePadCustomAction[] getR() {
		return new GamePadCustomAction[] {new GamePadCustomAction("施法"  )};
	}

	@Override
	public GamePadCustomAction[] getX() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.特殊攻击)};
	}

	@Override
	public GamePadCustomAction[] getY() {
		return new GamePadCustomAction[] {};
	}

	@Override
	public GamePadCustomAction[] getA() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.Dash)};
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
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.交互)};
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
		return new GamePadCustomAction[] {};
	}

	@Override
	public GamePadCustomAction[] getRS() {
		return  new GamePadCustomAction[] {};
	}

}
