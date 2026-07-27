package com.sellgirl.sellgirlPayWeb.model.game;

import org.springframework.stereotype.Component;

import com.sellgirl.sellgirlPayWeb.model.GamePadAction;
import com.sellgirl.sellgirlPayWeb.model.GamePadCustomAction;
import com.sellgirl.sellgirlPayWeb.model.HomeGame;
import com.sellgirl.sellgirlPayWeb.model.ISwitchPadSetting;
//import com.sellgirl.sellgirlPayWeb.model.IGamePadSetting;
import com.sellgirl.sellgirlPayWeb.model.IXboxPadSetting;

@Component
public class NineSols implements ISwitchPadSetting{

	@Override
	public HomeGame getGame() {
		return null;
	}

	@Override
	public String getGameName() {
		return "九日";
	}

	@Override
	public GamePadCustomAction[] getZL() {
		return new GamePadCustomAction[] {new GamePadCustomAction("玄蝶侦查"  )};
	}

	@Override
	public GamePadCustomAction[] getL() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.Defend  )};
	}

	@Override
	public GamePadCustomAction[] getZR() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.ATK含长按  )};
	}

	@Override
	public GamePadCustomAction[] getR() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.爪钩飞索),new GamePadCustomAction(GamePadAction.交互 )};
	}

	@Override
	public GamePadCustomAction[] getX() {
		return new GamePadCustomAction[] {new GamePadCustomAction("击发弓箭")};
	}

	@Override
	public GamePadCustomAction[] getY() {
		return new GamePadCustomAction[] {new GamePadCustomAction("符咒")};
	}

	@Override
	public GamePadCustomAction[] getA() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.Dodge)};
	}

	@Override
	public GamePadCustomAction[] getB() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.Jump)};
	}

	@Override
	public GamePadCustomAction[] getUP() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.红药)};
	}

	@Override
	public GamePadCustomAction[] getDOWN() {
		return null;
	}

	@Override
	public GamePadCustomAction[] getLEFT() {
		return new GamePadCustomAction[] {new GamePadCustomAction("更换弓箭")};
	}

	@Override
	public GamePadCustomAction[] getRIGHT() {
		return new GamePadCustomAction[] {new GamePadCustomAction("更换弓箭")};
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
