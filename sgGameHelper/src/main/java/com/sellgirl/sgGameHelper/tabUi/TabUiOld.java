//package com.mygdx.game.share.tabUi;
//
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
//import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
//import com.badlogic.gdx.scenes.scene2d.ui.Skin;
//import com.badlogic.gdx.scenes.scene2d.ui.Slider;
//import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
//import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
//import com.badlogic.gdx.utils.Array;
//import com.mygdx.game.share.SGLibGdxHelper;
//import com.mygdx.game.share.gamepad.ISGPS5Gamepad;
//
///**
// * 用于手柄操作ui光标时
// *
// 使用方式：
// 1.
// tabUi.addItem(restoreButton);
// 2.
// if(null!=winOptions&&winOptions.isVisible()){
//    //弹窗打开时
//    if(0>=buttonWaitCount){
//        if (optionsWindowTabUi.isEditing()) {
//            //tabUi编辑状态
//        }else{
//             if(null!=sgcontroller&&sgcontroller.isUP()){
//                popups.getTabUi().up();
//                buttonWaitCount=buttonWait;
//             }
//        }
//    }
// }
// 3.
// if (0<buttonWaitCount ) {
//    buttonWaitCount -= delta;
// } else if (buttonWaitCount < 0) {
//    buttonWaitCount = 0;
// }
// */
//public class TabUiOld {
//
//    private Skin skin;
//    private int size=0;
//    private int current=-1;
//    private Array<Actor> items;
//    Actor currentActor=null;
//
//
//    boolean editing=false;
//
////    private SGAction<Actor,Object,Object> defaultAction;
////    private SGAction<Actor,Object,Object> selectAction;
//    public TabUiOld() {
//
//    }
//    public void setItem(Array<Actor> items){
//        this.items=items;
//        size=items.size;
//    }
//    public void addItem(Actor item){
//        if(null==this.items){
//            items=new Array<>();
//        }
//        items.add(item);
//        size++;
//    }
//    public void up(){
////        if(null!=currentActor){
////            currentActor.setColor(Color.BLACK);
////        }
////        if(-1==current){
////            current=0;
////        }
////        else if(0>=current){
////            current=size-1;
////        }else{
////            current--;
////        }
////        currentActor=items.get(current);
////        currentActor.setColor(Color.RED);
//        move(-1);
//    }
//    public void down(){
//        move(1);
//    }
//
//    public void move(int y){
//        if(null!=currentActor){
//            //currentActor.setColor(Color.BLACK);
//            //defaultAction.go(currentActor,null,null);
//            if(CheckBox.class==currentActor.getClass()){
//                ((CheckBox)currentActor).setStyle(normalCheckBoxStyle);
//            }else if(TextButton.class==currentActor.getClass()){
////                ((TextButton)currentActor).setChecked(false);
//                ((TextButton)currentActor).setStyle(normalTextButtonStyle);
//            }else if(SelectBox.class==currentActor.getClass()){
//                ((SelectBox)currentActor).setStyle(normalSelectBoxStyle);
//            }else if(Slider.class==currentActor.getClass()){
//                ((Slider)currentActor).setStyle(normalSliderStyle);
//            }
//        }
//        if(-1==current){
//            current=0;
//        }else{
//            current=(current+y)%size;
//            if(0>current){current+=size;}
//        }
//        currentActor=items.get(current);
//        //currentActor.setColor(Color.RED);
////        selectAction.go(currentActor,null,null);
//
//        if(CheckBox.class==currentActor.getClass()){
//            CheckBox button=((CheckBox)currentActor);
//            normalCheckBoxStyle=button.getStyle();
//            if(null== focusCheckBoxStyle){
////                focusTextButtonStyle =new TextButton.TextButtonStyle(button.getStyle());
//                setFocusCheckBoxStyle(button.getStyle(),button);
//            }
//            //((TextButton)currentActor).setChecked(true);
//            button.setStyle(focusCheckBoxStyle);
////            Color aa=button
//        }else if(TextButton.class==currentActor.getClass()){
//            TextButton button=((TextButton)currentActor);
//            normalTextButtonStyle=button.getStyle();
//            if(null== focusTextButtonStyle){
////                focusTextButtonStyle =new TextButton.TextButtonStyle(button.getStyle());
//                setFocusTextButtonStyle(button.getStyle());
//            }
//            //((TextButton)currentActor).setChecked(true);
//            button.setStyle(focusTextButtonStyle);
////            Color aa=button
//        }else if(SelectBox.class==currentActor.getClass()){
//            SelectBox button=((SelectBox)currentActor);
//            normalSelectBoxStyle=button.getStyle();
//            if(null== focusSelectBoxStyle){
////                focusTextButtonStyle =new TextButton.TextButtonStyle(button.getStyle());
//                setFocusSelectBoxStyle(button.getStyle());
//            }
//            //((TextButton)currentActor).setChecked(true);
//            button.setStyle(focusSelectBoxStyle);
////            Color aa=button
//        }else if(Slider.class==currentActor.getClass()){
//            Slider button=((Slider)currentActor);
//            normalSliderStyle=button.getStyle();
//            if(null== focusSliderStyle){
//                setFocusSliderStyle(button.getStyle());
//            }
//            button.setStyle(focusSliderStyle);
//        }
//    }
//    TextButton.TextButtonStyle focusTextButtonStyle =null;
//    TextButton.TextButtonStyle normalTextButtonStyle=null;
//    SelectBox.SelectBoxStyle focusSelectBoxStyle=null;
//    SelectBox.SelectBoxStyle normalSelectBoxStyle=null;
//    CheckBox.CheckBoxStyle focusCheckBoxStyle=null;
//    CheckBox.CheckBoxStyle normalCheckBoxStyle=null;
//    Slider.SliderStyle focusSliderStyle=null;
//    Slider.SliderStyle normalSliderStyle=null;
//    private void setFocusTextButtonStyle(TextButton.TextButtonStyle style){
//        focusTextButtonStyle =new TextButton.TextButtonStyle(style);
//        focusTextButtonStyle.fontColor=Color.YELLOW;
////                focusTextButtonStyle.checkedFontColor=Color.YELLOW;
////                focusTextButtonStyle.focusedFontColor=Color.YELLOW;
////                focusTextButtonStyle.checkedFocusedFontColor=Color.YELLOW;
////                focusTextButtonStyle.checkedDownFontColor=Color.YELLOW;
////                focusTextButtonStyle.checkedOverFontColor=Color.YELLOW;
////                focusTextButtonStyle.downFontColor=Color.YELLOW;
////                focusTextButtonStyle.overFontColor=Color.YELLOW;
//    }
//    private void setFocusSelectBoxStyle(SelectBox.SelectBoxStyle style){
//        focusSelectBoxStyle =new SelectBox.SelectBoxStyle(style);
//        focusSelectBoxStyle.fontColor=Color.YELLOW;
//    }
//
//    public Drawable checkboxOn, checkboxOff;
//    private void setFocusCheckBoxStyle(CheckBox.CheckBoxStyle style,CheckBox button){
//        focusCheckBoxStyle =new CheckBox.CheckBoxStyle(style);
//        focusCheckBoxStyle.fontColor=Color.YELLOW;
////        Drawable tmp1=button.getSkin().newDrawable("white", Color.YELLOW);
////        tmp1.setMinHeight(3);
////        style.ba = tmp1;//非必需,但设置了可以挡住后面
//
//        Skin skin=button.getSkin();
//        if(null==skin){skin=this.skin;}
//        if(null!=skin) {
//            int w = 10;
//            int w2 = 10;
//            if(null==checkboxOn) {
//                checkboxOn = skin.newDrawable("white", Color.YELLOW);
//                checkboxOn.setMinWidth(w);
//                checkboxOn.setMinHeight(w);
//            }
////		textButtonStyle.checkboxOn = skin.newDrawable("white", Color.DARK_GRAY);
//            focusCheckBoxStyle.checkboxOn = checkboxOn;
//            if(null==checkboxOff) {
//                checkboxOff = skin.newDrawable("white", Color.RED);
//                checkboxOff.setMinWidth(w2);
//                checkboxOff.setMinHeight(w2);
////		textButtonStyle.checkboxOff = skin.newDrawable("white", Color.LIGHT_GRAY);
//            }
//            focusCheckBoxStyle.checkboxOff = checkboxOff;
//        }
//    }
//    public Drawable sliderBackground, sliderKnob;
//    private void setFocusSliderStyle(Slider.SliderStyle style){
//        focusSliderStyle =new Slider.SliderStyle(style);
////        focusSliderStyle.fontColor=Color.YELLOW;
////        Drawable tmp1=button.getSkin().newDrawable("white", Color.YELLOW);
////        tmp1.setMinHeight(3);
////        style.ba = tmp1;//非必需,但设置了可以挡住后面
//
//        Skin skin=null;//button.getSkin();
//        if(null==skin){skin=this.skin;}
//        if(null!=skin) {
//            int w = 10;
//            int w2 = 10;
//            if(null==sliderBackground) {
//                sliderBackground = skin.newDrawable("white", Color.RED);
////                sliderBackground.setMinWidth(w2);
////                sliderBackground.setMinHeight(w2);
//                sliderBackground.setMinHeight(3);
////		textButtonStyle.checkboxOff = skin.newDrawable("white", Color.LIGHT_GRAY);
//            }
//            focusSliderStyle.background = sliderBackground;
//
//            if(null==sliderKnob) {
//                sliderKnob = skin.newDrawable("white", Color.YELLOW);
////                sliderKnob.setMinWidth(w);
////                sliderKnob.setMinHeight(w);
//                sliderKnob.setMinWidth(6);
//                sliderKnob.setMinHeight(14);
//            }
////		textButtonStyle.checkboxOn = skin.newDrawable("white", Color.DARK_GRAY);
//            focusSliderStyle.knob = sliderKnob;
//        }
//    }
////    public void click(){
////        if(null!=currentActor){
////            currentActor.cli
////        }
////    }
//    @Deprecated
//    public Actor getCurrentActor(){
//        return currentActor;
//    }
//
//    /**
//     * select和unselect均调用此方法
//     */
//    public void select(){
//        if(null!=currentActor){
//            //注意这里的顺序要按继承顺序来
//            if(CheckBox.class==currentActor.getClass()){
//                //ok
////                ((ClickListener)((TextButton)currentActor).getListeners().get(((TextButton)currentActor).getListeners().size-1)).clicked(null,0,0);
//
//////                //((ClickListener)((CheckBox)currentActor).getListeners().get(((CheckBox)currentActor).getListeners().size-1)).clicked(null,0,0);
//////                ClickListener last=((ClickListener)((TextButton)currentActor).getListeners().get(((TextButton)currentActor).getListeners().size-1));
//////                last.clicked(null,0,0);
//////                InputEvent e=new InputEvent();
////                ChangeListener.ChangeEvent e=new ChangeListener.ChangeEvent();
////                e.setTarget(currentActor);
//////                currentActor.notify(e,true);//fail
//////                currentActor.notify(e,false);//fail
////                currentActor.fire(e);//fail
//
//                executeButtonListener2(currentActor);
//            }else if(TextButton.class==currentActor.getClass()){
////                ((ClickListener)((TextButton)currentActor).getListeners().get(((TextButton)currentActor).getListeners().size-1)).clicked(null,0,0);
//
////                ChangeListener.ChangeEvent e=new ChangeListener.ChangeEvent();
////                e.setTarget(currentActor);
//////                currentActor.notify(e,true);//fail
//////                currentActor.notify(e,false);//fail
////                currentActor.fire(e);//fail
//
//                executeButtonListener2(currentActor);
//            }else if(SelectBox.class==currentActor.getClass()){
//                SelectBox button=((SelectBox)currentActor);
//                if(editing){
//                    button.hideScrollPane();
//                    editing=false;
//                }else {
//                    button.showScrollPane();
//                    editing=true;
//                }
//            }else if(Slider.class==currentActor.getClass()){
//                //Slider button=((Slider)currentActor);
//                if(editing){
//                    //button.hideScrollPane();
//                    editing=false;
//                }else {
//                    //button.showScrollPane();
//                    editing=true;
//                }
//            }
//        }
//}
////    private void executeButtonListener(EventListener listener){
////        if(ClickListener.class==listener.getClass()){
////            ((ClickListener)listener).clicked(null,0,0);
////        }
////    }
//    private void executeButtonListener2(Actor actor){
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
//
//    public boolean isEditing() {
//        return editing;
//    }
//    public boolean edit(ISGPS5Gamepad pad){
//        if(!editing){return false;}
//        boolean isUp=pad.isUP();
//        boolean isLeft=pad.isLEFT();
//        boolean isRight=pad.isRIGHT();
//        if(SelectBox.class==currentActor.getClass()){
//
//            SelectBox button=((SelectBox)currentActor);
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
//
//        }else if(Slider.class==currentActor.getClass()){
//
//            Slider button=((Slider)currentActor);
//            float step=button.getStepSize();
//            if(isLeft){
//                button.setValue(button.getValue()-step);
//                return true;
//            }else if(isRight){
//                button.setValue(button.getValue()+step);
//                return true;
//            }
////            int boxSize=button.getItems().size;
////            int boxIdx=button.getSelectedIndex();
////
////            if(pad.isUP()||pad.isDOWN()){
////                int y=isUp?-1:1;
////                if(-1==boxIdx){
////                    boxIdx=0;
////                }else{
////                    boxIdx=(boxIdx+y)%boxSize;
////                    if(0>boxIdx){boxIdx+=boxSize;}
////
////                }
////                button.setSelected(button.getItems().get(boxIdx));
////                button.hideScrollPane();
////                button.showScrollPane();
////                return true;
////            }
//
//        }
//        return false;
//    }
//    public void setSkin(Skin skin) {
//        this.skin = skin;
//    }
////    public void setDefaultAction(SGAction<Actor, Object, Object> defaultAction) {
////        this.defaultAction = defaultAction;
////    }
////
////    public void setSelectAction(SGAction<Actor, Object, Object> selectAction) {
////        this.selectAction = selectAction;
////    }
//
//    /**
//     * 旧菜单只有上下移，此类希望实现上下左右（这种方式也有缺点，就是首行最最后一行不容易循环
//     */
//    private static class TabUiItem{
//        public TabUiItem up=null;
//        public TabUiItem down=null;
//    }
//}
