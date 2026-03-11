package org.sgGameHelper.btree;

import org.sgGameHelper.btree.model.ISGManActorD3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.Task.Status;
import com.badlogic.gdx.ai.btree.branch.Selector;
import com.sellgirl.sgGameHelper.gamepad.XBoxKey;
import org.sgGameHelper.btree.model.AttackTask;
import org.sgGameHelper.btree.model.FindPathFollowTask;
import org.sgGameHelper.btree.model.GiantActorD3;
import org.sgGameHelper.btree.model.WalkTask;

public class UncheckBtree001  {
	public static void main(String[] args) {
		testBtree3();
	}
	/**
	 * 第1个任务就成功时
----step1----
AttackTask SUCCEEDED delta:0.016666668
----step2----
AttackTask SUCCEEDED delta:0.016666668
----end----
	 */
	public static void testBtree() {
		//1
		GdxAI.getTimepiece().update(1f/60f);
		
		//2
        Selector<ISGManActorD3> root = new Selector<ISGManActorD3>();
        root.addChild(new AttackTask());
        FindPathFollowTask findPathFollowTask=new FindPathFollowTask();
        root.addChild(findPathFollowTask);
        root.addChild(new WalkTask());
        BehaviorTree bTree = new BehaviorTree<ISGManActorD3>(root,new GiantActorD3());

        //3
		System.out.println("----step1----");
        bTree.step();
		System.out.println("----step2----");
        bTree.step();
        

		System.out.println("----end----");
	}

	/**
	 * 第1个任务失败时(可以看出，每次step都是从root开始的)
----step1----
AttackTask FAILED delta:0.016666668
FindPathFollowTask SUCCEEDED delta:0.016666668
----step2----
AttackTask FAILED delta:0.016666668
FindPathFollowTask SUCCEEDED delta:0.016666668
----end----
	 */
	public static void testBtree2() {
		//1
		GdxAI.getTimepiece().update(1f/60f);
		
		//2
        Selector<ISGManActorD3> root = new Selector<ISGManActorD3>();
        AttackTask attackTask=new AttackTask();
        root.addChild(attackTask);
        FindPathFollowTask findPathFollowTask=new FindPathFollowTask();
        root.addChild(findPathFollowTask);
        root.addChild(new WalkTask());
        BehaviorTree bTree = new BehaviorTree<ISGManActorD3>(root,new GiantActorD3());

        //3
        attackTask.setCurrentStatus(Status.FAILED);
		System.out.println("----step1----");
        bTree.step();
		System.out.println("----step2----");
        bTree.step();
        

		System.out.println("----end----");
	}
/**
 * 第1个任务失败，第2个任务running时
 * (kof连招树可以用这种方式，使task知道当前处于哪个节点,
 *  sashaknight目前不是这种方式，而是用doComboAttack(auto)配合SGAttackTreeNode来做连击, 这是永远只出第1组连击的简单方案，在没有随机选择连招的简单情况下可行，
 *  如果复杂点的情况(根据距离随机选择连招)，可以用LeafTask+SGAttackTreeNode的方式来处理，应该更好，当有task是running时，如果被敌人打断连招，要btree.reset重新从root开始,
 *  不过这样好像非常复杂，感觉战术型的才用得上，比如多远时，跳起从空中攻击)
----step1----
AttackTask FAILED delta:0.016666668
FindPathFollowTask RUNNING delta:0.016666668
----step2----
FindPathFollowTask RUNNING delta:0.016666668
----end----
 */
	public static void testBtree3() {
		//1
		GdxAI.getTimepiece().update(1f/60f);
		
		//2
        Selector<ISGManActorD3> root = new Selector<ISGManActorD3>();
        AttackTask attackTask=new AttackTask();
        root.addChild(attackTask);
        FindPathFollowTask findPathFollowTask=new FindPathFollowTask();
        root.addChild(findPathFollowTask);
        root.addChild(new WalkTask());
        BehaviorTree bTree = new BehaviorTree<ISGManActorD3>(root,new GiantActorD3());

        //3
        attackTask.setCurrentStatus(Status.FAILED);
        findPathFollowTask.setCurrentStatus(Status.RUNNING);
		System.out.println("----step1----");
        bTree.step();
		System.out.println("----step2----");
        bTree.step();
        

		System.out.println("----end----");
	}
}
