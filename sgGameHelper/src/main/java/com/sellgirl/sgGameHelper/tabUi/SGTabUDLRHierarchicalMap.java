//package com.mygdx.game.share.tabUi;
//
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.mygdx.game.share.ISGFuture;
//import com.sellgirl.sgJavaHelper.SGAction1;
//import com.sellgirl.sgJavaHelper.SGFunc;
//
///**
// * 支持上下左右灵活地切换的菜单地图，并支持分层结构（暂没使用到）
// *
// * 这个分层有问题，过于依赖 SGTabUDLRNode 类型，SGTabUDLRNode.tabMap限制必需是ISGTabHierarchicalMap
// * 所以 SGTabUpDownMap 没有Node类型的似乎用不了
// * 因为要通过Actor找它的parentMap
// *
// * 后来发现，如果要抛弃 SGTabUDLRNode，那 ISGTabMap 必需 有parent属性，才可以往上级找
// */
//@Deprecated
//public class SGTabUDLRHierarchicalMap
//        implements ISGTabHierarchicalMap
//{
//
//    SGFunc<ISGTabHierarchicalNode, Object, Object, ISGTabHierarchicalNode> upF=new SGFunc<ISGTabHierarchicalNode, Object, Object, ISGTabHierarchicalNode>() {
//        @Override
//        public ISGTabHierarchicalNode go(ISGTabHierarchicalNode sgTabUDLRNode, Object o, Object o2) {
//            return sgTabUDLRNode.getUp();
//        }
//    };
//    SGFunc<ISGTabHierarchicalNode, Object, Object, ISGTabHierarchicalNode> downF=new SGFunc<ISGTabHierarchicalNode, Object, Object, ISGTabHierarchicalNode>() {
//        @Override
//        public ISGTabHierarchicalNode go(ISGTabHierarchicalNode sgTabUDLRNode, Object o, Object o2) {
//            return sgTabUDLRNode.getDown();
//        }
//    };
//    SGFunc<ISGTabHierarchicalNode, Object, Object, ISGTabHierarchicalNode> leftF=new SGFunc<ISGTabHierarchicalNode, Object, Object, ISGTabHierarchicalNode>() {
//        @Override
//        public ISGTabHierarchicalNode go(ISGTabHierarchicalNode sgTabUDLRNode, Object o, Object o2) {
//            return sgTabUDLRNode.getLeft();
//        }
//    };
//    SGFunc<ISGTabHierarchicalNode, Object, Object, ISGTabHierarchicalNode> rightF=new SGFunc<ISGTabHierarchicalNode, Object, Object, ISGTabHierarchicalNode>() {
//        @Override
//        public ISGTabHierarchicalNode go(ISGTabHierarchicalNode sgTabUDLRNode, Object o, Object o2) {
//            return sgTabUDLRNode.getRight();
//        }
//    };
//
//    SGTabUDLRNode firstNode;
//    /**
//     * 本层的当前节点
//     *
//     */
//    ISGTabHierarchicalNode currentNode=null;
//    /**
//     * 最上层保存的当前叶节点（可能非本层，用于返回）(子层的此属性保持null即可)
//     */
//    ISGTabHierarchicalNode currentLeafNode=null;
//    //Object currentLeafNode=null;
//    /**
//     * 由于 ISGTabMap
//     */
//    ISGTabMap currentLeafMap=null;
//    //SGTabUDLRNode parent=null;
//    /**
//     * 感觉保存parentMap更便于递归查找
//     */
//    SGTabUDLRHierarchicalMap parentMap=null;
//
//    public void setFirstNode(SGTabUDLRNode first){
//        firstNode=first;
//    }
//
//    public boolean isTopLevel(){
//        return null==parentMap;
//    }
//    public ISGTabHierarchicalMap getParentMap(){
//        return parentMap;
//    }
//    public void setCurrentNode(ISGTabHierarchicalNode currentNode){
//        this.currentNode=currentNode;
//    }
//
//
//    /**
//     * 非递归
//     * @param dir1
//     */
//    public boolean move(
//            SGFunc<ISGTabHierarchicalNode,Object,Object,ISGTabHierarchicalNode> dir1){
//        if(null==currentNode){
//            //必然有值
//            //currentNode=firstNode.findLeaf();
//            currentNode=firstNode;
//            currentLeafNode=firstNode.findLeaf();
//            return true;
////            if(firstNode.isLeaf()){
////                return;
////            }
//        }else if(
////                null!=currentLeafNode.up
//                null!=dir1.go(currentLeafNode,null,null)
//        ){
//            //必然有值
////            //currentNode=currentLeafNode.up;
////            currentLeafNode=currentLeafNode.up.findLeaf();
//            currentLeafNode=((SGTabUDLRNode)dir1.go(currentLeafNode,null,null)).findLeaf();
//            return true;
//        }else if(!currentLeafNode.getParent().isTopLevel()){
//            ISGTabHierarchicalMap tmpMap=currentLeafNode.getParent().getParentMap();
//            while (null!=tmpMap){
//                if(null==tmpMap.getCurrentNode()){
//                    tmpMap.down();
//                }
//                if(
//                    //null!=tmpMap.getCurrentNode().up
//                        null!=dir1.go(tmpMap.getCurrentNode(),null,null)
//                ){
//                    //必然有值
//                    //                currentLeafNode=tmpMap.getCurrentNode().up.findLeaf();
////                    tmpMap.setCurrentNode(tmpMap.getCurrentNode().up);
//                    tmpMap.setCurrentNode(dir1.go(tmpMap.getCurrentNode(),null,null));
//                    currentLeafNode=((SGTabUDLRNode)tmpMap.getCurrentNode()).findLeaf();
//                    return true;
////                    break;
//                }else{
//                    tmpMap=tmpMap.getParentMap();
//                }
//            }
//        }
//        return false;
////old
////        if(null==currentNode){
////            currentNode=firstNode;
////        }else if(null!=currentNode.up){
////            currentNode=currentNode.up;
////        }
//    }
//
//    /**
//     * 分层的原则是(假设置往左find)：
//     * 1.current一定是actor leaf节点
//     * 2.如果current是本层最后一个，返回parent的left
//     *   如果parent.left是leaf, 就返回
//     *   注意如果往上层找没有leaf时，当前层不要移动，保持当前就好了
//     * 3.要有个flag表示在找的过程中，是否已经move 1次了
//     * 4.如果是往parent找，可能为null, 就不移动
//     *   只要left有值, 那么必然有值
//     */
//    @Override
//    public boolean up(){
////        if(null==currentNode){
////            //必然有值,递归第1次
////            //currentNode=firstNode.findLeaf();
////            currentNode=firstNode;
////            currentLeafNode=firstNode.findLeaf();
//////            if(firstNode.isLeaf()){
//////                return;
//////            }
////        }else if(null!=currentLeafNode.up){
////            //必然有值,递归第1次
////            //currentNode=currentLeafNode.up;
////            currentLeafNode=currentLeafNode.up.findLeaf();
////        }else if(!currentLeafNode.parent.isTopLevel()){
////            ISGTabHierarchicalMap tmpMap=currentLeafNode.parent.getParentMap();
////            while (null!=tmpMap){
////                if(null==tmpMap.getCurrentNode()){
////                    tmpMap.down();
////                }
////                if(null!=tmpMap.getCurrentNode().up){
////                    //必然有值
////    //                currentLeafNode=tmpMap.getCurrentNode().up.findLeaf();
////                    tmpMap.setCurrentNode(tmpMap.getCurrentNode().up);
////                    currentLeafNode=tmpMap.getCurrentNode().findLeaf();
////                    break;
////                }else{
////                    tmpMap=tmpMap.getParentMap();
////                }
////            }
////        }
//
//        return move(upF);
//    }
//
//
//    @Override
//    public boolean down(){
//        return move(downF);
//    }
//
//    @Override
//    public boolean left() {
//
//        return move(leftF);
//    }
//
//    @Override
//    public boolean right() {
//        return move(rightF);
//    }
//
//    @Override
//    public Object getCurrent() {
//        //return currentNode==null?null:currentNode.actor;
//        if(currentLeafNode==null){return null;}
//        if(SGTabUDLRNode.class==currentLeafNode.getClass()){
//            return ((SGTabUDLRNode)currentLeafNode).actor;
//        }
//        return currentLeafNode;
//    }
//    public ISGTabHierarchicalNode getCurrentNode() {
//        return currentNode;
//    }
//
//
//     /**
//     * 旧菜单只有上下移，此类希望实现上下左右（这种方式也有缺点，就是首行最最后一行不容易循环
//     * ISGTabMap接口已实现
//     */
//    public static class SGTabUDLRNode implements ISGTabHierarchicalNode{
//        public SGTabUDLRNode up=null;
//        public SGTabUDLRNode down=null;
//         public SGTabUDLRNode left=null;
//         public SGTabUDLRNode right=null;
//         public Object actor=null;
//         /**
//          * node的内层map
//          * 感觉改 ISGTabMap 类型就更通用
//          */
//         public ISGTabHierarchicalMap tabMap=null;
//         //public ISGTabMap tabMap=null;
//         /**
//          * node的所属map,非空
//          */
//         public ISGTabHierarchicalMap parent=null;
//         public  boolean isLeaf(){
//             return null!=actor;
//         }
//         public ISGTabHierarchicalMap getParent(){
//             return parent;
//         }
//         public Object getActor(){
//             return actor;
//         }
//         public ISGTabMap getTabMap(){
//             return tabMap;
//         }
//         public ISGTabHierarchicalNode getUp() {
//             return up;
//         }
//         public ISGTabHierarchicalNode getDown() {
//             return down;
//         }
//         public  ISGTabHierarchicalNode getLeft() {
//             return left;
//         }
//         public ISGTabHierarchicalNode getRight() {
//             return right;
//         }
//         @Override
//         public void setActor(Object n) {
//             actor=n;
//         }
//
//         @Override
//         public void setUp(ISGTabNode n) {
//             up= (SGTabUDLRNode) n;
//         }
//
//         @Override
//         public void setDown(ISGTabNode n) {
//             down= (SGTabUDLRNode) n;
//         }
//
//         @Override
//         public void setLeft(ISGTabNode n) {
//             left= (SGTabUDLRNode) n;
//         }
//
//         @Override
//         public void setRight(ISGTabNode n) {
//             right= (SGTabUDLRNode) n;
//         }
//         public ISGTabHierarchicalNode findLeaf(){
//             if(isLeaf()){return this;}
//             if(null!=tabMap){
//                 if (null == tabMap.getCurrentNode()) {
//                     tabMap.down();
//                 }
//                 return ((SGTabUDLRNode)tabMap.getCurrentNode()).findLeaf();
////                 if(SGTabUDLRHierarchicalMap.class==tabMap.getClass()) {
////                     SGTabUDLRHierarchicalMap tmpMap= (SGTabUDLRHierarchicalMap) tabMap;
////                     if (null == tmpMap.getCurrentNode()) {
////                         tabMap.down();
////                     }
////                     return tmpMap.getCurrentNode().findLeaf();
////                 }else{
////                     if (null == tabMap.getCurrent()) {
////                         tabMap.down();
////                     }
////                     return tabMap.getCurrent();
////                 }
//             }
//             //正常不可能null
//             return null;
//         }
////         /**
////          *
////          * @deprecated 此方法应该在 SGTabUDLRHierarchicalMap 实现才合适
////          * @return
////          */
////         @Deprecated
////         public SGTabUDLRNode findLeft(){
////            if(null!=left){
////                if(null!=left.actor){
////                    return left;
////                }else if(null!=left.tabMap){
////                    if(null==left.tabMap.getCurrent()){
////                        left.tabMap.left();
////                        return left.tabMap.getCurrentNode();
////                    }else{
////                        if(null!=left.tabMap.getCurrentNode().left){
////                            return left.tabMap.getCurrentNode().left;
////                        }
////                    }
////                }
////            }
////            if(null!=parent){
////                parent.left();
////                return parent.getCurrentNode();
////            }
////            return null;
////         }
//    }
////    public SGTabUDLRNode getParent(){
////        return parent;
////    }
//}
