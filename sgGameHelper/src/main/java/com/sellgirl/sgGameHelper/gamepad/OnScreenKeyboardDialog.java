package com.sellgirl.sgGameHelper.gamepad;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.sellgirl.sgGameHelper.SGGameHelper;
import com.sellgirl.sgGameHelper.ScreenSetting;
import com.sellgirl.sgGameHelper.tabUi.ISGTabNode;
import com.sellgirl.sgGameHelper.tabUi.SGTabUDLRMap;
import com.sellgirl.sgGameHelper.tabUi.TabUi;

/**
 * 英文输入虚拟键盘
 * 来自deepseek
 */
public class OnScreenKeyboardDialog extends Dialog// Group 
{
    public interface KeyboardListener {
        void onKeyPressed(char character);
        void onBackspace();
        void onEnter();
        void onShiftToggle(boolean shifted);
        void onKeyboardHidden();
    }

    private Table mainTable;
    private Skin skin;
    private KeyboardListener listener;
    private boolean isShifted = false;
    private boolean isVisible = false;

    // 按键定义
    private final String[] row1 = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
    private final String[] upperRow = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"};
    private final String[] middleRow = {"A", "S", "D", "F", "G", "H", "J", "K", "L", "@"};
    private final String[] lowerRow = {"Z", "X", "C", "V", "B", "N", "M", ",","."};
    
    private Array<Button> keyButtons = new Array<>();
//    private Array<Label> keyButtons = new Array<>();
    private Button shiftBtn=null;
    private Button backspaceBtn=null;
    private Button hideBtn=null;
    private Button spaceBtn=null;
    private Button enterBtn=null;

    public OnScreenKeyboardDialog(Skin skin, KeyboardListener listener) {
    	super("", skin);
        this.skin = skin;
        this.listener = listener;
        setupKeyboard();
        setTouchable(Touchable.enabled);
    }

    private TabUi tabUi;
    private SGTabUDLRMap tabMap;
    private void setupKeyboard() {
    	//原本非弹窗的写法
//        mainTable = new Table();
////        mainTable.setBackground(skin.getDrawable("window"));
//		mainTable.align(Align.topLeft);
//		mainTable.pad(10);
//		mainTable.setX(0);
//		ScreenSetting screenSetting=SGGameHelper.getGameConfig().getScreenSetting();
//		mainTable.setY(screenSetting.getWORLD_HEIGHT());
//        mainTable.defaults().pad(2).size(60, 60);
        
        //弹窗的写法
        Table contentTable = getContentTable();
        contentTable.pad(10);
        mainTable = new Table();
        mainTable.defaults().pad(2).size(60, 60)//.fillX().uniform().expandX()
        ;

        // 第1行
        for (String key : row1) {
            addKeyButton(key);
        }
        mainTable.row();
        // 第一行
        for (String key : upperRow) {
            addKeyButton(key);
        }
        mainTable.row();

        // 第二行
        for (String key : middleRow) {
            addKeyButton(key);
        }
        mainTable.row();

        // 第三行
        // Shift 键
        shiftBtn = new TextButton("↑", skin);
        shiftBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toggleShift();
            }
        });
        mainTable.add(shiftBtn);

        for (String key : lowerRow) {
            addKeyButton(key);
        }

        // Backspace 键
        backspaceBtn = new TextButton("←", skin);
        backspaceBtn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
                if (listener != null) listener.onBackspace();
            }
        });
        mainTable.add(backspaceBtn);
        mainTable.row();

        // 第四行 - 功能键
         hideBtn = new TextButton("隐藏", skin);
        hideBtn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
                hide();
            }
        });
        mainTable.add(hideBtn).colspan(2);

         spaceBtn = new TextButton("空格", skin);
        spaceBtn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
                if (listener != null) listener.onKeyPressed(' ');
            }
        });
        mainTable.add(spaceBtn).colspan(5);

         enterBtn = new TextButton("确定", skin);
        enterBtn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
                if (listener != null) listener.onEnter();
            }
        });
        mainTable.add(enterBtn).colspan(2);

//        addActor(mainTable);
////        setSize(mainTable.getPrefWidth(), mainTable.getPrefHeight());
////        setX(0);
////        setY(0);

        contentTable.add(mainTable);
        setSize(mainTable.getPrefWidth(), mainTable.getPrefHeight());
        
        //tabUi处理
        //1行10个 2行10个 3行10个 第4行11个
        this.tabMap=new SGTabUDLRMap();
//        Array<Button> b=this.keyButtons;
        ISGTabNode b1=a(0);
        tabMap.generateUDLRConnect(
        		b1,a(1),a(2),a(3),a(4),a(5),a(6),a(7),a(8),a(9),null,
        		a(10),a(11),a(12),a(13),a(14),a(15),a(16),a(17),a(18),a(19),null,
        		a(20),a(21),a(22),a(23),a(24),a(25),a(26),a(27),a(28),a(29),null,
        		tabMap.newNode(shiftBtn),a(30),a(31),a(32),a(33),a(34),a(35),a(36),a(37),a(38),tabMap.newNode(backspaceBtn),null,//,a(39)//,a(40),a(41)
        		tabMap.newNode(hideBtn),tabMap.newNode(spaceBtn),tabMap.newNode(enterBtn)//,null,
        		//a(42),a(43),a(44)
        		);
        tabMap.setFirstNode(b1);
        tabUi=new TabUi();
   	 tabUi.setTabMap(tabMap);
        
    }
    private ISGTabNode a(int i) {
    	return tabMap.newNode(keyButtons.get(i));
    }
//
    private void addKeyButton(String key) {
        String displayText = isShifted ? key.toUpperCase() : key.toLowerCase();
        Button btn = new TextButton(displayText, skin);
        btn.setUserObject(key); // 存储原始键值
//        btn.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                String character = isShifted ? key.toUpperCase() : key.toLowerCase();
//                if (listener != null) listener.onKeyPressed(character.charAt(0));
//                
//                // 按下Shift后自动取消Shift（可选）
//                // if (isShifted) toggleShift();
//            }
//        });
        btn.addListener(new ChangeListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                String character = isShifted ? key.toUpperCase() : key.toLowerCase();
//                if (listener != null) listener.onKeyPressed(character.charAt(0));
//                
//                // 按下Shift后自动取消Shift（可选）
//                // if (isShifted) toggleShift();
//            }

			@Override
			public void changed(ChangeEvent event, Actor actor) {
                String character = isShifted ? key.toUpperCase() : key.toLowerCase();
                if (listener != null) listener.onKeyPressed(character.charAt(0));
			}
        });
        mainTable.add(btn);
        keyButtons.add(btn);
    }

//    private void addKeyButton(String key) {
//        String displayText = isShifted ? key.toUpperCase() : key.toLowerCase();
//        Label btn = new Label(displayText, skin);
//        btn.setUserObject(key); // 存储原始键值
//        btn.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                String character = isShifted ? key.toUpperCase() : key.toLowerCase();
//                if (listener != null) listener.onKeyPressed(character.charAt(0));
//                
//                // 按下Shift后自动取消Shift（可选）
//                // if (isShifted) toggleShift();
//            }
//        });
//        mainTable.add(btn);
//        keyButtons.add(btn);
//    }
    
    private void toggleShift() {
        isShifted = !isShifted;
        updateKeyLabels();
        if (listener != null) listener.onShiftToggle(isShifted);
    }

    private void updateKeyLabels() {
        for (Button btn : keyButtons) {
            if (btn instanceof TextButton) {
                String originalKey = (String) btn.getUserObject();
                String displayText = isShifted ? originalKey.toUpperCase() : originalKey.toLowerCase();
                ((TextButton) btn).setText(displayText);
            }
        }
    }
//    private void updateKeyLabels() {
//        for (Label btn : keyButtons) {
//            //if (btn instanceof TextButton) {
//                String originalKey = (String) btn.getUserObject();
//                String displayText = isShifted ? originalKey.toUpperCase() : originalKey.toLowerCase();
////                ((TextButton) btn).setText(displayText);
//                btn.setText(displayText);
//            //}
//        }
//    }

    public void show() {
        setVisible(true);
        isVisible = true;
        // 将键盘放置在屏幕底部
        setPosition(
            (Gdx.graphics.getWidth() - getWidth()) / 2,
            0
        );
    }

    public void hide() {
        setVisible(false);
        isVisible = false;
        if (listener != null) listener.onKeyboardHidden();
    }

    public boolean isKeyboardVisible() {
        return isVisible;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        setTouchable(visible ? Touchable.enabled : Touchable.disabled);
    }
    
    public TabUi getTabUi() {
    	return tabUi;
    }
}