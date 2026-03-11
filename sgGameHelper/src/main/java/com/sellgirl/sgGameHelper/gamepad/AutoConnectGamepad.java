package com.sellgirl.sgGameHelper.gamepad;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.sellgirl.sgJavaHelper.ISGDisposable;

public class AutoConnectGamepad implements ISGDisposable{
	protected List<ISGPS5Gamepad> gamepad;
	SGConnectControllerListener controllerListener;
//	public AutoConnectGamepad(List<ISGPS5Gamepad> gamepad) {
//		this.gamepad=gamepad;
//		controllerListener=new SGConnectControllerListener();
//		Controllers.addListener(controllerListener);
//	}
	public AutoConnectGamepad() {
		this.gamepad=new ArrayList<ISGPS5Gamepad>();
		controllerListener=new SGConnectControllerListener();
		Controllers.addListener(controllerListener);
	}
	public void addGamepad(ISGPS5Gamepad gamepad) {
		this.gamepad.add(gamepad);
	}
	 private  class SGConnectControllerListener extends ControllerAdapter  {

			@Override
			public boolean buttonDown (Controller controller, int buttonIndex) {
//////////		         if(XBoxKey.CROSS.ordinal()==buttonIndex) {
//////////			         Gdx.app.log(TAG, "jump: " +buttonIndex);
//////////		         }
//////				 Gdx.app.log(TAG, "buttonDown: " +buttonIndex);
////////		         if(buttonIndex==controller.getMapping().buttonA) {
////////			         //Gdx.app.log(TAG, "jump: " +buttonIndex);
////////			         //actor.setState(SashaState.JUMP);
////////		         }
//				SGDataHelper.getLog().print(controller.getName()+"__"+controller.getPlayerIndex()+"__"+ buttonIndex);
				return false;
			}

			@Override
			public boolean buttonUp (Controller controller, int buttonIndex) {
				return false;
			}
			@Override
			public boolean axisMoved (Controller controller, int axisIndex, float value) {
////				if(value>0.1||value<-0.1) {
//					if(value>0.5||value<-0.5) {
//
//					 Gdx.app.log(TAG, "axisMoved: " +axisIndex+"__"+value);	
//				}
				return false;
			}
		   @Override
		   public void connected(Controller controller) {
//			   if(uuid.equals(controller.getUniqueId())) {
//				   SGPS5Gamepad.this.controller=controller;
//				   SGPS5Gamepad.this.controller.addListener(controllerListener);
//				   msg="connected";
//			   }
			   for(ISGPS5Gamepad i2:gamepad) {
				   if(SGPS5Gamepad.class!=i2.getClass()) {
					   continue;
				   }
				   SGPS5Gamepad i=(SGPS5Gamepad) i2;
				   if(
						   (!i.getController().isConnected())
						   &&i.getController().getName().equals(controller.getName())
						   ) {
					   i.setController(controller);
					   break;
				   }
			   }			
		   }

		   @Override
		   public void disconnected(Controller controller) {
//			   if(SGPS5Gamepad.this.controller==controller) {
//				   SGPS5Gamepad.this.controller.removeListener(controllerListener);
//				   msg="disconnected";
//			   }
//			   for(SGPS5Gamepad i:gamepad) {
//				   if(i.getController()==controller) {
//					   
//				   }
//				   
//			   }
		   }
		 }
	@Override
	public void dispose() {
		gamepad.clear();
		gamepad=null;
		Controllers.removeListener(controllerListener);
		
	}
	
}
