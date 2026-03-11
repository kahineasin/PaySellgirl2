package com.sellgirl.sellgirlPayWeb.model.game;

import org.springframework.stereotype.Component;

import com.sellgirl.sellgirlPayWeb.model.GamePadAction;
import com.sellgirl.sellgirlPayWeb.model.GamePadCustomAction;
import com.sellgirl.sellgirlPayWeb.model.HomeGame;
import com.sellgirl.sellgirlPayWeb.model.ISwitchPadSetting;

@Component
public class Zelda implements ISwitchPadSetting{

	@Override
	public HomeGame getGame() {
		return HomeGame.Zelda;
	}

	@Override
	public String getGameName() {
		return "塞尔达";
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
		
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.射击)};
	}

	@Override
	public GamePadCustomAction[] getR() {
		
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.抓投  )};
	}

	@Override
	public GamePadCustomAction[] getX() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.Dash),new GamePadCustomAction(GamePadAction.收起武器)};
	}

	@Override
	public GamePadCustomAction[] getY() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.LATK)};
	}

	@Override
	public GamePadCustomAction[] getA() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.OK)};
	}

	@Override
	public GamePadCustomAction[] getB() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.Jump),new GamePadCustomAction(GamePadAction.Cancel)};
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
		return new GamePadCustomAction[] {new GamePadCustomAction("盾切换")};
	}

	@Override
	public GamePadCustomAction[] getRIGHT() {
		return new GamePadCustomAction[] {new GamePadCustomAction("剑切换")};
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
