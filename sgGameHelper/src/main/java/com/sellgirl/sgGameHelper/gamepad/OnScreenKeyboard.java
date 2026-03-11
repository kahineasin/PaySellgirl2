package com.sellgirl.sgGameHelper.gamepad;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

/**
 * 英文输入虚拟键盘
 * 来自deepseek
 * @deprecated 改用OnScreenKeyboardDialog.java
 */
@Deprecated
public class OnScreenKeyboard extends Group {
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
    private final String[] upperRow = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"};
    private final String[] middleRow = {"A", "S", "D", "F", "G", "H", "J", "K", "L"};
    private final String[] lowerRow = {"Z", "X", "C", "V", "B", "N", "M", ",","."};
    
    private Array<Button> keyButtons = new Array<>();

    public OnScreenKeyboard(Skin skin, KeyboardListener listener) {
        this.skin = skin;
        this.listener = listener;
        setupKeyboard();
        setTouchable(Touchable.enabled);
    }

    private void setupKeyboard() {
        mainTable = new Table();
//        mainTable.setBackground(skin.getDrawable("window"));
        mainTable.defaults().pad(2).size(60, 60);

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
        Button shiftBtn = new TextButton("↑", skin);
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
        Button backspaceBtn = new TextButton("←", skin);
        backspaceBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (listener != null) listener.onBackspace();
            }
        });
        mainTable.add(backspaceBtn);
        mainTable.row();

        // 第四行 - 功能键
        Button hideBtn = new TextButton("隐藏", skin);
        hideBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });
        mainTable.add(hideBtn).colspan(2);

        Button spaceBtn = new TextButton("空格", skin);
        spaceBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (listener != null) listener.onKeyPressed(' ');
            }
        });
        mainTable.add(spaceBtn).colspan(5);

        Button enterBtn = new TextButton("确定", skin);
        enterBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (listener != null) listener.onEnter();
            }
        });
        mainTable.add(enterBtn).colspan(2);

        addActor(mainTable);
        setSize(mainTable.getPrefWidth(), mainTable.getPrefHeight());
        setX(0);
        setY(0);
    }

    private void addKeyButton(String key) {
        String displayText = isShifted ? key.toUpperCase() : key.toLowerCase();
        Button btn = new TextButton(displayText, skin);
        btn.setUserObject(key); // 存储原始键值
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String character = isShifted ? key.toUpperCase() : key.toLowerCase();
                if (listener != null) listener.onKeyPressed(character.charAt(0));
                
                // 按下Shift后自动取消Shift（可选）
                // if (isShifted) toggleShift();
            }
        });
        mainTable.add(btn);
        keyButtons.add(btn);
    }

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
}