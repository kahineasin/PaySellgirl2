package com.sellgirl.sgGameHelper.tabUi;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;

/**
 * 只支持上下切换的菜单地图，优点在于构造简单
 */
public class SGTabUpDownMap implements ISGTabMap{
    private int size=0;
    private int current=-1;
    private Array<Actor> items;
    Actor currentActor=null;
    /**
     * 首尾相接
     * 当嵌套在ISGTabHierarchicalMap 的内层使用时，需要设置为false, 这样内层end后才能回到外层
     */
    public EndToBeginType endToBegin=EndToBeginType.TO;
    public enum EndToBeginType{
        /**
         * 边缘时不动
         */
        STAY,
        /**
         * 边缘时清空(作为多层嵌套的内层使用时，多用此方式)
         */
        EMPTY,
        /**
         * 首尾相接
         */
        TO
    }

    public void setItem(Array<Actor> items){
        this.items=items;
        size=items.size;
    }
    public void addItem(Actor item){
        if(null==this.items){
            items=new Array<>();
        }
        items.add(item);
        size++;
    }

    @Override
    public boolean up(){
        return move(-1);
    }
    @Override
    public boolean down(){
        return move(1);
    }

    @Override
    public boolean left() {
        return false;
    }

    @Override
    public boolean right() {
        return false;
    }

    @Override
    public Object getCurrent() {
        return currentActor;
    }

    public boolean move(int y){
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
        boolean result=false;
        if(-1==current){
//            current=0;
//            result=true;

            if(0<y){
                current=0;
            }else if(0>y){
                current=size-1;
            }
            result=true;
        }else{
            //旧版本循环时返回true, 多层嵌套时就不知道结束没有
//            current=(current+y)%size;
//            if( 0>current){current+=size;}
//            result=true;

            switch (endToBegin) {
                case TO:
                    //旧版本循环时返回true, 多层嵌套时就不知道结束没有
                    current=(current+y)%size;
                    if( 0>current){current+=size;}
                    result=true;
                    break;
                case STAY:
                    current=(current+y);
                    if(0>current||size-1<current){
                        result=false;
                        current=current%size;
                        if( 0>current){current+=size;}
                    }else{
                        result=true;
                    }
                    break;
                case EMPTY:
                    current=(current+y);
                    if(0>current||size-1<current){
                        current=-1;
                        return false;
                    }else{
                        result=true;
                    }
                    break;
                default:
                    break;
            }
//            current=(current+y)%size;
//            if( 0>current){
//                if(endToBegin) {
//                    current += size;
//                }else {
//                    return false;
//                }
//            }else if(size-1< current){
//
//            }
//            result=true;
        }
        currentActor=items.get(current);
        return result;
        //currentActor.setColor(Color.RED);
//        selectAction.go(currentActor,null,null);


    }
}
