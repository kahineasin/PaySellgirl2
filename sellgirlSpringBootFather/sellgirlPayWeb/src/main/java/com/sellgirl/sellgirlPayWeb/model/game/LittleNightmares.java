package com.sellgirl.sellgirlPayWeb.model.game;

import org.springframework.stereotype.Component;

import com.sellgirl.sellgirlPayWeb.model.GamePadAction;
import com.sellgirl.sellgirlPayWeb.model.GamePadCustomAction;
import com.sellgirl.sellgirlPayWeb.model.HomeGame;
//import com.sellgirl.sellgirlPayWeb.model.IGamePadSetting;
import com.sellgirl.sellgirlPayWeb.model.ISwitchPadSetting;

@Component
public class LittleNightmares implements ISwitchPadSetting{

	@Override
	public HomeGame getGame() {
		return HomeGame.LittleNightmares;
	}

	@Override
	public String getGameName() {
		return "小小梦魇";
	}

	@Override
	public GamePadCustomAction[] getZL() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.Crouch)};
	}

	@Override
	public GamePadCustomAction[] getL() {
		return new GamePadCustomAction[] {};
	}

	@Override
	public GamePadCustomAction[] getZR() {
		// TODO Auto-generated method stub
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.抓投)};
	}

	@Override
	public GamePadCustomAction[] getR() {
		// TODO Auto-generated method stub
		return new GamePadCustomAction[] {};
	}

	@Override
	public GamePadCustomAction[] getX() {
		return new GamePadCustomAction[] {};
	}

	@Override
	public GamePadCustomAction[] getY() {
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.Dash)};
	}

	@Override
	public GamePadCustomAction[] getA() {
		// TODO Auto-generated method stub
		return new GamePadCustomAction[] {new GamePadCustomAction("打火机")};
	}

	@Override
	public GamePadCustomAction[] getB() {
		// TODO Auto-generated method stub
		return new GamePadCustomAction[] {new GamePadCustomAction(GamePadAction.Jump)};
	}

	@Override
	public GamePadCustomAction[] getUP() {
		// TODO Auto-generated method stub
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
