package org.sgGameHelper;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.sellgirl.sgGameHelper.gamepad.XBoxKey;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public class UncheckVector  {
	public static void main(String[] args) {
//		testIntRange();
//		testRandom();
		testCrossProduct();
	}
	public static void testCrossProduct() {
		Vector3 direction=new Vector3(0,0,-1);
		Vector3 up=new Vector3(0,1,0);
		Vector3 right=direction.crs(up);
		SGDataHelper.getLog().print(""+right.x+" "+right.y+" "+right.z);
	}

	public static void testRandom() {
		int curIdx=0;
		int idx=0;
		ArrayList<String> song=new ArrayList<String>();
		song.add("aa");
        int random= MathUtils.random(0,song.size()-2);//error:n must be positive
        idx=random>=curIdx?(random+1):random;
        String currentActor=song.get(idx);
	}
}
