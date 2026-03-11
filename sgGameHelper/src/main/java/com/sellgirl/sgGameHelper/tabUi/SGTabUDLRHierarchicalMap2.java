package com.sellgirl.sgGameHelper.tabUi;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.sellgirl.sgGameHelper.list.Array2;
//import com.mygdx.game.share.ISGFuture;
//import com.mygdx.game.share.list.Array2;
import com.sellgirl.sgJavaHelper.SGAction1;
import com.sellgirl.sgJavaHelper.SGFunc;

/**
 * 与SGTabUDLRHierarchicalMap的区别，更灵活的地方是：
 * SGTabUDLRNode.tabMap不要求是ISGTabHierarchicalMap，只需是ISGTabMap
 * 应该以Map为主来做递归
 * 此类未完成
 *
 * 支持上下左右灵活地切换的菜单地图，并支持分层结构（暂没使用到）
 *
 * 这个分层有问题，过于依赖 SGTabUDLRNode 类型，所以 SGTabUpDownMap 没有Node类型的似乎用不了
 *
 * 使用方法：
 * 		SGTabUDLRHierarchicalMap2 tabMap=new SGTabUDLRHierarchicalMap2();
 * 		SGTabUpDownMap tabMap2=new SGTabUpDownMap();
 * 		tabMap2.endToBegin= SGTabUpDownMap.EndToBeginType.EMPTY;
 * 		ISGTabNode node1=tabMap.newNode(btn1);
 * 		SGTabUDLRHierarchicalMap2.SGTabUDLRNode node2=new SGTabUDLRHierarchicalMap2.SGTabUDLRNode();
 * 		tabMap2.addItem(btn2);
 * 		tabMap2.addItem(btn3);
 * 		node2.tabMap=tabMap2;
 * 		node1.setDown(node2);
 * 		node2.setUp(node1);
 * 		node2.setDown(node1);
 * 		node1.setUp(node2);
 * 		tabMap.setFirstNode(node1);
 * 		tabUi.setTabMap(tabMap);
 */
//@ISGFuture
//@Deprecated
public class SGTabUDLRHierarchicalMap2
        implements ISGTabHierarchicalMap
{

    SGFunc<ISGTabHierarchicalNode, Object, Object, ISGTabHierarchicalNode> upF=new SGFunc<ISGTabHierarchicalNode, Object, Object, ISGTabHierarchicalNode>() {
        @Override
        public ISGTabHierarchicalNode go(ISGTabHierarchicalNode sgTabUDLRNode, Object o, Object o2) {
            return sgTabUDLRNode.getUp();
        }
    };
    SGFunc<ISGTabHierarchicalNode, Object, Object, ISGTabHierarchicalNode> downF=new SGFunc<ISGTabHierarchicalNode, Object, Object, ISGTabHierarchicalNode>() {
        @Override
        public ISGTabHierarchicalNode go(ISGTabHierarchicalNode sgTabUDLRNode, Object o, Object o2) {
            return sgTabUDLRNode.getDown();
        }
    };
    SGFunc<ISGTabHierarchicalNode, Object, Object, ISGTabHierarchicalNode> leftF=new SGFunc<ISGTabHierarchicalNode, Object, Object, ISGTabHierarchicalNode>() {
        @Override
        public ISGTabHierarchicalNode go(ISGTabHierarchicalNode sgTabUDLRNode, Object o, Object o2) {
            return sgTabUDLRNode.getLeft();
        }
    };
    SGFunc<ISGTabHierarchicalNode, Object, Object, ISGTabHierarchicalNode> rightF=new SGFunc<ISGTabHierarchicalNode, Object, Object, ISGTabHierarchicalNode>() {
        @Override
        public ISGTabHierarchicalNode go(ISGTabHierarchicalNode sgTabUDLRNode, Object o, Object o2) {
            return sgTabUDLRNode.getRight();
        }
    };

    SGFunc<ISGTabMap, Object, Object, Boolean> upMapF=new SGFunc<ISGTabMap, Object, Object, Boolean>() {
        @Override
        public Boolean go(ISGTabMap sgTabUDLRNode, Object o, Object o2) {
            return sgTabUDLRNode.up();
        }
    };
    SGFunc<ISGTabMap, Object, Object, Boolean> downMapF=new SGFunc<ISGTabMap, Object, Object, Boolean>() {
        @Override
        public Boolean go(ISGTabMap sgTabUDLRNode, Object o, Object o2) {
            return sgTabUDLRNode.down();
        }
    };
    SGFunc<ISGTabMap, Object, Object, Boolean> leftMapF=new SGFunc<ISGTabMap, Object, Object, Boolean>() {
        @Override
        public Boolean go(ISGTabMap sgTabUDLRNode, Object o, Object o2) {
            return sgTabUDLRNode.left();
        }
    };
    SGFunc<ISGTabMap, Object, Object, Boolean> rightMapF=new SGFunc<ISGTabMap, Object, Object, Boolean>() {
        @Override
        public Boolean go(ISGTabMap sgTabUDLRNode, Object o, Object o2) {
            return sgTabUDLRNode.right();
        }
    };

    SGTabUDLRNode firstNode;
    /**
     * 本层的当前节点
     *
     */
    ISGTabHierarchicalNode currentNode=null;
    /**
     * 最上层保存的当前叶节点（可能非本层，用于返回）(子层的此属性保持null即可)
     */
    ISGTabHierarchicalNode currentLeafNode=null;
    //Object currentLeafNode=null;
    /**
     * 由于 ISGTabMap
     */
    ISGTabMap currentLeafMap=null;
    //SGTabUDLRNode parent=null;
    /**
     * 感觉保存parentMap更便于递归查找
     */
    SGTabUDLRHierarchicalMap2 parentMap=null;

    public void setFirstNode(ISGTabNode first){
        firstNode= (SGTabUDLRNode) first;
    }
    public SGTabUDLRNode newNode(Actor actor){
        SGTabUDLRNode n=new SGTabUDLRNode();
        n.setActor(actor);
        return n;
    }

    public boolean isTopLevel(){
        return null==parentMap;
    }
    public ISGTabHierarchicalMap getParentMap(){
        return parentMap;
    }
    public void setCurrentNode(ISGTabHierarchicalNode currentNode){
        this.currentNode=currentNode;
    }


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
//            currentLeafNode=dir1.go(currentLeafNode,null,null).findLeaf();
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
//                    currentLeafNode=tmpMap.getCurrentNode().findLeaf();
//                    break;
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

    /**
     * 当末层不是this时, 就要保存上层和内层之间的结构
     * 这此属性，就不要求ISGTabMap保存parent的指针
     */
    Array2<ISGTabMap> leafMaps=new Array2<>();
    /**
     * 分层的原则是(假设置往左find)：
     * 1.current一定是actor leaf节点
     * 2.如果current是本层最后一个，返回parent的left
     *   如果parent.left是leaf, 就返回
     *   注意如果往上层找没有leaf时，当前层不要移动，保持当前就好了
     * 3.要有个flag表示在找的过程中，是否已经move 1次了
     * 4.如果是往parent找，可能为null, 就不移动
     *   只要left有值, 那么必然有值
     */
    @Override
    public boolean up(){
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
////        move(upF);
//
//        if(null==currentNode){
//            currentNode=firstNode;
//            currentLeafMap=currentNode.findLeaf().getParent();
//            return true;
//        }
////        else if(null!=currentNode){
////            ISGTabHierarchicalNode tmpNode=currentNode;
////            ISGTabHierarchicalMap tmpMap=currentNode.getParent();
////            while (!tmpNode.isLeaf()){
////                tmpNode=tmpNode.ma
////            }
////        }
////        else if(null!=currentNode.getUp()){
////            currentNode=currentNode.getUp();
////            return true;
////        }
//        else if(null!=currentLeafMap){
//            if(currentLeafMap.up()){
//                if(SGTabUDLRHierarchicalMap2.class==currentLeafMap.getClass()){
//                    currentLeafMap=((SGTabUDLRHierarchicalMap2)currentLeafMap).getCurrentNode().findLeaf().getParent();
//                    return true;
//                }else{
//                    return true;
//                }
//            }
//        }
//        else if(SGTabUDLRHierarchicalMap2.class==currentLeafMap.getClass()
//                &&!((SGTabUDLRHierarchicalMap2)currentLeafMap).isTopLevel()){
//            ISGTabHierarchicalMap tmpMap=((SGTabUDLRHierarchicalMap2)currentLeafMap).getParentMap();
//            while (null!=tmpMap){
//                if(null==tmpMap.getCurrentNode()){
//                    tmpMap.down();
//                }
//                if(
//                    null!=tmpMap.getCurrentNode().getUp()
//                    //    null!=dir1.go(tmpMap.getCurrentNode(),null,null)
//                ){
//                    //必然有值
//                    //                currentLeafNode=tmpMap.getCurrentNode().up.findLeaf();
//                    tmpMap.setCurrentNode(tmpMap.getCurrentNode().getUp());
//                    //tmpMap.setCurrentNode(dir1.go(tmpMap.getCurrentNode(),null,null));
//                    currentLeafNode=tmpMap.getCurrentNode().findLeaf();
//                    return true;
////                    break;
//                }else{
//                    tmpMap=tmpMap.getParentMap();
//                }
//            }
//        }
////        else if(currentLeafMap==this){
//////            if(null==currentNode){
//////                currentNode=firstNode;
//////                return true;
//////            }else if(null!=currentNode.getUp()){
//////                currentNode=currentNode.getUp();
//////                return true;
//////            }
////
////            if(currentLeafMap.up()){
////                if(SGTabUDLRHierarchicalMap2.class==currentLeafMap.getClass()){
////                    currentLeafMap=((SGTabUDLRHierarchicalMap2)currentLeafMap).getCurrentNode().findLeaf().getParent();
////                }else{
////                    return true;
////                }
////            }
////        }else{
////            boolean success=false;
////            ISGTabMap tmpMap=currentLeafMap;
////            success=tmpMap.up();
////            while (!success){
////
////            }
////            if(currentLeafMap.up()){
////                if(SGTabUDLRHierarchicalMap2.class==currentLeafMap.getClass()){
////                    currentLeafMap=((SGTabUDLRHierarchicalMap2)currentLeafMap).getCurrentNode().findLeaf().getParent();
////                }else{
////                    return true;
////                }
////            }
////        }
//        return false;
        return move(upF,upMapF);
    }

    public boolean doDown(){
        //doUp对应普通ISGTabMap.up()
        //所以此方法只考虑本层map, 不处理其它层，这样也变于给其它地方调用

        if(null==currentNode){
            currentNode=firstNode;
            return true;
        }else if(null!=currentNode.getDown()){
            currentNode=currentNode.getDown();
            return true;
        }
        return false;
    }

    public boolean doMove(SGFunc<ISGTabHierarchicalNode,Object,Object,ISGTabHierarchicalNode>
                          dir1){
        //doUp对应普通ISGTabMap.up()
        //所以此方法只考虑本层map, 不处理其它层，这样也变于给其它地方调用

        if(null==currentNode){
            currentNode=firstNode;
            return true;
        }else if(null!=dir1.go(currentNode,null,null)){
            currentNode=dir1.go(currentNode,null,null);
            return true;
        }
        return false;
    }

    @Override
    public boolean down(){
//        if(leafMaps.isEmpty()){
////            boolean b=doDown();
//            boolean b=doMove(downF);
//            if(b){
//                if(this.getCurrentNode().isLeaf()){
//
//                }else{
//                    ISGTabMap tabMap= this.getCurrentNode().getTabMap();
//                    boolean b2=tabMap.down();
//                    if(b2){
//                        this.leafMaps.add(tabMap);
//                    }
//                }
//            }
//            return true;
//        }else{
//            //从内到外
//            for(int i=leafMaps.size-1;0<=i;i--){
//                ISGTabMap tabMap=leafMaps.get(i);
//                boolean b=tabMap.down();
//                if(b){
//                    return true;
//                }else{
//                    leafMaps.removeIndex(i);
//                }
//            }
////            boolean b=doDown();
//            boolean b=doMove(downF);
//            if(b){return true;}
//        }
//        return false;
        return move(downF,downMapF);
    }
    public boolean move(SGFunc<ISGTabHierarchicalNode,Object,Object,ISGTabHierarchicalNode>
                                dir1,
                        SGFunc<ISGTabMap,Object,Object,Boolean> dir2){
        if(leafMaps.isEmpty()){
            //当前是最外层时
//            boolean b=doDown();
            boolean b=doMove(dir1);
            if(b){
                if(this.getCurrentNode().isLeaf()){

                }else{
                    ISGTabMap tabMap= this.getCurrentNode().getTabMap();
//                    boolean b2=tabMap.down();
                    boolean b2=dir2.go( tabMap,null,null);
                    if(b2){
                        this.leafMaps.add(tabMap);
                    }
                }
            }
            return true;
        }else{
            //从内到外
            for(int i=leafMaps.size-1;0<=i;i--){
                ISGTabMap tabMap=leafMaps.get(i);
                //boolean b=tabMap.down();
                boolean b=dir2.go( tabMap,null,null);
                if(b){
                    return true;
                }else{
                    leafMaps.removeIndex(i);
                }
            }
//            boolean b=doDown();
            boolean b=doMove(dir1);
            if(b){return true;}
        }
        return false;
        //return move(downF);
    }

    @Override
    public boolean left() {
//        return false;
//        return move(leftF);
        return move(leftF,leftMapF);
    }

    @Override
    public boolean right() {

        //return false;//return move(rightF);
        return move(rightF,rightMapF);
    }

    @Override
    public Object getCurrent() {
        if(leafMaps.isEmpty()){
            return currentNode==null?null:currentNode.getActor();
        }
        return leafMaps.get(leafMaps.size-1).getCurrent();
//        //return currentNode==null?null:currentNode.actor;
//        if(currentLeafNode==null){return null;}
//        if(SGTabUDLRNode.class==currentLeafNode.getClass()){
//            return ((SGTabUDLRNode)currentLeafNode).actor;
//        }
//        return currentLeafNode;
    }
    public ISGTabHierarchicalNode getCurrentNode() {
        return currentNode;
    }


     /**
     * 旧菜单只有上下移，此类希望实现上下左右（这种方式也有缺点，就是首行最最后一行不容易循环
     * ISGTabMap接口已实现
     */
    public static class SGTabUDLRNode implements ISGTabHierarchicalNode{
        public SGTabUDLRNode up=null;
        public SGTabUDLRNode down=null;
         public SGTabUDLRNode left=null;
         public SGTabUDLRNode right=null;
         public Object actor=null;
         /**
          * node的内层map
          * 感觉改 ISGTabMap 类型就更通用
          */
//         public ISGTabHierarchicalMap tabMap=null;
         public ISGTabMap tabMap=null;
         /**
          * node的所属map,非空
          */
         public ISGTabHierarchicalMap parent=null;
         public  boolean isLeaf(){
             return null!=actor;
         }
         public ISGTabHierarchicalMap getParent(){
             return parent;
         }
         public ISGTabMap getTabMap(){
             return tabMap;
         }
         public Object getActor(){
             return actor;
         }
         public ISGTabHierarchicalNode getUp() {
             return up;
         }
         public ISGTabHierarchicalNode getDown() {
             return down;
         }
         public  ISGTabHierarchicalNode getLeft() {
             return left;
         }
         public ISGTabHierarchicalNode getRight() {
             return right;
         }

         @Override
         public void setActor(Object n) {
             actor=n;
         }

         @Override
         public void setUp(ISGTabNode n) {
            up= (SGTabUDLRNode) n;
         }

         @Override
         public void setDown(ISGTabNode n) {
            down= (SGTabUDLRNode) n;
         }

         @Override
         public void setLeft(ISGTabNode n) {
             left= (SGTabUDLRNode) n;
         }

         @Override
         public void setRight(ISGTabNode n) {
            right= (SGTabUDLRNode) n;
         }
//         public ISGTabHierarchicalNode findLeaf(){
////             if(isLeaf()){return this;}
////             if(null!=tabMap){
////                 if (null == tabMap.getCurrentNode()) {
////                     tabMap.down();
////                 }
////                 return tabMap.getCurrentNode().findLeaf();
//////                 if(SGTabUDLRHierarchicalMap.class==tabMap.getClass()) {
//////                     SGTabUDLRHierarchicalMap tmpMap= (SGTabUDLRHierarchicalMap) tabMap;
//////                     if (null == tmpMap.getCurrentNode()) {
//////                         tabMap.down();
//////                     }
//////                     return tmpMap.getCurrentNode().findLeaf();
//////                 }else{
//////                     if (null == tabMap.getCurrent()) {
//////                         tabMap.down();
//////                     }
//////                     return tabMap.getCurrent();
//////                 }
////             }
////             //正常不可能null
//             return null;
//         }
//         /**
//          *
//          * @deprecated 此方法应该在 SGTabUDLRHierarchicalMap 实现才合适
//          * @return
//          */
//         @Deprecated
//         public SGTabUDLRNode findLeft(){
//            if(null!=left){
//                if(null!=left.actor){
//                    return left;
//                }else if(null!=left.tabMap){
//                    if(null==left.tabMap.getCurrent()){
//                        left.tabMap.left();
//                        return left.tabMap.getCurrentNode();
//                    }else{
//                        if(null!=left.tabMap.getCurrentNode().left){
//                            return left.tabMap.getCurrentNode().left;
//                        }
//                    }
//                }
//            }
//            if(null!=parent){
//                parent.left();
//                return parent.getCurrentNode();
//            }
//            return null;
//         }
    }
//    public SGTabUDLRNode getParent(){
//        return parent;
//    }
}
