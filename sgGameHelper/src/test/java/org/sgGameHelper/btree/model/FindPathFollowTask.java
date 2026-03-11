package org.sgGameHelper.btree.model;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.Task.Status;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.behaviors.Jump;
import com.badlogic.gdx.ai.steer.limiters.LinearLimiter;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.ai.utils.random.ConstantIntegerDistribution;
import com.badlogic.gdx.ai.utils.random.IntegerDistribution;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
//import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Array;
//import com.mygdx.game.sasha.GameSetting;
//import com.mygdx.game.sasha.SashaState;
//import com.mygdx.game.sasha.bulletD3.actor.GiantActorD3;
//import com.mygdx.game.sasha.bulletD3.ai.pfa.FlatTiledNode;
//import com.mygdx.game.sasha.bulletD3.ai.pfa.INavMesh;
//import com.mygdx.game.sasha.bulletD3.ai.pfa.NavMesh;
//import com.mygdx.game.sasha.bulletD3.ai.pfa.TiledNode;
//import com.mygdx.game.sasha.bulletD3.ai.pfa.TiledSmoothableGraphPath2;
//import com.mygdx.game.share.ISGManActorD3;
//import com.mygdx.game.share.SGGameHelper;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

//import static com.mygdx.game.sasha.bulletD3.GameScreenD3.GRAVITY_COMPONENT_HANDLER;
//import static com.mygdx.game.share.SGGameHelper.FollowState.WALK;

/**
 * ManActor共用
 */
public class FindPathFollowTask<T extends ISGManActorD3> extends LeafTask<T> {

    private final String TAG="FindPathFollowTask";
    @TaskAttribute
    public IntegerDistribution times = ConstantIntegerDistribution.ONE;
//    private  INavMesh navMesh=null;
    private int t;

    private int i = 0;
//    public boolean followPath=true;
    private static final SteeringAcceleration<Vector3> steeringOutput = new SteeringAcceleration<Vector3>(new Vector3());
    @Override
    public void start () {
        super.start();
        i = 0;
//        t = times.nextInt();
        walk[0]=(float)(Math.random()*2f-1f);
        walk[1]=(float)(Math.random()*2f-1f);

//        //旧版本每帧都new了一次
//        if(null==wayPoints) {
//            wayPoints = new Array<>();//发现每次execute之前都会start，所以wayPoint当前会清空，不能用于判断上次的状态--benjamin 20241028
//        }
    }

    float[] walk=new float[2];
//    private boolean hasWayPoint(){
//        return 1<wayPoints.size;
//    }
    private boolean canFollow=false;
    private Status currentStatus=Status.SUCCEEDED;
    @Override
    public Status execute () {
    	System.out.println(TAG+" "+currentStatus+" delta:"+GdxAI.getTimepiece().getDeltaTime());
    	return currentStatus;

//        return Status.SUCCEEDED;
//        final ISGManActorD3 man = (ISGManActorD3)getObject();
//
//        actorFollowEnemy(man,man.getCouple());
//
//        //canFollow=null!=wayPoints&&0<wayPoints.size;
//
////        //canFollow=true;
////        if(canFollow=true){
////
////        }
//
//        if (man.getFollowPathSB() != null) {
//            if(canFollow){
//                //canFollow是寻路的时候设置的，为了防止Follow行为报错，这里要再验证
//                canFollow=man.getFollowPathSB().isEnabled()
//                        &&null!=man.getFollowPathSB().getPath()//sasha发气功和巨人攻击同帧时，下一句null报错--benjamin 20240920
//                ;
//                if(canFollow&&LinePath.class==man.getFollowPathSB().getPath().getClass()){
//                    // Cannot read field "cumulativeLength" because "nearestSegment" is null(LinePath.calculateDistance()
//                    Array<LinePath.Segment> segments=((LinePath)man.getFollowPathSB().getPath()).getSegments();
//                    canFollow=null!=segments&&0<segments.size;
//                }
//            }
//            // Calculate steering acceleration
//            boolean steerSuccess=false;
//            try {
//                if(//man.getFollowPathSB().isEnabled()
//                     //   &&null!=man.getFollowPathSB().getPath()//sasha发气功和巨人攻击同帧时，下一句null报错--benjamin 20240920
//                //        &&
//                canFollow//wayPoint空时，应该会导致LinePath.calculateDistance()由于nearestSegment为null而报错--benjamin 20241028
//                ) {
//    //                if(0==wayPoints.size){
//    //                    String aa="aa";
//    //                }
//                        man.getFollowPathSB().calculateSteering(steeringOutput);
//                    steerSuccess=applySteering(man,steeringOutput, GdxAI.getTimepiece().getDeltaTime());
//                }
//            }catch (Throwable e){
//                SGDataHelper.getLog().writeException(e,TAG);
//            }
//
//            /*
//             * Here you might want to add a motor control layer filtering steering accelerations.
//             *
//             * For instance, a car in a driving game has physical constraints on its movement: it cannot turn while stationary; the
//             * faster it moves, the slower it can turn (without going into a skid); it can brake much more quickly than it can
//             * accelerate; and it only moves in the direction it is facing (ignoring power slides).
//             */
//
//            // Apply steering acceleration
//            if(
//                    steerSuccess
//            ){
//                return Status.SUCCEEDED;
//            }else{
////                return Status.FAILED;
//                return Status.SUCCEEDED;
//                //return FollowTask.moveToCouple(man);
//            }
//
//        }
//        return Status.FAILED;
    }

    @Override
    protected Task<T> copyTo (Task<T> task) {
        FindPathFollowTask bark = (FindPathFollowTask)task;
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

    private final Vector3 tmpVector3 = new Vector3();
    private final Vector2 tmpVector2 = new Vector2();
//    protected boolean applySteering (ISGManActorD3 man,SteeringAcceleration<Vector3> steering, float deltaTime) {
//        boolean anyAccelerations = false;
//
//        //printSteeringAcceleration(steeringOutput);
//
//        // Update position and linear velocity
//        if (!steeringOutput.linear.isZero()
//
//        ) {
//
//
//            tmpVector3.set(man.getLinearVelocity());
//            tmpVector2.set(tmpVector3.x,tmpVector3.z);
////                tmpVector3.x=0>steeringOutput.linear.x*tmpVector3.x?0:tmpVector3.x+steeringOutput.linear.x;
////                tmpVector3.z=0>steeringOutput.linear.z*tmpVector3.z?0:tmpVector3.z+steeringOutput.linear.z;
////                tmpVector3.add(steeringOutput.linear);
//
////            tmpVector3.x+=steeringOutput.linear.x*deltaTime;
////            tmpVector3.z+=steeringOutput.linear.z*deltaTime;
////            tmpVector3.limit(this.getMaxLinearSpeed());
//
//            tmpVector2.x+=steeringOutput.linear.x*deltaTime;
//            tmpVector2.y+=steeringOutput.linear.z*deltaTime;
//            tmpVector2.limit(man.getMaxLinearSpeed());//注意这个速度不限制跳的速度
//
//            tmpVector3.x=tmpVector2.x;
//            //保证转向不会转成反向
////            tmpVector3.x=(0>(this.getFollowPathSB().getInternalTargetPosition().x-this.getPosition().x)*tmpVector2.x)?0:tmpVector2.x;//这样做, 接近后一样会抖动, 晚些再处理 --benjamin todo
//            tmpVector3.z=tmpVector2.y;
//
////            //保证转向不会转成反向
////            if(0<=(this.getFollowPathSB().getInternalTargetPosition().x-this.getPosition().x)*tmpVector3.x){
////                SGGameHelper.walkState(GiantActorD3.this, tmpVector3);
////                body.setLinearVelocity(tmpVector3);
////            }
//            SGGameHelper.walkState(man, tmpVector3);
////            body.setLinearVelocity(tmpVector3);
//
//            //这样会有个bug,giant刚落地时, y向下的, path向前,所以targetY是0, 所以streer的y是向上的, 就使得刚落地时又跳起
//            //这个问题主要是由于转向类没有地面的判断, 到地时其实不用转向Y正,地面产生的作用已经可以使velocityY=0;
//            //解决方案
//            //方案1. 一种解决方法是继承FollowPath类, 当在地面时, 不产生回y的力
//            //方案2. 比较path的nextPoint的y, 如果更大, 才跳
////            if(){
////
////            }
//            if (0.1 < steeringOutput.linear.y) {
////                this.jump();
////                    GiantActorD3.this.jumping = true;
//                if(//getFollowPathSB()==getSteeringBehavior()
//                        //&&
//                        this.getFollowPathSB().getInternalTargetPosition().y>=man.getPosition().y+0.1f//解决FollowPath落地时反复跳的问题.
//                ){
////                    if(this.getFollowPathSB().getInternalTargetPosition().y+0.2f>this.getPosition().y&&jumping){
////                        //测试FollowPath落地时反复跳的问题.
////                        //因为弹性, this.getPosition().y 是可能小于 Path 在地面时的高度的
////                        int aa=1;
////                    }
//                    man.jump();
//                }
//            }
//            return true;
//        }else if(SashaState.WALK==man.getState()){
//            man.setState(SashaState.STAND);
//            man.getFollowPathSB().setEnabled(false);
//            return true;
//        }
//        man.getFollowPathSB().setEnabled(false);
//        return false;
////        // Update orientation and angular velocity
////        if (isIndependentFacing()) {
////            if (steeringOutput.angular != 0) {
////                // this method internally scales the torque by deltaTime
////                body.applyTorque(tmpVector3.set(0, steeringOutput.angular, 0));
////                anyAccelerations = true;
////            }
////        }
////        else {
////            // If we haven't got any velocity, then we can do nothing.
////            Vector3 linVel = getLinearVelocity();
////            if (!linVel.isZero(getZeroLinearSpeedThreshold())) {
////                //
////                // TODO: Commented out!!!
////                // Looks like the code below creates troubles in combination with the applyCentralForce above
////                // Maybe we should be more consistent by only applying forces or setting velocities.
////                //
//////				float newOrientation = vectorToAngle(linVel);
//////				Vector3 angVel = body.getAngularVelocity();
//////				angVel.y = (newOrientation - oldOrientation) % MathUtils.PI2;
//////				if (angVel.y > MathUtils.PI) angVel.y -= MathUtils.PI2;
//////				angVel.y /= deltaTime;
//////				body.setAngularVelocity(angVel);
//////				anyAccelerations = true;
//////				oldOrientation = newOrientation;
////            }
////        }
////        if (anyAccelerations) {
////            body.activate();
////
////////            //例子的这种转向方式,和纸片人的防旋转模式(非左即右方式)有冲突
////////            // TODO:
////////            // Looks like truncating speeds here after applying forces doesn't work as expected.
////////            // We should likely cap speeds form inside an InternalTickCallback, see
////////            // http://www.bulletphysics.org/mediawiki-1.5.8/index.php/Simulation_Tick_Callbacks
////////
////////            // Cap the linear speed
////            Vector3 velocity = body.getLinearVelocity();
////            float currentSpeedSquare = velocity.len2();
////            float maxLinearSpeed = getMaxLinearSpeed();
////            if (currentSpeedSquare > maxLinearSpeed * maxLinearSpeed) {
////                //SGGameHelper.walkState(GiantActorD3.this,velocity.scl(maxLinearSpeed / (float)Math.sqrt(currentSpeedSquare)));
////                body.setLinearVelocity(velocity.scl(maxLinearSpeed / (float)Math.sqrt(currentSpeedSquare)));
////            }
//////
//////            // Cap the angular speed
//////            Vector3 angVelocity = body.getAngularVelocity();
//////            if (angVelocity.y > getMaxAngularSpeed()) {
//////                angVelocity.y = getMaxAngularSpeed();
//////                body.setAngularVelocity(angVelocity);
//////            }
////
////////            //纸片人转向方式
//////            tmpVector3.set(steeringOutput.linear);
//////            float needVelocityLen=tmpVector3.len();
//////            if (needVelocityLen > getMaxLinearSpeed() ) {
//////                //SGGameHelper.walkState(GiantActorD3.this,velocity.scl(maxLinearSpeed / (float)Math.sqrt(currentSpeedSquare)));
//////                tmpVector3.scl(getMaxLinearSpeed() / needVelocityLen);
//////            }
//////            SGGameHelper.walkState(GiantActorD3.this,tmpVector3);
//////            if(0<tmpVector3.y){
////////                this.jump();
//////                GiantActorD3.this.jumping=true;
//////            }
////            body.setLinearVelocity(tmpVector3);
////
////        }
//    }
//    private FollowPath<Vector3, LinePath.LinePathParam> followPathSB=null;
//    public FollowPath<Vector3, LinePath.LinePathParam>  getFollowPathSB() {
//        return followPathSB;
//    }
//
//    public void setFollowPathSB(FollowPath followPathSB) {
//        this.followPathSB = followPathSB;
//    }
//
//    protected boolean pathSuccess=false;
//    protected boolean smooth = true;
////    private static final Vector3 tmpVector3 = new Vector3();
//    private  final Vector3 oldTargetPoint = new Vector3();
//    protected Array<Vector3> wayPoints;
//    /**
//     *
//     * @param playerActor
//     * @param tmp enemy
//     */
//    protected void actorFollowEnemy(ISGManActorD3 playerActor,ISGManActorD3 tmp){
//
//
//        TiledNode l=navMesh.getWorldMap().getNodeByActor(playerActor);
//        TiledNode r=navMesh.getWorldMap().getNodeByActor(tmp);
//
////        HierarchicalTiledNode l=navMesh.worldMap.getNodeByActor(playerActor);
////        HierarchicalTiledNode r=navMesh.worldMap.getNodeByActor(tmp);
//
//
//        //走到人的左边或右边
//        float[] enemyPos=tmp.getPos();
//        boolean onRight=enemyPos[0]>playerActor.getPos()[0];
//        Vector3 targetPoint=tmpVector3.set(
//                enemyPos[0]
//                        +(((tmp.getWidth()*0.5f)+(playerActor.getWidth()*0.5f)+0.5f)
//                        *(onRight?-1:1)),
//                enemyPos[1],enemyPos[2]);
//        //if(null==r) {//找enemy左右的点，应该优先的
//        TiledNode tmpNode=navMesh.getWorldMap().getNode(r.x+(onRight?-1:1),r.y,r.z);
//        //HierarchicalTiledNode tmpNode=navMesh.worldMap.getNode(0,(int) targetPoint.x,r.y, (int) targetPoint.z);
//        if (null != tmpNode) {
//            r = tmpNode;
//        }
//        //}
//        boolean needUpatePath=false;
//        if(null!=r){
//            if(null==playerActor.getFollowPathSB()
//                    ||0>=((LinePath)playerActor.getFollowPathSB().getPath()).getSegments().size
//                    ||!playerActor.getFollowPathSB().isEnabled()
//            ){
//                needUpatePath=true;
//            }else{
//                //            Vector3 pos=r.getPos();
//                Vector3 pos=targetPoint;
//                Vector3 oldTargetPos= playerActor.getFollowPathSB().getPath().getEndPoint();
//                //                                if(null==targetPos){
//                //                                    targetPos=new Vector3(pos);
//                //                                    return true;
//                //                                }
//                if(
//                    //                    (int)oldTargetPos.x!=(int)pos.x
//                    ////                    ||(int)oldTargetPos.y!=(int)(pos.y+(playerActor.getTall()*0.5f))
//                    //                    ||(int)oldTargetPos.y!=(int)(pos.y)
//                    //                    ||(int)oldTargetPos.z!=(int)pos.z
//                        !pos.epsilonEquals(oldTargetPos,0.1f)
//                ){
//                    //targetPos.set(pos);
//                    needUpatePath= true;
//                }else{
//                    needUpatePath= false;
//                }
//            }
//        }
//        if(null!=l&&null!=r
////                                    &&playerActor.updateTargetPos(r.getPos())
//                &&needUpatePath
//        ){
//            //如果每1帧都更新路径, 会存在问题, 即使目标位置没有变, 可能path的第2个点会1帧在左, 1帧在右, 只因为own的位置变了
//            //navMesh.getGraphPath().clear();//path是out的,不用clear. 不对, demo中也有这句, 不clear的话, 旧路径不会清理干净的
//            navMesh.getGraphPath2().clear();
//
//            //这1步，其实框架没使用到xy的，完全是用cost来寻路
////            pathSuccess=navMesh.getPathFinder().searchNodePath(l,r , navMesh.getHeuristic(), navMesh.getGraphPath());
//            pathSuccess=navMesh.getPathFinder().searchConnectionPath(l,r , navMesh.getHeuristic(), navMesh.getGraphPath2());
////            path.add();//endNode放这不合适，因为Node的x是int的
//            if(pathSuccess){
//                //path.setPosOffset(0,playerActor.getTall()*0.5f,0);
//                if (smooth) {
//                    //这1步，使用了path.getNodePosition, 所以应该可以在这之前做身高的平移
//                    //navMesh.getPathSmoother().smoothPath(navMesh.getGraphPath());
////                    navMesh.getPathSmoother().smoothPath(navMesh.getGraphPath2());
//                    navMesh.getPathSmoother2().smoothPath(navMesh.getGraphPath2());
//
//                }
//
////                                    //LinePath方式不适合用来跟踪角色, 感觉也合适, 问题只是怎么把GraphPath转为LinePath, 尝试用wayPoint来过渡转换
//                wayPoints.clear();
//
////                //Node方式
////                for(Object wayNode:navMesh.getGraphPath()){
//////                                        wayPoints.add(wayNode.getPos());
//////                                        wayPoints.add(wayNode.getPos().cpy().add(0,playerActor2.getTall()*0.5f,0));
////                    wayPoints.add(((TiledNode)wayNode).getOffsetPos(0,playerActor.getTall()*0.5f,0));
//////                    wayPoints.add(wayNode.getPos());
////                }
//                //Connection方式
//                //boolean first=true;
//                if(0<navMesh.getGraphPath2().getCount()) {
//                    for (Object wayNode : navMesh.getGraphPath2()) {
////                    if(first){
////                        first=false;
////                        wayPoints.add(((TiledNode)(((Connection)navMesh.getGraphPath2().get(0)).getFromNode())).getOffsetPos(0,playerActor.getTall()*0.5f,0));
////                    }
////                    wayPoints.add(((TiledNode)(((Connection)wayNode).getToNode())).getOffsetPos(0,playerActor.getTall()*0.5f,0));
//
//                        //wayPoints.add(((TiledNode) (((Connection) navMesh.getGraphPath2().get(0)).getFromNode())).getOffsetPos(0, playerActor.getTall() * 0.5f, 0));
//                        wayPoints.add(((TiledNode)(((Connection)wayNode).getFromNode())).getOffsetPos(0,playerActor.getTall()*0.5f,0));
//                    }
//                    wayPoints.add(((TiledNode) (((Connection) navMesh.getGraphPath2().get(navMesh.getGraphPath2().getCount() - 1))
//                            .getToNode())).getOffsetPos(0, playerActor.getTall() * 0.5f, 0));
//                }
//
//                wayPoints.add(oldTargetPoint.set( targetPoint));
//                //这样好像没有用,跟不了
//                //linePath.createPath(wayPoints);
//                if(1<wayPoints.size){
//
//                    if(null==playerActor.getFollowPathSB()){
//                        //Array<Vector3> wayPoints =new Array<>();
//                        LinePath<Vector3> linePath = new LinePath<Vector3>(wayPoints, true);//false
//
//                        FollowPath followPathSB = new FollowPath<Vector3, LinePath.LinePathParam>(
//                                playerActor, linePath,
//                                //tmpactor.getTall()/2f
//                                0.5f// 0是跟到目标, 0.5 是超过目标0.5米, -0.5是在目标的0.5米前就停下的意思,应该是. 但实测这里填0就不行...
//                        ) //demo 0.5f todo
//                                // Setters below are only useful to arrive at the end of an open path
//                                .setArriveEnabled(true) //false
//                                .setTimeToTarget(0.1f) //
//                                .setArrivalTolerance(0.1f) //原值0.5
//                                .setDecelerationRadius(0.1f);  //3, 设置为3时, 角色速度非常大, 不知道哪FollowPath哪个计算有问题, 真是服了; 设置为0时较正常
//
//                        playerActor.setSteeringBehavior(followPathSB);
//                        playerActor.setFollowPathSB(followPathSB);
////                                            playerActor.setLinePath(linePath);
//                        canFollow=true;
//                    }else{
//                        if(!playerActor.getFollowPathSB().isEnabled()){
//                            playerActor.getFollowPathSB().setEnabled(true);
//                        }
//                        ((LinePath<Vector3>)playerActor.getFollowPathSB().getPath()).createPath(wayPoints);
////                                            playerActor.getLinePath().createPath(wayPoints);
//                        canFollow=true;
//                    }
//
//                }
//
//            }
////            else{
////                int aa=1;
////            }
//        }
//    }
//
//    public void setNavMesh(INavMesh navMesh){
//        this.navMesh=navMesh;
//    }

    public Status getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(Status currentStatus) {
		this.currentStatus = currentStatus;
	}
}