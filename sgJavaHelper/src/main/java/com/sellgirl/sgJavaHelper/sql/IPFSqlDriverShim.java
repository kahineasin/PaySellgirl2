package com.sellgirl.sgJavaHelper.sql;

import java.sql.Driver;

/**
 * 为了解决动态从jar包加载sql驱动时,不套1层就不能加载成功的问题.
 * @author 1011712002
 *
 */
public interface IPFSqlDriverShim  {
	String getDriverName();
	boolean isMatch(Driver other);
}
