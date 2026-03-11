package org.sgGameHelper.btree.model;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.Task.Status;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import com.badlogic.gdx.ai.utils.random.ConstantIntegerDistribution;
import com.badlogic.gdx.ai.utils.random.IntegerDistribution;
//import com.mygdx.game.share.ISGManActorD3;
//import com.mygdx.game.share.SGGameHelper;

public class WalkTask extends LeafTask<ISGManActorD3> {
    private final String TAG="WalkTask";

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
//        ISGManActorD3 dog = getObject();
////        for (int i = 0; i < t; i++)
////        {
////            SGGameHelper.walkD3(dog,walk[0],walk[1]);
////            //break;
////        }
////        return Status.SUCCEEDED;
//        i++;
//        SGGameHelper.walkD3(dog,walk[0],walk[1]);
//        return i < 3 ? Status.RUNNING : Status.SUCCEEDED;
    }

    @Override
    protected Task<ISGManActorD3> copyTo (Task<ISGManActorD3> task) {
        WalkTask bark = (WalkTask)task;
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