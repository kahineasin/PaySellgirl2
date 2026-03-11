package com.sellgirl.sgGameHelper.tabUi;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.sellgirl.sgGameHelper.SGLibGdxHelper;
import com.sellgirl.sgGameHelper.gamepad.ISGPS5Gamepad;
import com.sellgirl.sgGameHelper.gamepad.OnScreenKeyboard;
import com.sellgirl.sgGameHelper.gamepad.OnScreenKeyboard.KeyboardListener;
import com.sellgirl.sgGameHelper.gamepad.OnScreenKeyboardDialog;
import com.badlogic.gdx.scenes.scene2d.Event;

/**
 * 用于手柄操作ui光标时
 *
 * 使用方式： 
 * 1. tabUi.addItem(restoreButton); 
 * 2.
 * if(null!=winOptions&&winOptions.isVisible()){ 
 * //弹窗打开时 if(0>=buttonWaitCount){
 * if (optionsWindowTabUi.isEditing()) { 
 * //tabUi编辑状态 if (sgcontroller.isROUND())
 * { tabUi.select(); oWait = oWaitConst; } 
 * else { if (tabUi.edit(sgcontroller))
 * { oWait = oWaitConst; } } }
 * else{ //无编辑时的主要操作
 * if(null!=sgcontroller&&sgcontroller.isUP()){ 
 * popups.getTabUi().up();
 * buttonWaitCount=buttonWait; }//else down left right... 
 * else if (null !=sgcontroller && sgcontroller.isCROSS()) { 
 * tabUi.select(); oWait = oWaitConst;
 * } else if (null != sgcontroller && sgcontroller.isROUND()) {
 * game.setScreen(new MainMenuScreen(game)); dispose(); } } } } 
 * 3. 
 * if(0<buttonWaitCount ) { buttonWaitCount -= delta; } 
 * else if (buttonWaitCount <0) { buttonWaitCount = 0; } 
 * 4. 如果select的按钮中有跳转页面的按钮，需要在render中加判断，否则stage
 * null报错 if(null==stage){//tabUi触发页面跳转 return; }
 */
public class TabUi {

	private Skin skin;
//    private int size=0;
//    private int current=-1;
//    private Array<Actor> items;
//    Actor currentActor=null;
	private ISGTabMap tabMap = null;

	boolean editing = false;

//    private SGAction<Actor,Object,Object> defaultAction;
//    private SGAction<Actor,Object,Object> selectAction;
	public TabUi() {

	}

	public void setItem(Array<Actor> items) {
//        this.items=items;
//        size=items.size;

		SGTabUpDownMap tmpTabMap = new SGTabUpDownMap();
		tmpTabMap.setItem(items);
		tabMap = tmpTabMap;
	}

	public void addItem(Actor item) {
//        if(null==this.items){
//            items=new Array<>();
//        }
//        items.add(item);
//        size++;

		if (null == this.tabMap) {
			tabMap = new SGTabUpDownMap();
		}
		((SGTabUpDownMap) tabMap).addItem(item);
	}

	public void setTabMap(ISGTabMap tabMap) {
		this.tabMap = tabMap;
	}

	public void up() {
//        if(null!=currentActor){
//            currentActor.setColor(Color.BLACK);
//        }
//        if(-1==current){
//            current=0;
//        }
//        else if(0>=current){
//            current=size-1;
//        }else{
//            current--;
//        }
//        currentActor=items.get(current);
//        currentActor.setColor(Color.RED);
//        move(-1);
		move(Direction.UP);
	}

	public void down() {

//        move(1);
		move(Direction.DOWN);
	}

	public void left() {

//        move(1);
		move(Direction.LEFT);
	}

	public void right() {

//        move(1);
		move(Direction.RIGHT);
	}

	private enum Direction {
		UP, DOWN, LEFT, RIGHT
	}

	public void move(Direction y) {
		Actor currentActor = (Actor) tabMap.getCurrent();
		if (null != currentActor) {
			// currentActor.setColor(Color.BLACK);
			// defaultAction.go(currentActor,null,null);
			if (CheckBox.class == currentActor.getClass()) {
				((CheckBox) currentActor).setStyle(normalCheckBoxStyle);
			} else if (TextButton.class == currentActor.getClass()) {
//                ((TextButton)currentActor).setChecked(false);
				((TextButton) currentActor).setStyle(normalTextButtonStyle);
			} else if (SelectBox.class == currentActor.getClass()) {
				((SelectBox) currentActor).setStyle(normalSelectBoxStyle);
			} else if (Slider.class == currentActor.getClass()) {
				((Slider) currentActor).setStyle(normalSliderStyle);
			} else if (Label.class == currentActor.getClass()) {
				((Label) currentActor).setStyle(normalLabelStyle);
			}
		}
//        if(-1==current){
//            current=0;
//        }else{
//            current=(current+y)%size;
//            if(0>current){current+=size;}
//        }
//        currentActor=items.get(current);

//        if(y>0){
//            tabMap.down();
//        }else if(y<0){
//            tabMap.up();
//        }
		switch (y) {
		case UP:
			tabMap.up();
			break;
		case DOWN:
			tabMap.down();
			break;
		case LEFT:
			tabMap.left();
			break;
		case RIGHT:
			tabMap.right();
			break;
		default:
			break;
		}
		currentActor = (Actor) tabMap.getCurrent();

		if (null == currentActor) {
			return;
		}
		// currentActor.setColor(Color.RED);
//        selectAction.go(currentActor,null,null);

		if (CheckBox.class == currentActor.getClass()) {
			CheckBox button = ((CheckBox) currentActor);
			normalCheckBoxStyle = button.getStyle();
			if (null == focusCheckBoxStyle) {
//                focusTextButtonStyle =new TextButton.TextButtonStyle(button.getStyle());
				setFocusCheckBoxStyle(button.getStyle(), button);
			}
			// ((TextButton)currentActor).setChecked(true);
			button.setStyle(focusCheckBoxStyle);
//            Color aa=button
		} else if (TextButton.class == currentActor.getClass()) {
			TextButton button = ((TextButton) currentActor);
			normalTextButtonStyle = button.getStyle();
			if (null == focusTextButtonStyle) {
//                focusTextButtonStyle =new TextButton.TextButtonStyle(button.getStyle());
				setFocusTextButtonStyle(button.getStyle());
			}
			// ((TextButton)currentActor).setChecked(true);
			button.setStyle(focusTextButtonStyle);
//            Color aa=button
		} else if (SelectBox.class == currentActor.getClass()) {
			SelectBox button = ((SelectBox) currentActor);
			normalSelectBoxStyle = button.getStyle();
			if (null == focusSelectBoxStyle) {
//                focusTextButtonStyle =new TextButton.TextButtonStyle(button.getStyle());
				setFocusSelectBoxStyle(button.getStyle());
			}
			// ((TextButton)currentActor).setChecked(true);
			button.setStyle(focusSelectBoxStyle);
//            Color aa=button
		} else if (Slider.class == currentActor.getClass()) {
			Slider button = ((Slider) currentActor);
			normalSliderStyle = button.getStyle();
			if (null == focusSliderStyle) {
				setFocusSliderStyle(button.getStyle());
			}
			button.setStyle(focusSliderStyle);
		} else if (Label.class == currentActor.getClass()) {
			Label button = ((Label) currentActor);
			normalLabelStyle = button.getStyle();
			if (null == focusLabelStyle) {
//                focusTextButtonStyle =new TextButton.TextButtonStyle(button.getStyle());
				setFocusLabelStyle(button.getStyle());
			}
			// ((TextButton)currentActor).setChecked(true);
			button.setStyle(focusLabelStyle);
//            Color aa=button
		}
	}

	TextButton.TextButtonStyle focusTextButtonStyle = null;
	TextButton.TextButtonStyle normalTextButtonStyle = null;
	SelectBox.SelectBoxStyle focusSelectBoxStyle = null;
	SelectBox.SelectBoxStyle normalSelectBoxStyle = null;
	CheckBox.CheckBoxStyle focusCheckBoxStyle = null;
	CheckBox.CheckBoxStyle normalCheckBoxStyle = null;
	Slider.SliderStyle focusSliderStyle = null;
	Slider.SliderStyle normalSliderStyle = null;
	Label.LabelStyle focusLabelStyle = null;
	Label.LabelStyle normalLabelStyle = null;

	private void setFocusTextButtonStyle(TextButton.TextButtonStyle style) {
		focusTextButtonStyle = new TextButton.TextButtonStyle(style);
		focusTextButtonStyle.fontColor = Color.YELLOW;
//                focusTextButtonStyle.checkedFontColor=Color.YELLOW;//Button总是切换到Checked状态，想想怎么处理好
//                focusTextButtonStyle.focusedFontColor=Color.YELLOW;
//                focusTextButtonStyle.checkedFocusedFontColor=Color.YELLOW;
//                focusTextButtonStyle.checkedDownFontColor=Color.YELLOW;
//                focusTextButtonStyle.checkedOverFontColor=Color.YELLOW;
//                focusTextButtonStyle.downFontColor=Color.YELLOW;
//                focusTextButtonStyle.overFontColor=Color.YELLOW;
	}

	private void setFocusSelectBoxStyle(SelectBox.SelectBoxStyle style) {
		focusSelectBoxStyle = new SelectBox.SelectBoxStyle(style);
		focusSelectBoxStyle.fontColor = Color.YELLOW;
	}

	public Drawable checkboxOn, checkboxOff;

	private void setFocusCheckBoxStyle(CheckBox.CheckBoxStyle style, CheckBox button) {
		focusCheckBoxStyle = new CheckBox.CheckBoxStyle(style);
		focusCheckBoxStyle.fontColor = Color.YELLOW;
//        Drawable tmp1=button.getSkin().newDrawable("white", Color.YELLOW);
//        tmp1.setMinHeight(3);
//        style.ba = tmp1;//非必需,但设置了可以挡住后面

		Skin skin = button.getSkin();
		if (null == skin) {
			skin = this.skin;
		}
		if (null != skin) {
			int w = 10;
			int w2 = 10;
			if (null == checkboxOn) {
				checkboxOn = skin.newDrawable("white", Color.YELLOW);
				checkboxOn.setMinWidth(w);
				checkboxOn.setMinHeight(w);
			}
//		textButtonStyle.checkboxOn = skin.newDrawable("white", Color.DARK_GRAY);
			focusCheckBoxStyle.checkboxOn = checkboxOn;
			if (null == checkboxOff) {
				checkboxOff = skin.newDrawable("white", Color.RED);
				checkboxOff.setMinWidth(w2);
				checkboxOff.setMinHeight(w2);
//		textButtonStyle.checkboxOff = skin.newDrawable("white", Color.LIGHT_GRAY);
			}
			focusCheckBoxStyle.checkboxOff = checkboxOff;
		}
	}

	public Drawable sliderBackground, sliderKnob;

	private void setFocusSliderStyle(Slider.SliderStyle style) {
		focusSliderStyle = new Slider.SliderStyle(style);
//        focusSliderStyle.fontColor=Color.YELLOW;
//        Drawable tmp1=button.getSkin().newDrawable("white", Color.YELLOW);
//        tmp1.setMinHeight(3);
//        style.ba = tmp1;//非必需,但设置了可以挡住后面

		Skin skin = null;// button.getSkin();
		if (null == skin) {
			skin = this.skin;
		}
		if (null != skin) {
			int w = 10;
			int w2 = 10;
			if (null == sliderBackground) {
				sliderBackground = skin.newDrawable("white", Color.RED);
//                sliderBackground.setMinWidth(w2);
//                sliderBackground.setMinHeight(w2);
				sliderBackground.setMinHeight(3);
//		textButtonStyle.checkboxOff = skin.newDrawable("white", Color.LIGHT_GRAY);
			}
			focusSliderStyle.background = sliderBackground;

			if (null == sliderKnob) {
				sliderKnob = skin.newDrawable("white", Color.YELLOW);
//                sliderKnob.setMinWidth(w);
//                sliderKnob.setMinHeight(w);
				sliderKnob.setMinWidth(6);
				sliderKnob.setMinHeight(14);
			}
//		textButtonStyle.checkboxOn = skin.newDrawable("white", Color.DARK_GRAY);
			focusSliderStyle.knob = sliderKnob;
		}
	}

	private void setFocusLabelStyle(Label.LabelStyle style) {
		focusLabelStyle = new Label.LabelStyle(style);
		focusLabelStyle.fontColor = Color.YELLOW;
//                focusTextButtonStyle.checkedFontColor=Color.YELLOW;
//                focusTextButtonStyle.focusedFontColor=Color.YELLOW;
//                focusTextButtonStyle.checkedFocusedFontColor=Color.YELLOW;
//                focusTextButtonStyle.checkedDownFontColor=Color.YELLOW;
//                focusTextButtonStyle.checkedOverFontColor=Color.YELLOW;
//                focusTextButtonStyle.downFontColor=Color.YELLOW;
//                focusTextButtonStyle.overFontColor=Color.YELLOW;
	}
//    public void click(){
//        if(null!=currentActor){
//            currentActor.cli
//        }
//    }
//    @Deprecated
//    public Actor getCurrentActor(){
//        return currentActor;
//    }

	/**
	 * select和unselect均调用此方法
	 */
	public void select() {
		Actor currentActor = (Actor) tabMap.getCurrent();
		if (null != currentActor) {
			// 注意这里的顺序要按继承顺序来
			if (CheckBox.class == currentActor.getClass()) {
				// ok
//                ((ClickListener)((TextButton)currentActor).getListeners().get(((TextButton)currentActor).getListeners().size-1)).clicked(null,0,0);

////                //((ClickListener)((CheckBox)currentActor).getListeners().get(((CheckBox)currentActor).getListeners().size-1)).clicked(null,0,0);
////                ClickListener last=((ClickListener)((TextButton)currentActor).getListeners().get(((TextButton)currentActor).getListeners().size-1));
////                last.clicked(null,0,0);
////                InputEvent e=new InputEvent();
//                ChangeListener.ChangeEvent e=new ChangeListener.ChangeEvent();
//                e.setTarget(currentActor);
////                currentActor.notify(e,true);//fail
////                currentActor.notify(e,false);//fail
//                currentActor.fire(e);//fail

				// 不会勾上
////                executeButtonListener2(currentActor);
//                Event e=new ChangeEvent();
//                e.setTarget(currentActor);
//                e.setStage(currentActor.getStage());
//                SGLibGdxHelper.executeButtonListener(currentActor,e);
////                ((CheckBox)currentActor).setChecked(editing);

				InputEvent e2 = new InputEvent();
				e2.setTarget(currentActor);
				e2.setStage(currentActor.getStage());
				e2.setType(InputEvent.Type.touchDown);
				e2.setListenerActor(currentActor);
				SGLibGdxHelper.executeButtonListener(currentActor, e2);
				InputEvent e3 = new InputEvent();
				e3.setTarget(currentActor);
				e3.setStage(currentActor.getStage());
				e3.setType(InputEvent.Type.touchUp);
				e3.setListenerActor(currentActor);
				SGLibGdxHelper.executeButtonListener(currentActor, e3);

			} else if (TextButton.class == currentActor.getClass()) {
//                ((ClickListener)((TextButton)currentActor).getListeners().get(((TextButton)currentActor).getListeners().size-1)).clicked(null,0,0);

//                ChangeListener.ChangeEvent e=new ChangeListener.ChangeEvent();
//                e.setTarget(currentActor);
////                currentActor.notify(e,true);//fail
////                currentActor.notify(e,false);//fail
//                currentActor.fire(e);//fail

//                executeButtonListener2(currentActor);

				//这里InputEvent会改变Checked状态,尽量去掉的话，样式更好，
				//但直接去掉可能不会触发ClickListener
				for(int i=1;currentActor.getListeners().size>i;i++) {
					//第0个是button的内部listener,如果后面有用户的clickListener，才发InputEvent
					if(currentActor.getListeners().get(i) instanceof ClickListener) {
						InputEvent e2 = new InputEvent();
						e2.setTarget(currentActor);
						e2.setStage(currentActor.getStage());
						e2.setType(InputEvent.Type.touchDown);
						e2.setListenerActor(currentActor);
						SGLibGdxHelper.executeButtonListener(currentActor, e2);
						InputEvent e3 = new InputEvent();
						e3.setTarget(currentActor);
						e3.setStage(currentActor.getStage());
						e3.setType(InputEvent.Type.touchUp);
						e3.setListenerActor(currentActor);
						SGLibGdxHelper.executeButtonListener(currentActor, e3);
						break;
					}
				}
				Event e = new ChangeEvent();
				e.setTarget(currentActor);
				e.setStage(currentActor.getStage());
				SGLibGdxHelper.executeButtonListener(currentActor, e);
			} else if (SelectBox.class == currentActor.getClass()) {
				SelectBox button = ((SelectBox) currentActor);
				if (editing) {
					button.hideScrollPane();
					editing = false;
				} else {
					button.showScrollPane();
					editing = true;
				}
			} else if (Slider.class == currentActor.getClass()) {
				// Slider button=((Slider)currentActor);
				if (editing) {
					// button.hideScrollPane();
					editing = false;
				} else {
					// button.showScrollPane();
					editing = true;
				}
			} else if (Label.class == currentActor.getClass()) {
//                executeButtonListener2(currentActor);
				Event e = new ChangeEvent();
				e.setTarget(currentActor);
				e.setStage(currentActor.getStage());
				SGLibGdxHelper.executeButtonListener(currentActor, e);
				InputEvent e2 = new InputEvent();
				e2.setTarget(currentActor);
				e2.setStage(currentActor.getStage());
				e2.setType(InputEvent.Type.touchDown);
				e2.setListenerActor(currentActor);
				SGLibGdxHelper.executeButtonListener(currentActor, e2);
				InputEvent e3 = new InputEvent();
				e3.setTarget(currentActor);
				e3.setStage(currentActor.getStage());
				e3.setType(InputEvent.Type.touchUp);
				e3.setListenerActor(currentActor);
				SGLibGdxHelper.executeButtonListener(currentActor, e3);
			} else if (TextField.class == currentActor.getClass()) {
//            	//方法1.这样可以调用系统虚拟键盘，但在安卓电视上回调无响应
//                Event e=new ChangeEvent();
//                e.setTarget(currentActor);
//                e.setStage(currentActor.getStage());
//                SGLibGdxHelper.executeButtonListener(currentActor,e);
//                InputEvent e2=new InputEvent();
//                e2.setTarget(currentActor);
//                e2.setStage(currentActor.getStage());
//                e2.setType(InputEvent.Type.touchDown);
//                e2.setListenerActor(currentActor);
//                SGLibGdxHelper.executeButtonListener(currentActor,e2);
//                InputEvent e3=new InputEvent();
//                e3.setTarget(currentActor);
//                e3.setStage(currentActor.getStage());
//                e3.setType(InputEvent.Type.touchUp);
//                e3.setListenerActor(currentActor);
//                SGLibGdxHelper.executeButtonListener(currentActor,e3);

				if(null==enInputPopups&&null!=currentActor.getStage()) {
//					OnScreenKeyboardDialog kb=tabUi.initKeyboard();
					currentActor.getStage().addActor(initKeyboard());
				}
				// 方法2
				if (//null != currentActor.getStage()
						null!=enInputPopups
						) {
					if (editing) {
						// button.hideScrollPane();
						enInputPopups.hide();
						editing = false;
					} else {
						// button.showScrollPane();
						editing = true;

						// 创建键盘

//                        // 创建示例文本输入框
//                        final TextField textField = new TextField("", skin);
//                        textField.setPosition(100, 600);
//                        textField.setSize(400, 60);

//                        // 点击文本框时显示键盘
//                        textField.addListener(new ClickListener() {
//                            @Override
//                            public void clicked(InputEvent event, float x, float y) {
//                                activeTextField = textField;
//                                enInputPopups.show();
//                            }
//                        });
						activeTextField = (TextField) currentActor;
						enInputPopups.show();
					}
				}
			}
		}
	}


	// deepseek说gdx提供了这个调用系统键盘的类，但并没有找到
//    private TextInputDialog inputDialog=null; 
//	private OnScreenKeyboard enInputPopups = null;
	private OnScreenKeyboardDialog enInputPopups = null;
	private TextField activeTextField = null;

	private OnScreenKeyboardDialog initKeyboard() {
		if (null == enInputPopups) {
			enInputPopups = new OnScreenKeyboardDialog(skin, new com.sellgirl.sgGameHelper.gamepad.OnScreenKeyboardDialog.KeyboardListener() {
				// 实现 KeyboardListener 接口
				@Override
				public void onKeyPressed(char character) {
					if (activeTextField != null) {
						String currentText = activeTextField.getText();
						activeTextField.setText(currentText + character);
					}
				}

				@Override
				public void onBackspace() {
					if (activeTextField != null) {
						String currentText = activeTextField.getText();
						if (currentText.length() > 0) {
							activeTextField.setText(currentText.substring(0, currentText.length() - 1));
						}
					}
				}

				@Override
				public void onEnter() {
					if (activeTextField != null) {
						// 处理回车键逻辑，例如提交表单
						enInputPopups.hide();
						editing = false;
					}
				}

				@Override
				public void onShiftToggle(boolean shifted) {
					// 处理Shift切换
				}

				@Override
				public void onKeyboardHidden() {
					activeTextField = null;
					editing = false;
				}
			});
			enInputPopups.setVisible(false); // 默认隐藏
//		Stage stage = currentActor.getStage();
//		stage.addActor(enInputPopups);
		}
		return enInputPopups;
	}
//    private Stage stage;
//    public void setStage(Stage stage) {
//    	this.stage=stage;
//    }
//    private void executeButtonListener(EventListener listener){
//        if(ClickListener.class==listener.getClass()){
//            ((ClickListener)listener).clicked(null,0,0);
//        }
//    }
//    private void executeButtonListener2(Actor actor){
////    	((TextButton)actor).fi
//        SGLibGdxHelper.executeButtonListener(actor);
////        EventListener last=actor.getListeners().get(actor.getListeners().size-1);
//////        if(ClickListener.class==last.getClass()){
//////            ((ClickListener)last).clicked(null,0,0);
//////        }else if(ChangeListener.class==last.getClass()){
//////            ((ChangeListener)last).changed(null,null);
//////        }
////        //注意匿名类不能这样判断 ClickListener.class==last.getClass()
////        if(last instanceof ClickListener){
////            ((ClickListener)last).clicked(null,0,0);
////        }else if(last instanceof ChangeListener){
////            ((ChangeListener)last).changed(null,null);
////        }
//    }

	public boolean isEditing() {
		return editing;
	}

	public boolean edit(ISGPS5Gamepad pad) {
		if (!editing) {
			return false;
		}
		boolean isUp = pad.isUP() || pad.isStick1Up();
		boolean isDown = pad.isDOWN() || pad.isStick1Down();
		boolean isLeft = pad.isLEFT() || pad.isStick1Left();
		boolean isRight = pad.isRIGHT() || pad.isStick1Right();
		Actor currentActor = (Actor) tabMap.getCurrent();
		if (SelectBox.class == currentActor.getClass()) {

			SelectBox button = ((SelectBox) currentActor);
			int boxSize = button.getItems().size;
			int boxIdx = button.getSelectedIndex();

			if (isUp || isDown) {
				int y = isUp ? -1 : 1;
				if (-1 == boxIdx) {
					boxIdx = 0;
				} else {
					boxIdx = (boxIdx + y) % boxSize;
					if (0 > boxIdx) {
						boxIdx += boxSize;
					}

				}
				button.setSelected(button.getItems().get(boxIdx));
				button.hideScrollPane();
				button.showScrollPane();
				return true;
			}

		} else if (Slider.class == currentActor.getClass()) {

			Slider button = ((Slider) currentActor);
			float step = button.getStepSize();
			if (isLeft) {
				button.setValue(button.getValue() - step);
				return true;
			} else if (isRight) {
				button.setValue(button.getValue() + step);
				return true;
			}
//            int boxSize=button.getItems().size;
//            int boxIdx=button.getSelectedIndex();
//
//            if(pad.isUP()||pad.isDOWN()){
//                int y=isUp?-1:1;
//                if(-1==boxIdx){
//                    boxIdx=0;
//                }else{
//                    boxIdx=(boxIdx+y)%boxSize;
//                    if(0>boxIdx){boxIdx+=boxSize;}
//
//                }
//                button.setSelected(button.getItems().get(boxIdx));
//                button.hideScrollPane();
//                button.showScrollPane();
//                return true;
//            }

		} else if (TextField.class == currentActor.getClass()) {
			if(null != enInputPopups) {
//			TextField button = ((TextField) currentActor);
//			if (isRight) {
////        		enInputPopups.
//			}
				if(isUp) {
					enInputPopups.getTabUi().up();
					return true;
				}else if(isDown) {
					enInputPopups.getTabUi().down();
					return true;
				}else if(isLeft) {
					enInputPopups.getTabUi().left();
					return true;
				}else if(isRight) {
					enInputPopups.getTabUi().right();
					return true;
				}else if(pad.isCROSS()) {
					enInputPopups.getTabUi().select();
					return true;
				}
			}
		}
		return false;
	}

	public void setSkin(Skin skin) {
		this.skin = skin;
	}
//    public void setDefaultAction(SGAction<Actor, Object, Object> defaultAction) {
//        this.defaultAction = defaultAction;
//    }
//
//    public void setSelectAction(SGAction<Actor, Object, Object> selectAction) {
//        this.selectAction = selectAction;
//    }

//    /**
//     * 旧菜单只有上下移，此类希望实现上下左右（这种方式也有缺点，就是首行最最后一行不容易循环
//     * ISGTabMap接口已实现
//     */
//    private static class TabUiItem{
//        public TabUiItem up=null;
//        public TabUiItem down=null;
//    }
}
