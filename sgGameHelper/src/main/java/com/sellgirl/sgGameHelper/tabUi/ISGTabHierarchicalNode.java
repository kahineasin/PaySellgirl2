package com.sellgirl.sgGameHelper.tabUi;

//import com.mygdx.game.share.ISGFuture;

/**
 * 分层的map(暂未使用)
 */
public  interface ISGTabHierarchicalNode extends ISGTabNode {
    boolean isLeaf();
    /**
     * 内部map
     * @return
     */
    ISGTabMap getTabMap();
    //ISGTabHierarchicalNode findLeaf();
    ISGTabHierarchicalMap getParent();
    ISGTabHierarchicalNode getUp();
    ISGTabHierarchicalNode getDown();
    ISGTabHierarchicalNode getLeft();
    ISGTabHierarchicalNode getRight();
}