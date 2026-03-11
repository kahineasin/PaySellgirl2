package com.sellgirl.sgGameHelper.tabUi;

//import com.mygdx.game.share.ISGFuture;

/**
 * 分层的map(暂未使用)
 */
//@ISGFuture
//@Deprecated
public  interface ISGTabHierarchicalMap extends ISGTabMap{
    boolean isTopLevel();
    //SGTabUDLRHierarchicalMap.SGTabUDLRNode getParent();
    ISGTabHierarchicalMap getParentMap();

    ISGTabHierarchicalNode getCurrentNode();
    void setCurrentNode(ISGTabHierarchicalNode currentNode);
//    SGTabUDLRHierarchicalMap.SGTabUDLRNode getCurrentLeafNode();
}