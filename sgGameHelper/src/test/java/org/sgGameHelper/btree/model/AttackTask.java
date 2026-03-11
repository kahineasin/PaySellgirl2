package org.sgGameHelper.btree.model;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.Task.Status;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import com.badlogic.gdx.ai.utils.random.ConstantIntegerDistribution;
import com.badlogic.gdx.ai.utils.random.IntegerDistribution;
//import com.mygdx.game.sasha.SashaState;
////import com.mygdx.game.sasha.bulletD3.actor.GiantActorAiD3;
//import com.mygdx.game.sasha.bulletD3.actor.GiantActorD3;
//import com.mygdx.game.share.ISGManActorD3;

/**
 * giant attack
 */
public class AttackTask extends LeafTask<ISGManActorD3> {
    private final String TAG="AttackTask";

    @TaskAttribute
    public IntegerDistribution times = ConstantIntegerDistribution.ONE;

    private int t;

    private int i = 0;
    @Override
    public void start () {
        super.start();
        i = 0;
//        t = times.nextInt();
        walk[0]=(float)(Math.random()*2f-1f);
        walk[1]=(float)(Math.random()*2f-1f);
    }

    float[] walk=new float[2];
    private Status currentStatus=Status.SUCCEEDED;

	@Override
    public Status execute () {
    	System.out.println(TAG+" "+currentStatus+" delta:"+GdxAI.getTimepiece().getDeltaTime());
    	return currentStatus;
//    	return Status.SUCCEEDED;
//        GiantActorD3 man = (GiantActorD3)getObject();
//        ISGManActorD3 enemy = man.getCouple();
////        ISGManActorD3 enemy = man.attackTarget;
//////        for (int i = 0; i < t; i++)
//////        {
//////            SGGameHelper.walkD3(dog,walk[0],walk[1]);
//////            //break;
//////        }
//////        return Status.SUCCEEDED;
////        i++;
////        SGGameHelper.walkD3(dog,walk[0],walk[1]);
//
//        boolean isNoXChange = true;
//        boolean isNoYChange = true;
//        boolean r2=true;
//
//        if (null != enemy&&0<enemy.getLife()) {
//            ISGManActorD3 enemy1=(ISGManActorD3)enemy;
//            float[] pos=man.getPos();
//            float[] enemyPos=enemy1.getPos();
//
//            //float distanceD3 =((Double) Math.sqrt( Math.pow(enemyPos[0] - pos[0],2)+(Math.pow(enemyPos[2] - pos[2],2)))).floatValue();
//            float distanceBodyZ = Math.abs( enemyPos[2] - pos[2])-(enemy1.getZWidth()/2+man.getZWidth()/2);
//            if(0>distanceBodyZ){distanceBodyZ=0;}
//
//            float distanceBodyX = Math.abs( enemyPos[0] - pos[0])-(enemy1.getWidth()/2+man.getWidth()/2);
//            if(0>distanceBodyX){distanceBodyX=0;}
//
//            if(enemy instanceof ISGManActorD3&&((ISGManActorD3)enemy).isCaught()) {
//////				if(SGGameHelper.followActorD3(this,enemy1,1 )) {
//////					isNoXChange=false;
//////				}
////
////                SGGameHelper.FollowState tmp=SGGameHelper.followActorD3Check(this,pos,enemyPos,distanceBodyX,distanceBodyZ,1 );
////                if(SGGameHelper.FollowState.NEAR==tmp) {
////                    //isNoXChange=false;
////                }else if(SGGameHelper.FollowState.INACCESSIBLE ==tmp){
//////					r=-2;
//////					r=tmp;
////                    r2=false;
////                }else if(WALK==tmp){
////
////                    isNoXChange=false;
////                }
//                return Status.FAILED;
//            }else{
//                if (man.ultimateSkill1Pow<=man.getUltimateSkillCount()&&0>=distanceBodyZ&&1f > distanceBodyX) {//1<distance
//                    if(enemyPos[0]!=pos[0]){man.setFaceRight(enemyPos[0]>pos[0]);}
//                    man.doUltimateSkill1();
//                    isNoXChange = false;
//                    return Status.SUCCEEDED;
//                }
//                else if(0.0f>=distanceBodyZ&&1f>=distanceBodyX){
//                    //有一点z距离, 应该可以攻击的
//                    if(enemyPos[0]!=pos[0]){man.setFaceRight(enemyPos[0]>pos[0]);}
//                    man.setState(SashaState.SWORD1);
//                    return Status.SUCCEEDED;
//                }
//                else if(0<distanceBodyZ
//                        ||1f<distanceBodyX
//                    //||(1f<distanceBodyX&& 1>this.ultimateSkillCount)
//                ){
//////					float xAxis = distanceBodyX > 1 ? 10 : distanceBodyX*10;// 5;
//////					float distanceZ =  enemyPos[2]-pos[2];
//////					float zAxis = distanceZ > 1 ? 10 : (distanceZ<-1?-10:distanceZ*10);
////
////                    if(SGGameHelper.isCanWalk(this,enemyPos[0] - pos[0],enemyPos[2] - pos[2])){
////
//////						walkD3(xAxis*(0>enemyPos[0]-pos[0]?-1f:1f),zAxis);
////                        SGGameHelper.walkToActor(this,pos,enemyPos,distanceBodyX);
////                        isNoXChange = false;
////                    }else{
////                        r2=false;
////                    }
//                    return Status.FAILED;
//                }
////                else  if(0>=distanceBodyZ && 1f >= distanceBodyX){
////                    if(enemyPos[0]!=pos[0]){man.setFaceRight(enemyPos[0]>pos[0]);}
////                    man.setState(SashaState.SWORD1);
////                    return Status.SUCCEEDED;
////                }
//            }
//
//        }
////        if (isNoXChange && isNoYChange) {
////            man.stop();
////        }
//        return Status.FAILED;
//
////        return i < 3 ? Status.RUNNING : Status.SUCCEEDED;
    }

    @Override
    protected Task<ISGManActorD3> copyTo (Task<ISGManActorD3> task) {
        AttackTask bark = (AttackTask)task;
        bark.times = times;

        return task;
    }

    @Override
    public void reset() {
        times = ConstantIntegerDistribution.ONE;
        t = 0;
        i = 0;
        super.reset();
    }

    public Status getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(Status currentStatus) {
		this.currentStatus = currentStatus;
	}

}