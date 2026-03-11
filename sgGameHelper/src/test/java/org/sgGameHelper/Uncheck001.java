package org.sgGameHelper;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.sellgirl.sgGameHelper.gamepad.XBoxKey;

public class Uncheck001  {
	public static void main(String[] args) {
//		testIntRange();
		testRandom();
	}
	public static void testIntRange() {
//		System.out.println(""+Long.MAX_VALUE);
		System.out.println(""+XBoxKey.stick2Right.ordinal());
//		long mask=1<<29;
//		for(long i=1;i<=40;i++) {
//			mask=1l<<i;
//			System.out.println(""+mask+"   "+i);	
//		}
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
