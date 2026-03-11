package com.sellgirl.sgGameHelper;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.sellgirl.sgJavaHelper.time.OnceWaiter;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.touchable;

/**
 * 帮助显示保存提示的工具
 * 使用方法：
 * 1. msgContainer.showErrorText(TXT.g("url error"));
 * 2. msgContainer.showSuccessText(TXT.g("save success"));
 * 3. render方法中 msgContainer.act();
 */
public class MsgLabelContainer {
	Label saveResultLbl;Color successColor;Color errorColor;
    private OnceWaiter msgWaiter;
	public MsgLabelContainer(Label msgLbl,Color successColor,Color errorColor) {
		this.saveResultLbl=msgLbl;
		this.successColor=successColor;
		this.errorColor=errorColor;
		msgWaiter=new OnceWaiter(2);
	}
	public void showErrorText(String err){
		saveResultLbl.setText(err);
		saveResultLbl.setColor(errorColor);
//		this.msgWaitCount=this.msgWait;
		msgWaiter.start();
		showSaveResultLbl(true,false,saveResultLbl);
	}
	public void showSuccessText(String msg){
		saveResultLbl.setText(msg);
		saveResultLbl.setColor(successColor);
//		this.msgWaitCount=this.msgWait;
		msgWaiter.start();
		showSaveResultLbl(true,false,saveResultLbl);
	}
	public void act() {
//		if(msgWaiter.isOK()){
//			if(Touchable.disabled!=saveResultLbl.getTouchable()) {
//				showSaveResultLbl(false, true,saveResultLbl);
//			}
//		}

		if(Touchable.disabled!=saveResultLbl.getTouchable()&&msgWaiter.isOK()) {//顺序不能变，因为msgWaiter的flag会变的
			showSaveResultLbl(false, true,saveResultLbl);
		}
	}
    /**
     * 显示保存提示信息信息
     * @param visible
     * @param animated
     * @param saveResultLbl
     */
    private void showSaveResultLbl(boolean visible, boolean animated,Label saveResultLbl) {
		float alphaTo = visible ? 0.8f : 0.0f;
		float duration = animated ? 1.0f : 0.0f;
		Touchable touchEnabled = visible ? Touchable.enabled : Touchable.disabled;
		if(saveResultLbl.hasActions()){
			saveResultLbl.clearActions();
		}
		saveResultLbl.addAction(sequence(
				touchable(touchEnabled),
				alpha(alphaTo, duration)));
	}
}
