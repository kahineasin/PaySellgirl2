package com.sellgirl.sgGameHelper;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

/**
 * 通过4元数获得旋转后的up right的方位
 * 其实原理就是把标准的up按quaternion做旋转，得到的结果就是了
 * 
 * 注意，如果Quaternion是从缩放过的Matrix4获得的，那计算值就不正确，可以把m4的缩放还原再调用此方法
 */
public class SGQuaternionHelper {

    // 临时变量，避免频繁创建新对象（可选，但有利于性能）
    private static final Vector3 tmpVec = new Vector3();

    /**
     * 从四元数计算前向向量 (Forward, -Z)
     * @param rotation 旋转四元数
     * @param out 计算结果存储在这个Vector3中
     * @return 前向向量 (out)
     */
    public static Vector3 getForwardFromQuaternion(Quaternion rotation, Vector3 out) {
        // 1. 获取未旋转时的前向向量（假设是-Z轴）
        out.set(Vector3.Z).scl(-1); // 或者直接 out.set(0, 0, -1)
        // 2. 应用四元数旋转
        rotation.transform(out);
        return out;
    }

    /**
     * 从四元数计算右向量 (Right, +X)
     * @param rotation 旋转四元数
     * @param out 计算结果存储在这个Vector3中
     * @return 右向量 (out)
     */
    public static Vector3 getRightFromQuaternion(Quaternion rotation, Vector3 out) {
        // 1. 获取未旋转时的右向量（假设是+X轴）
        out.set(Vector3.X);
        // 2. 应用四元数旋转
        rotation.transform(out);
        return out;
    }

    /**
     * 从四元数计算上向量 (Up, +Y)
     * @param rotation 旋转四元数
     * @param out 计算结果存储在这个Vector3中
     * @return 上向量 (out)
     */
    public static Vector3 getUpFromQuaternion(Quaternion rotation, Vector3 out) {
        // 1. 获取未旋转时的上向量（假设是+Y轴）
        out.set(Vector3.Y);
        // 2. 应用四元数旋转
        rotation.transform(out);
        return out;
    }

    // 使用示例：
    public static void exampleUsage() {
        Quaternion myRotation = new Quaternion();
        // ... 通过某种方式设置 myRotation 的值 ...

        printQuaternion(myRotation);
        
//        Vector3 forward = new Vector3();
//        Vector3 right = new Vector3();
//        Vector3 up = new Vector3();
//        getForwardFromQuaternion(myRotation, forward);
//        getRightFromQuaternion(myRotation, right);
//        getUpFromQuaternion(myRotation, up);
//
//        System.out.println("Forward: " + forward);
//        System.out.println("Right: " + right);
//        System.out.println("Up: " + up);
//        System.out.println("x: " + myRotation.x);
//        System.out.println("y: " + myRotation.y);
//        System.out.println("z: " + myRotation.z);
//        System.out.println("w: " + myRotation.w);
    }
    public static void printQuaternion(Quaternion myRotation) {
        getForwardFromQuaternion(myRotation, tmpVec);

        System.out.println("Forward: " + tmpVec);
        getRightFromQuaternion(myRotation, tmpVec);
        System.out.println("Right: " + tmpVec);
        getUpFromQuaternion(myRotation, tmpVec);
        System.out.println("Up: " + tmpVec);
        System.out.println("x: " + myRotation.x);
        System.out.println("y: " + myRotation.y);
        System.out.println("z: " + myRotation.z);
        System.out.println("w: " + myRotation.w);
    }
}