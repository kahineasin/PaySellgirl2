package com.sellgirl.sgGameHelper.tabUi;

import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.mygdx.game.share.ISGFuture;

/**
 * 分层的map
 */
public  interface ISGTabNode {
//    boolean isLeaf();
//    //ISGTabHierarchicalNode findLeaf();
//    ISGTabHierarchicalMap getParent();

    /**
     * 用object类型是为了在其它情况下通用
     * @return
     */
    Object getActor();
    ISGTabNode getUp();
    ISGTabNode getDown();
    ISGTabNode getLeft();
    ISGTabNode getRight();
    void setActor(Object actor);
    void setUp(ISGTabNode n);
    void setDown(ISGTabNode n);
    void setLeft(ISGTabNode n);
    void setRight(ISGTabNode n);
}