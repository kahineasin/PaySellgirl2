package com.sellgirl.sellgirlPayWeb.model.game;

import org.springframework.stereotype.Component;

import com.sellgirl.sellgirlPayWeb.model.GamePadAction;
import com.sellgirl.sellgirlPayWeb.model.GamePadCustomAction;
import com.sellgirl.sellgirlPayWeb.model.HomeGame;
import com.sellgirl.sellgirlPayWeb.model.ISwitchPadSetting;

@Component
public class TrailsInTheFlash2 implements ISwitchPadSetting{

	@Override
	public HomeGame getGame() {
		return HomeGame.TrailsInTheFlash2;
	}

	@Override
	public String getGameName() {
		return "闪之轨迹2";
	}

	@Override
	public GamePadCustomAction[] getZL() {
		return new GamePadCustomAction[] {new GamePadCustomAction("加速")};
	}

	@Override
	public GamePadCustomAction[] getL() {
		return new GamePadCustomAction[] {new GamePadCustomAction("变更领队")};
	}

	@Override
	public GamePadCustomAction[] getZR() {
		// TODO Auto-generated method stub
		return new GamePadCustomAction[] {new GamePadCustomAction("料理笔记")};
	}

	@Override
	public GamePadCustomAction[] getR() {
		// TODO Auto-generated method stub
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.Dash  )};
	}

	@Override
	public GamePadCustomAction[] getX() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.菜单)};
	}

	@Override
	public GamePadCustomAction[] getY() {
		return new GamePadCustomAction[] {new GamePadCustomAction("任务提示")};
	}

	@Override
	public GamePadCustomAction[] getA() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.MATK),new GamePadCustomAction(GamePadAction.OK)};
	}

	@Override
	public GamePadCustomAction[] getB() {
		return new GamePadCustomAction[] {new GamePadCustomAction("慢走"),new GamePadCustomAction(GamePadAction.Cancel)};
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GamePadCustomAction[] getRS() {
		// TODO Auto-generated method stub
		return null;
	}

}
