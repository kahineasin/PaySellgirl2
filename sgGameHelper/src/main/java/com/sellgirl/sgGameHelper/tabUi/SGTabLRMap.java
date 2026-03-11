package com.sellgirl.sgGameHelper.tabUi;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

/**
 * 只支持左右切换的菜单地图，优点在于构造简单
 */
public class SGTabLRMap implements ISGTabMap{
    private int size=0;
    private int current=-1;
    private Array<Actor> items;
    Actor currentActor=null;
    /**
     * 首尾相接
     * 当嵌套在ISGTabHierarchicalMap 的内层使用时，需要设置为false, 这样内层end后才能回到外层
     */
    public SGTabUpDownMap.EndToBeginType endToBegin= SGTabUpDownMap.EndToBeginType.TO;
//    public enum EndToBeginType{
//        /**
//         * 边缘时不动
//         */
//        STAY,
//        /**
//         * 边缘时清空(作为多层嵌套的内层使用时，多用此方式)
//         */
//        EMPTY,
//        /**
//         * 首尾相接
//         */
//        TO
//    }

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
        //return false;
        //return move(-1);

        switch (endToBegin) {
            case TO:
                break;
            case STAY:
                break;
            case EMPTY:
                if(-1==current){
                    return move(-1);
                }else{
                    current=-1;
                    return false;
                }
                //break;
            default:
                break;
        }
        return false;
    }
    @Override
    public boolean down(){
        //return false;
        //return move(1);
//        return move(1);

        switch (endToBegin) {
            case TO:
                break;
            case STAY:
                break;
            case EMPTY:
                if(-1==current){
                    return move(1);
                }else{
                    current=-1;
                    return false;
                }
                //break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean left() {
        //return false;

        return move(-1);
    }

    @Override
    public boolean right() {

//        return false;
        return move(1);
    }

    @Override
    public Object getCurrent() {
        return currentActor;
    }

    public boolean move(int x){
        boolean result=false;
        if(-1==current){
//            current=0;
//            result=true;

            if(0<x){
                current=0;
            }else if(0>x){
                current=size-1;
            }
            result=true;
        }else{
            switch (endToBegin) {
                case TO:
                    //旧版本循环时返回true, 多层嵌套时就不知道结束没有
                    current=(current+x)%size;
                    if( 0>current){current+=size;}
                    result=true;
                    break;
                case STAY:
                    current=(current+x);
                    if(0>current||size-1<current){
                        result=false;
                        current=current%size;
                        if( 0>current){current+=size;}
                    }else{
                        result=true;
                    }
                    break;
                case EMPTY:
                    current=(current+x);
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
        }
        currentActor=items.get(current);
        return result;
    }
}
