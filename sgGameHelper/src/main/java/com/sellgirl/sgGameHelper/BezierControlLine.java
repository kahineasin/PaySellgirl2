package com.sellgirl.sgGameHelper;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.sellgirl.sgGameHelper.list.ISGList;

/**
 * 曲线控制线，类似PhotoShop钢笔工具的曲线控制线
 * 适用于Bezier和SPline等曲线
 */
public class BezierControlLine {

    public Vector2 left;
    public Vector2 middle;
    public Vector2 right;
    public void move(float x,float y){

        middle.x+=x;
        middle.y+=y;
        if(null!=left){

            left.x+=x;
            left.y+=y;
        }
        if(null!=right){

            right.x+=x;
            right.y+=y;
        }
    }
    public void moveTo(float x,float y){
        move(x-middle.x,y-middle.y);
    }
    
    public static ArrayList<Vector2> lineToPoint(Iterable<BezierControlLine> bezierControls){

        ArrayList<Vector2> r=new ArrayList<>();
        int idx=0;
        for(BezierControlLine i:bezierControls){
//            if(0<idx){
//                sb.append(",");
//            }

            if(null!=i.left){
//                sb.append("("+PtM(i.left.x)+","+PtMY(i.left.y)+")");
                r.add(new Vector2(i.left.x,i.left.y));
            }else{
//                sb.append("("+PtM(i.middle.x)+","+PtMY(i.middle.y)+")");
                r.add(new Vector2(i.middle.x,i.middle.y));
            }
//            sb.append(",");
//            sb.append("("+PtM(i.middle.x)+","+PtMY(i.middle.y)+")");
            r.add(new Vector2(i.middle.x,i.middle.y));
//            sb.append(",");
            if(null!=i.right){
//                sb.append("("+PtM(i.right.x)+","+PtMY(i.right.y)+")");
                r.add(new Vector2(i.right.x,i.right.y));
            }else{
//                sb.append("("+PtM(i.middle.x)+","+PtMY(i.middle.y)+")");
                r.add(new Vector2(i.middle.x,i.middle.y));
            }

            idx++;
        }
        return r;
    }
}
