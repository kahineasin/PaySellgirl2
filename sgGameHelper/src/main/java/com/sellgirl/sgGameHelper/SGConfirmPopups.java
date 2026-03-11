package com.sellgirl.sgGameHelper;

import java.util.function.Consumer;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.sellgirl.sgGameHelper.tabUi.SGTabLRMap;
import com.sellgirl.sgGameHelper.tabUi.TabUi;

/**
 * 
 * 暂停不可以用弹窗,因为弹窗是和stage绑一起的,如果stage要显示,角色也会动
 * Created by Benjamin Schulte on 26.11.2018.
 */

public class SGConfirmPopups extends Dialog {

    //private final GdxPayApp app;
//    SashaGame game;
//    GameScreen screen;
//    private final TextButton restoreButton;
    private final TextButton closeButton;
    private final TextButton confirmButton;

    //private boolean restorePressed;
//    private IapButton buyEntitlement;
//    private IapButton buyConsumable;
    Consumer<?> action;
    String text;
    private TabUi tabUi; 

    public SGConfirmPopups(String text,Consumer<?> action,Skin skin) {
        super("", skin);
//        this.game=game;
//        this.screen=screen;
        //this.app = app;
        this.action=action;
        this.text=text;
        
        ISGLanguage TXT=SGGameHelper.getLanguage();

        closeButton = new TextButton(TXT.g("close"), skin);
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            	hide();
            }
        });
        button(closeButton);

        confirmButton = new TextButton(TXT.g("confirm"), skin);
        //confirmButton.setDisabled(true);
        confirmButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //restorePressed = true;
                //restoreButton.setDisabled(true);
                //app.purchaseManager.purchaseRestore();
            	SGConfirmPopups.this.action.accept(null);
            	hide();
            }
        });
//
        getButtonTable().add(confirmButton);
        tabUi=new TabUi();
        SGTabLRMap tabMap=new SGTabLRMap();
        tabMap.addItem(closeButton);
        tabMap.addItem(confirmButton);
        tabUi.setTabMap(tabMap);
        // GUI erstmal so aufbauen
        //fillContent(app);

        fillContent();

        // den Init lostreten so früh es geht, aber nicht bevor die GUI-Referenzen existieren :-)
        //initPurchaseManager();
    }

    private void fillContent(//GdxPayApp app
    		) {
        Table contentTable = getContentTable();
        contentTable.pad(10);


        TextButton btn1=new TextButton(text,this.getSkin());
        contentTable.add(btn1);
        contentTable.row();
//        contentTable.add(btn2);
//        contentTable.row();
//        contentTable.add(btn3);
//        contentTable.row();
    }
    public TabUi getTab() {
    	return tabUi;
    }


}
