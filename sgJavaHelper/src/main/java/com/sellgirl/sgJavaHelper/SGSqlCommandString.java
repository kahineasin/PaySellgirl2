package com.sellgirl.sgJavaHelper;

import java.util.ArrayList;

/**
 * 由于发现mysql8.0.23里stmt.execute(sql)的sql参数不支持分号分隔的多命令,只能stmt.addBatch(sql1)之后stmt.executeBatch()
 * 但又想减少以前代码的影响,所以有此类
 * @author Administrator
 *
 */
public class SGSqlCommandString extends ArrayList<String>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -198723182700951417L;
	public SGSqlCommandString(String... s) {
		for(String i :s) {
			this.add(i);
		}
	}
	@Override
	public String toString() {
		return String.join(";", this);
	}
//	public void each(Consumer<String> action) {
//		for(int i=0;i<this.size();i++) {
//			action.accept(this.get(i));
//		}
//	}
}
