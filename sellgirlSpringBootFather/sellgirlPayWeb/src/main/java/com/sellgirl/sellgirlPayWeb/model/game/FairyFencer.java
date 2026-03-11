package com.sellgirl.sellgirlPayWeb.model.game;

import org.springframework.stereotype.Component;

import com.sellgirl.sellgirlPayWeb.model.GamePadAction;
import com.sellgirl.sellgirlPayWeb.model.GamePadCustomAction;
import com.sellgirl.sellgirlPayWeb.model.HomeGame;
//import com.sellgirl.sellgirlPayWeb.model.IGamePadSetting;
import com.sellgirl.sellgirlPayWeb.model.ISwitchPadSetting;

@Component
public class FairyFencer implements ISwitchPadSetting{

	@Override
	public HomeGame getGame() {
		return HomeGame.FairyFencer;
	}

	@Override
	public String getGameName() {
		return "妖精剑士";
	}

	@Override
	public GamePadCustomAction[] getZL() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.Dash)};
	}

	@Override
	public GamePadCustomAction[] getL() {
		return null;
	}

	@Override
	public GamePadCustomAction[] getZR() {
		return null;
	}

	@Override
	public GamePadCustomAction[] getR() {
		return null;
	}

	@Override
	public GamePadCustomAction[] getX() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.菜单),new GamePadCustomAction(GamePadAction.自动对话  )};
	}

	@Override
	public GamePadCustomAction[] getY() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.任务),new GamePadCustomAction(GamePadAction.高速模式)};
	}

	@Override
	public GamePadCustomAction[] getA() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.MATK),new GamePadCustomAction(GamePadAction.OK)};
	}

	@Override
	public GamePadCustomAction[] getB() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.Jump),new GamePadCustomAction(GamePadAction.Cancel)};
	}

	@Override
	public GamePadCustomAction[] getUP() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.视角近)};
	}

	@Override
	public GamePadCustomAction[] getDOWN() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.视角远)};
	}

	@Override
	public GamePadCustomAction[] getLEFT() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.视角左)};
	}

	@Override
	public GamePadCustomAction[] getRIGHT() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.视角右)};
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
