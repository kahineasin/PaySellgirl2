package com.sellgirl.sgGameHelper.tabUi;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.sellgirl.sgGameHelper.tabUi.SGTabUpDownMap.EndToBeginType;

/**
 * 支持上下左右灵活地切换的菜单地图，
 * 缺点在于构造复杂(需要为SGTabUDLRNode建很多临时变量才容易处理)
 * 参照sasha.MainMenuScreen里面的使用
 *
 * 使用方法：
 *
 方法1.
	 SGTabUDLRMap tabMap=new SGTabUDLRMap();
	 ISGTabNode node1=tabMap.newNode(btn1);
	 ISGTabNode node2=tabMap.newNode(btn2);
	 ISGTabNode node3=tabMap.newNode(btn3);
	 node1.setDown(node2);
	 node2.setUp(node1);
	 node2.setDown(node3);
	 node3.setUp(node2);
	 node3.setDown(node1);
	 node1.setUp(node3);
	 tabMap.setFirstNode(node1);
	 tabUi.setTabMap(tabMap);
 方法2.
	 tabMap.generateUDLRConnect(b1,...);
     tabMap.setFirstNode(b1);
 */
public class SGTabUDLRMap implements ISGTabMap{
    SGTabUDLRNode firstNode;
    //其实没必要多个node,从current去倒退回去就行了
//    SGTabUDLRNode endNode;
    SGTabUDLRNode currentNode=null;
    public EndToBeginType endToBegin=EndToBeginType.STAY;

//    @Deprecated
//    public void setItem(Array<Actor> items){
//        boolean first=true;
//        SGTabUDLRNode lastNode=null;
//        for(Actor i:items){
//            if(first){
//                firstNode=new SGTabUDLRNode();
//                firstNode.actor=i;
//                lastNode=firstNode;
//                first=false;
//            }
//            else{
//                SGTabUDLRNode newNode=new SGTabUDLRNode();
//                newNode.actor=i;
//                lastNode.down=newNode;
//                newNode.up=lastNode;
//                lastNode=newNode;
//            }
//        }
//    }
//    @Deprecated
//    public void addItem(Actor item){
//        if(null==this.firstNode){
//            firstNode=new SGTabUDLRNode();
//            firstNode.actor=item;
//        }else{
//
//            SGTabUDLRNode newNode=new SGTabUDLRNode();
//            newNode.actor=item;
//            SGTabUDLRNode lastNode=null;
//            if(null==firstNode.down){
//                lastNode=firstNode;
//            }else {
//                lastNode=firstNode.up;
//            }
//            lastNode.down=newNode;
//            newNode.up=lastNode;
//        }
//    }
    public void setFirstNode(ISGTabNode first){
        firstNode= (SGTabUDLRNode) first;
    }
//    public void setEndNode(ISGTabNode end){
//        endNode= (SGTabUDLRNode) end;
//    }
//    public ISGTabNode getEndNode(){
//        return endNode;
//    }
    public SGTabUDLRNode newNode(Actor actor){
        SGTabUDLRNode n=new SGTabUDLRNode();
        n.setActor(actor);
        return n;
    }

    @Override
    public boolean up(){
        if(null==currentNode){
//            currentNode=null!=endNode?endNode:firstNode;
            currentNode=firstNode;
            while(null!=currentNode.down
            		&&currentNode.down!=firstNode//防止死循环--benjamin20241123
            		) {
            	currentNode=currentNode.down;
            }
            return true;
        }else if(null!=currentNode.up){
            currentNode=currentNode.up;
            return true;
        }
        //这里时，currentNode肯定有值，up无值
        switch(endToBegin) {
	        case STAY:
	        	break;
	        case EMPTY:
	        	currentNode=null;
	        	break;
	        case TO:
	            //currentNode=null!=endNode?endNode:firstNode;
	            while(null!=currentNode.down) {
	            	currentNode=currentNode.down;
	            }
	            return true;
//	        	break;
        	default:
        		break;
        }
        return false;
//        move(-1);
    }
    @Override
    public boolean down(){
        if(null==currentNode){
            currentNode=firstNode;
            return true;
        }else if(null!=currentNode.down){
            currentNode=currentNode.down;
            return true;
        }

        switch(endToBegin) {
	        case STAY:
	        	break;
	        case EMPTY:
	        	currentNode=null;
	        	break;
	        case TO:
//	            currentNode=firstNode;
	            while(null!=currentNode.up) {
	            	currentNode=currentNode.up;
	            }
	            return true;
//	        	break;
        	default:
        		break;
        }
        return false;
    }

    @Override
    public boolean left() {
        if(null==currentNode){
            currentNode=firstNode;
            return true;
        }else if(null!=currentNode.left){
            currentNode=currentNode.left;
            return true;
        }

        switch(endToBegin) {
	        case STAY:
	        	break;
	        case EMPTY:
	        	currentNode=null;
	        	break;
	        case TO:
//	            currentNode=firstNode;
	            while(null!=currentNode.right) {
	            	currentNode=currentNode.right;
	            }
	            return true;
//	        	break;
        	default:
        		break;
        }
        return false;
    }

    @Override
    public boolean right() {
        if(null==currentNode){
            currentNode=firstNode;
            return true;
        }else if(null!=currentNode.right){
            currentNode=currentNode.right;
            return true;
        }
        switch(endToBegin) {
        case STAY:
        	break;
        case EMPTY:
        	currentNode=null;
        	break;
        case TO:
//            currentNode=firstNode;
            while(null!=currentNode.left) {
            	currentNode=currentNode.left;
            }
            return true;
//        	break;
    	default:
    		break;
    }
        return false;
    }

    @Override
    public Object getCurrent() {
        return currentNode==null?null:currentNode.actor;
    }


     /**
     * 旧菜单只有上下移，此类希望实现上下左右（这种方式也有缺点，就是首行最最后一行不容易循环
     * ISGTabMap接口已实现
     */
    public static class SGTabUDLRNode implements ISGTabNode{

         public SGTabUDLRNode up=null;
        public SGTabUDLRNode down=null;
         public SGTabUDLRNode left=null;
         public SGTabUDLRNode right=null;
         public Object actor=null;


         @Override
         public ISGTabNode getUp() {
             return up;
         }

         public void setUp(ISGTabNode up) {
             this.up = (SGTabUDLRNode) up;
         }

         @Override
         public ISGTabNode getDown() {
             return down;
         }

         public void setDown(ISGTabNode down) {
             this.down = (SGTabUDLRNode) down;
         }

         @Override
         public ISGTabNode getLeft() {
             return left;
         }

         public void setLeft(ISGTabNode left) {
             this.left = (SGTabUDLRNode) left;
         }

         @Override
         public ISGTabNode getRight() {
             return right;
         }

         public void setRight(ISGTabNode right) {
             this.right = (SGTabUDLRNode) right;
         }

         public Object getActor() {
             return actor;
         }

         @Override
         public void setActor(Object actor) {
             this.actor = actor;
         }
    }

	/**
	 * 以table方式上下左右连接nodes, 以null为换行标志
	 * 待提取到UDLRMap中通用
	 * @param nodes
	 */
	public void generateUDLRConnect(ISGTabNode... nodes){
		ISGTabNode lastNode=null;//上次左边的节点
		ISGTabNode lastRowNode=null;
		for(ISGTabNode i:nodes){
//			if(null==lastNode&&null!=i){
//				lastNode=i;
//				continue;
//			}else
			if( null!=i){
				boolean lastRowLess=false;
				if(null!=lastNode){
					//有左节点, 左右连接
					if(null!=lastRowNode&&null!=lastRowNode.getRight()){
						lastRowNode=lastRowNode.getRight();
					}else{
						lastRowLess=true;
					}
					lastNode.setRight(i);
					i.setLeft(lastNode);
				}else {
					//行首
				}
				if(null!=lastRowNode){
					//有上行时，上下连接
					if(!lastRowLess) {
						lastRowNode.setDown(i);
					}
					i.setUp(lastRowNode);
					//lastRowNode=lastRowNode.getRight();
				}
				lastNode=i;
			}else if(null==i&&null!=lastNode){
				//null==i为行尾
				while (null!=lastRowNode&&null!=lastRowNode.getRight()){
					//上一行比本行多node时
					lastRowNode.getRight().setDown(lastNode);
					lastRowNode=lastRowNode.getRight();
				}
				lastRowNode=lastNode;
				while (null!=lastRowNode.getLeft()){
					lastRowNode=lastRowNode.getLeft();
				}
				lastNode=null;
			}

		}
		//最后行没有null标记，在这里处理
		while (null!=lastRowNode&&null!=lastRowNode.getRight()){
			//上一行比本行多node时
			lastRowNode.getRight().setDown(lastNode);
			lastRowNode=lastRowNode.getRight();
		}

	}
}
